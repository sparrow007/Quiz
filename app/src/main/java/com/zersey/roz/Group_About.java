package com.zersey.roz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zersey.roz.Data.TransactionDbHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Group_About.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Group_About#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Group_About extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	private List<ContactModel> list;
	private LinearLayout Add_Member;
	private RecyclerView recyclerView;
	private ArrayList<ContactModel> Item_list;
	public static Group_About_Adapter RecyclerView_Adapter;
	private TransactionDbHelper mDbHelper;

	private GroupModel mParam1;

	private OnFragmentInteractionListener mListener;

	public Group_About() {
		// Required empty public constructor
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @param param1 Parameter 1.
	 * @param param2 Parameter 2.
	 * @return A new instance of fragment Group_About.
	 */
	// TODO: Rename and change types and number of parameters
	public static Group_About newInstance(String param1, String param2) {
		Group_About fragment = new Group_About();
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
		mDbHelper = TransactionDbHelper.getInstance(getContext());
		list = new ArrayList<>();
		String[] users = mParam1.getUsers().split(",");

		SharedPreferences prefs = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);

		for (String user : users) {
			if (user.equals(prefs.getString("userid", null))) {
				ContactModel m = new ContactModel();
				m.setName("You");
				m.setUserId(Long.parseLong(prefs.getString("userid", null)));
				m.setNumber(prefs.getString("phone", null));
				list.add(m);
			}
		}

		list.addAll(mDbHelper.getUserWithUserId(users));
	}

	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		View fragmentLayout = inflater.inflate(R.layout.fragment_group__about, container, false);
		TextView desc = fragmentLayout.findViewById(R.id.group_desc);
		Add_Member = fragmentLayout.findViewById(R.id.Add_Group_Member_Layout);
		/*Add_Member.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				Intent intent = new Intent(getContext(), Add_Members_Activity.class);
				startActivityForResult(intent, 1);
			}
		}); */
		desc.setText(mParam1.getGroupDesc());
		fragmentLayout = initRecyclerView(fragmentLayout);
		return fragmentLayout;
	}

	public View initRecyclerView(View fragmentLayout) {
		recyclerView = fragmentLayout.findViewById(R.id.Group_About_RecyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		RecyclerView_Adapter = new Group_About_Adapter(list);
		recyclerView.setAdapter(RecyclerView_Adapter);
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

	@Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Log.d("onActivityResult: ", resultCode + "");
		if (resultCode == -1) {
			recyclerView.setVisibility(View.VISIBLE);
			Item_list = new ArrayList<>();
			if (TextUtils.equals(list.get(0).toString(), "No Members")) {
				list = new ArrayList<>();
			}
			Item_list.addAll((List<ContactModel>) data.getSerializableExtra("ADDED"));
			list.addAll(Item_list);
			Log.d("onActivityResult: ", list.size() + "");
			RecyclerView_Adapter = new Group_About_Adapter(list);
			recyclerView.setAdapter(RecyclerView_Adapter);
		}
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
