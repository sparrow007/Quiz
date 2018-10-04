package com.zersey.roz;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.zersey.roz.Data.TransactionDbHelper;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Add_Groups_Form extends DialogFragment{

    private static final int REQUEST_CODE_ADD_MEMBER = 1;

    private LinearLayout Group_Add_Member_Layout;
    private ListView Add_Member_ListView;
    private ArrayList<ContactModel> Item_list;
    private List<Split_Contact_model> Split_List;
    private Spinner Split_Spinner;
    private TextView Split_Notes;
    private RecyclerView Split_RecyclerView;
    private RecyclerView Contact_RecyclerView;
    private DialogSplitRecyclerViewAdapter Adapter;
    private EditText Amount_Edit, Description_Edit, Group_Name_Edit;
    private StringBuffer users = new StringBuffer("");
    private StringBuffer users_number = new StringBuffer("");
    String USERS = "No Members";
    private ContactRecyclerViewAdapter RecyclerView_Adapter;
    private TransactionDbHelper mDbHelper;
    private Context context;
    private Button Group_Submit;




    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context=inflater.getContext();
        final View PromptView=inflater.inflate(R.layout.activity_group_form,container,false);
        Item_list = new ArrayList<>();
        mDbHelper = TransactionDbHelper.getInstance(context);
        Description_Edit =PromptView.findViewById(R.id.group_desc_edit_text);
        Group_Name_Edit = PromptView.findViewById(R.id.group_name_edit_text);
        Contact_RecyclerView = PromptView.findViewById(R.id.Add_Member_Group_Form_RecyclerView);
        Group_Submit=PromptView.findViewById(R.id.Group_Submit);
        Contact_RecyclerView.setLayoutManager(
                new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,
                        false));

        Group_Add_Member_Layout=PromptView.findViewById(R.id.Group_Add_Member_Layout);

        Group_Add_Member_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddMembersActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_MEMBER);
            }
        });

        Group_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupModel model = new GroupModel();
                model.setGroupName(
                        ((EditText) PromptView.findViewById(R.id.group_name_edit_text)).getText().toString());
                model.setGroupDesc(
                        ((EditText) PromptView.findViewById(R.id.group_desc_edit_text)).getText().toString());
                model.setUsers(users.toString());
                //model.setMobile_no(users_number.toString());
                Log.d( "onClick: ",users.toString());
                long groupId = mDbHelper.createGroup(model);
                model.setId(groupId);
                model.setTypeId(0);
                new ServerUtil(context).createGroup(model, Item_list);
                Intent intent = new Intent();
                intent.putExtra("group", model);
                GroupsFragment.billsAdapter.addItem(model);
                dismiss();
                /*setResult(Activity.RESULT_OK, intent);
                finish();*/
            }
        });
        SharedPreferences prefs = context.getSharedPreferences("login", MODE_PRIVATE);
        users.append(prefs.getString("userid", null));

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.TransparentWhite)));
        //dialog.getWindow().setBackgroundDrawableResource(R.color.translucent_black);
        getDialog().show();
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
  /*      LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));




        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
*/
        getDialog().getWindow()
                .clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        return PromptView;
    }


    /*public void addGroup(View view) {
        //if (groupAboutAdapter != null) {
        //	initUser();
        //}

        GroupModel model = new GroupModel();
        model.setGroupName(
                ((EditText) findViewById(R.id.group_name_edit_text)).getText().toString());
        model.setGroupDesc(
                ((EditText) findViewById(R.id.group_desc_edit_text)).getText().toString());
        model.setUsers(users.toString());
        //model.setMobile_no(users_number.toString());

        long groupId = mDbHelper.createGroup(model);
        model.setId(groupId);
        model.setTypeId(0);
        new ServerUtil(this).createGroup(model, Item_list);
        Intent intent = new Intent();
        intent.putExtra("group", model);
        GroupsFragment.billsAdapter.addItem(model);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }*/



    public boolean Check_Contact_List(String Number) {
        Boolean check = false;
        for (int i = 0; i < Item_list.size(); i++) {
            if (TextUtils.equals(Item_list.get(i).getNumber(), Number)) {
                check = true;
                //return true;
            } else {
                check = false;
            }
        }
        return check;
    }



    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_MEMBER) {
            if (data != null) {
                Contact_RecyclerView.setVisibility(View.VISIBLE);
                List<ContactModel> list = (List<ContactModel>) data.getSerializableExtra("ADDED");
                Item_list.addAll(list);
                RecyclerView_Adapter = new ContactRecyclerViewAdapter(Item_list);
                Contact_RecyclerView.setAdapter(RecyclerView_Adapter);
                for (ContactModel contactModel : list) {
                    users.append(",").append(contactModel.getUserId());
                    users_number.append(",").append(7011+contactModel.getUserId());
                }
            }
        }
    }


}
