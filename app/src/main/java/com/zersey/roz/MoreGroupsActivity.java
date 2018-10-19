package com.zersey.roz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class MoreGroupsActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
		//noinspection unchecked
		List<GroupModel> groupList = (List<GroupModel>) getIntent().getSerializableExtra("groupList");
		RecyclerView groupListView = findViewById(R.id.More_RecyclerView);
		groupListView.setLayoutManager(new LinearLayoutManager(this));
		groupListView.setHasFixedSize(true);
		groupListView.setAdapter(new GroupMoreAdapter(groupList, MoreGroupsActivity.this));

	}

}
