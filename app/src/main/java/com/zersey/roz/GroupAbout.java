package com.zersey.roz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zersey.roz.Data.TransactionDbHelper;
import java.util.ArrayList;
import java.util.List;

public class GroupAbout extends Fragment {

	private List<ContactModel> list, notFound;
	public static GroupAboutAdapter groupAboutAdapter;
	private TransactionDbHelper mDbHelper;
	private static final int REQUEST_CODE_ADD_MEMBER = 1;

	private GroupModel groupModel;
	private StringBuilder newUsers = new StringBuilder();

	public GroupAbout() {
		// Required empty public constructor
	}

	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			groupModel = (GroupModel) getArguments().getSerializable("group");
		}

		mDbHelper = TransactionDbHelper.getInstance(getContext());
		list = new ArrayList<>();
		notFound = new ArrayList<>();
		String[] users = groupModel.getUsers().split(",");

		String myUserId = "", myNumber = "";

		if (getActivity() != null) {
			SharedPreferences prefs =
				getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);
			myUserId = prefs.getString("userid", null);
			myNumber = prefs.getString("phone", null);
		}

		for (String user : users) {
			if (user.equals(myUserId)) {
				ContactModel m = new ContactModel();
				m.setName("You");
				m.setUserId(Long.parseLong(myUserId));
				m.setNumber(myNumber);
				list.add(m);
			}
		}

		List<ContactModel> tempList = new ArrayList<>(mDbHelper.getUserWithUserId(users));
		String[] mobile_no = groupModel.getMobile_no().split(",");
		String[] names = groupModel.getFullname().split(",");

		for (int i = 0; i < users.length; i++) {
			boolean flag = false;
			for (ContactModel model : tempList) {
				if (users[i].equals(Long.toString(model.getUserId()))) {
					list.add(model);
					flag = true;
					break;
				}
			}
			if (!flag && !users[i].equals(myUserId)) {
				ContactModel contactModel = new ContactModel();
				contactModel.setNumber(mobile_no[i]);
				contactModel.setName(names[i]);
				notFound.add(contactModel);
			}
		}
	}

	@Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		View fragmentLayout = inflater.inflate(R.layout.fragment_group__about, container, false);
		TextView desc = fragmentLayout.findViewById(R.id.group_desc);
		LinearLayout addMember = fragmentLayout.findViewById(R.id.Add_Group_Member_Layout);
		addMember.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				Intent intent = new Intent(getContext(), AddMembersActivity.class);
				startActivityForResult(intent, REQUEST_CODE_ADD_MEMBER);
			}
		});
		desc.setText(groupModel.getGroupDesc());
		fragmentLayout = initRecyclerView(fragmentLayout);
		return fragmentLayout;
	}

	public View initRecyclerView(View fragmentLayout) {
		RecyclerView recyclerView = fragmentLayout.findViewById(R.id.Group_About_RecyclerView);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		groupAboutAdapter = new GroupAboutAdapter(list, notFound);
		recyclerView.setAdapter(groupAboutAdapter);
		return fragmentLayout;
	}

	@Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_ADD_MEMBER) {
			if (resultCode == Activity.RESULT_OK) {
				@SuppressWarnings("unchecked") ArrayList<ContactModel> itemList =
					new ArrayList<>((ArrayList<ContactModel>) data.getSerializableExtra("ADDED"));
				list.addAll(itemList);
				groupAboutAdapter.notifyDataSetChanged();
				for (ContactModel contactModel : itemList) {
					newUsers.append(",").append(contactModel.getUserId());
				}
				groupModel.setUsers(groupModel.getUsers() + newUsers.toString());
				mDbHelper.updateGroup(groupModel);
				new ServerUtil(getContext()).editGroup(groupModel, itemList);
			}
		}
	}
}
