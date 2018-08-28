package com.zersey.roz;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

public class First_Slider_Adapter
	extends RecyclerView.Adapter<First_Slider_Adapter.First_Slider_ViewHolder> {
	Context context;
	int ViewType=0;
	List<GroupModel> list;

	public First_Slider_Adapter(Context context, List<GroupModel> list) {
		this.context = context;
		this.list = list;
	}

	@NonNull @Override
	public First_Slider_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		if(viewType==1){
			View view = LayoutInflater.from(parent.getContext())
					.inflate(R.layout.add_button, parent, false);
			return new First_Slider_ViewHolder(view);
		} else if(viewType==2) {
			View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.dummy_layout, parent, false);
			return new First_Slider_ViewHolder(view);
		} else {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			View view = inflater.inflate(R.layout.first_slider, parent, false);
			return new First_Slider_ViewHolder(view);}
	}

	@Override public void onBindViewHolder(@NonNull First_Slider_ViewHolder holder, int position) {
		if (list.size()==0){
			holder.Dummy_Layout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(context,Group_Form.class);
					context.startActivity(intent);
				}
			});
		}else if (position == list.size() && list.size()!=0) {			holder.Plus_Button.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(context,Group_Form.class);
					context.startActivity(intent);
				}
			});
		}  else if(list.size()!=0){
			//holder.tv.setText(list.get(position));
			holder.tv.setText(list.get(position).getGroupName());
		}
	}

	@Override public int getItemCount() {
		return list.size() + 1;
	}

	@Override
	public int getItemViewType(int position) {
		if (list.size()==0){
			ViewType=2;
			return 2;
		} else if(position==list.size()){
			ViewType=1;
			return 1;
		}else {
			ViewType=0;
			return 0;}
	}

	public class First_Slider_ViewHolder extends RecyclerView.ViewHolder
		implements View.OnClickListener {
		TextView tv,Add_Group_TextView;
		LinearLayout Dummy_Layout;
		ImageButton Plus_Button;

		public First_Slider_ViewHolder(View itemView) {
			super(itemView);
			if (list.size()==0){
				Dummy_Layout=(LinearLayout)itemView.findViewById(R.id.Dummy_Layout);
			}else if(ViewType==1 && list.size()!=0) {				//Add_Group_TextView = (TextView) itemView.findViewById(R.id.Add_Group_TextView);
				Plus_Button = (ImageButton) itemView.findViewById(R.id.Add_Group_Plus_Button);
				Plus_Button.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {

					}
				});
			}else {
				tv = itemView.findViewById(R.id.First_Title);}
			itemView.setOnClickListener(this);
		}

		@Override public void onClick(View view) {
			if (Plus_Button!=null) {
				Intent intent = new Intent(view.getContext(), Group_Form.class);
				((Activity) view.getContext()).startActivityForResult(intent, 1234);
			} else {
				Intent intent = new Intent(view.getContext(), Specific_Group.class);
				intent.putExtra("group", list.get(getAdapterPosition()));
				view.getContext().startActivity(intent);
			}
		}
	}
}
