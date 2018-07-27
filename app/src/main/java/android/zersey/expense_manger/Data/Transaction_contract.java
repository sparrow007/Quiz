package android.zersey.expense_manger.Data;

import android.provider.BaseColumns;

public class Transaction_contract {



    private Transaction_contract(){}

    public static final class Transaction_Entry implements BaseColumns {

        public final static String Table_name="Expenses";

        public final static String _id=BaseColumns._ID;
        public final static String Column_Title="Title";
        public final static String Column_Category="Category";
        public final static String Column_Amount="Amount";
        public final static String Column_Date_Created="DateCreated";
        public final static String Column_Notes="Notes";
        public final static String Column_Date_Updated="DateUpdated";
        public final static String Column_Image="Image";      }

}
