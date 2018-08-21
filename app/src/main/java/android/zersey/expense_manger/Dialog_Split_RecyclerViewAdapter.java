package android.zersey.expense_manger;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

public class Dialog_Split_RecyclerViewAdapter extends RecyclerView.Adapter<Dialog_Split_RecyclerViewAdapter.Split_ViewHolder>{
    private List<Split_Contact_model> list;

    private Context context;
    public Dialog_Split_RecyclerViewAdapter(Context context,List<Split_Contact_model> list){
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public Split_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.slip_contact_recyclerview_layout,parent,false);
        return new Split_ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Split_ViewHolder holder, final int position) {
        Log.d( "Dialog: ",list.size()+"");
        String name=list.get(position).getSplit_Amount();
        Log.d( "Dialog: ",name);
        holder.Contact_Name.setText(list.get(position).getContact_Name());
        holder.Split_Amount.setText(list.get(position).getSplit_Amount());
        holder.Split_Amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        holder.Split_Amount.addTextChangedListener(new TextWatcher() {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
     list.add(position,new Split_Contact_model(holder.Contact_Name.getText().toString(),holder.Split_Amount.getText().toString()));
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
});
    }

    public List<Split_Contact_model> getList() {
        return list;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class Split_ViewHolder extends RecyclerView.ViewHolder{
        public TextView Contact_Name;
        public EditText Split_Amount;
        public Split_ViewHolder(View itemView) {
            super(itemView);
            Contact_Name=(TextView)itemView.findViewById(R.id.RecyclerView_Contact_Name);
            Split_Amount=(EditText)itemView.findViewById(R.id.RecyclerView_Split_Amount);
        }

        public EditText getSplit_Amount() {
            return Split_Amount;
        }
    }
}
