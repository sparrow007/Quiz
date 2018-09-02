package com.zersey.roz;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class ContactModel implements Serializable {

	private long id;
	@SerializedName("userId") private long userId;
	@SerializedName("fullname") private String name;
	@SerializedName("mobile") private String number;

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

	@Override public String toString() {
		return "ContactModel{"
			+ "id="
			+ id
			+ ", userId="
			+ userId
			+ ", name='"
			+ name
			+ '\''
			+ ", number='"
			+ number
			+ '\''
			+ '}';
	}
}
