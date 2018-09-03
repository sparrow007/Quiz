package com.zersey.roz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.zersey.roz.Data.TransactionDbHelper;
import java.util.ArrayList;
import java.util.List;

public class Add_Members_Activity extends AppCompatActivity {
	private List<ContactModel> Contact_List = new ArrayList<>();
	public static Contact_RecyclerView_Adapter Add_Member_Adapter;
	public static List<ContactModel> Added_Members;
	public EditText Search_Edit;
	private int count=0;
	public static Contact_List_RecyclerView_Adapter Adapter;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_member);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		initContactList();
		initRecyclerView();

		Search_Edit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
                Adapter.Search(s.toString());
			}

			@Override
			public void afterTextChanged(Editable s) {
                Adapter.Search(s.toString());
			}
		});

	}

	private void initContactList() {
		TransactionDbHelper instance = TransactionDbHelper.getInstance(this);
		Contact_List.addAll(instance.getContacts());
		Search_Edit=findViewById(R.id.search_view);

		for (int i=0;i<Contact_List.size();i++){
			if (Contact_List.get(i).getUserId()!=0){
             count++;
			}
		}
		Log.d("initContactList: ",count+"");
	}

	public void initRecyclerView() {
		Added_Members = new ArrayList<>();
		RecyclerView contactListRecyclerView = findViewById(R.id.Contacts_List_RecyclerView);
		contactListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		Adapter = new Contact_List_RecyclerView_Adapter(Contact_List,count);
		contactListRecyclerView.setAdapter(Adapter);


		RecyclerView addedMembersRecyclerView = findViewById(R.id.ADD_Members_RecyclerView);
		addedMembersRecyclerView.setLayoutManager(
			new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
		Add_Member_Adapter = new Contact_RecyclerView_Adapter(Added_Members);
		addedMembersRecyclerView.setAdapter(Add_Member_Adapter);
//		contactListRecyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL),1);
	}

	public void Add_Members(View view) {
		Intent intent = new Intent();
		Added_Members = Add_Member_Adapter.getList();
		intent.putExtra("ADDED", (ArrayList<ContactModel>) Added_Members);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}
}
