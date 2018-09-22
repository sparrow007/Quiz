package com.zersey.roz;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class GroupModel implements Serializable {

	private long id;

	@SerializedName("id") private long groupId;
	@SerializedName("group_name") private String groupName;
	@SerializedName("group_description") private String groupDesc;
	@SerializedName("users") private String users;
	@SerializedName("mobile_no") private String mobile_no;
	@SerializedName("fullname") private String fullname;
	@SerializedName("type_id") private int typeId;

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getFullname() {
		return fullname;
	}

	public long getGroupId() {
		return groupId;
	}

	public long getId() {
		return id;
	}

	public void setMobile_no(String mobile_no) {
		this.mobile_no = mobile_no;
	}

	public String getMobile_no() {
		return mobile_no;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}

	public void setUsers(String users) {
		this.users = users;
	}

	public String getGroupName() {
		return groupName;
	}

	public String getGroupDesc() {
		return groupDesc;
	}

	public String getUsers() {
		return users;
	}

	@Override public String toString() {
		return "GroupModel{"
			+ "groupId="
			+ groupId
			+ ", id="
			+ id
			+ ", groupName='"
			+ groupName
			+ '\''
			+ ", groupDesc='"
			+ groupDesc
			+ '\''
			+ ", users='"
			+ users
			+ '\''
			+ ", fullname='"
			+ fullname
			+ '\''
            + ", mobile_no='"
            + mobile_no
            + '\''
			+ '}';
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}
}
