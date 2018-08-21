package android.zersey.expense_manger;

import com.google.gson.annotations.SerializedName;

public class Custom_Contact_items {

	@SerializedName("userId") private String id;
	@SerializedName("fullname") private String Contact_Person_Name;
	@SerializedName("mobile") private String Contact_Person_Number;

	public Custom_Contact_items(){}

	public Custom_Contact_items(String Name, String Number) {
		Contact_Person_Name = Name;
		Contact_Person_Number = Number;
	}

	public String getContact_Person_Name() {
		return Contact_Person_Name;
	}

	public String getContact_Person_Number() {
		return Contact_Person_Number;
	}

	public String getId() {
		return id;
	}

	public void setContact_Person_Name(String contact_Person_Name) {
		Contact_Person_Name = contact_Person_Name;
	}

	public void setContact_Person_Number(String contact_Person_Number) {
		Contact_Person_Number = contact_Person_Number;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override public String toString() {
		return "Custom_Contact_items{"
			+ "id='"
			+ id
			+ '\''
			+ ", Contact_Person_Name='"
			+ Contact_Person_Name
			+ '\''
			+ ", Contact_Person_Number='"
			+ Contact_Person_Number
			+ '\''
			+ '}';
	}
}
