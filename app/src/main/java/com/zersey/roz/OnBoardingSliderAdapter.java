package com.zersey.roz;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class OnBoardingSliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public OnBoardingSliderAdapter(Context context){
        this.context=context;
    }

    @Override
    public int getCount() {
        return Slider_Headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(RelativeLayout)object;
    }

    public String[] Slider_Headings={
            "EAT",
            "SLEEP",
            "CODE"
    };

    public String des="It refers to the systems deployed by a business to process, pay, and audit employee-initiated expenses." +
            " These costs include, but are not limited to, expenses incurred for travel and entertainment.";

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.slider_layout,container,false);
        ImageView Slider_Image=(ImageView)view.findViewById(R.id.Slider_Image) ;
        TextView Heading=(TextView)view.findViewById(R.id.Heading);
        TextView Description=(TextView)view.findViewById(R.id.Slider_Description);

        Slider_Image.setImageResource(R.drawable.temple);
        Heading.setText(Slider_Headings[position]);
        Description.setText(des);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
