package com.zersey.roz;

import android.media.Image;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class OnBoarding_Screens extends AppCompatActivity {
    private ViewPager Slider_ViewPager;
    private LinearLayout Dot_Layout,background_Layout;
    private OnBoardingSliderAdapter Slider_Adapter;
    private ImageView First,Second,Third;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding_screens);
        getSupportActionBar().hide();
        background_Layout=(LinearLayout)findViewById(R.id.Boarding_Screen_Relative);
        background_Layout.setBackgroundColor(getResources().getColor(R.color.White));

        final int[] Slider_Color={
                R.color.Red,
                R.color.Green,
                R.color.newdarkblue
        };
        final int[] Slide_Image={
                R.drawable.notepad,
                R.drawable.cardiogram,
                R.drawable.cardiogramone
        };
        First=(ImageView)findViewById(R.id.First_Dot);
        Second=(ImageView)findViewById(R.id.Second_Dot);
        Third=(ImageView)findViewById(R.id.Third_Dot);
        Second.setImageResource(R.drawable.circle);
        Third.setImageResource(R.drawable.circle);
        final LinearLayout.LayoutParams FirstParams=(LinearLayout.LayoutParams) First.getLayoutParams();
        final LinearLayout.LayoutParams SecondParams=(LinearLayout.LayoutParams) Second.getLayoutParams();
        final LinearLayout.LayoutParams ThirdParams=(LinearLayout.LayoutParams) Third.getLayoutParams();
        //layoutParams.width=20;
        //layoutParams.height=20;

        Slider_ViewPager=(ViewPager)findViewById(R.id.Slider_ViewPager);

        Slider_Adapter=new OnBoardingSliderAdapter(getApplicationContext());
        Slider_ViewPager.setAdapter(Slider_Adapter);
        Slider_ViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //Slider_Adapter.notifyDataSetChanged();

            }

            @Override
            public void onPageSelected(final int position) {
                //Slider_Adapter.notifyDataSetChanged();
                if (position==0)
                { Slider_Adapter.Animate();
                Toast.makeText(OnBoarding_Screens.this,"Page Scrolled",Toast.LENGTH_LONG).show();
                background_Layout.setBackgroundColor(getResources().getColor(R.color.White));
                FirstParams.width=FirstParams.height=100;
                SecondParams.width=SecondParams.height=30;
                ThirdParams.width=ThirdParams.height=30;
                    First.setLayoutParams(FirstParams);
                    Third.setLayoutParams(ThirdParams);
                    Second.setLayoutParams(SecondParams);
                    First.setImageResource(Slide_Image[position]);
                    Second.setImageResource(R.drawable.circle);
                    Third.setImageResource(R.drawable.circle);
                    }
                if (position==1)
                {
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //Do something after 100ms
                            Slider_Adapter.Animate();
                            Toast.makeText(OnBoarding_Screens.this,"Page Scrolled",Toast.LENGTH_LONG).show();
                            background_Layout.setBackgroundColor(getResources().getColor(R.color.White));
                            FirstParams.width=FirstParams.height=30;
                            SecondParams.width=SecondParams.height=100;
                            ThirdParams.width=ThirdParams.height=30;
                            First.setLayoutParams(FirstParams);
                            Third.setLayoutParams(ThirdParams);
                            Second.setLayoutParams(SecondParams);
                            First.setImageResource(R.drawable.circle);
                            Second.setImageResource(Slide_Image[position]);
                            Third.setImageResource(R.drawable.circle);
                        }
                    }, 200);

                 }
                if (position==2)
                {Slider_Adapter.Animate();
                Toast.makeText(OnBoarding_Screens.this,"Page Scrolled",Toast.LENGTH_LONG).show();
                    background_Layout.setBackgroundColor(getResources().getColor(R.color.White));
                    FirstParams.width=FirstParams.height=30;
                    SecondParams.width=SecondParams.height=30;
                    ThirdParams.width=ThirdParams.height=100;
                    First.setLayoutParams(FirstParams);
                    Third.setLayoutParams(ThirdParams);
                    Second.setLayoutParams(SecondParams);
                    First.setImageResource(R.drawable.circle);
                    Second.setImageResource(R.drawable.circle);
                    Third.setImageResource(Slide_Image[position]);
                    }
                //Slider_Adapter.Animate();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
}
