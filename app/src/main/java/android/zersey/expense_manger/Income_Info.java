package android.zersey.expense_manger;

public class Income_Info {
    private String Income_amount,Income_date,Income_title,Income_month,Income_year;
    private int Income_day;
    private String[] Months={"Jan","Feb","March","April","May","June","July","Aug","Sept","Oct","Nov","Dec"};
    Income_Info(String amount,String date,String title){
Income_amount=amount;
Income_date=date;
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
        Income_day=Integer.parseInt(extractNumber(Income_date));
        return Income_day;
    }

    public String getIncome_month() {
        for(int i=0;i<12;i++)
        { if(Income_date.contains(Months[i]))
        { Income_month=Months[i];
        }
        }
        return Income_month;
    }

    public String getIncome_year() {
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
