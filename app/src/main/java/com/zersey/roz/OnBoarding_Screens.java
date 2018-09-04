package com.zersey.roz;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class OnBoarding_Screens extends AppCompatActivity {
    private ViewPager Slider_ViewPager;
    private LinearLayout Dot_Layout;

    private OnBoardingSliderAdapter Slider_Adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding_screens);
        getSupportActionBar().hide();

        Slider_ViewPager=(ViewPager)findViewById(R.id.Slider_ViewPager);
        Slider_Adapter=new OnBoardingSliderAdapter(getApplicationContext());

        Slider_ViewPager.setAdapter(Slider_Adapter);
    }
}
