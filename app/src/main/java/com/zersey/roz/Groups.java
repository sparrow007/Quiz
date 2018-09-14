package com.zersey.roz;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zersey.roz.Data.TransactionDbHelper;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class Groups extends Fragment {
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	private TextView First_More, Second_More;
	private String mParam1;
	private String mParam2;
	private RecyclerView First_RecyclerView, Second_RecyclerView, Task_RecyclerView;
	private List<GroupModel> First_List, Second_List;
	private OnFragmentInteractionListener mListener;
	public static First_Slider_Adapter ADAPTER;
	private TransactionDbHelper mDbHelper;
	private List<IncomeModel> Item_list;
	private List<Task_Model> Task_list;
	private List<GroupModel> list;
	public static RecyclerAdapter adapter;
	public static Task_Slider_Adapter task_slider_adapter;

	public Groups() {
		// Required empty public constructor
	}

	public static Groups newInstance(String param1, String param2) {
		Groups fragment = new Groups();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		list = new ArrayList<>();
		if (getArguments() != null) {
			list.addAll((List<GroupModel>) getArguments().getSerializable("groupList"));
		}
		//mDbHelper = new TransactionDbHelper(getContext());
		First_List = new ArrayList<>(list);
		Second_List = new ArrayList<>();

		//initList();
		mDbHelper = TransactionDbHelper.getInstance(getContext());
		Item_list = new ArrayList<>(mDbHelper.getAllEntries());
		Task_list = new ArrayList<>();
		Task_list = mDbHelper.getTask(-1);
		/*for (int i=0;i<10;i++){
			Task_list.add(new Task_Model("New Task","New Description","null",false));
		}*/
	}

	@Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		View fragmentLayout = inflater.inflate(R.layout.fragment_groups, container, false);
		First_More = fragmentLayout.findViewById(R.id.First_More);
		Second_More = fragmentLayout.findViewById(R.id.Second_More);
		First_More.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				Intent intent = new Intent(getContext(), More_Activity.class);
				intent.putExtra("More", (ArrayList<GroupModel>) First_List);
				Log.d("onClick: ", First_List.size() + "");
				startActivity(intent);
			}
		});
		Second_More.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				Intent intent = new Intent(getContext(), Transactions.class);
				//intent.putExtra("More", (ArrayList<GroupModel>) Second_List);
				//Log.d("onClick: ", First_List.size() + "");
				startActivity(intent);
			}
		});
		fragmentLayout = initRecyclerView(fragmentLayout);

		((Main2Activity) getActivity()).setFragmentRefreshListener(
			new Main2Activity.FragmentRefreshListener() {
				@Override public void onRefresh(List<GroupModel> groups) {
					First_List.clear();
					First_List.addAll(groups);
					First_RecyclerView.getAdapter().notifyDataSetChanged();
					Item_list.clear();
					Item_list.addAll(mDbHelper.getAllEntries());
					Second_RecyclerView.getAdapter().notifyDataSetChanged();
				}

				@Override public void onTaskRefresh(List<Task_Model> taskList) {
					Task_list.clear();
					Task_list.addAll(taskList);
					Task_RecyclerView.getAdapter().notifyDataSetChanged();
				}
			});

		return fragmentLayout;
	}

	//private void initList() {
	//	for (int i = 0; i < 5; i++) {
	//		First_List.add("Title");
	//		Second_List.add("WAO 1");
	//	}
	//}

	private View initRecyclerView(View fragmentLayout) {
		Task_RecyclerView = fragmentLayout.findViewById(R.id.Task_Slider);
		Task_RecyclerView.setLayoutManager(
			new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
		task_slider_adapter = new Task_Slider_Adapter(Task_list);
		Task_RecyclerView.setAdapter(task_slider_adapter);

		First_RecyclerView = fragmentLayout.findViewById(R.id.First_Slider);
		First_RecyclerView.setLayoutManager(
			new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
		//new CardSnapHelper().attachToRecyclerView(First_RecyclerView);
		ADAPTER = new First_Slider_Adapter(getContext(), First_List);
		First_RecyclerView.setAdapter(ADAPTER);
		//First_RecyclerView.smoothScrollToPosition(0);

		Second_RecyclerView = fragmentLayout.findViewById(R.id.Second_Slider);
		Second_RecyclerView.setLayoutManager(
			new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
		adapter = new RecyclerAdapter(Item_list);
		Second_RecyclerView.setAdapter(adapter);

		return fragmentLayout;
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

	public interface OnFragmentInteractionListener {
		void onFragmentInteraction(Uri uri);
	}

	@Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1234) {
			if (resultCode == RESULT_OK) {
				GroupModel model = (GroupModel) data.getSerializableExtra("group");
				First_List.add(model);
				First_RecyclerView.getAdapter().notifyItemInserted(First_List.size() - 1);
			}
		} else if (resultCode == 52) {

		}
	}
}
