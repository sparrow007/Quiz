package android.zersey.expense_manger;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.zersey.expense_manger.Data.TransactionDbHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Transactions.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Transactions#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Transactions extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	private List<IncomeModel> Item_list;
	private TransactionDbHelper mDbHelper;
	public static RecyclerAdapter adapter;

	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;

	private OnFragmentInteractionListener mListener;
	private List<IncomeModel> newModels;

	public Transactions() {

	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment Transactions.
	 */
	// TODO: Rename and change types and number of parameters
	public static Transactions newInstance(String param1, String param2) {
		Transactions fragment = new Transactions();
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
		Item_list = new ArrayList<>();

		List<GroupModel> groups = mDbHelper.getAllGroups();
		List<IncomeModel> models = mDbHelper.getAllEntries();

		//for (IncomeModel model : models) {
		//	Log.d("hueh", "onCreate: " + model.toString());
		//}
		//
		//for (GroupModel model : groups) {
		//	Log.d("hueh", "onCreate: " + model.toString());
		//}

		newModels = new ArrayList<>();
		int i = 0;


		if (models.size() > 0) {

			newModels.add(models.get(i));
			List<IncomeModel> subModels = new ArrayList<>();
			for (int j = 1; j < models.size(); j++) {
				if (groups.get(i).getGroupId() == models.get(j).getGroupId()) {
					subModels.add(models.get(j));
				} else {
					newModels.get(i).setSubList(subModels);
					newModels.add(models.get(j));
					subModels.clear();
					i++;
				}
			}

			Collections.sort(newModels, new Comparator<IncomeModel>() {
				@Override public int compare(IncomeModel model, IncomeModel t1) {
					return (int) (model.getId() - t1.getId());
				}
			});
		}

		Item_list.addAll(newModels);
		// Required empty public constructor
	}

	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View fragmentLayout = inflater.inflate(R.layout.fragment_transactions, container, false);
		RecyclerView recyclerView = fragmentLayout.findViewById(R.id.Fragment_listView);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		adapter = new RecyclerAdapter(newModels);
		recyclerView.setAdapter(adapter);

		//Item_list=bundle.getParcelableArrayList("ARRAYLIST");}

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
