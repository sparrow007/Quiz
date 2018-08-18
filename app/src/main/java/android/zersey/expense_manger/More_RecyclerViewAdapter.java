package android.zersey.expense_manger;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import java.util.Timer;

public class More_RecyclerViewAdapter extends RecyclerView.Adapter<More_RecyclerViewAdapter.More_RecyclerViewHolder>{
   // private Context context;
    private List<String> list;
    private List<String> list2;
    public More_RecyclerViewAdapter(Context context,List<String> list,List<String> list2){
       // this.context=context;
        this.list=list;
        this.list2=list2;
    }

    @NonNull
    @Override
    public More_RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.more_layout,parent,false);
        return new More_RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull More_RecyclerViewHolder holder, int position) {
    holder.title.setText(list.get(position));
    holder.title2.setText(list2.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class More_RecyclerViewHolder extends RecyclerView.ViewHolder{
     TextView title,title2;
        public More_RecyclerViewHolder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.More_Title);
            title2=(TextView)itemView.findViewById(R.id.More2_Title);
        }
    }
}
