package android.zersey.expense_manger;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
 private List<String> list;
 public CategoryAdapter(List<String> list){
     this.list=list;
 }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.modelcategorylayout,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryViewHolder holder, final int position) {
    holder.category.setText(list.get(position));
    holder.category.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(position==0){
                //holder.category.setBackgroundColor(Color.parseColor("#000000"));
                //holder.get
            }
        }
    });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
     CardView cardView;
    TextView category;
    public CategoryViewHolder(View itemView) {
        super(itemView);
        category=(TextView)itemView.findViewById(R.id.Cat_View);
        cardView=(CardView)itemView.findViewById(R.id.Specific_CardView);
        itemView.setOnClickListener(this);
    }

        @Override
        public void onClick(View v) {
            cardView.setBackgroundColor(Color.parseColor("#000000"));
        }
    }
}
