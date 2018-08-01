package android.zersey.expense_manger;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.JsonObject;
import java.util.Date;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Typeface.BOLD;

public class PasswordOtpActivity extends BaseActivity {

	private static final String TAG = PasswordOtpActivity.class.getSimpleName();
	//common widgets
	ImageView back;

	View forget_password_layout, signup_layout, login_layout;

	//signup widgets
	ImageView show_hide_password;
	private EditText otp1, otp2, otp3, otp4, otp5, otp6;//global To-Do otp reader
	EditText password, name, dob;
	TextView otp_by_voice, resend_otp, proceed, wrongOtp;

	//login widgets
	EditText password_login;
	ImageView show_hide_password_login;
	TextView proceed_login, error_message, forgot_pass, first, rest;

	//constants
	final String Signup = "signup";//applicable for variable UserActivity

	//variables
	String UserActivity;
	Boolean isSignupPasswordShown = false;
	Boolean isLoginPasswordShown = false;
	SharedPreferences prefs;
	Boolean isResetPass = false;
	private String rewardId;
	protected String actualReward;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_otp);
		//initializing shared preferences
		prefs = getSharedPreferences("login", MODE_PRIVATE);
		// getting user activity type weather signup or login from intent
		UserActivity = getIntent().getStringExtra("UserActivity");

		first = (TextView) findViewById(R.id.first);
		rest = (TextView) findViewById(R.id.rest);
		//Typeface type = Typeface.createFromAsset(getAssets(), "RobotoThin.ttf");
		//first.setTypeface(type, BOLD);
		//rest.setTypeface(type);

		//back button
		back = (ImageView) findViewById(R.id.back);

		forget_password_layout = findViewById(R.id.forgot_pass_layout);
		signup_layout = findViewById(R.id.signup_layout);
		login_layout = findViewById(R.id.login_layout);

		//signup widgets
		otp1 = (EditText) findViewById(R.id.otp1);
		otp2 = (EditText) findViewById(R.id.otp2);
		otp3 = (EditText) findViewById(R.id.otp3);
		otp4 = (EditText) findViewById(R.id.otp4);
		otp5 = (EditText) findViewById(R.id.otp5);
		otp6 = (EditText) findViewById(R.id.otp6);
		wrongOtp = (TextView) findViewById(R.id.wrongOtp);
		resend_otp = (TextView) findViewById(R.id.resend_otp);
		otp_by_voice = (TextView) findViewById(R.id.otp_by_voice);
		password = (EditText) findViewById(R.id.password_signup);
		show_hide_password = (ImageView) findViewById(R.id.show_hide_password);
		name = (EditText) findViewById(R.id.name_signup);
		dob = (EditText) findViewById(R.id.dob_signup);
		dob.setKeyListener(null);
		proceed = (TextView) findViewById(R.id.proceed);

		//login widgets
		error_message = (TextView) findViewById(R.id.error_message);
		password_login = (EditText) findViewById(R.id.password_login);
		show_hide_password_login = (ImageView) findViewById(R.id.show_hide_password_login);
		proceed_login = (TextView) findViewById(R.id.proceed_login);
		forgot_pass = (TextView) findViewById(R.id.forgot_pass);

		//forgot pass on clickListener
		forgot_pass.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View view) {
				signup_layout.setVisibility(View.VISIBLE);
				login_layout.setVisibility(View.INVISIBLE);
				resendOtp();
				isResetPass = true;
				name.setVisibility(View.INVISIBLE);
				dob.setVisibility(View.GONE);
			}
		});

		//default userActivity is login hence show signUp form if signUp
		if (UserActivity.equals(Signup)) {
			login_layout.setVisibility(View.INVISIBLE);
			signup_layout.setVisibility(View.VISIBLE);
			rewardId = prefs.getString("reference", "");
		}
		//back button
		back.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View view) {
				onBackPressed();
			}
		});

		name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_NEXT) {
					dob.performClick();
				}
				return false;
			}
		});

		dob.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				DatePickerDialog datePickerDialog =
					new DatePickerDialog(PasswordOtpActivity.this, R.style.AppTheme,
						new DatePickerDialog.OnDateSetListener() {
							@Override public void onDateSet(DatePicker view, int year, int month,
								int dayOfMonth) {
								String end = year
									+ "-"
									+ Util.twoDigitFormat(month + 1)
									+ "-"
									+ Util.twoDigitFormat(dayOfMonth);
								dob.setText(end);
								password.requestFocus();
								password.performClick();
							}
						}, 1995, 1, 1);
				datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
				datePickerDialog.setTitle("select date of birth");
				datePickerDialog.show();
			}
		});

		password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
				if (i == EditorInfo.IME_ACTION_NEXT) {
					proceed.performClick();
				}
				return false;
			}
		});

		password_login.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_NEXT) {
					proceed_login.performClick();
				}
				return false;
			}
		});

		//show hide login pass
		show_hide_password_login.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View view) {
				if (!isLoginPasswordShown) {
					isLoginPasswordShown = true;
					password_login.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
					password_login.setSelection(password_login.getText().length());
					show_hide_password_login.setImageResource(R.drawable.visible_new);
				} else if (isLoginPasswordShown) {
					isLoginPasswordShown = false;
					password_login.setInputType(
						InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
					password_login.setSelection(password_login.getText().length());
					show_hide_password_login.setImageResource(R.drawable.invisible_new);
				}
			}
		});
		// setting proceed login button functionality
		proceed_login.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View view) {

				if (password_login.getText().toString().length() < 3) {
					Toast.makeText(getApplicationContext(), "please enter your password",
						Toast.LENGTH_LONG).show();
				} else {
					verifyLogin(password_login.getText().toString());
				}
			}
		});

		//otp's text watcher
		otp1.addTextChangedListener(new Watcher(otp2));
		otp2.addTextChangedListener(new Watcher(otp3));
		otp3.addTextChangedListener(new Watcher(otp4));
		otp4.addTextChangedListener(new Watcher(otp5));
		otp5.addTextChangedListener(new Watcher(otp6));

		//setting resend otp button
		resend_otp.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View view) {
				resendOtp();
			}
		});
		otp_by_voice.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View view) {
				sendVoiceOtp();
			}
		});
		//show or hide pass in signup form
		show_hide_password.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View view) {
				if (!isSignupPasswordShown) {
					isSignupPasswordShown = true;
					password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
					password.setSelection(password.getText().length());
					show_hide_password.setImageResource(R.drawable.visible_new);
				} else if (isSignupPasswordShown) {
					isSignupPasswordShown = false;
					password.setInputType(
						InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
					password.setSelection(password.getText().length());
					show_hide_password.setImageResource(R.drawable.invisible_new);
				}
			}
		});
		//proceed button functionality
		proceed.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View view) {
				if (isResetPass) {
					if (otp1.getText().toString().length() < 1
						|| otp2.getText().toString().length() < 1
						|| otp3.getText().toString().length() < 1
						|| otp4.getText().toString().length() < 1
						|| otp5.getText().toString().length() < 1
						|| otp6.getText().toString().length() < 1) {
						Toast.makeText(getApplicationContext(), "please enter an OTP",
							Toast.LENGTH_LONG).show();
					} else if (!IsPasswordValid(password.getText().toString())) {
						Toast.makeText(getApplicationContext(), "please enter a valid password",
							Toast.LENGTH_LONG).show();
					} else {
						String otp =
							otp1.getText().toString()
								+ otp2.getText().toString()
								+ otp3.getText()
								.toString()
								+ otp4.getText().toString()
								+ otp5.getText().toString()
								+ otp6.getText().toString();
						forgotPasswordVerify(password.getText().toString(), otp);
					}
				} else {
					if (otp1.getText().toString().length() < 1
						|| otp2.getText().toString().length() < 1
						|| otp3.getText().toString().length() < 1
						|| otp4.getText().toString().length() < 1
						|| otp5.getText().toString().length() < 1
						|| otp6.getText().toString().length() < 1) {
						Toast.makeText(getApplicationContext(), "please enter an OTP",
							Toast.LENGTH_LONG).show();
					} else if (!IsPasswordValid(password.getText().toString())) {
						Toast.makeText(getApplicationContext(), "please enter a valid password",
							Toast.LENGTH_LONG).show();
					} else if (name.getText().toString().length() < 3) {
						Toast.makeText(getApplicationContext(), "please enter your name",
							Toast.LENGTH_LONG).show();
					} else if (Util.isEmpty(dob.getText().toString())) {
						Toast.makeText(getApplicationContext(), "please enter Date of birth",
							Toast.LENGTH_LONG).show();
					} else {
						String otp =
							otp1.getText().toString()
								+ otp2.getText().toString()
								+ otp3.getText()
								.toString()
								+ otp4.getText().toString()
								+ otp5.getText().toString()
								+ otp6.getText().toString();
						newRegister(password.getText().toString(), name.getText().toString(),
							dob.getText().toString(), otp);
					}
				}
			}
		});
	}

	@Override protected void onResume() {
		super.onResume();
		registerReceiver(smsReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
	}

	public BroadcastReceiver smsReceiver = new BroadcastReceiver() {

		public void onReceive(Context context, Intent intent) {
			// Retrieves a map of extended data from the intent.
			final Bundle bundle = intent.getExtras();
			try {

				if (bundle != null) {

					final Object[] pdusObj = (Object[]) bundle.get("pdus");

					for (int i = 0; i < pdusObj.length; i++) {

						SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
						String phoneNumber = currentMessage.getDisplayOriginatingAddress();

						String senderNum = phoneNumber;
						String message = currentMessage.getDisplayMessageBody();

						Log.i("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);

						if (senderNum.equalsIgnoreCase("HP-PLVSMS")) {
							String otp = message.trim().split(" ")[0];
							otp1.setText(otp.substring(0, 1));
							otp2.setText(otp.substring(1, 2));
							otp3.setText(otp.substring(2, 3));
							otp4.setText(otp.substring(3, 4));
							otp5.setText(otp.substring(4, 5));
							otp6.setText(otp.substring(5, 6));
						}
					} // end for loop
				} // bundle is null
			} catch (Exception e) {
				Log.e("SmsReceiver", "Exception smsReceiver" + e);
			}
		}
	};

	@Override protected void onPause() {
		super.onPause();
		unregisterReceiver(smsReceiver);
	}

	private class Watcher implements TextWatcher {
		private EditText editText;

		public Watcher(EditText editText) {
			this.editText = editText;
		}

		@Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

		}

		@Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
			if (charSequence.length() == 1) {
				editText.requestFocus();
			}
		}

		@Override public void afterTextChanged(Editable editable) {

		}
	}

	//on backpressed
	@Override public void onBackPressed() {
		if (isResetPass) {
			login_layout.setVisibility(View.VISIBLE);
			signup_layout.setVisibility(View.INVISIBLE);
			isResetPass = false;
			name.setVisibility(View.VISIBLE);
			back.setVisibility(View.INVISIBLE);
		} else {
			startActivity(new Intent(PasswordOtpActivity.this, LoginActivity.class));
			finish();
		}
	}

	private void verifyLogin(String password) {
		showProgress("Verifying password...");
		RestAdapterAPI api = NetworkUtil.getRestAdapter(this);
		Call<JsonObject> result = api.verifyPassword(password, "android_app");
		result.enqueue(new Callback<JsonObject>() {
			@Override public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
				try {
					JsonObject jsonObject = response.body();
					Boolean success = jsonObject.get("success").getAsBoolean();
					String status = jsonObject.get("status").getAsString();
					if (success) {
						String phone = null;
						try {
							JsonObject user = jsonObject.get("user").getAsJsonObject();
							phone = user.get("mobile").getAsString();
							String lock_code = user.get("notes_password").getAsString();
							Util.setLockPassword(PasswordOtpActivity.this, lock_code);
						} catch (Exception e) {
							e.printStackTrace();
						}
						String username = jsonObject.get("username").getAsString();
						String fullName = jsonObject.get("fullName").getAsString();
						String userid = jsonObject.get("userid").getAsString();
						SharedPreferences.Editor editor = prefs.edit();
						editor.putString("userid", userid);
						editor.putString("type", "signin");
						editor.putString("username", username);
						editor.putString("fullname", fullName);
						editor.putString("phone", phone);
						editor.apply();

						Intent intent = new Intent(PasswordOtpActivity.this, Main2Activity.class);
						startActivity(intent);
						finish();
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
					Toast.makeText(PasswordOtpActivity.this, "Something went wrong...",
						Toast.LENGTH_SHORT).show();
				}
				dismissProgress();
			}

			@Override public void onFailure(Call<JsonObject> call, Throwable t) {
				Toast.makeText(PasswordOtpActivity.this, "Network error!", Toast.LENGTH_SHORT)
					.show();
				dismissProgress();
			}
		});
	}

	private void resendOtp() {
		showProgress("Requesting otp...");
		RestAdapterAPI api = NetworkUtil.getRestAdapter(this);
		Call<JsonObject> result = api.resendOtp();
		result.enqueue(new Callback<JsonObject>() {
			@Override public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
				try {
					JsonObject jsonObject = response.body();
					Boolean success = jsonObject.get("success").getAsBoolean();
					String status = jsonObject.get("status").getAsString();
					Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(PasswordOtpActivity.this, "Something went wrong.",
						Toast.LENGTH_SHORT).show();
				}
				dismissProgress();
			}

			@Override public void onFailure(Call<JsonObject> call, Throwable t) {
				dismissProgress();
				Toast.makeText(PasswordOtpActivity.this, "Network error!", Toast.LENGTH_SHORT)
					.show();
			}
		});
	}

	private void newRegister(String password, String fullName, String dob, String otp) {
		String id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
		showProgress("Signing up...");
		RestAdapterAPI api = NetworkUtil.getRestAdapter(this);
		Call<JsonObject> result = api.signUp(password, fullName, otp, dob, actualReward, id);
		result.enqueue(new Callback<JsonObject>() {
			@Override public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
				try {
					JsonObject jsonObject = response.body();
					Boolean success = jsonObject.get("success").getAsBoolean();
					String status = jsonObject.get("status").getAsString();
					if (success) {
						String phone = null;
						try {
							phone = jsonObject.get("user")
								.getAsJsonObject()
								.get("mobile")
								.getAsString();
						} catch (Exception e) {

						}
						String username = jsonObject.get("username").getAsString();
						String fullName = jsonObject.get("fullName").getAsString();
						String userid = jsonObject.get("userid").getAsString();

						SharedPreferences.Editor editor = prefs.edit();
						editor.putString("userid", userid);
						editor.putString("username", username);
						editor.putString("fullname", fullName);
						editor.putString("phone", phone);
						editor.apply();

						Intent intent = new Intent(PasswordOtpActivity.this, Main2Activity.class);
						intent.putExtra("userid", userid);
						intent.putExtra("cookie", prefs.getString("cookies", null));
						startActivity(intent);
						finish();
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
				}
				dismissProgress();
			}

			@Override public void onFailure(Call<JsonObject> call, Throwable t) {
				dismissProgress();
				Toast.makeText(PasswordOtpActivity.this, "Network error!", Toast.LENGTH_SHORT)
					.show();
			}
		});
	}

	private void forgotPasswordVerify(String password, String otp) {
		showProgress("Verifying...");
		RestAdapterAPI api = NetworkUtil.getRestAdapter(this);
		Call<JsonObject> result = api.forgotPassword(password, otp);
		result.enqueue(new Callback<JsonObject>() {
			@Override public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
				try {
					JsonObject jsonObject = response.body();
					Boolean success = jsonObject.get("success").getAsBoolean();
					String status = jsonObject.get("status").getAsString();
					if (success) {
						String username = jsonObject.get("username").getAsString();
						String fullName = jsonObject.get("fullName").getAsString();
						String userid = jsonObject.get("userid").getAsString();
						SharedPreferences.Editor editor = prefs.edit();
						editor.putString("userid", userid);
						editor.putString("type", "signin");
						editor.putString("username", username);
						editor.putString("fullname", fullName);
						editor.apply();

						Intent intent = new Intent(PasswordOtpActivity.this, Main2Activity.class);
						startActivity(intent);
						finish();
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
				}
				dismissProgress();
			}

			@Override public void onFailure(Call<JsonObject> call, Throwable t) {
				dismissProgress();
			}
		});
	}

	//checking if password is valid
	public Boolean IsPasswordValid(String myPassword) {
		if (myPassword.length() < 7) {
			return false;
		} else if (myPassword.matches("[A-Za-z0-9]+")) {
			return true;
		} else {
			return false;
		}
	}

	private void sendVoiceOtp() {
		showProgress("Requesting voice OTP");
		RestAdapterAPI api = NetworkUtil.getRestAdapter(this);
		Call<Void> result = api.sendVoiceOTP();
		result.enqueue(new Callback<Void>() {
			@Override public void onResponse(Call call, Response response) {
				dismissProgress();
				Toast.makeText(getApplicationContext(), "You'll receive a call soon",
					Toast.LENGTH_LONG).show();
			}

			@Override public void onFailure(Call call, Throwable t) {
				dismissProgress();
				Toast.makeText(PasswordOtpActivity.this, "Network error!", Toast.LENGTH_SHORT)
					.show();
			}
		});
	}
}
