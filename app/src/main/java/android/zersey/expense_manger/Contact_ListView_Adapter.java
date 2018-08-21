package android.zersey.expense_manger;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Contact_ListView_Adapter extends ArrayAdapter<Custom_Contact_items> {

    private int layoutResource;
    private List<Custom_Contact_items> list;
    public Contact_ListView_Adapter(@NonNull Context context, int resource,List<Custom_Contact_items> Custom_item_contact) {
        super(context, resource,Custom_item_contact);
        this.layoutResource = resource;
        list=Custom_item_contact;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(R.layout.contacts_list_layout, null);
        }

        Custom_Contact_items Contactitems = getItem(position);

        if ( Contactitems!= null) {
            TextView Contact_Name = (TextView) view.findViewById(R.id.Contact_Name_TextView);
            TextView Contact_Number = (TextView) view.findViewById(R.id.Contact_Number_TextView);




            if (Contact_Name != null) {
                Contact_Name.setText(Contactitems.getContact_Person_Name());
            }
            if (Contact_Number != null) {
                Contact_Number.setText(Contactitems.getContact_Person_Number());
            }

        }

        return view;
    }


}
