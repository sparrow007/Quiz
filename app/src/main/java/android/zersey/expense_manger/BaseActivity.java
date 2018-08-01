package android.zersey.expense_manger;

import android.support.v7.app.AppCompatActivity;
import android.view.Window;

public class BaseActivity extends AppCompatActivity {

	ProgressDialog progressDialog;


	public void showProgress(String message, boolean... isCancelable) throws IllegalStateException {
		if (isDestroyed())
			return;
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
		if (progressDialog != null)
			progressDialog.dismiss();
	}
}
