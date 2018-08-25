package android.zersey.expense_manger;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
	List<IncomeModel> list;
	int ViewType=0;
	Context context;
	public RecyclerAdapter(List<IncomeModel> list) {
		this.list = new ArrayList<>();
		this.list.addAll(list);
	}

	@NonNull @Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		context=parent.getContext();
		if(viewType==1){
			View view = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.add_button, parent, false);
			return new ViewHolder(view);
		}else{
			View view = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.transaction_list_layout, parent, false);
			return new ViewHolder(view);
		}
	}

	@Override public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

		IncomeModel customitems = new IncomeModel();
		if(list.size()>position){
			customitems = list.get(position);
		}else {
			customitems=null;
		}
		if (customitems != null) {

			if (holder.customcategory != null) {
					holder.category_logo.setImageResource(R.drawable.salary);
					holder.customcategory.setText(customitems.getType());
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
		if (position == list.size()) {

			//holder.Add_Group_TextView.setText("Add transactions");
			holder.Plus_Button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(context,MainActivity.class);
					context.startActivity(intent);
				}
			});


		}
	}

	@Override public int getItemCount() {
		return list.size()+1;
	}

	@Override
	public int getItemViewType(int position) {
		if(position==list.size()){
			ViewType=1;
			return 1;
		}else {
			ViewType=0;
			return 0;}
	}

	public void addItem(IncomeModel model) {
		list.add(model);
		notifyDataSetChanged();
	}

	public void addAll(List<IncomeModel> transactionList){
		list.clear();
		list.addAll(transactionList);
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
		ImageButton Plus_Button;

		public ViewHolder(View itemView) {
			super(itemView);
			if(ViewType==1) {
				//Add_Group_TextView = (TextView) itemView.findViewById(R.id.Add_Group_TextView);
				Plus_Button = (ImageButton) itemView.findViewById(R.id.Add_Group_Plus_Button);
				Plus_Button.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				});
			}else{
				customcategory = (TextView) itemView.findViewById(R.id.Custom_category);
				customamount = (TextView) itemView.findViewById(R.id.Custom_amount);
				customdate = (TextView) itemView.findViewById(R.id.Custom_date);
				customtitle = (TextView) itemView.findViewById(R.id.Custom_title);
				category_logo = (ImageView) itemView.findViewById(R.id.Custom_logo);
			}
			itemView.setOnClickListener(this);
		}

		@Override public void onClick(View view) {
			//String temp=list.get(getAdapterPosition()).getCatId();
			//Log.d( "onClick: ",temp);
			if (Plus_Button==null) {
				String temp = list.get(getAdapterPosition()).getCatId();
				//Log.d( "onClick: ",temp);
				Intent i = new Intent(view.getContext(), SpecificTransactions.class);
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
}
