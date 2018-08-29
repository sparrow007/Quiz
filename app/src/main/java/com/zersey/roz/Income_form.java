package com.zersey.roz;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.github.florent37.materialtextfield.MaterialTextField;
import com.zersey.roz.Data.Contacts_contract;
import com.zersey.roz.Data.Contactsdbhelper;
import com.zersey.roz.Data.TransactionDbHelper;
import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class Income_form extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private boolean person_added = false;
	private String Contact_Person_Name, Contact_Person_Number;
	private ImageView Img_File;
	private FloatingActionButton fab;
	private LinearLayout Contact_LinearLayout;
	private ArrayList<String> Contact_list;
	private MaterialTextField Material_Title, Material_Amount, Material_Date, Material_Contact,
		Material_Amount_Due;
	private EditText dateEdit, AmountEdit, TitleEdit, Amount_Due_Edit;
	private View fragmentLayout;
	private int year_x, month_x, day_x, Selected_date = 0;
	private long Updated_Id;
	private String CardClicked, Updated_Category, Updated_Title, Updated_Amount, Updated_Date;
	private Calendar cal;
	private AutoCompleteTextView AutoCompleteContacts;
	private ArrayAdapter<String> ContactAdapter;
	private String[] Months = {
		"Jan", "Feb", "March", "April", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"
	}, Contact_Names;
	private DatePickerDialog datePicker;
	private Button Submit_Income;
	private TextView More_TextView_Button;
	private ImageButton Delete_Button, Camera_Button, Contact_Button;

	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private OnFragmentInteractionListener mListener;
	private int pos;
	private TransactionDbHelper mDbHelper;
	private IncomeModel model;

	public Income_form() {
		// Required empty public constructor
	}

	public void setString(IncomeModel model, String updated_Title, String updated_Amount,
		String updated_Date, String updated_Category, int id, int pos) {
		Updated_Title = model.getTitle();
		//Updated_Amount = model.getTotalAmount();
		Updated_Date = model.getPaidAtDate();
		//Updated_Category = model.getCatId();
		Updated_Id = model.getId();
		this.pos = pos;
		this.model = model;
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment Income_form.
	 */
	// TODO: Rename and change types and number of parameters
	public static Income_form newInstance(String param1, String param2) {
		Income_form fragment = new Income_form();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
		cal = Calendar.getInstance();
		year_x = cal.get(Calendar.YEAR);
		day_x = cal.get(Calendar.DAY_OF_MONTH);
		month_x = cal.get(Calendar.MONTH);
		mDbHelper = TransactionDbHelper.getInstance(getContext());
	}

	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {

		Contact_list = new ArrayList<String>();
		fragmentLayout = inflater.inflate(R.layout.fragment_income_form, container, false);
		Material_Title =
			(MaterialTextField) fragmentLayout.findViewById(R.id.Material_Title_Income);
		Material_Title.setHasFocus(true);
		Material_Amount =
			(MaterialTextField) fragmentLayout.findViewById(R.id.Material_Income_Amount);
		Material_Amount.setHasFocus(true);
		Material_Date = (MaterialTextField) fragmentLayout.findViewById(R.id.Material_Date_Income);
		Material_Date.setHasFocus(true);
		Material_Contact =
			(MaterialTextField) fragmentLayout.findViewById(R.id.Material_Contact_Income);
		Material_Amount_Due =
			(MaterialTextField) fragmentLayout.findViewById(R.id.Material_Amount_Due_Income);
		Material_Amount_Due.setVisibility(View.GONE);
		dateEdit = (EditText) fragmentLayout.findViewById(R.id.Date_Income_Edit);
		//dateEdit.setText(day_x + " " + Months[month_x] + " " + year_x);
		dateEdit.setText(year_x + "-" + (month_x + 1) + "-" + day_x);
		Delete_Button = (ImageButton) fragmentLayout.findViewById(R.id.Delete_Income_Button);
		Delete_Button.setVisibility(View.GONE);
		//Camera_Button=(ImageButton)fragmentLayout.findViewById(R.id.Camera_Income_Button);
		Contact_Button = (ImageButton) fragmentLayout.findViewById(R.id.Contact_Income_Button);
		More_TextView_Button = (TextView) fragmentLayout.findViewById(R.id.More_Income_Button);
		Img_File = (ImageView) fragmentLayout.findViewById(R.id.Img_Income_file);
		Img_File.setVisibility(View.GONE);
		Contact_LinearLayout =
			(LinearLayout) fragmentLayout.findViewById(R.id.Contact_Income_Layout);
		Contact_LinearLayout.setVisibility(View.GONE);

		Submit_Income = (Button) fragmentLayout.findViewById(R.id.Submit_Income_Button);
		TitleEdit = (EditText) fragmentLayout.findViewById(R.id.Title_Income_Edit);
		AmountEdit = (EditText) fragmentLayout.findViewById(R.id.Amount_Income_Edit);
		Amount_Due_Edit = (EditText) fragmentLayout.findViewById(R.id.Amount_Due_Income_Edit);
		AutoCompleteContacts =
			(AutoCompleteTextView) fragmentLayout.findViewById(R.id.Contact_Income_AutoComplete);
		if (!Util.isEmpty(Updated_Title)) {
			Updated_Amount = Updated_Amount.replace("Rs ", "");
			AmountEdit.setText(Updated_Amount);
			TitleEdit.setText(Updated_Title);
			dateEdit.setText(Updated_Date);
			Delete_Button.setVisibility(View.VISIBLE);
		}

		fab = (FloatingActionButton) fragmentLayout.findViewById(R.id.fab_Income_Camera_Button);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/

				if (TextUtils.isEmpty(AutoCompleteContacts.getText().toString())) {
					Onclick_Image_button();
				} else {
					if (person_added == false) {
						final EditText Image_Link, Contact_number;
						LayoutInflater LI = LayoutInflater.from(getContext());
						View PromptsView = LI.inflate(R.layout.image_dialog, null);
						Contact_number = (EditText) PromptsView.findViewById(R.id.Contact_Number);
						//Conact_number.setText(cNumber);
						Image_Link = (EditText) PromptsView.findViewById(R.id.Image_Link);
						//Image_Link.setText(name);
						android.support.v7.app.AlertDialog.Builder alertDialogBuilder =
							new android.support.v7.app.AlertDialog.Builder(getContext());

						alertDialogBuilder.setView(PromptsView);
						final String Name = AutoCompleteContacts.getText().toString();
						if (extractNumber(Name).length() == 10) {
							Contact_number.setText(Name);
						} else {
							if (extractNumber(Name).length() == 0) {
								if (!TextUtils.isEmpty(Name)) {
									Image_Link.setText(Name);
								}
							}
						}
						alertDialogBuilder.setCancelable(true)
							.setPositiveButton("Add Person", new DialogInterface.OnClickListener() {
								@Override public void onClick(DialogInterface dialog, int which) {
									Boolean cancel = false;
									View focus_View = Image_Link;
									String name = Image_Link.getText().toString();
									String finalCNumber = Contact_number.getText().toString();
									if (TextUtils.isEmpty(name)) {
										Image_Link.setError("Required field");
										focus_View = Image_Link;
										cancel = true;
									}
									if (extractNumber(finalCNumber).length() >= 10
										&& extractNumber(finalCNumber).length() <= 10) {
										Contact_number.setError("Required field");
										focus_View = Contact_number;
										cancel = true;
									}
									if (cancel) {
										focus_View.requestFocus();
									} else {

										final SpannableStringBuilder sb =
											new SpannableStringBuilder();
										TextView tv = createContactTextView(name);
										BitmapDrawable bd =
											(BitmapDrawable) convertViewToDrawable(tv);
										bd.setBounds(0, 0, bd.getIntrinsicWidth(),
											bd.getIntrinsicHeight());

										sb.append(name);
										sb.setSpan(new ImageSpan(bd), sb.length() - (name.length()),
											sb.length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
										AutoCompleteContacts.setText(sb);

										//AutoCompleteContacts.setText(name,TextView.BufferType.EDITABLE);
										person_added = true;
										Contact_Person_Name = name;
										Contact_Person_Number = finalCNumber;
										Toast.makeText(getContext(), name
											+ "\n"
											+ extractNumber(finalCNumber)
											+ "\nwas added", Toast.LENGTH_LONG).show();
									}
								}
							});

						android.support.v7.app.AlertDialog alertDialog =
							alertDialogBuilder.create();
						alertDialog.show();
					} else {
						Onclick_Image_button();
					}
				}
			}
		});
		TitleEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus == true) {
					if (TextUtils.isEmpty(AutoCompleteContacts.getText().toString())) {

					} else {
						if (person_added == false) {
							final EditText Image_Link, Contact_number;
							LayoutInflater LI = LayoutInflater.from(getContext());
							View PromptsView = LI.inflate(R.layout.image_dialog, null);
							Contact_number =
								(EditText) PromptsView.findViewById(R.id.Contact_Number);
							//Conact_number.setText(cNumber);
							Image_Link = (EditText) PromptsView.findViewById(R.id.Image_Link);
							//Image_Link.setText(name);
							android.support.v7.app.AlertDialog.Builder alertDialogBuilder =
								new android.support.v7.app.AlertDialog.Builder(getContext());

							alertDialogBuilder.setView(PromptsView);
							final String Name = AutoCompleteContacts.getText().toString();
							if (extractNumber(Name).length() == 10) {
								Contact_number.setText(Name);
							} else {
								if (extractNumber(Name).length() == 0) {
									if (!TextUtils.isEmpty(Name)) {
										Image_Link.setText(Name);
									}
								}
							}
							alertDialogBuilder.setCancelable(true)
								.setPositiveButton("Add Person",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											Boolean cancel = false;
											View focus_View = Image_Link;
											String name = Image_Link.getText().toString();
											String finalCNumber =
												Contact_number.getText().toString();
											if (TextUtils.isEmpty(name)) {
												Image_Link.setError("Required field");
												focus_View = Image_Link;
												cancel = true;
											}
											if (extractNumber(finalCNumber).length() == 10) {
												Contact_number.setError("Required field");
												focus_View = Contact_number;
												cancel = true;
											}
											if (cancel) {
												focus_View.requestFocus();
											} else {
												final SpannableStringBuilder sb =
													new SpannableStringBuilder();
												TextView tv = createContactTextView(name);
												BitmapDrawable bd =
													(BitmapDrawable) convertViewToDrawable(tv);
												bd.setBounds(0, 0, bd.getIntrinsicWidth(),
													bd.getIntrinsicHeight());

												sb.append(name);
												sb.setSpan(new ImageSpan(bd),
													sb.length() - (name.length()), sb.length() - 1,
													Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
												AutoCompleteContacts.setText(sb);

												//AutoCompleteContacts.setText(name,TextView.BufferType.EDITABLE);
												person_added = true;
												Contact_Person_Name = name;
												Contact_Person_Number = finalCNumber;
												Toast.makeText(getContext(), name
													+ "\n"
													+ extractNumber(finalCNumber)
													+ "\nwas added", Toast.LENGTH_LONG).show();
											}
										}
									});

							android.support.v7.app.AlertDialog alertDialog =
								alertDialogBuilder.create();
							alertDialog.show();
						}
					}
				}
			}
		});

		AutoCompleteContacts.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (person_added) {
					person_added = false;
					Contact_Person_Name = "";
					Contact_Person_Number = "";
				}
			}

			@Override public void afterTextChanged(Editable s) {

			}
		});

		AutoCompleteContacts.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {

				} else {

				}
			}
		});

		AmountEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus == true) {
					if (TextUtils.isEmpty(AutoCompleteContacts.getText().toString())) {

					} else {
						if (person_added == false) {
							final EditText Image_Link, Contact_number;
							LayoutInflater LI = LayoutInflater.from(getContext());
							View PromptsView = LI.inflate(R.layout.image_dialog, null);
							Contact_number =
								(EditText) PromptsView.findViewById(R.id.Contact_Number);
							//Conact_number.setText(cNumber);
							Image_Link = (EditText) PromptsView.findViewById(R.id.Image_Link);
							//Image_Link.setText(name);
							android.support.v7.app.AlertDialog.Builder alertDialogBuilder =
								new android.support.v7.app.AlertDialog.Builder(getContext());

							alertDialogBuilder.setView(PromptsView);
							String Name = AutoCompleteContacts.getText().toString();
							Name = Name.replace(",", "");
							if (extractNumber(Name).length() == 10) {
								Contact_number.setText(Name);
							} else {
								if (extractNumber(Name).length() == 0) {
									if (!TextUtils.isEmpty(Name)) {
										Image_Link.setText(Name);
									}
								}
							}
							alertDialogBuilder.setCancelable(true)
								.setPositiveButton("Add Person",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											Boolean cancel = false;
											View focus_View = Image_Link;
											String name = Image_Link.getText().toString();
											String finalCNumber =
												Contact_number.getText().toString();
											if (TextUtils.isEmpty(name)) {
												Image_Link.setError("Required field");
												focus_View = Image_Link;
												cancel = true;
											}
											if (extractNumber(finalCNumber).length() < 10) {
												Contact_number.setError("Required field");
												focus_View = Contact_number;
												cancel = true;
											}
											if (cancel) {
												focus_View.requestFocus();
											} else {
												final SpannableStringBuilder sb =
													new SpannableStringBuilder();
												TextView tv = createContactTextView(name);
												BitmapDrawable bd =
													(BitmapDrawable) convertViewToDrawable(tv);
												bd.setBounds(0, 0, bd.getIntrinsicWidth(),
													bd.getIntrinsicHeight());

												sb.append(name);
												sb.setSpan(new ImageSpan(bd), 0, sb.length(),
													Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
												AutoCompleteContacts.setText("");
												AutoCompleteContacts.setText(sb);

												//AutoCompleteContacts.setText(name,TextView.BufferType.EDITABLE);
												person_added = true;
												Contact_Person_Name = name;
												Contact_Person_Number = finalCNumber;
												Toast.makeText(getContext(), name
													+ "\n"
													+ extractNumber(finalCNumber)
													+ "\nwas added", Toast.LENGTH_LONG).show();
											}
										}
									});

							android.support.v7.app.AlertDialog alertDialog =
								alertDialogBuilder.create();
							alertDialog.show();
						} else {
							Toast.makeText(getContext(),
								"Person was added" + AutoCompleteContacts.getText(),
								Toast.LENGTH_LONG).show();
						}
					}
				}
			}
		});

		dateEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus == true) {
					if (TextUtils.isEmpty(AutoCompleteContacts.getText().toString())) {

					} else {
						if (person_added == false) {
							final EditText Image_Link, Contact_number;
							LayoutInflater LI = LayoutInflater.from(getContext());
							View PromptsView = LI.inflate(R.layout.image_dialog, null);
							Contact_number =
								(EditText) PromptsView.findViewById(R.id.Contact_Number);
							//Conact_number.setText(cNumber);
							Image_Link = (EditText) PromptsView.findViewById(R.id.Image_Link);
							//Image_Link.setText(name);
							android.support.v7.app.AlertDialog.Builder alertDialogBuilder =
								new android.support.v7.app.AlertDialog.Builder(getContext());

							alertDialogBuilder.setView(PromptsView);
							final String Name = AutoCompleteContacts.getText().toString();
							if (extractNumber(Name).length() == 10) {
								Contact_number.setText(Name);
							} else {
								if (extractNumber(Name).length() == 0) {
									if (!TextUtils.isEmpty(Name)) {
										Image_Link.setText(Name);
									}
								}
							}
							alertDialogBuilder.setCancelable(true)
								.setPositiveButton("Add Person",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											Boolean cancel = false;
											View focus_View = Image_Link;
											String name = Image_Link.getText().toString();
											String finalCNumber =
												Contact_number.getText().toString();
											if (TextUtils.isEmpty(name)) {
												Image_Link.setError("Required field");
												focus_View = Image_Link;
												cancel = true;
											}
											if (extractNumber(finalCNumber).length() < 10) {
												Contact_number.setError("Required field");
												focus_View = Contact_number;
												cancel = true;
											}
											if (cancel) {
												focus_View.requestFocus();
											} else {
												final SpannableStringBuilder sb =
													new SpannableStringBuilder();
												TextView tv = createContactTextView(name);
												BitmapDrawable bd =
													(BitmapDrawable) convertViewToDrawable(tv);
												bd.setBounds(0, 0, bd.getIntrinsicWidth(),
													bd.getIntrinsicHeight());

												sb.append(name);
												sb.setSpan(new ImageSpan(bd), 0, sb.length(),
													Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
												AutoCompleteContacts.setText(sb);

												//AutoCompleteContacts.setText(name,TextView.BufferType.EDITABLE);
												person_added = true;
												Contact_Person_Name = name;
												Contact_Person_Number = finalCNumber;
												Toast.makeText(getContext(), name
													+ "\n"
													+ extractNumber(finalCNumber)
													+ "\nwas added", Toast.LENGTH_LONG).show();
											}
										}
									});

							android.support.v7.app.AlertDialog alertDialog =
								alertDialogBuilder.create();
							alertDialog.show();
						}
					}
				}
			}
		});

		Amount_Due_Edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus == true) {
					if (TextUtils.isEmpty(AutoCompleteContacts.getText().toString())) {

					} else {
						if (person_added == false) {
							final EditText Image_Link, Contact_number;
							LayoutInflater LI = LayoutInflater.from(getContext());
							View PromptsView = LI.inflate(R.layout.image_dialog, null);
							Contact_number =
								(EditText) PromptsView.findViewById(R.id.Contact_Number);
							//Conact_number.setText(cNumber);
							Image_Link = (EditText) PromptsView.findViewById(R.id.Image_Link);
							//Image_Link.setText(name);
							android.support.v7.app.AlertDialog.Builder alertDialogBuilder =
								new android.support.v7.app.AlertDialog.Builder(getContext());

							alertDialogBuilder.setView(PromptsView);
							final String Name = AutoCompleteContacts.getText().toString();
							if (extractNumber(Name).length() == 10) {
								Contact_number.setText(Name);
							} else {
								if (extractNumber(Name).length() == 0) {
									if (!TextUtils.isEmpty(Name)) {
										Image_Link.setText(Name);
									}
								}
							}
							alertDialogBuilder.setCancelable(true)
								.setPositiveButton("Add Person",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											Boolean cancel = false;
											View focus_View = Image_Link;
											String name = Image_Link.getText().toString();
											String finalCNumber =
												Contact_number.getText().toString();
											if (TextUtils.isEmpty(name)) {
												Image_Link.setError("Required field");
												focus_View = Image_Link;
												cancel = true;
											}
											if (extractNumber(finalCNumber).length() < 10) {
												Contact_number.setError("Required field");
												focus_View = Contact_number;
												cancel = true;
											}
											if (cancel) {
												focus_View.requestFocus();
											} else {
												final SpannableStringBuilder sb =
													new SpannableStringBuilder();
												TextView tv = createContactTextView(name);
												BitmapDrawable bd =
													(BitmapDrawable) convertViewToDrawable(tv);
												bd.setBounds(0, 0, bd.getIntrinsicWidth(),
													bd.getIntrinsicHeight());

												sb.append(name);
												sb.setSpan(new ImageSpan(bd), 0, sb.length(),
													Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
												AutoCompleteContacts.setText(sb);

												//AutoCompleteContacts.setText(name,TextView.BufferType.EDITABLE);
												person_added = true;
												Contact_Person_Name = name;
												Contact_Person_Number = finalCNumber;
												Toast.makeText(getContext(), name
													+ "\n"
													+ extractNumber(finalCNumber)
													+ "\nwas added", Toast.LENGTH_LONG).show();
											}
										}
									});

							android.support.v7.app.AlertDialog alertDialog =
								alertDialogBuilder.create();
							alertDialog.show();
						}
					}
				}
			}
		});

		datePicker = new DatePickerDialog(getContext(), R.style.Theme_AppCompat_DayNight_Dialog,
			dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
			cal.get(Calendar.DAY_OF_MONTH));
		datePicker.setOnDateSetListener(dateSetListener);
		datePicker.setCancelable(true);
		datePicker.setTitle("Select Date");

		dateEdit.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				datePicker.show();
			}
		});
		Submit_Income.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {

				if (TextUtils.isEmpty(AutoCompleteContacts.getText().toString())) {
					Submit();
				} else {
					if (person_added == false) {
						final EditText Image_Link, Contact_number;
						LayoutInflater LI = LayoutInflater.from(getContext());
						View PromptsView = LI.inflate(R.layout.image_dialog, null);
						Contact_number = (EditText) PromptsView.findViewById(R.id.Contact_Number);
						//Conact_number.setText(cNumber);
						Image_Link = (EditText) PromptsView.findViewById(R.id.Image_Link);
						//Image_Link.setText(name);
						android.support.v7.app.AlertDialog.Builder alertDialogBuilder =
							new android.support.v7.app.AlertDialog.Builder(getContext());

						alertDialogBuilder.setView(PromptsView);
						final String Name = AutoCompleteContacts.getText().toString();
						if (extractNumber(Name).length() == 10) {
							Contact_number.setText(Name);
						} else {
							if (extractNumber(Name).length() == 0) {
								if (!TextUtils.isEmpty(Name)) {
									Image_Link.setText(Name);
								}
							}
						}
						alertDialogBuilder.setCancelable(true)
							.setPositiveButton("Add Person", new DialogInterface.OnClickListener() {
								@Override public void onClick(DialogInterface dialog, int which) {
									Boolean cancel = false;
									View focus_View = Image_Link;
									String name = Image_Link.getText().toString();
									String finalCNumber = Contact_number.getText().toString();
									if (TextUtils.isEmpty(name)) {
										Image_Link.setError("Required field");
										focus_View = Image_Link;
										cancel = true;
									}
									if (extractNumber(finalCNumber).length() < 10) {
										Contact_number.setError("Required field");
										focus_View = Contact_number;
										cancel = true;
									}
									if (cancel) {
										focus_View.requestFocus();
									} else {
										final SpannableStringBuilder sb =
											new SpannableStringBuilder();
										TextView tv = createContactTextView(name);
										BitmapDrawable bd =
											(BitmapDrawable) convertViewToDrawable(tv);
										bd.setBounds(0, 0, bd.getIntrinsicWidth(),
											bd.getIntrinsicHeight());

										sb.append(name);
										sb.setSpan(new ImageSpan(bd), 0, sb.length(),
											Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
										AutoCompleteContacts.setText(sb);

										//AutoCompleteContacts.setText(name,TextView.BufferType.EDITABLE);
										person_added = true;
										Contact_Person_Name = name;
										Contact_Person_Number = finalCNumber;
										Toast.makeText(getContext(), name
											+ "\n"
											+ extractNumber(finalCNumber)
											+ "\nwas added", Toast.LENGTH_LONG).show();
									}
								}
							});

						android.support.v7.app.AlertDialog alertDialog =
							alertDialogBuilder.create();
						alertDialog.show();
					} else {
						Submit();
					}
				}
			}
		});
		Delete_Button.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				Delete_Button();
			}
		});
		Contact_Button.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				Contact_Button();
			}
		});

        /*Camera_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Onclick_Image_button();
            }
        });*/
		More_TextView_Button.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				MoreButton();
			}
		});

		return fragmentLayout;
	}

	private DatePickerDialog.OnDateSetListener dateSetListener =
		new DatePickerDialog.OnDateSetListener() {
			@Override public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
				year_x = year;
				month_x = month + 1;
				day_x = dayOfMonth;
				Selected_date = dayOfMonth;
				//year_x = cal.get(Calendar.YEAR);
				//day_x = cal.get(Calendar.DAY_OF_MONTH);
				month_x = cal.get(Calendar.MONTH);
                     /*if(year==cal.get(Calendar.YEAR) && month==cal.get(Calendar.MONTH) && dayOfMonth==cal.get(Calendar.DAY_OF_MONTH) ){
                         dateEdit.setText("Today");
                     } else if(year==cal.get(Calendar.YEAR) && month==cal.get(Calendar.MONTH) && dayOfMonth==cal.get(Calendar.DAY_OF_MONTH)+1 ){
                         dateEdit.setText("Tomorrow");
                     }else if(year==cal.get(Calendar.YEAR) && month==cal.get(Calendar.MONTH) && dayOfMonth==cal.get(Calendar.DAY_OF_MONTH)-1 ){
                         dateEdit.setText("Yesterday");
                     }else{ dateEdit.setText(dayOfMonth+" "+Months[month]+" "+year);}*/
				//dateEdit.setText(dayOfMonth + " " + Months[month] + " " + year);
				dateEdit.setText(year_x + "-" + month_x + "-" + day_x);
			}
		};

	Thread th = new Thread() {
		@Override public void run() {

			ArrayAdapter<String> contactAdapter =
				new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line,
					Contact_Names);
			AutoCompleteTextView autoCompleteContacts =
				(AutoCompleteTextView) fragmentLayout.findViewById(
					R.id.Contact_Income_AutoComplete);

			autoCompleteContacts.setAdapter(contactAdapter);
			//autoCompleteContacts.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
		}
	};

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}

	@Override public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnFragmentInteractionListener) {
			mListener = (OnFragmentInteractionListener) context;
		} else {
			throw new RuntimeException(
				context.toString() + " must implement OnFragmentInteractionListener");
		}
	}

	@Override public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		void onFragmentInteraction(Uri uri);
	}

	public void Submit() {
		String Amount_text = AmountEdit.getText().toString();
		String Contacts = AutoCompleteContacts.getText().toString();
		String Title_text = TitleEdit.getText().toString();
		View focus = null;
		Boolean cancel = false;

		if (day_x == 0) {
			dateEdit.setError("Date can't be empty");
			focus = dateEdit;
			cancel = true;
		}
		if (!TextUtils.isDigitsOnly(Amount_text)) {
			AmountEdit.setError("Amount can't be empty");
			focus = AmountEdit;
			cancel = true;
		}
		if (TextUtils.isEmpty(Title_text)) {
			TitleEdit.setError("Title can't be empty");
			focus = TitleEdit;
			cancel = true;
		}

		if (cancel) {
			focus.requestFocus();
		} else {
			if (person_added) {
				if (Check_Contacts(Contact_Person_Name, Contact_Person_Number)) {
					Toast.makeText(getContext(), "Contact is already present in the database",
						Toast.LENGTH_LONG).show();
				} else {
					Contactsdbhelper mdbhelper = new Contactsdbhelper(getContext());
					SQLiteDatabase db = mdbhelper.getWritableDatabase();
					ContentValues values = new ContentValues();

					values.put(Contacts_contract.Contacts_Entry.Column_Contact_Name,
						Contact_Person_Name);
					values.put(Contacts_contract.Contacts_Entry.Column_Contact_Number,
						Contact_Person_Number);
					//values.put(Transaction_contract.Transaction_Entry.Column_Amount, "Rs " + Amount_text);
					//values.put(Transaction_contract.Transaction_Entry.Column_Date_Created, DateEdit_text);

					long newRowId =
						db.insert(Contacts_contract.Contacts_Entry.Table_name, null, values);
					if (newRowId == -1) {
						// If the row COLUMN_ONLINE_ID is -1, then there was an error with insertion.
						Toast.makeText(getContext(), "Error with saving Contact",
							Toast.LENGTH_SHORT).show();
					} else {
						// Otherwise, the insertion was successful and we can display a toast with the row COLUMN_ONLINE_ID.
						Toast.makeText(getContext(), "Contact saved with row id: " + newRowId,
							Toast.LENGTH_SHORT).show();
					}
				}
			}

			if (Util.isEmpty(Updated_Title)) {
				String DateEdit_text = dateEdit.getText().toString();
				//Custom_items items = new Custom_items(Category_text,
				//      Title_text, "Rs " + Amount_text, day_x + " " + Months[month_x - 1] + " " + year_x);
				//customlist.add(items);

				SharedPreferences prefs = getContext().getSharedPreferences("login", MODE_PRIVATE);

				GroupModel model = new GroupModel();
				model.setGroupName(Title_text);
				//model.setGroupDesc();
				model.setUsers(prefs.getString("userid", null));
				long groupId = mDbHelper.createGroup(model);

				IncomeModel incomeModel = new IncomeModel();
				incomeModel.setTitle(Title_text);
				incomeModel.setGroupId(groupId);
				//incomeModel.setTotalAmount(Amount_text);
				incomeModel.setPaidAtDate(DateEdit_text);
				incomeModel.setCatId(Updated_Category);
				incomeModel.setType("income");
				//incomeModel.setUuid(Util.generateUuid(prefs.getString("userid", null)));
				//incomeModel.setAmountDue(Amount_text);

				mDbHelper.createEntry(incomeModel);

				//long rowId = mDbHelper.createEntry(incomeModel);
				//incomeModel.setId(rowId);
				//if (NetworkUtil.hasInternetConnection(getContext())) {
				//	new ServerUtil(getContext()).createEntry(incomeModel);
				//} else {
				//	mDbHelper.addToTemp(rowId, 0, "new");
				//}
				//new ServerUtil(getContext()).createEntry(incomeModel);
				//if (newRowId == -1) {
				//	// If the row COLUMN_ONLINE_ID is -1, then there was an error with insertion.
				//	Toast.makeText(getContext(), "Error with saving pet", Toast.LENGTH_SHORT)
				//		.show();
				//} else {
				//	// Otherwise, the insertion was successful and we can display a toast with the row COLUMN_ONLINE_ID.
				//	Toast.makeText(getContext(), "Recipe saved with row id: " + newRowId,
				//		Toast.LENGTH_SHORT).show();
				//}
				Bundle args = new Bundle();
				//args.putParcelableArrayList("ARRAYLIST",(ArrayList<? extends Parcelable>) customlist);
				Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG).show();
			} else {
				Updated_Title = TitleEdit.getText().toString();
				Updated_Amount = AmountEdit.getText().toString();
				Updated_Date = dateEdit.getText().toString();
				//
				//model.setPaidAtDate(Updated_Date);
				//model.setTotalAmount(Updated_Amount);
				//model.setTitle(Updated_Title);

				mDbHelper.updateEntry(pos, model);
				if (NetworkUtil.hasInternetConnection(getContext())) {
					new ServerUtil(getContext()).updateEntry(model);
				} else {
					mDbHelper.addToTemp(model.getId(), 0, "edit");
				}
				//	TransactionDbHelper mdbhelper = new TransactionDbHelper(getContext());
				//	SQLiteDatabase db = mdbhelper.getWritableDatabase();
				//	ContentValues values = new ContentValues();
				//	//values.put(recipe_entry.Column_Recipe_Image,byteimage);
				//	values.put(TransactionDbContract.Transaction_Entry.COLUMN_TITLE, Updated_Title);
				//	values.put(TransactionDbContract.Transaction_Entry.COLUMN_CATEGORY,
				//		Updated_Category);
				//	values.put(TransactionDbContract.Transaction_Entry.COLUMN_AMOUNT,
				//		"Rs " + Updated_Amount);
				//	values.put(TransactionDbContract.Transaction_Entry.COLUMN_DATE_CREATED,
				//		Updated_Date);
				//	db.update(TransactionDbContract.Transaction_Entry.TABLE_NAME, values,
				//		TransactionDbContract.Transaction_Entry.COLUMN_ONLINE_ID + "=" + Updated_Id, null);
				//	new ServerUtil(getContext()).updateEntry(incomeModel);
				//}
			}
			//Intent intent = new Intent(getContext(), Main2Activity.class);
			//startActivity(intent);
			getActivity().finish();
		}
	}

	public void Onclick_Image_button() {
		final EditText Image_Link;

		LayoutInflater LI = LayoutInflater.from(getContext());
		//View PromptsView=LI.inflate(R.layout.image_dialog,null);

		//Image_Link=(EditText)PromptsView.findViewById(R.id.);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

		//alertDialogBuilder.setView(PromptsView);

		alertDialogBuilder.setCancelable(true)
			.setTitle(Html.fromHtml("<font color='#3F51B5'>Choose an Image from</font>"))
			.setPositiveButton(Html.fromHtml("<font color='#3F51B5'>camera</font>"),
				new DialogInterface.OnClickListener() {
					@Override public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						startActivityForResult(intent, 2);
						//mEditor.insertImage(Image_Link.getText().toString(),"Image");
						//mEditor.insertLink(Href_Link.getText().toString(), Href_Title.getText().toString());

					}
				})
			.setNegativeButton(Html.fromHtml("<font color='#3F51B5'>Gallery</font>"),
				new DialogInterface.OnClickListener() {
					@Override public void onClick(DialogInterface dialog, int which) {
						Intent i = new Intent(Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

						startActivityForResult(i, 1);
					}
				});

		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public void Delete_Button() {
		if (NetworkUtil.hasInternetConnection(getContext())) {
			new ServerUtil(getContext()).deleteEntry(model.getOnlineId());
		} else {
			mDbHelper.addToTemp(model.getId(), model.getOnlineId(), "delete");
		}

		mDbHelper.deleteEntry(pos, model.getId());
		getActivity().finish();
	}

	@Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//callbackManager.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 2 && resultCode == RESULT_OK) {

			//Bitmap photo = (Bitmap) data.getExtras().get("data");
			Uri uri = data.getData();
			Img_File.setVisibility(View.VISIBLE);
			Img_File.setImageURI(uri);
			//Image_uri=uri;
		} else if (requestCode == 1 && resultCode == RESULT_OK) {
			//Bitmap photo = (Bitmap) data.getExtras().get("data");
			Uri uri = data.getData();
			//Img_File.setVisibility(View.VISIBLE);
			//Image_uri=uri;
			Img_File.setVisibility(View.VISIBLE);
			Img_File.setImageURI(uri);
		} else if (requestCode == 3 && resultCode == RESULT_OK) {
			//Bitmap photo = (Bitmap) data.getExtras().get("data");
			// Uri uri = data.getData();
			Uri contactData = data.getData();
			Cursor c = getActivity().managedQuery(contactData, null, null, null, null);
			if (c.moveToFirst()) {
				String cNumber = null;
				final String name =
					c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

				String hasPhone =
					c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

				if (hasPhone.equalsIgnoreCase("1")) {
					Cursor phones = getActivity().getContentResolver()
						.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null,
							null);
					phones.moveToFirst();
					cNumber = phones.getString(phones.getColumnIndex("data1"));
					//System.out.println("number is:"+cNumber);
				}

				AutoCompleteContacts.setText(name);
				final EditText Image_Link, Contact_number;
				LayoutInflater LI = LayoutInflater.from(getContext());
				View PromptsView = LI.inflate(R.layout.image_dialog, null);
				Contact_number = (EditText) PromptsView.findViewById(R.id.Contact_Number);
				cNumber = cNumber.replace("+", "");
				cNumber = cNumber.replace(" ", "");
				Contact_number.setText(cNumber);
				Image_Link = (EditText) PromptsView.findViewById(R.id.Image_Link);
				Image_Link.setText(name);
				android.support.v7.app.AlertDialog.Builder alertDialogBuilder =
					new android.support.v7.app.AlertDialog.Builder(getContext());

				alertDialogBuilder.setView(PromptsView);
				final String finalCNumber = cNumber;
				alertDialogBuilder.setCancelable(true)
					.setPositiveButton("Add Person", new DialogInterface.OnClickListener() {
						@Override public void onClick(DialogInterface dialog, int which) {
							Boolean cancel = false;
							View focus_View = Image_Link;
							String name = Image_Link.getText().toString();
							String finalCNumber = Contact_number.getText().toString();
							if (TextUtils.isEmpty(name)) {
								Image_Link.setError("Required field");
								focus_View = Image_Link;
								cancel = true;
							}
							if (extractNumber(finalCNumber).length() > 13) {
								Contact_number.setError("Required field");
								focus_View = Contact_number;
								cancel = true;
							}
							if (cancel) {
								focus_View.requestFocus();
							} else {
								final SpannableStringBuilder sb = new SpannableStringBuilder();
								TextView tv = createContactTextView(name);
								BitmapDrawable bd = (BitmapDrawable) convertViewToDrawable(tv);
								bd.setBounds(0, 0, bd.getIntrinsicWidth(), bd.getIntrinsicHeight());

								sb.append(name);
								sb.setSpan(new ImageSpan(bd), 0, sb.length(),
									Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
								AutoCompleteContacts.setText(sb);

								//AutoCompleteContacts.setText(name,TextView.BufferType.EDITABLE);
								person_added = true;
								Contact_Person_Name = name;
								Contact_Person_Number = finalCNumber;
								Toast.makeText(getContext(),
									name + "\n" + extractNumber(finalCNumber) + "\nwas added",
									Toast.LENGTH_LONG).show();
							}
						}
					});

				android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
			}
		}
	}

	public void MoreButton() {
		Fetch_Contacts();
		th.run();
		if (Contact_LinearLayout.getVisibility() == View.GONE) {
			Contact_LinearLayout.setVisibility(View.VISIBLE);
			Material_Amount_Due.setVisibility(View.VISIBLE);
			if (!TextUtils.isEmpty(AmountEdit.getText().toString())) {
				Amount_Due_Edit.setText(AmountEdit.getText().toString());
			} else {
				Amount_Due_Edit.setText("");
			}

            /*YoYo.with(Techniques.SlideInLeft)
                    .duration(1000)
                    .repeat(0)
                    .playOn(Notes_Layout);*/
			Material_Contact.setHasFocus(true);
			Material_Amount_Due.setHasFocus(true);
			// More_Button.setImageDrawable(getResources().getDrawable(R.drawable.uparrow));

		} else {
			Material_Contact.setHasFocus(false);
			Material_Amount_Due.setHasFocus(false);
			Contact_LinearLayout.setVisibility(View.GONE);
			Material_Amount_Due.setVisibility(View.GONE);
			//More_Button.setImageDrawable(getResources().getDrawable(R.drawable.downarrow));
           /* YoYo.with(Techniques.SlideOutLeft)
                    .duration(1000)
                    .repeat(0)
                    .playOn(Notes_Layout);
            final Handler handler3 = new Handler();
            handler3.postDelayed(new Runnable() {
                @Override
                public void run() {

                    Notes_Layout.setVisibility(View.GONE);
                    More_Button.setImageDrawable(getResources().getDrawable(R.drawable.downarrow));
                }
            }, 500);*/

		}
	}

	public void Contact_Button() {
		Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

		startActivityForResult(intent, 3);
	}

	public void Fetch_Contacts() {
		ContentResolver cr = getContext().getContentResolver();
		Cursor phones = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		//phones.moveToFirst();
		while (phones.moveToNext()) {
			String name =
				phones.getString(phones.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			//String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			//Toast.makeText(getApplicationContext(),name, Toast.LENGTH_LONG).show();
			Contact_list.add(name);
			phones.moveToNext();
		}
		phones.close();

		Contact_Names = new String[200];

		try {
			for (int i = 0; i < 200; i++) {
				Contact_Names[i] = Contact_list.get(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Toast.makeText(getApplicationContext(),"Number of contacts present "+Contact_list.size(), Toast.LENGTH_LONG).show();
		//Toast.makeText(getApplicationContext(),"last contact :"+Contact_Names[199], Toast.LENGTH_LONG).show();

	}

	public static String extractNumber(final String str) {

		if (str == null || str.isEmpty()) return "";

		StringBuilder sb = new StringBuilder();
		boolean found = false;
		for (char c : str.toCharArray()) {
			if (Character.isDigit(c)) {
				sb.append(c);
				found = true;
			} else if (found) {
				// If we already found a digit before and this char is not a digit, stop looping
				break;
			}
		}

		return sb.toString();
	}

	public TextView createContactTextView(String text) {
		//creating textview dynamically
		TextView tv = new TextView(getContext());
		tv.setText(text);
		tv.setTextSize(20);
		tv.setBackgroundResource(R.color.newdarkblue);
		//tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_clear_search_api_holo_light, 0);
		return tv;
	}

	public static Object convertViewToDrawable(View view) {
		int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		view.measure(spec, spec);
		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
		Bitmap b = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
			Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(b);
		c.translate(-view.getScrollX(), -view.getScrollY());
		view.draw(c);
		view.setDrawingCacheEnabled(true);
		Bitmap cacheBmp = view.getDrawingCache();
		Bitmap viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
		view.destroyDrawingCache();
		return new BitmapDrawable(viewBmp);
	}

	public Boolean Check_Contacts(String contactname, String contactnumber) {
		ArrayList<Custom_Contact_items> Item_list = new ArrayList<>();
		Contactsdbhelper mDbHelper = new Contactsdbhelper(getContext());
		SQLiteDatabase db1 = mDbHelper.getReadableDatabase();
		//final  ArrayList<String> labellist=new ArrayList<>();
		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = {
			Contacts_contract.Contacts_Entry._ID,
			Contacts_contract.Contacts_Entry.Column_Contact_Name,
			Contacts_contract.Contacts_Entry.Column_Contact_Number
		};

		//Cursor cursor=db1.rawQuery("SELECT * FROM "+recipe_entry.Table_name,null);
		// Perform a query on the pets table
		Cursor cursor =
			db1.query(Contacts_contract.Contacts_Entry.Table_name,   // The table to query
				projection,            // The columns to return
				null,                  // The columns for the WHERE clause
				null,                  // The values for the WHERE clause
				null,                  // Don't group the rows
				null,                  // Don't filter by row groups
				null);                   // The sort order

		try {

			cursor.moveToFirst();
			// Iterate through all the returned rows in the cursor
			while (!cursor.isAfterLast()) {
				// Use that index to extract the String or Int value of the word
				// at the current row the cursor is on.
				int currentID =
					cursor.getInt(cursor.getColumnIndex(Contacts_contract.Contacts_Entry._id));
				String Name = cursor.getString(
					cursor.getColumnIndex(Contacts_contract.Contacts_Entry.Column_Contact_Name));
				String Number = cursor.getString(
					cursor.getColumnIndex(Contacts_contract.Contacts_Entry.Column_Contact_Number));
				if (TextUtils.equals(Number, contactnumber)) {
					return true;
				}
				Custom_Contact_items CCItem = new Custom_Contact_items(Name, Number);
				Item_list.add(CCItem);

				cursor.moveToNext();
			}

			for (int i = 0; i < Item_list.size(); i++) {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
}
