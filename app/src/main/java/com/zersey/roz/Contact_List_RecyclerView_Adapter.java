package com.zersey.roz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Contact_List_RecyclerView_Adapter
	extends RecyclerView.Adapter<Contact_List_RecyclerView_Adapter.ListViewHolder> {
	private List<ContactModel> list,temp_list,Permanent_list;
	private AddMembersActivity Members;
	private Context context;
	private int Count;

	public Contact_List_RecyclerView_Adapter(List<ContactModel> list,int Count) {
		this.list = list;
		this.Permanent_list = list;
		this.Count=Count;
	}

	@NonNull @Override
	public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		context = parent.getContext();
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		View view = inflater.inflate(R.layout.contacts_list_layout, parent, false);
		return new ListViewHolder(view);
	}

	@Override public void onBindViewHolder(@NonNull ListViewHolder holder, final int position) {
if (position==Count-1){
if (Count!=0){
holder.LineDivider.setVisibility(View.VISIBLE);}
}
		holder.Name.setText(list.get(position).getName());
		holder.Number.setText(list.get(position).getNumber());

		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View v) {

				if (Members.Add_Member_Adapter != null) {
					if (Members.Add_Member_Adapter.CheckItem(list.get(position).getNumber())) {
						Toast.makeText(context, "Contact was already Added", Toast.LENGTH_LONG)
							.show();
					} else {
						Members.Add_Member_Adapter.add(list.get(position));
						list.remove(list.get(position));
						notifyDataSetChanged();
					}
				} else if (Group_About.RecyclerView_Adapter != null) {
					if (Group_About.RecyclerView_Adapter.CheckItem(
						list.get(position).getNumber())) {
						Toast.makeText(context, "Contact was already Added", Toast.LENGTH_LONG)
							.show();
					} else {
						Members.Add_Member_Adapter.add(list.get(position));
						list.remove(list.get(position));
						notifyDataSetChanged();
					}
				} else {
					Members.Add_Member_Adapter.add(list.get(position));
					list.remove(list.get(position));
					notifyDataSetChanged();
				}



			}
		});
	}

	public void Search(String str){
	    temp_list=new ArrayList<>();

	    if(!TextUtils.isEmpty(str)) {
			for (int i = 0; i < Permanent_list.size(); i++) {
				if (Permanent_list.get(i).getName().toLowerCase().contains(str.toLowerCase())) {
					temp_list.add(Permanent_list.get(i));
				}
			}

			if(temp_list.size()>0){
				list=temp_list;
			}else {
				list=new ArrayList<>();

			}
	    }else {
	    	//list=new ArrayList<>();
			list=Permanent_list;
		}


		notifyDataSetChanged();

	}

	@Override public int getItemCount() {
		return list.size();
	}

	public class ListViewHolder extends RecyclerView.ViewHolder {

		private TextView Name, Number;
		private LinearLayout LineDivider;

		public ListViewHolder(View itemView) {
			super(itemView);
			Name = itemView.findViewById(R.id.Contact_Name_TextView);
			Number = itemView.findViewById(R.id.Contact_Number_TextView);
			LineDivider=itemView.findViewById(R.id.Line_Divider);
		}
	}
}
