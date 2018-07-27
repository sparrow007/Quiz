package android.zersey.expense_manger;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import android.zersey.expense_manger.Data.Transaction_contract.Transaction_Entry;
import android.zersey.expense_manger.Data.Transactiondbhelper;

import com.github.florent37.materialtextfield.MaterialTextField;

public class MainActivity extends AppCompatActivity {
ImageView Img_File;

private MaterialTextField Material_Title,Material_Amount,Material_Date;
public View layout_view=null;
private List<Custom_items> customlist;
private TextView Category_text_view;
private int year_x,month_x,day_x,Selected_date=0,Updated_Id;
private String Category_text,Notes_text,Amount_text,Title_text;
private Uri Image_uri=null;
private static int DIALOG_ID=0;
private EditText dateEdit,AmountEdit,NotesEdit,TitleEdit;
private String CardClicked,Updated_Category,Updated_Title,Updated_Amount,Updated_Date;
private Calendar cal;
private String[] Months={"Jan","Feb","March","April","May","June","July","Aug","Sept","Oct","Nov","Dec"};
private LinearLayout Clothing,Entertainment,Food,Fuel,Health,Salary,More;
private CheckBox Clothing_checkbox,Entertainment_checkbox,Food_checkbox,Fuel_checkbox,Health_checkbox,Salary_checkbox,More_checkbox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Expense Manager");
        setContentView(R.layout.activity_main);
        checkRunTimePermission();
        customlist=new ArrayList<>();
        //customlist=(ArrayList<Custom_items>)getIntent().getBundleExtra("Bundle").getSerializable("ARRAYLIST");
        Material_Title=(MaterialTextField)findViewById(R.id.Material_Title);
        Material_Amount=(MaterialTextField)findViewById(R.id.Material_Amount);
        Material_Date=(MaterialTextField)findViewById(R.id.Material_Date);
        cal=Calendar.getInstance();
        year_x=cal.get(Calendar.YEAR);
        day_x=cal.get(Calendar.DAY_OF_MONTH);
        month_x=cal.get(Calendar.MONTH);

