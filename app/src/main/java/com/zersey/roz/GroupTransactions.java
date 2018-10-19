package com.zersey.roz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zersey.roz.Data.TransactionDbHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class GroupTransactions extends Fragment {

	private List<BillModel> list;

	private TransactionDbHelper mDbHelper;
	private RecyclerView recyclerView;
	private OnFragmentInteractionListener mListener;
	public static GroupTransactionAdapter adapter;

	private Map<String, Double> aggregates;
	List<String> balances;

	public GroupTransactions() {
		// Required empty public constructor
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDbHelper = TransactionDbHelper.getInstance(getContext());
		list = new ArrayList<>();


		list.addAll(mDbHelper.getGroupEntries(((SpecificGroup) getActivity()).model.getGroupId()));
		SharedPreferences prefs = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
		adapter = new GroupTransactionAdapter(list, prefs.getString("userid", null));
	}


	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View fragmentLayout =
				inflater.inflate(R.layout.fragment_group_transactions, container, false);
		fragmentLayout = initRecyclerView(fragmentLayout);
		return fragmentLayout;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 123) {
			if (resultCode == Activity.RESULT_OK) {
				adapter.addItem((BillModel) data.getSerializableExtra("model"));
			}
		}
	}


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

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnFragmentInteractionListener) {
			mListener = (OnFragmentInteractionListener) context;
		} else {
			throw new RuntimeException(
					context.toString() + " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	public interface OnFragmentInteractionListener {
		void onFragmentInteraction(Uri uri);
	}
}
