package com.zersey.roz;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zersey.roz.Data.TransactionDbHelper;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.zersey.roz.Group_About.RecyclerView_Adapter;

public class Task_Form_Activity extends AppCompatActivity {
private EditText Task_Title,Task_Des;
private TextView Task_Date;
    private int year_x, month_x, day_x;
    private Calendar cal;
    private DatePickerDialog datePicker;
    private ImageView Img_File;
    private Uri Image_uri;
    private GroupModel model;
    private Boolean Updated=false;
    private Task_Model TaskModel;
    private Spinner Task_Spinner;
    private List<GroupModel> list;
    private TransactionDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_form);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        list = new ArrayList<>();
        mDbHelper = TransactionDbHelper.getInstance(this);
        list.addAll(mDbHelper.getGroups(0));
        /*if ((GroupModel) getIntent().getSerializableExtra("Group") != null) {
            Updated = true;
            model = (GroupModel) getIntent().getSerializableExtra("Group");
            TaskModel = (Task_Model) getIntent().getSerializableExtra("Task");
        }*/
        cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        month_x = cal.get(Calendar.MONTH);
        Task_Title = findViewById(R.id.Task_Title_Edit);
        Task_Des = findViewById(R.id.Task_Description_Edit);
        Task_Date = findViewById(R.id.Task_Date_Edit);
        Task_Spinner = findViewById(R.id.Task_Spinner);
        Task_Date.setText(year_x + "-" + (month_x + 1) + "-" + day_x);
        Img_File = findViewById(R.id.Task_Image_view);
        Img_File.setVisibility(View.GONE);
        String[] stringlist = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            stringlist[i] = list.get(i).getGroupName();
        }
        ArrayAdapter<String> adapter0 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, stringlist);
        adapter0.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        Task_Spinner.setAdapter(adapter0);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        datePicker =
                new DatePickerDialog(Task_Form_Activity.this, R.style.Theme_AppCompat_DayNight_Dialog,
                        dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));
        datePicker.setCancelable(true);
        datePicker.setTitle("Select Date");
        if ((GroupModel) getIntent().getSerializableExtra("Group") != null && (Task_Model) getIntent().getSerializableExtra("Task") != null) {
            TaskModel = (Task_Model) getIntent().getSerializableExtra("Task");
            Task_Title.setText(TaskModel.getTask_Title());
            Task_Des.setText(TaskModel.getTask_Des());
            Task_Date.setText(TaskModel.getTask_Date());
            model = (GroupModel) getIntent().getSerializableExtra("Group");
            Task_Spinner.setSelection(adapter0.getPosition(model.getGroupName()));
        }else if ((Task_Model) getIntent().getSerializableExtra("Task") != null) {
            TaskModel = (Task_Model) getIntent().getSerializableExtra("Task");
            Task_Title.setText(TaskModel.getTask_Title());
            Task_Des.setText(TaskModel.getTask_Des());
            Task_Date.setText(TaskModel.getTask_Date());
            for(int i=0;i<list.size();i++){
                if (TaskModel.getGroup_Id()==list.get(i).getGroupId()){
            Task_Spinner.setSelection(adapter0.getPosition(list.get(i).getGroupName()));}}
        } else if ((GroupModel) getIntent().getSerializableExtra("Group") != null) {
            model = (GroupModel) getIntent().getSerializableExtra("Group");
            Task_Spinner.setSelection(adapter0.getPosition(model.getGroupName()));
        }

    }

    public void Submit(View view){
        String Title=Task_Title.getText().toString();
        String Des=Task_Des.getText().toString();
        String Date=Task_Date.getText().toString();
        View Focus=null;
        Boolean cancel=false;
        if(TextUtils.isEmpty(Title)){
            Task_Title.setError("Error");
            Focus=Task_Title;
            cancel=true;
        }else if(TextUtils.isEmpty(Des)){
            Task_Des.setError("Error");
            Focus=Task_Des;
            cancel=true;
        }else if(TextUtils.isEmpty(Date)){
            Task_Date.setError("Error");
            Focus=Task_Date;
            cancel=true;
        }
        if(cancel){
            Focus.requestFocus();
        }{ Intent intent=new Intent();
             if (TaskModel==null){
                 TaskModel=new Task_Model(Title,Des,Date,false);
                 String selectedItem=Task_Spinner.getSelectedItem().toString();
                 for (int i=0;i<list.size();i++){
                     if(TextUtils.equals(selectedItem,list.get(i).getGroupName())){
                         TaskModel.setGroup_Id(list.get(i).getGroupId());
                     }
                 }

             }
        long id= mDbHelper.addGroupNotes(TaskModel);
            if(id<0){
                Toast.makeText(getApplicationContext(),"Task was not saved with "+id,Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getApplicationContext(),"Task saved with "+id,Toast.LENGTH_LONG).show();
            intent.putExtra("Title",Title);
            intent.putExtra("Date",Date);
            intent.putExtra("Des",Des);
            setResult(52, intent);
                Groups.task_slider_adapter.add(new Task_Model(Title,Des,Date,false));
            finish();}
        }


    }


    public void OnclickDate(View view){
        datePicker.show();
    }

    public void Onclick_Image_button(View view) {
        final EditText Image_Link;

        LayoutInflater LI = LayoutInflater.from(Task_Form_Activity.this);
        //View PromptsView=LI.inflate(R.layout.image_dialog,null);

        //Image_Link=(EditText)PromptsView.findViewById(R.id.);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Task_Form_Activity.this);

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





    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //callbackManager.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult: ",requestCode+"");
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
        }
    }






    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    year_x = year;
                    month_x = month + 1;
                    day_x = dayOfMonth;
                    //Selected_date = dayOfMonth;
				/*year_x = cal.get(Calendar.YEAR);
				day_x = cal.get(Calendar.DAY_OF_MONTH);
				month_x = cal.get(Calendar.MONTH);
                     if(year==cal.get(Calendar.YEAR) && month==cal.get(Calendar.MONTH) && dayOfMonth==cal.get(Calendar.DAY_OF_MONTH) ){
                         dateEdit.setText("Today");
                     } else if(year==cal.get(Calendar.YEAR) && month==cal.get(Calendar.MONTH) && dayOfMonth==cal.get(Calendar.DAY_OF_MONTH)+1 ){
                         dateEdit.setText("Tomorrow");
                     }else if(year==cal.get(Calendar.YEAR) && month==cal.get(Calendar.MONTH) && dayOfMonth==cal.get(Calendar.DAY_OF_MONTH)-1 ){
                         dateEdit.setText("Yesterday");
                     }else{ dateEdit.setText(dayOfMonth+" "+Months[month]+" "+year);}*/
                    Task_Date.setText(year_x + "-" + month_x + "-" + day_x);
                }
            };
}
