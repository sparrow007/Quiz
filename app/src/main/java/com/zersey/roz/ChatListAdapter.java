package com.zersey.roz;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder> {
    private List<GroupModel> list;

    public ChatListAdapter(List<GroupModel> list) {
        this.list = list;
    }


    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chat_list, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.groupName.setText(list.get(position).getGroupName());
        holder.groupIcon.setText(list.get(position).getGroupName().substring(0,1));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView groupIcon;
        TextView groupName;

        public ChatViewHolder(View itemView) {
            super(itemView);
            groupIcon = itemView.findViewById(R.id.group_icon);
            groupName = itemView.findViewById(R.id.group_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ChatActivity.class);
                    intent.putExtra("group", list.get(getAdapterPosition()));
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}
