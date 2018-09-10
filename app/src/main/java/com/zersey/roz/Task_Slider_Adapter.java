package com.zersey.roz;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

public class Task_Slider_Adapter extends RecyclerView.Adapter<Task_Slider_Adapter.Task_ViewHolder> {
    List<Task_Model> list;
    Task_Slider_Adapter(List<Task_Model> list){
        this.list=list;
    }

    @NonNull
    @Override
    public Task_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.task_card,parent,false);
        return new Task_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Task_ViewHolder holder, int position) {
        holder.TaskTitle.setText(list.get(position).getTask_Title());
        //holder.Des.setText(list.get(position).getTask_Des());
        if (list.get(position).getTask_Checked()){
            holder.Check.setImageResource(R.drawable.checked);
        }else {
            holder.Check.setImageResource(R.drawable.unchecked);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void add(Task_Model model){
        list.add(model);
        notifyDataSetChanged();
    }


    public class Task_ViewHolder extends RecyclerView.ViewHolder{
     TextView TaskTitle,Des;
     ImageButton Check;
        public Task_ViewHolder(View itemView) {
            super(itemView);
            TaskTitle=itemView.findViewById(R.id.Horizontal_Task_Title);
            Des=itemView.findViewById(R.id.Horizontal_Task_Des);
            Check=itemView.findViewById(R.id.Horizontal_Task_Check);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Task_Model model=list.get(getAdapterPosition());

                    if(list.get(getAdapterPosition()).getTask_Checked()){
                        model.setTask_Checked(false);
                        list.set(getAdapterPosition(),model);
                        notifyDataSetChanged();
                    }else {
                        model.setTask_Checked(true);
                        list.set(getAdapterPosition(),model);
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }
}
