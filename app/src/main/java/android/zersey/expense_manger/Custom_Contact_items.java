package android.zersey.expense_manger;

public class Custom_Contact_items {
    private String Contact_Person_Name,Contact_Person_Number;
    public Custom_Contact_items(String Name,String Number){
        Contact_Person_Name=Name;
        Contact_Person_Number=Number;
    }

    public String getContact_Person_Name() {
        return Contact_Person_Name;
    }

    public String getContact_Person_Number() {
        return Contact_Person_Number;
    }
}
