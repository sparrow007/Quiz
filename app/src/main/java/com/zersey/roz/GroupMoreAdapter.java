package com.zersey.roz;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zersey.roz.Data.TransactionDbHelper;

import java.util.List;
import java.util.Locale;

public class GroupMoreAdapter extends RecyclerView.Adapter<GroupMoreAdapter.MoreVieHolder> {
	private List<GroupModel> list;
	private TransactionDbHelper dbHelper;

	public GroupMoreAdapter(List<GroupModel> list, Context context) {
		this.list = list;
		dbHelper = TransactionDbHelper.getInstance(context);
	}

	@NonNull
	@Override
	public MoreVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View view = inflater.inflate(R.layout.new_transaction_card_layout, parent, false);
		return new MoreVieHolder(view);
	}

	@Override
	public void onBindViewHolder(@NonNull MoreVieHolder holder, int position) {
		holder.category.setText(String.format(Locale.getDefault(), "%d members", list.get
				(position).getUsers().split(",").length));
		holder.amount.setText(String.format(Locale.getDefault(), "Rs %.2f", dbHelper.getTotalGroupAmount(list.get
				(position).getGroupId())));
		holder.title.setText(list.get(position).getGroupName());
		holder.date.setText(list.get(position).getUpdatedAt().split(" ")[0]);
	}

	@Override
	public int getItemCount() {
		return list.size();
	}

	public class MoreVieHolder extends RecyclerView.ViewHolder {
		TextView category, amount, date, title;

		public MoreVieHolder(View itemView) {
			super(itemView);
			category = itemView.findViewById(R.id.category_text_view);
			amount = itemView.findViewById(R.id.amount_text_view);
			date = itemView.findViewById(R.id.date_text_view);
			title = itemView.findViewById(R.id.title_text_view);
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(v.getContext(), SpecificGroup.class);
					intent.putExtra("group", list.get(getAdapterPosition()));
					v.getContext().startActivity(intent);
				}
			});
		}
	}
}
