package com.zersey.roz;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

public class Paid_By_Dialog extends DialogFragment {

    private RecyclerView Paid_RecyclerView;
    private Context context;
    private List<ContactModel> list;
    public static Paid_RecyclerView_Adapter Paid_Adapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        list=new ArrayList<>();
        ContactModel model=new ContactModel();
        model.setName("You");
        list.add(model);
        if (getArguments() != null) {
            //mDbHelper=TransactionDbHelper.getInstance(getContext());
            //long count=mDbHelper.getGroupsCount();
            //Log.d( "onCreate: ",count+"");
            list.addAll((List<ContactModel>) getArguments().getSerializable("Contact_list"));
            int size=list.size();
            Log.d( "onCreateDialog: ",size+"");
        }
        int size=list.size();
        Log.d( "onCreateDialog: ",size+"");
        return super.onCreateDialog(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context=inflater.getContext();
        View Paid_View=inflater.inflate(R.layout.paid_by_layout,container,false);
        Paid_RecyclerView=new RecyclerView(context);
        Paid_RecyclerView=Paid_View.findViewById(R.id.Paid_By_RecyclerView);
        Paid_RecyclerView.setLayoutManager(new LinearLayoutManager(context));
        Paid_Adapter=new Paid_RecyclerView_Adapter(context,list);
        Paid_RecyclerView.setAdapter(Paid_Adapter);
        getDialog().setTitle("Choose payer");
        return Paid_View;
    }

}
