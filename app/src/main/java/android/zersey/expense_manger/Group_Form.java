package android.zersey.expense_manger;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class Group_Form extends AppCompatActivity {
private LinearLayout Add_Member_Layout;
private ListView Add_Member_ListView;
private ArrayList<Custom_Contact_items> Item_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_form);
        Item_list=new ArrayList<>();
        Add_Member_Layout=(LinearLayout)findViewById(R.id.Add_Member_Layout);
        Add_Member_ListView=(ListView)findViewById(R.id.Add_Member_ListView);

    }

    public void Add_Members(View view){
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

        startActivityForResult(intent, 0);
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
