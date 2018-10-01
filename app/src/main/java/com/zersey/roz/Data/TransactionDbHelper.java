package com.zersey.roz.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.zersey.roz.BillModel;
import com.zersey.roz.ContactModel;
import com.zersey.roz.GroupModel;
import com.zersey.roz.Groups;
import com.zersey.roz.TaskModel;
import com.zersey.roz.Temp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class TransactionDbHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "Transactions.db";
	private static final int DATABASE_VERSION = 1;
	private static TransactionDbHelper mInstance;

	private TransactionDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public static TransactionDbHelper getInstance(Context context) {
		if (mInstance == null) {
			synchronized (TransactionDbHelper.class) {
				if (mInstance == null) {
					mInstance = new TransactionDbHelper(context.getApplicationContext());
				}
			}
		}
		return mInstance;
	}

	@Override public void onCreate(SQLiteDatabase db) {
		db.execSQL(TransactionDbContract.Transaction_Entry.SQL_CREATE_TRANSACTIONS_TABLE);
		db.execSQL(TransactionDbContract.GroupEntry.SQL_CREATE_GROUP_TABLE);
		db.execSQL(TransactionDbContract.ContactEntry.SQL_CREATE_CONTACTS_TABLE);
		db.execSQL(TransactionDbContract.NoteEntry.SQL_CREATE_NOTES_TABLE);
		db.execSQL(TransactionDbContract.TempEntry.SQL_CREATE_TEMP_TABLE);
	}

	@Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public long createGroup(GroupModel model) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(TransactionDbContract.GroupEntry.COLUMN_GROUP_ID, model.getGroupId());
		cv.put(TransactionDbContract.GroupEntry.COLUMN_GROUP_NAME, model.getGroupName());
		cv.put(TransactionDbContract.GroupEntry.COLUMN_GROUP_DESC, model.getGroupDesc());
		cv.put(TransactionDbContract.GroupEntry.COLUMN_USERS, model.getUsers());
		cv.put(TransactionDbContract.GroupEntry.COLUMN_TYPE_ID, model.getTypeId());
		cv.put(TransactionDbContract.GroupEntry.COLUMN_MOBILE_NUMBER, model.getMobile_no());
		return db.insert(TransactionDbContract.GroupEntry.TABLE_NAME, null, cv);
	}

	public long updateGroup(GroupModel model) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(TransactionDbContract.GroupEntry.COLUMN_GROUP_ID, model.getGroupId());
		cv.put(TransactionDbContract.GroupEntry.COLUMN_GROUP_NAME, model.getGroupName());
		cv.put(TransactionDbContract.GroupEntry.COLUMN_GROUP_DESC, model.getGroupDesc());
		cv.put(TransactionDbContract.GroupEntry.COLUMN_USERS, model.getUsers());
		cv.put(TransactionDbContract.GroupEntry.COLUMN_TYPE_ID, model.getTypeId());
		return db.update(TransactionDbContract.GroupEntry.TABLE_NAME, cv,
			TransactionDbContract.GroupEntry._ID + "=?",
			new String[] { Long.toString(model.getId()) });
	}

	public void addGroups(List<GroupModel> list) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = new ContentValues();
		db.beginTransaction();
		long count = 0;
		for (GroupModel model : list) {
			cv.put(TransactionDbContract.GroupEntry.COLUMN_GROUP_ID, model.getGroupId());
			cv.put(TransactionDbContract.GroupEntry.COLUMN_GROUP_NAME, model.getGroupName());
			cv.put(TransactionDbContract.GroupEntry.COLUMN_GROUP_DESC, model.getGroupDesc());
			cv.put(TransactionDbContract.GroupEntry.COLUMN_USERS, model.getUsers());
			cv.put(TransactionDbContract.GroupEntry.COLUMN_FULL_NAME, model.getFullname());
			cv.put(TransactionDbContract.GroupEntry.COLUMN_MOBILE_NUMBER, model.getMobile_no());
			cv.put(TransactionDbContract.GroupEntry.COLUMN_TYPE_ID, model.getTypeId());
			count = db.insert(TransactionDbContract.GroupEntry.TABLE_NAME, null, cv);
			cv.clear();
		}
		Log.d("addGroups: ", count + "");
		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public void addTransactions(List<BillModel> list, boolean groupEntries) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		db.beginTransaction();

		for (BillModel model : list) {
			values.put(TransactionDbContract.Transaction_Entry.COLUMN_TITLE, model.getTitle());
			values.put(TransactionDbContract.Transaction_Entry.COLUMN_DESCRIPTION,
				model.getDescription());
			values.put(TransactionDbContract.Transaction_Entry.COLUMN_GROUP_ID, model.getGroupId());
			values.put(TransactionDbContract.Transaction_Entry.COLUMN_UUID, model.getUuid());
			values.put(TransactionDbContract.Transaction_Entry.COLUMN_CATEGORY, model.getCatId());
			values.put(TransactionDbContract.Transaction_Entry.COLUMN_AMOUNT,
				model.getTotalAmount());
			values.put(TransactionDbContract.Transaction_Entry.COLUMN_AMOUNT_DUE,
				model.getAmountDue());
			values.put(TransactionDbContract.Transaction_Entry.COLUMN_TYPE, model.getType());
			values.put(TransactionDbContract.Transaction_Entry.COLUMN_INVOICE_ID,
				model.getInvoiceId());
			values.put(TransactionDbContract.Transaction_Entry.COLUMN_PAYER_ID, model.getPayerId());
			values.put(TransactionDbContract.Transaction_Entry.COLUMN_ONLINE_ID,
				model.getOnlineId());
			values.put(TransactionDbContract.Transaction_Entry.COLUMN_DATE_CREATED,
				model.getPaidAtDate());
			db.insert(TransactionDbContract.Transaction_Entry.TABLE_NAME, null, values);
			values.clear();
		}

		db.setTransactionSuccessful();
		db.endTransaction();
		if(!groupEntries){
			Groups.groupsAdapter.addAll(list);
		}
	}

	public long createEntry(final BillModel model) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_TITLE, model.getTitle());
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_DESCRIPTION,
			model.getDescription());
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_GROUP_ID, model.getGroupId());
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_UUID, model.getUuid());
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_CATEGORY, model.getCatId());
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_AMOUNT, model.getTotalAmount());
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_AMOUNT_DUE, model.getAmountDue());
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_TYPE, model.getType());
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_INVOICE_ID, model.getInvoiceId());
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_PAYER_ID, model.getPayerId());
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_ONLINE_ID, model.getOnlineId());
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_DATE_CREATED,
			model.getPaidAtDate());
		long newRowId = db.insert(TransactionDbContract.Transaction_Entry.TABLE_NAME, null, values);
		model.setId(newRowId);
		return newRowId;
	}

	public void updateEntry(final int pos, final BillModel model) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_TITLE, model.getTitle());
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_CATEGORY, model.getCatId());
		//values.put(TransactionDbContract.Transaction_Entry.COLUMN_AMOUNT, model.getTotalAmount());
		//values.put(TransactionDbContract.Transaction_Entry.COLUMN_DATE_CREATED,
		//	model.getPaidAtDate());
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_TYPE, model.getType());
		db.update(TransactionDbContract.Transaction_Entry.TABLE_NAME, values,
			TransactionDbContract.Transaction_Entry._ID + "=" + model.getId(), null);
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			public void run() {
				//Transactions.groupsAdapter.updateItem(pos, model);
				Groups.groupsAdapter.updateItem(pos, model);
			}
		});
	}

	public void Update_GroupMembers(final int pos, final BillModel model) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_TITLE, model.getTitle());
		//values.put(TransactionDbContract.Transaction_Entry.COLUMN_CATEGORY, model.getCatId());
		//values.put(TransactionDbContract.Transaction_Entry.COLUMN_AMOUNT, model.getTotalAmount());
		//values.put(TransactionDbContract.Transaction_Entry.COLUMN_DATE_CREATED,
		//	model.getPaidAtDate());
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_TYPE, model.getType());
		db.update(TransactionDbContract.Transaction_Entry.TABLE_NAME, values,
			TransactionDbContract.Transaction_Entry._ID + "=" + model.getId(), null);
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			public void run() {
				//Transactions.groupsAdapter.updateItem(pos, model);
				Groups.groupsAdapter.updateItem(pos, model);
			}
		});
	}

	public void deleteEntry(final int pos, long id) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TransactionDbContract.Transaction_Entry.TABLE_NAME,
			TransactionDbContract.Transaction_Entry._ID + " = ?", new String[] { "" + id });

		new Handler(Looper.getMainLooper()).post(new Runnable() {
			public void run() {
				//Transactions.groupsAdapter.deleteItem(pos);
				Groups.groupsAdapter.deleteItem(pos);
			}
		});
	}

	public int getEntriesCount() {
		String countQuery = "SELECT * FROM " + TransactionDbContract.Transaction_Entry.TABLE_NAME;
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();
		//        db.close();

		return count;
	}

	public int getGroupsCount() {
		String countQuery = "SELECT * FROM " + TransactionDbContract.GroupEntry.TABLE_NAME;
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.moveToFirst();
		long currentID =
			cursor.getLong(cursor.getColumnIndex(TransactionDbContract.GroupEntry._ID));
		String title = cursor.getString(
			cursor.getColumnIndex(TransactionDbContract.GroupEntry.COLUMN_GROUP_NAME));
		String desc = cursor.getString(
			cursor.getColumnIndex(TransactionDbContract.GroupEntry.COLUMN_GROUP_DESC));
		long groupId =
			cursor.getLong(cursor.getColumnIndex(TransactionDbContract.GroupEntry.COLUMN_GROUP_ID));
		String usersGroup =
			cursor.getString(cursor.getColumnIndex(TransactionDbContract.GroupEntry.COLUMN_USERS));
		String fullnameGroup = cursor.getString(
			cursor.getColumnIndex(TransactionDbContract.GroupEntry.COLUMN_FULL_NAME));
		String mobileGroup = cursor.getString(
			cursor.getColumnIndex(TransactionDbContract.GroupEntry.COLUMN_MOBILE_NUMBER));
		int te =
			cursor.getInt(cursor.getColumnIndex(TransactionDbContract.GroupEntry.COLUMN_TYPE_ID));
		Log.d("getGroupsCount: ", currentID
			+ " "
			+ title
			+ " "
			+ desc
			+ " "
			+ groupId
			+ " "
			+ usersGroup
			+ " "
			+ fullnameGroup
			+ " "
			+ mobileGroup
			+ " "
			+ te);
		cursor.close();
		//        db.close();

		return count;
	}

	public void addOnlineId(GroupModel model) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TransactionDbContract.GroupEntry.COLUMN_GROUP_ID, model.getGroupId());
		// Updating row
		int id = db.update(TransactionDbContract.GroupEntry.TABLE_NAME, values,
			TransactionDbContract.GroupEntry._ID + "=?",
			new String[] { String.valueOf(model.getId()) });
	}

	public long addGroupNotes(TaskModel model) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(TransactionDbContract.NoteEntry.COLUMN_TITLE, model.getTask_Title());
		cv.put(TransactionDbContract.NoteEntry.COLUMN_DESCRIPTION, model.getTask_Des());
		cv.put(TransactionDbContract.NoteEntry.COLUMN_DATE_REMINDER, model.getTask_Date());
		cv.put(TransactionDbContract.NoteEntry.COLUMN_GROUP_ID, model.getGroup_Id());
		cv.put(TransactionDbContract.NoteEntry.COLUMN_NOTE_ASSIGNED, model.getAssignedTo());
		return db.insert(TransactionDbContract.NoteEntry.TABLE_NAME, null, cv);
	}

	public void addTasks(List<TaskModel> taskList) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = new ContentValues();
		db.beginTransaction();

		for (TaskModel model : taskList) {
			cv.put(TransactionDbContract.NoteEntry.COLUMN_TITLE, model.getTask_Title());
			cv.put(TransactionDbContract.NoteEntry.COLUMN_DESCRIPTION, model.getTask_Des());
			cv.put(TransactionDbContract.NoteEntry.COLUMN_DATE_REMINDER, model.getTask_Date());
			cv.put(TransactionDbContract.NoteEntry.COLUMN_GROUP_ID, model.getGroup_Id());
			cv.put(TransactionDbContract.NoteEntry.COLUMN_NOTE_ASSIGNED, model.getAssignedTo());
			cv.put(TransactionDbContract.NoteEntry.COLUMN_NOTE_ID, model.getTask_Id());
			cv.put(TransactionDbContract.NoteEntry.COLUMN_NOTE_CREATOR, model.getAssignedBy());
			db.insert(TransactionDbContract.NoteEntry.TABLE_NAME, null, cv);
			cv.clear();
		}

		db.setTransactionSuccessful();
		db.endTransaction();
	}

	public List<TaskModel> getTask(long Group_id) {
		SQLiteDatabase db1 = getReadableDatabase();
		List<TaskModel> list = new ArrayList<>();
		Cursor cursor;
		if (Group_id < 0) {
			cursor =
				db1.query(TransactionDbContract.NoteEntry.TABLE_NAME, null, null, null, null, null,
					null);
		} else {
			cursor = db1.query(TransactionDbContract.NoteEntry.TABLE_NAME, null,
				TransactionDbContract.NoteEntry.COLUMN_GROUP_ID + "=" + Long.toString(Group_id),
				null, null, null, null);
		}
		while (cursor.moveToNext()) {
			long currentID =
				cursor.getLong(cursor.getColumnIndex(TransactionDbContract.NoteEntry._ID));
			long groupID = cursor.getLong(
				cursor.getColumnIndex(TransactionDbContract.NoteEntry.COLUMN_GROUP_ID));

			String title = cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.NoteEntry.COLUMN_TITLE));
			String desc = cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.NoteEntry.COLUMN_DESCRIPTION));

			String date = cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.NoteEntry.COLUMN_DATE_REMINDER));

			TaskModel model = new TaskModel();
			model.setTask_Title(title);
			model.setTask_Des(desc);
			model.setTask_Date(date);
			model.setTask_Checked(false);

			model.setTask_Id(currentID);
			model.setGroup_Id(groupID);

			model.setTask_Id(cursor.getLong(
				cursor.getColumnIndex(TransactionDbContract.NoteEntry.COLUMN_NOTE_ID)));
			model.setAssignedTo(cursor.getLong(
				cursor.getColumnIndex(TransactionDbContract.NoteEntry.COLUMN_NOTE_ASSIGNED)));
			model.setAssignedBy(cursor.getLong(
				cursor.getColumnIndex(TransactionDbContract.NoteEntry.COLUMN_NOTE_CREATOR)));

			list.add(model);
		}
		Log.d("getTask: ", "" + list.size());
		cursor.close();
		return list;
	}

	public int getGroupNotesCount() {
		String countQuery = "SELECT * FROM " + TransactionDbContract.NoteEntry.TABLE_NAME;
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();
		//        db.close();
		return count;
	}

	public List<GroupModel> getGroups(int type) {
		SQLiteDatabase db1 = getReadableDatabase();
		List<GroupModel> list = new ArrayList<>();

		Cursor cursor =
			db1.query(TransactionDbContract.GroupEntry.TABLE_NAME, null, null, null, null, null,
				null);
		int i = cursor.getCount();
		Log.d("getGroups: ", i + "");

		while (cursor.moveToNext()) {

			long currentID =
				cursor.getLong(cursor.getColumnIndex(TransactionDbContract.GroupEntry._ID));
			String title = cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.GroupEntry.COLUMN_GROUP_NAME));
			String desc = cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.GroupEntry.COLUMN_GROUP_DESC));
			long groupId = cursor.getLong(
				cursor.getColumnIndex(TransactionDbContract.GroupEntry.COLUMN_GROUP_ID));
			String usersGroup = cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.GroupEntry.COLUMN_USERS));
			String fullnameGroup = cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.GroupEntry.COLUMN_FULL_NAME));
			String mobileGroup = cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.GroupEntry.COLUMN_MOBILE_NUMBER));

			GroupModel model = new GroupModel();
			model.setId(currentID);
			model.setTypeId(cursor.getInt(
				cursor.getColumnIndex(TransactionDbContract.GroupEntry.COLUMN_TYPE_ID)));
			model.setGroupName(title);
			model.setGroupDesc(desc);
			model.setGroupId(groupId);
			model.setUsers(usersGroup);
			model.setFullname(fullnameGroup);
			model.setMobile_no(mobileGroup);
			list.add(model);
		}
		cursor.close();
		return list;
	}

	public ArrayList<BillModel> getAllEntries() {
		SQLiteDatabase db1 = getReadableDatabase();
		ArrayList<BillModel> list = new ArrayList<>();

		Cursor cursor = db1.rawQuery(
			"SELECT * FROM expenses, groups WHERE groups.group_id=expenses.group_id AND groups.type_id=1",
			null);

		while (cursor.moveToNext()) {
			long currentID =
				cursor.getLong(cursor.getColumnIndex(TransactionDbContract.Transaction_Entry._ID));
			String title = cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_TITLE));
			String category = cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_CATEGORY));
			String amount = cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_AMOUNT));
			String type = cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_TYPE));
			String date = cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_DATE_CREATED));
			BillModel model = new BillModel();
			model.setId(currentID);
			model.setGroupId(cursor.getLong(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_GROUP_ID)));
			model.setTitle(title);
			model.setTotalAmount(amount);
			model.setUuid(cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_UUID)));
			model.setAmountDue(cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_AMOUNT_DUE)));
			model.setDescription(cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_DESCRIPTION)));
			model.setPayerId(cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_PAYER_ID)));
			model.setInvoiceId(cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_INVOICE_ID)));
			model.setCatId(category);
			model.setPaidAtDate(date);
			model.setType(type);
			model.setOnlineId(cursor.getLong(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_ONLINE_ID)));
			list.add(model);
		}
		cursor.close();
		Log.d("huehue", "getAllEntries: " + list.size());
		return list;
	}

	public void addToTemp(long localId, long onlineId, String action) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(TransactionDbContract.TempEntry.COLUMN_LOCAL_ID, localId);
		cv.put(TransactionDbContract.TempEntry.COLUMN_ACTION, action);
		cv.put(TransactionDbContract.TempEntry.COLUMN_ONLINE_ID, onlineId);
		db.insert(TransactionDbContract.TempEntry.TABLE_NAME, null, cv);
		//printTempDb();
	}

	public List<Temp> getTempEntries() {
		SQLiteDatabase db = getReadableDatabase();
		List<Temp> list = new ArrayList<>();

		Cursor cursor =
			db.query(TransactionDbContract.TempEntry.TABLE_NAME, null, null, null, null, null,
				null);

		while (cursor.moveToNext()) {
			Temp temp = new Temp();
			temp.setId(cursor.getLong(cursor.getColumnIndex(TransactionDbContract.TempEntry._ID)));
			temp.setLocalId(cursor.getLong(
				cursor.getColumnIndex(TransactionDbContract.TempEntry.COLUMN_LOCAL_ID)));
			temp.setAction(cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.TempEntry.COLUMN_ACTION)));
			temp.setOnlineId(cursor.getLong(
				cursor.getColumnIndex(TransactionDbContract.TempEntry.COLUMN_ONLINE_ID)));
			list.add(temp);
		}
		cursor.close();
		return list;
	}

	public BillModel findEntryWithTemp(Temp temp) {
		BillModel model = new BillModel();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(TransactionDbContract.Transaction_Entry.TABLE_NAME, null,
			TransactionDbContract.Transaction_Entry._ID + "=" + temp.getLocalId(), null, null, null,
			null);

		while (cursor.moveToNext()) {
			long currentID =
				cursor.getLong(cursor.getColumnIndex(TransactionDbContract.Transaction_Entry._ID));
			String title = cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_TITLE));
			String category = cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_CATEGORY));
			String amount = cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_AMOUNT));
			String type = cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_TYPE));
			String date = cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_DATE_CREATED));
			model.setId(currentID);
			model.setTitle(title);
			//model.setTotalAmount(amount);
			model.setCatId(category);
			//model.setPaidAtDate(date);
			model.setType(type);
			model.setOnlineId(cursor.getLong(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_ONLINE_ID)));
		}
		cursor.close();
		return model;
	}

	public void deleteTemp(Temp temp) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TransactionDbContract.TempEntry.TABLE_NAME,
			TransactionDbContract.TempEntry._ID + " = ?", new String[] { "" + temp.getId() });
		//printTempDb();
	}

	public void printTempDb() {
		List<Temp> tempList = getTempEntries();
		for (Temp temp : tempList) {
			Log.d("hueh", "printTempDb: " + temp.toString());
		}
	}

	public void addTransactionOnlineId(BillModel model) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_ONLINE_ID, model.getOnlineId());
		// Updating row
		int id = db.update(TransactionDbContract.Transaction_Entry.TABLE_NAME, values,
			TransactionDbContract.Transaction_Entry._ID + "=?",
			new String[] { String.valueOf(model.getId()) });
	}

	public List<BillModel> getGroupEntries(long groupId) {
		List<BillModel> list = new ArrayList<>();
		SQLiteDatabase db1 = getReadableDatabase();

		Cursor cursor =
			db1.rawQuery("SELECT * FROM expenses WHERE expenses.group_id=" + Long.toString(groupId),
				null);

		while (cursor.moveToNext()) {
			long currentID =
				cursor.getLong(cursor.getColumnIndex(TransactionDbContract.Transaction_Entry._ID));
			String title = cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_TITLE));
			String category = cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_CATEGORY));
			String amount = cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_AMOUNT));
			String type = cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_TYPE));
			String date = cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_DATE_CREATED));
			BillModel model = new BillModel();
			model.setId(currentID);
			model.setGroupId(cursor.getLong(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_GROUP_ID)));
			model.setTitle(title);
			model.setTotalAmount(amount);
			model.setUuid(cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_UUID)));
			model.setAmountDue(cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_AMOUNT_DUE)));
			model.setDescription(cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_DESCRIPTION)));
			model.setPayerId(cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_PAYER_ID)));
			model.setInvoiceId(cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_INVOICE_ID)));
			model.setCatId(category);
			model.setPaidAtDate(date);
			model.setType(type);
			model.setOnlineId(cursor.getLong(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_ONLINE_ID)));
			list.add(model);
		}
		cursor.close();
		return list;
	}

	public long addContact(ContactModel model) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(TransactionDbContract.ContactEntry.COLUMN_NAME, model.getName());
		cv.put(TransactionDbContract.ContactEntry.COLUMN_NUMBER, model.getNumber());
		long id = 0;
		try {
			id = db.insertOrThrow(TransactionDbContract.ContactEntry.TABLE_NAME, null, cv);
		} catch (Exception ignored) {

		}
		return id;
	}

	public int getContactCount() {
		String countQuery = "SELECT * FROM " + TransactionDbContract.ContactEntry.TABLE_NAME;
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
		cursor.close();
		return count;
	}

	public List<ContactModel> getContacts() {
		SQLiteDatabase db = getReadableDatabase();
		List<ContactModel> list = new ArrayList<>();
		List<ContactModel> listWithoutId = new ArrayList<>();
		Cursor cursor =
			db.query(TransactionDbContract.ContactEntry.TABLE_NAME, null, null, null, null, null,
				null, null);
		while (cursor.moveToNext()) {
			ContactModel model = new ContactModel();
			model.setId(
				cursor.getLong(cursor.getColumnIndex(TransactionDbContract.ContactEntry._ID)));
			model.setName(cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.ContactEntry.COLUMN_NAME)));
			model.setNumber(cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.ContactEntry.COLUMN_NUMBER)));
			model.setUserId(cursor.getLong(
				cursor.getColumnIndex(TransactionDbContract.ContactEntry.COLUMN_USER_ID)));

			if (model.getUserId() == 0) {
				listWithoutId.add(model);
			} else {
				list.add(model);
			}
		}
		cursor.close();

		Collections.sort(list, new Comparator<ContactModel>() {
			@Override public int compare(ContactModel contactModel, ContactModel t1) {
				return contactModel.getName().compareTo(t1.getName());
			}
		});

		Collections.sort(listWithoutId, new Comparator<ContactModel>() {
			@Override public int compare(ContactModel contactModel, ContactModel t1) {
				return contactModel.getName().compareTo(t1.getName());
			}
		});

		list.addAll(listWithoutId);
		return list;
	}

	public void updateUserId(long rowId, ContactModel model) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(TransactionDbContract.ContactEntry.COLUMN_USER_ID, model.getUserId());
		db.update(TransactionDbContract.ContactEntry.TABLE_NAME, cv,
			TransactionDbContract.ContactEntry._ID + "=?", new String[] { Long.toString(rowId) });
	}

	public List<ContactModel> getUserWithUserId(String[] userIds) {
		SQLiteDatabase db = getReadableDatabase();
		List<ContactModel> list = new ArrayList<>();
		Boolean Flag = false;

		StringBuilder query = new StringBuilder(userIds[0]);
		for (int i = 1; i < userIds.length; i++) {
			query.append(",").append(userIds[i]);
		}

		Cursor cursor = db.rawQuery("SELECT * FROM "
			+ TransactionDbContract.ContactEntry.TABLE_NAME
			+ " WHERE "
			+ TransactionDbContract.ContactEntry.COLUMN_USER_ID
			+ " IN ("
			+ query.toString()
			+ ");", null);
		int t = cursor.getCount();
		Log.d("getUserWithUserId: ", t + "");
		while (cursor.moveToNext()) {
			ContactModel model = new ContactModel();
			model.setId(
				cursor.getLong(cursor.getColumnIndex(TransactionDbContract.ContactEntry._ID)));
			model.setName(cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.ContactEntry.COLUMN_NAME)));
			model.setNumber(cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.ContactEntry.COLUMN_NUMBER)));
			model.setUserId(cursor.getLong(
				cursor.getColumnIndex(TransactionDbContract.ContactEntry.COLUMN_USER_ID)));

			list.add(model);
		}

		Log.d("onCreate: ", list.size() + "");
		/*for(int i=0;i<userIds.length;i++){
			for(int j=0;j<list.size();j++){
				if(TextUtils.equals(userIds[i],list.get(j).getUserId()+"")){
                   Flag=true;
				}else {
                   Flag=false;
				}
			}
			if(!Flag){
				ContactModel model=new ContactModel();
				model.setName(userIds[i]+"7011");
				model.setNumber(userIds[i]+"7011");
				model.setUserId(Long.parseLong(userIds[i]));
				list.add(model);
			}
		}*/
		cursor.close();
		return list;
	}

	public void updateUserIds(HashMap<String, Long> map) {
		SQLiteDatabase db = getWritableDatabase();
		for (String key : map.keySet()) {
			ContentValues cv = new ContentValues();
			cv.put(TransactionDbContract.ContactEntry.COLUMN_USER_ID, map.get(key));
			db.update(TransactionDbContract.ContactEntry.TABLE_NAME, cv,
				TransactionDbContract.ContactEntry.COLUMN_NUMBER + "=?", new String[] { key });
		}
	}

	public void addOnlineIdForNote(TaskModel model) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TransactionDbContract.NoteEntry.COLUMN_NOTE_ID, model.getTask_Id());
		int id = db.update(TransactionDbContract.NoteEntry.TABLE_NAME, values,
			TransactionDbContract.NoteEntry._ID + "=?",
			new String[] { String.valueOf(model.getId()) });
	}
}
