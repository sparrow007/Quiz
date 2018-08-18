package android.zersey.expense_manger;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

public class Group_Balances_Adapter extends RecyclerView.Adapter<Group_Balances_Adapter.BalancesViewHolder>{
    private List<String> list;
    public  Group_Balances_Adapter(List<String> list){
        this.list=list;
    }

    @NonNull
    @Override
    public BalancesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.group_balance_layout,parent,false);
        return new BalancesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BalancesViewHolder holder, int position) {
        holder.Balances_TextView.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BalancesViewHolder extends RecyclerView.ViewHolder{
        TextView Balances_TextView;
        public BalancesViewHolder(View itemView) {
            super(itemView);
            Balances_TextView=(TextView)itemView.findViewById(R.id.Group_Balances_TextView);
        }
    }
}
