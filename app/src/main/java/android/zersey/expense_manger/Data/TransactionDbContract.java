package android.zersey.expense_manger.Data;

public class TransactionDbContract {

	private TransactionDbContract() {
	}

	public static final class Transaction_Entry {

		public final static String TABLE_NAME = "expenses";

		public final static String ID = "id";
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
			+ Transaction_Entry.ID
			+ " INTEGER PRIMARY KEY, "
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
}
