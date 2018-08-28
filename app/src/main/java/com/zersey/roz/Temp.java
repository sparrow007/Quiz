package com.zersey.roz;

public class Temp {

	private long localId, onlineId;
	private long id;
	private String action;

	public long getLocalId() {
		return localId;
	}

	public void setLocalId(long localId) {
		this.localId = localId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public long getOnlineId() {
		return onlineId;
	}

	public void setOnlineId(long onlineId) {
		this.onlineId = onlineId;
	}

	@Override public String toString() {
		return "Temp{"
			+ "localId="
			+ localId
			+ ", onlineId="
			+ onlineId
			+ ", id="
			+ id
			+ ", action='"
			+ action
			+ '\''
			+ '}';
	}
}