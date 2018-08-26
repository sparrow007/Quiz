package android.zersey.expense_manger;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.crashlytics.android.Crashlytics;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity {

	JsonParser parser = new JsonParser();
	List<String> codes;
	private AutoCompleteTextView countryCode;
	private EditText phone;
	final String Signup = "signup";//applicable for variable UserActivity
	final String Login = "login";//applicable for variable UserActivity
	private SharedPreferences prefs;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Fabric.with(this, new Crashlytics());
		setContentView(R.layout.activity_login);

		prefs = getSharedPreferences("login", MODE_PRIVATE);
		if (prefs.getString("userid", null) != null) {
			Intent i = new Intent(this, Main2Activity.class);

			//containing the intent to start discover tab
			//might have started from oneSignal notification
			startActivity(i);
			finish();
		}

		JsonArray countryCodes = parser.parse(CountryCodes.jsonArray).getAsJsonArray();
		codes = new ArrayList<>();
		for (JsonElement countryCode : countryCodes) {
			JsonObject object = countryCode.getAsJsonObject();
			codes.add(
				object.get("iso").getAsString().toUpperCase() + " " + object.get("country_code")
					.getAsString());
		}

		ArrayAdapter<String> adapter =
			new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, codes);
		phone = findViewById(R.id.phone);
		countryCode = findViewById(R.id.country_code);
		countryCode.setAdapter(adapter);
		countryCode.setKeyListener(null);
		countryCode.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View view) {
				countryCode.showDropDown();
			}
		});
		phone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_NEXT) {
					doLoginFieldCheck(null);
				}
				return false;
			}
		});

		askPermissions();
	}

	private void askPermissions() {
		ActivityCompat.requestPermissions(this, new String[] {
			android.Manifest.permission.RECEIVE_SMS, android.Manifest.permission.READ_SMS,
			android.Manifest.permission.READ_CONTACTS, android.Manifest.permission.CALL_PHONE,
			android.Manifest.permission.RECEIVE_BOOT_COMPLETED,
		}, 1);
	}

	@Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
		@NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		for (int result : grantResults) {
			if (result != PackageManager.PERMISSION_GRANTED) {
				new AlertDialog.Builder(this).setIcon(R.mipmap.ic_launcher)
					.setTitle("Info")
					.setMessage(
						"Permissions not granted, app may behave unusual.\nDo you want to retry?")
					.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						@Override public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							askPermissions();
						}
					})
					.setNegativeButton("No", new DialogInterface.OnClickListener() {
						@Override public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					})
					.show();
				return;
			}
		}
	}

	public void doLoginFieldCheck(View view) {
		if (phone.getText().length() == 0) {
			phone.setError("Enter phone number");
			Toast.makeText(this, "Enter phone number", Toast.LENGTH_SHORT).show();
			return;
		}

		String phone = this.phone.getText().toString();
		String country = null;
		try {
			country = countryCode.getText().toString().split(" ")[1];
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "Invalid country code!", Toast.LENGTH_SHORT).show();
		}

		startLogin(country, phone);
	}

	private void startLogin(String country, String phone) {
		showProgress("Checking phone...");
		RestAdapterAPI api = NetworkUtil.getRestAdapter(this);
		Call<JsonObject> result = api.doLogin(country, phone);
		result.enqueue(new Callback<JsonObject>() {
			@Override public void onResponse(@NonNull Call<JsonObject> call,
				@NonNull Response<JsonObject> response) {
				try {
					JsonObject jsonObject = response.body();
					Boolean success = jsonObject.get("success").getAsBoolean();
					String status = jsonObject.get("status").getAsString();
					if (success) {
						if (status.equals("User has already signedIn.")) {
							String userid = jsonObject.get("userid").getAsString();
							JsonObject user = jsonObject.get("user").getAsJsonObject();
							String fullName = user.get("fullname").getAsString();
							SharedPreferences.Editor editor = prefs.edit();
							editor.putString("userid", userid);
							editor.putString("fullname", fullName);
							editor.apply();

							startActivity(new Intent(LoginActivity.this, Main2Activity.class));
							finish();
						} else {
							String navigate = jsonObject.get("navigate").getAsString();
							Intent intent =
								new Intent(LoginActivity.this, PasswordOtpActivity.class);

							switch (navigate) {
								case "login":
									intent.putExtra("UserActivity", Login);
									startActivity(intent);
									finish();
									break;
								case "signup":
									intent.putExtra("UserActivity", Signup);
									startActivity(intent);
									finish();
									break;
								case "verify":
									//                                ResendOTP();
									intent.putExtra("UserActivity", Signup);
									startActivity(intent);
									finish();
									break;
							}
						}
					} else {
						String error;
						if (jsonObject.has("error") && !jsonObject.get("error").isJsonNull()) {
							error = jsonObject.get("error").getAsString();
						} else {
							error = status;
						}
						Toast.makeText(getApplicationContext(), error, Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "Something went wrong!",
						Toast.LENGTH_LONG).show();
				}
				dismissProgress();
			}

			@Override public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
				dismissProgress();
				Toast.makeText(LoginActivity.this, "Network error!", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
