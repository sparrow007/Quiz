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
import java.util.Locale;

public class GroupTransactionAdapter
		extends RecyclerView.Adapter<GroupTransactionAdapter.TransactionViewHolder> {
	private List<BillModel> list, temp_list, Permanent_list;
	private List<ContactModel> ContactList;
	private TransactionDbHelper mdbHelper;
	private String userId;
	private int noOfMembers;

	public GroupTransactionAdapter(List<BillModel> list, String userId) {
		ContactList = new ArrayList<>();
		this.list = list;
		this.Permanent_list = list;
		this.userId = userId;
	}

	@NonNull
	@Override
	public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		mdbHelper = TransactionDbHelper.getInstance(parent.getContext());
		View view = inflater.inflate(R.layout.group_transactions_layout, parent, false);

		return new GroupTransactionAdapter.TransactionViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
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
			if (list.get(position).getPayerId().split(",").length > 0) {
				noOfMembers = list.get(position).getPayerId().split(",").length;
			} else {
				noOfMembers = Integer.parseInt(list.get(position).getPayerId());
			}
			if (userId.equals(list.get(position).getPayerId().split(",")[0])) {
				holder.Paid.setText(String.format("%s paid Rs %s", "You", sum));
				if (noOfMembers > 1) {
					//--noOfMembers;
					holder.groupTransactionAmount.setText(String.format(Locale.getDefault(), "Rs %.2f", (sum /
							noOfMembers) * (noOfMembers - 1)));
				} else {
					holder.groupTransactionAmount.setText("Rs " + 0.00);
				}

			} else {
				List<ContactModel> contactList =
						mdbHelper.getUserWithUserId(list.get(position).getPayerId().split(","));
				if (contactList.size() > 0) {
					holder.Paid.setText(
							String.format("%s paid Rs %s", contactList.get(0).getName(), sum));
					if (noOfMembers > 1) {
						--noOfMembers;
						holder.groupTransactionAmount.setText(String.format(Locale.getDefault(), "Rs %.2f", (sum /
								noOfMembers) * (noOfMembers - 1)));
					} else {
						holder.groupTransactionAmount.setText("Rs " + 0.00);
					}
				}
			}
		} else {
			holder.Paid.setText(" PayerID: null ");
			holder.groupTransactionAmount.setText("Rs " + 0.0);
		}
	}

	public void addItem(BillModel model) {
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

	@Override
	public int getItemCount() {
		return list.size();
	}

	public class TransactionViewHolder extends RecyclerView.ViewHolder {
		TextView Transaction_TextView, groupTransactionAmount, Paid;

		public TransactionViewHolder(View itemView) {
			super(itemView);
			Transaction_TextView = itemView.findViewById(R.id.group_transaction_text_view);
			groupTransactionAmount = itemView.findViewById(R.id.group_transaction_amount_text_view);
			Paid = itemView.findViewById(R.id.group_trans_desc_text_view);
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(v.getContext(), SeparateGroupTransaction.class);
					intent.putExtra("pos", getAdapterPosition());
					intent.putExtra("_ID", list.get(getAdapterPosition()).getGroupId());
					intent.putExtra("CardClicked", "Yes");
					intent.putExtra("title", list.get(getAdapterPosition()).getTitle());
					intent.putExtra("Type", list.get(getAdapterPosition()).getType());
					intent.putExtra("category", list.get(getAdapterPosition()).getCatId());
					intent.putExtra("amount", list.get(getAdapterPosition()).getTotalAmount());
					intent.putExtra("DateCreated", list.get(getAdapterPosition()).getPaidAtDate());
					intent.putExtra("model", list.get(getAdapterPosition()));
					v.getContext().startActivity(intent);
				}
			});
		}
	}
}
