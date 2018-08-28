package com.zersey.roz;

public class Split_Contact_model {
    private String Contact_Name,Split_Amount;
    public Split_Contact_model(String Contact,String Amount){
        this.Contact_Name=Contact;
        this.Split_Amount=Amount;
    }

    public String getContact_Name() {
        return Contact_Name;
    }

    public String getSplit_Amount() {
        return Split_Amount;
    }
}
