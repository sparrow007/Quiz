package com.zersey.roz;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.zersey.roz.Data.TransactionDbHelper;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Task_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Task_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Task_Fragment extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private GroupModel mParam1;
	private String mParam2;

	public static Task_Adapter adapter;
	private List<com.zersey.roz.TaskModel> Task_list;
	private RecyclerView Task_RecyclerView;
	private com.zersey.roz.TaskModel TaskModel;
	private int year_x, month_x, day_x;
	private Calendar cal;
	//TransactionDbHelper mDbHelper;
	private List<GroupModel> Grouplist;
	private DatePickerDialog datePicker;
	private List<GroupModel> groupModels;
	private TransactionDbHelper mdbHelper;
	private OnFragmentInteractionListener mListener;


	public Task_Fragment() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment Task_Fragment.
	 */
	// TODO: Rename and change types and number of parameters
	public static Task_Fragment newInstance(String param1, String param2) {
		Task_Fragment fragment = new Task_Fragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = (GroupModel) getArguments().getSerializable("group");
		}
		mdbHelper = TransactionDbHelper.getInstance(getContext());
		initTaskList();
	}

	@Override public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View fragment = inflater.inflate(R.layout.fragment_task, container, false);
		fragment = initRecyclerView(fragment);
		//FloatingActionButton fab = getActivity().findViewById(R.id.fab_Transaction);
		//fab.setOnClickListener(new View.OnClickListener() {
		//	@Override public void onClick(View view) {
		//		Task_Dialog();
		//	}
		//});
		return fragment;
	}

	public void initTaskList() {
		Task_list = new ArrayList<>();
        /*for (int i=0;i<10;i++){
            Task_list.add(new TaskModel("New Task","New Description","null",false));
        }*/
		Log.d("initTaskList: ", "" + mParam1.getGroupId());
		Task_list = mdbHelper.getTask(mParam1.getGroupId());
		Log.d("initTaskList: ", "" + Task_list.size());
	}

	public void Task_Dialog(){
		Grouplist = new ArrayList<>();
		mdbHelper = TransactionDbHelper.getInstance(getContext());
		Grouplist.addAll(mdbHelper.getGroups(0));
		String[] stringlist = new String[Grouplist.size()];
		for (int i = 0; i < Grouplist.size(); i++) {
			stringlist[i] = Grouplist.get(i).getGroupName();
		}

		final EditText Task_Title, Task_Des;
		final TextView Task_Date;
		final Spinner Task_Spinner;
		final Button Submit;
		LayoutInflater LI = LayoutInflater.from(getContext());
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
				new DatePickerDialog(getContext(), R.style.Theme_AppCompat_DayNight_Dialog,
						dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
						cal.get(Calendar.DAY_OF_MONTH));
		datePicker.setCancelable(true);
		datePicker.setTitle("Select Date");

		ArrayAdapter<String> adapter0 =
				new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, stringlist);
		adapter0.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
		Task_Spinner.setAdapter(adapter0);
		Task_Spinner.setSelection(adapter0.getPosition(mParam1.getGroupName()));
		android.support.v7.app.AlertDialog.Builder alertDialogBuilder =
				new android.support.v7.app.AlertDialog.Builder(getContext());
		alertDialogBuilder.setView(PromptsView);
		alertDialogBuilder.setCancelable(true);

		/*alertDialogBuilder.setPositiveButton("submitBill", new DialogInterface.OnClickListener() {
			@Override public void onClick(DialogInterface dialog, int which) {
				//Split_List = Adapter.getList();
				Toast.makeText(context,"submitBill Clicked",Toast.LENGTH_LONG).show();
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
								TaskModel = new TaskModel();
								TaskModel.setTask_Title(Title);
								TaskModel.setTask_Des(Des);
								TaskModel.setTask_Date(Date);
								TaskModel.setTask_Checked(false);
								TaskModel.setGroup_Id(Grouplist.get(Task_Spinner.getSelectedItemPosition()).getGroupId());
								Log.d( "onClick: ",Grouplist.get(Task_Spinner.getSelectedItemPosition()).getGroupId()+"");
							}
							long id = mdbHelper.addGroupNotes(TaskModel);
							if (id < 0) {
								Toast.makeText(getContext(), "Task was not saved with " + id,
										Toast.LENGTH_LONG).show();
							} else {
								Toast.makeText(getContext(), "Task saved with " + id, Toast.LENGTH_LONG)
										.show();
								new ServerUtil(getContext()).createGroupTask(TaskModel);
								intent.putExtra("task", TaskModel);
								//setResult(Activity.RESULT_OK, intent);
								//GroupsFragment.tasksAdapter.add(TaskModel);
								adapter.add(TaskModel);
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
		dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getContext().getResources().getColor(R.color.TransparentWhite)));
		//dialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
		dialog.show();
		dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.MATCH_PARENT);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));



		// Copy the alert dialog window attributes to new layout parameter instance
		//layoutParams.copyFrom(positive_Button.getLayoutParams());

		// Set the width and height for the layout parameters
		// This will bet the width and height of alert dialog
		layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
		//layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;

		//positive_Button.setLayoutParams(layoutParams);
		//positive_Button.setWidth();
		dialog.getWindow()
				.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
						| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
	}

	public View initRecyclerView(View fragment) {
		Task_RecyclerView = new RecyclerView(getContext());
		Task_RecyclerView = fragment.findViewById(R.id.Task_Recycler_View);
		Task_RecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		adapter = new Task_Adapter(Task_list, mParam1);
		Task_RecyclerView.setAdapter(adapter);
		return fragment;
	}

	@Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			com.zersey.roz.TaskModel model = (com.zersey.roz.TaskModel)data.getSerializableExtra("task");
			if (model.getGroup_Id() == mParam1.getGroupId())
				adapter.add(model);
		}
	}

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

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		void onFragmentInteraction(Uri uri);
	}
}
