package com.zersey.roz.Data;

import android.provider.BaseColumns;

public class TransactionDbContract {

	private TransactionDbContract() {
	}

	public static final class Transaction_Entry implements BaseColumns {

		public final static String TABLE_NAME = "expenses";

		public final static String COLUMN_ONLINE_ID = "online_id";
		public static final String COLUMN_UUID = "uuid";
		public static final String COLUMN_GROUP_ID = "group_id";
		public final static String COLUMN_TITLE = "title";
		public static final String COLUMN_DESCRIPTION = "desc";
		public final static String COLUMN_CATEGORY = "category";
		public final static String COLUMN_TOTAL_AMOUNT = "total_amount";
		public final static String COLUMN_AMOUNT_PAID = "amount_paid";
		public final static String COLUMN_PAY_DATE = "pay_date";
		public final static String COLUMN_DATE_CREATED = "date_created";
		public final static String COLUMN_DATE_UPDATED = "date_updated";
		public static final String COLUMN_TYPE = "type";
		public static final String COLUMN_AMOUNT_DUE = "amount_due";
		public static final String COLUMN_PAYER_ID = "payer_id";
		public static final String COLUMN_INVOICE_ID = "invoice_id";
		public static final String COLUMN_PAYER_NUMBERS = "payer_numbers";
		public static final String COLUMN_PAYER_NAMES = "payer_names";

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
				+ Transaction_Entry.COLUMN_DESCRIPTION
				+ " TEXT, "
				+ Transaction_Entry.COLUMN_CATEGORY
				+ " TEXT, "
				+ Transaction_Entry.COLUMN_TOTAL_AMOUNT
				+ " TEXT, "
				+ Transaction_Entry.COLUMN_PAY_DATE
				+ " TEXT, "
				+ Transaction_Entry.COLUMN_DATE_CREATED
				+ " TEXT, "
				+ Transaction_Entry.COLUMN_DATE_UPDATED
				+ " TEXT, "
				+ COLUMN_PAYER_ID
				+ " TEXT, "
				+ COLUMN_PAYER_NUMBERS
				+ " TEXT, "
				+ COLUMN_PAYER_NAMES
				+ " TEXT, "
				+ COLUMN_AMOUNT_DUE
				+ " TEXT, "
				+ COLUMN_AMOUNT_PAID
				+ " TEXT, "
				+ COLUMN_INVOICE_ID
				+ " TEXT, "
				+ Transaction_Entry.COLUMN_GROUP_ID
				+ " INTEGER, "
				+ Transaction_Entry.COLUMN_UUID
				+ " TEXT);";
	}

	public static final class TempEntry implements BaseColumns {

		public static final String TABLE_NAME = "temp_table";
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

	public static final class GroupEntry implements BaseColumns {

		public static final String TABLE_NAME = "groups";
		public static final String COLUMN_GROUP_ID = "group_id";
		public static final String COLUMN_GROUP_NAME = "group_name";
		public static final String COLUMN_GROUP_DESC = "group_desc";
		public static final String COLUMN_USERS = "users";
		public static final String COLUMN_TYPE_ID = "type_id";
		public static final String COLUMN_MOBILE_NUMBER = "mobile_no";
		public static final String COLUMN_FULL_NAME = "full_name";
		public static final String COLUMN_CREATED_AT = "created_at";
		public static final String COLUMN_UPDATED_AT = "updated_at";
		public static final String COLUMN_GROUP_SETTINGS = "group_settings";

		public static final String SQL_CREATE_GROUP_TABLE = "CREATE TABLE "
				+ TABLE_NAME
				+ " ( "
				+ GroupEntry._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COLUMN_GROUP_ID
				+ " INTEGER, "
				+ COLUMN_TYPE_ID
				+ " INTEGER, "
				+ COLUMN_GROUP_NAME
				+ " TEXT, "
				+ COLUMN_GROUP_DESC
				+ " TEXT, "
				+ COLUMN_FULL_NAME
				+ " TEXT, "
				+ COLUMN_MOBILE_NUMBER
				+ " TEXT, "
				+ COLUMN_USERS
				+ " TEXT, "
				+ COLUMN_CREATED_AT
				+ " TEXT, "
				+ COLUMN_UPDATED_AT
				+ " TEXT, "
				+ COLUMN_GROUP_SETTINGS
				+ " TEXT"
				+ ");";
	}

	public static final class ContactEntry implements BaseColumns {

		public static final String TABLE_NAME = "platform_users";
		public static final String COLUMN_USER_ID = "user_id";
		public static final String COLUMN_NAME = "name";
		public static final String COLUMN_NUMBER = "number";

		public static final String SQL_CREATE_CONTACTS_TABLE = "CREATE TABLE "
				+ TABLE_NAME
				+ " ( "
				+ ContactEntry._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ COLUMN_NAME
				+ " TEXT, "
				+ COLUMN_NUMBER
				+ " TEXT UNIQUE, "
				+ COLUMN_USER_ID
				+ " INTEGER);";
	}

	public static final class NoteEntry implements BaseColumns {
		public static final String TABLE_NAME = "group_notes";
		public static final String COLUMN_GROUP_ID = "group_id";
		public static final String COLUMN_TITLE = "note_title";
		public static final String COLUMN_NOTE_ID = "note_id";
		public static final String COLUMN_DESCRIPTION = "note_description";
		public static final String COLUMN_DATE_CREATED = "note_created_date";
		public static final String COLUMN_DATE_REMINDER = "note_reminder_date";
		public static final String COLUMN_NOTE_CREATOR = "note_creator";
		public static final String COLUMN_NOTE_ASSIGNED = "note_assigned";

		public static final String SQL_CREATE_NOTES_TABLE = "CREATE TABLE "
				+ NoteEntry.TABLE_NAME
				+ " ( "
				+ NoteEntry._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ NoteEntry.COLUMN_NOTE_ID
				+ " INTEGER, "
				+ COLUMN_TITLE
				+ " TEXT, "
				+ COLUMN_DESCRIPTION
				+ " TEXT, "
				+ COLUMN_NOTE_CREATOR
				+ " INTEGER, "
				+ COLUMN_NOTE_ASSIGNED
				+ " INTEGER, "
				+ COLUMN_DATE_CREATED
				+ " DATE, "
				+ COLUMN_DATE_REMINDER
				+ " DATE, "
				+ COLUMN_GROUP_ID
				+ " INTEGER, "
				+ "FOREIGN KEY ("
				+ COLUMN_GROUP_ID
				+ ") REFERENCES "
				+ GroupEntry.TABLE_NAME
				+ "("
				+ GroupEntry._ID
				+ "));";
	}
}
