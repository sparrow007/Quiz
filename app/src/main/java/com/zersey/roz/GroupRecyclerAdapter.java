package com.zersey.roz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

public class GroupRecyclerAdapter
	extends RecyclerView.Adapter<GroupRecyclerAdapter.GroupItemViewHolder> {
	Context context;
	int ViewType = 0;
	List<GroupModel> list;

	interface GroupItemClickListener {
		void onGroupPlusClick();
	}

	private GroupItemClickListener mListener;

	public GroupRecyclerAdapter(Context context, List<GroupModel> list,
		GroupItemClickListener listener) {
		this.context = context;
		this.list = list;
		mListener = listener;
	}

	@NonNull @Override
	public GroupItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		if (viewType == 1) {
			View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.add_button, parent, false);
			return new GroupItemViewHolder(view);
		} else if (viewType == 2) {
			View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.dummy_layout, parent, false);
			return new GroupItemViewHolder(view);
		} else {
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			View view = inflater.inflate(R.layout.first_slider, parent, false);
			return new GroupItemViewHolder(view);
		}
	}

	@Override public void onBindViewHolder(@NonNull GroupItemViewHolder holder, int position) {
		if (list.size() == 0) {
			holder.Dummy_Layout.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View v) {
					mListener.onGroupPlusClick();
				}
			});
		} else if (position == list.size() && list.size() != 0) {
			holder.Plus_Button.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View v) {
					mListener.onGroupPlusClick();
				}
			});
		} else if (list.size() != 0) {
			//holder.tv.setText(list.get(position));
			holder.tv.setText(list.get(position).getGroupName());
		}
	}

	@Override public int getItemCount() {
		return list.size() + 1;
	}

	public void addItem(GroupModel model) {
		list.add(model);
		notifyDataSetChanged();
	}

	@Override public int getItemViewType(int position) {
		if (list.size() == 0) {
			ViewType = 2;
			return 2;
		} else if (position == list.size()) {
			ViewType = 1;
			return 1;
		} else {
			ViewType = 0;
			return 0;
		}
	}

	public class GroupItemViewHolder extends RecyclerView.ViewHolder
		implements View.OnClickListener {
		TextView tv, Add_Group_TextView;
		LinearLayout Dummy_Layout;
		ImageButton Plus_Button;

		public GroupItemViewHolder(View itemView) {
			super(itemView);
			if (list.size() == 0) {
				Dummy_Layout = itemView.findViewById(R.id.Dummy_Layout);
			} else if (ViewType == 1
				&& list.size()
				!= 0) {                //Add_Group_TextView = (TextView) itemView.findViewById(R.id.Add_Group_TextView);
				Plus_Button = itemView.findViewById(R.id.Add_Group_Plus_Button);
				Plus_Button.setOnClickListener(new View.OnClickListener() {
					@Override public void onClick(View v) {

					}
				});
			} else {
				tv = itemView.findViewById(R.id.First_Title);
			}
			itemView.setOnClickListener(this);
		}

		@Override public void onClick(View view) {
			if (Plus_Button != null) {
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
