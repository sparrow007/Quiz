package com.zersey.roz;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class TaskModel implements Serializable {
	private Boolean Task_Checked = false;
	private long id;

	@SerializedName("id")private long Task_Id;
	@SerializedName("group_id") private long Group_Id;
	@SerializedName("item_title") private String Task_Title;
	@SerializedName("item_description") private String Task_Des;
	@SerializedName("reminder_date") private String Task_Date;
	@SerializedName("assigned_to") private long assignedTo;
	private long assignedBy;

	public long getGroup_Id() {
		return Group_Id;
	}

	public void setGroup_Id(long group_Id) {
		Group_Id = group_Id;
	}

	public void setTask_Id(long task_Id) {
		Task_Id = task_Id;
	}

	public void setTask_Title(String task_Title) {
		Task_Title = task_Title;
	}

	public void setTask_Des(String task_Des) {
		Task_Des = task_Des;
	}

	public void setTask_Checked(Boolean task_Checked) {
		Task_Checked = task_Checked;
	}

	public Boolean getTask_Checked() {
		return Task_Checked;
	}

	public long getTask_Id() {
		return Task_Id;
	}

	public String getTask_Des() {
		return Task_Des;
	}

	public String getTask_Title() {
		return Task_Title;
	}

	public void setTask_Date(String task_Date) {
		Task_Date = task_Date;
	}

	public String getTask_Date() {
		return Task_Date;
	}

	public long getAssignedTo() {
		return assignedTo;
	}

	public void setAssignedTo(long assignedTo) {
		this.assignedTo = assignedTo;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAssignedBy() {
		return assignedBy;
	}

	public void setAssignedBy(long assignedBy) {
		this.assignedBy = assignedBy;
	}
}
