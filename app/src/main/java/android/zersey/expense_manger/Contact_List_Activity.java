package android.zersey.expense_manger;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.zersey.expense_manger.Data.Contacts_contract.Contacts_Entry;
import android.zersey.expense_manger.Data.Contactsdbhelper;
import android.zersey.expense_manger.Data.Transaction_contract;

import java.util.ArrayList;

public class Contact_List_Activity extends AppCompatActivity {
private ListView Contact_List_View;
private ArrayList<Custom_Contact_items> Item_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact__list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Contact_List_View=(ListView)findViewById(R.id.Contact_List_View);
        Contact_List_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Contactsdbhelper mDbHelper=new Contactsdbhelper(Contact_List_Activity.this);
                SQLiteDatabase db1 = mDbHelper.getReadableDatabase();

                String[] projection = {
                        Contacts_Entry._ID,
                        Contacts_Entry.Column_Contact_Name,
                        Contacts_Entry.Column_Contact_Number};

                Cursor cursor = db1.query(
                        Contacts_Entry.Table_name,   // The table to query
                        projection,            // The columns to return
                        null,                  // The columns for the WHERE clause
                        null,                  // The values for the WHERE clause
                        null,                  // Don't group the rows
                        null,                  // Don't filter by row groups
                        null);                   // The sort order
                cursor.moveToPosition(position);

                int currentID = cursor.getInt(cursor.getColumnIndex(Contacts_Entry._id));
                String Name = cursor.getString(cursor.getColumnIndex(Contacts_Entry.Column_Contact_Name));
                //Log.d( "current label ",currentName);
                String Number = cursor.getString(cursor.getColumnIndex(Contacts_Entry.Column_Contact_Number));
                //String amount = cursor.getString(cursor.getColumnIndex(Transaction_contract.Transaction_Entry.Column_Amount));
                //String dateCreated = cursor.getString(cursor.getColumnIndex(Transaction_contract.Transaction_Entry.Column_Date_Created));
                //Custom_items csitem=new Custom_items(category,title,amount,dateCreated);
                Intent i=new Intent(Contact_List_Activity.this,Add_Contacts_Activity.class);
                i.putExtra("_ID",currentID);
                i.putExtra("CardClicked","Yes");
                i.putExtra("Name",Name);
                i.putExtra("Number",Number);
                startActivity(i);

            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Contact_List_Activity.this,Add_Contacts_Activity.class);
                startActivity(i);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        Item_list=new ArrayList<>();
        Contactsdbhelper mDbHelper=new Contactsdbhelper(Contact_List_Activity.this);
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

                Custom_Contact_items CCItem = new Custom_Contact_items(Name,Number);
                Item_list.add(CCItem);

                cursor.moveToNext();

            }
            Contact_ListView_Adapter adapter=new Contact_ListView_Adapter(this,R.layout.contacts_list_layout,Item_list);
            Contact_List_View.setAdapter(adapter);

        }catch (Exception e){e.printStackTrace();}
        finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }



    }
    @Override public void onBackPressed() {
        Intent intent=new Intent(Contact_List_Activity.this,Main2Activity.class);
        startActivity(intent);
    }

}
