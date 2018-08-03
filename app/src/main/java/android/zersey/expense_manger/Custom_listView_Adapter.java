package android.zersey.expense_manger;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Custom_listView_Adapter extends ArrayAdapter<Custom_items> {

    private int layoutResource;
    private ArrayList<Custom_items> list;
    public Custom_listView_Adapter(@NonNull Context context, int resource, ArrayList<Custom_items> Custom_item_list) {
        super(context, resource,Custom_item_list);
        this.layoutResource = resource;
        list=Custom_item_list;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            view = layoutInflater.inflate(layoutResource, null);
        }

        Custom_items customitems = getItem(position);

        if ( customitems!= null) {
            TextView customcategory = (TextView) view.findViewById(R.id.Custom_category);
            TextView customamount = (TextView) view.findViewById(R.id.Custom_amount);
            TextView customdate = (TextView) view.findViewById(R.id.Custom_date);
            TextView customtitle = (TextView) view.findViewById(R.id.Custom_title);
            ImageView category_logo=(ImageView)view.findViewById(R.id.Custom_logo);


            if (customcategory != null) {
                customcategory.setText(customitems.getCategory());
                if (customitems.getCategory().equals("Clothing")){
                    category_logo.setImageResource(R.drawable.cloth);
                }else if (customitems.getCategory().equals("Entertainment")){
                    category_logo.setImageResource(R.drawable.entertainment);
                }else if (customitems.getCategory().equals("Food")){
                    category_logo.setImageResource(R.drawable.food);
                }else if (customitems.getCategory().equals("Fuel")){
                    category_logo.setImageResource(R.drawable.fuel);
                }else if (customitems.getCategory().equals("Health")){
                    category_logo.setImageResource(R.drawable.health);
                }else if (customitems.getCategory().equals("Income")){
                    category_logo.setImageResource(R.drawable.salary);
                }

            }
            if (customtitle != null) {
                customtitle.setText(customitems.getTitle());
            }
            if (customamount != null) {
                customamount.setText(customitems.getAmount());
            }
            if (customdate != null) {
                customdate.setText(customitems.getDate());
            }
        }

        return view;
    }



}
