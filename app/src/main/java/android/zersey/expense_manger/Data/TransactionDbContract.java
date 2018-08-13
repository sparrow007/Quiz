package android.zersey.expense_manger.Data;

import android.provider.BaseColumns;

public class TransactionDbContract {

	private TransactionDbContract() {
	}

	public static final class Transaction_Entry implements BaseColumns {

		public final static String TABLE_NAME = "expenses";

		public final static String COLUMN_ONLINE_ID = "online_id";
		public final static String COLUMN_TITLE = "title";
		public final static String COLUMN_CATEGORY = "category";
		public final static String COLUMN_AMOUNT = "amount";
		public final static String COLUMN_DATE_CREATED = "date_created";
		public final static String COLUMN_NOTES = "notes";
		public final static String COLUMN_DATE_UPDATED = "date_updated";
		public final static String COLUMN_IMAGE = "image";
		public static final String COLUMN_TYPE = "type";

		public final static String SQL_CREATE_TRANSACTIONS_TABLE = "CREATE TABLE "
			+ Transaction_Entry.TABLE_NAME
			+ "("
			+ Transaction_Entry._ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_ONLINE_ID
			+ " INTEGER, "
			+ COLUMN_TYPE
			+ " TEXT, "
			+ Transaction_Entry.COLUMN_TITLE
			+ " TEXT, "
			+ Transaction_Entry.COLUMN_CATEGORY
			+ " TEXT, "
			+ Transaction_Entry.COLUMN_AMOUNT
			+ " TEXT, "
			+ Transaction_Entry.COLUMN_DATE_CREATED
			+ " TEXT, "
			+ Transaction_Entry.COLUMN_DATE_UPDATED
			+ " TEXT, "
			+ Transaction_Entry.COLUMN_IMAGE
			+ " BLOB, "
			+ Transaction_Entry.COLUMN_NOTES
			+ " TEXT );";
	}

	public static final class TempEntry implements BaseColumns {

		public static final String TABLE_NAME = "temp";
		public static final String COLUMN_LOCAL_ID = "local_id";
		public static final String COLUMN_ACTION = "action";
		public static final String COLUMN_ONLINE_ID = "online_id";

		public static final String SQL_CREATE_TEMP_TABLE = "CREATE TABLE "
			+ TABLE_NAME
			+ " ( "
			+ TempEntry._ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ COLUMN_LOCAL_ID
			+ " INTEGER, "
			+ COLUMN_ONLINE_ID
			+ " INTEGER, "
			+ COLUMN_ACTION
			+ " TEXT,"
			+ "FOREIGN KEY ("
			+ COLUMN_LOCAL_ID
			+ ") REFERENCES "
			+ Transaction_Entry.TABLE_NAME
			+ "("
			+ Transaction_Entry._ID
			+ ")"
			+ ");";
	}
}
