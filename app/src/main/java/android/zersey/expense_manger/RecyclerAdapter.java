package android.zersey.expense_manger;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
	List<IncomeModel> list;

	public RecyclerAdapter(List<IncomeModel> list) {
		this.list = new ArrayList<>();
		this.list.addAll(list);
	}

	@NonNull @Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
			.inflate(R.layout.transaction_list_layout, parent, false);
		return new ViewHolder(view);
	}

	@Override public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

		IncomeModel customitems = list.get(position);
		Log.d("hueh", "onBindViewHolder: " + customitems.toString());
		if (customitems != null) {

			if (holder.customcategory != null) {
				if (Util.isEmpty(customitems.getCatId())) {
					holder.category_logo.setImageResource(R.drawable.salary);
					holder.customcategory.setText("Income");
				} else {
					holder.customcategory.setText(customitems.getCatId());
					switch (customitems.getCatId()) {
						case "Clothing":
							holder.category_logo.setImageResource(R.drawable.cloth);
							break;
						case "Entertainment":
							holder.category_logo.setImageResource(R.drawable.entertainment);
							break;
						case "Food":
							holder.category_logo.setImageResource(R.drawable.food);
							break;
						case "Fuel":
							holder.category_logo.setImageResource(R.drawable.fuel);
							break;
						case "Health":
							holder.category_logo.setImageResource(R.drawable.health);
							break;
					}
				}
			}
			if (holder.customtitle != null) {
				holder.customtitle.setText(customitems.getTitle());
			}
			if (holder.customamount != null) {
				holder.customamount.setText(customitems.getTotalAmount());
			}
			if (holder.customdate != null) {
				holder.customdate.setText(customitems.getPaidAtDate());
			}
		}
	}

	@Override public int getItemCount() {
		return list.size();
	}

	public void addItem(IncomeModel model) {
		list.add(model);
		notifyDataSetChanged();
	}

	public void updateItem(int pos, IncomeModel model) {
		if (pos != -1) {
			list.set(pos, model);
			notifyItemChanged(pos);
		}
	}

	public void deleteItem(int pos) {
		if (pos != -1) {
			list.remove(pos);
			notifyItemRemoved(pos);
		}
	}

	public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
		TextView customcategory, customamount, customdate, customtitle;
		ImageView category_logo;

		public ViewHolder(View itemView) {
			super(itemView);
			customcategory = (TextView) itemView.findViewById(R.id.Custom_category);
			customamount = (TextView) itemView.findViewById(R.id.Custom_amount);
			customdate = (TextView) itemView.findViewById(R.id.Custom_date);
			customtitle = (TextView) itemView.findViewById(R.id.Custom_title);
			category_logo = (ImageView) itemView.findViewById(R.id.Custom_logo);
			itemView.setOnClickListener(this);
		}

		@Override public void onClick(View view) {
			Intent i = new Intent(view.getContext(), MainActivity.class);
			i.putExtra("pos", getAdapterPosition());
			i.putExtra("_ID", list.get(getAdapterPosition()).getId());
			i.putExtra("CardClicked", "Yes");
			i.putExtra("Title", list.get(getAdapterPosition()).getTitle());
			i.putExtra("Type", list.get(getAdapterPosition()).getType());
			i.putExtra("Category", list.get(getAdapterPosition()).getCatId());
			i.putExtra("Amount", list.get(getAdapterPosition()).getTotalAmount());
			i.putExtra("DateCreated", list.get(getAdapterPosition()).getPaidAtDate());
			i.putExtra("model", list.get(getAdapterPosition()));
			view.getContext().startActivity(i);
		}
	}
}
