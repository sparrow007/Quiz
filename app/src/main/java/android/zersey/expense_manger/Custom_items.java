package android.zersey.expense_manger;

public class Custom_items {
    private String Title,Category,Amount,Date;
    public Custom_items(String cat,String title,String Amnt,String date){
        this.Category=cat;
        this.Date=date;
        this.Amount=Amnt;
        this.Title=title;
        }

    public String getCategory() {
        return Category;
    }

    public String getAmount() {
        return Amount;
    }

    public String getDate() {
        return Date;
    }

    public String getTitle() { return Title; }
}
