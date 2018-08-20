package android.zersey.expense_manger;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.zersey.expense_manger.Data.TransactionDbHelper;
import java.util.ArrayList;
import java.util.List;

public class Group_Form extends AppCompatActivity {
	private ListView Add_Member_ListView;
	private List<Custom_Contact_items> Item_list;
	private Contact_ListView_Adapter adapter;
	private TransactionDbHelper mDbHelper;
	private String users = "";

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_form);
		mDbHelper = new TransactionDbHelper(this);
		Item_list = new ArrayList<>();
		Add_Member_ListView = findViewById(R.id.Add_Member_ListView);
		adapter =
			new Contact_ListView_Adapter(Group_Form.this, R.layout.contacts_list_layout, Item_list);
		Add_Member_ListView.setAdapter(adapter);

		SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
		users += prefs.getString("userid", null);
	}

	public void Add_Members(View view) {
		Intent intent =
			new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
		startActivityForResult(intent, 0);
	}

	@Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 0 && resultCode == RESULT_OK) {
			//List<String> temp=getIntent().getStringArrayListExtra("Previous_Contacts");
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
						//Log.d("hueh", "onActivityResult: " + name + ", " + code + ", " + cNumber);
					}

					Custom_Contact_items item2 = new Custom_Contact_items(name, cNumber);
					Log.d("onActivityResult: ", Item_list.size() + "");
					Add_Member_ListView.setVisibility(View.VISIBLE);
					Log.d("onActivityResult: ", "Number " + cNumber);
					c.close();
					Custom_Contact_items item =
						new ServerUtil(Group_Form.this).getUserIdFromServer(cNumber, code);
					Item_list.add(item2);
					users += "," + item.getId();
					adapter.notifyDataSetChanged();
				}
			}
		}
	}

	public void addGroup(View view) {
		GroupModel model = new GroupModel();
		model.setGroupName(
			((EditText) findViewById(R.id.group_name_edit_text)).getText().toString());
		model.setGroupDesc(
			((EditText) findViewById(R.id.group_desc_edit_text)).getText().toString());
		model.setUsers(users);
		//mDbHelper.createGroup(model);
		Intent intent = new Intent();
		intent.putExtra("group", model);
		Log.d("hueh", "addGroup: " + model.toString());
		setResult(Activity.RESULT_OK, intent);
		finish();
	}
}
