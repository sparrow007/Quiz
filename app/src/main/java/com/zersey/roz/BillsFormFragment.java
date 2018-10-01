package com.zersey.roz;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.zersey.roz.Data.TransactionDbHelper;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class BillsFormFragment extends DialogFragment {
	private int year_x, month_x, day_x;
	private List<ContactModel> contactList;
	private RecyclerView contactRecyclerView;
	private DatePickerDialog datePicker;
	private RecyclerView categoryRecyclerView;
	private List<String> categoryList;
	private ImageView imgFile;
	private TextView dateEditText;
	public static Button positive_Button;
	private TextView splitButton;
	private EditText amountEditText, titleEditText;
	private String users = "";
	private RecyclerView splitRecyclerView;
	private DialogSplitRecyclerViewAdapter dialogSplitAdapter;
	private GroupModel groupModel;
	private String code;
	private TransactionDbHelper mDbHelper;

	private List<Split_Contact_model> splitList;
	public static TextView splitNotes;
	public static GroupRecyclerAdapter adapter;
	private String amount;
	private CategoryAdapter categoryAdapter;
	private Uri Image_uri = null;
	private Context context;
	private ArrayList<Split_Contact_model> secondSplitList;

	@NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
		mDbHelper = TransactionDbHelper.getInstance(getContext());
		secondSplitList = new ArrayList<>();

		return super.onCreateDialog(savedInstanceState);
	}

	@Nullable @Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
		Bundle savedInstanceState) {
		context = inflater.getContext();
		View view = inflater.inflate(R.layout.content_main, container, false);

		contactList = new ArrayList<>();
		categoryList = new ArrayList<>();
		categoryList.add("Expense");
		categoryList.add("Income");
		categoryList.add("Group");

		categoryRecyclerView = view.findViewById(R.id.Category_Recycler_View);
		view = initView(view);

		splitButton = view.findViewById(R.id.Split_Dialog);
		splitButton.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				splitDialog();
			}
		});

		Calendar cal = Calendar.getInstance();
		year_x = cal.get(Calendar.YEAR);
		day_x = cal.get(Calendar.DAY_OF_MONTH);
		month_x = cal.get(Calendar.MONTH);

		imgFile = view.findViewById(R.id.Img_file);
		imgFile.setVisibility(View.GONE);

		dateEditText = view.findViewById(R.id.Date_Edit);
		dateEditText.setText(
			String.format(Locale.getDefault(), "%d-%d-%d", year_x, month_x + 1, day_x));

		titleEditText = view.findViewById(R.id.Title_Edit);
		amountEditText = view.findViewById(R.id.Amount_Edit);
		LinearLayout addMembersButton = view.findViewById(R.id.Add_Member_Layout);
		splitRecyclerView = new RecyclerView(context);
		SharedPreferences prefs = context.getSharedPreferences("login", MODE_PRIVATE);
		users += prefs.getString("userid", null);

		TextView submitButton = view.findViewById(R.id.Submit_Transaction_form);
		dateEditText.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				datePicker.show();
			}
		});

		addMembersButton.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				addMembers();
			}
		});

		DatePickerDialog.OnDateSetListener dateSetListener =
			new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
					dateEditText.setText(
						String.format(Locale.getDefault(), "%d-%d-%d", year, month + 1,
							dayOfMonth));
				}
			};
		datePicker =
			new DatePickerDialog(context, R.style.Theme_AppCompat_DayNight_Dialog, dateSetListener,
				cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
		datePicker.setCancelable(true);
		datePicker.setTitle("Select Date");
		ImageButton cameraButton = view.findViewById(R.id.Fab_Camera_Button);
		cameraButton.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View view) {
				selectImage();
			}
		});

		submitButton.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				submitBill();
				dismiss();
			}
		});

		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (getDialog().getWindow() != null) {
			getDialog().getWindow()
				.setBackgroundDrawable(
					new ColorDrawable(context.getResources().getColor(R.color.TransparentWhite)));
			getDialog().show();
			getDialog().getWindow()
				.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
					WindowManager.LayoutParams.MATCH_PARENT);
			getDialog().getWindow()
				.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
					| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		}
		return view;
	}

	public void selectImage() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setCancelable(true)
			.setTitle(Html.fromHtml("<font color='#3F51B5'>Choose an Image from</font>"))
			.setPositiveButton(Html.fromHtml("<font color='#3F51B5'>camera</font>"),
				new DialogInterface.OnClickListener() {
					@Override public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						startActivityForResult(intent, 2);
					}
				})
			.setNegativeButton(Html.fromHtml("<font color='#3F51B5'>Gallery</font>"),
				new DialogInterface.OnClickListener() {
					@Override public void onClick(DialogInterface dialog, int which) {
						Intent i = new Intent(Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

						startActivityForResult(i, 1);
					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public void splitDialog() {
		amount = amountEditText.getText().toString();
		if (TextUtils.isEmpty(amount)) {
			amount = "0";
		}
		Boolean check = true;
		if (!check) {
			groupSplit();
		} else if (check) {
			singleSplit();
		}

		if (TextUtils.isEmpty(amount)) {
			amount = "0";
		}
		LayoutInflater inflater = LayoutInflater.from(context);
		View dialogView = inflater.inflate(R.layout.split_dialog_layout, null);
		Spinner splitSpinner = dialogView.findViewById(R.id.Split_Spinner);
		splitRecyclerView = dialogView.findViewById(R.id.Dialog_RecyclerView);
		splitNotes = dialogView.findViewById(R.id.Dialog_Split_Notes);
		splitRecyclerView.setLayoutManager(new LinearLayoutManager(context));
		dialogSplitAdapter =
			new DialogSplitRecyclerViewAdapter(splitList, "", Integer.parseInt(amount));
		splitRecyclerView.setAdapter(dialogSplitAdapter);

		splitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (position == 2) {
					dialogSplitAdapter = new DialogSplitRecyclerViewAdapter(splitList, "ratio",
						Integer.parseInt(amount));
					splitRecyclerView.setAdapter(dialogSplitAdapter);
				}
				if (position == 1) {
					dialogSplitAdapter =
						new DialogSplitRecyclerViewAdapter(splitList, "", Integer.parseInt(amount));
					splitRecyclerView.setAdapter(dialogSplitAdapter);
				}
				if (position == 0) {
					dialogSplitAdapter = new DialogSplitRecyclerViewAdapter(splitList, "Equally",
						Integer.parseInt(amount));
					splitRecyclerView.setAdapter(dialogSplitAdapter);
				}
			}

			@Override public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		android.support.v7.app.AlertDialog.Builder alertDialogBuilder =
			new android.support.v7.app.AlertDialog.Builder(context);
		alertDialogBuilder.setView(dialogView);
		alertDialogBuilder.setCancelable(false);

		alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override public void onClick(DialogInterface dialog, int which) {
				splitList = dialogSplitAdapter.getList();
			}
		});
		final android.support.v7.app.AlertDialog dialog = alertDialogBuilder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override public void onShow(DialogInterface dialogInterface) {
				positive_Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
				positive_Button.setTextColor(getResources().getColor(R.color.colorPrimary));
			}
		});

		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

		if (getDialog().getWindow() != null) {
			getDialog().getWindow()
				.setBackgroundDrawable(
					new ColorDrawable(context.getResources().getColor(R.color.TransparentWhite)));
			getDialog().show();
			getDialog().getWindow()
				.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
					WindowManager.LayoutParams.MATCH_PARENT);
			getDialog().getWindow()
				.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
					| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		}
	}

	private void singleSplit() {
		int no_of_Person = contactList.size();
		String Specific_Amount;
		amount = amountEditText.getText().toString();
		if (TextUtils.isEmpty(amount)) {
			Specific_Amount = "0";
		} else {
			Specific_Amount = "" + Integer.parseInt(amount) / (contactList.size() + 1);
		}
		splitList = new ArrayList<>();
		secondSplitList = new ArrayList<>();
		ContactModel you = new ContactModel();
		you.setName("You");
		splitList.add(new Split_Contact_model(you, Specific_Amount));
		secondSplitList.add(new Split_Contact_model(you, amount));

		if (contactList.isEmpty()) {
			Toast.makeText(context, "Add members ", Toast.LENGTH_SHORT).show();
			return;
		}
		for (int i = 0; i < contactList.size(); i++) {

			if ((i == contactList.size() - 1) && (no_of_Person + 1) % 2 != 0) {
				Specific_Amount = Integer.parseInt(Specific_Amount) + 1 + "";
			}
			splitList.add(new Split_Contact_model(contactList.get(i), Specific_Amount));
			secondSplitList.add(new Split_Contact_model(contactList.get(i), "0"));
		}
	}

	private void groupSplit() {
		String[] names = groupModel.getUsers().split(",");
		List<ContactModel> userList = mDbHelper.getUserWithUserId(names);
		String Specific_Amount;
		amount = amountEditText.getText().toString();
		if (TextUtils.isEmpty(amount)) {
			Specific_Amount = "0";
		} else {
			Specific_Amount = "" + Integer.parseInt(amount) / (userList.size() + 1);
		}
		splitList = new ArrayList<>();

		ContactModel you = new ContactModel();
		you.setName("You");
		splitList.add(new Split_Contact_model(you, Specific_Amount));

		for (int i = 0; i < userList.size(); i++) {
			splitList.add(new Split_Contact_model(userList.get(i), Specific_Amount));
		}
	}

	private View initView(View view) {
		contactRecyclerView = view.findViewById(R.id.Add_Member_RecyclerView);
		contactRecyclerView.setLayoutManager(
			new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
		String updated_Type = "";
		if (TextUtils.isEmpty(updated_Type)) {
			categoryAdapter = new CategoryAdapter(context, categoryList, "");
		} else {
			categoryAdapter = new CategoryAdapter(context, categoryList, updated_Type);
		}
		categoryRecyclerView.setLayoutManager(
			new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
		categoryRecyclerView.setAdapter(categoryAdapter);
		return view;
	}

	public void addMembers() {
		Intent intent = new Intent(context, AddMembersActivity.class);
		startActivityForResult(intent, 4);
	}

	public void submitBill() {
		String amountText = amountEditText.getText().toString();
		ContactModel you = new ContactModel();
		you.setName("You");
		secondSplitList.add(new Split_Contact_model(you, "0"));
		for (int i = 0; i < contactList.size(); i++) {
			secondSplitList.add(new Split_Contact_model(contactList.get(i), "0"));
		}
		String titleText = titleEditText.getText().toString();
		String categoryText = categoryAdapter.getLastCategory();

		if (groupModel == null && TextUtils.isEmpty(categoryText)) {
			Toast.makeText(context, "Category can't be empty", Toast.LENGTH_SHORT).show();
			return;
		}

		if (day_x == 0) {
			dateEditText.setError("Date can't be empty");
			return;
		}
		if (Util.isEmpty(amountText)) {
			amountEditText.setError("amount can't be empty");
			return;
		}
		if (Util.isEmpty(titleText)) {
			titleEditText.setError("Title can't be empty");
			return;
		}

		BillModel billModel = new BillModel();

		String dateText = dateEditText.getText().toString();
		SharedPreferences prefs = context.getSharedPreferences("login", MODE_PRIVATE);

		if (groupModel != null) {
			groupSplit();
		} else {
			singleSplit();
		}

		StringBuilder totalAmounts = new StringBuilder();
		StringBuilder payerIds = new StringBuilder();
		StringBuilder amountsPaid = new StringBuilder();
		StringBuilder datesPaid = new StringBuilder();
		StringBuilder amountsDue = new StringBuilder();
		int i = 0;

		for (Split_Contact_model s : splitList) {
			totalAmounts.append(s.getSplit_Amount()).append(",");
			if (s.getContact_Name().getName().equalsIgnoreCase("you")) {
				payerIds.append(prefs.getString("userid", null)).append(",");
			} else {
				payerIds.append(s.getContact_Name().getUserId()).append(",");
			}
			amountsPaid.append(secondSplitList.get(i).getSplit_Amount()).append(",");

			double amountDuePerPerson =
				Double.parseDouble(s.getSplit_Amount()) - Double.parseDouble(
					secondSplitList.get(i).getSplit_Amount());
			amountsDue.append(Double.toString(amountDuePerPerson)).append(",");
			datesPaid.append(dateText).append(",");
			i++;
		}

		billModel.setType(categoryText);
		billModel.setTitle(titleText);
		billModel.setTotalAmount(totalAmounts.toString());
		billModel.setPaidAtDate(datesPaid.toString());
		billModel.setAmountPaid(amountsPaid.toString());
		billModel.setAmountDue(amountsDue.toString());
		billModel.setPayerId(payerIds.toString());
		billModel.setInvoiceId("");
		try {
			billModel.setUuid(Util.generateUuid(prefs.getString("userid", null)));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		if (groupModel != null) {
			billModel.setGroupId(groupModel.getGroupId());
			String str = groupModel.getGroupId() + "";
			Log.d("submitBill: ", str);
			billModel.setType("gt");
			long rowId = mDbHelper.createEntry(billModel);
			new ServerUtil(context).createEntry(billModel);
			billModel.setId(rowId);
		}

		if (groupModel == null) {
			GroupModel groupModel2 = new GroupModel();
			groupModel2.setGroupName(titleText);
			groupModel2.setTypeId(1);

			groupModel2.setUsers(users);
			long newRowId = mDbHelper.createGroup(groupModel2);
			groupModel2.setId(newRowId);
			new ServerUtil(context).createSingleGroup(groupModel2, billModel, null);
		}

		Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();

		if (groupModel != null) {
			Intent intent = new Intent();
			intent.putExtra("model", billModel);
			//set result
		}
	}

	@Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 2 && resultCode == RESULT_OK) {
			Uri uri = data.getData();
			imgFile.setVisibility(View.VISIBLE);
			imgFile.setImageURI(uri);
			Image_uri = uri;
		} else if (requestCode == 1 && resultCode == RESULT_OK) {
			Uri uri = data.getData();
			imgFile.setVisibility(View.VISIBLE);
			Image_uri = uri;
			imgFile.setVisibility(View.VISIBLE);
			imgFile.setImageURI(uri);
		} else if (resultCode == RESULT_OK && requestCode == 4) {
			contactRecyclerView.setVisibility(View.VISIBLE);
			//noinspection unchecked
			contactList.addAll((List<ContactModel>) data.getSerializableExtra("ADDED"));
			ContactRecyclerViewAdapter contactRecyclerViewAdapter =
				new ContactRecyclerViewAdapter(contactList);
			contactRecyclerView.setAdapter(contactRecyclerViewAdapter);
		}
	}
}
