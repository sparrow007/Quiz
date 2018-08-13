package android.zersey.expense_manger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;
import android.zersey.expense_manger.Data.TransactionDbHelper;
import java.util.List;

public class NetworkChangeReceiver extends BroadcastReceiver {

	private boolean flag;

	@Override public void onReceive(final Context context, final Intent intent) {
		final ConnectivityManager connMgr =
			(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		final android.net.NetworkInfo wifi;
		if (connMgr != null) {
			wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			final android.net.NetworkInfo mobile =
				connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

			if (NetworkUtil.hasInternetConnection(context)) {
				Log.d("Network Available ", "Flag No 1");
				flag = true;
				syncWithServer(context);
			} else {
				Log.d("Network Available ", "Flag No 0");
				flag = false;
			}
			//if ((wifi.isAvailable() || mobile.isAvailable()) && !flag) {
			//	// Do something
			//
			//} else {
			//
			//}
		}
	}

	private void syncWithServer(Context context) {
		TransactionDbHelper dbHelper = new TransactionDbHelper(context);
		List<Temp> list = dbHelper.getTempEntries();

		for (Temp temp : list) {
			if (flag) {
				IncomeModel model = dbHelper.findEntryWithTemp(temp);
				switch (temp.getAction()) {
					case "new":
						new ServerUtil(context).createEntry(model);
						break;

					case "edit":
						new ServerUtil(context).updateEntry(model);
						break;

					case "delete":
						new ServerUtil(context).deleteEntry(temp.getOnlineId());
						break;
				}
				dbHelper.deleteTemp(temp);
			}
		}
	}
}