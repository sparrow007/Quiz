package com.zersey.roz;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import com.zersey.roz.Data.TransactionDbHelper;
import java.util.ArrayList;
import java.util.List;

public class Group_Form extends AppCompatActivity {
	private static final int REQUEST_CODE_ADD_MEMBER = 1;

	private LinearLayout Add_Member_Layout;
	private ListView Add_Member_ListView;
	private ArrayList<ContactModel> Item_list;
	private List<Split_Contact_model> Split_List;
	private Spinner Split_Spinner;
	private TextView Split_Notes;
	private RecyclerView Split_RecyclerView;
	private RecyclerView Contact_RecyclerView;
	private DialogSplitRecyclerViewAdapter Adapter;
	private EditText Amount_Edit, Description_Edit, Group_Name_Edit;
	private StringBuffer users = new StringBuffer("");
	private StringBuffer users_number = new StringBuffer("");
	String USERS = "No Members";
	private ContactRecyclerViewAdapter RecyclerView_Adapter;
	private TransactionDbHelper mDbHelper;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_form);
		Item_list = new ArrayList<>();
		mDbHelper = TransactionDbHelper.getInstance(this);

		Description_Edit = findViewById(R.id.group_desc_edit_text);
		Group_Name_Edit = findViewById(R.id.group_name_edit_text);
		Contact_RecyclerView = findViewById(R.id.Add_Member_Group_Form_RecyclerView);
		Contact_RecyclerView.setLayoutManager(
			new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,
				false));

		SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
		users.append(prefs.getString("userid", null));
		//users_number.append(7011+prefs.getString("userid", null));
	}

	public void Add_Members(View view) {
		Intent intent = new Intent(this, AddMembersActivity.class);
		startActivityForResult(intent, REQUEST_CODE_ADD_MEMBER);
	}

	/*public void splitDialog(View view) {
		String Amount = Amount_Edit.getText().toString();
		int no_of_Person = Item_list.size();
		String Specific_Amount;
		if (TextUtils.isEmpty(Amount)) {
			Specific_Amount = "0";
		} else {
			Specific_Amount = "" + Integer.parseInt(Amount) / (Item_list.size() + 1);
		}
		Log.d("splitDialog: ", Item_list.size() + "");
		if (Item_list.size() > 0) {
			Split_List = new ArrayList<>();
			for (int i = 0; i < Item_list.size(); i++) {

				Split_List.add(new Split_Contact_model(Item_list.get(i).getContact_Person_Name(),
					Specific_Amount));
			}
		} else {
			Split_List = new ArrayList<>();
			Split_List.add(new Split_Contact_model("Bharat", Specific_Amount));
		}
		Log.d("splitDialog: ", Split_List.size() + "");
		LayoutInflater LI = LayoutInflater.from(Group_Form.this);
		View PromptsView = LI.inflate(R.layout.split_dialog_layout, null);
		Split_Spinner = (Spinner) PromptsView.findViewById(R.id.Split_Spinner);
		Split_RecyclerView = (RecyclerView) PromptsView.findViewById(R.id.Dialog_RecyclerView);
		splitNotes = (TextView) PromptsView.findViewById(R.id.Dialog_Split_Notes);
		Split_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
		Adapter = new DialogSplitRecyclerViewAdapter(getApplicationContext(), Split_List, "",
			Integer.parseInt(Amount));
		Split_RecyclerView.setAdapter(Adapter);

		Split_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

			}

			@Override public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		android.support.v7.app.AlertDialog.Builder alertDialogBuilder =
			new android.support.v7.app.AlertDialog.Builder(Group_Form.this);
		alertDialogBuilder.setView(PromptsView);
		alertDialogBuilder.setCancelable(false);
		alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override public void onClick(DialogInterface dialog, int which) {

			}
		});
		android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();

		alertDialog.show();
	} */

	@Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_ADD_MEMBER) {
			if (data != null) {
				Contact_RecyclerView.setVisibility(View.VISIBLE);
				List<ContactModel> list = (List<ContactModel>) data.getSerializableExtra("ADDED");
				Item_list.addAll(list);
				RecyclerView_Adapter = new ContactRecyclerViewAdapter(Item_list);
				Contact_RecyclerView.setAdapter(RecyclerView_Adapter);
				for (ContactModel contactModel : list) {
					users.append(",").append(contactModel.getUserId());
					users_number.append(",").append(7011+contactModel.getUserId());
				}
			}
		}
	}

	public boolean Check_Contact_List(String Number) {
		Boolean check = false;
		for (int i = 0; i < Item_list.size(); i++) {
			if (TextUtils.equals(Item_list.get(i).getNumber(), Number)) {
				check = true;
				//return true;
			} else {
				check = false;
			}
		}
		return check;
	}

	/*public void initUser() {
		USERS = "";
		List<ContactModel> itemsList = RecyclerView_Adapter.getList();
		for (int i = 0; i < itemsList.size(); i++) {
			if (i < itemsList.size() - 1) {
				USERS = USERS + itemsList.get(i).getId() + ",";
			} else if (i == (itemsList.size() - 1)) {
				USERS = USERS + itemsList.get(i).getId();
			}
		}
	} */

	public void addGroup(View view) {
		//if (RecyclerView_Adapter != null) {
		//	initUser();
		//}

		GroupModel model = new GroupModel();
		model.setGroupName(
			((EditText) findViewById(R.id.group_name_edit_text)).getText().toString());
		model.setGroupDesc(
			((EditText) findViewById(R.id.group_desc_edit_text)).getText().toString());
		model.setUsers(users.toString());
		//model.setMobile_no(users_number.toString());

		long groupId = mDbHelper.createGroup(model);
		model.setId(groupId);
		model.setTypeId(0);
		new ServerUtil(this).createGroup(model, Item_list);
		Intent intent = new Intent();
		intent.putExtra("group", model);
		Groups.billsAdapter.addItem(model);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}
}
