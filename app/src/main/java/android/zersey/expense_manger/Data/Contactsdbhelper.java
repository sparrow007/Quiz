package android.zersey.expense_manger.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.zersey.expense_manger.Data.Contacts_contract.Contacts_Entry;

public class Contactsdbhelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="ContactsInformation.db";
    private static final int DATABAE_VERSION= 1;

    public Contactsdbhelper(Context context) { super(context,DATABASE_NAME,null,DATABAE_VERSION); }


    @Override
    public void onCreate(SQLiteDatabase db) {


        String SQL_CREATE_RECIPE_TABLE="CREATE TABLE "+ Contacts_Entry.Table_name+"("
                + Contacts_Entry._id+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contacts_Entry.Column_Contact_Name+" TEXT NOT NULL, "
                + Contacts_Entry.Column_Contact_Number+" TEXT NOT NULL );";
        db.execSQL(SQL_CREATE_RECIPE_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
