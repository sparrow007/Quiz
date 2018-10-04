package com.zersey.roz;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zersey.roz.Data.TransactionDbHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class Add_Task_form extends DialogFragment {

    private List<com.zersey.roz.TaskModel> list;
    private Context context;
    private com.zersey.roz.TaskModel TaskModel;
    int ViewType = 0;
    private int year_x, month_x, day_x;
    private Calendar cal;
    //Button positive_Button;
    TransactionDbHelper mDbHelper;
    private List<GroupModel> Grouplist;
    private DatePickerDialog datePicker;


    private EditText Task_Title, Task_Des;
    private TextView Task_Date;
    private ImageView Img_File;
    private ImageButton Task_Image_Upload;
    private Uri Image_uri;
    private GroupModel model;
    private Boolean Updated = false;
    private Spinner Task_Spinner;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        cal = Calendar.getInstance();
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = inflater.getContext();
        Grouplist = new ArrayList<>();
        mDbHelper = TransactionDbHelper.getInstance(context);
        Grouplist.addAll(mDbHelper.getGroups(0));
        String[] stringlist = new String[Grouplist.size()];
        for (int i = 0; i < Grouplist.size(); i++) {
            stringlist[i] = Grouplist.get(i).getGroupName();
        }

        final EditText Task_Title, Task_Des;
        final TextView Task_Date;
        final Spinner Task_Spinner;
        final Button Submit;
        //LayoutInflater LI = LayoutInflater.from(context);
        final View PromptsView = inflater.inflate(R.layout.content_task__form_, container,false);
        Task_Title = PromptsView.findViewById(R.id.Task_Title_Edit);
        Task_Des = PromptsView.findViewById(R.id.Task_Description_Edit);
        Task_Date = PromptsView.findViewById(R.id.Task_Date_Edit);
        Task_Spinner = PromptsView.findViewById(R.id.Task_Spinner);
        Img_File=PromptsView.findViewById(R.id.Task_Image_view);
        Task_Image_Upload=PromptsView.findViewById(R.id.Fab_Camera_Button);
        Submit=PromptsView.findViewById(R.id.Submit_Task_Button);

        year_x = cal.get(Calendar.YEAR);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        month_x = cal.get(Calendar.MONTH);
        Task_Date.setText(year_x + "-" + (month_x + 1) + "-" + day_x);
        DatePickerDialog.OnDateSetListener dateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    @Override public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        year_x = year;
                        month_x = month + 1;
                        day_x = dayOfMonth;
                        Task_Date.setText(year_x + "-" + month_x + "-" + day_x);
                    }
                };
        datePicker =
                new DatePickerDialog(context, R.style.Theme_AppCompat_DayNight_Dialog,

                        dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),

                        cal.get(Calendar.DAY_OF_MONTH));
        datePicker.setCancelable(true);
        datePicker.setTitle("Select Date");

        ArrayAdapter<String> adapter0 =
                new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, stringlist);
        adapter0.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        Task_Spinner.setAdapter(adapter0);
        Task_Spinner.setSelection(0);
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder =
                new android.support.v7.app.AlertDialog.Builder(context);
        alertDialogBuilder.setView(PromptsView);
        alertDialogBuilder.setCancelable(true);

		/*alertDialogBuilder.setPositiveButton("submitBill", new DialogInterface.OnClickListener() {
			@Override public void onClick(DialogInterface dialog, int which) {
				//Split_List = Adapter.getList();
				Toast.makeText(context,"submitBill Clicked",Toast.LENGTH_LONG).show();
			}
		});*/

       // final android.support.v7.app.AlertDialog dialog = alertDialogBuilder.create();


        Task_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show();
            }
        });

        Task_Image_Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Onclick_Image_button();
            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Title = Task_Title.getText().toString();
                String Des = Task_Des.getText().toString();
                String Date = Task_Date.getText().toString();
                View Focus = null;
                Boolean cancel = false;
                if (TextUtils.isEmpty(Title)) {
                    Task_Title.setError("Error");
                    Focus = Task_Title;
                    cancel = true;
                } else if (TextUtils.isEmpty(Des)) {
                    Task_Des.setError("Error");
                    Focus = Task_Des;
                    cancel = true;
                } else if (TextUtils.isEmpty(Date)) {
                    Task_Date.setError("Error");
                    Focus = Task_Date;
                    cancel = true;
                }
                if (cancel) {
                    Focus.requestFocus();
                }
                {
                    Intent intent = new Intent();
                    if (TaskModel == null) {
                        TaskModel = new TaskModel();
                        TaskModel.setTask_Title(Title);
                        TaskModel.setTask_Des(Des);
                        TaskModel.setTask_Date(Date);
                        TaskModel.setTask_Checked(false);
                        TaskModel.setGroup_Id(Grouplist.get(Task_Spinner.getSelectedItemPosition()).getGroupId());
                        Log.d( "onClick: ",Grouplist.get(Task_Spinner.getSelectedItemPosition()).getGroupId()+"");
                    }
                    long id = mDbHelper.addGroupNotes(TaskModel);
                    if (id < 0) {
                        Toast.makeText(context, "Task was not saved with " + id,
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, "Task saved with " + id, Toast.LENGTH_LONG)
                                .show();
                        new ServerUtil(context).createGroupTask(TaskModel);
                        intent.putExtra("task", TaskModel);
                        //setResult(Activity.RESULT_OK, intent);
                        GroupsFragment.tasksAdapter.add(TaskModel);
                        //finish();
                        dismiss();
                    }
                }
                dismiss();
            }
        });


        /*dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override public void onShow(DialogInterface dialogInterface) {


                //positive_Button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                //Button Negative = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
                //Negative.setTextColor(getColor(R.color.colorPrimary));
				/*positive_Button.setBackgroundColor(context.getColor(R.color.colorPrimary));
				positive_Button.setTextColor(context.getColor(R.color.White));
				positive_Button.setWidth(PromptsView.getLayoutParams().width);
				positive_Button.setForegroundGravity(Gravity.CENTER_HORIZONTAL);
				positive_Button.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);

				*/
				/*Negative.setOnClickListener(new View.OnClickListener() {
					@Override public void onClick(View v) {
						alertDialog.dismiss();
					}
				});*/
				/*positive_Button.setOnClickListener(new View.OnClickListener() {

					@Override public void onClick(View view) {
						// TODO Do something

						//Dismiss once everything is OK.

					}
            }
        });*/
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



        return PromptsView;
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult: ", requestCode + "");
        if (requestCode == 2 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Img_File.setVisibility(View.VISIBLE);
            Img_File.setImageURI(uri);
            Image_uri = uri;
        } else if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Img_File.setVisibility(View.VISIBLE);
            Image_uri = uri;
            Img_File.setVisibility(View.VISIBLE);
            Img_File.setImageURI(uri);
        }
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

}