        //requestPermissions(Manifest.permission.CAMERA,1111);
        Img_File=(ImageView)findViewById(R.id.Img_file);
        dateEdit=(EditText)findViewById(R.id.Date_Edit);
        dateEdit.setText(day_x+" "+Months[month_x]+" "+year_x);
        TitleEdit=(EditText)findViewById(R.id.Title_Edit);
        AmountEdit=(EditText)findViewById(R.id.Amount_Edit);
        NotesEdit=(EditText)findViewById(R.id.Notes_Edit);
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
        More_checkbox.setOnCheckedChangeListener(checkedChangeListener);
        CardClicked=getIntent().getStringExtra("CardClicked");
        if (!TextUtils.isEmpty(CardClicked)){
            Material_Title.setHasFocus(true);
            Material_Amount.setHasFocus(true);
            Material_Date.setHasFocus(true);
            Updated_Category=getIntent().getStringExtra("Category");
            Updated_Amount=getIntent().getStringExtra("Amount");
            Updated_Title=getIntent().getStringExtra("Title");
            Updated_Date=getIntent().getStringExtra("DateCreated");
            Updated_Id=getIntent().getIntExtra("_ID",0);
            if ("Clothing".equals(Updated_Category)){
                Clothing.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                Category_text="Clothing";
            }else if ("Entertainment".equals(Updated_Category)){
                Entertainment.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                Category_text="Entertainment";
            }else if ("Food".equals(Updated_Category)){
                Food.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                Category_text="Food";
            }else if ("Fuel".equals(Updated_Category)){
                Fuel.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                Category_text="Fuel";
            }else if ("Health".equals(Updated_Category)){
                Health.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                Category_text="Health";
            }else if ("Salary".equals(Updated_Category)){
                Salary.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                Category_text="Salary";
            }
            Updated_Amount=Updated_Amount.replace("Rs ","");
            Log.d("Rs replaced",Updated_Amount);
            AmountEdit.setText(Updated_Amount);
            TitleEdit.setText(Updated_Title);
            dateEdit.setText(Updated_Date);
        }

    }


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
                        }
                        if(buttonView.getParent().equals(Entertainment)){
                            Category_text="";
                            Entertainment.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                      /* Clothing.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));*/
                        }
                        if(buttonView.getParent().equals(Food)){
                            Category_text="";
                            Food.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                       /*Entertainment.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Clothing.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));*/
                        }
                        if(buttonView.getParent().equals(Fuel)){
                            Category_text="";
                            Fuel.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                  /*     Entertainment.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Clothing.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));*/
                        }
                        if(buttonView.getParent().equals(Health)){
                            Category_text="";
                            Health.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                       /*Entertainment.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Clothing.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));*/
                        }
                        if(buttonView.getParent().equals(Salary)){
                            Category_text="";
                            Salary.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                       /*Entertainment.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Clothing.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));*/
                        }
                        if(buttonView.getParent().equals(More)){
                            Category_text="";
                            More.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newlightblue));
                      /* Entertainment.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));
                       Clothing.setBackgroundTintList(MainActivity.this.getResources().getColorStateList(R.color.newdarkblue));*/
                        }
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
        }
        if (requestCode == 1 && resultCode==RESULT_OK) {
            //Bitmap photo = (Bitmap) data.getExtras().get("data");
            Uri uri = data.getData();
            Img_File.setVisibility(View.VISIBLE);
            Image_uri=uri;
            Img_File.setImageURI(uri);
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
                     dateEdit.setText(dayOfMonth+" "+Months[month]+" "+year);
                     }
             };

    private void checkRunTimePermission() {
        String[] permissionArrays = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionArrays, 11111);
        } else {
            // if already permition granted
            // PUT YOUR ACTION (Like Open cemara etc..)
        }
    }



    public void Submit(View view){
        Amount_text=AmountEdit.getText().toString();
        Notes_text=NotesEdit.getText().toString();
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
                    Transactiondbhelper mdbhelper = new Transactiondbhelper(this);
                    SQLiteDatabase db = mdbhelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    //values.put(recipe_entry.Column_Recipe_Image,byteimage);
                    values.put(Transaction_Entry.Column_Title, Title_text);
                    values.put(Transaction_Entry.Column_Category, Category_text);
                    values.put(Transaction_Entry.Column_Amount, "Rs " + Amount_text);
                    Log.d("Date created", DateEdit_text);
                    values.put(Transaction_Entry.Column_Date_Created, DateEdit_text);
                    //values.put(recipe_entry.Column_Recipe_Nutri_label,Nlabel);
                    //values.put(recipe_entry.Column_Recipe_Nutri_Quantity,Nquantity);
                    long newRowId = db.insert(Transaction_Entry.Table_name, null, values);
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

                    Transactiondbhelper mdbhelper = new Transactiondbhelper(this);
                    SQLiteDatabase db = mdbhelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    //values.put(recipe_entry.Column_Recipe_Image,byteimage);
                    values.put(Transaction_Entry.Column_Title, Updated_Title);
                    values.put(Transaction_Entry.Column_Category, Updated_Category);
                    values.put(Transaction_Entry.Column_Amount, "Rs " + Updated_Amount);
                    values.put(Transaction_Entry.Column_Date_Created, Updated_Date);
                    db.update(Transaction_Entry.Table_name,values,Transaction_Entry._id+"="+Updated_Id,null);

                }

                Intent intent = new Intent(MainActivity.this, Main2Activity.class);
                startActivity(intent);

            }
        }
    }



    /*public void clothing(View view){



       if(check_clothing==false){
           //view.setBackgroundColor(Color.parseColor("#0091EA"));
           view.setBackgroundTintList(this.getResources().getColorStateList(R.color.newlightblue));
           check_clothing=true;
       }else if(check_clothing==true){
           view.setBackgroundTintList(this.getResources().getColorStateList(R.color.newdarkblue));
           //view.setBackgroundColor(Color.parseColor("#E1F5FE"));
           check_clothing=false;
       }

    }*/
}
