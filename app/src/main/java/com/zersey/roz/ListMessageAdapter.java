package com.zersey.roz;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.Calendar;

class ListMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private final long userId;
	private GroupChat groupChat;

	public ListMessageAdapter(GroupChat groupChat, long userId) {
		this.groupChat = groupChat;
		this.userId = userId;
	}

	@NonNull @Override
	public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		if (viewType == ChatActivity.VIEW_TYPE_FRIEND_MESSAGE) {
			View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.rc_item_message_friend, parent, false);
			return new ItemMessageFriendHolder(view);
		} else if (viewType == ChatActivity.VIEW_TYPE_USER_MESSAGE) {
			View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.rc_item_message_user, parent, false);
			return new ItemMessageUserHolder(view);
		}
		return null;
	}

	@Override public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
		if (holder instanceof ItemMessageFriendHolder) {
			((ItemMessageFriendHolder) holder).txtContent.setText(
				groupChat.getMessageList().get(position).getText());
			((ItemMessageFriendHolder) holder).senderName.setText(
				groupChat.getMessageList().get(position).getSenderName());
			((ItemMessageFriendHolder) holder).time.setText(getTime());
		} else if (holder instanceof ItemMessageUserHolder) {
			((ItemMessageUserHolder) holder).txtContent.setText(
				groupChat.getMessageList().get(position).getText());
			((ItemMessageUserHolder) holder).time.setText(getTime());
		}
	}

	@Override public int getItemViewType(int position) {
		return groupChat.getMessageList().get(position).getIdSender() == userId
			? ChatActivity.VIEW_TYPE_USER_MESSAGE : ChatActivity.VIEW_TYPE_FRIEND_MESSAGE;
	}

	private String getTime() {
		Calendar rightNow = Calendar.getInstance();
		int hr = rightNow.get(Calendar.HOUR);
		int hr2 = rightNow.get(Calendar.HOUR_OF_DAY);
		int min = rightNow.get(Calendar.MINUTE);
		String half;
		String time;
		if (hr2 >= 12) {
			half = "PM";
		} else {
			half = "AM";
		}
		if (min > 10) {
			time = Integer.toString(hr) + ":" + Integer.toString(min) + " " + half;
		} else {
			time = Integer.toString(hr) + ":0" + Integer.toString(min) + " " + half;
		}
		return (time);
	}

	@Override public int getItemCount() {
		return groupChat.getMessageList().size();
	}

	class ItemMessageUserHolder extends RecyclerView.ViewHolder {
		public TextView txtContent, time;

		public ItemMessageUserHolder(View itemView) {
			super(itemView);
			txtContent = itemView.findViewById(R.id.textContentUser);
			time = itemView.findViewById(R.id.time);
		}
	}

	class ItemMessageFriendHolder extends RecyclerView.ViewHolder {
		public TextView txtContent, senderName, time;

		public ItemMessageFriendHolder(View itemView) {
			super(itemView);
			txtContent = itemView.findViewById(R.id.textContentFriend);
			senderName = itemView.findViewById(R.id.sender_name);
			time = itemView.findViewById(R.id.time);
		}
	}
}
