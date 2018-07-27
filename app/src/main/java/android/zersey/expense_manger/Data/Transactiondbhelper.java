package android.zersey.expense_manger.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.zersey.expense_manger.Data.Transaction_contract.Transaction_Entry;

public class Transactiondbhelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME="Transactions.db";
    private static final int DATABAE_VERSION= 1;

    public Transactiondbhelper(Context context) { super(context,DATABASE_NAME,null,DATABAE_VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_RECIPE_TABLE="CREATE TABLE "+Transaction_Entry.Table_name+"("
                +Transaction_Entry._id+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +Transaction_Entry.Column_Title+" TEXT NOT NULL, "
                +Transaction_Entry.Column_Category+" TEXT NOT NULL, "
                +Transaction_Entry.Column_Amount+" TEXT NOT NULL, "
                +Transaction_Entry.Column_Date_Created+" TEXT NOT NULL, "
                +Transaction_Entry.Column_Date_Updated+" TEXT, "
                +Transaction_Entry.Column_Image+" BLOB, "
                +Transaction_Entry.Column_Notes+" TEXT );";
        db.execSQL(SQL_CREATE_RECIPE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
