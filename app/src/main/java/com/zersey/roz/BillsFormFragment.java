package com.zersey.roz;

import android.app.DatePickerDialog;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.zersey.roz.Data.TransactionDbHelper;
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
	private EditText amountEditText, titleEditText;
	private StringBuilder users = new StringBuilder();
	private RecyclerView splitRecyclerView;
	private DialogSplitRecyclerViewAdapter dialogSplitAdapter;
	private GroupModel groupModel;
	private String code;
	private TransactionDbHelper mDbHelper;

	private List<Split_Contact_model> splitList;
	public static TextView splitNotes;
	public static BillRecyclerAdapter adapter;
	private String amount;
	private CategoryAdapter categoryAdapter;
	private Uri Image_uri = null;
	private Context context;
	private ArrayList<Split_Contact_model> secondSplitList;
	private List<GroupModel> groupList;

	@Override public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDbHelper = TransactionDbHelper.getInstance(getContext());
		secondSplitList = new ArrayList<>();
		if (getArguments() != null) {
			groupModel = (GroupModel) getArguments().getSerializable("model");
		}
		groupList = mDbHelper.getGroups(0);
	}

	@Nullable @Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
		Bundle savedInstanceState) {
		context = inflater.getContext();
		View fragmentView = inflater.inflate(R.layout.fragment_bills_form, container, false);

		contactList = new ArrayList<>();
		categoryList = new ArrayList<>();
		categoryList.add("Expense");
		categoryList.add("Income");
		categoryList.add("Group");

		categoryRecyclerView = fragmentView.findViewById(R.id.Category_Recycler_View);
		fragmentView = initView(fragmentView);
		if (groupModel != null) {
			categoryRecyclerView.setVisibility(View.GONE);
			fragmentView.findViewById(R.id.Category_text_view).setVisibility(View.GONE);
		}
		TextView splitButton = fragmentView.findViewById(R.id.Split_Dialog);
		splitButton.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				splitDialog();
			}
		});

		Calendar cal = Calendar.getInstance();
		year_x = cal.get(Calendar.YEAR);
		day_x = cal.get(Calendar.DAY_OF_MONTH);
		month_x = cal.get(Calendar.MONTH);

		imgFile = fragmentView.findViewById(R.id.Img_file);
		imgFile.setVisibility(View.GONE);

		dateEditText = fragmentView.findViewById(R.id.Date_Edit);
		dateEditText.setText(
			String.format(Locale.getDefault(), "%d-%d-%d", year_x, month_x + 1, day_x));

		titleEditText = fragmentView.findViewById(R.id.Title_Edit);
		amountEditText = fragmentView.findViewById(R.id.Amount_Edit);
		splitRecyclerView = new RecyclerView(context);
		SharedPreferences prefs = context.getSharedPreferences("login", MODE_PRIVATE);
		users.append(prefs.getString("userid", null)).append(",");

		TextView submitButton = fragmentView.findViewById(R.id.Submit_Transaction_form);
		dateEditText.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				datePicker.show();
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
		ImageButton cameraButton = fragmentView.findViewById(R.id.Fab_Camera_Button);
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

		final View finalFragmentView = fragmentView;
		fragmentView.findViewById(R.id.share_with_group)
			.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(final View view) {
					AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
					LayoutInflater inflater = getActivity().getLayoutInflater();
					final View view2 = inflater.inflate(R.layout.select_group_dialog, null);
					builder.setView(view2);
					final AlertDialog dialog = builder.create();

					if (groupModel != null) {
						view2.findViewById(R.id.add_member).setVisibility(View.GONE);
					}

					RecyclerView recyclerView = view2.findViewById(R.id.group_list);
					recyclerView.setHasFixedSize(true);
					recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
					SelectGroupAdapter selectGroupAdapter = new SelectGroupAdapter(groupList);
					selectGroupAdapter.setGroupSelectListener(
						new SelectGroupAdapter.GroupSelectListener() {
							@Override public void groupSelected(GroupModel groupModel) {
								BillsFormFragment.this.groupModel = groupModel;
								((TextView) view.findViewById(R.id.group_name)).setText(
									groupModel.getGroupName());
								categoryRecyclerView.setVisibility(View.GONE);
								finalFragmentView.findViewById(R.id.Category_text_view)
									.setVisibility(View.GONE);
								dialog.dismiss();
							}
						});
					recyclerView.setAdapter(selectGroupAdapter);

					view.findViewById(R.id.cross_button)
						.setOnClickListener(new View.OnClickListener() {
							@Override public void onClick(View v) {
								BillsFormFragment.this.groupModel = null;
								((TextView) view.findViewById(R.id.group_name)).setText("None");
								categoryRecyclerView.setVisibility(View.VISIBLE);
								finalFragmentView.findViewById(R.id.Category_text_view)
									.setVisibility(View.VISIBLE);
							}
						});

					view2.findViewById(R.id.add_member)
						.setOnClickListener(new View.OnClickListener() {
							@Override public void onClick(View view) {
								Intent intent = new Intent(getContext(), AddMembersActivity.class);
								startActivityForResult(intent, 4);
								dialog.dismiss();
							}
						});
					dialog.show();
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
		return fragmentView;
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
		//getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

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
		secondSplitList = new ArrayList<>();

		ContactModel you = new ContactModel();
		you.setName("You");
		splitList.add(new Split_Contact_model(you, Specific_Amount));
		secondSplitList.add(new Split_Contact_model(you, amount));

		for (int i = 0; i < userList.size(); i++) {

			if ((i == userList.size() - 1) && (userList.size() + 1) % 2 != 0) {
				Specific_Amount = Integer.parseInt(Specific_Amount) + 1 + "";
			}
			splitList.add(new Split_Contact_model(userList.get(i), Specific_Amount));
			secondSplitList.add(new Split_Contact_model(userList.get(i), "0"));
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
		billModel.setUuid(Util.generateUuid());
		billModel.setCreatedAt(Util.getDateTime());

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

			groupModel2.setUsers(users.toString());
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
			List<ContactModel> list = (List<ContactModel>) data.getSerializableExtra("ADDED");
			contactList.addAll(list);
			ContactRecyclerViewAdapter contactRecyclerViewAdapter =
				new ContactRecyclerViewAdapter(contactList);
			contactRecyclerView.setAdapter(contactRecyclerViewAdapter);
			for (ContactModel model : list) {
				users.append(model.getUserId()).append(",");
			}
		}
	}
}