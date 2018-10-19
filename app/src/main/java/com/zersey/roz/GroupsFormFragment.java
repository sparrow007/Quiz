package com.zersey.roz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.zersey.roz.Data.TransactionDbHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class GroupsFormFragment extends DialogFragment {

	private static final int REQUEST_CODE_ADD_MEMBER = 1;

	private ArrayList<ContactModel> itemList;
	private RecyclerView contactRecyclerView;
	private EditText descriptionEditText, titleEditText;
	private StringBuffer users = new StringBuffer();
	private TransactionDbHelper mDbHelper;
	private Context context;
	private int split = 1;
	private SharedPreferences prefs;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDbHelper = TransactionDbHelper.getInstance(getContext());
		itemList = new ArrayList<>();
		if (getActivity() != null) {
			prefs = getActivity().getSharedPreferences("login", MODE_PRIVATE);
			users.append(prefs.getString("userid", null));
		}
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		context = inflater.getContext();
		final View layoutView = inflater.inflate(R.layout.fragment_groups_form, container, false);
		RadioGroup radioGroup = layoutView.findViewById(R.id.split_radio_group);
		radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int i) {
				switch (i) {
					case R.id.radio_split:
						split = 1;
						break;
					case R.id.radio_no_split:
						split = 0;
						break;
				}
			}
		});
		descriptionEditText = layoutView.findViewById(R.id.group_desc_edit_text);
		titleEditText = layoutView.findViewById(R.id.group_name_edit_text);
		contactRecyclerView = layoutView.findViewById(R.id.group_member_recycler_view);
		Button submitButton = layoutView.findViewById(R.id.group_submit);
		contactRecyclerView.setLayoutManager(
				new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

		TextView addMemberButton = layoutView.findViewById(R.id.group_member_add_layout);

		addMemberButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, AddMembersActivity.class);
				startActivityForResult(intent, REQUEST_CODE_ADD_MEMBER);
			}
		});

		submitButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				GroupModel model = new GroupModel();
				model.setGroupName(titleEditText.getText().toString());
				model.setGroupDesc(descriptionEditText.getText().toString());
				model.setUsers(users.toString());
				List<ContactModel> userList = mDbHelper.getUserWithUserId(users.toString().split(","));
				StringBuilder fullNames = new StringBuilder();
				fullNames.append(prefs.getString("fullname", null)).append(",");
				StringBuilder phones = new StringBuilder();
				phones.append(prefs.getString("phone", null)).append(",");
				for (ContactModel m : userList) {
					fullNames.append(m.getName()).append(",");
					phones.append(m.getNumber()).append(",");
				}
				model.setFullNames(fullNames.toString());
				model.setMobileNos(phones.toString());

				JSONObject jsonObject = new JSONObject();

				try {
					if (split == 1) {
						jsonObject.put("split", 1);
						jsonObject.put("no_split", 0);
					} else {
						jsonObject.put("split", 0);
						jsonObject.put("no_split", 1);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				model.setTypeId(0);
				model.setGroupSettings(jsonObject.toString());
				model.setCreatedAt(Util.getDateTime());
				model.setUpdatedAt(Util.getDateTime());

				long groupId = mDbHelper.createGroup(model);
				model.setId(groupId);
				new ServerUtil(context).createGroup(model, itemList);
				Intent intent = new Intent();
				intent.putExtra("group", model);
				GroupsFragment.groupRecyclerAdapter.addItem(model);
				dismiss();
                /*setResult(Activity.RESULT_OK, intent);
                finish();*/
			}
		});

		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (getDialog().getWindow() != null) {
			getDialog().getWindow()
					.setBackgroundDrawable(
							new ColorDrawable(context.getResources().getColor(R.color.TransparentWhite)));
			getDialog().show();
			getDialog().getWindow()
					.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
							WindowManager.LayoutParams.MATCH_PARENT);
			getDialog().getWindow()
					.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
							| WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		}
		return layoutView;
	}

	/*public boolean findInList(String Number) {
		Boolean check = false;
		for (int i = 0; i < itemList.size(); i++) {
			check = TextUtils.equals(itemList.get(i).getNumber(), Number);
		}
		return check;
	}*/

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_ADD_MEMBER) {
			if (data != null) {
				contactRecyclerView.setVisibility(View.VISIBLE);
				//noinspection unchecked
				List<ContactModel> list = (List<ContactModel>) data.getSerializableExtra("ADDED");
				itemList.addAll(list);
				ContactRecyclerViewAdapter contactAdapter =
						new ContactRecyclerViewAdapter(itemList);
				contactRecyclerView.setAdapter(contactAdapter);
				for (ContactModel contactModel : list) {
					users.append(",").append(contactModel.getUserId());
				}
			}
		}
	}
}
