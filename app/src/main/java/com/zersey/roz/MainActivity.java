package com.zersey.roz;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.zersey.roz.Data.Contacts_contract;
import com.zersey.roz.Data.Contactsdbhelper;
import com.zersey.roz.Data.TransactionDbHelper;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@SuppressLint("NewApi") public class MainActivity extends AppCompatActivity
	implements UserIdInterface {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link FragmentPagerAdapter} derivative, which will keep every
	 * loaded fragment in memory. If MainActivity.this becomes too memory intensive, it
	 * may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */

    /*ImageView Img_File;
    private TextView More_Button;
    private ImageButton Delete_Button;
    private AutoCompleteTextView AutoCompleteContacts;
    private ArrayAdapter<String> ContactAdapter;
    private MaterialTextField Material_Title, Material_Amount, Material_Date, Material_Notes, Material_Amount_Due;
    public View layout_view = null;
    private List<IncomeModel> customlist;
    private ArrayList<String> Contact_list;
    private TextView Category_text_view;*/
	private CategoryAdapter adapter;
	private boolean person_added = false;
	private RecyclerView Category_Recycler_View;
	private String Contact_Person_Name, Contact_Person_Number;
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	private ImageButton fab;
	private RecyclerView Contact_RecyclerView;
	private Contact_RecyclerView_Adapter RecyclerView_Adapter;
	private DatePickerDialog datePicker;
	//private View fragmentLayout;
	ImageView Img_File;
	private TextView Submit_button;
	private TextView More_TextButton;
	private ImageButton Delete_Button, Camera_Button;
	//private AutoCompleteTextView AutoCompleteContacts;
	private ArrayAdapter<String> ContactAdapter;
	private MaterialTextField Material_Title, Material_Amount, Material_Amount_Due;
	public View layout_view = null;
	private List<IncomeModel> customlist;
	private ArrayList<String> Contact_list;
	private List<String> Category_list;
	private TextView Category_text_view, dateEdit;
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
	private LinearLayout Clothing, Entertainment, Food, Fuel, Health, Salary, More, Notes_Layout;
	//private CheckBox Clothing_checkbox, Entertainment_checkbox, Food_checkbox, Fuel_checkbox,
	//		Health_checkbox, Salary_checkbox, More_checkbox;

	private ListView Add_Member_ListView;
	private ArrayList<Custom_Contact_items> Item_list;
	private List<Split_Contact_model> Split_List;
	private Spinner Split_Spinner;
	private TextView Split_Notes;
	private RecyclerView Split_RecyclerView;
	private Dialog_Split_RecyclerViewAdapter Adapter;
	private EditText Amount_Edit, Description_Edit, Group_Name_Edit;

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private int pos;
	private TransactionDbHelper mDbHelper;
	private IncomeModel model;

	//private pageradapter adapter;
	private String Updated_Type = "";
	private GroupModel groupModel;
	private String payerId;
	private String cNumber, code;
	private String users = "";

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//getSupportActionBar().setTitle("Roz");
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		mDbHelper = TransactionDbHelper.getInstance(this);
		Category_list = new ArrayList<>();

		SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
		users += prefs.getString("userid", null);

		Dexter.withActivity(MainActivity.this)
			.withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
				Manifest.permission.READ_CONTACTS)
			.withListener(new MultiplePermissionsListener() {
				@Override
				public void onPermissionsChecked(MultiplePermissionsReport report) {/* ... */}

				@Override
				public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
					PermissionToken token) {/* ... */}
			})
			.check();
		CardClicked = getIntent().getStringExtra("CardClicked");
		pos = getIntent().getIntExtra("pos", -1);
		if (!TextUtils.isEmpty(CardClicked)) {

			model = (IncomeModel) getIntent().getSerializableExtra("model");

			Updated_Category = getIntent().getStringExtra("Category");
			Updated_Amount = getIntent().getStringExtra("Amount");
			Updated_Title = getIntent().getStringExtra("Title");
			Updated_Date = getIntent().getStringExtra("DateCreated");
			Updated_Type = getIntent().getStringExtra("Type");
			Updated_Id = getIntent().getIntExtra("_ID", 0);
		}
		Contact_list = new ArrayList<String>();
		Category_list.add("Expense");
		Category_list.add("Income");
		Category_list.add("Group");

		Category_Recycler_View = findViewById(R.id.Category_Recycler_View);
		//Category_Recycler_View.setVisibility(View.VISIBLE);
		initRecyclerView();
		//customlist=(ArrayList<Custom_items>)getIntent().getBundleExtra("Bundle").getSerializable("ARRAYLIST");
		Material_Title = (MaterialTextField) findViewById(R.id.Material_Title);
		Material_Title.setHasFocus(true);
		Material_Amount = (MaterialTextField) findViewById(R.id.Material_Amount);
		Material_Amount.setHasFocus(true);
		Material_Amount_Due = (MaterialTextField) findViewById(R.id.Material_Amount_Due);
		Material_Amount_Due.setVisibility(View.GONE);
		More_TextButton = (TextView) findViewById(R.id.MoreButton);
		cal = Calendar.getInstance();
		year_x = cal.get(Calendar.YEAR);
		day_x = cal.get(Calendar.DAY_OF_MONTH);
		month_x = cal.get(Calendar.MONTH);
		More_TextButton = (TextView) findViewById(R.id.MoreButton);
		Delete_Button = (ImageButton) findViewById(R.id.Delete_Button);
		Delete_Button.setVisibility(View.GONE);
		//Contact_Button = (ImageButton) findViewById(R.id.Contact_Button);
		//Camera_Button=(ImageButton)findViewById(R.id.Camera_Button);
		//requestPermissions(Manifest.permission.CAMERA,1111);
		Img_File = (ImageView) findViewById(R.id.Img_file);
		Img_File.setVisibility(View.GONE);
		dateEdit = (TextView) findViewById(R.id.Date_Edit);
		dateEdit.setText(year_x + "-" + (month_x + 1) + "-" + day_x);
		TitleEdit = (EditText) findViewById(R.id.Title_Edit);
		AmountEdit = (EditText) findViewById(R.id.Amount_Edit);
		Amount_Due_Edit = (EditText) findViewById(R.id.Amount_Due_Edit);
		//AutoCompleteContacts = (AutoCompleteTextView) findViewById(R.id.Notes_Edit);

		if (getIntent().getSerializableExtra("group") != null) {
			groupModel = (GroupModel) getIntent().getSerializableExtra("group");
			Category_Recycler_View.setVisibility(View.GONE);
			findViewById(R.id.Category_text_view).setVisibility(View.GONE);
		}

