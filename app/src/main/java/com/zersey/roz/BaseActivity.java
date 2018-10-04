package com.zersey.roz;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import com.zersey.roz.Data.TransactionDbHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class BaseActivity extends AppCompatActivity {

	ProgressDialog progressDialog;

	public void showProgress(String message, boolean... isCancelable) throws IllegalStateException {
		if (isDestroyed()) return;
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
		progressDialog = new ProgressDialog(this);
		progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		progressDialog.setMessage(message);
		if (isCancelable != null && isCancelable.length != 0) {
			progressDialog.setCancelable(isCancelable[0]);
		}
		progressDialog.show();
	}

	public void dismissProgress() throws IllegalStateException {
        /*if (isDestroyed())
            return;*/
		if (progressDialog != null) progressDialog.dismiss();
	}

	private void clearDatabases() {
		TransactionDbHelper rb = TransactionDbHelper.getInstance(this);
		rb.clearDatabase();
	}

	public void logout(){
		RestAdapterAPI api = NetworkUtil.getRestAdapter(BaseActivity.this);
		Call<Void> result = api.logout();
		result.enqueue(new Callback<Void>() {
			@Override
			public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
				try {
					Util.clearPreference(getBaseContext());
					clearDatabases();
					Intent i = new Intent(BaseActivity.this, LoginActivity.class);
					startActivity(i);
					finish();
				} catch (Exception e) {
					e.printStackTrace();
				}
				dismissProgress();
			}

			@Override
			public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
				dismissProgress();
			}
		});
	}
}
