package com.zersey.roz;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class GroupAboutAdapter
	extends RecyclerView.Adapter<GroupAboutAdapter.GroupAboutViewHolder> {
	private List<ContactModel> list;
	private List<ContactModel> mNotFound;

	public GroupAboutAdapter(List<ContactModel> list, List<ContactModel> notFound) {
		this.list = list;
		mNotFound = notFound;
	}

	@NonNull @Override
	public GroupAboutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View view = inflater.inflate(R.layout.group_about_layout, parent, false);
		return new GroupAboutViewHolder(view);
	}

	@Override public void onBindViewHolder(@NonNull GroupAboutViewHolder holder, int position) {

		if (position < list.size()) {
			holder.nameTextView.setText(list.get(position).getName());
		} else {
			holder.nameTextView.setText(mNotFound.get(position - list.size()).getNumber());
			holder.numberTextView.setText(mNotFound.get(position - list.size()).getName());
		}
	}

	@Override public int getItemCount() {
		return list.size() + mNotFound.size();
	}

	public Boolean CheckItem(String number) {
		for (int i = 0; i < list.size(); i++) {
			if (TextUtils.equals(list.get(i).getNumber(), number)) {
				return true;
			}
		}
		return false;
	}

	public class GroupAboutViewHolder extends RecyclerView.ViewHolder {
		TextView nameTextView, numberTextView;

		public GroupAboutViewHolder(View itemView) {
			super(itemView);
			nameTextView = itemView.findViewById(R.id.person_name);
			numberTextView = itemView.findViewById(R.id.person_number);
		}
	}
}
