package com.zersey.roz;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Group_About_Adapter extends RecyclerView.Adapter<Group_About_Adapter.AboutViewHolder> {
private List<String> list,list2;
public Group_About_Adapter(List<String> list,List<String> list2){
    this.list=list;
    this.list2=list2;
}

    @NonNull
    @Override
    public AboutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.group_about_layout,parent,false);
        return new AboutViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AboutViewHolder holder, int position) {
      holder.About_TextView.setText(list.get(position));
      holder.textView2.setText(" ");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Boolean CheckItem(String Name){
        for(int i=0;i<list.size();i++){
            if (TextUtils.equals(list.get(i).toString(),Name)){
                return true;
            }
        }
        return false;
    }

    public class AboutViewHolder extends RecyclerView.ViewHolder{
   TextView About_TextView,textView2;
     public AboutViewHolder(View itemView) {
         super(itemView);
         About_TextView=(TextView)itemView.findViewById(R.id.Group_About_TextView);
         textView2=(TextView)itemView.findViewById(R.id.Group_About2_TextView);
     }
 }
}
