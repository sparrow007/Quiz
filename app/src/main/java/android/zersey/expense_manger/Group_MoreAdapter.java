package android.zersey.expense_manger;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zersey.roz.GroupModel;
import com.zersey.roz.R;

import java.util.List;

public class Group_MoreAdapter extends RecyclerView.Adapter<Group_MoreAdapter.MoreVieHolder>{
    private List<GroupModel> list;
    public Group_MoreAdapter(List<GroupModel> list){
        this.list=list;
    }

    @NonNull
    @Override
    public MoreVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.new_transaction_card_layout,parent,false);
        return new MoreVieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoreVieHolder holder, int position) {
        holder.Category.setText(list.get(position).getGroupDesc());
        holder.Amount.setText("    ");
        holder.Title.setText(list.get(position).getGroupName());
        holder.Date.setText("     ");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MoreVieHolder extends RecyclerView.ViewHolder{
        TextView Category,Amount,Date,Title;
        public MoreVieHolder(View itemView) {
            super(itemView);
            Category=(TextView)itemView.findViewById(R.id.Category_TextView);
            Amount=(TextView)itemView.findViewById(R.id.Amount_TextView);
            Date=(TextView)itemView.findViewById(R.id.Date_TextView);
            Title=(TextView)itemView.findViewById(R.id.Title_TexTVew);
        }
    }
}
