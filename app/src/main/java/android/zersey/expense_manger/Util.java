package android.zersey.expense_manger;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import java.util.Locale;

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
}

