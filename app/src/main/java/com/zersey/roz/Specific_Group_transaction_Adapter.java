package com.zersey.roz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import java.util.List;

public class Specific_Group_transaction_Adapter
	extends RecyclerView.Adapter<Specific_Group_transaction_Adapter.Specific_ViewHolder> {
	private List<Split_Contact_model> list;
	private Context context;

	Specific_Group_transaction_Adapter(List<Split_Contact_model> list) {
		this.list = list;
	}

	@NonNull @Override
	public Specific_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		context = parent.getContext();
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View view = inflater.inflate(R.layout.specific_recycler_view_layout, parent, false);
		return new Specific_ViewHolder(view);
	}

	@Override public void onBindViewHolder(@NonNull Specific_ViewHolder holder, int position) {
		holder.textView.setText(list.get(position).getContact_Name().getName());
		holder.totalAmountTextView.setText(list.get(position).getSplit_Amount());
		if (SeparateGroupTransaction.class.isInstance(context)) {
			holder.Pay_Button.setText("Amount Due");
		}
	}

	@Override public int getItemCount() {
		return list.size();
	}

	public class Specific_ViewHolder extends RecyclerView.ViewHolder {
		TextView textView, totalAmountTextView;
		Button Pay_Button;

		public Specific_ViewHolder(View itemView) {
			super(itemView);
			textView = itemView.findViewById(R.id.specific_group_Transaction_Name);
			Pay_Button = itemView.findViewById(R.id.Pay_Button);
			totalAmountTextView = itemView.findViewById(R.id.total_amount);
		}
	}
}
