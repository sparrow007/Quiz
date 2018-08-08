package android.zersey.expense_manger;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
	implements Expense_Form.OnFragmentInteractionListener,
	Income_form.OnFragmentInteractionListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link FragmentPagerAdapter} derivative, which will keep every
	 * loaded fragment in memory. If this becomes too memory intensive, it
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
	private int year_x, month_x, day_x, Selected_date = 0, Updated_Id;
	private String Category_text, Notes_text, Amount_text, Title_text;
	private Uri Image_uri = null;
	private static int DIALOG_ID = 0;
	private EditText dateEdit, AmountEdit, TitleEdit, Amount_Due_Edit;
	private String CardClicked, Updated_Category, Updated_Title, Updated_Amount, Updated_Date;
	private Calendar cal;
	private String[] Months = {
		"Jan", "Feb", "March", "April", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"
	}, Contact_Names;
	private LinearLayout Clothing, Entertainment, Food, Fuel, Health, Salary, More, Notes_Layout;
	private CheckBox Clothing_checkbox, Entertainment_checkbox, Food_checkbox, Fuel_checkbox,
		Health_checkbox, Salary_checkbox, More_checkbox;
	private ViewPager mViewPager;
	private pageradapter adapter;
	private String Updated_Type = "";

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//getSupportActionBar().setTitle("Expense Manager");
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		Dexter.withActivity(this)
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


        /*Contact_list=new ArrayList<String>();

        //customlist=(ArrayList<Custom_items>)getIntent().getBundleExtra("Bundle").getSerializable("ARRAYLIST");
        Material_Title=(MaterialTextField)findViewById(R.id.Material_Title);
        Material_Title.setHasFocus(true);
        Material_Amount=(MaterialTextField)findViewById(R.id.Material_Amount);
        Material_Amount.setHasFocus(true);
        Material_Date=(MaterialTextField)findViewById(R.id.Material_Date);
        Material_Date.setHasFocus(true);
        Material_Notes=(MaterialTextField)findViewById(R.id.Material_Notes);
        Notes_Layout=(LinearLayout)findViewById(R.id.Notes_Layout);
        Material_Amount_Due=(MaterialTextField)findViewById(R.id.Material_Amount_Due);
        Material_Amount_Due.setVisibility(View.GONE);

        Notes_Layout.setVisibility(View.GONE);
        cal=Calendar.getInstance();
        year_x=cal.get(Calendar.YEAR);
        day_x=cal.get(Calendar.DAY_OF_MONTH);
        month_x=cal.get(Calendar.MONTH);
        More_Button=(TextView) findViewById(R.id.MoreButton);
        Delete_Button=(ImageButton)findViewById(R.id.Delete_Button);
        Delete_Button.setVisibility(View.GONE);
        //requestPermissions(Manifest.permission.CAMERA,1111);
        Img_File=(ImageView)findViewById(R.id.Img_file);
        Img_File.setVisibility(View.GONE);
        dateEdit=(EditText)findViewById(R.id.Date_Edit);
        dateEdit.setText(day_x+" "+Months[month_x]+" "+year_x);
        TitleEdit=(EditText)findViewById(R.id.Title_Edit);
        AmountEdit=(EditText)findViewById(R.id.Amount_Edit);
        Amount_Due_Edit=(EditText)findViewById(R.id.Amount_Due_Edit);
        AutoCompleteContacts=(AutoCompleteTextView) findViewById(R.id.Notes_Edit);
        Clothing=(LinearLayout)findViewById(R.id.Clothing_layout);
        Entertainment=(LinearLayout)findViewById(R.id.Entertainment_layout);
        Food=(LinearLayout)findViewById(R.id.food_layout);
        Fuel=(LinearLayout)findViewById(R.id.fuel_layout);
        Health=(LinearLayout)findViewById(R.id.Health_layout);
        Salary=(LinearLayout)findViewById(R.id.Salary_layout);
        More=(LinearLayout)findViewById(R.id.More_layout);
        Clothing_checkbox=(CheckBox)findViewById(R.id.Clothing_checkbox);
        Entertainment_checkbox=(CheckBox)findViewById(R.id.Entertainment_checkbox);
        Food_checkbox=(CheckBox)findViewById(R.id.Food_checkbox);
        Fuel_checkbox=(CheckBox)findViewById(R.id.Fuel_checkbox);
        Health_checkbox=(CheckBox)findViewById(R.id.Health_checkbox);
        Salary_checkbox=(CheckBox)findViewById(R.id.Salary_checkbox);
        More_checkbox=(CheckBox)findViewById(R.id.More_checkbox);
        Category_text_view=(TextView)findViewById(R.id.Category_text_view);

        Clothing_checkbox.setOnCheckedChangeListener(checkedChangeListener);
        Entertainment_checkbox.setOnCheckedChangeListener(checkedChangeListener);
        Food_checkbox.setOnCheckedChangeListener(checkedChangeListener);
        Fuel_checkbox.setOnCheckedChangeListener(checkedChangeListener);
        Health_checkbox.setOnCheckedChangeListener(checkedChangeListener);
        Salary_checkbox.setOnCheckedChangeListener(checkedChangeListener);
        More_checkbox.setOnCheckedChangeListener(checkedChangeListener);*/

		TabLayout tab_layout = (TabLayout) findViewById(R.id.form_tabLayout);
		tab_layout.addTab(tab_layout.newTab().setText("Income"));
		tab_layout.addTab(tab_layout.newTab().setText("Expense"));
		tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);

		mViewPager = (ViewPager) findViewById(R.id.form_viewPager);

		CardClicked = getIntent().getStringExtra("CardClicked");
		if (!TextUtils.isEmpty(CardClicked)) {
          /*  Delete_Button.setVisibility(View.VISIBLE);
            Material_Title.setHasFocus(true);
            Material_Amount.setHasFocus(true);
            Material_Date.setHasFocus(true);*/
			Updated_Category = getIntent().getStringExtra("Category");
			Updated_Amount = getIntent().getStringExtra("Amount");
			Updated_Title = getIntent().getStringExtra("Title");
			Updated_Date = getIntent().getStringExtra("DateCreated");
			Updated_Type = getIntent().getStringExtra("Type");
			Updated_Id = getIntent().getIntExtra("_ID", 0);
            /*if ("Clothing".equals(Updated_Category)) {
                Clothing.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                Category_text = "Clothing";
            } else if ("Entertainment".equals(Updated_Category)) {
                Entertainment.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                Category_text = "Entertainment";
            } else if ("Food".equals(Updated_Category)) {
                Food.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                Category_text = "Food";
            } else if ("Fuel".equals(Updated_Category)) {
                Fuel.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                Category_text = "Fuel";
            } else if ("Health".equals(Updated_Category)) {
                Health.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                Category_text = "Health";
            } else if ("Salary".equals(Updated_Category)) {
                Salary.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                Category_text = "Salary";
            }*/
			Updated_Amount = Updated_Amount.replace("Rs ", "");
			Log.d("Rs replaced", Updated_Amount);
            /*AmountEdit.setText(Updated_Amount);
            TitleEdit.setText(Updated_Title);
            dateEdit.setText(Updated_Date);*/

			adapter =
				new pageradapter(getSupportFragmentManager(), tab_layout.getTabCount(), CardClicked,
					Updated_Title, Updated_Amount, Updated_Date, Updated_Category, Updated_Id);
		} else {
			adapter = new pageradapter(getSupportFragmentManager(), tab_layout.getTabCount());
		}

		mViewPager.setAdapter(adapter);
		mViewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layout));

		tab_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override public void onTabSelected(TabLayout.Tab tab) {
				mViewPager.setCurrentItem(tab.getPosition());
			}

			@Override public void onTabUnselected(TabLayout.Tab tab) {

			}

			@Override public void onTabReselected(TabLayout.Tab tab) {

			}
		});
		//Fetch_Contacts();
		//Toast.makeText(this, Contact_list.size()+" Contact Names", Toast.LENGTH_SHORT).show();

		//th.run();
		if (!TextUtils.isEmpty(CardClicked)) {
			if (Util.isEmpty(Updated_Category)) {
				mViewPager.setCurrentItem(0);
			} else {
				mViewPager.setCurrentItem(1);
			}
		}
	}

	@Override public void onFragmentInteraction(Uri uri) {

	}
