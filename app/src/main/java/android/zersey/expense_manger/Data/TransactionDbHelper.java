package android.zersey.expense_manger.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.zersey.expense_manger.IncomeModel;
import android.zersey.expense_manger.Temp;
import android.zersey.expense_manger.Transactions;
import java.util.ArrayList;
import java.util.List;

public class TransactionDbHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "Transactions.db";
	private static final int DATABASE_VERSION = 1;

	public TransactionDbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override public void onCreate(SQLiteDatabase db) {
		db.execSQL(TransactionDbContract.Transaction_Entry.SQL_CREATE_TRANSACTIONS_TABLE);
		db.execSQL(TransactionDbContract.TempEntry.SQL_CREATE_TEMP_TABLE);
	}

	@Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public long createEntry(final IncomeModel model) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_TITLE, model.getTitle());
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_CATEGORY, model.getCatId());
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_AMOUNT, model.getTotalAmount());
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_ONLINE_ID, model.getOnlineId());
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_DATE_CREATED,
			model.getPaidAtDate());
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_TYPE, model.getType());
		long newRowId = db.insert(TransactionDbContract.Transaction_Entry.TABLE_NAME, null, values);
		model.setId(newRowId);
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			public void run() {
				Transactions.adapter.addItem(model);
			}
		});
		return newRowId;
	}

	public void updateEntry(final int pos, final IncomeModel model) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_TITLE, model.getTitle());
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_CATEGORY, model.getCatId());
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_AMOUNT, model.getTotalAmount());
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_DATE_CREATED,
			model.getPaidAtDate());
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_TYPE, model.getType());
		db.update(TransactionDbContract.Transaction_Entry.TABLE_NAME, values,
			TransactionDbContract.Transaction_Entry._ID + "=" + model.getId(), null);
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			public void run() {
				Transactions.adapter.updateItem(pos, model);
			}
		});
	}

	public void deleteEntry(final int pos, long id) {
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TransactionDbContract.Transaction_Entry.TABLE_NAME,
			TransactionDbContract.Transaction_Entry._ID + " = ?", new String[] { "" + id });

		new Handler(Looper.getMainLooper()).post(new Runnable() {
			public void run() {
				Transactions.adapter.deleteItem(pos);
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

	public void addOnlineId(IncomeModel model) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_ONLINE_ID, model.getOnlineId());
		// Updating row
		int id = db.update(TransactionDbContract.Transaction_Entry.TABLE_NAME, values,
			TransactionDbContract.Transaction_Entry._ID + "=?",
			new String[] { String.valueOf(model.getId()) });
		if (id > 0) {
			Log.i("hueh", "1 record updated!");
		} else {
			Log.i("hueh", "NO record found!");
		}
	}

	public List<IncomeModel> getAllEntries() {
		SQLiteDatabase db1 = getReadableDatabase();
		List<IncomeModel> list = new ArrayList<>();

		String[] projection = {
			TransactionDbContract.Transaction_Entry._ID,
			TransactionDbContract.Transaction_Entry.COLUMN_ONLINE_ID,
			TransactionDbContract.Transaction_Entry.COLUMN_TYPE,
			TransactionDbContract.Transaction_Entry.COLUMN_TITLE,
			TransactionDbContract.Transaction_Entry.COLUMN_CATEGORY,
			TransactionDbContract.Transaction_Entry.COLUMN_AMOUNT,
			TransactionDbContract.Transaction_Entry.COLUMN_DATE_CREATED,
			TransactionDbContract.Transaction_Entry.COLUMN_DATE_UPDATED,
			TransactionDbContract.Transaction_Entry.COLUMN_IMAGE,
			TransactionDbContract.Transaction_Entry.COLUMN_NOTES
		};

		Cursor cursor =
			db1.query(TransactionDbContract.Transaction_Entry.TABLE_NAME, projection, null, null,
				null, null, null);

		while (cursor.moveToNext()) {
			long currentID =
				cursor.getLong(cursor.getColumnIndex(TransactionDbContract.Transaction_Entry._ID));
			String title = cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_TITLE));
			//Log.d( "current label ",currentName);
			String category = cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_CATEGORY));
			String amount = cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_AMOUNT));
			String type = cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_TYPE));
			String date = cursor.getString(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_DATE_CREATED));
			IncomeModel model = new IncomeModel();
			model.setId(currentID);
			model.setTitle(title);
			model.setTotalAmount(amount);
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

		String[] projection = {
			TransactionDbContract.TempEntry.COLUMN_LOCAL_ID,
			TransactionDbContract.TempEntry.COLUMN_ACTION,
			TransactionDbContract.TempEntry.COLUMN_ONLINE_ID, TransactionDbContract.TempEntry._ID
		};

		Cursor cursor =
			db.query(TransactionDbContract.TempEntry.TABLE_NAME, projection, null, null, null, null,
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

	public IncomeModel findEntryWithTemp(Temp temp) {
		IncomeModel model = new IncomeModel();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor =
			db.rawQuery("select * from expenses where expenses._id=" + temp.getLocalId(), null);

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
			model.setTotalAmount(amount);
			model.setCatId(category);
			model.setPaidAtDate(date);
			model.setType(type);
			model.setOnlineId(cursor.getLong(
				cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.COLUMN_ONLINE_ID)));
		}
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
}