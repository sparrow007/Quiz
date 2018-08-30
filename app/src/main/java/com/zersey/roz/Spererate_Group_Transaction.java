package com.zersey.roz;

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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Spererate_Group_Transaction extends AppCompatActivity {
    AppBarLayout mAppBarLayout;
    private List<String> list;
    private RecyclerView Specific_recyclerView;
    private Toolbar toolbar;
    private TextView DATE,AMOUNT,TITLE;
    private String Amount,Date,Title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spererate_group_transaction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        list=new ArrayList<>();
        list.add("Bharat");
        list.add("Amrit");
        list.add("Rahul");
        mAppBarLayout = findViewById(R.id.appbar);
        DATE=(TextView)findViewById(R.id.Transaction_DATE_TextView);
        AMOUNT=(TextView)findViewById(R.id.Transaction_AMOUNT_TextView);
        TITLE=(TextView)findViewById(R.id.Transaction_toolbar_TextView);
        Amount=getIntent().getStringExtra("Amount");
        Title=getIntent().getStringExtra("Title");
        Date=getIntent().getStringExtra("DateCreated");
        if(!TextUtils.isEmpty(Amount) && !TextUtils.isEmpty(Date) && !TextUtils.isEmpty(Title) ) {
            AMOUNT.setText("Rs " + Amount);
            DATE.setText(Date);
            TITLE.setText(Title);
        }
        initRecyclerView();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void initRecyclerView(){
        Specific_recyclerView=new RecyclerView(this);
        Specific_recyclerView=findViewById(R.id.Separate_Recycler_View);
        Specific_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Specific_recyclerView.setAdapter(new Specific_Group_transaction_Adapter(list));
    }
}
