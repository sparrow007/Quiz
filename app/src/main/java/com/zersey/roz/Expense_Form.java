package com.zersey.roz;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
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
import android.widget.TextView;
import android.widget.Toast;
import com.github.florent37.materialtextfield.MaterialTextField;
import com.zersey.roz.Data.Contacts_contract;
import com.zersey.roz.Data.Contactsdbhelper;
import com.zersey.roz.Data.TransactionDbHelper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
  interface
 * to handle interaction events.
 * Use the {@link Expense_Form#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Expense_Form extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private boolean person_added = false;
	private String Contact_Person_Name, Contact_Person_Number;
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	private ImageButton fab;
	private DatePickerDialog datePicker;
	private View fragmentLayout;
	ImageView Img_File;
	private Button Submit_button;
	private TextView More_TextButton;
	private ImageButton Delete_Button, Camera_Button;
	private AutoCompleteTextView AutoCompleteContacts;
	private ArrayAdapter<String> ContactAdapter;
	private MaterialTextField Material_Title, Material_Amount,
		Material_Amount_Due;
	public View layout_view = null;
	private List<BillModel> customlist;
	private ArrayList<String> Contact_list;
	private TextView Category_text_view;
	private int year_x, month_x, day_x, Selected_date = 0;
	private long Updated_Id;
	private String Category_text, Notes_text, Amount_text, Title_text;
	private Uri Image_uri = null;
	private static int DIALOG_ID = 0;
	private EditText dateEdit, AmountEdit, TitleEdit, Amount_Due_Edit;
	private String CardClicked, Updated_Category, Updated_Title, Updated_Amount, Updated_Date;
	private Calendar cal;
	private String[] Months = {
		"Jan", "Feb", "March", "April", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"
	}, Contact_Names;
/*	private LinearLayout Clothing, Entertainment, Food, Fuel, Health, Salary, More, Notes_Layout;
	private CheckBox Clothing_checkbox, Entertainment_checkbox, Food_checkbox, Fuel_checkbox,
		Health_checkbox, Salary_checkbox, More_checkbox;*/

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	//private OnFragmentInteractionListener mListener;
	private int pos;
	private TransactionDbHelper mDbHelper;
	private BillModel model;

	public Expense_Form() {
		// Required empty public constructor
	}

	public void setString(BillModel model, String cardClicked, String updated_Title,
		String updated_Amount, String updated_Date, String updated_Category, int id, int pos) {
		CardClicked = cardClicked;
		Updated_Title = model.getTitle();
		//Updated_Amount = model.getTotalAmount();
		Updated_Date = model.getPaidAtDate();
		Updated_Category = model.getCatId();
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
	 * @return A new instance of fragment Expense_Form.
	 */
	// TODO: Rename and change types and number of parameters
	public static Expense_Form newInstance(String param1, String param2) {
		Expense_Form fragment = new Expense_Form();
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
		mDbHelper = TransactionDbHelper.getInstance(getContext());
	}

	@SuppressLint("NewApi") @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		fragmentLayout = inflater.inflate(R.layout.fragment_bills_form, container, false);

		Contact_list = new ArrayList<String>();

		//customlist=(ArrayList<Custom_items>)getIntent().getBundleExtra("Bundle").getSerializable("ARRAYLIST");
		Material_Title = (MaterialTextField) fragmentLayout.findViewById(R.id.Material_Title);
		Material_Title.setHasFocus(true);
		//Material_Amount = (MaterialTextField) fragmentLayout.findViewById(R.id.Material_Amount);
		Material_Amount.setHasFocus(true);
		//Material_Date = (MaterialTextField) fragmentLayout.findViewById(R.id.Material_Date);
		//Material_Date.setHasFocus(true);
		//Material_Notes = (MaterialTextField) fragmentLayout.findViewById(R.id.Material_Notes);
		//Notes_Layout = (LinearLayout) fragmentLayout.findViewById(R.id.Notes_Layout);
		//Material_Amount_Due =
		//	(MaterialTextField) fragmentLayout.findViewById(R.id.Material_Amount_Due);
		//Material_Amount_Due.setVisibility(View.GONE);
		//More_TextButton = (TextView) fragmentLayout.findViewById(R.id.MoreButton);
		//Notes_Layout.setVisibility(View.GONE);
		cal = Calendar.getInstance();
		year_x = cal.get(Calendar.YEAR);
		day_x = cal.get(Calendar.DAY_OF_MONTH);
		month_x = cal.get(Calendar.MONTH);
		//More_TextButton = (TextView) fragmentLayout.findViewById(R.id.MoreButton);
		//Delete_Button = (ImageButton) fragmentLayout.findViewById(R.id.Delete_Button);
		//Delete_Button.setVisibility(View.GONE);
		//addMembers = (ImageButton) fragmentLayout.findViewById(R.id.addMembers);
		//Camera_Button=(ImageButton)fragmentLayout.findViewById(R.id.Camera_Button);
		//requestPermissions(Manifest.permission.CAMERA,1111);
		//Img_File = (ImageView) fragmentLayout.findViewById(R.id.Img_file);
		//Img_File.setVisibility(View.GONE);
		//dateEdit = (EditText) fragmentLayout.findViewById(R.id.Date_Edit);
		//dateEdit.setText(year_x + "-" + (month_x + 1) + "-" + day_x);
		TitleEdit = (EditText) fragmentLayout.findViewById(R.id.Title_Edit);
		AmountEdit = (EditText) fragmentLayout.findViewById(R.id.Amount_Edit);
		//Amount_Due_Edit = (EditText) fragmentLayout.findViewById(R.id.Amount_Due_Edit);
		//AutoCompleteContacts = (AutoCompleteTextView) fragmentLayout.findViewById(R.id.Notes_Edit);
		/*Clothing = (LinearLayout) fragmentLayout.findViewById(R.id.Clothing_layout);
		Entertainment = (LinearLayout) fragmentLayout.findViewById(R.id.Entertainment_layout);
		Food = (LinearLayout) fragmentLayout.findViewById(R.id.food_layout);
		Fuel = (LinearLayout) fragmentLayout.findViewById(R.id.fuel_layout);
		Health = (LinearLayout) fragmentLayout.findViewById(R.id.Health_layout);
		Salary = (LinearLayout) fragmentLayout.findViewById(R.id.Salary_layout);
		More = (LinearLayout) fragmentLayout.findViewById(R.id.More_layout);
		Clothing_checkbox = (CheckBox) fragmentLayout.findViewById(R.id.Clothing_checkbox);
		Entertainment_checkbox =
			(CheckBox) fragmentLayout.findViewById(R.id.Entertainment_checkbox);
		Food_checkbox = (CheckBox) fragmentLayout.findViewById(R.id.Food_checkbox);
		Fuel_checkbox = (CheckBox) fragmentLayout.findViewById(R.id.Fuel_checkbox);
		Health_checkbox = (CheckBox) fragmentLayout.findViewById(R.id.Health_checkbox);
		Salary_checkbox = (CheckBox) fragmentLayout.findViewById(R.id.Salary_checkbox);
		More_checkbox = (CheckBox) fragmentLayout.findViewById(R.id.More_checkbox);
		Category_text_view = (TextView) fragmentLayout.findViewById(R.id.Category_text_view);
		Submit_button = (Button) fragmentLayout.findViewById(R.id.Submit_Button);
		Clothing_checkbox.setOnCheckedChangeListener(checkedChangeListener);
		Entertainment_checkbox.setOnCheckedChangeListener(checkedChangeListener);
		Food_checkbox.setOnCheckedChangeListener(checkedChangeListener);
		Fuel_checkbox.setOnCheckedChangeListener(checkedChangeListener);
		Health_checkbox.setOnCheckedChangeListener(checkedChangeListener);
		Salary_checkbox.setOnCheckedChangeListener(checkedChangeListener);
		More_checkbox.setOnCheckedChangeListener(checkedChangeListener);*/

		dateEdit.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				if (TextUtils.isEmpty(AutoCompleteContacts.getText().toString())) {
					onclick_date();
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
						alertDialogBuilder.setView(PromptsView);

						Image_Link.setText(Name);
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
					}
				}
			}
		});

		Delete_Button.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				Delete_Button();
			}
		});




        /*Camera_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });*/
		More_TextButton.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				MoreButton();
			}
		});
		Submit_button.setOnClickListener(new View.OnClickListener() {
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

		AmountEdit.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				if (TextUtils.isEmpty(AutoCompleteContacts.getText().toString())) {

				} else {

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
									final SpannableStringBuilder sb = new SpannableStringBuilder();
									TextView tv = createContactTextView(name);
									BitmapDrawable bd = (BitmapDrawable) convertViewToDrawable(tv);
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

		AmountEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus == false) {
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

		Amount_Due_Edit.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				if (TextUtils.isEmpty(AutoCompleteContacts.getText().toString())) {

				} else {
					{
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
				}
			}

			@Override public void afterTextChanged(Editable s) {

			}
		});

		TitleEdit.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				if (TextUtils.isEmpty(AutoCompleteContacts.getText().toString())) {

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
					}
				}
			}
		});

		datePicker = new DatePickerDialog(getContext(), R.style.Theme_AppCompat_DayNight_Dialog,
			dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
			cal.get(Calendar.DAY_OF_MONTH));
		datePicker.setCancelable(true);
		datePicker.setTitle("Select date");
		fab = (ImageButton) fragmentLayout.findViewById(R.id.Fab_Camera_Button);
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
						Onclick_Image_button();
					}
				}
			}
		});

		if (!TextUtils.isEmpty(CardClicked)) {
			Delete_Button.setVisibility(View.VISIBLE);
			Material_Title.setHasFocus(true);
			Material_Amount.setHasFocus(true);
			//Material_Date.setHasFocus(true);

            /*
            Updated_Category = getIntent().getStringExtra("category");
            Updated_Amount = getIntent().getStringExtra("amount");
            Updated_Title = getIntent().getStringExtra("title");
            Updated_Date = getIntent().getStringExtra("DateCreated");
            Updated_Id = getIntent().getIntExtra("_ID", 0);*/
			/*if ("Clothing".equals(Updated_Category)) {
				Clothing.setBackgroundTintList(
					getContext().getResources().getColorStateList(R.color.newdarkblue));
				Category_text = "Clothing";
			} else if ("Entertainment".equals(Updated_Category)) {
				Entertainment.setBackgroundTintList(
					getContext().getResources().getColorStateList(R.color.newdarkblue));
				Category_text = "Entertainment";
			} else if ("Food".equals(Updated_Category)) {
				Food.setBackgroundTintList(
					getContext().getResources().getColorStateList(R.color.newdarkblue));
				Category_text = "Food";
			} else if ("Fuel".equals(Updated_Category)) {
				Fuel.setBackgroundTintList(
					getContext().getResources().getColorStateList(R.color.newdarkblue));
				Category_text = "Fuel";
			} else if ("Health".equals(Updated_Category)) {
				Health.setBackgroundTintList(
					getContext().getResources().getColorStateList(R.color.newdarkblue));
				Category_text = "Health";
			} else if ("Salary".equals(Updated_Category)) {
				Salary.setBackgroundTintList(
					getContext().getResources().getColorStateList(R.color.newdarkblue));
				Category_text = "Salary";
			}*/
			Updated_Amount = Updated_Amount.replace("Rs ", "");
			AmountEdit.setText(Updated_Amount);
			TitleEdit.setText(Updated_Title);
			dateEdit.setText(Updated_Date);
		}

		return fragmentLayout;
	}









	/*Thread th = new Thread() {
		@Override public void run() {

			ArrayAdapter<String> contactAdapter =
				new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line,
					Contact_Names);
			AutoCompleteContacts =
				(AutoCompleteTextView) fragmentLayout.findViewById(R.id.Notes_Edit);
			AutoCompleteContacts.setThreshold(1);
			AutoCompleteContacts.setAdapter(contactAdapter);
		}
	};*/

	/*private CheckBox.OnCheckedChangeListener checkedChangeListener =
		new CheckBox.OnCheckedChangeListener() {
			@SuppressLint("NewApi") @Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked == true) {
					if (buttonView.getParent().equals(Clothing)) {
						Category_text = "Clothing";
						Clothing.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newdarkblue));
						Entertainment.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
						Food.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
						Fuel.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
						Health.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
						Salary.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
						More.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
					}
					if (buttonView.getParent().equals(Entertainment)) {
						Category_text = "Entertainment";
						Entertainment.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newdarkblue));
						Clothing.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
						Food.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
						Fuel.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
						Health.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
						Salary.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
						More.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
					}
					if (buttonView.getParent().equals(Food)) {
						Category_text = "Food";
						Food.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newdarkblue));
						Entertainment.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
						Clothing.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
						Fuel.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
						Health.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
						Salary.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
						More.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
					}
					if (buttonView.getParent().equals(Fuel)) {
						Category_text = "Fuel";
						Fuel.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newdarkblue));
						Entertainment.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
						Food.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
						Clothing.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
						Health.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
						Salary.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
						More.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
					}
					if (buttonView.getParent().equals(Health)) {
						Category_text = "Health";
						Health.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newdarkblue));
						Entertainment.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
						Food.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
						Fuel.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
						Clothing.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
						Salary.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
						More.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
					}

                        /*if(buttonView.getParent().equals(Salary)){
                            Category_text="Salary";
                            Salary.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                            Entertainment.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Food.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Fuel.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Health.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Clothing.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            More.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                        }
                        if(buttonView.getParent().equals(More)){

                            More.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                            Entertainment.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Food.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Fuel.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Health.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            Salary.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                            More.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                        }
				} else {
					if (buttonView.getParent().equals(Clothing)) {
						Category_text = "";
						Clothing.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
                     /*  Entertainment.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
					}
					if (buttonView.getParent().equals(Entertainment)) {
						Category_text = "";
						Entertainment.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
                      /* Clothing.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
					}
					if (buttonView.getParent().equals(Food)) {
						Category_text = "";
						Food.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
                       /*Entertainment.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Clothing.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
					}
					if (buttonView.getParent().equals(Fuel)) {
						Category_text = "";
						Fuel.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
                  /*     Entertainment.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Clothing.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
					}
					if (buttonView.getParent().equals(Health)) {
						Category_text = "";
						Health.setBackgroundTintList(
							getContext().getResources().getColorStateList(R.color.newlightblue));
                       /*Entertainment.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Clothing.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
					}
                        /*if(buttonView.getParent().equals(Salary)){
                            Category_text="";
                            Salary.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                       /*Entertainment.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Clothing.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       More.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));*/
					//}
                        /*if(buttonView.getParent().equals(More)){
                            Category_text="";
                            More.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newlightblue));
                      /* Entertainment.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Food.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Fuel.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Health.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Salary.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
                       Clothing.setBackgroundTintList(getContext().getResources().getColorStateList(R.color.newdarkblue));
					//}
				}
			}
		};*/

	@Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//callbackManager.onActivityResult(requestCode, resultCode, data);

		if (requestCode == 2 && resultCode == RESULT_OK) {

			//Bitmap photo = (Bitmap) data.getExtras().get("data");
			Uri uri = data.getData();
			Img_File.setVisibility(View.VISIBLE);
			Img_File.setImageURI(uri);
			Image_uri = uri;
		} else if (requestCode == 1 && resultCode == RESULT_OK) {
			//Bitmap photo = (Bitmap) data.getExtras().get("data");
			Uri uri = data.getData();
			Img_File.setVisibility(View.VISIBLE);
			Image_uri = uri;
			Img_File.setVisibility(View.VISIBLE);
			Img_File.setImageURI(uri);
		} else if (requestCode == 3 && resultCode == RESULT_OK) {
			//Bitmap photo = (Bitmap) data.getExtras().get("data");
			// Uri uri = data.getData();
			Uri contactData = data.getData();
			Cursor c =
				getActivity().getContentResolver().query(contactData, null, null, null, null);
			if (c.moveToFirst()) {
				c.moveToFirst();

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
				cNumber = cNumber.replaceAll("[^0-9]", "");
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

	public void onclick_date() {
		datePicker.show();
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


  /*  @Override
    protected splitDialog onCreateDialog(int id){
        if(id==DIALOG_ID){
            return new DatePickerDialog(getContext(),dateSetListener,year_x,month_x,day_x);
        }
        return null;
    }
*/

	private DatePickerDialog.OnDateSetListener dateSetListener =
		new DatePickerDialog.OnDateSetListener() {
			@Override public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
				year_x = year;
				month_x = month + 1;
				day_x = dayOfMonth;
				Selected_date = dayOfMonth;
				year_x = cal.get(Calendar.YEAR);
				day_x = cal.get(Calendar.DAY_OF_MONTH);
				month_x = cal.get(Calendar.MONTH);
                     /*if(year==cal.get(Calendar.YEAR) && month==cal.get(Calendar.MONTH) && dayOfMonth==cal.get(Calendar.DAY_OF_MONTH) ){
                         dateEdit.setText("Today");
                     } else if(year==cal.get(Calendar.YEAR) && month==cal.get(Calendar.MONTH) && dayOfMonth==cal.get(Calendar.DAY_OF_MONTH)+1 ){
                         dateEdit.setText("Tomorrow");
                     }else if(year==cal.get(Calendar.YEAR) && month==cal.get(Calendar.MONTH) && dayOfMonth==cal.get(Calendar.DAY_OF_MONTH)-1 ){
                         dateEdit.setText("Yesterday");
                     }else{ dateEdit.setText(dayOfMonth+" "+Months[month]+" "+year);}*/
				dateEdit.setText(year_x + "-" + month_x + "-" + day_x);
			}
		};

	public void Submit() {
		Amount_text = AmountEdit.getText().toString();
		Notes_text = AutoCompleteContacts.getText().toString();
		Title_text = TitleEdit.getText().toString();
		View focus = null;
		Boolean cancel = false;
		if (TextUtils.isEmpty(Category_text)) {
			Snackbar.make(fragmentLayout, "category can't be empty", Snackbar.LENGTH_LONG)
				.setAction("Action", null)
				.show();
		} else {
			if (day_x == 0) {
				dateEdit.setError("date can't be empty");
				focus = dateEdit;
				cancel = true;
			}
			if (!TextUtils.isDigitsOnly(Amount_text)) {
				AmountEdit.setError("amount can't be empty");
				focus = AmountEdit;
				cancel = true;
			}
			if (TextUtils.isEmpty(Title_text)) {
				TitleEdit.setError("title can't be empty");
				focus = TitleEdit;
				cancel = true;
			}

			if (cancel == true) {
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
				if (TextUtils.isEmpty(CardClicked)) {
					String DateEdit_text = dateEdit.getText().toString();
					//Custom_items items = new Custom_items(Category_text,
					//      Title_text, "Rs " + Amount_text, day_x + " " + Months[month_x - 1] + " " + year_x);
					//customlist.add(items);

					SharedPreferences prefs =
						getContext().getSharedPreferences("login", MODE_PRIVATE);

					GroupModel model = new GroupModel();
					model.setGroupName(Title_text);
					//model.setGroupDesc();
					model.setUsers(prefs.getString("userid", null));
					long groupId = mDbHelper.createGroup(model);

					BillModel billModel = new BillModel();
					billModel.setTitle(Title_text);
					billModel.setGroupId(groupId);
					//billModel.setTotalAmount(Amount_text);
					billModel.setPaidAtDate(DateEdit_text);
					billModel.setCatId(Updated_Category);
					billModel.setType("income");

					//billModel.setAmountDue(Amount_text);

					mDbHelper.createEntry(billModel);

					//long rowId = mDbHelper.createEntry(expenseModel);
					//expenseModel.setId(rowId);
					//if (NetworkUtil.hasInternetConnection(getContext())) {
					//	new ServerUtil(getContext()).createEntry(expenseModel);
					//} else {
					//	mDbHelper.addToTemp(rowId, 0, "new");
					//}

					//TransactionDbHelper mdbhelper = new TransactionDbHelper(getContext());
					//SQLiteDatabase db = mdbhelper.getWritableDatabase();
					//ContentValues values = new ContentValues();
					////values.put(recipe_entry.Column_Recipe_Image,byteimage);
					//values.put(TransactionDbContract.Transaction_Entry.COLUMN_TITLE, Title_text);
					//values.put(TransactionDbContract.Transaction_Entry.COLUMN_CATEGORY,
					//	Category_text);
					//values.put(TransactionDbContract.Transaction_Entry.COLUMN_TOTAL_AMOUNT,
					//	"Rs " + Amount_text);
					//values.put(TransactionDbContract.Transaction_Entry.COLUMN_PAY_DATE,
					//	DateEdit_text);
					//values.put(recipe_entry.Column_Recipe_Nutri_label,Nlabel);
					//values.put(recipe_entry.Column_Recipe_Nutri_Quantity,Nquantity);
					//long newRowId =
					//	db.insert(TransactionDbContract.Transaction_Entry.TABLE_NAME, null, values);
					//if (newRowId == -1) {
					//	// If the row COLUMN_ONLINE_ID is -1, then there was an error with insertion.
					//	Toast.makeText(getContext(), "Error with saving pet", Toast.LENGTH_SHORT)
					//		.show();
					//} else {
					//	// Otherwise, the insertion was successful and we can display a toast with the row COLUMN_ONLINE_ID.
					//	Toast.makeText(getContext(), "Recipe saved with row id: " + newRowId,
					//		Toast.LENGTH_SHORT).show();
					//	new ServerUtil(getContext()).createEntry(expenseModel);
					//}
					Bundle args = new Bundle();
					//args.putParcelableArrayList("ARRAYLIST",(ArrayList<? extends Parcelable>) customlist);
					Toast.makeText(getContext(), "Success", Toast.LENGTH_LONG).show();
				} else {
					Updated_Title = TitleEdit.getText().toString();
					Updated_Category = Category_text;
					Updated_Amount = AmountEdit.getText().toString();
					Updated_Date = dateEdit.getText().toString();

					model.setPaidAtDate(Updated_Date);
					model.setTotalAmount(Updated_Amount);
					model.setTitle(Updated_Title);
					model.setCatId(Updated_Category);

					mDbHelper.updateEntry(pos, model);
					if (NetworkUtil.hasInternetConnection(getContext())) {
						new ServerUtil(getContext()).updateEntry(model);
					} else {
						mDbHelper.addToTemp(model.getId(), 0, "edit");
					}

					//new ServerUtil(getContext()).updateEntry(pos, expenseModel);
					//
					//
					//TransactionDbHelper mdbhelper = new TransactionDbHelper(getContext());
					//SQLiteDatabase db = mdbhelper.getWritableDatabase();
					//ContentValues values = new ContentValues();
					////values.put(recipe_entry.Column_Recipe_Image,byteimage);
					//values.put(TransactionDbContract.Transaction_Entry.COLUMN_TITLE, Updated_Title);
					//values.put(TransactionDbContract.Transaction_Entry.COLUMN_CATEGORY,
					//	Updated_Category);
					//values.put(TransactionDbContract.Transaction_Entry.COLUMN_TOTAL_AMOUNT,
					//	"Rs " + Updated_Amount);
					//values.put(TransactionDbContract.Transaction_Entry.COLUMN_PAY_DATE,
					//	Updated_Date);
					//db.update(TransactionDbContract.Transaction_Entry.TABLE_NAME, values,
					//	TransactionDbContract.Transaction_Entry.COLUMN_ONLINE_ID + "=" + Updated_Id, null);
					//new ServerUtil(getContext()).updateEntry(expenseModel);
				}
				getActivity().finish();
			}
		}
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

	public void MoreButton() {
		Fetch_Contacts();
		//th.run();
		/*if (Notes_Layout.getVisibility() == View.GONE) {
			Notes_Layout.setVisibility(View.VISIBLE);
			Material_Amount_Due.setVisibility(View.VISIBLE);
			if (!TextUtils.isEmpty(AmountEdit.getText().toString())) {
				Amount_Due_Edit.setText(AmountEdit.getText().toString());
			} else {
				Amount_Due_Edit.setText("");
			}

            /*YoYo.with(Techniques.SlideInLeft)
                    .duration(1000)
                    .repeat(0)
                    .playOn(Notes_Layout);
			Material_Notes.setHasFocus(true);
			Material_Amount_Due.setHasFocus(true);
			// More_Button.setImageDrawable(getResources().getDrawable(R.drawable.uparrow));

		} else {
			Material_Notes.setHasFocus(false);
			Material_Amount_Due.setHasFocus(false);
			Notes_Layout.setVisibility(View.GONE);
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
            }, 500);

		}*/
	}

	public void Contact_Button() {
		Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

		startActivityForResult(intent, 3);
	}

	@Override public void onRequestPermissionsResult(int requestCode, String permissions[],
		int[] grantResults) {
		switch (requestCode) {
			case 100: {

				// If request is cancelled, the result arrays are empty.
				if (grantResults.length > 0
					&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {

					// permission was granted, yay! Do the
					// contacts-related task you need to do.
				} else {

					// permission denied, boo! Disable the
					// functionality that depends on this permission.
					Toast.makeText(getContext(), "Permission denied to read your External storage",
						Toast.LENGTH_SHORT).show();
				}
				return;
			}

			// other 'case' lines to check for other
			// permissions this app might request
		}
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
		for (int i = 0; i < 200; i++) {
			Contact_Names[i] = Contact_list.get(i);
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
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
}
