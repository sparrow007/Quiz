package com.zersey.roz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.zersey.roz.Data.TransactionDbHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Group_Transactions.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Group_Transactions#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Group_Transactions extends Fragment {
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	private List<IncomeModel> list;
	private String mParam1;
	private String mParam2;
	private TransactionDbHelper mDbHelper;
	private RecyclerView recyclerView;
	private OnFragmentInteractionListener mListener;
	public static Group_Transaction_Adapter adapter;
	private List<ContactModel> ContactList;

	public Group_Transactions() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment Group_Transactions.
	 */
	// TODO: Rename and change types and number of parameters
	public static Group_Transactions newInstance(String param1, String param2) {
		Group_Transactions fragment = new Group_Transactions();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDbHelper = TransactionDbHelper.getInstance(getContext());
		list = new ArrayList<>();
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
		//initList();
		list.addAll(mDbHelper.getGroupEntries(((Specific_Group) getActivity()).model.getGroupId()));
		adapter = new Group_Transaction_Adapter(list);
	}

	@Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		View fragmentLayout =
			inflater.inflate(R.layout.fragment_group_transactions, container, false);
		fragmentLayout = initRecyclerView(fragmentLayout);
		FloatingActionButton fab = getActivity().findViewById(R.id.fab_Transaction);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View view) {
				Intent intent = new Intent(getContext(), MainActivity.class);
				intent.putExtra("group", ((Specific_Group) getActivity()).model);
				startActivityForResult(intent, 123);
			}
		});
		return fragmentLayout;
	}

	@Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 123) {
			if (resultCode == Activity.RESULT_OK) {
				adapter.addItem((IncomeModel) data.getSerializableExtra("model"));
			}
		}
	}
	//public void initList(){
	//    list=new ArrayList<>();
	//    for(int i=0;i<20;i++){
	//        list.add("Title");
	//    }
	//}

	public View initRecyclerView(View fragmentLayout) {
		recyclerView = fragmentLayout.findViewById(R.id.Group_Transaction_RecyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		recyclerView.setAdapter(adapter);
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
