package android.zersey.expense_manger;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.zersey.expense_manger.Data.TransactionDbHelper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import retrofit2.Response;

public class Util {

	public static String twoDigitFormat(Object... args) {
		return String.format(Locale.ENGLISH, "%02d", args);
	}

	public static boolean isEmpty(@Nullable String val) {
		return TextUtils.isEmpty(val) || val.equalsIgnoreCase("null");
	}

	public static void setLockPassword(Context context, @NonNull String pass) {
		SharedPreferences preferences =
			context.getSharedPreferences("locker", Context.MODE_PRIVATE);
		preferences.edit().putString("pass", pass).apply();
	}

	public static void getNotesList(Context context, Response<JsonObject> response) {
		HandleNotes2 notes = new HandleNotes2();
		notes.context = context;
		notes.execute(response);
	}

	static class HandleNotes2 extends AsyncTask {

		Context context;
		boolean working = false;

		@Override protected void onPreExecute() {
			super.onPreExecute();
			((Main2Activity) context).showProgress("Updating notes...");
		}

		@Override protected Object doInBackground(Object[] objects) {

			Response<JsonObject> response = (Response<JsonObject>) objects[0];
			final TransactionDbHelper rb = new TransactionDbHelper(context);
			working = true;

			List<IncomeModel> list = new ArrayList<>();

			try {
				// store attachments, set up the alarm dates, set alarms, add the parent id to all sub reminders;
				//Type 0, 3, 4, 5 need to set alarm dates others are already set properly;

				JsonArray array = response.body().get("entries").getAsJsonArray();
				for (int i = 0; i < array.size(); i++) {
					JsonObject obj = array.get(i).getAsJsonObject();
					IncomeModel model = JsonHandler.handleSingleReminder(obj);
					list.add(model);
					rb.createEntry(model);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override protected void onPostExecute(Object o) {
			super.onPostExecute(o);
			((Main2Activity) context).dismissProgress();
			//Fragment frg = null;
			//frg = ((Main2Activity) context).getSupportFragmentManager().findFragmentByTag("Your_Fragment_TAG");
			//final FragmentTransaction ft = ((Main2Activity) context).getSupportFragmentManager().beginTransaction();
			//ft.detach(frg);
			//ft.attach(frg);
			//ft.commit();
		}
	}
}


