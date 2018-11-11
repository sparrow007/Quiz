package com.zersey.roz;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.zersey.roz.Data.TransactionDbHelper;
import com.zersey.roz.model.LoanModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class BillsFormFragment extends DialogFragment implements View.OnClickListener {
	private int year_x, month_x, day_x;
	private List<ContactModel> contactList;
	private RecyclerView contactRecyclerView, loanContactRecyclerView;
	private DatePickerDialog datePicker;
	private RecyclerView categoryRecyclerView;
	private List<String> categoryList;
	private ImageView imgFile;
	private TextView dateEditText;
	public static Button positive_Button;
	private TextView amountEditText;
	private EditText titleEditText;
	private StringBuilder users = new StringBuilder();
	private RecyclerView splitRecyclerView;
	private DialogSplitRecyclerViewAdapter dialogSplitAdapter;
	private GroupModel groupModel;
	private String code;
	private TransactionDbHelper mDbHelper;

	private List<Split_Contact_model> splitList;
	public static TextView splitNotes;
	public static BillRecyclerAdapter adapter;
	private String amount;
	private CategoryAdapter categoryAdapter;
	private Uri Image_uri = null;
	private Context context;
	private ArrayList<Split_Contact_model> secondSplitList;
	private List<GroupModel> groupList;
	public InterfaceCommunicator communicator;
	private long groupId = -1;
	private String myUserId;

	private Button sevenButton , eightButton, nineButton, multiplyButton;
	private Button fourButton, fiveButton, sixButton, subButton;
	private Button oneButton, twoButton, threeButton, addButton;
	private Button dotButton, zeroButton, divideButton , equalButton;

	private int first = -1, second = -1;
	private boolean operatorPressd = false;
	private int operatorValue = -1;
	private boolean sizeChanged = false;
	private boolean firstFilled = false;
	private CountDownTimer countDownTimer;
	private boolean counDownRunning = false;

	private Stack<Integer> operand = new Stack<>();
	private ArrayList<Integer> operator = new ArrayList<>();
	private StringBuilder stringBuilder = new StringBuilder();

	private boolean isCalculatorShow = false;

	private LinearLayout expanseLayout;
	private RelativeLayout loanLayout;

	private String date [] = {"month", "day", "year"};
	private String compoundSelection [] = {"Simple Interest", "Compound Interest"};
	private Spinner compundSpinnerSelection;

	private CheckBox checkBox;
	private int user_date , user_month, user_year;
	private DatePickerDialog datePickerDialog;
	private boolean isCheckedBox;
	private int durationType;
	private Spinner timeInterestSpinner;
	private int n;
	private boolean isCalculatorFocus = false;
	private RelativeLayout relativeLayout;

	private EditText titleLoanText , amountLoanText, loanRateText, loanDurationText;

	public interface UpdateFrag {
		 void updatefrag();
	}

	private Main2Activity mCallbackActivity;

	@NonNull
	@Override
	public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
		return new Dialog(getActivity(), getTheme()) {
			@Override
			public void onBackPressed() {
				if (isCalculatorFocus) {
					relativeLayout.setVisibility(View.INVISIBLE);
					isCalculatorFocus = false;
				}else  {
					super.onBackPressed();
				}

			}
		};
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDbHelper = TransactionDbHelper.getInstance(getContext());
		secondSplitList = new ArrayList<>();
		if (getArguments() != null) {
			groupModel = (GroupModel) getArguments().getSerializable("model");
			if (groupModel != null)
				groupId = groupModel.getGroupId();
		}
		groupList = mDbHelper.getGroups(0);
	}

	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
	                         Bundle savedInstanceState) {
		context = inflater.getContext();
		View fragmentView = inflater.inflate(R.layout.fragment_bills_form, container, false);

		contactList = new ArrayList<>();
		categoryList = new ArrayList<>();
		categoryList.add("Expense");
		categoryList.add("Loan");
		categoryList.add("Group");

		((Main2Activity)getActivity()).updateApi(new UpdateFrag() {
			@Override
			public void updatefrag() {
                   Log.e("MY TAG", "WE CALL THE FRAG");
			}
		});

		fragmentView.findViewById(R.id.Notes_Image_Button)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Add_notes(view);
					}
				});

		categoryRecyclerView = fragmentView.findViewById(R.id.Category_Recycler_View);
		compundSpinnerSelection = fragmentView.findViewById(R.id.compound_Interest_spinner);
		fragmentView = initView(fragmentView);
		if (groupModel != null) {
			categoryRecyclerView.setVisibility(View.GONE);
			fragmentView.findViewById(R.id.Category_text_view).setVisibility(View.GONE);
		}
		TextView splitButton = fragmentView.findViewById(R.id.Split_Dialog);
		splitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				splitDialog();
			}
		});


		sevenButton = fragmentView.findViewById(R.id.btn_seven);
		eightButton = fragmentView.findViewById(R.id.btn_eight);
		nineButton = fragmentView.findViewById(R.id.btn_nine);
		multiplyButton = fragmentView.findViewById(R.id.btn_multiple);
		fourButton = fragmentView.findViewById(R.id.btn_four);
		fiveButton = fragmentView.findViewById(R.id.btn_five);
		sixButton = fragmentView.findViewById(R.id.btn_six);
		subButton = fragmentView.findViewById(R.id.btn_minus);
		oneButton = fragmentView.findViewById(R.id.btn_one);
		twoButton = fragmentView.findViewById(R.id.btn_two);
		threeButton = fragmentView.findViewById(R.id.btn_three);
		addButton = fragmentView.findViewById(R.id.btn_plus);
		dotButton = fragmentView.findViewById(R.id.btn_dot);
		zeroButton = fragmentView.findViewById(R.id.btn_zero);
		divideButton = fragmentView.findViewById(R.id.btn_divide);
		equalButton = fragmentView.findViewById(R.id.btn_equal);
        expanseLayout = fragmentView.findViewById(R.id.bill_fragment_change);
        loanLayout = fragmentView.findViewById(R.id.loan_layout);
		sevenButton.setOnClickListener(this);
		nineButton.setOnClickListener(this);
		eightButton.setOnClickListener(this);
		multiplyButton.setOnClickListener(this);
		fourButton.setOnClickListener(this);
		fiveButton.setOnClickListener(this);
		sixButton.setOnClickListener(this);
		subButton.setOnClickListener(this);
		oneButton.setOnClickListener(this);
		twoButton.setOnClickListener(this);
		threeButton.setOnClickListener(this);
		addButton.setOnClickListener(this);
		dotButton.setOnClickListener(this);
		zeroButton.setOnClickListener(this);
		divideButton.setOnClickListener(this);
		equalButton.setOnClickListener(this);
		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH)+1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		titleLoanText = fragmentView.findViewById(R.id.title_loan);
		amountLoanText = fragmentView.findViewById(R.id.loan_amount);
		loanRateText = fragmentView.findViewById(R.id.loan_rate);
		loanDurationText = fragmentView.findViewById(R.id.loan_duration);


		timeInterestSpinner = fragmentView.findViewById(R.id.no_time_interest);
		Spinner spinner = fragmentView.findViewById(R.id.spinner);
		ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,date);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String spinnerData[] = new String[3];

				switch (position) {
					case 0:
						n = 12;
						spinnerData[0] = "Month";
						spinnerData[1] = "Day";
						spinnerData[2] = "Year";
						break;
					case 1:
						n = 365;
						spinnerData[0] = "Day";
						spinnerData[1] = "Month";
						spinnerData[2] = "Year";
						break;
					case 2:
						n = 1;
						spinnerData[0] = "Year";
						spinnerData[1] = "Day";
						spinnerData[2] = "Month";
						break;
				}

				ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,spinnerData);
				aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				//Setting the ArrayAdapter data on the Spinner
				timeInterestSpinner.setAdapter(aa);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		spinner.setAdapter(adapter);
		final TextView timeText = fragmentView.findViewById(R.id.date);
		timeText.setText(day + "/" + month + "/" + year);
		timeText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Calendar c = Calendar.getInstance();
				int year = c.get(Calendar.YEAR);
				int month = c.get(Calendar.MONTH)+1;
				int day = c.get(Calendar.DAY_OF_MONTH);


				/*datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
						timeText.setText(dayOfMonth + "/" + month + "/" + year);

						user_date = dayOfMonth;
						user_month = month;
						user_year = year;
					}
				}, day, month, year);
				datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
				datePickerDialog.show();*/
			}
		});

		ArrayAdapter aa = new ArrayAdapter(getActivity(),android.R.layout.simple_spinner_item,compoundSelection);
		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		//Setting the ArrayAdapter data on the Spinner
		compundSpinnerSelection.setAdapter(aa);
		compundSpinnerSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				switch (position) {
					case 0:
						timeInterestSpinner.setVisibility(View.INVISIBLE);
						break;
					case 1:
						timeInterestSpinner.setVisibility(View.VISIBLE);
						break;

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		countDownTimer = new CountDownTimer(800, 125) {
			@Override
			public void onTick(long millisUntilFinished) {
				counDownRunning = true;
			}

			@Override
			public void onFinish() {

				if (counDownRunning)
					performCal();

			}
		};

		Calendar cal = Calendar.getInstance();
		year_x = cal.get(Calendar.YEAR);
		day_x = cal.get(Calendar.DAY_OF_MONTH);
		month_x = cal.get(Calendar.MONTH);

		imgFile = fragmentView.findViewById(R.id.Img_file);
		imgFile.setVisibility(View.GONE);

		dateEditText = fragmentView.findViewById(R.id.Date_Edit);
		dateEditText.setText(
				String.format(Locale.getDefault(), "%d-%d-%d", year_x, month_x + 1, day_x));

		titleEditText = fragmentView.findViewById(R.id.Title_Edit);
		relativeLayout = fragmentView.findViewById(R.id.layout_calculator);
		amountEditText = fragmentView.findViewById(R.id.Amount_Edit);
		amountEditText.setShowSoftInputOnFocus(false);

		amountEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, final boolean hasFocus) {
				if (!hasFocus) {
					relativeLayout.setVisibility(View.INVISIBLE);
					isCalculatorFocus = false;
					return;
				}
				InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(amountEditText.getWindowToken(), 0);
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						if (hasFocus) {
							relativeLayout.setVisibility(View.VISIBLE);
							isCalculatorFocus = true;

						}
					}
				}, 400);
			}
		});

		ImageView deletButton = fragmentView.findViewById(R.id.clear_text);
		deletButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				for (int i = stringBuilder.length()-1; i >= 0; i--) {
					char ch = stringBuilder.charAt(i);
					if (Character.isDigit(ch)) {
						stringBuilder.deleteCharAt(i);
						break;
					}else if(Character.isWhitespace(ch)) {
						operatorPressd = false;
						countDownTimer.cancel();
						stringBuilder.deleteCharAt(i);
					}
				}
				//Run the countdown when operation has been perform.
				if (operatorPressd && counDownRunning) {
					countDownTimer.cancel();
					counDownRunning=false;
					countDownTimer.start();
				}else if (operatorPressd && !counDownRunning){
					countDownTimer.start();
				}
				amountEditText.setText(stringBuilder.toString());

			}
		});
		deletButton.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				operatorPressd = false;
				countDownTimer.cancel();
				if (operatorPressd && counDownRunning) {
					countDownTimer.cancel();
					counDownRunning=false;
					countDownTimer.start();
				}else if (operatorPressd && !counDownRunning){
					countDownTimer.start();
				}
				stringBuilder.delete(0, stringBuilder.length());
				amountEditText.setText(stringBuilder.toString());
				return false;
			}
		});


		splitRecyclerView = new RecyclerView(context);
		SharedPreferences prefs = context.getSharedPreferences("login", MODE_PRIVATE);
		myUserId = prefs.getString("userid", null);
		users.append(myUserId).append(",");

		Button submitLoanButton = fragmentView.findViewById(R.id.Submit_loan);

		TextView submitButton = fragmentView.findViewById(R.id.Submit_Transaction_form);
		dateEditText.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				datePicker.show();
			}
		});

		DatePickerDialog.OnDateSetListener dateSetListener =
				new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
						dateEditText.setText(
								String.format(Locale.getDefault(), "%d-%d-%d", year, month + 1,
										dayOfMonth));
					}
				};
		datePicker =
				new DatePickerDialog(context, R.style.Theme_AppCompat_DayNight_Dialog,
						dateSetListener,
						cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar
						.DAY_OF_MONTH));
		datePicker.setCancelable(true);
		datePicker.setTitle("Select date");
		ImageButton cameraButton = fragmentView.findViewById(R.id.Fab_Camera_Button);
		cameraButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				selectImage();
			}
		});

		submitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				submitBill();
				dismiss();
			}
		});

		submitLoanButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				LoanModel loanModel = new LoanModel();
				int amount = Integer.parseInt(amountLoanText.getText().toString());
				float rate = Float.parseFloat(loanRateText.getText().toString());
				Date date = getDate(user_year ,user_month, user_date );
				int user_duration = Integer.parseInt(loanDurationText.getText().toString());
				float interest;

				float Principal = calPric(amount);

				float Rate = calInt(rate);

				float Months = calMonth(user_duration);

				float Dvdnt = calDvdnt( Rate, Months);

				float FD = calFinalDvdnt (Principal, Rate, Dvdnt);

				float D = calDivider(Dvdnt);
				if (isCheckedBox) {
					interest = (float) (amount*(Math.pow (1 + (getRate((float) (rate))/n), (n * user_duration))));
				}else {
					interest = amount * (1 + getRate(rate) * simpleInterest(user_duration));
				}

				float emi = calEmi(FD, D);
				loanModel.setEmi(emi);
				loanModel.setAmount(amount);
				loanModel.setRate(rate);
				loanModel.setDate(date);
				loanModel.setDuration(user_duration);
				loanModel.setDurationType(durationType);
				loanModel.setInterestAccrued(interest);



				submitLoan(loanModel);
				dismiss();
			}
		});

		final View finalFragmentView = fragmentView;

		if(groupModel!=null){
			View shareWithView = fragmentView.findViewById(R.id.share_with_box);
			shareWithView.setVisibility(View.VISIBLE);
			((TextView) shareWithView.findViewById(R.id.group_name)).setText(groupModel.getGroupName());
		}

		((Main2Activity)getActivity()).setActivityCommunicateListner(new Main2Activity.OnCommunicateListener() {
			@Override
			public void onCommunicate() {
				Log.e("MY TAG", "YES WE COMMUNCIATE ");
			}
		});
		fragmentView.findViewById(R.id.share_with_group)
				.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(final View view) {
						AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
						LayoutInflater inflater = getActivity().getLayoutInflater();
						final View selectGroupDialog = inflater.inflate(R.layout.select_group_dialog, null);
						builder.setView(selectGroupDialog);
						final AlertDialog dialog = builder.create();

						if (groupModel != null) {
							selectGroupDialog.findViewById(R.id.add_member).setVisibility(View.GONE);
						}

						RecyclerView recyclerView = selectGroupDialog.findViewById(R.id.group_list);
						recyclerView.setHasFixedSize(true);
						recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
						SelectGroupAdapter selectGroupAdapter = new SelectGroupAdapter(groupList);
						selectGroupAdapter.setGroupSelectListener(
								new SelectGroupAdapter.GroupSelectListener() {
									@Override
									public void groupSelected(GroupModel groupModel) {
										BillsFormFragment.this.groupModel = groupModel;
										((TextView) view.findViewById(R.id.group_name)).setText(
												groupModel.getGroupName());
										categoryRecyclerView.setVisibility(View.GONE);
										finalFragmentView.findViewById(R.id.Category_text_view)
												.setVisibility(View.GONE);
										view.findViewById(R.id.share_with_box).setVisibility(View
												.VISIBLE);
										dialog.dismiss();
									}
								});
						recyclerView.setAdapter(selectGroupAdapter);

						view.findViewById(R.id.cross_button)
								.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										BillsFormFragment.this.groupModel = null;
										((TextView) view.findViewById(R.id.group_name)).setText
												("None");
										categoryRecyclerView.setVisibility(View.VISIBLE);
										finalFragmentView.findViewById(R.id.Category_text_view)
												.setVisibility(View.VISIBLE);
										view.findViewById(R.id.share_with_box).setVisibility(View
												.GONE);
									}
								});

						selectGroupDialog.findViewById(R.id.add_member)
								.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View view) {
										Intent intent = new Intent(getContext(),
												AddMembersActivity.class);
										startActivityForResult(intent, 4);
										dialog.dismiss();
									}
								});
						dialog.show();
					}
				});

		fragmentView.findViewById(R.id.loan_share_with_group).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View view) {
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				LayoutInflater inflater = getActivity().getLayoutInflater();
				final View selectGroupDialog = inflater.inflate(R.layout.select_group_dialog, null);
				builder.setView(selectGroupDialog);
				final AlertDialog dialog = builder.create();

				if (groupModel != null) {
					selectGroupDialog.findViewById(R.id.add_member).setVisibility(View.GONE);
				}

				RecyclerView recyclerView = selectGroupDialog.findViewById(R.id.group_list);
				recyclerView.setHasFixedSize(true);
				recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
				SelectGroupAdapter selectGroupAdapter = new SelectGroupAdapter(groupList);
				selectGroupAdapter.setGroupSelectListener(
						new SelectGroupAdapter.GroupSelectListener() {
							@Override
							public void groupSelected(GroupModel groupModel) {
								BillsFormFragment.this.groupModel = groupModel;
								((TextView) view.findViewById(R.id.group_name)).setText(
										groupModel.getGroupName());
								categoryRecyclerView.setVisibility(View.GONE);
								finalFragmentView.findViewById(R.id.Category_text_view)
										.setVisibility(View.GONE);
								view.findViewById(R.id.loan_share_with_box).setVisibility(View
										.VISIBLE);
								dialog.dismiss();
							}
						});
				recyclerView.setAdapter(selectGroupAdapter);

				view.findViewById(R.id.cross_button)
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								BillsFormFragment.this.groupModel = null;
								((TextView) view.findViewById(R.id.group_name)).setText
										("None");
								categoryRecyclerView.setVisibility(View.VISIBLE);
								finalFragmentView.findViewById(R.id.Category_text_view)
										.setVisibility(View.VISIBLE);
								view.findViewById(R.id.share_with_box).setVisibility(View
										.GONE);
							}
						});

				selectGroupDialog.findViewById(R.id.add_member)
						.setOnClickListener(new View.OnClickListener() {
							@Override
							public void onClick(View view) {
								Intent intent = new Intent(getContext(),
										AddMembersActivity.class);
								startActivityForResult(intent, 4);
								dialog.dismiss();
							}
						});
				dialog.show();
			}
		});

		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (getDialog().getWindow() != null) {
			getDialog().getWindow()
					.setBackgroundDrawable(
							new ColorDrawable(context.getResources().getColor(R.color
									.TransparentWhite)));
			getDialog().show();
			getDialog().getWindow()
					.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
							WindowManager.LayoutParams.MATCH_PARENT);
		}
		return fragmentView;
	}

	private void submitLoan(LoanModel loanModel) {
      String titleText = titleLoanText.getText().toString();
      String amountText = amountLoanText.getText().toString();
      String rateInterestText = loanRateText.getText().toString();
      String durationText = loanDurationText.getText().toString();
      String category = "Loan";

      if (titleText.isEmpty()) {
		  Toast.makeText(getActivity(), "Title field can't be empty", Toast.LENGTH_SHORT).show();
		  return;
	  }else if (amountText.isEmpty()) {
		  Toast.makeText(getActivity(), "Amount field can't be empty", Toast.LENGTH_SHORT).show();
		  return;
	  }else if  (rateInterestText.isEmpty()) {
		  Toast.makeText(getActivity(), "Rate of Interest field can't be empty", Toast.LENGTH_SHORT).show();
		  return;
	  } else if  (durationText.isEmpty()) {
		  Toast.makeText(getActivity(), "Duration field can't be empty", Toast.LENGTH_SHORT).show();
		  return;
	  }

		BillModel billModel = new BillModel();

		String dateText = dateEditText.getText().toString();
		SharedPreferences prefs = context.getSharedPreferences("login", MODE_PRIVATE);

		if (groupModel != null) {
			groupSplit();
		} else {
			singleSplit();
		}

		StringBuilder totalAmounts = new StringBuilder();
		StringBuilder payerIds = new StringBuilder();
		StringBuilder amountsPaid = new StringBuilder();
		StringBuilder datesPaid = new StringBuilder();
		StringBuilder amountsDue = new StringBuilder();
		int i = 0;

	for (Split_Contact_model s : splitList) {
			totalAmounts.append(s.getSplit_Amount()).append(",");
			if (s.getContact_Name().getName().equalsIgnoreCase("you")) {
				payerIds.append(prefs.getString("userid", null)).append(",");
			} else {
				payerIds.append(s.getContact_Name().getUserId()).append(",");
			}
			//amountsPaid.append(secondSplitList.get(i).getSplit_Amount()).append(",");

			double amountDuePerPerson = s.getSplit_Amount() - secondSplitList.get(i)
					.getSplit_Amount();

			datesPaid.append(dateText).append(",");
			i++;
		}
		amountsDue.append(Double.toString(loanModel.getInterestAccrued())).append(",");
		amountsPaid.append(loanModel.getInterestAccrued()).append(",");

		billModel.setType(category);
		billModel.setTitle(titleText);
		billModel.setTotalAmount(String.valueOf(loanModel.getInterestAccrued()));
		billModel.setPaidAtDate(datesPaid.toString());
		billModel.setAmountPaid(amountsPaid.toString());
		billModel.setAmountDue(amountsDue.toString());
		billModel.setPayerId(payerIds.toString());
		billModel.setInvoiceId("");
		billModel.setUuid(Util.generateUuid());
		billModel.setCreatedAt(Util.getDateTime());

		if (groupModel != null) {
			billModel.setGroupId(groupModel.getGroupId());
			billModel.setType("gt");
			groupModel.setUpdatedAt(Util.getDateTime());
			mDbHelper.setGroupUpdatedDate(groupModel.getUpdatedAt(), groupModel.getGroupId());
			long rowId = mDbHelper.createEntry(billModel);
			new ServerUtil(context).createEntry(billModel);
			billModel.setId(rowId);
		}

		if (groupModel == null) {
			GroupModel groupModel2 = new GroupModel();
			groupModel2.setGroupName(titleText);
			groupModel2.setTypeId(1);
			groupModel2.setCreatedAt(Util.getDateTime());
			groupModel2.setUpdatedAt(Util.getDateTime());
			groupModel2.setUsers(users.toString());
			long newRowId = mDbHelper.createGroup(groupModel2);
			groupModel2.setId(newRowId);
			new ServerUtil(context).createSingleGroup(groupModel2, billModel, null);
		}

		Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();

		if (getArguments() != null && groupModel != null) {
			if (getArguments().getBoolean("fromGroup") && groupId == groupModel.getGroupId()) {
				communicator.sendRequestCode(billModel);
			}
		}

	}


	public void selectImage() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setCancelable(true)
				.setTitle(Html.fromHtml("<font color='#3F51B5'>Choose an Image from</font>"))
				.setPositiveButton(Html.fromHtml("<font color='#3F51B5'>camera</font>"),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
								startActivityForResult(intent, 2);
							}
						})
				.setNegativeButton(Html.fromHtml("<font color='#3F51B5'>Gallery</font>"),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								Intent i = new Intent(Intent.ACTION_PICK,
										android.provider.MediaStore.Images.Media
												.EXTERNAL_CONTENT_URI);

								startActivityForResult(i, 1);
							}
						});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}


	public void splitDialog() {
		amount = amountEditText.getText().toString();
		if (TextUtils.isEmpty(amount)) {
			amount = "0";
			Toast.makeText(context, "Enter amount first", Toast.LENGTH_SHORT).show();
			return;
		}

		if (groupModel != null) {
			groupSplit();
		} else {
			singleSplit();
		}


		LayoutInflater inflater = LayoutInflater.from(getContext());
		View dialogView = inflater.inflate(R.layout.split_dialog_layout, null);
		Spinner splitSpinner = dialogView.findViewById(R.id.Split_Spinner);
		splitRecyclerView = dialogView.findViewById(R.id.Dialog_RecyclerView);
		splitNotes = dialogView.findViewById(R.id.Dialog_Split_Notes);
		splitRecyclerView.setLayoutManager(new LinearLayoutManager(context));
		dialogSplitAdapter =
				new DialogSplitRecyclerViewAdapter(splitList, "", Integer.parseInt(amount));
		splitRecyclerView.setAdapter(dialogSplitAdapter);

		splitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (position == 2) {
					dialogSplitAdapter = new DialogSplitRecyclerViewAdapter(splitList, "ratio",
							Integer.parseInt(amount));
					splitRecyclerView.setAdapter(dialogSplitAdapter);
				}
				if (position == 1) {
					dialogSplitAdapter =
							new DialogSplitRecyclerViewAdapter(splitList, "", Integer.parseInt
									(amount));
					splitRecyclerView.setAdapter(dialogSplitAdapter);
				}
				if (position == 0) {
					dialogSplitAdapter = new DialogSplitRecyclerViewAdapter(splitList, "Equally",
							Integer.parseInt(amount));
					splitRecyclerView.setAdapter(dialogSplitAdapter);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		android.support.v7.app.AlertDialog.Builder alertDialogBuilder =
				new android.support.v7.app.AlertDialog.Builder(getActivity());
		alertDialogBuilder.setView(dialogView);

		alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				splitList = dialogSplitAdapter.getList();
			}
		});
		final Dialog dialog = alertDialogBuilder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			@Override
			public void onShow(DialogInterface dialogInterface) {
				positive_Button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
				positive_Button.setTextColor(getResources().getColor(R.color.colorPrimary));
			}
		});


		dialog.show();
		dialog.getWindow()
					.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
							| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);



