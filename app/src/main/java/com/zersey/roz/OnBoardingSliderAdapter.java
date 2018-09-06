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

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class OnBoardingSliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    private int pos=0;
    private ImageView Slider_Image;
    private TextView Heading,Description;
    private RelativeLayout relativeLayout;

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

    public void Animate(){
        YoYo.with(Techniques.FadeInUp)
                .duration(1000)
                .repeat(0)
                .playOn(Slider_Image);
        YoYo.with(Techniques.FadeInUp)
                .duration(1000)
                .repeat(0)
                .playOn(Heading);
        YoYo.with(Techniques.FadeInUp)
                .duration(1000)
                .repeat(0)
                .playOn(Description);
        /*YoYo.with(Techniques.FadeIn)
                .duration(1500)
                .repeat(0)
                .playOn(relativeLayout);*/
    }

    public int[] Slide_Image={
      R.drawable.notepad,
      R.drawable.cardiogram,
      R.drawable.cardiogramone
    };

    public int[] Slider_Color={
            R.color.colorAccent,
            R.color.Orange,
            R.color.newdarkblue
    };

    public String des="It refers to the systems deployed by a business to process, pay, and audit employee-initiated expenses.";

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.slider_layout, container, false);
        Slider_Image = (ImageView) view.findViewById(R.id.Slider_Image);
        Heading = (TextView) view.findViewById(R.id.Heading);
        Description = (TextView) view.findViewById(R.id.Slider_Description);
        relativeLayout=(RelativeLayout)view.findViewById(R.id.Relative_Slider);


        Slider_Image.setImageResource(Slide_Image[position]);
        Heading.setText(Slider_Headings[position]);
        Description.setText(des);
        //relativeLayout.setBackgroundColor(context.getResources().getColor(Slider_Color[position]));
        Animate();
        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
