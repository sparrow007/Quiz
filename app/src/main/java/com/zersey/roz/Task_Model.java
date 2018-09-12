package com.zersey.roz;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Task_Model implements Serializable {
    @SerializedName("Task_id") private long Task_Id;
    @SerializedName("group_id") private long Group_Id;
    @SerializedName("Task_Title") private String Task_Title;
    @SerializedName("Task_Description") private String Task_Des;
    @SerializedName("Task_Checked") private Boolean Task_Checked;
    @SerializedName("Task_Date") private String Task_Date;


    public Task_Model(String title,String des,String Date,Boolean Checked){
        this.Task_Title=title;
        this.Task_Des=des;
        this.Task_Checked=Checked;
        this.Task_Date=Date;
    }

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
}
