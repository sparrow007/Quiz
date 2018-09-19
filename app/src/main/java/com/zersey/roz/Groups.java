package com.zersey.roz;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.materialtextfield.MaterialTextField;
import com.zersey.roz.Data.Contacts_contract;
import com.zersey.roz.Data.Contactsdbhelper;
import com.zersey.roz.Data.TransactionDbHelper;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

@SuppressLint("NewApi") public class Groups extends Fragment
		implements UserIdInterface{
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	private TextView First_More, Second_More;
	private String mParam1;
	private String mParam2;
	private int pos=-1;
	private String Contact_Person_Name, Contact_Person_Number;
	private boolean person_added = false;
	private CategoryAdapter Catadapter;
	private RecyclerView First_RecyclerView, Second_RecyclerView, Task_RecyclerView;
	private List<GroupModel> First_List, Second_List;
	private OnFragmentInteractionListener mListener;
	public static First_Slider_Adapter ADAPTER;
	private TransactionDbHelper mDbHelper;
	private List<IncomeModel> Item_list;
	private List<Task_Model> Task_list;
	private List<GroupModel> list;
	private MaterialTextField Material_Title, Material_Amount, Material_Amount_Due;
	private int year_x, month_x, day_x, Selected_date = 0;
	private long Updated_Id;
	private String Category_text, Add_Person_text, Amount_text, Title_text;
	private Uri Image_uri = null;
	private static int DIALOG_ID = 0;
	private EditText AmountEdit, TitleEdit, Amount_Due_Edit;
	private String CardClicked, Updated_Category, Updated_Title, Updated_Amount, Updated_Date;
	private Calendar cal;
	private String[] Months = {
			"Jan", "Feb", "March", "April", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"
	}, Contact_Names;
	private LinearLayout Clothing, Entertainment, Food, Fuel, Health, Salary, More, Notes_Layout,
			Add_Member_Layout;
	//private CheckBox Clothing_checkbox, Entertainment_checkbox, Food_checkbox, Fuel_checkbox,
	//		Health_checkbox, Salary_checkbox, More_checkbox;

	private ListView Add_Member_ListView;
	//private ArrayList<ContactModel> Item_list;
	private List<Split_Contact_model> Split_List;
	private Spinner Split_Spinner;
	public static TextView Split_Notes;
	private RecyclerView Split_RecyclerView;
	private Dialog_Split_RecyclerViewAdapter Adapter;
	private EditText Amount_Edit, Description_Edit, Group_Name_Edit;
	public static RecyclerAdapter adapter;
	public static Task_Slider_Adapter task_slider_adapter;

	private IncomeModel model;
	private Boolean check = true;
	private String Amount;
	//private pageradapter adapter;
	private String Updated_Type = "";
	private GroupModel groupModel;
	private String payerId;
	private String cNumber, code;
	private String users = "";

	private List<ContactModel> Contact_list;
	private RecyclerView Contact_RecyclerView;
	private Contact_RecyclerView_Adapter RecyclerView_Adapter;
	private DatePickerDialog datePicker;
	private RecyclerView Category_Recycler_View;
	private List<String> Category_list;
	private TextView Submit_button;
	private TextView More_TextButton;
	private ImageButton Delete_Button, Camera_Button;
	private ImageView Img_File;
	private TextView Category_text_view, dateEdit;



	public Groups() {
		// Required empty public constructor
	}

	public static Groups newInstance(String param1, String param2) {
		Groups fragment = new Groups();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		list = new ArrayList<>();
		if (getArguments() != null) {
			list.addAll((List<GroupModel>) getArguments().getSerializable("groupList"));
		}
		//mDbHelper = new TransactionDbHelper(getContext());
		First_List = new ArrayList<>(list);
		Second_List = new ArrayList<>();

		//initList();
		mDbHelper = TransactionDbHelper.getInstance(getContext());
		Item_list = new ArrayList<>(mDbHelper.getAllEntries());
		Task_list = new ArrayList<>();
		Task_list = mDbHelper.getTask(-1);
		/*for (int i=0;i<10;i++){
			Task_list.add(new Task_Model("New Task","New Description","null",false));
		}*/
	}

	@Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		View fragmentLayout = inflater.inflate(R.layout.fragment_groups, container, false);
		FloatingActionButton fab = getActivity().findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
						.setAction("Action", null)
						.show();
				Transaction_Form_Dialog();
				/*Intent intent = new Intent(getContext(), Task_Form_Activity.class);
				intent.putExtra("Group", mParam1);
				startActivityForResult(intent, 1);*/
			}
		});
		First_More = fragmentLayout.findViewById(R.id.First_More);
		Second_More = fragmentLayout.findViewById(R.id.Second_More);
		First_More.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				Intent intent = new Intent(getContext(), More_Activity.class);
				intent.putExtra("More", (ArrayList<GroupModel>) First_List);
				Log.d("onClick: ", First_List.size() + "");
				startActivity(intent);
			}
		});
		Second_More.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				Intent intent = new Intent(getContext(), Transactions.class);
				//intent.putExtra("More", (ArrayList<GroupModel>) Second_List);
				//Log.d("onClick: ", First_List.size() + "");
				startActivity(intent);
			}
		});
		fragmentLayout = initRecyclerView(fragmentLayout);

		((Main2Activity) getActivity()).setFragmentRefreshListener(
			new Main2Activity.FragmentRefreshListener() {
				@Override public void onRefresh(List<GroupModel> groups) {
					First_List.clear();
					First_List.addAll(groups);
					First_RecyclerView.getAdapter().notifyDataSetChanged();
					Item_list.clear();
					Item_list.addAll(mDbHelper.getAllEntries());
					Second_RecyclerView.getAdapter().notifyDataSetChanged();
				}

				@Override public void onTaskRefresh(List<Task_Model> taskList) {
					Task_list.clear();
					Task_list.addAll(taskList);
					Task_RecyclerView.getAdapter().notifyDataSetChanged();
				}
			});

		return fragmentLayout;
	}

	void Transaction_Form_Dialog()
	{   LayoutInflater LI = LayoutInflater.from(getContext());
		View PromptsView = LI.inflate(R.layout.content_main, null);
		Contact_list = new ArrayList<>();
		Category_list=new ArrayList<>();
		Category_list.add("Expense");
		Category_list.add("Income");
		Category_list.add("Group");

		Category_Recycler_View = PromptsView.findViewById(R.id.Category_Recycler_View);
		//Category_Recycler_View.setVisibility(View.VISIBLE);
		PromptsView=initView(PromptsView);
		//customlist=(ArrayList<Custom_items>)getIntent().getBundleExtra("Bundle").getSerializable("ARRAYLIST");
		Material_Title = PromptsView.findViewById(R.id.Material_Title);
		Material_Title.setHasFocus(true);
		Material_Amount = PromptsView.findViewById(R.id.Material_Amount);
		Material_Amount.setHasFocus(true);
		Material_Amount_Due = PromptsView.findViewById(R.id.Material_Amount_Due);
		Material_Amount_Due.setVisibility(View.GONE);
		More_TextButton = PromptsView.findViewById(R.id.MoreButton);
		cal = Calendar.getInstance();
		year_x = cal.get(Calendar.YEAR);
		day_x = cal.get(Calendar.DAY_OF_MONTH);
		month_x = cal.get(Calendar.MONTH);
		More_TextButton = PromptsView.findViewById(R.id.MoreButton);
		Delete_Button = PromptsView.findViewById(R.id.Delete_Button);
		Delete_Button.setVisibility(View.GONE);
		//Contact_Button = (ImageButton) findViewById(R.id.Contact_Button);
		//Camera_Button=(ImageButton)findViewById(R.id.Camera_Button);
		//requestPermissions(Manifest.permission.CAMERA,1111);
		Img_File = PromptsView.findViewById(R.id.Img_file);
		Img_File.setVisibility(View.GONE);
		dateEdit = PromptsView.findViewById(R.id.Date_Edit);
		dateEdit.setText(year_x + "-" + (month_x + 1) + "-" + day_x);
		TitleEdit = PromptsView.findViewById(R.id.Title_Edit);
		AmountEdit = PromptsView.findViewById(R.id.Amount_Edit);
		Amount_Due_Edit = PromptsView.findViewById(R.id.Amount_Due_Edit);
		Add_Member_Layout = PromptsView.findViewById(R.id.Add_Member_Layout);
		Split_RecyclerView = new RecyclerView(getContext());
		SharedPreferences prefs = getContext().getSharedPreferences("login", MODE_PRIVATE);
		users += prefs.getString("userid", null);
		//Add_Member_Layout=(LinearLayout)findViewById(R.id.Add_Member_Layout);
		//Add_Member_ListView=(ListView)findViewById(R.id.Add_Member_ListView);

		Submit_button = PromptsView.findViewById(R.id.Submit_Transaction_form);
		dateEdit.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				datePicker.show();
			}
		});

		Add_Member_Layout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Contact_Button(v);
			}
		});
		Delete_Button.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				Delete_Button();
			}
		});


        /*Camera_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Onclick_Image_button();
            }
        });*/
		More_TextButton.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				//MoreButton();
			}
		});


		DatePickerDialog.OnDateSetListener dateSetListener =
				new DatePickerDialog.OnDateSetListener() {
					@Override public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
						year_x = year;
						month_x = month + 1;
						day_x = dayOfMonth;
						Selected_date = dayOfMonth;
						dateEdit.setText(year_x + "-" + month_x + "-" + day_x);
					}
				};
		datePicker =
				new DatePickerDialog(getContext(), R.style.Theme_AppCompat_DayNight_Dialog,
						dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
						cal.get(Calendar.DAY_OF_MONTH));
		datePicker.setCancelable(true);
		datePicker.setTitle("Select Date");
		/*fab = findViewById(R.id.Fab_Camera_Button);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

				Onclick_Image_button();
			}
		});*/

		android.support.v7.app.AlertDialog.Builder alertDialogBuilder =
				new android.support.v7.app.AlertDialog.Builder(getContext());
		alertDialogBuilder.setView(PromptsView);
		alertDialogBuilder.setCancelable(true);
		final android.support.v7.app.AlertDialog dialog = alertDialogBuilder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			@Override
			public void onShow(DialogInterface dialog) {
				Submit_button.setOnClickListener(new View.OnClickListener() {
					@Override public void onClick(View v) {

						Submit();
					}
				});
			}
		});

		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getContext().getResources().getColor(R.color.TransparentWhite)));
		//dialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
		dialog.show();
		dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));




		layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;

		dialog.getWindow()
				.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

	}

	public void Onclick_Image_button() {
		final EditText Image_Link;

		LayoutInflater LI = LayoutInflater.from(getContext());
		//View PromptsView=LI.inflate(R.layout.image_dialog,null);

		//Image_Link=(EditText)PromptsView.findViewById(R.id.);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

		//alertDialogBuilder.setView(PromptsView);

		alertDialogBuilder.setCancelable(true)
				.setTitle(Html.fromHtml("<font color='#3F51B5'>Choose an Image from</font>"))
				.setPositiveButton(Html.fromHtml("<font color='#3F51B5'>camera</font>"),
						new DialogInterface.OnClickListener() {
							@Override public void onClick(DialogInterface dialog, int which) {
								Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
								startActivityForResult(intent, 2);
								//mEditor.insertImage(Image_Link.getText().toString(),"Image");
								//mEditor.insertLink(Href_Link.getText().toString(), Href_Title.getText().toString());

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

	public void Add_notes(View view) {
		final EditText Notes_Edit;
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View notes_view = inflater.inflate(R.layout.notes_dialogue_layout, null);
		Notes_Edit = notes_view.findViewById(R.id.Notes_EditText);
		android.support.v7.app.AlertDialog.Builder alertDialogBuilder =
				new android.support.v7.app.AlertDialog.Builder(getContext());
		alertDialogBuilder.setView(notes_view);
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.setPositiveButton(Html.fromHtml("<font color='#00796B'>Ok</font>"),
				null);
		//alertDialogBuilder.setNegativeButton("Cancel",null);
		alertDialogBuilder.setNeutralButton("Cancel", null);

		final android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override public void onShow(DialogInterface dialogInterface) {

				Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
				Button Negative = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
				Negative.setTextColor(getResources().getColor(R.color.colorPrimary));
				button.setTextColor(getResources().getColor(R.color.colorPrimary));
				Negative.setOnClickListener(new View.OnClickListener() {
					@Override public void onClick(View v) {
						alertDialog.dismiss();
					}
				});
				button.setOnClickListener(new View.OnClickListener() {

					@Override public void onClick(View view) {
						// TODO Do something

						//Dismiss once everything is OK.
						if (TextUtils.isEmpty(Notes_Edit.getText().toString())) {
							Toast.makeText(getContext(), "Please write something",
									Toast.LENGTH_LONG).show();
						} else {
							alertDialog.dismiss();
						}
					}
				});
			}
		});
		alertDialog.show();
	}

	public void Delete_Button() {
		if (NetworkUtil.hasInternetConnection(getContext())) {
			new ServerUtil(getContext()).deleteEntry(model.getOnlineId());
		} else {
			mDbHelper.addToTemp(model.getId(), model.getOnlineId(), "delete");
		}

		mDbHelper.deleteEntry(pos, model.getId());

		//finish();
	}

	public void Dialog(View view) {
		Amount = AmountEdit.getText().toString();
		if (TextUtils.isEmpty(Amount)) {
			Amount = "0";
		}
		if (!check) {
			groupSplit();
		} else if (check) {
			//String[] names = Item_list.get;

			singleSplit();
		}
		Log.d("Dialog: ", Split_List.size() + "");
		LayoutInflater LI = LayoutInflater.from(getContext());
		View PromptsView = LI.inflate(R.layout.split_dialog_layout, null);
		Split_Spinner = PromptsView.findViewById(R.id.Split_Spinner);
		Split_RecyclerView = PromptsView.findViewById(R.id.Dialog_RecyclerView);
		Split_Notes = PromptsView.findViewById(R.id.Dialog_Split_Notes);
		Split_RecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		Adapter = new Dialog_Split_RecyclerViewAdapter(Split_List, "", Integer.parseInt(Amount));
		Split_RecyclerView.setAdapter(Adapter);

		Split_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (position == 2) {
					Adapter = new Dialog_Split_RecyclerViewAdapter(Split_List, "ratio",
							Integer.parseInt(Amount));
					Split_RecyclerView.setAdapter(Adapter);
				}
				if (position == 1) {
					Adapter = new Dialog_Split_RecyclerViewAdapter(Split_List, "",
							Integer.parseInt(Amount));
					Split_RecyclerView.setAdapter(Adapter);
				}
				if (position == 0) {
					Adapter = new Dialog_Split_RecyclerViewAdapter(Split_List, "Equally",
							Integer.parseInt(Amount));
					Split_RecyclerView.setAdapter(Adapter);
				}
			}

			@Override public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		android.support.v7.app.AlertDialog.Builder alertDialogBuilder =
				new android.support.v7.app.AlertDialog.Builder(getContext());
		alertDialogBuilder.setView(PromptsView);
		alertDialogBuilder.setCancelable(false);

		alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override public void onClick(DialogInterface dialog, int which) {
				Split_List = Adapter.getList();
			}
		});
		final android.support.v7.app.AlertDialog dialog = alertDialogBuilder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override public void onShow(DialogInterface dialogInterface) {
                Button positive_Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
				//Button Negative = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
				//Negative.setTextColor(getColor(R.color.colorPrimary));
				positive_Button.setTextColor(getResources().getColor(R.color.colorPrimary));
				/*Negative.setOnClickListener(new View.OnClickListener() {
					@Override public void onClick(View v) {
						alertDialog.dismiss();
					}
				});*/
				/*positive_Button.setOnClickListener(new View.OnClickListener() {

					@Override public void onClick(View view) {
						// TODO Do something

						//Dismiss once everything is OK.
						if (Adapter.getSplit_Correctly()) {
							Toast.makeText(MainActivity.this, "Please Split Correctly",
									Toast.LENGTH_LONG).show();
						} else {
							dialog.dismiss();
						}
					}
				});*/
			}
		});
		dialog.show();
		dialog.getWindow()
				.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
	}

	private void singleSplit() {
		int no_of_Person = Contact_list.size();
		String Specific_Amount;
		Amount = Material_Amount.getEditText().getText().toString();
		if (TextUtils.isEmpty(Amount)) {
			Specific_Amount = "0";
		} else {
			Specific_Amount = "" + Integer.parseInt(Amount) / (Contact_list.size() + 1);
		}
		Log.d("Dialog: ", Contact_list.size() + "");
		Split_List = new ArrayList<>();
		ContactModel you = new ContactModel();
		you.setName("You");
		Split_List.add(new Split_Contact_model(you, Specific_Amount));
		if (Contact_list.isEmpty()) {
			Toast.makeText(getContext(), "Add members ", Toast.LENGTH_SHORT).show();
			return;
		}
		for (int i = 0; i < Contact_list.size(); i++) {

			if ((i == Contact_list.size() - 1) && (no_of_Person + 1) % 2 != 0) {
				Specific_Amount = Integer.parseInt(Specific_Amount) + 1 + "";
			}
			Split_List.add(new Split_Contact_model(Contact_list.get(i), Specific_Amount));
		}
	}

	private void groupSplit() {
		String[] names = groupModel.getUsers().split(",");
		List<ContactModel> userList = mDbHelper.getUserWithUserId(names);
		String Specific_Amount;
		Amount = Material_Amount.getEditText().getText().toString();
		if (TextUtils.isEmpty(Amount)) {
			Specific_Amount = "0";
		} else {
			Specific_Amount = "" + Integer.parseInt(Amount) / (userList.size() + 1);
		}
		Log.d("Dialog: ", Contact_list.size() + "");
		Split_List = new ArrayList<>();

		ContactModel you = new ContactModel();
		you.setName("You");
		Split_List.add(new Split_Contact_model(you, Specific_Amount));

		for (int i = 0; i < userList.size(); i++) {
			Split_List.add(new Split_Contact_model(userList.get(i), Specific_Amount));
		}
	}


	private View initView(View PromptView) {
		Contact_RecyclerView = new RecyclerView(getContext());
		Contact_RecyclerView = PromptView.findViewById(R.id.Add_Member_RecyclerView);
		Contact_RecyclerView.setLayoutManager(
				new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,
						false));
		if (TextUtils.isEmpty(Updated_Type)) {
			Catadapter = new CategoryAdapter(getContext(), Category_list, "");
		} else {
			Catadapter = new CategoryAdapter(getContext(), Category_list, Updated_Type);
		}
		Category_Recycler_View.setLayoutManager(
				new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,
						false));
		Category_Recycler_View.setAdapter(Catadapter);
		Category_Recycler_View.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
			}
		});
		return PromptView;
	}


	//private void initList() {
	//	for (int i = 0; i < 5; i++) {
	//		First_List.add("Title");
	//		Second_List.add("WAO 1");
	//	}
	//}



	private View initRecyclerView(View fragmentLayout) {
		Task_RecyclerView = fragmentLayout.findViewById(R.id.Task_Slider);
		Task_RecyclerView.setLayoutManager(
			new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
		task_slider_adapter = new Task_Slider_Adapter(Task_list);
		Task_RecyclerView.setAdapter(task_slider_adapter);

		First_RecyclerView = fragmentLayout.findViewById(R.id.First_Slider);
		First_RecyclerView.setLayoutManager(
			new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
		//new CardSnapHelper().attachToRecyclerView(First_RecyclerView);
		ADAPTER = new First_Slider_Adapter(getContext(), First_List);
		First_RecyclerView.setAdapter(ADAPTER);
		//First_RecyclerView.smoothScrollToPosition(0);

		Second_RecyclerView = fragmentLayout.findViewById(R.id.Second_Slider);
		Second_RecyclerView.setLayoutManager(
			new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
		adapter = new RecyclerAdapter(Item_list);
		Second_RecyclerView.setAdapter(adapter);

		return fragmentLayout;
	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnFragmentInteractionListener) {
			mListener = (OnFragmentInteractionListener) context;
		} else {
			throw new RuntimeException(
				context.toString() + " must implement OnFragmentInteractionListener");
		}
	}

	@Override public void onDetach() {
		super.onDetach();
		mListener = null;
	}
	public void Contact_Button(View view) {
		/*Intent intent =
			new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
		startActivityForResult(intent, 3);*/
		Intent intent = new Intent(getContext(), Add_Members_Activity.class);
		startActivityForResult(intent, 1);
	}

	@Override
	public void updateUserId(String userId) {
		payerId = userId;
		users += "," + userId;
	}

	public interface OnFragmentInteractionListener {
		void onFragmentInteraction(Uri uri);
	}

	public Boolean Check_Contacts(String contactname, String contactnumber) {
		ArrayList<Custom_Contact_items> Item_list = new ArrayList<>();
		Contactsdbhelper mDbHelper = new Contactsdbhelper(getContext());
		SQLiteDatabase db1 = mDbHelper.getReadableDatabase();
		//final  ArrayList<String> labellist=new ArrayList<>();
		// Define a projection that specifies which columns from the database
		// you will actually use after MainActivity.this query.
		String[] projection = {
				Contacts_contract.Contacts_Entry._ID,
				Contacts_contract.Contacts_Entry.Column_Contact_Name,
				Contacts_contract.Contacts_Entry.Column_Contact_Number
		};

		//Cursor cursor=db1.rawQuery("SELECT * FROM "+recipe_entry.Table_name,null);
		// Perform a query on the pets table
		Cursor cursor =
				db1.query(Contacts_contract.Contacts_Entry.Table_name,   // The table to query
						projection,            // The columns to return
						null,                  // The columns for the WHERE clause
						null,                  // The values for the WHERE clause
						null,                  // Don't group the rows
						null,                  // Don't filter by row groups
						null);                   // The sort order

		try {

			cursor.moveToFirst();
			// Iterate through all the returned rows in the cursor
			while (!cursor.isAfterLast()) {
				// Use that index to extract the String or Int value of the word
				// at the current row the cursor is on.
				int currentID =
						cursor.getInt(cursor.getColumnIndex(Contacts_contract.Contacts_Entry._id));
				String Name = cursor.getString(
						cursor.getColumnIndex(Contacts_contract.Contacts_Entry.Column_Contact_Name));
				//Log.d( "current label ",currentName);
				String Number = cursor.getString(
						cursor.getColumnIndex(Contacts_contract.Contacts_Entry.Column_Contact_Number));
				if (TextUtils.equals(Number, contactnumber)) {
					return true;
				}
				Custom_Contact_items CCItem = new Custom_Contact_items(Name, Number);
				Item_list.add(CCItem);

				cursor.moveToNext();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}


	public void Submit() {
		Amount_text = AmountEdit.getText().toString();
		//Add_Person_text = AutoCompleteContacts.getText().toString();
		Title_text = TitleEdit.getText().toString();
		Category_text = Catadapter.getLastCategory();
		View focus = null;
		Boolean cancel = false;
		if (groupModel == null && TextUtils.isEmpty(Category_text)) {
			/*Snackbar.make(findViewById(R.id.myCoordinaterLayout), "Category can't be empty",
					Snackbar.LENGTH_LONG).setAction("Action", null).show();*/
			Toast.makeText(getContext(),"Category can't be empty\"",Toast.LENGTH_LONG).show();
		} else {
			if (day_x == 0) {
				dateEdit.setError("Date can't be empty");
				focus = dateEdit;
				cancel = true;
			}
			if (Util.isEmpty(Amount_text)) {
				AmountEdit.setError("Amount can't be empty");
				focus = AmountEdit;
				cancel = true;
			}
			if (TextUtils.isEmpty(Title_text)) {
				TitleEdit.setError("Title can't be empty");
				focus = TitleEdit;
				cancel = true;
			}

			if (cancel) {
				focus.requestFocus();
			} else {
				if (person_added) {
					if (Check_Contacts(Contact_Person_Name, Contact_Person_Number)) {
						Toast.makeText(getContext(),
								"Contact is already present in the database", Toast.LENGTH_LONG).show();
					} else {
						Contactsdbhelper mdbhelper = new Contactsdbhelper(getContext());
						SQLiteDatabase db = mdbhelper.getWritableDatabase();
						ContentValues values = new ContentValues();

						values.put(Contacts_contract.Contacts_Entry.Column_Contact_Name,
								Contact_Person_Name);
						values.put(Contacts_contract.Contacts_Entry.Column_Contact_Number,
								Contact_Person_Number);
						//values.put(Transaction_contract.Transaction_Entry.Column_Amount, "Rs " + Amount_text);
						//Log.d("Date created", DateEdit_text);
						//values.put(Transaction_contract.Transaction_Entry.Column_Date_Created, DateEdit_text);

						long newRowId =
								db.insert(Contacts_contract.Contacts_Entry.Table_name, null, values);
						if (newRowId == -1) {
							// If the row COLUMN_ONLINE_ID is -1, then there was an error with insertion.
							Toast.makeText(getContext(), "Error with saving Contact",
									Toast.LENGTH_SHORT).show();
						} else {
							// Otherwise, the insertion was successful and we can display a toast with the row COLUMN_ONLINE_ID.
							Toast.makeText(getContext(),
									"Contact saved with row id: " + newRowId, Toast.LENGTH_SHORT)
									.show();
						}
					}
				}

				IncomeModel expenseModel = new IncomeModel();

				if (TextUtils.isEmpty(CardClicked)) {
					String DateEdit_text = dateEdit.getText().toString();
					SharedPreferences prefs = getContext().getSharedPreferences("login", MODE_PRIVATE);

					if (groupModel != null) {
						groupSplit();
					} else {
						singleSplit();
					}

					StringBuilder totalAmounts = new StringBuilder();
					StringBuilder payerIds = new StringBuilder();
					StringBuilder amountsPaid = new StringBuilder();
					StringBuilder datesPaid = new StringBuilder();

					for (Split_Contact_model s : Split_List) {
						totalAmounts.append(s.getSplit_Amount()).append(",");
						if (s.getContact_Name().getName().equalsIgnoreCase("you")) {
							payerIds.append(prefs.getString("userid", null)).append(",");
						} else {
							payerIds.append(s.getContact_Name().getUserId()).append(",");
						}
						amountsPaid.append("0.00").append(",");
						datesPaid.append(DateEdit_text).append(",");
					}

					expenseModel.setType(Category_text);
					expenseModel.setTitle(Title_text);
					expenseModel.setTotalAmount(totalAmounts.toString());
					expenseModel.setPaidAtDate(datesPaid.toString());
					expenseModel.setAmountPaid(amountsPaid.toString());
					expenseModel.setAmountDue(totalAmounts.toString());
					expenseModel.setPayerId(payerIds.toString());
					expenseModel.setInvoiceId("");
					try {
						expenseModel.setUuid(Util.generateUuid(prefs.getString("userid", null)));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					}

					if (groupModel != null) {
						expenseModel.setGroupId(groupModel.getGroupId());
						String str=groupModel.getGroupId()+"";
						Log.d( "Submit: ",str);
						expenseModel.setType("gt");
						long rowId = mDbHelper.createEntry(expenseModel);
						new ServerUtil(getContext()).createEntry(expenseModel);
						expenseModel.setId(rowId);
					}

					if (groupModel == null) {
						GroupModel groupModel2 = new GroupModel();
						groupModel2.setGroupName(Title_text);
						groupModel2.setTypeId(1);
						//groupModel.setGroupDesc();
						//String str=groupModel.getGroupId()+"";

						groupModel2.setUsers(users);
						long newrowId = mDbHelper.createGroup(groupModel2);
						Log.d( "Submit: ",""+expenseModel
						);
						groupModel2.setId(newrowId);
						new ServerUtil(getContext()).createSingleGroup(groupModel2,
								expenseModel, null);
					}

					Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG).show();
				} else {
					Updated_Title = TitleEdit.getText().toString();
					Updated_Category = Category_text;
					Updated_Amount = AmountEdit.getText().toString();
					Updated_Date = dateEdit.getText().toString();

					//model.setPaidAtDate(Updated_Date);
					//model.setTotalAmount(Updated_Amount);
					model.setTitle(Updated_Title);
					model.setCatId(Updated_Category);
					model.setGroupId(Updated_Id);

					mDbHelper.updateEntry(pos, model);
					if (NetworkUtil.hasInternetConnection(getContext())) {
						new ServerUtil(getContext()).updateEntry(model);
					} else {
						mDbHelper.addToTemp(model.getId(), 0, "edit");
					}

					//new ServerUtil(MainActivity.this).updateEntry(pos, expenseModel);
					//
					//
					//TransactionDbHelper mdbhelper = new TransactionDbHelper(MainActivity.this);
					//SQLiteDatabase db = mdbhelper.getWritableDatabase();
					//ContentValues values = new ContentValues();
					////values.put(recipe_entry.Column_Recipe_Image,byteimage);
					//values.put(TransactionDbContract.Transaction_Entry.COLUMN_TITLE, Updated_Title);
					//values.put(TransactionDbContract.Transaction_Entry.COLUMN_CATEGORY,
					//	Updated_Category);
					//values.put(TransactionDbContract.Transaction_Entry.COLUMN_AMOUNT,
					//	"Rs " + Updated_Amount);
					//values.put(TransactionDbContract.Transaction_Entry.COLUMN_DATE_CREATED,
					//	Updated_Date);
					//db.update(TransactionDbContract.Transaction_Entry.TABLE_NAME, values,
					//	TransactionDbContract.Transaction_Entry.COLUMN_ONLINE_ID + "=" + Updated_Id, null);
					//new ServerUtil(MainActivity.this).updateEntry(expenseModel);
				}

				if (groupModel != null) {
					Intent intent = new Intent();
					intent.putExtra("model", expenseModel);
					//setResult(Activity.RESULT_OK, intent);
				}

				//finish();
			}
		}
	}

	@Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1234) {
			if (resultCode == RESULT_OK) {
				GroupModel model = (GroupModel) data.getSerializableExtra("group");
				First_List.add(model);
				First_RecyclerView.getAdapter().notifyItemInserted(First_List.size() - 1);
			}
		} else if (resultCode == 52) {

		}
	}
}
