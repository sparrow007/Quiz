package com.zersey.roz;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GroupTransactionAdapter
		extends RecyclerView.Adapter<GroupTransactionAdapter.TransactionViewHolder> {
	private List<BillModel> list;
	private List<BillModel> permanentList;
	private String userId;
	private GroupModel groupModel;

	public GroupTransactionAdapter(List<BillModel> list, String userId, GroupModel groupModel) {
		this.groupModel = groupModel;
		this.list = list;
		this.permanentList = list;
		this.userId = userId;
	}

	@NonNull
	@Override
	public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View view = inflater.inflate(R.layout.group_transactions_layout, parent, false);
		return new TransactionViewHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
		holder.groupTransactionTitle.setText(list.get(position).getTitle());
		String[] amounts = list.get(position).getTotalAmount().split(",");

		double sum = 0;
		for (String s : amounts) {
			try {
				sum += Double.parseDouble(s);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}

		if (!Util.isEmpty(list.get(position).getPayerId())) {

			String[] payerIds = list.get(position).getPayerId().split(",");
			int noOfMembers = payerIds.length;

			String[] fullNames = groupModel.getFullname().split(",");
			String[] phones = groupModel.getMobile_no().split(",");
			String[] groupMembers = groupModel.getUsers().split(",");

			Log.d(GroupTransactionAdapter.class.getSimpleName(), "onBindViewHolder: " + groupModel
					.getFullname() + " # " + groupModel.getMobile_no());

			Map<String, String> nameFinder = new HashMap<>();
			Map<String, String> phoneFinder = new HashMap<>();
			for (int i = 0; i < groupMembers.length; i++) {
				nameFinder.put(groupMembers[i], fullNames[i]);
				phoneFinder.put(groupMembers[i], phones[i]);
			}

			holder.groupTransactionAmount.setText(String.format("Rs %s", 0.0));

			if (userId.equals(payerIds[0])) {
				holder.groupTransactionDescription.setText(String.format("%s paid Rs %s", "You",
						sum));
				holder.groupTransactionAmount.setText(String.format(Locale.getDefault(), "Rs " +
						"%.2f", (sum / noOfMembers) * (noOfMembers - 1)));
			} else {
				holder.groupTransactionDescription.setText(String.format("%s paid Rs %s",
						nameFinder.get(payerIds[0]),
						sum));

				if (Arrays.asList(payerIds).contains(userId)) {
					holder.groupBorrowedTextView.setText("You borrowed");
					holder.groupTransactionAmount.setText(String.format(Locale.getDefault(), "Rs" +
							" " +
							"%.2f", sum / noOfMembers));
				}
			}

		} else {
			holder.groupTransactionDescription.setText("PayerID: null");
			holder.groupTransactionAmount.setText(String.format("Rs %s", 0.0));
		}
	}

	public void addItem(BillModel model) {
		list.add(model);
		notifyItemInserted(list.size() - 1);
	}

	public void searchBill(String str) {
		List<BillModel> tempList = new ArrayList<>();

		if (!TextUtils.isEmpty(str)) {
			for (int i = 0; i < permanentList.size(); i++) {
				if (permanentList.get(i).getTitle().toLowerCase().contains(str.toLowerCase())) {
					tempList.add(permanentList.get(i));
				}
			}
			if (tempList.size() > 0) {
				list = tempList;
			} else {
				list = new ArrayList<>();
			}
		} else {
			list = permanentList;
		}
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount() {
		return list.size();
	}

	public class TransactionViewHolder extends RecyclerView.ViewHolder {
		TextView groupTransactionTitle, groupTransactionAmount, groupTransactionDescription,
				groupBorrowedTextView;

		public TransactionViewHolder(View itemView) {
			super(itemView);
			groupTransactionTitle = itemView.findViewById(R.id.group_transaction_text_view);
			groupTransactionAmount = itemView.findViewById(R.id
					.group_transaction_amount_text_view);
			groupBorrowedTextView = itemView.findViewById(R.id.group_borrowed_text_view);
			groupTransactionDescription = itemView.findViewById(R.id.group_trans_desc_text_view);
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(v.getContext(), DetailGroupTransaction.class);
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
