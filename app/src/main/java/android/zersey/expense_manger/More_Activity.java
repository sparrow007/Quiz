package android.zersey.expense_manger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class More_Activity extends AppCompatActivity {
private RecyclerView More_RecyclerView;
private List<String> More_List,More2_List;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        More_List=new ArrayList<>();
        More2_List=new ArrayList<>();
        More_RecyclerView=new RecyclerView(this);
        split(getIntent().getStringArrayListExtra("More"));
        initRecyclerView();

    }

    public void initRecyclerView(){
        More_RecyclerView=(RecyclerView)findViewById(R.id.More_RecyclerView);
        More_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
        More_RecyclerView.setAdapter(new More_RecyclerViewAdapter(this,More_List,More2_List));
    }


    public void split(List<String> list){
        Log.d( "onClick: ",list.size()+"");
        for(int i=0;i<list.size();i++){
            if(i==0 || i/2==0){
                More_List.add(list.get(i));
            }else {  More2_List.add(list.get(i));}
        }
    }
}
