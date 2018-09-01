package com.zersey.roz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.zersey.roz.Data.TransactionDbHelper;

import java.util.ArrayList;
import java.util.List;

public class Add_Members_Activity extends AppCompatActivity {
private List<ContactModel> Contact_List;
private TransactionDbHelper Instance;
private RecyclerView Contact_List_RecyclerView;
private RecyclerView Add_Member_Horizontal_RecyclerView;
public static Contact_RecyclerView_Adapter Add_Member_Adapter;
public static List<Custom_Contact_items> Added_Members;
public static Contact_List_RecyclerView_Adapter Adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initContactList();
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
    private void initContactList(){

        Instance=TransactionDbHelper.getInstance(this);
        Contact_List=Instance.getContacts();
        Log.d("initContactList: ",Contact_List.size()+"");
    }

    public void initRecyclerView(){
        Added_Members=new ArrayList<>();
        /*Custom_Contact_items items=new Custom_Contact_items();
        items.setContact_Person_Name("Add Members");
        Added_Members.add(items);*/
        Contact_List_RecyclerView=new RecyclerView(this);
        Contact_List_RecyclerView=(RecyclerView)findViewById(R.id.Contacts_List_RecyclerView);
        Contact_List_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Adapter=new Contact_List_RecyclerView_Adapter(Contact_List);
        Contact_List_RecyclerView.setAdapter(Adapter);
        Add_Member_Horizontal_RecyclerView = new RecyclerView(this);
        Add_Member_Horizontal_RecyclerView = (RecyclerView) findViewById(R.id.ADD_Members_RecyclerView);
        Add_Member_Horizontal_RecyclerView.setLayoutManager(
                new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL,
                        false));
        Add_Member_Adapter=new Contact_RecyclerView_Adapter(Added_Members);
        Add_Member_Horizontal_RecyclerView.setAdapter(Add_Member_Adapter);
    }

    public void Add_Members(View view){
        Intent intent=new Intent();
        //Bundle bundle=new Bundle();
Added_Members=Add_Member_Adapter.getList();
        Log.d( "onActivityResult: ",Added_Members.size()+"");
        //Added_Members.addAll(Add_Member_Adapter.getList());
        intent.putExtra("ADDED", (ArrayList<Custom_Contact_items>) Added_Members);
        setResult(Activity.RESULT_OK,intent);
        finish();
     //Add_Member_Adapter.getList();
    }

}
