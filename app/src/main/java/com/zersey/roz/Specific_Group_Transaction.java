package com.zersey.roz;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Specific_Group_Transaction extends AppCompatActivity {
    AppBarLayout mAppBarLayout;
    private List<String> list;
    private RecyclerView Specific_recyclerView;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_group_transaction);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        list=new ArrayList<>();
        list.add("Bharat");
        list.add("Amrit");
        list.add("Rahul");
        mAppBarLayout = findViewById(R.id.appbar);
        //toolbar.setTitle("Big Gift");
        //toolbar.setSubtitle("20 Aug");
        //getSupportActionBar().setDisplayShowTitleEnabled(true   );
        //setSupportActionBar(toolbar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                /*if (verticalOffset == 0) {
                    getSupportActionBar().setTitle(" ");
                    Toast.makeText(getApplicationContext(),"Expanded",Toast.LENGTH_LONG).show();
                }*
                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0) {
                    getSupportActionBar().setTitle("Gift");
                    Toast.makeText(getApplicationContext(),"Collapsed",Toast.LENGTH_LONG).show();
                } else {
                    getSupportActionBar().setTitle(" ");
                    Toast.makeText(getApplicationContext(),"Expanded",Toast.LENGTH_LONG).show();
                    //getSupportActionBar().setTitle(" ");
                    //Toast.makeText(getApplicationContext(),"Idle",Toast.LENGTH_LONG).show();
                }*/

            }
        });
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
        Specific_recyclerView=findViewById(R.id.Specific_Recycler_View);
        Specific_recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Specific_recyclerView.setAdapter(new Specific_Group_transaction_Adapter(list));
    }

}
