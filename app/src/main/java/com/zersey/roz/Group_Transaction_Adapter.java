package com.zersey.roz;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Group_Transaction_Adapter extends RecyclerView.Adapter<Group_Transaction_Adapter.TransactionViewHolder> {
    private List<IncomeModel> list;
    public Group_Transaction_Adapter(List<IncomeModel> list){
        this.list=list;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.group_transactions_layout,parent,false);
        return new Group_Transaction_Adapter.TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
		Log.d("hueh", "onBindViewHolder: " + list.get(position).toString());
    holder.Transaction_TextView.setText(list.get(position).getTitle());
    holder.groupTransactionAmount.setText("Rs " + list.get(position).getTotalAmount());

    }

    public void addItem(IncomeModel model){
    	list.add(model);
    	notifyItemInserted(list.size() - 1);
	}

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder{
        TextView Transaction_TextView, groupTransactionAmount;
        public TransactionViewHolder(View itemView) {
            super(itemView);
            Transaction_TextView=(TextView)itemView.findViewById(R.id.Group_Transaction_TextView);
            groupTransactionAmount = itemView.findViewById(R.id.Group_Transaction_Amount_TextView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),Spererate_Group_Transaction.class);
                intent.putExtra("pos", getAdapterPosition());
                intent.putExtra("_ID", list.get(getAdapterPosition()).getGroupId());
                intent.putExtra("CardClicked", "Yes");
                intent.putExtra("Title", list.get(getAdapterPosition()).getTitle());
                intent.putExtra("Type", list.get(getAdapterPosition()).getType());
                intent.putExtra("Category", list.get(getAdapterPosition()).getCatId());
                intent.putExtra("Amount", list.get(getAdapterPosition()).getTotalAmount());
                intent.putExtra("DateCreated", list.get(getAdapterPosition()).getPaidAtDate());
                intent.putExtra("model", list.get(getAdapterPosition()));
                v.getContext().startActivity(intent);
            }
        });
        }

    }
}
