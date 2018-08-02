package android.zersey.expense_manger;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.zersey.expense_manger.Data.Transaction_contract;
import android.zersey.expense_manger.Data.Transactiondbhelper;

import com.github.florent37.materialtextfield.MaterialTextField;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Expense_Form.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Expense_Form#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Expense_Form extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DatePickerDialog datePicker;
    private View fragmentLayout;
    ImageView Img_File;
    private Button Submit_button;
    private TextView More_TextButton;
    private ImageButton Delete_Button,Camera_Button,Contact_Button;
    private AutoCompleteTextView AutoCompleteContacts;
    private ArrayAdapter<String> ContactAdapter;
    private MaterialTextField Material_Title,Material_Amount,Material_Date,Material_Notes,Material_Amount_Due;
    public View layout_view=null;
    private List<Custom_items> customlist;
    private ArrayList<String> Contact_list;
    private TextView Category_text_view;
    private int year_x,month_x,day_x,Selected_date=0,Updated_Id;
    private String Category_text,Notes_text,Amount_text,Title_text;
    private Uri Image_uri=null;
    private static int DIALOG_ID=0;
    private EditText dateEdit,AmountEdit,TitleEdit,Amount_Due_Edit;
    private String CardClicked,Updated_Category,Updated_Title,Updated_Amount,Updated_Date;
    private Calendar cal;
    private String[] Months={"Jan","Feb","March","April","May","June","July","Aug","Sept","Oct","Nov","Dec"},Contact_Names;
    private LinearLayout Clothing,Entertainment,Food,Fuel,Health,Salary,More,Notes_Layout;
    private CheckBox Clothing_checkbox,Entertainment_checkbox,Food_checkbox,Fuel_checkbox,Health_checkbox,Salary_checkbox,More_checkbox;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Expense_Form() {
        // Required empty public constructor
    }
    public void setString(String cardClicked,String updated_Title,String updated_Amount,
                          String updated_Date,String updated_Category,int id) {
        CardClicked=cardClicked;
        Updated_Title=updated_Title;
        Updated_Amount=updated_Amount;
        Updated_Date=updated_Date;
        Updated_Category=updated_Category;
        Updated_Id=id;
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Expense_Form.
     */
    // TODO: Rename and change types and number of parameters
    public static Expense_Form newInstance(String param1, String param2) {
        Expense_Form fragment = new Expense_Form();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentLayout=inflater.inflate(R.layout.content_main, container, false);



        Contact_list=new ArrayList<String>();

        //customlist=(ArrayList<Custom_items>)getIntent().getBundleExtra("Bundle").getSerializable("ARRAYLIST");
        Material_Title=(MaterialTextField)fragmentLayout.findViewById(R.id.Material_Title);
        Material_Title.setHasFocus(true);
        Material_Amount=(MaterialTextField)fragmentLayout.findViewById(R.id.Material_Amount);
        Material_Amount.setHasFocus(true);
        Material_Date=(MaterialTextField)fragmentLayout.findViewById(R.id.Material_Date);
        Material_Date.setHasFocus(true);
        Material_Notes=(MaterialTextField)fragmentLayout.findViewById(R.id.Material_Notes);
        Notes_Layout=(LinearLayout)fragmentLayout.findViewById(R.id.Notes_Layout);
        Material_Amount_Due=(MaterialTextField)fragmentLayout.findViewById(R.id.Material_Amount_Due);
        Material_Amount_Due.setVisibility(View.GONE);
        More_TextButton=(TextView)fragmentLayout.findViewById(R.id.MoreButton);
        Notes_Layout.setVisibility(View.GONE);
        cal=Calendar.getInstance();
        year_x=cal.get(Calendar.YEAR);
        day_x=cal.get(Calendar.DAY_OF_MONTH);
        month_x=cal.get(Calendar.MONTH);
        More_TextButton=(TextView) fragmentLayout.findViewById(R.id.MoreButton);
        Delete_Button=(ImageButton)fragmentLayout.findViewById(R.id.Delete_Button);
        Delete_Button.setVisibility(View.GONE);
        Contact_Button=(ImageButton)fragmentLayout.findViewById(R.id.Contact_Button);
        Camera_Button=(ImageButton)fragmentLayout.findViewById(R.id.Camera_Button);
        //requestPermissions(Manifest.permission.CAMERA,1111);
        Img_File=(ImageView)fragmentLayout.findViewById(R.id.Img_file);
        Img_File.setVisibility(View.GONE);
        dateEdit=(EditText)fragmentLayout.findViewById(R.id.Date_Edit);
        dateEdit.setText(day_x+" "+Months[month_x]+" "+year_x);
        TitleEdit=(EditText)fragmentLayout.findViewById(R.id.Title_Edit);
        AmountEdit=(EditText)fragmentLayout.findViewById(R.id.Amount_Edit);
        Amount_Due_Edit=(EditText)fragmentLayout.findViewById(R.id.Amount_Due_Edit);
        AutoCompleteContacts=(AutoCompleteTextView) fragmentLayout.findViewById(R.id.Notes_Edit);
        Clothing=(LinearLayout)fragmentLayout.findViewById(R.id.Clothing_layout);
        Entertainment=(LinearLayout)fragmentLayout.findViewById(R.id.Entertainment_layout);
        Food=(LinearLayout)fragmentLayout.findViewById(R.id.food_layout);
        Fuel=(LinearLayout)fragmentLayout.findViewById(R.id.fuel_layout);
        Health=(LinearLayout)fragmentLayout.findViewById(R.id.Health_layout);
        Salary=(LinearLayout)fragmentLayout.findViewById(R.id.Salary_layout);
        More=(LinearLayout)fragmentLayout.findViewById(R.id.More_layout);
        Clothing_checkbox=(CheckBox)fragmentLayout.findViewById(R.id.Clothing_checkbox);
        Entertainment_checkbox=(CheckBox)fragmentLayout.findViewById(R.id.Entertainment_checkbox);
        Food_checkbox=(CheckBox)fragmentLayout.findViewById(R.id.Food_checkbox);
        Fuel_checkbox=(CheckBox)fragmentLayout.findViewById(R.id.Fuel_checkbox);
        Health_checkbox=(CheckBox)fragmentLayout.findViewById(R.id.Health_checkbox);
        Salary_checkbox=(CheckBox)fragmentLayout.findViewById(R.id.Salary_checkbox);
        More_checkbox=(CheckBox)fragmentLayout.findViewById(R.id.More_checkbox);
        Category_text_view=(TextView)fragmentLayout.findViewById(R.id.Category_text_view);
        Submit_button=(Button)fragmentLayout.findViewById(R.id.Submit_Button);
        Clothing_checkbox.setOnCheckedChangeListener(checkedChangeListener);
        Entertainment_checkbox.setOnCheckedChangeListener(checkedChangeListener);
        Food_checkbox.setOnCheckedChangeListener(checkedChangeListener);
        Fuel_checkbox.setOnCheckedChangeListener(checkedChangeListener);
        Health_checkbox.setOnCheckedChangeListener(checkedChangeListener);
        Salary_checkbox.setOnCheckedChangeListener(checkedChangeListener);
        More_checkbox.setOnCheckedChangeListener(checkedChangeListener);

        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick_date();
            }
        });

        Delete_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Delete_Button();
            }
        });

        Contact_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contact_Button();
            }
        });

        Camera_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Onclick_Image_button();
            }
        });
        More_TextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoreButton();
            }
        });
        Submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Submit();
            }
        });


        datePicker = new DatePickerDialog(getContext(),
                R.style.Theme_AppCompat_DayNight_Dialog, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        datePicker.setCancelable(true);
        datePicker.setTitle("Select Date");
        
        
        
        if (!TextUtils.isEmpty(CardClicked)) {
            Delete_Button.setVisibility(View.VISIBLE);
            Material_Title.setHasFocus(true);
            Material_Amount.setHasFocus(true);
            Material_Date.setHasFocus(true);
            /*
            Updated_Category = getIntent().getStringExtra("Category");
            Updated_Amount = getIntent().getStringExtra("Amount");
            Updated_Title = getIntent().getStringExtra("Title");
            Updated_Date = getIntent().getStringExtra("DateCreated");
            Updated_Id = getIntent().getIntExtra("_ID", 0);*/
            if ("Clothing".equals(Updated_Category)) {
                Clothing.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                Category_text = "Clothing";
            } else if ("Entertainment".equals(Updated_Category)) {
                Entertainment.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                Category_text = "Entertainment";
            } else if ("Food".equals(Updated_Category)) {
                Food.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                Category_text = "Food";
            } else if ("Fuel".equals(Updated_Category)) {
                Fuel.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                Category_text = "Fuel";
            } else if ("Health".equals(Updated_Category)) {
                Health.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                Category_text = "Health";
            } else if ("Salary".equals(Updated_Category)) {
                Salary.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                Category_text = "Salary";
            }
            Updated_Amount = Updated_Amount.replace("Rs ", "");
            Log.d("Rs replaced", Updated_Amount);
            AmountEdit.setText(Updated_Amount);
            TitleEdit.setText(Updated_Title);
            dateEdit.setText(Updated_Date);
        }


            return fragmentLayout;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }








    Thread th= new Thread(){
        @Override
        public void run() {
            final String[] COUNTRIES = new String[]{
                    "balanced", "high-protein", "high-fiber", "low-fat", "low-carb", "low-sodium"};
            ArrayAdapter<String> contactAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line,Contact_Names);
            AutoCompleteTextView autoCompleteContacts=(AutoCompleteTextView)fragmentLayout.findViewById(R.id.Notes_Edit);

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
                            Clothing.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                            Entertainment.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Food.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Fuel.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Health.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Salary.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            More.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                        }
                        if(buttonView.getParent().equals(Entertainment)){
                            Category_text="Entertainment";
                            Entertainment.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                            Clothing.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Food.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Fuel.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Health.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Salary.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            More.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                        }
                        if(buttonView.getParent().equals(Food)){
                            Category_text="Food";
                            Food.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                            Entertainment.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Clothing.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Fuel.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Health.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Salary.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            More.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                        }
                        if(buttonView.getParent().equals(Fuel)){
                            Category_text="Fuel";
                            Fuel.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                            Entertainment.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Food.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Clothing.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Health.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Salary.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            More.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                        }
                        if(buttonView.getParent().equals(Health)){
                            Category_text="Health";
                            Health.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                            Entertainment.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Food.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Fuel.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Clothing.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Salary.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            More.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                        }
                        /*if(buttonView.getParent().equals(Salary)){
                            Category_text="Salary";
                            Salary.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                            Entertainment.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Food.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Fuel.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Health.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Clothing.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            More.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                        }
                        if(buttonView.getParent().equals(More)){

                            More.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                            Entertainment.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Food.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Fuel.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Health.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Salary.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            More.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                        }*/
                    }else{
                        if(buttonView.getParent().equals(Clothing)){
                            Category_text="";
                            Clothing.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                     /*  Entertainment.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));*/
                        }
                        if(buttonView.getParent().equals(Entertainment)){
                            Category_text="";
                            Entertainment.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                      /* Clothing.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));*/
                        }
                        if(buttonView.getParent().equals(Food)){
                            Category_text="";
                            Food.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                       /*Entertainment.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Clothing.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));*/
                        }
                        if(buttonView.getParent().equals(Fuel)){
                            Category_text="";
                            Fuel.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                  /*     Entertainment.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Clothing.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));*/
                        }
                        if(buttonView.getParent().equals(Health)){
                            Category_text="";
                            Health.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                       /*Entertainment.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Clothing.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));*/
                        }
                        /*if(buttonView.getParent().equals(Salary)){
                            Category_text="";
                            Salary.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                       /*Entertainment.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Clothing.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));*/
                        //}
                        /*if(buttonView.getParent().equals(More)){
                            Category_text="";
                            More.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                      /* Entertainment.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Clothing.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));*/
                        //}
                    }


                }
            };

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
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
            Cursor c = getActivity().managedQuery(contactData, null, null, null, null);
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


    public void onclick_date(){
        datePicker.show();
        //getActivity().showDialog(DIALOG_ID);
    }

    public void Onclick_Image_button(){
        final EditText Image_Link;

        LayoutInflater LI=LayoutInflater.from(getContext());
        //View PromptsView=LI.inflate(R.layout.image_dialog,null);

        //Image_Link=(EditText)PromptsView.findViewById(R.id.);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

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

  /*  @Override
    protected Dialog onCreateDialog(int id){
        if(id==DIALOG_ID){
            return new DatePickerDialog(getContext(),dateSetListener,year_x,month_x,day_x);
        }
        return null;
    }
*/


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
                    dateEdit.setText(dayOfMonth+" "+Months[month]+" "+year);
                }
            };















    public void Submit(){
        Amount_text=AmountEdit.getText().toString();
        Notes_text=AutoCompleteContacts.getText().toString();
        Title_text=TitleEdit.getText().toString();
        View focus=null;
        Boolean cancel=false;
        if(TextUtils.isEmpty(Category_text)){
            Snackbar.make(fragmentLayout, "Category can't be empty", Snackbar.LENGTH_LONG)
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
                    Transactiondbhelper mdbhelper = new Transactiondbhelper(getContext());
                    SQLiteDatabase db = mdbhelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    //values.put(recipe_entry.Column_Recipe_Image,byteimage);
                    values.put(Transaction_contract.Transaction_Entry.Column_Title, Title_text);
                    values.put(Transaction_contract.Transaction_Entry.Column_Category, Category_text);
                    values.put(Transaction_contract.Transaction_Entry.Column_Amount, "Rs " + Amount_text);
                    Log.d("Date created", DateEdit_text);
                    values.put(Transaction_contract.Transaction_Entry.Column_Date_Created, DateEdit_text);
                    //values.put(recipe_entry.Column_Recipe_Nutri_label,Nlabel);
                    //values.put(recipe_entry.Column_Recipe_Nutri_Quantity,Nquantity);
                    long newRowId = db.insert(Transaction_contract.Transaction_Entry.Table_name, null, values);
                    if (newRowId == -1) {
                        // If the row ID is -1, then there was an error with insertion.
                        Toast.makeText(getContext(), "Error with saving pet", Toast.LENGTH_SHORT).show();
                    } else {
                        // Otherwise, the insertion was successful and we can display a toast with the row ID.
                        Toast.makeText(getContext(), "Recipe saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
                    }
                    Bundle args = new Bundle();
                    //args.putParcelableArrayList("ARRAYLIST",(ArrayList<? extends Parcelable>) customlist);
                    Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG).show();

                }else{
                    Updated_Title=TitleEdit.getText().toString();
                    Updated_Category=Category_text;
                    Updated_Amount=AmountEdit.getText().toString();
                    Updated_Date=dateEdit.getText().toString();

                    Transactiondbhelper mdbhelper = new Transactiondbhelper(getContext());
                    SQLiteDatabase db = mdbhelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    //values.put(recipe_entry.Column_Recipe_Image,byteimage);
                    values.put(Transaction_contract.Transaction_Entry.Column_Title, Updated_Title);
                    values.put(Transaction_contract.Transaction_Entry.Column_Category, Updated_Category);
                    values.put(Transaction_contract.Transaction_Entry.Column_Amount, "Rs " + Updated_Amount);
                    values.put(Transaction_contract.Transaction_Entry.Column_Date_Created, Updated_Date);
                    db.update(Transaction_contract.Transaction_Entry.Table_name,values, Transaction_contract.Transaction_Entry._id+"="+Updated_Id,null);

                }

                Intent intent = new Intent(getContext(), Main2Activity.class);
                startActivity(intent);

            }
        }
    }


    public void Delete_Button(){
        Transactiondbhelper mdbhelper = new Transactiondbhelper(getContext());
        SQLiteDatabase db = mdbhelper.getWritableDatabase();
        // ContentValues values = new ContentValues();
        db.delete(Transaction_contract.Transaction_Entry.Table_name, Transaction_contract.Transaction_Entry._id + " = ?", new String[]{""+Updated_Id});
        Intent intent = new Intent(getContext(), Main2Activity.class);
        startActivity(intent);

    }



    public void MoreButton(){
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
            Material_Notes.setHasFocus(true);
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

        }
    }


    public void Contact_Button(){
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
                    Toast.makeText(getContext(), "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }



    public void Fetch_Contacts(){
        ContentResolver cr = getContext().getContentResolver();
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



}
