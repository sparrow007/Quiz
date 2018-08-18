package android.zersey.expense_manger;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class First_Slider_Adapter extends RecyclerView.Adapter<First_Slider_Adapter.First_Slider_ViewHolder> {
    Context context;
    List<String> list;
    public First_Slider_Adapter(Context context,List<String> list){
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public First_Slider_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.first_slider,parent,false);
        return new First_Slider_ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull First_Slider_ViewHolder holder, int position) {
        if(position==list.size()){
            holder.tv.setText("Add Group");
        }else{
     holder.tv.setText(list.get(position));}
    }

    @Override
    public int getItemCount() {
        return list.size()+1;
    }

    public class First_Slider_ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv;
        public First_Slider_ViewHolder(View itemView) {
            super(itemView);
            tv=(TextView)itemView.findViewById(R.id.First_Title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            if (TextUtils.equals(tv.getText().toString(),"Add Group")){
                Intent intent=new Intent(view.getContext(),Group_Form.class);
                view.getContext().startActivity(intent);
            }else {
            Intent intent=new Intent(view.getContext(),Specific_Group.class);
            view.getContext().startActivity(intent);
            }
        }
    }
}
