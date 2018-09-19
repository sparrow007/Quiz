package com.zersey.roz;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zersey.roz.Data.TransactionDbHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.fabric.sdk.android.services.concurrency.Task;

public class Task_Slider_Adapter extends RecyclerView.Adapter<Task_Slider_Adapter.Task_ViewHolder> {

	 private List<Task_Model> list;
	private Context context;
	private Task_Model TaskModel;
	int ViewType = 0;
	private int year_x, month_x, day_x;
	private Calendar cal;
	//Button positive_Button;
	TransactionDbHelper mDbHelper;
	private List<GroupModel> Grouplist;
	private DatePickerDialog datePicker;

	Task_Slider_Adapter(List<Task_Model> list) {
		this.list = list;

	}

	@NonNull @Override
	public Task_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		context = parent.getContext();
		mDbHelper=TransactionDbHelper.getInstance(context);

		if (viewType == 1) {
			View view = inflater.inflate(R.layout.add_button, parent, false);
			return new Task_ViewHolder(view);
		} else {
			View view = inflater.inflate(R.layout.task_card, parent, false);
			return new Task_ViewHolder(view);
		}
	}

	@Override public void onBindViewHolder(@NonNull Task_ViewHolder holder, int position) {

		if (position == list.size()) {
			holder.Plus_Button.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View v) {

				    Task_Dialog();
					/*Intent intent = new Intent(context, Task_Form_Activity.class);
					context.startActivity(intent);*/
				}
			});
		} else {
			holder.TaskTitle.setText(list.get(position).getTask_Title());
			if (list.get(position).getTask_Checked()) {
				holder.Check.setImageResource(R.drawable.checked);
			} else {
				holder.Check.setImageResource(R.drawable.unchecked);
			}
		}
	}
    public void Task_Dialog(){
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
        LayoutInflater LI = LayoutInflater.from(context);
        final View PromptsView = LI.inflate(R.layout.content_task__form_, null);
        Task_Title = PromptsView.findViewById(R.id.Task_Title_Edit);
        Task_Des = PromptsView.findViewById(R.id.Task_Description_Edit);
        Task_Date = PromptsView.findViewById(R.id.Task_Date_Edit);
        Task_Spinner = PromptsView.findViewById(R.id.Task_Spinner);
        Submit=PromptsView.findViewById(R.id.Submit_Task_Button);
		cal = Calendar.getInstance();
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

		/*alertDialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
			@Override public void onClick(DialogInterface dialog, int which) {
				//Split_List = Adapter.getList();
				Toast.makeText(context,"Submit Clicked",Toast.LENGTH_LONG).show();
			}
		});*/

        final android.support.v7.app.AlertDialog dialog = alertDialogBuilder.create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {

			@Override public void onShow(DialogInterface dialogInterface) {

				Task_Date.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						datePicker.show();
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
								TaskModel = new Task_Model();
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
								Groups.task_slider_adapter.add(TaskModel);
								//finish();
                               dialog.dismiss();
							}
						}
					}
				});
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
				});*/
			}
		});
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
    }
	@Override public int getItemCount() {
		return list.size() + 1;
	}

	@Override public int getItemViewType(int position) {
		if (position == list.size()) {
			ViewType = 1;
			return 1;
		} else {
			ViewType = 0;
			return 0;
		}
	}

	public void add(Task_Model model) {
		list.add(model);
		notifyDataSetChanged();
	}

	public class Task_ViewHolder extends RecyclerView.ViewHolder {
		TextView TaskTitle, Des;
		ImageButton Check, Plus_Button;

		public Task_ViewHolder(View itemView) {
			super(itemView);

			if (ViewType == 1) {
				Plus_Button = itemView.findViewById(R.id.Add_Group_Plus_Button);
			} else {
				TaskTitle = itemView.findViewById(R.id.Horizontal_Task_Title);
				Des = itemView.findViewById(R.id.Horizontal_Task_Des);
				Check = itemView.findViewById(R.id.Horizontal_Task_Check);

				Check.setOnClickListener(new View.OnClickListener() {
					@Override public void onClick(View v) {
						Task_Model model = list.get(getAdapterPosition());

						if (list.get(getAdapterPosition()).getTask_Checked()) {
							model.setTask_Checked(false);
							list.set(getAdapterPosition(), model);
							notifyDataSetChanged();
						} else {
							model.setTask_Checked(true);
							list.set(getAdapterPosition(), model);
							notifyDataSetChanged();
						}
					}
				});

				TaskTitle.setOnClickListener(new View.OnClickListener() {
					@Override public void onClick(View v) {
						Intent intent = new Intent(context, Task_Form_Activity.class);
						intent.putExtra("Task", list.get(getAdapterPosition()));
						context.startActivity(intent);
					}
				});
			}
		}
	}
}
