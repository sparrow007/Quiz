package android.zersey.expense_manger;

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
import android.zersey.expense_manger.Data.TransactionDbHelper;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class Groups extends Fragment {
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	private TextView First_More, Second_More;
	private String mParam1;
	private String mParam2;
	private RecyclerView First_RecyclerView, Second_RecyclerView;
	private List<GroupModel> First_List, Second_List;
	private OnFragmentInteractionListener mListener;
	private TransactionDbHelper mDbHelper;

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
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
		mDbHelper = new TransactionDbHelper(getContext());
		First_List = new ArrayList<>(mDbHelper.getAllGroups());
		Second_List = new ArrayList<>();
		First_RecyclerView = new RecyclerView(getContext());
		Second_RecyclerView = new RecyclerView(getContext());
		//initList();
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
				Intent intent = new Intent(getContext(), More_Activity.class);
				intent.putExtra("More", (ArrayList<GroupModel>) Second_List);
				Log.d("onClick: ", First_List.size() + "");
				startActivity(intent);
			}
		});
		fragmentLayout = initRecyclerView(fragmentLayout);
		return fragmentLayout;
	}

	//private void initList() {
	//	for (int i = 0; i < 5; i++) {
	//		First_List.add("Title");
	//		Second_List.add("WAO 1");
	//	}
	//}

	private View initRecyclerView(View fragmentLayout) {
		First_RecyclerView = fragmentLayout.findViewById(R.id.First_Slider);
		First_RecyclerView.setLayoutManager(
			new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
		//new CardSnapHelper().attachToRecyclerView(First_RecyclerView);
		First_RecyclerView.setAdapter(new First_Slider_Adapter(getContext(), First_List));
		//First_RecyclerView.smoothScrollToPosition(0);

		Second_RecyclerView = fragmentLayout.findViewById(R.id.Second_Slider);
		Second_RecyclerView.setLayoutManager(
			new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
		//new CardSnapHelper().attachToRecyclerView(Second_RecyclerView);
		Second_RecyclerView.setAdapter(new Second_Slider_Adapter(getContext(), Second_List));
		//Second_RecyclerView.smoothScrollToPosition(0);

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
		Log.d("hueh", "onActivityResult: " + "in frag");
		if (data == null) {
			Log.d("hueh", "onActivityResult: frag" + "Data is null");
		}
		if (requestCode == 1234) {
			if (resultCode == RESULT_OK) {
				GroupModel model = (GroupModel) data.getSerializableExtra("group");
				Log.d("hueh", "onActivityResult: " + model.toString());
				First_List.add(model);
				First_RecyclerView.getAdapter().notifyItemInserted(First_List.size() - 1);
			}
		}
	}
}
