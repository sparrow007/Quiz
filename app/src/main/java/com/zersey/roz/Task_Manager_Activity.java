package com.zersey.roz;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class Task_Manager_Activity extends AppCompatActivity {
public static Task_Adapter adapter;
private List<Task_Model> Task_list;
private RecyclerView Task_RecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_manager);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initTaskList();
        initRecyclerView();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent=new Intent(Task_Manager_Activity.this,Task_Form_Activity.class);
                startActivityForResult(intent,1);
            }
        });
    }

    public void initTaskList(){
        Task_list=new ArrayList<>();
        for (int i=0;i<10;i++){
        Task_list.add(new Task_Model("New Task","New Description",false));
        }
    }

    public void initRecyclerView(){
        Task_RecyclerView=new RecyclerView(this);
        Task_RecyclerView=(RecyclerView) findViewById(R.id.Task_Recycler_View);
        Task_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new Task_Adapter(Task_list);
        Task_RecyclerView.setAdapter(adapter);
    }


    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            adapter.add(new Task_Model(data.getStringExtra("Title").toString(),data.getStringExtra("Des").toString(),false));
            //adapter.notifyDataSetChanged();
        }
    }
}