//		if (getDialog().getWindow() != null) {
//
//			getDialog().getWindow()
//					.setBackgroundDrawable(
//							new ColorDrawable(context.getResources().getColor(R.color
//									.TransparentWhite)));
//			getDialog().show();
//			getDialog().getWindow()
//					.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
//							WindowManager.LayoutParams.MATCH_PARENT);
//			getDialog().getWindow()
//					.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//							| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//		}
		//getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

	}

	public void Add_notes(View view) {
		final EditText Notes_Edit;
		LayoutInflater inflater = LayoutInflater.from(getContext());
		View notes_view = inflater.inflate(R.layout.notes_dialogue_layout, null);
		Notes_Edit = notes_view.findViewById(R.id.Notes_EditText);
		android.support.v7.app.AlertDialog.Builder alertDialogBuilder =
				new android.support.v7.app.AlertDialog.Builder(getContext());
		alertDialogBuilder.setView(notes_view);
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.setPositiveButton(Html.fromHtml("<font color='#00796B'>Ok</font>"),
				null);
		//alertDialogBuilder.setNegativeButton("Cancel",null);
		alertDialogBuilder.setNeutralButton("Cancel", null);

		final android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override
			public void onShow(DialogInterface dialogInterface) {

				Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
				Button Negative = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
				Negative.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
				button.setTextColor(getContext().getResources().getColor(R.color.colorPrimary));
				Negative.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						alertDialog.dismiss();
					}
				});
				button.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View view) {
						// TODO Do something

						//Dismiss once everything is OK.
						if (TextUtils.isEmpty(Notes_Edit.getText().toString())) {
							Toast.makeText(getContext(), "Please write something",
									Toast.LENGTH_LONG).show();
						} else {
							alertDialog.dismiss();
						}
					}
				});
			}
		});
		alertDialog.show();
	}

	private void singleSplit() {
		int no_of_Person = contactList.size();
		int Specific_Amount;
		amount = amountEditText.getText().toString();
		//amount = amountLoanText.getText().toString();
		//Log.e("MY TAG", "I AM IN THE SINGLE SPLIT");
		if (Util.isEmpty(amount)) {
			Toast.makeText(context, "Please enter amount first", Toast.LENGTH_SHORT).show();
			return;
		}

		Specific_Amount = Integer.parseInt(amount) / (contactList.size() + 1);

		splitList = new ArrayList<>();
		secondSplitList = new ArrayList<>();
		ContactModel you = new ContactModel();
		you.setName("You");
		splitList.add(new Split_Contact_model(you, Specific_Amount));
		secondSplitList.add(new Split_Contact_model(you, Integer.parseInt(amount)));

		if (contactList.isEmpty()) {
			Toast.makeText(context, "Add members ", Toast.LENGTH_SHORT).show();
			return;
		}
		for (int i = 0; i < contactList.size(); i++) {

			if ((i == contactList.size() - 1) && (no_of_Person + 1) % 2 != 0) {
				Specific_Amount = Specific_Amount + 1;
			}
			splitList.add(new Split_Contact_model(contactList.get(i), Specific_Amount));
			secondSplitList.add(new Split_Contact_model(contactList.get(i), 0));
		}
	}

	private void groupSplit() {

		amount = amountEditText.getText().toString();
		if (Util.isEmpty(amount)) {
			Toast.makeText(context, "Please enter amount first", Toast.LENGTH_SHORT).show();
			return;
		}

		String[] userIds = groupModel.getUsers().split(",");
		String[] fullNames = groupModel.getFullname().split(",");
		String[] phones = groupModel.getMobile_no().split(",");

		int specificAmount;
		specificAmount = Integer.parseInt(amount) / userIds.length;

		splitList = new ArrayList<>();
		secondSplitList = new ArrayList<>();

		ContactModel you = new ContactModel();
		you.setName("You");
		you.setUserId(Long.parseLong(myUserId));
		splitList.add(new Split_Contact_model(you, specificAmount));
		secondSplitList.add(new Split_Contact_model(you, Double.parseDouble(amount)));

		for (int i = 0; i < userIds.length; i++) {

			if (userIds[i].equals(myUserId))
				continue;

			ContactModel contactModel = new ContactModel();
			contactModel.setUserId(Long.parseLong(userIds[i]));
			contactModel.setName(fullNames[i]);
			contactModel.setNumber(phones[i]);


			if (i == userIds.length - 1 && (i + 1) * specificAmount < Integer.parseInt(amount)) {
				specificAmount += Integer.parseInt(amount) - (i + 1) * specificAmount;
			}

			splitList.add(new Split_Contact_model(contactModel, specificAmount));
			secondSplitList.add(new Split_Contact_model(contactModel, 0));
		}
	}

	private View initView(View view) {
		contactRecyclerView = view.findViewById(R.id.Add_Member_RecyclerView);
		loanContactRecyclerView = view.findViewById(R.id.loan_Add_Member_RecyclerView);
		loanContactRecyclerView.setLayoutManager(
				new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
		contactRecyclerView.setLayoutManager(
				new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
		String updated_Type = "";
		if (TextUtils.isEmpty(updated_Type)) {
			categoryAdapter = new CategoryAdapter(context, categoryList, "");
		} else {
			categoryAdapter = new CategoryAdapter(context, categoryList, updated_Type);
		}
		categoryRecyclerView.setLayoutManager(
				new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
		categoryRecyclerView.setAdapter(categoryAdapter);
		categoryAdapter.setOnCategoryClickListener(new CategoryAdapter.OnCategoryClickListener() {
			@Override
			public void onClick(String category) {

				switch (category) {
					case "Loan" :
						expanseLayout.setVisibility(View.INVISIBLE);
						loanLayout.setVisibility(View.VISIBLE);
						break;
					case "Expense" :
						expanseLayout.setVisibility(View.VISIBLE);
						loanLayout.setVisibility(View.INVISIBLE);
						break;
					case "Group" :

						break;
				}


			}
		});
		return view;
	}

	public void addMembers() {
		Intent intent = new Intent(context, AddMembersActivity.class);
		startActivityForResult(intent, 4);
	}

	public void submitBill() {
		String amountText = amountEditText.getText().toString();
		String titleText = titleEditText.getText().toString();
		String categoryText = "Expense";

		if (groupModel == null && TextUtils.isEmpty(categoryText)) {
			Toast.makeText(context, "category can't be empty", Toast.LENGTH_SHORT).show();
			return;
		}

		if (day_x == 0) {
			dateEditText.setError("date can't be empty");
			return;
		}
		if (Util.isEmpty(amountText)) {
			amountEditText.setError("amount can't be empty");
			return;
		}
		if (Util.isEmpty(titleText)) {
			titleEditText.setError("title can't be empty");
			return;
		}

		BillModel billModel = new BillModel();

		String dateText = dateEditText.getText().toString();
		SharedPreferences prefs = context.getSharedPreferences("login", MODE_PRIVATE);

		if (groupModel != null) {
			groupSplit();
		} else {
			singleSplit();
		}

		StringBuilder totalAmounts = new StringBuilder();
		StringBuilder payerIds = new StringBuilder();
		StringBuilder amountsPaid = new StringBuilder();
		StringBuilder datesPaid = new StringBuilder();
		StringBuilder amountsDue = new StringBuilder();
		int i = 0;

		for (Split_Contact_model s : splitList) {
			totalAmounts.append(s.getSplit_Amount()).append(",");
			if (s.getContact_Name().getName().equalsIgnoreCase("you")) {
				payerIds.append(prefs.getString("userid", null)).append(",");
			} else {
				payerIds.append(s.getContact_Name().getUserId()).append(",");
			}
			amountsPaid.append(secondSplitList.get(i).getSplit_Amount()).append(",");

			double amountDuePerPerson = s.getSplit_Amount() - secondSplitList.get(i)
					.getSplit_Amount();
			amountsDue.append(Double.toString(amountDuePerPerson)).append(",");
			datesPaid.append(dateText).append(",");
			i++;
		}

		billModel.setType(categoryText);
		billModel.setTitle(titleText);
		billModel.setTotalAmount(totalAmounts.toString());
		billModel.setPaidAtDate(datesPaid.toString());
		billModel.setAmountPaid(amountsPaid.toString());
		billModel.setAmountDue(amountsDue.toString());
		billModel.setPayerId(payerIds.toString());
		billModel.setInvoiceId("");
		billModel.setUuid(Util.generateUuid());
		billModel.setCreatedAt(Util.getDateTime());

		if (groupModel != null) {
			billModel.setGroupId(groupModel.getGroupId());
			billModel.setType("gt");
			groupModel.setUpdatedAt(Util.getDateTime());
			mDbHelper.setGroupUpdatedDate(groupModel.getUpdatedAt(), groupModel.getGroupId());
			long rowId = mDbHelper.createEntry(billModel);
			new ServerUtil(context).createEntry(billModel);
			billModel.setId(rowId);
		}

		if (groupModel == null) {
			GroupModel groupModel2 = new GroupModel();
			groupModel2.setGroupName(titleText);
			groupModel2.setTypeId(1);
			groupModel2.setCreatedAt(Util.getDateTime());
			groupModel2.setUpdatedAt(Util.getDateTime());
			groupModel2.setUsers(users.toString());
			long newRowId = mDbHelper.createGroup(groupModel2);
			groupModel2.setId(newRowId);
			new ServerUtil(context).createSingleGroup(groupModel2, billModel, null);
		}

		Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();

		if (getArguments() != null && groupModel != null) {
			if (getArguments().getBoolean("fromGroup") && groupId == groupModel.getGroupId()) {
				communicator.sendRequestCode(billModel);
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 2 && resultCode == RESULT_OK) {
			Uri uri = data.getData();
			imgFile.setVisibility(View.VISIBLE);
			imgFile.setImageURI(uri);
			Image_uri = uri;
		} else if (requestCode == 1 && resultCode == RESULT_OK) {
			Uri uri = data.getData();
			imgFile.setVisibility(View.VISIBLE);
			Image_uri = uri;
			imgFile.setVisibility(View.VISIBLE);
			imgFile.setImageURI(uri);
		} else if (resultCode == RESULT_OK && requestCode == 4) {
			contactRecyclerView.setVisibility(View.VISIBLE);
			loanContactRecyclerView.setVisibility(View.VISIBLE);
			//noinspection unchecked
			List<ContactModel> list = (List<ContactModel>) data.getSerializableExtra("ADDED");
			contactList.addAll(list);
			ContactRecyclerViewAdapter contactRecyclerViewAdapter =
					new ContactRecyclerViewAdapter(contactList);
			contactRecyclerView.setAdapter(contactRecyclerViewAdapter);
			loanContactRecyclerView.setAdapter(contactRecyclerViewAdapter);
			for (ContactModel model : list) {
				users.append(model.getUserId()).append(",");
			}
		}
	}

	@Override
	public void onClick(View v) {

		if (firstFilled) {

			stringBuilder.delete(0, stringBuilder.length());
			amountEditText.setText(stringBuilder);
			firstFilled = false;
		}

		switch (v.getId()) {

			case R.id.btn_seven:

				stringBuilder.append(7);
				if (operatorPressd && counDownRunning) {
					countDownTimer.cancel();
					counDownRunning = false;
					countDownTimer.start();
				} else if (operatorPressd && !counDownRunning) {
					countDownTimer.start();
				}

				break;
			case R.id.btn_eight:

				stringBuilder.append(8);
				if (operatorPressd && counDownRunning) {
					countDownTimer.cancel();
					counDownRunning = false;
					countDownTimer.start();
				} else if (operatorPressd && !counDownRunning) {
					countDownTimer.start();
				}
				break;
			case R.id.btn_nine:

				stringBuilder.append(9);
				if (operatorPressd && counDownRunning) {
					countDownTimer.cancel();
					counDownRunning = false;
					countDownTimer.start();
				} else if (operatorPressd && !counDownRunning) {
					countDownTimer.start();
				}
				break;
			case R.id.btn_multiple:

				if (!operatorPressd) {
					first = Integer.parseInt(stringBuilder.toString());
					firstFilled = true;
					operatorPressd = true;
					operatorValue = 3;
				}
				break;
			case R.id.btn_six:

				stringBuilder.append(6);
				if (operatorPressd && counDownRunning) {
					countDownTimer.cancel();
					counDownRunning = false;
					countDownTimer.start();
				} else if (operatorPressd && !counDownRunning) {
					countDownTimer.start();
				}
				break;
			case R.id.btn_five:

				stringBuilder.append(5);
				if (operatorPressd && counDownRunning) {
					countDownTimer.cancel();
					counDownRunning = false;
					countDownTimer.start();
				} else if (operatorPressd && !counDownRunning) {
					countDownTimer.start();
				}
				break;
			case R.id.btn_four:


				stringBuilder.append(4);
				if (operatorPressd && counDownRunning) {
					countDownTimer.cancel();
					counDownRunning = false;
					countDownTimer.start();
				} else if (operatorPressd && !counDownRunning) {
					countDownTimer.start();
				}
				break;
			case R.id.btn_three:

				stringBuilder.append(3);
				if (operatorPressd && counDownRunning) {
					countDownTimer.cancel();
					counDownRunning = false;
					countDownTimer.start();
				} else if (operatorPressd && !counDownRunning) {
					countDownTimer.start();
				}
				break;
			case R.id.btn_two:

				stringBuilder.append(2);
				if (operatorPressd && counDownRunning) {
					countDownTimer.cancel();
					counDownRunning = false;
					countDownTimer.start();
				} else if (operatorPressd && !counDownRunning) {
					countDownTimer.start();
				}
				break;
			case R.id.btn_one:

				stringBuilder.append(1);
				if (operatorPressd && counDownRunning) {
					countDownTimer.cancel();
					counDownRunning = false;
					countDownTimer.start();
				} else if (operatorPressd && !counDownRunning) {
					countDownTimer.start();
				}
				break;
			case R.id.btn_minus:

				if (!operatorPressd) {
					first = Integer.parseInt(stringBuilder.toString());
					operatorPressd = true;
					firstFilled = true;
					operatorValue = 0;
				}

				break;
			case R.id.btn_plus:

				if (!operatorPressd) {

					first = Integer.parseInt(stringBuilder.toString());
					operatorPressd = true;
					firstFilled = true;
					operatorValue = 1;
				}

				break;
			case R.id.btn_divide:

				if (!operatorPressd) {
					first = Integer.parseInt(stringBuilder.toString());
					operatorPressd = true;
					firstFilled = true;
					operatorValue = 2;
				}

				break;
			case R.id.btn_dot:

				stringBuilder.append(".");
				if (operatorPressd && counDownRunning) {
					countDownTimer.cancel();
					counDownRunning = false;
					countDownTimer.start();
				} else if (operatorPressd && !counDownRunning) {
					countDownTimer.start();
				}
				break;
			case R.id.btn_equal:

				return;
			case R.id.btn_zero:

				stringBuilder.append(0);
				if (operatorPressd && counDownRunning) {
					countDownTimer.cancel();
					counDownRunning = false;
					countDownTimer.start();
				} else if (operatorPressd && !counDownRunning) {
					countDownTimer.start();
				}
				break;
		}

		amountEditText.setText(stringBuilder);


	}

	public interface InterfaceCommunicator {
		void sendRequestCode(BillModel billModel);
	}

	private void performCal() {


		second = Integer.parseInt(stringBuilder.toString());

		switch (operatorValue) {

			case 1:

				first = first + second;

				second = 0;
				operatorPressd = false;
				break;
			case 0:

				first = first - second;

				second = 0;
				operatorPressd = false;
				break;

			case 2:

				try {
					first = first / second;
				}catch (Exception e) {
					Toast.makeText(getActivity(), "Divide by zero", Toast.LENGTH_SHORT).show();
				}
				second = 0;
				operatorPressd = false;
				break;
			case 3:

				first = first * second;
				second = 0;
				operatorPressd = false;
				break;

		}

		amountEditText.setText(String.valueOf(first));
		stringBuilder.delete(0, stringBuilder.length());
		stringBuilder.append(first);
		firstFilled = false;
		sizeChanged = false;
		counDownRunning =false;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		mCallbackActivity = (Main2Activity) context;
		mCallbackActivity.setActivityCommunicateListner(new Main2Activity.OnCommunicateListener() {
			@Override
			public void onCommunicate() {
				Log.e("MY TAG", "THIS IS COLL");
			}
		});
		if (getArguments().getBoolean("fromGroup"))
			communicator = (InterfaceCommunicator) context;
	}

	public static Date getDate(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month);
		cal.set(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public  float calPric(float p) {

		return (float) (p);

	}

	private float getRate (float i) {

		if (n == 1) {
			return (float) (i/100);
		}else if (n == 12){

			return (float) (i/12/100);
		} else {
			return (float) (i/365/100);
		}


	}

	private float simpleInterest(float i) {
		if (n == 1) {
			return i / 100f;
		} else if (n == 12) {
			return (float) (i/12/100);
		}else {
			return (float) (i/365/100f);
		}
	}
	public  float calInt(float i) {

		return (float) (i/12/100);

	}

	public  float calMonth(float y) {

		return (float) (y * 12);

	}

	public  float calDvdnt(float Rate, float Months) {

		return (float) (Math.pow(1+Rate, Months));

	}

	public  float calFinalDvdnt(float Principal, float Rate, float Dvdnt) {

		return (float) (Principal * Rate * Dvdnt);

	}

	public  float calDivider(float Dvdnt) {

		return (float) (Dvdnt-1);

	}

	public  float calEmi(float FD, Float D) {

		return (float) (FD/D);

	}

	public  float calTa(float emi, Float Months) {

		return (float) (emi*Months);

	}

	public  float calTotalInt(float TA, float Principal) {

		return (float) (TA - Principal);

	}
}
