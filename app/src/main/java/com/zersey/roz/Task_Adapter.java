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

public class Task_Adapter extends RecyclerView.Adapter<Task_Adapter.Task_View_Holder> {
    private List<Task_Model> list;
    private GroupModel model;
    private Context context;
    Task_Adapter(List<Task_Model> list,GroupModel model){
        this.list=list;
        this.model=model;
    }

    @NonNull
    @Override
    public Task_View_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        context=parent.getContext();
        View view=inflater.inflate(R.layout.task_layout,parent,false);
        return new Task_View_Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Task_View_Holder holder, int position) {
       holder.Task_Title.setText(list.get(position).getTask_Title());
       holder.Task_Description.setText(list.get(position).getTask_Des());
       if (list.get(position).getTask_Checked()){
           holder.Task_Check.setImageResource(R.drawable.checked);
       }else {
           holder.Task_Check.setImageResource(R.drawable.unchecked);
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

    public class Task_View_Holder extends RecyclerView.ViewHolder{
        TextView Task_Title,Task_Description;
        ImageButton Task_Check;
        public Task_View_Holder(final View itemView) {
            super(itemView);
            Task_Title=itemView.findViewById(R.id.Task_Title);
            Task_Description=itemView.findViewById(R.id.Task_Description);
            Task_Check=itemView.findViewById(R.id.Task_Check);
            Task_Title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,Task_Form_Activity.class);
                    intent.putExtra("Group", model);
                    intent.putExtra("Task", list.get(getAdapterPosition()));
                    context.startActivity(intent);

                }
            });

            Task_Check.setOnClickListener(new View.OnClickListener() {
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
