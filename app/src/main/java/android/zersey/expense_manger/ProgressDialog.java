package android.zersey.expense_manger;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import okhttp3.internal.Util;

public class ProgressDialog extends Dialog {

	String message = "Loading...";
	private View view;
	TextView textView;

	public ProgressDialog(@NonNull Context context) {
		super(context);
	}


	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.progress_dialog);
		view = getWindow().getDecorView().findViewById(android.R.id.content);
		textView = findViewById(R.id.text);
		textView.setText(message);
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	protected void onStart() {
		//AnimationUtil.applyZoomEffect(view);
	}

	public void close() {
		//AnimationUtil.applyZoomOutEffect(view);
		dismiss();
	}
}
