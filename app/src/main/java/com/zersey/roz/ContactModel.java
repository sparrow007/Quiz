package com.zersey.roz;

public class ContactModel {

	private long id, userId;
	private String name;
	private String number;

	public void setId(long id) {
		this.id = id;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public long getId() {
		return id;
	}

	public long getUserId() {
		return userId;
	}

	public String getName() {
		return name;
	}

	public String getNumber() {
		return number;
	}

}
