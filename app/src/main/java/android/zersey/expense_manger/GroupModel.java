package android.zersey.expense_manger;

import java.io.Serializable;

public class GroupModel implements Serializable {

	private long groupId, id;
	private String groupName, groupDesc, users;

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getGroupId() {
		return groupId;
	}

	public long getId() {
		return id;
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
			+ '}';
	}
}
