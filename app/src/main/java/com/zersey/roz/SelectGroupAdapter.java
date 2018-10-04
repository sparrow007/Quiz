package com.zersey.roz;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class SelectGroupAdapter extends RecyclerView.Adapter<SelectGroupAdapter.ViewHolder> {

	private List<GroupModel> list;

	interface GroupSelectListener {
		void groupSelected(GroupModel groupModel);
	}

	private GroupSelectListener mListener;

	public SelectGroupAdapter(List<GroupModel> list) {
		this.list = list;
	}

	public void setGroupSelectListener(GroupSelectListener listener){
		mListener = listener;
	}

	@NonNull @Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
			.inflate(R.layout.group_about_layout, parent, false);
		return new ViewHolder(view);
	}

	@Override public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		holder.groupName.setText(list.get(position).getGroupName());
	}

	@Override public int getItemCount() {
		return list.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		TextView groupName;

		public ViewHolder(final View itemView) {
			super(itemView);
			groupName = itemView.findViewById(R.id.person_name);
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View view) {
					mListener.groupSelected(list.get(getAdapterPosition()));
				}
			});
		}
	}
}
