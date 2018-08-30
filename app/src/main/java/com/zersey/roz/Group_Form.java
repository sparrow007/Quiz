package com.zersey.roz;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.zersey.roz.Data.TransactionDbHelper;
import java.util.ArrayList;
import java.util.List;

public class Group_Form extends AppCompatActivity implements UserIdInterface {
	private LinearLayout Add_Member_Layout;
	private ListView Add_Member_ListView;
	private List<Custom_Contact_items> Item_list;
	private List<Split_Contact_model> Split_List;
	private Spinner Split_Spinner;
	private TextView Split_Notes;
	private RecyclerView Split_RecyclerView;
	private RecyclerView Contact_RecyclerView;
	private Dialog_Split_RecyclerViewAdapter Adapter;
	private EditText Amount_Edit, Description_Edit, Group_Name_Edit;
	private String users = "",USERS="No Members";
	private Contact_RecyclerView_Adapter RecyclerView_Adapter;
	private TransactionDbHelper mDbHelper;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_form);
		Item_list = new ArrayList<>();
		//Split_List = new ArrayList<>();
		//Amount_Edit = (EditText) findViewById(R.id.Group_Amount_Edit);
		Description_Edit = (EditText) findViewById(R.id.group_desc_edit_text);
		Group_Name_Edit = (EditText) findViewById(R.id.group_name_edit_text);
		//Split_RecyclerView = new RecyclerView(this);
		Contact_RecyclerView=new RecyclerView(this);
		Contact_RecyclerView = (RecyclerView) findViewById(R.id.Add_Member_Group_Form_RecyclerView);
		Contact_RecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));


		mDbHelper = TransactionDbHelper.getInstance(this);

		SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
		users += prefs.getString("userid", null);
	}

	public void Add_Members(View view) {
		Intent intent =
			new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
		startActivityForResult(intent, 0);
	}

	public void Dialog(View view) {
		String Amount = Amount_Edit.getText().toString();
		int no_of_Person = Item_list.size();
		String Specific_Amount;
		if (TextUtils.isEmpty(Amount)) {
			Specific_Amount = "0";
		} else {
			Specific_Amount = "" + Integer.parseInt(Amount) / no_of_Person;
		}
		Log.d("Dialog: ", Item_list.size() + "");
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
		Log.d("Dialog: ", Split_List.size() + "");
		LayoutInflater LI = LayoutInflater.from(Group_Form.this);
		View PromptsView = LI.inflate(R.layout.split_dialog_layout, null);
		Split_Spinner = (Spinner) PromptsView.findViewById(R.id.Split_Spinner);
		Split_RecyclerView = (RecyclerView) PromptsView.findViewById(R.id.Dialog_RecyclerView);
		Split_Notes = (TextView) PromptsView.findViewById(R.id.Dialog_Split_Notes);
		Split_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
		Adapter = new Dialog_Split_RecyclerViewAdapter(getApplicationContext(), Split_List);
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
	}

	@Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0 && resultCode == RESULT_OK) {
			if (data != null) {
				Uri contactData = data.getData();
				Cursor c = getContentResolver().query(contactData, null, null, null, null);
				if (c != null && c.moveToFirst()) {
					String cNumber = null, code = null;
					final String name = c.getString(
						c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
					//String id = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
					String hasPhone = c.getString(
						c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER));
					if (hasPhone.equalsIgnoreCase("1")) {
						//Cursor phones = getContentResolver().query(
						//	ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
						//	ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null, null);
						//phones.moveToFirst();
						//cNumber = phones.getString(phones.getColumnIndex("data1"));
						////System.out.println("number is:"+cNumber);

						cNumber = c.getString(
							c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						cNumber = cNumber.replace(" ", "").replace("+", "");
						if (cNumber.length() > 10) {
							code = cNumber.substring(0, cNumber.length() - 10);
							cNumber = cNumber.substring(cNumber.length() - 10);
						} else {
							code = "91";
						}
					}

					Custom_Contact_items item2 = new Custom_Contact_items(name, cNumber);
					if (!Check_Contact_List(cNumber)){
						Item_list.add(item2);
						Log.d("onActivityResult: ",Item_list.size()+"");
						Contact_RecyclerView.setVisibility(View.VISIBLE);
						RecyclerView_Adapter=new Contact_RecyclerView_Adapter(Item_list);
						Contact_RecyclerView.setAdapter(RecyclerView_Adapter);
						Log.d("onActivityResult: ", cNumber+"  "+Contact_RecyclerView.getChildCount());
					}else {
						Toast.makeText(Group_Form.this,"Contact is already added",Toast.LENGTH_LONG).show();
					}
					RecyclerView_Adapter.notifyDataSetChanged();
				}
			}
		}
	}

	public boolean Check_Contact_List(String Number){
		Boolean check=false;
		for(int i=0;i<Item_list.size();i++){
			if(TextUtils.equals(Item_list.get(i).getContact_Person_Number(),Number)){
				check=true;
				//return true;
			}else {
				check=false;
			}
		}
		return check;
	}
public void initUser(){
		USERS="";
		List<Custom_Contact_items> itemsList=RecyclerView_Adapter.getList();
		for(int i=0;i<itemsList.size();i++){
			if (i<itemsList.size()-1){
			USERS=USERS+itemsList.get(i).getContact_Person_Name()+",";
			}else if (i==(itemsList.size()-1)){
				USERS=USERS+itemsList.get(i).getContact_Person_Name();
			}
		}
}
	public void addGroup(View view) {
		if(RecyclerView_Adapter!=null){
		initUser();}
		GroupModel model = new GroupModel();
		model.setGroupName(
			((EditText) findViewById(R.id.group_name_edit_text)).getText().toString());
		model.setGroupDesc(
			((EditText) findViewById(R.id.group_desc_edit_text)).getText().toString());
		model.setUsers(USERS);
		long groupId = mDbHelper.createGroup(model);
		model.setId(groupId);
		model.setTypeId(0);
		new ServerUtil(this).createGroup(model);
		Intent intent = new Intent();
		intent.putExtra("group", model);
		Groups.ADAPTER.addItem(model);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	@Override public void updateUserId(String userId) {
		users += "," + userId;
	}
}
