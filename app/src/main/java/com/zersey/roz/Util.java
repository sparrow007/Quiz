package com.zersey.roz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.zersey.roz.Data.TransactionDbHelper;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
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

	private static String convertToHex(byte[] data) {
		StringBuilder buf = new StringBuilder();
		for (byte b : data) {
			int halfbyte = (b >>> 4) & 0x0F;
			int two_halfs = 0;
			do {
				buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte)
					: (char) ('a' + (halfbyte - 10)));
				halfbyte = b & 0x0F;
			} while (two_halfs++ < 1);
		}
		return buf.toString();
	}

	public static String SHA1(String text)
		throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		byte[] textBytes = text.getBytes("iso-8859-1");
		md.update(textBytes, 0, textBytes.length);
		byte[] sha1hash = md.digest();
		return convertToHex(sha1hash);
	}

	public static List<BillModel> getNotesList(Context context, Response<JsonObject> response,
		boolean groupEntries) {
		List<BillModel> list = new ArrayList<>();
		try {
			JsonArray array = response.body().get("entries").getAsJsonArray();
			for (int i = 0; i < array.size(); i++) {
				JsonObject obj = array.get(i).getAsJsonObject();
				BillModel model = JsonHandler.handleSingleReminder(obj);
				if (groupEntries && (model.getType().equals("gt") || Util.isEmpty(
					model.getType()))) {
					list.add(model);
				} else if (!groupEntries && !Util.isEmpty(model.getType())) list.add(model);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TransactionDbHelper dbHelper = TransactionDbHelper.getInstance(context);
		dbHelper.addTransactions(list, groupEntries);
		return list;
	}

	public static List<TaskModel> parseTaskresponse(JsonObject response) {
		List<TaskModel> list = new ArrayList<>();
		try {

			JsonArray array = response.get("groups").getAsJsonArray();
			for (int i = 0; i < array.size(); i++) {
				JsonObject obj = array.get(i).getAsJsonObject();
				TaskModel model = new TaskModel();
				model.setTask_Id(obj.get("id").getAsLong());
				model.setTask_Title(obj.get("item_title").getAsString());
				model.setGroup_Id(obj.get("group_id").getAsLong());
				model.setAssignedBy(obj.get("assigned_by").getAsLong());
				if (!obj.get("item_description").isJsonNull()) {
					model.setTask_Des(obj.get("item_description").getAsString());
				}
				if (!obj.get("assigned_to").isJsonNull()) {
					model.setAssignedTo(obj.get("assigned_to").getAsLong());
				}
				if (!obj.get("reminder_date").isJsonNull()) {
					model.setTask_Date(obj.get("reminder_date").getAsString());
				}
				list.add(model);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public static List<GroupModel> parseGroupResponse(JsonObject response) {

		List<GroupModel> list = new ArrayList<>();
		try {

			JsonArray array = response.get("groups").getAsJsonArray();
			for (int i = 0; i < array.size(); i++) {
				String temp = null;
				JsonObject obj = array.get(i).getAsJsonObject();
				GroupModel model = new GroupModel();
				model.setGroupId(obj.get("id").getAsLong());
				model.setGroupName(obj.get("group_name").getAsString());
				if (!obj.get("group_description").isJsonNull()) {
					model.setGroupDesc(obj.get("group_description").getAsString());
				}
				if (!obj.get("users").isJsonNull()) model.setUsers(obj.get("users").getAsString());
				if (!obj.get("mobile").isJsonNull()) {
					model.setMobile_no(obj.get("mobile").getAsString());
				}
				if (!obj.get("fullname").isJsonNull()) model.setFullname(obj.get("fullname").getAsString());
				if (!obj.get("type_id").isJsonNull()) {
					model.setTypeId(obj.get("type_id").getAsInt());
				}
				list.add(model);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	static class HandleNotes2 extends AsyncTask {

		Context context;

		@Override protected Object doInBackground(Object[] objects) {

			Response<JsonObject> response = (Response<JsonObject>) objects[0];

			return null;
		}

		@Override protected void onPostExecute(Object o) {
			super.onPostExecute(o);
		}
	}

	public static String generateUuid(String userId)
		throws UnsupportedEncodingException, NoSuchAlgorithmException {
		String s = userId + Calendar.getInstance().getTimeInMillis();
		return SHA1(s);
	}
}


