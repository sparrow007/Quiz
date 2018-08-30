package com.zersey.roz;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Contact_List_RecyclerView_Adapter extends RecyclerView.Adapter<Contact_List_RecyclerView_Adapter.ListViewHolder>{
private List<ContactModel> list;
private Add_Members_Activity Members;
public Contact_List_RecyclerView_Adapter(List<ContactModel> list){
    this.list=list;
}

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.contacts_list_layout,parent,false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, final int position) {
     holder.Name.setText(list.get(position).getName());
     holder.Name.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Custom_Contact_items items=new Custom_Contact_items();
             items.setContact_Person_Name(list.get(position).getName());
             Members.Add_Member_Adapter.add(items);
         }
     });
     holder.Number.setText(list.get(position).getNumber());
     
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder{
    TextView Name,Number;
        public ListViewHolder(View itemView) {
            super(itemView);
            Name=(TextView)itemView.findViewById(R.id.Contact_Name_TextView);
            Number=(TextView)itemView.findViewById(R.id.Contact_Number_TextView);
        }
    }
}
