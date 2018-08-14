package android.zersey.expense_manger;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.zersey.expense_manger.Data.Contacts_contract.Contacts_Entry;
import android.zersey.expense_manger.Data.Contactsdbhelper;

import com.github.florent37.materialtextfield.MaterialTextField;

import java.util.ArrayList;

public class Add_Contacts_Activity extends AppCompatActivity {

    private EditText ContactNameEdit,ContactNumberEdit,ContactAddressEdit,ContactEmailEdit,ContactCompanyEdit;
    private ImageButton Contact_Delete_Button;
    private String Updated_Name,Updated_Number,CardClicked;
    private int Updated_Id;
    private MaterialTextField Material_Name,Material_Number,Material_Address,Material_Email,Material_Company;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_add__contacts_);
        Contact_Delete_Button=(ImageButton)findViewById(R.id.Delete_Contact_Button);
        Material_Address=(MaterialTextField)findViewById(R.id.Material_Contact_Address_Edit);
        Material_Email=(MaterialTextField)findViewById(R.id.Material_Contact_Email_ID_Edit);
        Material_Company=(MaterialTextField)findViewById(R.id.Material_Contact_Company_Edit);
        ContactNameEdit=(EditText)findViewById(R.id.Contact_Name_Edit);
        ContactNumberEdit=(EditText)findViewById(R.id.Contact_Number_Edit);
        ContactEmailEdit=(EditText)findViewById(R.id.Contact_address_Edit);
        ContactAddressEdit=(EditText)findViewById(R.id.Contact_Email_ID_Edit);
        ContactCompanyEdit=(EditText)findViewById(R.id.Contact_Company_Edit);
        CardClicked=getIntent().getStringExtra("CardClicked");
        if (!TextUtils.isEmpty(CardClicked)) {
            Updated_Name=getIntent().getStringExtra("Name");
            Updated_Number=getIntent().getStringExtra("Number");
            Updated_Id=getIntent().getIntExtra("_ID",0);
            ContactNameEdit.setText(Updated_Name);
            ContactNumberEdit.setText(Updated_Number);
            Contact_Delete_Button.setVisibility(View.VISIBLE);
            }

    }

    public void Add_Contact(View view){
        String name=ContactNameEdit.getText().toString();
        String Number=ContactNumberEdit.getText().toString();
        boolean cancel=false;
        View focus=ContactNameEdit;
        if (TextUtils.isEmpty(name)){
            ContactNameEdit.setError("Required Field");
            focus=ContactNameEdit;
            cancel=true;
        }
        if (!TextUtils.isDigitsOnly(Number)){
            ContactNumberEdit.setError("Please enter digits only");
            focus=ContactNumberEdit;
            cancel=true;
        }
        if (Number.length()<10 || Number.length()>12){
            ContactNumberEdit.setError("Please enter a correct number");
            focus=ContactNumberEdit;
            cancel=true;
        }
        if (cancel){
            focus.requestFocus();
        }else {
            if(TextUtils.isEmpty(CardClicked)) {
                if (Check_Contacts(Number)) {
                    ContactNumberEdit.setError("Number is Already Present");
                    View view1=ContactNumberEdit;
                    view1.requestFocus();

                }else{
                Contactsdbhelper mdbhelper = new Contactsdbhelper(Add_Contacts_Activity.this);
                SQLiteDatabase db = mdbhelper.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put(Contacts_Entry.Column_Contact_Name, name);
                values.put(Contacts_Entry.Column_Contact_Number, Number);
                //values.put(Transaction_contract.Transaction_Entry.Column_Amount, "Rs " + Amount_text);
                //Log.d("Date created", DateEdit_text);
                //values.put(Transaction_contract.Transaction_Entry.Column_Date_Created, DateEdit_text);

                long newRowId = db.insert(Contacts_Entry.Table_name, null, values);
                if (newRowId == -1) {
                    // If the row ID is -1, then there was an error with insertion.
                    Toast.makeText(Add_Contacts_Activity.this, "Error with saving Contact", Toast.LENGTH_SHORT).show();
                } else {
                    // Otherwise, the insertion was successful and we can display a toast with the row ID.
                    Toast.makeText(Add_Contacts_Activity.this, "Contact saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
                }

                    Intent intent=new Intent(Add_Contacts_Activity.this,Contact_List_Activity.class);
                    startActivity(intent);

            }
            }else {
                if(Check_Contacts(Number)){
                    ContactNumberEdit.setError("Number is Already Present");
                    View view1=ContactNumberEdit;
                    view1.requestFocus();

                }else{
                Updated_Name = ContactNameEdit.getText().toString();
                Updated_Number = ContactNumberEdit.getText().toString();

                Contactsdbhelper mdbhelper = new Contactsdbhelper(Add_Contacts_Activity.this);
                SQLiteDatabase db = mdbhelper.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put(Contacts_Entry.Column_Contact_Name, Updated_Name);
                values.put(Contacts_Entry.Column_Contact_Number, Updated_Number);

                db.update(Contacts_Entry.Table_name, values, Contacts_Entry._id + "=" + Updated_Id, null);

                    Intent intent=new Intent(Add_Contacts_Activity.this,Contact_List_Activity.class);
                    startActivity(intent);

            }
            }

        }
    }


    public void Add_Address(View view){
        Material_Address.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);
    }
    public void Add_Email(View view){
        Material_Email.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);
    }
    public void Add_Company(View view){
        Material_Company.setVisibility(View.VISIBLE);
        view.setVisibility(View.GONE);
    }

    public void Delete_Button(View view){
        Contactsdbhelper mdbhelper = new Contactsdbhelper(Add_Contacts_Activity.this);
        SQLiteDatabase db = mdbhelper.getWritableDatabase();
        // ContentValues values = new ContentValues();
        db.delete(Contacts_Entry.Table_name, Contacts_Entry._id + " = ?", new String[]{""+Updated_Id});
        Intent intent = new Intent(this, Contact_List_Activity.class);
        startActivity(intent);

    }


    public Boolean Check_Contacts(String contactnumber){
        ArrayList<Custom_Contact_items> Item_list=new ArrayList<>();
        Contactsdbhelper mDbHelper=new Contactsdbhelper(Add_Contacts_Activity.this);
        SQLiteDatabase db1 = mDbHelper.getReadableDatabase();
        //final  ArrayList<String> labellist=new ArrayList<>();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                Contacts_Entry._ID,
                Contacts_Entry.Column_Contact_Name,
                Contacts_Entry.Column_Contact_Number
        };

        //Cursor cursor=db1.rawQuery("SELECT * FROM "+recipe_entry.Table_name,null);
        // Perform a query on the pets table
        Cursor cursor = db1.query(
                Contacts_Entry.Table_name,   // The table to query
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
                int currentID = cursor.getInt(cursor.getColumnIndex(Contacts_Entry._id));
                String Name = cursor.getString(cursor.getColumnIndex(Contacts_Entry.Column_Contact_Name));
                //Log.d( "current label ",currentName);
                String Number = cursor.getString(cursor.getColumnIndex(Contacts_Entry.Column_Contact_Number));
                if(TextUtils.equals(Number,contactnumber)){
                    return true;
                }
                Custom_Contact_items CCItem = new Custom_Contact_items(Name,Number);
                Item_list.add(CCItem);

                cursor.moveToNext();

            }

            for(int i=0;i<Item_list.size();i++){

            }

        }catch (Exception e){e.printStackTrace();}

        return false;

    }

    public void backpress(View view){
        Intent intent=new Intent(Add_Contacts_Activity.this,Contact_List_Activity.class);
        startActivity(intent);
    }


}
