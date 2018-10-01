package com.zersey.roz;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.List;

public class ContactRecyclerViewAdapter
	extends RecyclerView.Adapter<ContactRecyclerViewAdapter.ContactHolder> {
	private List<ContactModel> list;

	public ContactRecyclerViewAdapter(List<ContactModel> list) {
		this.list = list;
	}

	@NonNull @Override
	public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View view = inflater.inflate(R.layout.contact_recyclerview_layout, parent, false);
		return new ContactHolder(view);
	}

	@Override public void onBindViewHolder(@NonNull ContactHolder holder, final int position) {
		holder.Cross_Button.bringToFront();
		holder.Cross_Button.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {
				removeAt(position);
			}
		});
		String[] Initials = list.get(position).getName().split(" ");
		if (Initials.length > 1) {
			holder.Initials.setText(Initials[0].substring(0, 1) + Initials[1].substring(0, 1));
		} else {
			holder.Initials.setText(Initials[0].substring(0, 1));
		}

		holder.Number.setText(Initials[0]);
	}

	@Override public int getItemCount() {
		return list.size();
	}

	public void add(ContactModel items) {
		list.add(items);
		notifyItemInserted(list.size() - 1);
	}

	public void removeAt(int position) {
		list.remove(position);
		notifyItemRemoved(position);
		notifyItemRangeChanged(position, list.size());
	}

	public Boolean CheckItem(String number) {
		for (int i = 0; i < list.size(); i++) {
			if (TextUtils.equals(list.get(i).getName(), number)) {
				return true;
			}
		}
		return false;
	}

	public List<ContactModel> getList() {
		return list;
	}

	public class ContactHolder extends RecyclerView.ViewHolder {
		TextView Initials;
		TextView Number;
		ImageButton Cross_Button;

		public ContactHolder(View itemView) {
			super(itemView);
			Cross_Button = itemView.findViewById(R.id.Cross_Button);
			Cross_Button.bringToFront();
			Initials = itemView.findViewById(R.id.Contact_RecyclerView_Initials);
			Number = itemView.findViewById(R.id.Contact_RecyclerView_Number);
		}
	}
}
