package com.zersey.roz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.zersey.roz.Data.TransactionDbHelper;
import java.util.ArrayList;
import java.util.List;

public class More_Activity extends AppCompatActivity {
private RecyclerView More_RecyclerView;
private List<GroupModel> More_List,More2_List;
    private List<IncomeModel> Item_list;
    private TransactionDbHelper mDbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        More_List=new ArrayList<>();
        More2_List=new ArrayList<>();
        More_RecyclerView=new RecyclerView(this);
        split((List<GroupModel>)getIntent().getSerializableExtra("More"));
        mDbHelper = TransactionDbHelper.getInstance(this);
        Item_list = new ArrayList<>();
        Item_list.addAll(mDbHelper.getAllEntries());
        initRecyclerView();

    }

    public void initRecyclerView(){
        More_RecyclerView=(RecyclerView)findViewById(R.id.More_RecyclerView);
        More_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //More_RecyclerView.setAdapter(new More_RecyclerViewAdapter(this,More_List,More2_List));
        More_RecyclerView.setAdapter(new Transaction_MoreAdapter(Item_list));

    }


    public void split(List<GroupModel> list){
        Log.d( "onClick: ",list.size()+"");
        for(int i=0;i<list.size();i++){
            if(i==0 || i/2==0){
                More_List.add(list.get(i));
            }else {  More2_List.add(list.get(i));}
        }
    }
}