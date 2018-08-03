package android.zersey.expense_manger;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Income_form.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Income_form#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Income_form extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private ImageView Img_File;
    private FloatingActionButton fab;
    private LinearLayout Contact_LinearLayout;
    private ArrayList<String> Contact_list;
    private MaterialTextField Material_Title,Material_Amount,Material_Date,Material_Contact,Material_Amount_Due;
    private EditText dateEdit,AmountEdit,TitleEdit,Amount_Due_Edit;
    private View fragmentLayout;
    private int year_x,month_x,day_x,Selected_date=0,Updated_Id;
    private String CardClicked,Updated_Category,Updated_Title,Updated_Amount,Updated_Date;
    private Calendar cal;
    private AutoCompleteTextView AutoCompleteContacts;
    private ArrayAdapter<String> ContactAdapter;
    private String[] Months={"Jan","Feb","March","April","May","June","July","Aug","Sept","Oct","Nov","Dec"},Contact_Names;
    private DatePickerDialog datePicker;
    private Button Submit_Income;
    private TextView More_TextView_Button;
    private ImageButton Delete_Button,Camera_Button,Contact_Button;



    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Income_form() {
        // Required empty public constructor
    }

    public void setString(String updated_Title,String updated_Amount,
                          String updated_Date,String updated_Category,int id) {
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
     * @return A new instance of fragment Income_form.
     */
    // TODO: Rename and change types and number of parameters
    public static Income_form newInstance(String param1, String param2) {
        Income_form fragment = new Income_form();
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
        cal= Calendar.getInstance();
        year_x=cal.get(Calendar.YEAR);
        day_x=cal.get(Calendar.DAY_OF_MONTH);
        month_x=cal.get(Calendar.MONTH);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Contact_list=new ArrayList<String>();
        fragmentLayout=inflater.inflate(R.layout.fragment_income_form, container, false);
        Material_Title=(MaterialTextField)fragmentLayout.findViewById(R.id.Material_Title_Income);
        Material_Title.setHasFocus(true);
        Material_Amount=(MaterialTextField)fragmentLayout.findViewById(R.id.Material_Income_Amount);
        Material_Amount.setHasFocus(true);
        Material_Date=(MaterialTextField)fragmentLayout.findViewById(R.id.Material_Date_Income);
        Material_Date.setHasFocus(true);
        Material_Contact=(MaterialTextField)fragmentLayout.findViewById(R.id.Material_Contact_Income);
        Material_Amount_Due=(MaterialTextField)fragmentLayout.findViewById(R.id.Material_Amount_Due_Income);
        Material_Amount_Due.setVisibility(View.GONE);
        dateEdit=(EditText)fragmentLayout.findViewById(R.id.Date_Income_Edit);
        dateEdit.setText(day_x+" "+Months[month_x]+" "+year_x);
        Delete_Button=(ImageButton)fragmentLayout.findViewById(R.id.Delete_Income_Button);
        Delete_Button.setVisibility(View.GONE);
        //Camera_Button=(ImageButton)fragmentLayout.findViewById(R.id.Camera_Income_Button);
        Contact_Button=(ImageButton)fragmentLayout.findViewById(R.id.Contact_Income_Button);
        More_TextView_Button=(TextView)fragmentLayout.findViewById(R.id.More_Income_Button);
        Img_File=(ImageView)fragmentLayout.findViewById(R.id.Img_Income_file);
        Img_File.setVisibility(View.GONE);
        Contact_LinearLayout=(LinearLayout)fragmentLayout.findViewById(R.id.Contact_Income_Layout);
        Contact_LinearLayout.setVisibility(View.GONE);

        Submit_Income=(Button)fragmentLayout.findViewById(R.id.Submit_Income_Button);
        TitleEdit=(EditText)fragmentLayout.findViewById(R.id.Title_Income_Edit);
        AmountEdit=(EditText)fragmentLayout.findViewById(R.id.Amount_Income_Edit);
        Amount_Due_Edit=(EditText)fragmentLayout.findViewById(R.id.Amount_Due_Income_Edit);
        AutoCompleteContacts=(AutoCompleteTextView) fragmentLayout.findViewById(R.id.Contact_Income_AutoComplete);
        if(TextUtils.equals(Updated_Category,"Income")){
            Updated_Amount = Updated_Amount.replace("Rs ", "");
            Log.d("Rs replaced", Updated_Amount);
            AmountEdit.setText(Updated_Amount);
            TitleEdit.setText(Updated_Title);
            dateEdit.setText(Updated_Date);
            Delete_Button.setVisibility(View.VISIBLE);
        }

        fab = (FloatingActionButton) fragmentLayout.findViewById(R.id.fab_Income_Camera_Button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

                Onclick_Image_button();
            }
        });
        datePicker = new DatePickerDialog(getContext(),
                R.style.Theme_AppCompat_DayNight_Dialog, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        datePicker.setCancelable(true);
        datePicker.setTitle("Select Date");

        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show();
            }
        });
        Submit_Income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Submit();
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

        /*Camera_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Onclick_Image_button();
            }
        });*/
        More_TextView_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoreButton();
            }
        });

        return fragmentLayout;
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
                    dateEdit.setText(dayOfMonth+" "+Months[month]+" "+year);
                }
            };


    Thread th= new Thread(){
        @Override
        public void run() {

            ArrayAdapter<String> contactAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line,Contact_Names);
            AutoCompleteTextView autoCompleteContacts=(AutoCompleteTextView)fragmentLayout.findViewById(R.id.Contact_Income_AutoComplete);

            autoCompleteContacts.setThreshold(1);
            autoCompleteContacts.setAdapter(contactAdapter);
        }
    };



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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    public void Submit(){
        String Amount_text=AmountEdit.getText().toString();
        String Contacts=AutoCompleteContacts.getText().toString();
        String Title_text=TitleEdit.getText().toString();
        View focus=null;
        Boolean cancel=false;

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
                if (!TextUtils.equals(Updated_Category,"Income")) {
                    String DateEdit_text = dateEdit.getText().toString();
                    //Custom_items items = new Custom_items(Category_text,
                    //      Title_text, "Rs " + Amount_text, day_x + " " + Months[month_x - 1] + " " + year_x);
                    //customlist.add(items);
                    Transactiondbhelper mdbhelper = new Transactiondbhelper(getContext());
                    SQLiteDatabase db = mdbhelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    //values.put(recipe_entry.Column_Recipe_Image,byteimage);
                    values.put(Transaction_contract.Transaction_Entry.Column_Title, Title_text);
                    values.put(Transaction_contract.Transaction_Entry.Column_Category, "Income");
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

    public void Delete_Button(){
        Transactiondbhelper mdbhelper = new Transactiondbhelper(getContext());
        SQLiteDatabase db = mdbhelper.getWritableDatabase();
        // ContentValues values = new ContentValues();
        db.delete(Transaction_contract.Transaction_Entry.Table_name, Transaction_contract.Transaction_Entry._id + " = ?", new String[]{""+Updated_Id});
        Intent intent = new Intent(getContext(), Main2Activity.class);
        startActivity(intent);

    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        //callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode==RESULT_OK) {

            //Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri uri = data.getData();
            Img_File.setVisibility(View.VISIBLE);
            Img_File.setImageURI(uri);
            //Image_uri=uri;
        }else if (requestCode == 1 && resultCode==RESULT_OK) {
            //Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri uri = data.getData();
            //Img_File.setVisibility(View.VISIBLE);
            //Image_uri=uri;
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



    public void MoreButton(){
        Fetch_Contacts();
        th.run();
        if(Contact_LinearLayout.getVisibility()==View.GONE){
            Contact_LinearLayout.setVisibility(View.VISIBLE);
            Material_Amount_Due.setVisibility(View.VISIBLE);
            if(!TextUtils.isEmpty(AmountEdit.getText().toString())) {
                Amount_Due_Edit.setText(AmountEdit.getText().toString());
            }else{Amount_Due_Edit.setText("");}
            /*YoYo.with(Techniques.SlideInLeft)
                    .duration(1000)
                    .repeat(0)
                    .playOn(Notes_Layout);*/
            Material_Contact.setHasFocus(true);
            Material_Amount_Due.setHasFocus(true);
            // More_Button.setImageDrawable(getResources().getDrawable(R.drawable.uparrow));

        }else{
            Material_Contact.setHasFocus(false);
            Material_Amount_Due.setHasFocus(false);
            Contact_LinearLayout.setVisibility(View.GONE);
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
