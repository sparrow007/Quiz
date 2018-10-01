package com.zersey.roz;

import java.util.List;

public class GroupChat {

	private long groupId;
	private List<Message> messageList;

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public void setMessageList(List<Message> messageList) {
		this.messageList = messageList;
	}

	public long getGroupId() {
		return groupId;
	}

	public List<Message> getMessageList() {
		return messageList;
	}
}
