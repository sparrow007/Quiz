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
import android.zersey.expense_manger.Main2Activity;
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
	}

	@Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void createEntry(final IncomeModel model) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_TITLE, model.getTitle());
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_CATEGORY, model.getCatId());
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_AMOUNT, model.getTotalAmount());
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_DATE_CREATED,
			model.getPaidAtDate());
		values.put(TransactionDbContract.Transaction_Entry.COLUMN_TYPE, model.getType());
		values.put(TransactionDbContract.Transaction_Entry.ID, model.getId());
		long newRowId = db.insert(TransactionDbContract.Transaction_Entry.TABLE_NAME, null, values);

		new Handler(Looper.getMainLooper()).post(new Runnable() {
			public void run() {
				Transactions.adapter.addItem(model);
			}
		});
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
			TransactionDbContract.Transaction_Entry.ID + "=" + model.getId(), null);
		new Handler(Looper.getMainLooper()).post(new Runnable() {
			public void run() {
				Transactions.adapter.updateItem(pos, model);
			}
		});
	}

	public void deleteEntry(final int pos, int id){
		SQLiteDatabase db = getWritableDatabase();
		db.delete(TransactionDbContract.Transaction_Entry.TABLE_NAME,
			TransactionDbContract.Transaction_Entry.ID + " = ?", new String[] { "" + id });

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

	public List<IncomeModel> getAllEntries() {
		SQLiteDatabase db1 = getReadableDatabase();
		List<IncomeModel> list = new ArrayList<>();

		String[] projection = {
			TransactionDbContract.Transaction_Entry.ID,
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
			int currentID =
				cursor.getInt(cursor.getColumnIndex(TransactionDbContract.Transaction_Entry.ID));
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
			list.add(model);
		}
		cursor.close();
		return list;
	}
}
