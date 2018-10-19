package com.zersey.roz;

import android.content.Context;
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

import com.zersey.roz.Data.TransactionDbHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


public class GroupBalances extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	private RecyclerView recyclerView;
	private List<String> list;
	private OnFragmentInteractionListener mListener;
	private TransactionDbHelper mDbHelper;

	public GroupBalances() {
		// Required empty public constructor
	}

	public static GroupBalances newInstance(String param1, String param2) {
		GroupBalances fragment = new GroupBalances();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
		list = new ArrayList<>();
		mDbHelper = TransactionDbHelper.getInstance(getContext());
		list.addAll(getBalances());
	}

	private List<String> getBalances() {

		List<String> balances = new ArrayList<>();

		Map<String, Double> aggregates = new TreeMap<>();

		List<BillModel> list = new ArrayList<>(mDbHelper.getGroupEntries(((SpecificGroup) getActivity()).model.getGroupId()));



		String[] names = ((SpecificGroup) getActivity()).model.getFullname().split(",");
		String[] groupUsers = ((SpecificGroup) getActivity()).model.getUsers().split(",");

		Map<String, String> namesMap = new HashMap<>();

		for(int i = 0; i < groupUsers.length; i++){
			namesMap.put(groupUsers[i], names[i]);
		}

		for (BillModel bill : list) {
			String[] dues = bill.getAmountDue().split(",");
			String[] users = bill.getPayerId().split(",");

			for (int i = 0; i < dues.length; i++) {
				if (!aggregates.containsKey(users[i])) {
					aggregates.put(users[i], Double.parseDouble(dues[i]));
				} else {
					aggregates.put(users[i], aggregates.get(users[i]) + Double.parseDouble(dues[i]));
				}
			}
		}

		if(((SpecificGroup) getActivity()).model.getGroupId() == 195) {
			for(String key: aggregates.keySet()){
				Log.d(GroupBalances.class.getSimpleName(), "getBalances: Key-" + key + ", Value-" + aggregates.get(key));

			}
		}


		List<String> keys = new ArrayList<>(aggregates.keySet());
		List<Double> values = new ArrayList<>(aggregates.values());

		int n = values.size();
		for (int i = 0; i < n - 1; i++)
			for (int j = 0; j < n - i - 1; j++)
				if (values.get(j) > values.get(j + 1)) {
					double temp = values.get(j);
					values.set(j, values.get(j + 1));
					values.set(j + 1, temp);

					String str = keys.get(j);
					keys.set(j, keys.get(j + 1));
					keys.set(j + 1, str);

					str = names[j];
					names[j] = names[j + 1];
					names[j + 1] = str;
				}


		for (int i = 0, j = values.size() - 1; i < j; ) {
			if (Math.abs(values.get(i)) > values.get(j)) {
				values.set(i, values.get(i) + values.get(j));
				balances.add(namesMap.get(keys.get(j)) + " owes Rs " + values.get(j) + " to " + namesMap.get(keys.get(i)));
				j--;
			} else if (Math.abs(values.get(i)) < values.get(j)) {
				values.set(j, values.get(i) + values.get(j));
				balances.add(namesMap.get(keys.get(j)) + " owes Rs " + values.get(i) + " to " + namesMap.get(keys.get(i)));
				i++;
			} else {
				balances.add(namesMap.get(keys.get(j)) + " owes Rs " + values.get(j) + " to " + namesMap.get(keys.get(i)));
				i++;
				j--;
			}
		}

		return balances;
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		View fragmentLayout = inflater.inflate(R.layout.fragment_group_balances, container, false);
		fragmentLayout = initRecyclerView(fragmentLayout);
		return fragmentLayout;
	}

	public void initList() {
		for (int i = 0; i < 20; i++) {
			list.add("Bharat get back Rs10,000 in total");
		}
	}

	public View initRecyclerView(View fragmentLayout) {
		recyclerView = (RecyclerView) fragmentLayout.findViewById(R.id.Group_Balances_RecyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		recyclerView.setAdapter(new Group_Balances_Adapter(list));
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
			throw new RuntimeException(context.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
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
