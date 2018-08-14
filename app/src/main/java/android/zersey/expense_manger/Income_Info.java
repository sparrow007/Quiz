package android.zersey.expense_manger;

public class Income_Info {
    private String Income_amount,Income_date,Income_title;
    private int Income_day,Income_month,Income_year;
    private String[] Months={"Jan","Feb","March","April","May","June","July","Aug","Sept","Oct","Nov","Dec"};
    Income_Info(String amount,String date,String title){
Income_amount=amount;
String[] temp=date.split("-");
Income_day=Integer.parseInt(temp[2]);
Income_month=Integer.parseInt(temp[1]);
Income_year=Integer.parseInt(temp[0]);
Income_title=title;
    }

    public Float getIncome_amount() {
        Float temp_amount=Float.parseFloat(Income_amount);
        return temp_amount;
    }

    public String getIncome_date() {
        return Income_date;
    }

    public String getIncome_title() {
        return Income_title;
    }

    public int getIncome_day() {
        return Income_day;
    }

    public int getIncome_month() {
        return Income_month;
    }

    public int getIncome_year() {
        return Income_year;
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
