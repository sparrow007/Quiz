package com.zersey.roz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.zersey.roz.Data.TransactionDbHelper;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class GroupsFragment extends Fragment implements GroupRecyclerAdapter.GroupItemClickListener, BillRecyclerAdapter.BillItemClickListener {

	private RecyclerView billsRecyclerView, groupsRecyclerView, taskRecyclerView;
	public static GroupRecyclerAdapter billsAdapter;
	private List<BillModel> billsList;
	private List<TaskModel> taskList;
	private List<GroupModel> groupsList;

	public static BillRecyclerAdapter groupsAdapter;
	public static TaskRecyclerAdapter tasksAdapter;

	public GroupsFragment() {
	}

	@SuppressWarnings("unchecked") @Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		groupsList = new ArrayList<>();
		billsList = new ArrayList<>();

		if (getArguments() != null) {
			groupsList.addAll((List<GroupModel>) getArguments().getSerializable("groupList"));
			billsList.addAll((List<BillModel>) getArguments().getSerializable("billList"));
		}
		TransactionDbHelper dbHelper = TransactionDbHelper.getInstance(getContext());
		taskList = new ArrayList<>();
		taskList = dbHelper.getTask(-1);
	}

	@Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		View fragmentLayout = inflater.inflate(R.layout.fragment_groups, container, false);
		TextView moreBillsButton = fragmentLayout.findViewById(R.id.First_More);
		TextView moreGroupsButton = fragmentLayout.findViewById(R.id.Second_More);
		moreBillsButton.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				Intent intent = new Intent(getContext(), More_Activity.class);
				intent.putExtra("More", (ArrayList<GroupModel>) groupsList);
				startActivity(intent);
			}
		});
		moreGroupsButton.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				Intent intent = new Intent(getContext(), Transactions.class);
				startActivity(intent);
			}
		});

		fragmentLayout = initRecyclerView(fragmentLayout);

		if (getActivity() != null && getFragmentManager() != null) {
			((Main2Activity) getActivity()).setFragmentRefreshListener(
				new Main2Activity.FragmentRefreshListener() {
					@Override public void onRefresh(List<GroupModel> groups) {
						groupsList.clear();
						groupsList.addAll(groups);
						billsRecyclerView.getAdapter().notifyDataSetChanged();
					}

					@Override public void onTaskRefresh(List<TaskModel> taskList) {
						GroupsFragment.this.taskList.clear();
						GroupsFragment.this.taskList.addAll(taskList);
						taskRecyclerView.getAdapter().notifyDataSetChanged();
					}

					@Override public void onBillsRefresh(List<BillModel> billsList) {
						GroupsFragment.this.billsList.clear();
						GroupsFragment.this.billsList.addAll(billsList);
						groupsRecyclerView.getAdapter().notifyDataSetChanged();
					}
				});

			FloatingActionButton openBillFab = getActivity().findViewById(R.id.fab_open_bill_form);
			openBillFab.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View view) {
					startDialogTransaction(new BillsFormFragment());
					((FloatingActionsMenu) (getActivity()).findViewById(R.id.fab)).collapse();
				}
			});

			FloatingActionButton openTaskFormFab =
				getActivity().findViewById(R.id.fab_open_tasks_form);
			openTaskFormFab.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View view) {
					startDialogTransaction(new Add_Task_form());
					((FloatingActionsMenu) (getActivity()).findViewById(R.id.fab)).collapse();
				}
			});

			FloatingActionButton openGroupFormFab =
				getActivity().findViewById(R.id.fab_open_group_form);
			openGroupFormFab.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View view) {
					startDialogTransaction(new Add_Groups_Form());
					((FloatingActionsMenu) (getActivity()).findViewById(R.id.fab)).collapse();
				}
			});
		}

		return fragmentLayout;
	}

	private void startDialogTransaction(DialogFragment dialog) {
		if (getFragmentManager() != null) {
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			Fragment prev = getFragmentManager().findFragmentByTag("dialog");
			if (prev != null) {
				ft.remove(prev);
			}
			ft.addToBackStack(null);
			dialog.show(ft, "dialog");
		}
	}

	private View initRecyclerView(View fragmentLayout) {
		taskRecyclerView = fragmentLayout.findViewById(R.id.Task_Slider);
		taskRecyclerView.setLayoutManager(
			new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
		tasksAdapter = new TaskRecyclerAdapter(taskList);
		taskRecyclerView.setAdapter(tasksAdapter);

		billsRecyclerView = fragmentLayout.findViewById(R.id.First_Slider);
		billsRecyclerView.setLayoutManager(
			new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
		billsAdapter = new GroupRecyclerAdapter(getContext(), groupsList, this);
		billsRecyclerView.setAdapter(billsAdapter);

		groupsRecyclerView = fragmentLayout.findViewById(R.id.Second_Slider);
		groupsRecyclerView.setLayoutManager(
			new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
		groupsAdapter = new BillRecyclerAdapter(billsList, this);
		groupsRecyclerView.setAdapter(groupsAdapter);

		return fragmentLayout;
	}

	@Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("onActivityResult: ", resultCode + "" + requestCode);
		if (requestCode == 1234) {
			if (resultCode == RESULT_OK) {
				GroupModel model = (GroupModel) data.getSerializableExtra("group");
				groupsList.add(model);
				billsRecyclerView.getAdapter().notifyItemInserted(groupsList.size() - 1);
			}
		}
	}

	@Override public void onBillPlusCLick() {
		startDialogTransaction(new BillsFormFragment());
	}

	@Override public void onGroupPlusClick() {
		startDialogTransaction(new Add_Groups_Form());
	}
}
