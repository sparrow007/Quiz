package android.zersey.expense_manger;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Group_Form extends AppCompatActivity {
private LinearLayout Add_Member_Layout;
private ListView Add_Member_ListView;
private ArrayList<Custom_Contact_items> Item_list;
private List<Split_Contact_model> Split_List;
private Spinner Split_Spinner;
private TextView Split_Notes;
private RecyclerView Split_RecyclerView;
private Dialog_Split_RecyclerViewAdapter Adapter;
private EditText Amount_Edit,Description_Edit,Group_Name_Edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_form);
        Item_list=new ArrayList<>();
        Split_List=new ArrayList<>();
        Amount_Edit=(EditText)findViewById(R.id.Group_Amount_Edit);
        Description_Edit=(EditText)findViewById(R.id.Group_Description_Edit);
        Group_Name_Edit=(EditText)findViewById(R.id.Group_Amount_Edit);
        Split_RecyclerView=new RecyclerView(this);
        Add_Member_Layout=(LinearLayout)findViewById(R.id.Add_Member_Layout);
        Add_Member_ListView=(ListView)findViewById(R.id.Add_Member_ListView);

    }

    public void Add_Members(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

        startActivityForResult(intent, 0);
    }

    public void Dialog(View view){
        String Amount=Amount_Edit.getText().toString();
        int no_of_Person=Item_list.size();
        String Specific_Amount;
        if(TextUtils.isEmpty(Amount)){
            Specific_Amount="0";
        }else {
            Specific_Amount=""+Integer.parseInt(Amount)/no_of_Person;
        }
        Log.d( "Dialog: ",Item_list.size()+"");
        if(Item_list.size()>0){
            Split_List=new ArrayList<>();
            for (int i=0;i<Item_list.size();i++)
            {

               Split_List.add(new Split_Contact_model(Item_list.get(i).getContact_Person_Name(),Specific_Amount));
            }
        }else {
            Split_List=new ArrayList<>();
            Split_List.add(new Split_Contact_model("Bharat",Specific_Amount));
        }
        Log.d( "Dialog: ",Split_List.size()+"");
        LayoutInflater LI = LayoutInflater.from(Group_Form.this);
        View PromptsView = LI.inflate(R.layout.split_dialog_layout, null);
        Split_Spinner=(Spinner)PromptsView.findViewById(R.id.Split_Spinner);
        Split_RecyclerView=(RecyclerView)PromptsView.findViewById(R.id.Dialog_RecyclerView);
        Split_Notes=(TextView)PromptsView.findViewById(R.id.Dialog_Split_Notes);
        Split_RecyclerView.setLayoutManager(new LinearLayoutManager(this));
Adapter=new Dialog_Split_RecyclerViewAdapter(getApplicationContext(),Split_List);
        Split_RecyclerView.setAdapter(Adapter);

        Split_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder =
                new android.support.v7.app.AlertDialog.Builder(Group_Form.this);
        alertDialogBuilder.setView(PromptsView);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        android.support.v7.app.AlertDialog alertDialog =
                alertDialogBuilder.create();


        alertDialog.show();
    }

    @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            //List<String> temp=getIntent().getStringArrayListExtra("Previous_Contacts");
            Uri contactData = data.getData();
            Cursor c = this.managedQuery(contactData, null, null, null, null);
            if (c.moveToFirst()) {
                String cNumber = null;
                final String name =
                        c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                String hasPhone =
                        c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                if (hasPhone.equalsIgnoreCase("1")) {
                    Cursor phones = this.getContentResolver()
                            .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id, null,
                                    null);
                    phones.moveToFirst();
                    cNumber = phones.getString(phones.getColumnIndex("data1"));
                    //System.out.println("number is:"+cNumber);
                }
                Custom_Contact_items CCItem = new Custom_Contact_items(name,cNumber);
                Item_list.add(CCItem);
                Log.d("onActivityResult: ",Item_list.size()+"");
                Add_Member_ListView.setVisibility(View.VISIBLE);
                Contact_ListView_Adapter adapter=new Contact_ListView_Adapter(Group_Form.this,R.layout.contacts_list_layout,Item_list);
                Add_Member_ListView.setAdapter(adapter);
                Log.d("onActivityResult: ", cNumber);
            }
        }
    }
}
