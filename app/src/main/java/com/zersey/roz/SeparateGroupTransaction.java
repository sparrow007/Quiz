package com.zersey.roz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.zersey.roz.Data.TransactionDbHelper;
import java.util.ArrayList;
import java.util.List;

public class SeparateGroupTransaction extends AppCompatActivity {
	AppBarLayout mAppBarLayout;
	private RecyclerView Specific_recyclerView;
	private Toolbar toolbar;
	private TextView DATE, AMOUNT, TITLE;
	private ImageButton Settle_Button;
	private String Amount, Date, Title;
	private IncomeModel model;
	private TransactionDbHelper mDbHelper;
	private List<ContactModel> payers;
	private List<Split_Contact_model> splitList;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_spererate_group_transaction);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		model = (IncomeModel) getIntent().getSerializableExtra("model");
		mDbHelper = TransactionDbHelper.getInstance(this);
		splitList = new ArrayList<>();

		String[] users = model.getPayerId().split(",");
		String[] totalAmounts = model.getTotalAmount().split(",");
		String[] amountsDue = model.getAmountDue().split(",");

		SharedPreferences prefs = getSharedPreferences("login", Context.MODE_PRIVATE);

		for (int i = 0; i < users.length; i++) {
			if (users[i].equals(prefs.getString("userid", null))) {
				ContactModel m = new ContactModel();
				m.setName("You");
				m.setUserId(Long.parseLong(prefs.getString("userid", null)));
				m.setNumber(prefs.getString("phone", null));
				splitList.add(new Split_Contact_model(m, totalAmounts[i]));
			} else {
				splitList.add(new Split_Contact_model(
					mDbHelper.getUserWithUserId(new String[] { users[i] }).get(0),
					totalAmounts[i]));
			}
		}

		//payers.addAll(mDbHelper.getUserWithUserId(users));

		mAppBarLayout = findViewById(R.id.appbar);
		DATE = findViewById(R.id.Transaction_DATE_TextView);
		AMOUNT = findViewById(R.id.Transaction_AMOUNT_TextView);
		TITLE = findViewById(R.id.Transaction_toolbar_TextView);
		Settle_Button = findViewById(R.id.Transaction_Settle_Button);
		Amount = getIntent().getStringExtra("Amount");
		Title = getIntent().getStringExtra("Title");
		Date = getIntent().getStringExtra("DateCreated");

		if (!TextUtils.isEmpty(Amount) && !TextUtils.isEmpty(Date) && !TextUtils.isEmpty(Title)) {
			double sum = 0;
			for (String s : totalAmounts) {
				sum += Double.parseDouble(s);
			}
			AMOUNT.setText("Rs " + sum);
			DATE.setText(Date);
			TITLE.setText(Title);
		}
		initRecyclerView();
		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View view) {
				Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
					.setAction("Action", null)
					.show();
			}
		});

		Settle_Button.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {

				Intent i = new Intent(getApplicationContext(), MainActivity.class);
				//i.putExtra("pos", getAdapterPosition());
				//i.putExtra("_ID", Updated_Id);
				i.putExtra("CardClicked", "Yes");
				i.putExtra("Title", Title);
				//i.putExtra("Type", Updated_Type);
				//i.putExtra("Category", Updated_Category);
				i.putExtra("Amount", Amount);
				i.putExtra("DateCreated", Date);
				//i.putExtra("model", model);
				startActivity(i);
			}
		});
	}

	public void initRecyclerView() {
		Specific_recyclerView = findViewById(R.id.Separate_Recycler_View);
		Specific_recyclerView.setLayoutManager(new LinearLayoutManager(this));
		Specific_recyclerView.setAdapter(new Specific_Group_transaction_Adapter(splitList));
	}
}