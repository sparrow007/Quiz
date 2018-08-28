package com.zersey.roz;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
 private List<String> list;
 //SelectedItems;
 private int lastSelectedPosition = -1;
 private String lastCategory="";
 private Context context;

    public CategoryAdapter(Context context, List<String> list){
        this.context=context;
     this.list=list;
     //SelectedItems=new ArrayList<>();

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
   holder.category.setChecked(lastSelectedPosition == position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public String getLastCategory() {
        return lastCategory;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
     CardView cardView;
    RadioButton category;
    public CategoryViewHolder(View itemView) {
        super(itemView);
        category=(RadioButton) itemView.findViewById(R.id.Cat_View);
        cardView=(CardView)itemView.findViewById(R.id.Specific_CardView);
        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastSelectedPosition = getAdapterPosition();
                notifyDataSetChanged();
                //category.setBackgroundColor(Color.parseColor("#000000"));
                if(!TextUtils.equals(lastCategory,category.getText().toString())){
                    lastCategory=category.getText().toString();
                }
                Toast.makeText(context,
                        "selected offer is " + category.getText(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

        @Override
        public void onClick(View v) {

        }
    }
}
