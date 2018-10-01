package com.zersey.roz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zersey.roz.Data.TransactionDbHelper;
import java.util.ArrayList;
import java.util.List;

public class AddMembersActivity extends AppCompatActivity {
	private List<ContactModel> Contact_List = new ArrayList<>();
	public static ContactRecyclerViewAdapter Add_Member_Adapter;
	public static List<ContactModel> Added_Members;
	TextView Submit;
	public EditText Search_Edit;
	private TextView toolbar_title;
	private int count=0;
	private ImageButton back,Search_Icon,back_White;
	public static Contact_List_RecyclerView_Adapter Adapter;
	private InputMethodManager IMM;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_member);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		IMM=(InputMethodManager)this.getSystemService(Activity.INPUT_METHOD_SERVICE);
		initContactList();
		initRecyclerView();
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		toolbar_title=findViewById(R.id.toolbar_Text);
		toolbar_title.setText("Add Members");
		Submit=(TextView)findViewById(R.id.Add_Member_Submit);
		back_White=findViewById(R.id.back_Button_white);
		back=(ImageButton)findViewById(R.id.back_Button);
		Search_Icon=findViewById(R.id.Search_Icon);
		Search_Edit.setVisibility(View.GONE);
		back.setVisibility(View.GONE);
		Search_Edit.clearFocus();
		Search_Edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//submitBill.setVisibility(View.GONE);
				//back.setVisibility(View.VISIBLE);
			}
		});
		Search_Icon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//submitBill.setVisibility(View.GONE);
				back.setVisibility(View.VISIBLE);
				Search_Edit.setVisibility(View.VISIBLE);
				Search_Icon.setVisibility(View.GONE);
				back_White.setVisibility(View.GONE);
				toolbar_title.setVisibility(View.GONE);
				getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
			}
		});
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Search_Edit.clearFocus();
				toolbar_title.setVisibility(View.VISIBLE);
				toolbar_title.setText("Add Members");
				//submitBill.requestFocus();
				View view=AddMembersActivity.this.getCurrentFocus();
				IMM.hideSoftInputFromWindow(view.getWindowToken(),0);
				//submitBill.setVisibility(View.VISIBLE);
				Search_Edit.setVisibility(View.GONE);
				back.setVisibility(View.GONE);
				Search_Icon.setVisibility(View.VISIBLE);
				back_White.setVisibility(View.VISIBLE);
				getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.colorPrimary)));
			}
		});
		Search_Edit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					//submitBill.setVisibility(View.GONE);
					//back.setVisibility(View.VISIBLE);
				}else {
					//submitBill.setVisibility(View.VISIBLE);
				}
			}
		});
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
		Add_Member_Adapter = new ContactRecyclerViewAdapter(Added_Members);
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

	@Override
	public void onBackPressed() {
		View view= new View(this);
		Add_Members(view);
	}
}
