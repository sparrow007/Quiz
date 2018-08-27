package android.zersey.expense_manger;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Transaction_MoreAdapter extends RecyclerView.Adapter<Transaction_MoreAdapter.More_ViewHolder> {
    private List<IncomeModel> list;
    private String[] Months = {
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };
    public  Transaction_MoreAdapter(List<IncomeModel> list){
        this.list=list;
    }

    @NonNull
    @Override
    public More_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.new_transaction_card_layout,parent,false);
        return new More_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull More_ViewHolder holder, int position) {
holder.Category.setText(list.get(position).getType());
holder.Amount.setText("INR "+list.get(position).getTotalAmount());
holder.Title.setText(list.get(position).getTitle());
holder.Date.setText(list.get(position).getIncome_day()+" "+Months[list.get(position).getIncome_month()-1]);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class More_ViewHolder extends RecyclerView.ViewHolder{
        TextView Category,Amount,Date,Title;
        public More_ViewHolder(View itemView) {
            super(itemView);
            Category=(TextView)itemView.findViewById(R.id.Category_TextView);
            Amount=(TextView)itemView.findViewById(R.id.Amount_TextView);
            Date=(TextView)itemView.findViewById(R.id.Date_TextView);
            Title=(TextView)itemView.findViewById(R.id.Title_TexTVew);
        }
    }
}
