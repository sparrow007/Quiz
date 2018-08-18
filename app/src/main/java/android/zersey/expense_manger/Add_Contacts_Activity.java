package android.zersey.expense_manger;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;
import android.zersey.expense_manger.Data.Contacts_contract;
import android.zersey.expense_manger.Data.Contactsdbhelper;

public class Add_Contacts_Activity extends AppCompatActivity {

    private EditText ContactNameEdit,ContactNumberEdit,ContactAddressEdit,ContactEmailEdit,ContactCompanyEdit;
    private String Updated_Name,Updated_Number,CardClicked;
    private int Updated_Id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_add__contacts_);
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
            if(TextUtils.isEmpty(CardClicked)){
            Contactsdbhelper mdbhelper = new Contactsdbhelper(Add_Contacts_Activity.this);
            SQLiteDatabase db = mdbhelper.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(Contacts_contract.Contacts_Entry.Column_Contact_Name, name);
            values.put(Contacts_contract.Contacts_Entry.Column_Contact_Number, Number);
            //values.put(Transaction_contract.Transaction_Entry.Column_Amount, "Rs " + Amount_text);
            //Log.d("Date created", DateEdit_text);
            //values.put(Transaction_contract.Transaction_Entry.Column_Date_Created, DateEdit_text);

            long newRowId = db.insert(Contacts_contract.Contacts_Entry.Table_name, null, values);
            if (newRowId == -1) {
                // If the row ID is -1, then there was an error with insertion.
                Toast.makeText(Add_Contacts_Activity.this, "Error with saving Contact", Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast with the row ID.
                Toast.makeText(Add_Contacts_Activity.this, "Contact saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
            }
        }else{

                Updated_Name=ContactNameEdit.getText().toString();
                Updated_Number=ContactNumberEdit.getText().toString();

                Contactsdbhelper mdbhelper = new Contactsdbhelper(Add_Contacts_Activity.this);
                SQLiteDatabase db = mdbhelper.getWritableDatabase();
                ContentValues values = new ContentValues();

                values.put(Contacts_contract.Contacts_Entry.Column_Contact_Name, Updated_Name);
                values.put(Contacts_contract.Contacts_Entry.Column_Contact_Number, Updated_Number);

                db.update(Contacts_contract.Contacts_Entry.Table_name,values, Contacts_contract.Contacts_Entry._id+"="+Updated_Id,null);

            }
       Intent intent=new Intent(Add_Contacts_Activity.this,Contact_List_Activity.class);
            startActivity(intent);
        }
    }



    public void OnBackPressed(View view){
        Intent intent=new Intent(Add_Contacts_Activity.this, Main2Activity.class);
        startActivity(intent);
    }
}
