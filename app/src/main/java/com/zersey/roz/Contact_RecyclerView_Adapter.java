package com.zersey.roz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;


public class Contact_RecyclerView_Adapter extends RecyclerView.Adapter<Contact_RecyclerView_Adapter.ContactHolder> {
private List<Custom_Contact_items> list;
private Context context;

    public Contact_RecyclerView_Adapter(List<Custom_Contact_items> list){
        this.list=list;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.contact_recyclerview_layout,parent,false);
        return new ContactHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, final int position) {
    holder.Cross_Button.bringToFront();
    holder.Cross_Button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            removeAt(position);
        }
    });
    String[] Initials=list.get(position).getContact_Person_Name().split(" ");
    if(Initials.length>1)
    { holder.Initials.setText(Initials[0].substring(0,1)+Initials[1].substring(0,1));
    }else {holder.Initials.setText(Initials[0].substring(0,1));}

    holder.Number.setText(Initials[0]);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void removeAt(int position) {
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
    }

    public class ContactHolder extends RecyclerView.ViewHolder{
    TextView Initials;
    TextView Number;
    ImageButton Cross_Button;
    public ContactHolder(View itemView) {
        super(itemView);
        Cross_Button=(ImageButton)itemView.findViewById(R.id.Cross_Button);
        Cross_Button.bringToFront();
        Initials=(TextView)itemView.findViewById(R.id.Contact_RecyclerView_Initials);
        Number=(TextView)itemView.findViewById(R.id.Contact_RecyclerView_Number);
    }
}
}
