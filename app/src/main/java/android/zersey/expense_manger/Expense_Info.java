package android.zersey.expense_manger;

public class Expense_Info {
    private String Expense_amount,Expense_date,Expense_title,Expense_month,Expense_year;
    private int Expense_day;
    private String[] Months={"Jan","Feb","March","April","May","June","July","Aug","Sept","Oct","Nov","Dec"};
    Expense_Info(String amount,String date,String title){
        Expense_amount=amount;
        Expense_date=date;
        Expense_title=title;
    }

    public Float getExpense_amount() {
        Float temp_amount=Float.parseFloat(Expense_amount);
        return temp_amount;
    }

    public String getExpense_date() {
        return Expense_date;
    }

    public String getExpense_title() {
        return Expense_title;
    }

    public int getExpense_day() {
        Expense_day=Integer.parseInt(extractNumber(Expense_date));
        return Expense_day;
    }

    public String getExpense_month() {
        for(int i=0;i<12;i++)
        { if(Expense_date.contains(Months[i]))
            { Expense_month=Months[i];
            }
        }
        return Expense_month;
    }

    public String getExpense_year() {
        return Expense_year;
    }


    public static String extractNumber(final String str) {

        if(str == null || str.isEmpty()) return "";

        StringBuilder sb = new StringBuilder();
        boolean found = false;
        for(char c : str.toCharArray()){
            if(Character.isDigit(c)){
                sb.append(c);
                found = true;
            } else if(found){
                // If we already found a digit before and this char is not a digit, stop looping
                break;
            }
        }

        return sb.toString();
    }
}
