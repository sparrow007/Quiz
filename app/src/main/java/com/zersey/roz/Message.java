package com.zersey.roz;

public class Message {

	private long idSender;
	private String text, senderName;
	private long timestamp;

	public void setIdSender(long idSender) {
		this.idSender = idSender;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public long getIdSender() {
		return idSender;
	}

	public String getText() {
		return text;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
}
