package com.zersey.roz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Paid_RecyclerView_Adapter extends RecyclerView.Adapter<Paid_RecyclerView_Adapter.RecyclerView_Holder>{
    Context context;
    List<ContactModel> list;

    Paid_RecyclerView_Adapter(Context context, List<ContactModel> list){
     this.context=context;
     this.list=list;
    }

    @NonNull
    @Override
    public RecyclerView_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.paid_by_members_name_layout,parent,false);
        return new RecyclerView_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView_Holder holder, int position) {
    holder.Paid_Name.setText(list.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RecyclerView_Holder extends RecyclerView.ViewHolder{
        TextView Paid_Name;
        public RecyclerView_Holder(View itemView) {
            super(itemView);
            Paid_Name=itemView.findViewById(R.id.Paid_By_Name);
            Paid_Name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity.Paid_TextView.setText(list.get(getAdapterPosition()).getName());
                    MainActivity.Paid_Dialog.dismiss();
                }
            });
        }
    }
}