/*
    Thread th= new Thread(){
        @Override
        public void run() {
            final String[] COUNTRIES = new String[]{
                    "balanced", "high-protein", "high-fiber", "low-fat", "low-carb", "low-sodium"};
            ArrayAdapter<String> contactAdapter=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_dropdown_item_1line,Contact_Names);
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
                            Clothing.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                            Entertainment.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Food.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Fuel.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Health.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Salary.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            More.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                        }
                        if(buttonView.getParent().equals(Entertainment)){
                            Category_text="Entertainment";
                            Entertainment.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                            Clothing.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Food.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Fuel.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Health.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Salary.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            More.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                        }
                        if(buttonView.getParent().equals(Food)){
                            Category_text="Food";
                            Food.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                            Entertainment.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Clothing.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Fuel.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Health.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Salary.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            More.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                        }
                        if(buttonView.getParent().equals(Fuel)){
                            Category_text="Fuel";
                            Fuel.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                            Entertainment.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Food.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Clothing.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Health.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Salary.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            More.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                        }
                        if(buttonView.getParent().equals(Health)){
                            Category_text="Health";
                            Health.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                            Entertainment.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Food.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Fuel.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Clothing.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            Salary.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                            More.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                        }
                        if(buttonView.getParent().equals(Salary)){
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
                    }else{
                        if(buttonView.getParent().equals(Clothing)){
                            Category_text="";
                            Clothing.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                     /*  Entertainment.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));*/

                      /*  if(buttonView.getParent().equals(Entertainment)){
                            Category_text="";
                            Entertainment.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                      /* Clothing.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));*/
                       /* }
                        if(buttonView.getParent().equals(Food)){
                            Category_text="";
                            Food.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                       /*Entertainment.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Clothing.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));*/
                       /* }
                        if(buttonView.getParent().equals(Fuel)){
                            Category_text="";
                            Fuel.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                  /*     Entertainment.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Clothing.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));*/
                       /* }
                        if(buttonView.getParent().equals(Health)){
                            Category_text="";
                            Health.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                       /*Entertainment.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Clothing.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));*/
                      /*  }
                        if(buttonView.getParent().equals(Salary)){
                            Category_text="";
                            Salary.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                       /*Entertainment.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Clothing.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));*/
                      /*  }
                        if(buttonView.getParent().equals(More)){
                            Category_text="";
                            More.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                      /* Entertainment.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Clothing.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));*/
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

        LayoutInflater LI=LayoutInflater.from(MainActivity.this);
        //View PromptsView=LI.inflate(R.layout.image_dialog,null);

        //Image_Link=(EditText)PromptsView.findViewById(R.id.);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

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
            return new DatePickerDialog(this,dateSetListener,year_x,month_x,day_x);
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
        Notes_text=AutoCompleteContacts.getText().toString();
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
                    TransactionDbHelper mdbhelper = new TransactionDbHelper(this);
                    SQLiteDatabase db = mdbhelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    //values.put(recipe_entry.Column_Recipe_Image,byteimage);
                    values.put(Transaction_Entry.COLUMN_TITLE, Title_text);
                    values.put(Transaction_Entry.COLUMN_CATEGORY, Category_text);
                    values.put(Transaction_Entry.COLUMN_AMOUNT, "Rs " + Amount_text);
                    Log.d("Date created", DateEdit_text);
                    values.put(Transaction_Entry.COLUMN_DATE_CREATED, DateEdit_text);
                    //values.put(recipe_entry.Column_Recipe_Nutri_label,Nlabel);
                    //values.put(recipe_entry.Column_Recipe_Nutri_Quantity,Nquantity);
                    long newRowId = db.insert(Transaction_Entry.TABLE_NAME, null, values);
                    if (newRowId == -1) {
                        // If the row ID is -1, then there was an error with insertion.
                        Toast.makeText(this, "Error with saving pet", Toast.LENGTH_SHORT).show();
                    } else {
                        // Otherwise, the insertion was successful and we can display a toast with the row ID.
                        Toast.makeText(this, "Recipe saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
                    }
                    Bundle args = new Bundle();
                    //args.putParcelableArrayList("ARRAYLIST",(ArrayList<? extends Parcelable>) customlist);
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();

                }else{
                    Updated_Title=TitleEdit.getText().toString();
                    Updated_Category=Category_text;
                    Updated_Amount=AmountEdit.getText().toString();
                    Updated_Date=dateEdit.getText().toString();

                    TransactionDbHelper mdbhelper = new TransactionDbHelper(this);
                    SQLiteDatabase db = mdbhelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    //values.put(recipe_entry.Column_Recipe_Image,byteimage);
                    values.put(Transaction_Entry.COLUMN_TITLE, Updated_Title);
                    values.put(Transaction_Entry.COLUMN_CATEGORY, Updated_Category);
                    values.put(Transaction_Entry.COLUMN_AMOUNT, "Rs " + Updated_Amount);
                    values.put(Transaction_Entry.COLUMN_DATE_CREATED, Updated_Date);
                    db.update(Transaction_Entry.TABLE_NAME,values,Transaction_Entry._id+"="+Updated_Id,null);

                }

                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);

            }
        }
    }


    public void Delete_Button(View view){
        TransactionDbHelper mdbhelper = new TransactionDbHelper(this);
        SQLiteDatabase db = mdbhelper.getWritableDatabase();
       // ContentValues values = new ContentValues();
        db.delete(Transaction_Entry.TABLE_NAME, Transaction_Entry._id + " = ?", new String[]{""+Updated_Id});
        Intent intent = new Intent(MainActivity.this, Main2Activity.class);
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
				 // functionality that depends on this permission.
				 Toast.makeText(MainActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
			 }
			 return;
		 }

		 // other 'case' lines to check for other
		 // permissions this app might request
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
	private class pageradapter extends FragmentPagerAdapter {
		int mnooftabes;

		public pageradapter(FragmentManager fm, int Numberoftabes) {
			super(fm);
			mnooftabes = Numberoftabes;
		}

		public pageradapter(FragmentManager fm, int Numberoftabes, String cardClicked,
			String updated_Title, String updated_Amount, String updated_Date,
			String updated_Category, int id) {
			super(fm);
			mnooftabes = Numberoftabes;
		}

		@Override public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class below).
			switch (position) {

				case 1:
					Expense_Form expense_form = new Expense_Form();
					if (Updated_Type.equalsIgnoreCase("expense")) {
						expense_form.setString(CardClicked, Updated_Title, Updated_Amount,
							Updated_Date, Updated_Category, Updated_Id);
						return expense_form;
					} else {
						return expense_form;
					}
				case 0:
					Income_form income_form = new Income_form();
					if (Updated_Type.equalsIgnoreCase("income")) {
						income_form.setString(Updated_Title, Updated_Amount, Updated_Date,
							Updated_Category, Updated_Id);
						return income_form;
					} else {
						return income_form;
					}
				default:
					return null;
			}

			//return PlaceholderFragment.newInstance(position + 1);
		}

		@Override public int getCount() {
			// Show 3 total pages.
			return mnooftabes;
		}
	}
}
