package com.zersey.roz;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zersey.roz.Data.TransactionDbHelper;
import java.util.ArrayList;
import java.util.List;

public class Group_Transaction_Adapter
	extends RecyclerView.Adapter<Group_Transaction_Adapter.TransactionViewHolder> {
	private List<IncomeModel> list, temp_list, Permanent_list;
	private List<ContactModel> ContactList;
	private TransactionDbHelper mdbHelper;
	private String userId;
	private int noOfMembers ;

	public Group_Transaction_Adapter(List<IncomeModel> list, String userId) {
		ContactList = new ArrayList<>();
		this.list = list;
		this.Permanent_list = list;
		this.userId = userId;
	}

	@NonNull @Override
	public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		mdbHelper = TransactionDbHelper.getInstance(parent.getContext());
		//ContactList.addAll(mdbHelper.getContacts());
		View view = inflater.inflate(R.layout.group_transactions_layout, parent, false);

		return new Group_Transaction_Adapter.TransactionViewHolder(view);
	}

	@Override public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
		holder.Transaction_TextView.setText(list.get(position).getTitle());
		String[] amounts = list.get(position).getTotalAmount().split(",");


		double sum = 0;
		for (String s : amounts) {
			try {
				sum += Double.parseDouble(s);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

		if (list.get(position).getPayerId() != null) {
			if(list.get(position).getPayerId().split(",").length>0) {
				noOfMembers = list.get(position).getPayerId().split(",").length;
			}else {
				noOfMembers=Integer.parseInt(list.get(position).getPayerId());
			}
			if (userId.equals(list.get(position).getPayerId().split(",")[0])) {
				holder.Paid.setText(String.format("%s paid Rs %s", "You", sum));
				if(noOfMembers>1){
					//--noOfMembers;
					holder.groupTransactionAmount.setText("Rs " + (sum/noOfMembers)*(noOfMembers-1));
				}else {holder.groupTransactionAmount.setText("Rs " + 0.0);}

			} else {
				List<ContactModel> contactList =
					mdbHelper.getUserWithUserId(list.get(position).getPayerId().split(","));
				if (contactList.size() > 0) {
					holder.Paid.setText(
						String.format("%s paid Rs %s", contactList.get(0).getName(), sum));
					if(noOfMembers>1){
						--noOfMembers;
						holder.groupTransactionAmount.setText("Rs " + (sum/noOfMembers)*(noOfMembers-1));
					}else {holder.groupTransactionAmount.setText("Rs " + 0.0);}
				}
			}
		} else {
			holder.Paid.setText(" PayerID: null ");
			holder.groupTransactionAmount.setText("Rs " + 0.0);
		}
	}

	public void addItem(IncomeModel model) {
		list.add(model);
		notifyItemInserted(list.size() - 1);
	}

	public void Search(String str) {
		temp_list = new ArrayList<>();

		if (!TextUtils.isEmpty(str)) {
			for (int i = 0; i < Permanent_list.size(); i++) {
				if (Permanent_list.get(i).getTitle().toLowerCase().contains(str.toLowerCase())) {
					temp_list.add(Permanent_list.get(i));
				}
			}

			if (temp_list.size() > 0) {
				list = temp_list;
			} else {
				list = new ArrayList<>();
			}
		} else {
			//list=new ArrayList<>();
			list = Permanent_list;
		}

		notifyDataSetChanged();
	}

	@Override public int getItemCount() {
		return list.size();
	}

	public class TransactionViewHolder extends RecyclerView.ViewHolder {
		TextView Transaction_TextView, groupTransactionAmount, Paid;

		public TransactionViewHolder(View itemView) {
			super(itemView);
			Transaction_TextView = itemView.findViewById(R.id.Group_Transaction_TextView);
			groupTransactionAmount = itemView.findViewById(R.id.Group_Transaction_Amount_TextView);
			Paid = itemView.findViewById(R.id.Group_Transaction2_TextView);
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View v) {
					Intent intent = new Intent(v.getContext(), SeparateGroupTransaction.class);
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
