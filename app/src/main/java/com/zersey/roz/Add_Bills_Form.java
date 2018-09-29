package com.zersey.roz;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.materialtextfield.MaterialTextField;
import com.zersey.roz.Data.Contacts_contract;
import com.zersey.roz.Data.Contactsdbhelper;
import com.zersey.roz.Data.TransactionDbHelper;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class Add_Bills_Form extends android.support.v4.app.DialogFragment {
    private List<IncomeModel> Item_list;
    private MaterialTextField Material_Title, Material_Amount, Material_Amount_Due;
    private int year_x, month_x, day_x, Selected_date = 0;
    private List<ContactModel> Contact_list;
    private RecyclerView Contact_RecyclerView;
    private Contact_RecyclerView_Adapter RecyclerView_Adapter;
    private DatePickerDialog datePicker;
    private RecyclerView Category_Recycler_View;
    private List<String> Category_list;
    private TextView Submit_button;
    private TextView More_TextButton;
    private ImageButton Delete_Button, Camera_Button;
    private ImageView Img_File;
    private TextView Category_text_view, dateEdit;
    public static Button positive_Button;
    private TextView First_More, Second_More,Split_Button;
    private Calendar cal;
    private EditText AmountEdit, TitleEdit, Amount_Due_Edit;
    private LinearLayout Clothing, Entertainment, Food, Fuel, Health, Salary, More, Notes_Layout,
            Add_Member_Layout;
    private String users = "";
    private RecyclerView Split_RecyclerView;
    private Dialog_Split_RecyclerViewAdapter Adapter;
    private long Updated_Id;
    private String Category_text, Add_Person_text, Amount_text, Title_text;
    private boolean person_added = false;
    private String Updated_Type = "";
    private GroupModel groupModel;
    private String payerId;
    private String cNumber, code;
    private IncomeModel model;
    private TransactionDbHelper mDbHelper;
    private String CardClicked, Updated_Category, Updated_Title, Updated_Amount, Updated_Date;
    private int pos=-1;
    private String Contact_Person_Name, Contact_Person_Number;

    private List<Split_Contact_model> Split_List;
    private Spinner Split_Spinner;
    public static TextView Split_Notes;
    private EditText Amount_Edit, Description_Edit, Group_Name_Edit;
    public static RecyclerAdapter adapter;
    public static Task_Slider_Adapter task_slider_adapter;
    private Boolean check = true;
    private String Amount;
    private CategoryAdapter Catadapter;
    //private pageradapter adapter;
    private Uri Image_uri = null;
private Context context;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //initList();
        mDbHelper = TransactionDbHelper.getInstance(getContext());
        Item_list = new ArrayList<>(mDbHelper.getAllEntries());

        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        context=inflater.getContext();
        //LayoutInflater LI = LayoutInflater.from();
        View PromptsView = inflater.inflate(R.layout.content_main, container,false);
        Contact_list = new ArrayList<>();
        Category_list=new ArrayList<>();
        Category_list.add("Expense");
        Category_list.add("Income");
        Category_list.add("Group");

        Category_Recycler_View = PromptsView.findViewById(R.id.Category_Recycler_View);
        //Category_Recycler_View.setVisibility(View.VISIBLE);
        PromptsView=initView(PromptsView);
        //customlist=(ArrayList<Custom_items>)getIntent().getBundleExtra("Bundle").getSerializable("ARRAYLIST");
        Material_Title = PromptsView.findViewById(R.id.Material_Title);
        Material_Title.setHasFocus(true);
        Material_Amount = PromptsView.findViewById(R.id.Material_Amount);
        Material_Amount.setHasFocus(true);
        Material_Amount_Due = PromptsView.findViewById(R.id.Material_Amount_Due);
        Material_Amount_Due.setVisibility(View.GONE);
        More_TextButton = PromptsView.findViewById(R.id.MoreButton);
        Split_Button=PromptsView.findViewById(R.id.Split_Dialog);
        Split_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog(Split_Button);
            }
        });
        cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        month_x = cal.get(Calendar.MONTH);
        More_TextButton = PromptsView.findViewById(R.id.MoreButton);
        Delete_Button = PromptsView.findViewById(R.id.Delete_Button);
        Delete_Button.setVisibility(View.GONE);
        //Contact_Button = (ImageButton) findViewById(R.id.Contact_Button);
        //Camera_Button=(ImageButton)findViewById(R.id.Camera_Button);
        //requestPermissions(Manifest.permission.CAMERA,1111);
        Img_File = PromptsView.findViewById(R.id.Img_file);
        Img_File.setVisibility(View.GONE);
        dateEdit = PromptsView.findViewById(R.id.Date_Edit);
        dateEdit.setText(year_x + "-" + (month_x + 1) + "-" + day_x);
        TitleEdit = PromptsView.findViewById(R.id.Title_Edit);
        AmountEdit = PromptsView.findViewById(R.id.Amount_Edit);
        Amount_Due_Edit = PromptsView.findViewById(R.id.Amount_Due_Edit);
        Add_Member_Layout = PromptsView.findViewById(R.id.Add_Member_Layout);
        Split_RecyclerView = new RecyclerView(context);
        SharedPreferences prefs = context.getSharedPreferences("login", MODE_PRIVATE);
        users += prefs.getString("userid", null);
        //Add_Member_Layout=(LinearLayout)findViewById(R.id.Add_Member_Layout);
        //Add_Member_ListView=(ListView)findViewById(R.id.Add_Member_ListView);

        Submit_button = PromptsView.findViewById(R.id.Submit_Transaction_form);
        dateEdit.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                datePicker.show();
            }
        });

        Add_Member_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contact_Button(v);
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
                Onclick_Image_button();
            }
        });*/
        More_TextButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                //MoreButton();
            }
        });


        DatePickerDialog.OnDateSetListener dateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    @Override public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        year_x = year;
                        month_x = month + 1;
                        day_x = dayOfMonth;
                        Selected_date = dayOfMonth;
                        dateEdit.setText(year_x + "-" + month_x + "-" + day_x);
                    }
                };
        datePicker =
                new DatePickerDialog(context, R.style.Theme_AppCompat_DayNight_Dialog,
                        dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));
        datePicker.setCancelable(true);
        datePicker.setTitle("Select Date");
        Camera_Button = PromptsView.findViewById(R.id.Fab_Camera_Button);
        Camera_Button.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Onclick_Image_button();
            }
        });

        Submit_button.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {

                  Submit();
                dismiss();
            }
            });

       /* //android.support.v7.app.AlertDialog.Builder alertDialogBuilder =                new android.support.v7.app.AlertDialog.Builder(context);
        //alertDialogBuilder.setView(PromptsView);
        //alertDialogBuilder.setCancelable(true);
        //final android.support.v7.app.AlertDialog dialog = alertDialogBuilder.create();
        //dialog.setOnShowListener(new DialogInterface.OnShowListener() {
          //  @Override
            //public void onShow(final DialogInterface dialog) {
              //
            //}
        //});

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.TransparentWhite)));
        //dialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
        dialog.show();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));




        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;

        dialog.getWindow()
                .clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

*/     getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.TransparentWhite)));
        //dialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
        getDialog().show();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
  /*      LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));




        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
*/
        getDialog().getWindow()
                .clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        return PromptsView;
    }



    public void Onclick_Image_button() {
        final EditText Image_Link;

        LayoutInflater LI = LayoutInflater.from(context);
        //View PromptsView=LI.inflate(R.layout.image_dialog,null);

        //Image_Link=(EditText)PromptsView.findViewById(R.id.);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

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

    public void Add_notes(View view) {
        final EditText Notes_Edit;
        LayoutInflater inflater = LayoutInflater.from(context);
        View notes_view = inflater.inflate(R.layout.notes_dialogue_layout, null);
        Notes_Edit = notes_view.findViewById(R.id.Notes_EditText);
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder =
                new android.support.v7.app.AlertDialog.Builder(context);
        alertDialogBuilder.setView(notes_view);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(Html.fromHtml("<font color='#00796B'>Ok</font>"),
                null);
        //alertDialogBuilder.setNegativeButton("Cancel",null);
        alertDialogBuilder.setNeutralButton("Cancel", null);

        final android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override public void onShow(DialogInterface dialogInterface) {

                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button Negative = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                Negative.setTextColor(getResources().getColor(R.color.colorPrimary));
                button.setTextColor(getResources().getColor(R.color.colorPrimary));
                Negative.setOnClickListener(new View.OnClickListener() {
                    @Override public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                button.setOnClickListener(new View.OnClickListener() {

                    @Override public void onClick(View view) {
                        // TODO Do something

                        //Dismiss once everything is OK.
                        if (TextUtils.isEmpty(Notes_Edit.getText().toString())) {
                            Toast.makeText(context, "Please write something",
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

    public void Delete_Button() {
        if (NetworkUtil.hasInternetConnection(context)) {
            new ServerUtil(context).deleteEntry(model.getOnlineId());
        } else {
            mDbHelper.addToTemp(model.getId(), model.getOnlineId(), "delete");
        }

        mDbHelper.deleteEntry(pos, model.getId());

        //finish();
    }

    public void Dialog(View view) {
        Amount = AmountEdit.getText().toString();
        if (TextUtils.isEmpty(Amount)) {
            Amount = "0";
        }
        if (!check) {
            groupSplit();
        } else if (check) {
            //String[] names = Item_list.get;

            singleSplit();
        }
        if (TextUtils.isEmpty(Amount)) {
            Amount = "0";
        }
        Log.d("Dialog: ", Split_List.size() + "");
        LayoutInflater LI = LayoutInflater.from(context);
        View PromptsView = LI.inflate(R.layout.split_dialog_layout, null);
        Split_Spinner = PromptsView.findViewById(R.id.Split_Spinner);
        Split_RecyclerView = PromptsView.findViewById(R.id.Dialog_RecyclerView);
        Split_Notes = PromptsView.findViewById(R.id.Dialog_Split_Notes);
        Split_RecyclerView.setLayoutManager(new LinearLayoutManager(context));
        Adapter = new Dialog_Split_RecyclerViewAdapter(Split_List, "", Integer.parseInt(Amount));
        Split_RecyclerView.setAdapter(Adapter);

        Split_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 2) {
                    Adapter = new Dialog_Split_RecyclerViewAdapter(Split_List, "ratio",
                            Integer.parseInt(Amount));
                    Split_RecyclerView.setAdapter(Adapter);
                }
                if (position == 1) {
                    Adapter = new Dialog_Split_RecyclerViewAdapter(Split_List, "",
                            Integer.parseInt(Amount));
                    Split_RecyclerView.setAdapter(Adapter);
                }
                if (position == 0) {
                    Adapter = new Dialog_Split_RecyclerViewAdapter(Split_List, "Equally",
                            Integer.parseInt(Amount));
                    Split_RecyclerView.setAdapter(Adapter);
                }
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder =
                new android.support.v7.app.AlertDialog.Builder(context);
        alertDialogBuilder.setView(PromptsView);
        alertDialogBuilder.setCancelable(false);

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
                Split_List = Adapter.getList();
            }
        });
        final android.support.v7.app.AlertDialog dialog = alertDialogBuilder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override public void onShow(DialogInterface dialogInterface) {
                positive_Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                //Button Negative = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                //Negative.setTextColor(getColor(R.color.colorPrimary));
                positive_Button.setTextColor(getResources().getColor(R.color.colorPrimary));
				/*Negative.setOnClickListener(new View.OnClickListener() {
					@Override public void onClick(View v) {
						alertDialog.dismiss();
					}
				});*/
				/*positive_Button.setOnClickListener(new View.OnClickListener() {

					@Override public void onClick(View view) {
						// TODO Do something

						//Dismiss once everything is OK.
						if (Adapter.getSplit_Correctly()) {
							Toast.makeText(MainActivity.this, "Please Split Correctly",
									Toast.LENGTH_LONG).show();
						} else {
							dialog.dismiss();
						}
					}
				});*/
            }
        });




        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.TransparentWhite)));
        //dialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
        getDialog().show();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
  /*      LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));




        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
*/
        getDialog().getWindow()
                .clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
    }

    private void singleSplit() {
        int no_of_Person = Contact_list.size();
        String Specific_Amount;
        Amount = Material_Amount.getEditText().getText().toString();
        if (TextUtils.isEmpty(Amount)) {
            Specific_Amount = "0";
        } else {
            Specific_Amount = "" + Integer.parseInt(Amount) / (Contact_list.size() + 1);
        }
        Log.d("Dialog: ", Contact_list.size() + "");
        Split_List = new ArrayList<>();
        ContactModel you = new ContactModel();
        you.setName("You");
        Split_List.add(new Split_Contact_model(you, Specific_Amount));
        if (Contact_list.isEmpty()) {
            Toast.makeText(context, "Add members ", Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i < Contact_list.size(); i++) {

            if ((i == Contact_list.size() - 1) && (no_of_Person + 1) % 2 != 0) {
                Specific_Amount = Integer.parseInt(Specific_Amount) + 1 + "";
            }
            Split_List.add(new Split_Contact_model(Contact_list.get(i), Specific_Amount));
        }
    }

    private void groupSplit() {
        String[] names = groupModel.getUsers().split(",");
        List<ContactModel> userList = mDbHelper.getUserWithUserId(names);
        String Specific_Amount;
        Amount = Material_Amount.getEditText().getText().toString();
        if (TextUtils.isEmpty(Amount)) {
            Specific_Amount = "0";
        } else {
            Specific_Amount = "" + Integer.parseInt(Amount) / (userList.size() + 1);
        }
        Log.d("Dialog: ", Contact_list.size() + "");
        Split_List = new ArrayList<>();

        ContactModel you = new ContactModel();
        you.setName("You");
        Split_List.add(new Split_Contact_model(you, Specific_Amount));

        for (int i = 0; i < userList.size(); i++) {
            Split_List.add(new Split_Contact_model(userList.get(i), Specific_Amount));
        }
    }


    private View initView(View PromptView) {
        Contact_RecyclerView = new RecyclerView(context);
        Contact_RecyclerView = PromptView.findViewById(R.id.Add_Member_RecyclerView);
        Contact_RecyclerView.setLayoutManager(
                new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,
                        false));
        if (TextUtils.isEmpty(Updated_Type)) {
            Catadapter = new CategoryAdapter(context, Category_list, "");
        } else {
            Catadapter = new CategoryAdapter(context, Category_list, Updated_Type);
        }
        Category_Recycler_View.setLayoutManager(
                new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,
                        false));
        Category_Recycler_View.setAdapter(Catadapter);
        Category_Recycler_View.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
            }
        });
        return PromptView;
    }

    public void Contact_Button(View view) {
		/*Intent intent =
			new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
		startActivityForResult(intent, 3);*/
        Intent intent = new Intent(context, Add_Members_Activity.class);
        startActivityForResult(intent, 4);
    }

    public void Submit() {
        Amount_text = AmountEdit.getText().toString();
        //Add_Person_text = AutoCompleteContacts.getText().toString();
        Title_text = TitleEdit.getText().toString();
        Category_text = Catadapter.getLastCategory();
        View focus = null;
        Boolean cancel = false;
        if (groupModel == null && TextUtils.isEmpty(Category_text)) {
			/*Snackbar.make(findViewById(R.id.myCoordinaterLayout), "Category can't be empty",
					Snackbar.LENGTH_LONG).setAction("Action", null).show();*/
            Toast.makeText(context,"Category can't be empty\"",Toast.LENGTH_LONG).show();
        } else {
            if (day_x == 0) {
                dateEdit.setError("Date can't be empty");
                focus = dateEdit;
                cancel = true;
            }
            if (Util.isEmpty(Amount_text)) {
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
                        Toast.makeText(context,
                                "Contact is already present in the database", Toast.LENGTH_LONG).show();
                    } else {
                        Contactsdbhelper mdbhelper = new Contactsdbhelper(context);
                        SQLiteDatabase db = mdbhelper.getWritableDatabase();
                        ContentValues values = new ContentValues();

                        values.put(Contacts_contract.Contacts_Entry.Column_Contact_Name,
                                Contact_Person_Name);
                        values.put(Contacts_contract.Contacts_Entry.Column_Contact_Number,
                                Contact_Person_Number);
                        //values.put(Transaction_contract.Transaction_Entry.Column_Amount, "Rs " + Amount_text);
                        //Log.d("Date created", DateEdit_text);
                        //values.put(Transaction_contract.Transaction_Entry.Column_Date_Created, DateEdit_text);

                        long newRowId =
                                db.insert(Contacts_contract.Contacts_Entry.Table_name, null, values);
                        if (newRowId == -1) {
                            // If the row COLUMN_ONLINE_ID is -1, then there was an error with insertion.
                            Toast.makeText(context, "Error with saving Contact",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Otherwise, the insertion was successful and we can display a toast with the row COLUMN_ONLINE_ID.
                            Toast.makeText(context,
                                    "Contact saved with row id: " + newRowId, Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                }

                IncomeModel expenseModel = new IncomeModel();

                if (TextUtils.isEmpty(CardClicked)) {
                    String DateEdit_text = dateEdit.getText().toString();
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

                    for (Split_Contact_model s : Split_List) {
                        totalAmounts.append(s.getSplit_Amount()).append(",");
                        if (s.getContact_Name().getName().equalsIgnoreCase("you")) {
                            payerIds.append(prefs.getString("userid", null)).append(",");
                        } else {
                            payerIds.append(s.getContact_Name().getUserId()).append(",");
                        }
                        amountsPaid.append("0.00").append(",");
                        datesPaid.append(DateEdit_text).append(",");
                    }

                    expenseModel.setType(Category_text);
                    expenseModel.setTitle(Title_text);
                    expenseModel.setTotalAmount(totalAmounts.toString());
                    expenseModel.setPaidAtDate(datesPaid.toString());
                    expenseModel.setAmountPaid(amountsPaid.toString());
                    expenseModel.setAmountDue(totalAmounts.toString());
                    expenseModel.setPayerId(payerIds.toString());
                    expenseModel.setInvoiceId("");
                    try {
                        expenseModel.setUuid(Util.generateUuid(prefs.getString("userid", null)));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }

                    if (groupModel != null) {
                        expenseModel.setGroupId(groupModel.getGroupId());
                        String str=groupModel.getGroupId()+"";
                        Log.d( "Submit: ",str);
                        expenseModel.setType("gt");
                        long rowId = mDbHelper.createEntry(expenseModel);
                        new ServerUtil(context).createEntry(expenseModel);
                        expenseModel.setId(rowId);
                    }

                    if (groupModel == null) {
                        GroupModel groupModel2 = new GroupModel();
                        groupModel2.setGroupName(Title_text);
                        groupModel2.setTypeId(1);
                        //groupModel.setGroupDesc();
                        //String str=groupModel.getGroupId()+"";

                        groupModel2.setUsers(users);
                        long newrowId = mDbHelper.createGroup(groupModel2);
                        Log.d( "Submit: ",""+expenseModel
                        );
                        groupModel2.setId(newrowId);
                        new ServerUtil(context).createSingleGroup(groupModel2,
                                expenseModel, null);
                    }

                    Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();
                } else {
                    Updated_Title = TitleEdit.getText().toString();
                    Updated_Category = Category_text;
                    Updated_Amount = AmountEdit.getText().toString();
                    Updated_Date = dateEdit.getText().toString();

                    //model.setPaidAtDate(Updated_Date);
                    //model.setTotalAmount(Updated_Amount);
                    model.setTitle(Updated_Title);
                    model.setCatId(Updated_Category);
                    model.setGroupId(Updated_Id);

                    mDbHelper.updateEntry(pos, model);
                    if (NetworkUtil.hasInternetConnection(context)) {
                        new ServerUtil(context).updateEntry(model);
                    } else {
                        mDbHelper.addToTemp(model.getId(), 0, "edit");
                    }

                    //new ServerUtil(MainActivity.this).updateEntry(pos, expenseModel);
                    //
                    //
                    //TransactionDbHelper mdbhelper = new TransactionDbHelper(MainActivity.this);
                    //SQLiteDatabase db = mdbhelper.getWritableDatabase();
                    //ContentValues values = new ContentValues();
                    ////values.put(recipe_entry.Column_Recipe_Image,byteimage);
                    //values.put(TransactionDbContract.Transaction_Entry.COLUMN_TITLE, Updated_Title);
                    //values.put(TransactionDbContract.Transaction_Entry.COLUMN_CATEGORY,
                    //	Updated_Category);
                    //values.put(TransactionDbContract.Transaction_Entry.COLUMN_AMOUNT,
                    //	"Rs " + Updated_Amount);
                    //values.put(TransactionDbContract.Transaction_Entry.COLUMN_DATE_CREATED,
                    //	Updated_Date);
                    //db.update(TransactionDbContract.Transaction_Entry.TABLE_NAME, values,
                    //	TransactionDbContract.Transaction_Entry.COLUMN_ONLINE_ID + "=" + Updated_Id, null);
                    //new ServerUtil(MainActivity.this).updateEntry(expenseModel);
                }

                if (groupModel != null) {
                    Intent intent = new Intent();
                    intent.putExtra("model", expenseModel);
                    //setResult(Activity.RESULT_OK, intent);
                }

                //finish();
            }
        }
    }

    public Boolean Check_Contacts(String contactname, String contactnumber) {
        ArrayList<Custom_Contact_items> Item_list = new ArrayList<>();
        Contactsdbhelper mDbHelper = new Contactsdbhelper(context);
        SQLiteDatabase db1 = mDbHelper.getReadableDatabase();
        //final  ArrayList<String> labellist=new ArrayList<>();
        // Define a projection that specifies which columns from the database
        // you will actually use after MainActivity.this query.
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
                //Log.d( "current label ",currentName);
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

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d( "onActivityResult: ",resultCode+""+requestCode);
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
            //List<String> temp=getIntent().getStringArrayListExtra("Previous_Contacts");
            Uri contactData = data.getData();
            Cursor c = context.getContentResolver().query(contactData, null, null, null, null);
            if (c.moveToFirst()) {
                String cNumber = null;
                final String name =
                        c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                String hasPhone =
                        c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                if (hasPhone.equalsIgnoreCase("1")) {

                    cNumber = c.getString(
                            c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    cNumber = cNumber.replaceAll("[^0-9]", "");
                    if (cNumber.length() > 10) {
                        code = cNumber.substring(0, cNumber.length() - 10);
                        cNumber = cNumber.substring(cNumber.length() - 10);
                    } else {
                        code = "91";
                    }
                }
                ContactModel CCItem = new ContactModel();
                CCItem.setNumber(cNumber);
                CCItem.setName(name);

                if (!Check_Contact_List(cNumber)) {
                    Contact_list.add(CCItem);
                    Log.d("onActivityResult: ", Contact_list.size() + "");
                    Contact_RecyclerView.setVisibility(View.VISIBLE);
                    RecyclerView_Adapter = new Contact_RecyclerView_Adapter(Contact_list);
                    Contact_RecyclerView.setAdapter(RecyclerView_Adapter);
                    //					Log.d("onActivityResult: ", cNumber);
                } else {
                    Toast.makeText(context, "Contact is already added", Toast.LENGTH_LONG)
                            .show();
                }
            }
        }else if(resultCode == RESULT_OK && requestCode==4) {
            Contact_RecyclerView.setVisibility(View.VISIBLE);
            Contact_list.addAll((List<ContactModel>) data.getSerializableExtra("ADDED"));
            Log.d("onActivityResult: ", Item_list.size() + "");
            RecyclerView_Adapter = new Contact_RecyclerView_Adapter(Contact_list);
            Contact_RecyclerView.setAdapter(RecyclerView_Adapter);

        }

    }
    public boolean Check_Contact_List(String Number) {
        Boolean check = false;
        for (int i = 0; i < Contact_list.size(); i++) {
            //return true;
            check = TextUtils.equals(Contact_list.get(i).getNumber(), Number);
        }
        return check;
    }
}
