package com.zersey.roz;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import java.util.List;

public class Task_Slider_Adapter extends RecyclerView.Adapter<Task_Slider_Adapter.Task_ViewHolder> {
	List<Task_Model> list;
	private Context context;
	int ViewType = 0;

	Task_Slider_Adapter(List<Task_Model> list) {
		this.list = list;
	}

	@NonNull @Override
	public Task_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		LayoutInflater inflater = LayoutInflater.from(parent.getContext());
		context = parent.getContext();
		if (viewType == 1) {
			View view = inflater.inflate(R.layout.add_button, parent, false);
			return new Task_ViewHolder(view);
		} else {
			View view = inflater.inflate(R.layout.task_card, parent, false);
			return new Task_ViewHolder(view);
		}
	}

	@Override public void onBindViewHolder(@NonNull Task_ViewHolder holder, int position) {

		if (position == list.size()) {
			holder.Plus_Button.setOnClickListener(new View.OnClickListener() {
				@Override public void onClick(View v) {
					Intent intent = new Intent(context, Task_Form_Activity.class);
					context.startActivity(intent);
				}
			});
		} else {
			holder.TaskTitle.setText(list.get(position).getTask_Title());
			if (list.get(position).getTask_Checked()) {
				holder.Check.setImageResource(R.drawable.checked);
			} else {
				holder.Check.setImageResource(R.drawable.unchecked);
			}
		}
	}

	@Override public int getItemCount() {
		return list.size() + 1;
	}

	@Override public int getItemViewType(int position) {
		if (position == list.size()) {
			ViewType = 1;
			return 1;
		} else {
			ViewType = 0;
			return 0;
		}
	}

	public void add(Task_Model model) {
		list.add(model);
		notifyDataSetChanged();
	}

	public class Task_ViewHolder extends RecyclerView.ViewHolder {
		TextView TaskTitle, Des;
		ImageButton Check, Plus_Button;

		public Task_ViewHolder(View itemView) {
			super(itemView);

			if (ViewType == 1) {
				Plus_Button = itemView.findViewById(R.id.Add_Group_Plus_Button);
			} else {
				TaskTitle = itemView.findViewById(R.id.Horizontal_Task_Title);
				Des = itemView.findViewById(R.id.Horizontal_Task_Des);
				Check = itemView.findViewById(R.id.Horizontal_Task_Check);

				Check.setOnClickListener(new View.OnClickListener() {
					@Override public void onClick(View v) {
						Task_Model model = list.get(getAdapterPosition());

						if (list.get(getAdapterPosition()).getTask_Checked()) {
							model.setTask_Checked(false);
							list.set(getAdapterPosition(), model);
							notifyDataSetChanged();
						} else {
							model.setTask_Checked(true);
							list.set(getAdapterPosition(), model);
							notifyDataSetChanged();
						}
					}
				});

				TaskTitle.setOnClickListener(new View.OnClickListener() {
					@Override public void onClick(View v) {
						Intent intent = new Intent(context, Task_Form_Activity.class);
						intent.putExtra("Task", list.get(getAdapterPosition()));
						context.startActivity(intent);
					}
				});
			}
		}
	}
}
