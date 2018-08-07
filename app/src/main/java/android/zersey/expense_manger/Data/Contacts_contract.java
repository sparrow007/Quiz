package android.zersey.expense_manger.Data;

import android.provider.BaseColumns;

public class Contacts_contract {
    public static final class Contacts_Entry implements BaseColumns {

        public final static String Table_name="Contacts";
        public final static String _id=BaseColumns._ID;
        public final static String Column_Contact_Name="ContactName";
        public final static String Column_Contact_Number="ContactNumber";
    }
}
