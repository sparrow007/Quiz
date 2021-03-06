package com.zersey.roz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.zersey.roz.Data.TransactionDbHelper;
import java.util.ArrayList;
import java.util.List;

public class Transactions extends AppCompatActivity implements
	BillRecyclerAdapter.BillItemClickListener {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	private List<BillModel> Item_list;
	private TransactionDbHelper mDbHelper;
	public static BillRecyclerAdapter adapter;

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private List<BillModel> newModels;

	public Transactions() {

	}

	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_transactions);
		mDbHelper = TransactionDbHelper.getInstance(this);
		Item_list = new ArrayList<>();

		//groups.addAll(mDbHelper.getGroups());
		newModels = new ArrayList<>();
		newModels.addAll(mDbHelper.getAllEntries());
		//
		//
		//newModels = new ArrayList<>();
		//int i = 0;
		//
		//
		//if (models.size() > 0) {
		//
		//	newModels.add(models.get(i));
		//	List<BillModel> subModels = new ArrayList<>();
		//	for (int j = 1; j < models.size(); j++) {
		//		if (groups.get(i).getGroupId() == models.get(j).getGroupId()) {
		//			subModels.add(models.get(j));
		//		} else {
		//			newModels.get(i).setSubList(subModels);
		//			newModels.add(models.get(j));
		//			subModels.clear();
		//			i++;
		//		}
		//	}
		//
		//	Collections.sort(newModels, new Comparator<BillModel>() {
		//		@Override public int compare(BillModel model, BillModel t1) {
		//			return (int) (model.getId() - t1.getId());
		//		}
		//	});
		//}
		//
		//Item_list.addAll(newModels);
		RecyclerView recyclerView = findViewById(R.id.Fragment_listView);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		adapter = new BillRecyclerAdapter(newModels, this);
		recyclerView.setAdapter(adapter);

		//Item_list=bundle.getParcelableArrayList("ARRAYLIST");

	}

	@Override public void onBillPlusCLick() {

	}
}
