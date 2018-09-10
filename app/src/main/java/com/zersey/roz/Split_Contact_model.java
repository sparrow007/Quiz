package com.zersey.roz;

public class Split_Contact_model {
	private ContactModel Contact_Name;
	private String Split_Amount;

	public Split_Contact_model(ContactModel contact, String amount) {
		this.Contact_Name = contact;
		this.Split_Amount = amount;
	}

	public void setContact_Name(ContactModel contact_Name) {
		Contact_Name = contact_Name;
	}

	public void setSplit_Amount(String split_Amount) {
		Split_Amount = split_Amount;
	}

	public ContactModel getContact_Name() {
		return Contact_Name;
	}

	public String getSplit_Amount() {
		return Split_Amount;
	}
}