/*		Clothing = (LinearLayout) findViewById(R.id.Clothing_layout);
		Entertainment = (LinearLayout) findViewById(R.id.Entertainment_layout);
		Food = (LinearLayout) findViewById(R.id.food_layout);
		Fuel = (LinearLayout) findViewById(R.id.fuel_layout);
		Health = (LinearLayout) findViewById(R.id.Health_layout);
		Salary = (LinearLayout) findViewById(R.id.Salary_layout);
		More = (LinearLayout) findViewById(R.id.More_layout);
	Clothing_checkbox = (CheckBox) findViewById(R.id.Clothing_checkbox);
		Entertainment_checkbox =
				(CheckBox) findViewById(R.id.Entertainment_checkbox);
		Food_checkbox = (CheckBox) findViewById(R.id.Food_checkbox);
		Fuel_checkbox = (CheckBox) findViewById(R.id.Fuel_checkbox);
		Health_checkbox = (CheckBox) findViewById(R.id.Health_checkbox);
		Salary_checkbox = (CheckBox) findViewById(R.id.Salary_checkbox);
		More_checkbox = (CheckBox) findViewById(R.id.More_checkbox);
		Category_text_view = (TextView) findViewById(R.id.Category_text_view);

		Clothing_checkbox.setOnCheckedChangeListener(checkedChangeListener);
		Entertainment_checkbox.setOnCheckedChangeListener(checkedChangeListener);
		Food_checkbox.setOnCheckedChangeListener(checkedChangeListener);
		Fuel_checkbox.setOnCheckedChangeListener(checkedChangeListener);
		Health_checkbox.setOnCheckedChangeListener(checkedChangeListener);
		Salary_checkbox.setOnCheckedChangeListener(checkedChangeListener);
		More_checkbox.setOnCheckedChangeListener(checkedChangeListener);
*/
		Item_list = new ArrayList<>();
		Split_List = new ArrayList<>();
		// Amount_Edit=(EditText)findViewById(R.id.Group_Amount_Edit);
		//Description_Edit=(EditText)findViewById(R.id.Group_Description_Edit);
		//Group_Name_Edit=(EditText)findViewById(R.id.Group_Amount_Edit);
		Split_RecyclerView = new RecyclerView(this);
		//Add_Member_Layout=(LinearLayout)findViewById(R.id.Add_Member_Layout);
		//Add_Member_ListView=(ListView)findViewById(R.id.Add_Member_ListView);

		Submit_button = (TextView) findViewById(R.id.Submit_Button);
		dateEdit.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				onclick_date();
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
				MoreButton();
			}
		});
		Submit_button.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {

				Submit();
			}
		});

		datePicker =
			new DatePickerDialog(MainActivity.this, R.style.Theme_AppCompat_DayNight_Dialog,
				dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
				cal.get(Calendar.DAY_OF_MONTH));
		datePicker.setCancelable(true);
		datePicker.setTitle("Select Date");
		fab = (ImageButton) findViewById(R.id.Fab_Camera_Button);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

				Onclick_Image_button();
			}
		});

		if (!TextUtils.isEmpty(CardClicked)) {
			Delete_Button.setVisibility(View.VISIBLE);
			Material_Title.setHasFocus(true);
			Material_Amount.setHasFocus(true);
			//Material_Date.setHasFocus(true);

            /*
            Updated_Category = getIntent().getStringExtra("Category");
            Updated_Amount = getIntent().getStringExtra("Amount");
            Updated_Title = getIntent().getStringExtra("Title");
            Updated_Date = getIntent().getStringExtra("DateCreated");
            Updated_Id = getIntent().getIntExtra("_ID", 0);*/
			/*if ("Clothing".equals(Updated_Category)) {
				Clothing.setBackgroundTintList(
						MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
				Category_text = "Clothing";
			} else if ("Entertainment".equals(Updated_Category)) {
				Entertainment.setBackgroundTintList(
						MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
				Category_text = "Entertainment";
			} else if ("Food".equals(Updated_Category)) {
				Food.setBackgroundTintList(
						MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
				Category_text = "Food";
			} else if ("Fuel".equals(Updated_Category)) {
				Fuel.setBackgroundTintList(
						MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
				Category_text = "Fuel";
			} else if ("Health".equals(Updated_Category)) {
				Health.setBackgroundTintList(
						MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
				Category_text = "Health";
			} else if ("Salary".equals(Updated_Category)) {
				Salary.setBackgroundTintList(
						MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
				Category_text = "Salary";
			}*/
			Updated_Amount = Updated_Amount.replace("Rs ", "");
			AmountEdit.setText(Updated_Amount);
			TitleEdit.setText(Updated_Title);
			dateEdit.setText(Updated_Date);
		}
	}

	private void initRecyclerView() {
		//Category_Recycler_View=new RecyclerView(this);
		Contact_RecyclerView = new RecyclerView(this);
		Contact_RecyclerView = (RecyclerView) findViewById(R.id.Add_Member_RecyclerView);
		Contact_RecyclerView.setLayoutManager(
			new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,
				false));
		//View view=Category_Recycler_View.findViewHolderForAdapterPosition(0);
		adapter = new CategoryAdapter(getApplicationContext(), Category_list);
		Category_Recycler_View.setLayoutManager(
			new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,
				false));
		Category_Recycler_View.setAdapter(adapter);
		Category_Recycler_View.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
			}
		});
	}

	public void Add_notes(View view) {
		final EditText Notes_Edit;
		LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
		View notes_view = inflater.inflate(R.layout.notes_dialogue_layout, null);
		Notes_Edit = (EditText) notes_view.findViewById(R.id.Notes_EditText);
		android.support.v7.app.AlertDialog.Builder alertDialogBuilder =
			new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
		alertDialogBuilder.setView(notes_view);
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.setPositiveButton(Html.fromHtml("<font color='#00796B'>Ok</font>"),
			null);
		//alertDialogBuilder.setNegativeButton("Cancel",null);
		alertDialogBuilder.setNeutralButton("Cancel", null);

		final android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override public void onShow(DialogInterface dialogInterface) {

				Button button = ((AlertDialog) alertDialog).getButton(AlertDialog.BUTTON_POSITIVE);
				Button Negative = ((AlertDialog) alertDialog).getButton(AlertDialog.BUTTON_NEUTRAL);
				Negative.setTextColor(getColor(R.color.colorPrimary));
				button.setTextColor(getColor(R.color.colorPrimary));
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
							Toast.makeText(MainActivity.this, "Please write something",
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

	public void Dialog(View view) {
		String Amount = AmountEdit.getText().toString();
		int no_of_Person = Item_list.size();
		String Specific_Amount;
		if (TextUtils.isEmpty(Amount)) {
			Specific_Amount = "0";
		} else {
			Specific_Amount = "" + Integer.parseInt(Amount) / no_of_Person;
		}
		Log.d("Dialog: ", Item_list.size() + "");
		if (Item_list.size() > 0) {
			Split_List = new ArrayList<>();
			for (int i = 0; i < Item_list.size(); i++) {

				Split_List.add(new Split_Contact_model(Item_list.get(i).getContact_Person_Name(),
					Specific_Amount));
			}
		} else {
			Split_List = new ArrayList<>();
			Split_List.add(new Split_Contact_model("Bharat", Specific_Amount));
		}
		Log.d("Dialog: ", Split_List.size() + "");
		LayoutInflater LI = LayoutInflater.from(MainActivity.this);
		View PromptsView = LI.inflate(R.layout.split_dialog_layout, null);
		Split_Spinner = (Spinner) PromptsView.findViewById(R.id.Split_Spinner);
		Split_RecyclerView = (RecyclerView) PromptsView.findViewById(R.id.Dialog_RecyclerView);
		Split_Notes = (TextView) PromptsView.findViewById(R.id.Dialog_Split_Notes);
		Split_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
		Adapter = new Dialog_Split_RecyclerViewAdapter(getApplicationContext(), Split_List);
		Split_RecyclerView.setAdapter(Adapter);

		Split_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

			}

			@Override public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		android.support.v7.app.AlertDialog.Builder alertDialogBuilder =
			new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
		alertDialogBuilder.setView(PromptsView);
		alertDialogBuilder.setCancelable(true);
		alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override public void onClick(DialogInterface dialog, int which) {

			}
		});
		Dialog dialog = alertDialogBuilder.create();

		dialog.show();
		dialog.getWindow()
			.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

	}



	/*Thread th = new Thread() {
		@Override public void run() {

			ArrayAdapter<String> contactAdapter = new ArrayAdapter<String>(MainActivity.this,
				android.R.layout.simple_dropdown_item_1line, Contact_Names);
			AutoCompleteContacts = (AutoCompleteTextView) findViewById(R.id.Notes_Edit);
			AutoCompleteContacts.setThreshold(1);
			AutoCompleteContacts.setAdapter(contactAdapter);
		}
	};

	private CheckBox.OnCheckedChangeListener checkedChangeListener =
			new CheckBox.OnCheckedChangeListener() {
				 @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked == true) {
						if (buttonView.getParent().equals(Clothing)) {
							Category_text = "Expense";
							Clothing.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
							Entertainment.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
							Food.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
							/*Fuel.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
							Health.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
							Salary.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
							More.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
						}
						if (buttonView.getParent().equals(Entertainment)) {
							Category_text = "Income";
							Entertainment.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
							Clothing.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
							Food.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
							/*Fuel.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
							Health.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
							Salary.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
							More.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
						}
						if (buttonView.getParent().equals(Food)) {
							Category_text = "Group Expense";
							Food.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
							Entertainment.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
							Clothing.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
							Fuel.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
							Health.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
							Salary.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
							More.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
						}
						/*if (buttonView.getParent().equals(Fuel)) {
							Category_text = "Fuel";
							Fuel.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
							Entertainment.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
							Food.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
							Clothing.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
							Health.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
							Salary.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
							More.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
						}*/
						/*if (buttonView.getParent().equals(Health)) {
							Category_text = "Health";
							Health.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
							Entertainment.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
							Food.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
							Fuel.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
							Clothing.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
							Salary.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
							More.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
						}*/

                        /*if(buttonView.getParent().equals(Salary)){
                            Category_text="Salary";
                            Salary.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                            Entertainment.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Food.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Fuel.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Health.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Clothing.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            More.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                        }
                        if(buttonView.getParent().equals(More)){

                            More.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                            Entertainment.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Food.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Fuel.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Health.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Salary.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            More.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                        }
					} else {
						if (buttonView.getParent().equals(Clothing)) {
							Category_text = "";
							Clothing.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                     /*  Entertainment.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
						}
						if (buttonView.getParent().equals(Entertainment)) {
							Category_text = "";
							Entertainment.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                      /* Clothing.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
						}
						if (buttonView.getParent().equals(Food)) {
							Category_text = "";
							Food.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                       /*Entertainment.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Clothing.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
						}
						/*if (buttonView.getParent().equals(Fuel)) {
							Category_text = "";
							Fuel.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                       Entertainment.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Clothing.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
						}*/
						/*if (buttonView.getParent().equals(Health)) {
							Category_text = "";
							Health.setBackgroundTintList(
									MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                       Entertainment.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Clothing.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
						}*/
                        /*if(buttonView.getParent().equals(Salary)){
                            Category_text="";
                            Salary.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                       /*Entertainment.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Clothing.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));*/
	//}
                        /*if(buttonView.getParent().equals(More)){
                            Category_text="";
                            More.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                      /* Entertainment.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Clothing.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
						//}
					}
				}
			};*/

	@Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//callbackManager.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 2 && resultCode == RESULT_OK) {

			//Bitmap photo = (Bitmap) data.getExtras().get("data");
			Uri uri = data.getData();
			Img_File.setVisibility(View.VISIBLE);
			Img_File.setImageURI(uri);
			Image_uri = uri;
		} else if (requestCode == 1 && resultCode == RESULT_OK) {
			//Bitmap photo = (Bitmap) data.getExtras().get("data");
			Uri uri = data.getData();
			Img_File.setVisibility(View.VISIBLE);
			Image_uri = uri;
			Img_File.setVisibility(View.VISIBLE);
			Img_File.setImageURI(uri);
		} else if (requestCode == 3 && resultCode == RESULT_OK) {
			//List<String> temp=getIntent().getStringArrayListExtra("Previous_Contacts");
			Uri contactData = data.getData();
			Cursor c = MainActivity.this.managedQuery(contactData, null, null, null, null);
			if (c.moveToFirst()) {
				String cNumber = null;
				final String name =
					c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

				String hasPhone =
					c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

				if (hasPhone.equalsIgnoreCase("1")) {

					cNumber = c.getString(
						c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
					cNumber = cNumber.replace(" ", "").replace("+", "");
					if (cNumber.length() > 10) {
						code = cNumber.substring(0, cNumber.length() - 10);
						cNumber = cNumber.substring(cNumber.length() - 10);
					} else {
						code = "91";
					}
					//System.out.println("number is:"+cNumber);
				}
				Custom_Contact_items CCItem = new Custom_Contact_items(name, cNumber);
				if (!Check_Contact_List(cNumber)) {
					Item_list.add(CCItem);
					Log.d("onActivityResult: ", Item_list.size() + "");
					Contact_RecyclerView.setVisibility(View.VISIBLE);
					RecyclerView_Adapter = new Contact_RecyclerView_Adapter(Item_list);
					Contact_RecyclerView.setAdapter(RecyclerView_Adapter);
					//					Log.d("onActivityResult: ", cNumber);
				} else {
					Toast.makeText(MainActivity.this, "Contact is already added", Toast.LENGTH_LONG)
						.show();
				}
			}
		}
	}

	public boolean Check_Contact_List(String Number) {
		Boolean check = false;
		for (int i = 0; i < Item_list.size(); i++) {
			if (TextUtils.equals(Item_list.get(i).getContact_Person_Number(), Number)) {
				check = true;
				//return true;
			} else {
				check = false;
			}
		}
		return check;
	}

	public void onclick_date() {
		datePicker.show();
	}

	public void Onclick_Image_button() {
		final EditText Image_Link;

		LayoutInflater LI = LayoutInflater.from(MainActivity.this);
		//View PromptsView=LI.inflate(R.layout.image_dialog,null);

		//Image_Link=(EditText)PromptsView.findViewById(R.id.);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

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


  /*  @Override
    protected Dialog onCreateDialog(int id){
        if(id==DIALOG_ID){
            return new DatePickerDialog(MainActivity.this,dateSetListener,year_x,month_x,day_x);
        }
        return null;
    }
*/

	private DatePickerDialog.OnDateSetListener dateSetListener =
		new DatePickerDialog.OnDateSetListener() {
			@Override public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
				year_x = year;
				month_x = month + 1;
				day_x = dayOfMonth;
				Selected_date = dayOfMonth;
				/*year_x = cal.get(Calendar.YEAR);
				day_x = cal.get(Calendar.DAY_OF_MONTH);
				month_x = cal.get(Calendar.MONTH);
                     if(year==cal.get(Calendar.YEAR) && month==cal.get(Calendar.MONTH) && dayOfMonth==cal.get(Calendar.DAY_OF_MONTH) ){
                         dateEdit.setText("Today");
                     } else if(year==cal.get(Calendar.YEAR) && month==cal.get(Calendar.MONTH) && dayOfMonth==cal.get(Calendar.DAY_OF_MONTH)+1 ){
                         dateEdit.setText("Tomorrow");
                     }else if(year==cal.get(Calendar.YEAR) && month==cal.get(Calendar.MONTH) && dayOfMonth==cal.get(Calendar.DAY_OF_MONTH)-1 ){
                         dateEdit.setText("Yesterday");
                     }else{ dateEdit.setText(dayOfMonth+" "+Months[month]+" "+year);}*/
				dateEdit.setText(year_x + "-" + month_x + "-" + day_x);
			}
		};

	public void Submit() {
		Amount_text = AmountEdit.getText().toString();
		//Add_Person_text = AutoCompleteContacts.getText().toString();
		Title_text = TitleEdit.getText().toString();
		Category_text = adapter.getLastCategory();
		View focus = null;
		Boolean cancel = false;
		if (groupModel == null && TextUtils.isEmpty(Category_text)) {
			Snackbar.make(findViewById(R.id.myCoordinaterLayout), "Category can't be empty",
				Snackbar.LENGTH_LONG).setAction("Action", null).show();
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
						Toast.makeText(MainActivity.this,
							"Contact is already present in the database", Toast.LENGTH_LONG).show();
					} else {
						Contactsdbhelper mdbhelper = new Contactsdbhelper(MainActivity.this);
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
							Toast.makeText(MainActivity.this, "Error with saving Contact",
								Toast.LENGTH_SHORT).show();
						} else {
							// Otherwise, the insertion was successful and we can display a toast with the row COLUMN_ONLINE_ID.
							Toast.makeText(MainActivity.this,
								"Contact saved with row id: " + newRowId, Toast.LENGTH_SHORT)
								.show();
						}
					}
				}

				IncomeModel expenseModel = new IncomeModel();

				if (TextUtils.isEmpty(CardClicked)) {
					String DateEdit_text = dateEdit.getText().toString();
					//Custom_items items = new Custom_items(Category_text,
					//      Title_text, "Rs " + Amount_text, day_x + " " + Months[month_x - 1] + " " + year_x);
					//customlist.add(items);

					SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
					GroupModel groupModel2 = new GroupModel();

					if (groupModel == null) {

						groupModel2.setGroupName(Title_text);
						groupModel2.setTypeId(1);
						//groupModel.setGroupDesc();
						groupModel2.setUsers(users);
						long newrowId = mDbHelper.createGroup(groupModel2);
						new ServerUtil(MainActivity.this).createGroup(groupModel2);
						groupModel2.setId(newrowId);
					}
					expenseModel.setType(Category_text);
					expenseModel.setTitle(Title_text);
					expenseModel.setTotalAmount(Amount_text);
					expenseModel.setPaidAtDate(DateEdit_text);
					expenseModel.setAmountDue(Amount_text);
					expenseModel.setGroupId(groupModel2.getId());
					try {
						expenseModel.setUuid(Util.generateUuid(prefs.getString("userid", null)));
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					} catch (NoSuchAlgorithmException e) {
						e.printStackTrace();
					}
					expenseModel.setPayerId(payerId);

					if (groupModel != null) {
						expenseModel.setGroupId(groupModel.getGroupId());
						expenseModel.setType("gt");
					}

					long rowId = mDbHelper.createEntry(expenseModel);
					new ServerUtil(MainActivity.this).createEntry(expenseModel);
					expenseModel.setId(rowId);

					//if (NetworkUtil.hasInternetConnection(MainActivity.this)) {
					//	new ServerUtil(MainActivity.this).createEntry(expenseModel);
					//} else {
					//	mDbHelper.addToTemp(rowId, 0, "new");
					//}
					//TransactionDbHelper mdbhelper = new TransactionDbHelper(MainActivity.this);
					//SQLiteDatabase db = mdbhelper.getWritableDatabase();
					//ContentValues values = new ContentValues();
					////values.put(recipe_entry.Column_Recipe_Image,byteimage);
					//values.put(TransactionDbContract.Transaction_Entry.COLUMN_TITLE, Title_text);
					//values.put(TransactionDbContract.Transaction_Entry.COLUMN_CATEGORY,
					//	Category_text);
					//values.put(TransactionDbContract.Transaction_Entry.COLUMN_AMOUNT,
					//	"Rs " + Amount_text);
					//Log.d("Date created", DateEdit_text);
					//values.put(TransactionDbContract.Transaction_Entry.COLUMN_DATE_CREATED,
					//	DateEdit_text);
					//values.put(recipe_entry.Column_Recipe_Nutri_label,Nlabel);
					//values.put(recipe_entry.Column_Recipe_Nutri_Quantity,Nquantity);
					//long newRowId =
					//	db.insert(TransactionDbContract.Transaction_Entry.TABLE_NAME, null, values);
					//if (newRowId == -1) {
					//	// If the row COLUMN_ONLINE_ID is -1, then there was an error with insertion.
					//	Toast.makeText(MainActivity.this, "Error with saving pet", Toast.LENGTH_SHORT)
					//		.show();
					//} else {
					//	// Otherwise, the insertion was successful and we can display a toast with the row COLUMN_ONLINE_ID.
					//	Toast.makeText(MainActivity.this, "Recipe saved with row id: " + newRowId,
					//		Toast.LENGTH_SHORT).show();
					//	new ServerUtil(MainActivity.this).createEntry(expenseModel);
					//}
					Bundle args = new Bundle();
					//args.putParcelableArrayList("ARRAYLIST",(ArrayList<? extends Parcelable>) customlist);
					Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();
				} else {
					Updated_Title = TitleEdit.getText().toString();
					Updated_Category = Category_text;
					Updated_Amount = AmountEdit.getText().toString();
					Updated_Date = dateEdit.getText().toString();

					model.setPaidAtDate(Updated_Date);
					model.setTotalAmount(Updated_Amount);
					model.setTitle(Updated_Title);
					model.setCatId(Updated_Category);

					mDbHelper.updateEntry(pos, model);
					if (NetworkUtil.hasInternetConnection(MainActivity.this)) {
						new ServerUtil(MainActivity.this).updateEntry(model);
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
					setResult(Activity.RESULT_OK, intent);
				}

				finish();
			}
		}
	}

	public void Delete_Button() {
		if (NetworkUtil.hasInternetConnection(MainActivity.this)) {
			new ServerUtil(MainActivity.this).deleteEntry(model.getOnlineId());
		} else {
			mDbHelper.addToTemp(model.getId(), model.getOnlineId(), "delete");
		}

		mDbHelper.deleteEntry(pos, model.getId());
		finish();
	}

	public void MoreButton() {
		/*Fetch_Contacts();
		//th.run();
		if (Notes_Layout.getVisibility() == View.GONE) {
			Notes_Layout.setVisibility(View.VISIBLE);
			Material_Amount_Due.setVisibility(View.VISIBLE);
			if (!TextUtils.isEmpty(AmountEdit.getText().toString())) {
				Amount_Due_Edit.setText(AmountEdit.getText().toString());
			} else {
				Amount_Due_Edit.setText("");
			}

            /*YoYo.with(Techniques.SlideInLeft)
                    .duration(1000)
                    .repeat(0)
                    .playOn(Notes_Layout);*
			Material_Notes.setHasFocus(true);
			Material_Amount_Due.setHasFocus(true);
			// More_Button.setImageDrawable(getResources().getDrawable(R.drawable.uparrow));

		} else {
			Material_Notes.setHasFocus(false);
			Material_Amount_Due.setHasFocus(false);
			Notes_Layout.setVisibility(View.GONE);
			Material_Amount_Due.setVisibility(View.GONE);
			//More_Button.setImageDrawable(getResources().getDrawable(R.drawable.downarrow));
           /* YoYo.with(Techniques.SlideOutLeft)
                    .duration(1000)
                    .repeat(0)
                    .playOn(Notes_Layout);
            final Handler handler3 = new Handler();
            handler3.postDelayed(new Runnable() {
                @Override
                public void run() {

                    Notes_Layout.setVisibility(View.GONE);
                    More_Button.setImageDrawable(getResources().getDrawable(R.drawable.downarrow));
                }
            }, 500);

		}*/
	}

	public void OnBackPressed(View view) {
		Intent intent = new Intent(MainActivity.this, Main2Activity.class);
		startActivity(intent);
	}

	public void Contact_Button(View view) {
		Intent intent =
			new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
		startActivityForResult(intent, 3);
	}

	@Override public void onRequestPermissionsResult(int requestCode, String permissions[],
		int[] grantResults) {
		switch (requestCode) {
			case 100: {

				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
					&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {

					// permission was granted, yay! Do the
					// contacts-related task you need to do.
				} else {

					// permission denied, boo! Disable the
					// functionality that depends on MainActivity.this permission.
					Toast.makeText(MainActivity.this,
						"Permission denied to read your External storage", Toast.LENGTH_SHORT)
						.show();
				}
				return;
			}

			// other 'case' lines to check for other
			// permissions MainActivity.this app might request
		}
	}

	public void Fetch_Contacts() {
		ContentResolver cr = MainActivity.this.getContentResolver();
		Cursor phones = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		//phones.moveToFirst();
		while (phones.moveToNext()) {
			String name =
				phones.getString(phones.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			//String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			//Toast.makeText(getApplicationContext(),name, Toast.LENGTH_LONG).show();
			Contact_list.add(name);
			phones.moveToNext();
		}
		phones.close();

		Contact_Names = new String[Contact_list.size()];
		for (int i = 0; i < Contact_Names.length; i++) {
			Contact_Names[i] = Contact_list.get(i);
		}
		// Toast.makeText(getApplicationContext(),"Number of contacts present "+Contact_list.size(), Toast.LENGTH_LONG).show();
		//Toast.makeText(getApplicationContext(),"last contact :"+Contact_Names[199], Toast.LENGTH_LONG).show();

	}

	public static String extractNumber(final String str) {

		if (str == null || str.isEmpty()) return "";

		StringBuilder sb = new StringBuilder();
		boolean found = false;
		for (char c : str.toCharArray()) {
			if (Character.isDigit(c)) {
				sb.append(c);
				found = true;
			} else if (found) {
				// If we already found a digit before and MainActivity.this char is not a digit, stop looping
				break;
			}
		}

		return sb.toString();
	}

	public TextView createContactTextView(String text) {
		//creating textview dynamically
		TextView tv = new TextView(MainActivity.this);
		tv.setText(text);
		tv.setTextSize(20);
		tv.setBackgroundResource(R.color.newdarkblue);
		//tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_clear_search_api_holo_light, 0);
		return tv;
	}

	public static Object convertViewToDrawable(View view) {
		int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		view.measure(spec, spec);
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		Bitmap b = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
			Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		c.translate(-view.getScrollX(), -view.getScrollY());
		view.draw(c);
		view.setDrawingCacheEnabled(true);
		Bitmap cacheBmp = view.getDrawingCache();
		Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
		view.destroyDrawingCache();
		return new BitmapDrawable(viewBmp);
	}

	public Boolean Check_Contacts(String contactname, String contactnumber) {
		ArrayList<Custom_Contact_items> Item_list = new ArrayList<>();
		Contactsdbhelper mDbHelper = new Contactsdbhelper(MainActivity.this);
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

	@Override public void updateUserId(String userId) {
		payerId = userId;
		users += "," + userId;
	}










/*
    Thread th= new Thread(){
        @Override
        public void run() {
            final String[] COUNTRIES = new String[]{
                    "balanced", "high-protein", "high-fiber", "low-fat", "low-carb", "low-sodium"};
            ArrayAdapter<String> contactAdapter=new ArrayAdapter<String>(MainActivity.MainActivity.this,android.R.layout.simple_dropdown_item_1line,Contact_Names);
            AutoCompleteTextView autoCompleteContacts=(AutoCompleteTextView)findViewById(R.id.Notes_Edit);

            autoCompleteContacts.setThreshold(1);
            autoCompleteContacts.setAdapter(contactAdapter);
        }
    };

    private CheckBox.OnCheckedChangeListener checkedChangeListener=
            new CheckBox.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked==true){
                        if(buttonView.getParent().equals(Clothing)){
                            Category_text="Clothing";
                            Clothing.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                            Entertainment.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Food.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Fuel.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Health.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Salary.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            More.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                        }
                        if(buttonView.getParent().equals(Entertainment)){
                            Category_text="Entertainment";
                            Entertainment.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                            Clothing.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Food.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Fuel.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Health.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Salary.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            More.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                        }
                        if(buttonView.getParent().equals(Food)){
                            Category_text="Food";
                            Food.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                            Entertainment.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Clothing.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Fuel.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Health.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Salary.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            More.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                        }
                        if(buttonView.getParent().equals(Fuel)){
                            Category_text="Fuel";
                            Fuel.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                            Entertainment.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Food.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Clothing.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Health.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Salary.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            More.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                        }
                        if(buttonView.getParent().equals(Health)){
                            Category_text="Health";
                            Health.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                            Entertainment.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Food.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Fuel.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Clothing.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Salary.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            More.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                        }
                        if(buttonView.getParent().equals(Salary)){
                            Category_text="Salary";
                            Salary.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                            Entertainment.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Food.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Fuel.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Health.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Clothing.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            More.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                        }
                        if(buttonView.getParent().equals(More)){

                            More.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                            Entertainment.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Food.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Fuel.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Health.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Salary.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            More.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                        }
                    }else{
                        if(buttonView.getParent().equals(Clothing)){
                            Category_text="";
                            Clothing.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                     /*  Entertainment.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));*/

                      /*  if(buttonView.getParent().equals(Entertainment)){
                            Category_text="";
                            Entertainment.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                      /* Clothing.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));*/
                       /* }
                        if(buttonView.getParent().equals(Food)){
                            Category_text="";
                            Food.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                       /*Entertainment.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Clothing.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));*/
                       /* }
                        if(buttonView.getParent().equals(Fuel)){
                            Category_text="";
                            Fuel.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                  /*     Entertainment.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Clothing.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));*/
                       /* }
                        if(buttonView.getParent().equals(Health)){
                            Category_text="";
                            Health.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                       /*Entertainment.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Clothing.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));*/
                      /*  }
                        if(buttonView.getParent().equals(Salary)){
                            Category_text="";
                            Salary.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                       /*Entertainment.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Clothing.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));*/
                      /*  }
                        if(buttonView.getParent().equals(More)){
                            Category_text="";
                            More.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                      /* Entertainment.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Clothing.setBackgroundTintList(MainActivity.MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));*/
                     /*   }
                    }


                }
            };

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        //callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode==RESULT_OK) {

            //Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri uri = data.getData();
            Img_File.setVisibility(View.VISIBLE);
            Img_File.setImageURI(uri);
            Image_uri=uri;
        }else if (requestCode == 1 && resultCode==RESULT_OK) {
            //Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri uri = data.getData();
            Img_File.setVisibility(View.VISIBLE);
            Image_uri=uri;
            Img_File.setVisibility(View.VISIBLE);
            Img_File.setImageURI(uri);

        }else if (requestCode == 3 && resultCode==RESULT_OK) {
            //Bitmap photo = (Bitmap) data.getExtras().get("data");
           // Uri uri = data.getData();
            Uri contactData = data.getData();
            Cursor c = managedQuery(contactData, null, null, null, null);
            if (c.moveToFirst()) {
                String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                if(TextUtils.isEmpty(AutoCompleteContacts.getText().toString())){
                    AutoCompleteContacts.setText(name);
                    //Material_Notes.setHasFocus(true);
                }else {
                    AutoCompleteContacts.append(" , " + name);
                    //Material_Notes.setHasFocus(true);}
                }
            }
        }
    }


    public void onclick_date(View view){

        showDialog(DIALOG_ID);
    }

    public void Onclick_Image_button(View view){
        final EditText Image_Link;

        LayoutInflater LI=LayoutInflater.from(MainActivity.MainActivity.this);
        //View PromptsView=LI.inflate(R.layout.image_dialog,null);

        //Image_Link=(EditText)PromptsView.findViewById(R.id.);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.MainActivity.this);

        //alertDialogBuilder.setView(PromptsView);

        alertDialogBuilder.setCancelable(true)
                .setTitle(Html.fromHtml("<font color='#3F51B5'>Choose an Image from</font>"))
                .setPositiveButton(Html.fromHtml("<font color='#3F51B5'>camera</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent,2);
                        //mEditor.insertImage(Image_Link.getText().toString(),"Image");
                        //mEditor.insertLink(Href_Link.getText().toString(), Href_Title.getText().toString());

                    }
                })
                .setNegativeButton(Html.fromHtml("<font color='#3F51B5'>Gallery</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                        startActivityForResult(i, 1);
                    }
                });

        AlertDialog alertDialog=alertDialogBuilder.create();
        alertDialog.show();
    }

     @Override
     protected Dialog onCreateDialog(int id){
        if(id==DIALOG_ID){
            return new DatePickerDialog(MainActivity.this,dateSetListener,year_x,month_x,day_x);
        }
        return null;
     }



     private DatePickerDialog.OnDateSetListener dateSetListener =
             new DatePickerDialog.OnDateSetListener() {
                 @Override
                 public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                     year_x=year;
                     month_x=month+1;
                     day_x=dayOfMonth;
                     Selected_date=dayOfMonth;
                     year_x=cal.get(Calendar.YEAR);
                     day_x=cal.get(Calendar.DAY_OF_MONTH);
                     month_x=cal.get(Calendar.MONTH);
                     /*if(year==cal.get(Calendar.YEAR) && month==cal.get(Calendar.MONTH) && dayOfMonth==cal.get(Calendar.DAY_OF_MONTH) ){
                         dateEdit.setText("Today");
                     } else if(year==cal.get(Calendar.YEAR) && month==cal.get(Calendar.MONTH) && dayOfMonth==cal.get(Calendar.DAY_OF_MONTH)+1 ){
                         dateEdit.setText("Tomorrow");
                     }else if(year==cal.get(Calendar.YEAR) && month==cal.get(Calendar.MONTH) && dayOfMonth==cal.get(Calendar.DAY_OF_MONTH)-1 ){
                         dateEdit.setText("Yesterday");
                     }else{ dateEdit.setText(dayOfMonth+" "+Months[month]+" "+year);}*/
                    /* dateEdit.setText(dayOfMonth+" "+Months[month]+" "+year);
                     }
             };












    public void Submit(View view){
        Amount_text=AmountEdit.getText().toString();
        Add_Person_text=AutoCompleteContacts.getText().toString();
        Title_text=TitleEdit.getText().toString();
        View focus=null;
        Boolean cancel=false;
        if(TextUtils.isEmpty(Category_text)){
            Snackbar.make(view, "Category can't be empty", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
        }else {
            if (day_x == 0) {
                dateEdit.setError("Date can't be empty");
                focus = dateEdit;
                cancel = true;
            }
            if (!TextUtils.isDigitsOnly(Amount_text)) {
                AmountEdit.setError("Amount can't be empty");
                focus = AmountEdit;
                cancel = true;
            }
            if(TextUtils.isEmpty(Title_text)){
                TitleEdit.setError("Title can't be empty");
                focus=TitleEdit;
                cancel=true;
            }

            if (cancel == true) {
                focus.requestFocus();

            } else {
                if (TextUtils.isEmpty(CardClicked)) {
                    String DateEdit_text = dateEdit.getText().toString();
                    //Custom_items items = new Custom_items(Category_text,
                      //      Title_text, "Rs " + Amount_text, day_x + " " + Months[month_x - 1] + " " + year_x);
                    //customlist.add(items);
                    TransactionDbHelper mdbhelper = new TransactionDbHelper(MainActivity.this);
                    SQLiteDatabase db = mdbhelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    //values.put(recipe_entry.Column_Recipe_Image,byteimage);
                    values.put(Transaction_Entry.COLUMN_TITLE, Title_text);
                    values.put(Transaction_Entry.COLUMN_CATEGORY, Category_text);
                    values.put(Transaction_Entry.COLUMN_AMOUNT, "Rs " + Amount_text);
                    values.put(Transaction_Entry.COLUMN_DATE_CREATED, DateEdit_text);
                    //values.put(recipe_entry.Column_Recipe_Nutri_label,Nlabel);
                    //values.put(recipe_entry.Column_Recipe_Nutri_Quantity,Nquantity);
                    long newRowId = db.insert(Transaction_Entry.TABLE_NAME, null, values);
                    if (newRowId == -1) {
                        // If the row COLUMN_ONLINE_ID is -1, then there was an error with insertion.
                        Toast.makeText(MainActivity.this, "Error with saving pet", Toast.LENGTH_SHORT).show();
                    } else {
                        // Otherwise, the insertion was successful and we can display a toast with the row COLUMN_ONLINE_ID.
                        Toast.makeText(MainActivity.this, "Recipe saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
                    }
                    Bundle args = new Bundle();
                    //args.putParcelableArrayList("ARRAYLIST",(ArrayList<? extends Parcelable>) customlist);
                    Toast.makeText(MainActivity.MainActivity.this, "Success", Toast.LENGTH_LONG).show();

                }else{
                    Updated_Title=TitleEdit.getText().toString();
                    Updated_Category=Category_text;
                    Updated_Amount=AmountEdit.getText().toString();
                    Updated_Date=dateEdit.getText().toString();

                    TransactionDbHelper mdbhelper = new TransactionDbHelper(MainActivity.this);
                    SQLiteDatabase db = mdbhelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    //values.put(recipe_entry.Column_Recipe_Image,byteimage);
                    values.put(Transaction_Entry.COLUMN_TITLE, Updated_Title);
                    values.put(Transaction_Entry.COLUMN_CATEGORY, Updated_Category);
                    values.put(Transaction_Entry.COLUMN_AMOUNT, "Rs " + Updated_Amount);
                    values.put(Transaction_Entry.COLUMN_DATE_CREATED, Updated_Date);
                    db.update(Transaction_Entry.TABLE_NAME,values,Transaction_Entry._id+"="+Updated_Id,null);

                }

                Intent intent = new Intent(MainActivity.MainActivity.this, Main2Activity.class);
                startActivity(intent);

            }
        }
    }


    public void Delete_Button(View view){
        TransactionDbHelper mdbhelper = new TransactionDbHelper(MainActivity.this);
        SQLiteDatabase db = mdbhelper.getWritableDatabase();
       // ContentValues values = new ContentValues();
        db.delete(Transaction_Entry.TABLE_NAME, Transaction_Entry._id + " = ?", new String[]{""+Updated_Id});
        Intent intent = new Intent(MainActivity.MainActivity.this, Main2Activity.class);
        startActivity(intent);

    }



 public void MoreButton(View view){
     Fetch_Contacts();
     th.run();
        if(Notes_Layout.getVisibility()==View.GONE){
            Notes_Layout.setVisibility(View.VISIBLE);
            Material_Amount_Due.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(AmountEdit.getText().toString())) {
                Amount_Due_Edit.setText(AmountEdit.getText().toString());
            }else{Amount_Due_Edit.setText("");}
            /*YoYo.with(Techniques.SlideInLeft)
                    .duration(1000)
                    .repeat(0)
                    .playOn(Notes_Layout);*/
           /* Material_Notes.setHasFocus(true);
            Material_Amount_Due.setHasFocus(true);
           // More_Button.setImageDrawable(getResources().getDrawable(R.drawable.uparrow));

        }else{
            Material_Notes.setHasFocus(false);
            Material_Amount_Due.setHasFocus(false);
            Notes_Layout.setVisibility(View.GONE);
            Material_Amount_Due.setVisibility(View.GONE);
            //More_Button.setImageDrawable(getResources().getDrawable(R.drawable.downarrow));
           /* YoYo.with(Techniques.SlideOutLeft)
                    .duration(1000)
                    .repeat(0)
                    .playOn(Notes_Layout);
            final Handler handler3 = new Handler();
            handler3.postDelayed(new Runnable() {
                @Override
                public void run() {

                    Notes_Layout.setVisibility(View.GONE);
                    More_Button.setImageDrawable(getResources().getDrawable(R.drawable.downarrow));
                }
            }, 500);*/

	/* }
}


public void Contact_Button(View view){
  Intent intent= new Intent(Intent.ACTION_PICK,  ContactsContract.Contacts.CONTENT_URI);

  startActivityForResult(intent, 3);
}

 @Override
 public void onRequestPermissionsResult(int requestCode,
										String permissions[], int[] grantResults) {
	 switch (requestCode) {
		 case 100: {

			 // If request is cancelled, the result arrays are empty.
			 if (grantResults.length > 0
					 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

				 // permission was granted, yay! Do the
				 // contacts-related task you need to do.
			 } else {

				 // permission denied, boo! Disable the
				 // functionality that depends on MainActivity.this permission.
				 Toast.makeText(MainActivity.MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
			 }
			 return;
		 }

		 // other 'case' lines to check for other
		 // permissions MainActivity.this app might request
	 }
 }



 public void Fetch_Contacts(){
	 ContentResolver cr = getContentResolver();
  Cursor phones = cr.query(ContactsContract.Contacts.CONTENT_URI, null,null,null, null);
  //phones.moveToFirst();
  while (phones.moveToNext())
  {
	  String name=phones.getString(phones.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
	  //String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
	  //Toast.makeText(getApplicationContext(),name, Toast.LENGTH_LONG).show();
	  Contact_list.add(name);
	  phones.moveToNext();
  }
  phones.close();

  Contact_Names=new String[200];
  for(int i=0;i<200;i++){
	  Contact_Names[i]=Contact_list.get(i);
  }
	// Toast.makeText(getApplicationContext(),"Number of contacts present "+Contact_list.size(), Toast.LENGTH_LONG).show();
  //Toast.makeText(getApplicationContext(),"last contact :"+Contact_Names[199], Toast.LENGTH_LONG).show();

}


*/
}