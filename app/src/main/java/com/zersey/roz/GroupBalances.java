package com.zersey.roz;

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
	private List<String> list;
	private TransactionDbHelper mDbHelper;

	public GroupBalances() {
		// Required empty public constructor
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

		List<BillModel> list = new ArrayList<>(mDbHelper.getGroupEntries(((SpecificGroup)
				getActivity()).model.getGroupId()));


		String[] names = ((SpecificGroup) getActivity()).model.getFullname().split(",");
		String[] groupUsers = ((SpecificGroup) getActivity()).model.getUsers().split(",");

		Map<String, String> namesMap = new HashMap<>();

		for (int i = 0; i < groupUsers.length; i++) {
			namesMap.put(groupUsers[i], names[i]);
		}

		for (BillModel bill : list) {
			String[] dues = bill.getAmountDue().split(",");
			String[] users = bill.getPayerId().split(",");

			for (int i = 0; i < dues.length; i++) {
				if (!aggregates.containsKey(users[i])) {
					aggregates.put(users[i], Double.parseDouble(dues[i]));
				} else {
					double d = aggregates.get(users[i]);
					aggregates.remove(users[i]);
					aggregates.put(users[i], d + Double.parseDouble(dues[i]));
				}
			}
		}

		if (((SpecificGroup) getActivity()).model.getGroupId() == 195) {
			for (String key : aggregates.keySet()) {
				Log.d(GroupBalances.class.getSimpleName(), "getBalances: Key-" + key + ", Value-"
						+ aggregates.get(key));

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


		if (((SpecificGroup) getActivity()).model.getGroupId() == 195) {
			for (int i = 0; i < keys.size(); i++) {
				Log.d(GroupBalances.class.getSimpleName(), "After sort: Key-" + keys.get(i) + "," +
						" " +
						"Value-" + values.get(i));

			}
		}


		for (int i = 0, j = values.size() - 1; i < j; ) {
			if (Math.abs(values.get(i)) > values.get(j)) {
				values.set(i, values.get(i) + values.get(j));
				balances.add(namesMap.get(keys.get(j)) + " owes Rs " + values.get(j) + " to " +
						namesMap.get(keys.get(i)));
				j--;
			} else if (Math.abs(values.get(i)) < values.get(j)) {
				values.set(j, values.get(i) + values.get(j));
				balances.add(namesMap.get(keys.get(j)) + " owes Rs " + Math.abs(values.get(i)) +
						"" +
						" to " + namesMap.get(keys.get(i)));
				i++;
			} else {
				balances.add(namesMap.get(keys.get(j)) + " owes Rs " + values.get(j) + " to " +
						namesMap.get(keys.get(i)));
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


	public View initRecyclerView(View fragmentLayout) {
		RecyclerView recyclerView = (RecyclerView) fragmentLayout.findViewById(R.id
				.Group_Balances_RecyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		recyclerView.setAdapter(new Group_Balances_Adapter(list));
		return fragmentLayout;
	}
}
