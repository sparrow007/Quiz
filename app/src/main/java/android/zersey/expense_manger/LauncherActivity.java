package android.zersey.expense_manger;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.zersey.expense_manger.Data.Transaction_contract.Transaction_Entry;
import android.zersey.expense_manger.Data.Transactiondbhelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LauncherActivity extends AppCompatActivity {
private ArrayList<Custom_items> Item_list;
    private Transactiondbhelper mDbHelper ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        mDbHelper=new Transactiondbhelper(this);
        Item_list=new ArrayList<>();
        //if(getIntent().getBundleExtra("Bundle")!=null){
            Bundle bundle=new Bundle();
            //bundle=this.getIntent().getBundleExtra("Bundle");
         //ArrayList<? extends Parcelable> lllist=this.getIntent().getParcelableArrayListExtra("parcel");
        if(Item_list!=null){
        Update_layout();}
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args=new Bundle();
                args.putSerializable("ARRAYLIST",(Serializable)Item_list);
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent=new Intent(LauncherActivity
                        .this,MainActivity.class);
                //intent.putExtra("Bundle",args);
                startActivity(intent);
            }
        });
    }

    public void Update_layout(){


        SQLiteDatabase db1 = mDbHelper.getReadableDatabase();
        //final  ArrayList<String> labellist=new ArrayList<>();
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                Transaction_Entry._ID,
                Transaction_Entry.Column_Title,
                Transaction_Entry.Column_Category,
                Transaction_Entry.Column_Amount,
                Transaction_Entry.Column_Date_Created,
                Transaction_Entry.Column_Date_Updated,
                Transaction_Entry.Column_Image,
                Transaction_Entry.Column_Notes};

        //Cursor cursor=db1.rawQuery("SELECT * FROM "+recipe_entry.Table_name,null);
        // Perform a query on the pets table
        Cursor cursor = db1.query(
                Transaction_Entry.Table_name,   // The table to query
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
                int currentID = cursor.getInt(cursor.getColumnIndex(Transaction_Entry._id));
                String title = cursor.getString(cursor.getColumnIndex(Transaction_Entry.Column_Title));
                //Log.d( "current label ",currentName);
                String category = cursor.getString(cursor.getColumnIndex(Transaction_Entry.Column_Category));
                String amount = cursor.getString(cursor.getColumnIndex(Transaction_Entry.Column_Amount));
                String dateCreated = cursor.getString(cursor.getColumnIndex(Transaction_Entry.Column_Date_Created));
                Custom_items csitem=new Custom_items(category,title,amount,dateCreated);
                Item_list.add(csitem);
                // Display the values from each column of the current row in the cursor in the TextView
                /*displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        //currentBreed + " - " +
                        currentGender + " - " +
                        currentWeight));
                */
                cursor.moveToNext();

            }




            ListView listView = (ListView)findViewById(R.id.listView);
            Custom_listView_Adapter adapter=new Custom_listView_Adapter(LauncherActivity.this,R.layout.transaction_list_layout,Item_list);
            listView.setAdapter(adapter);
        }catch (Exception e){e.printStackTrace();}
        finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }



        //Item_list=bundle.getParcelableArrayList("ARRAYLIST");}




    }

}

