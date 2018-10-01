package com.zersey.roz;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SpecificTransactions extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;

    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;

    private LinearLayout mTitleContainer;
    private TextView mTitle;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private String Amount,Date,Title;
    private String Updated_Category,Updated_Type,CardClicked;
    private int Updated_Id;
    private TextView Amount_TextView,Date_TextView,Title_TextView;
    private Button Settle_Button;
    private BillModel model;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_transactions);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mAppBarLayout=(AppBarLayout)findViewById(R.id.appbar);
        setSupportActionBar(mToolbar);
        Amount_TextView=(TextView)findViewById(R.id.Specific_Amount);
        Date_TextView=(TextView)findViewById(R.id.Specific_Date);
        Settle_Button=(Button)findViewById(R.id.Settle_Button);
        //Title_TextView=(TextView)findViewById(R.id.toolbar_text);
      // Amount=getIntent().getStringExtra("Amount");
        //Title=getIntent().getStringExtra("Title");
        //Date=getIntent().getStringExtra("DateCreated");
        model = (BillModel) getIntent().getSerializableExtra("model");
        CardClicked = getIntent().getStringExtra("CardClicked");
        Updated_Category = getIntent().getStringExtra("Category");
        Amount = getIntent().getStringExtra("Amount");
        Title = getIntent().getStringExtra("Title");
        Date = getIntent().getStringExtra("DateCreated");
        Updated_Type = getIntent().getStringExtra("Type");
        Updated_Id = getIntent().getIntExtra("_ID", 0);
        if(!TextUtils.isEmpty(Amount) && !TextUtils.isEmpty(Date) && !TextUtils.isEmpty(Title) ){
            Amount_TextView.setText("Rs "+Amount);
            Date_TextView.setText(Date);
            getSupportActionBar().setTitle(Title);
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Settle_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                //i.putExtra("pos", getAdapterPosition());
                i.putExtra("_ID", Updated_Id);
                i.putExtra("CardClicked", "Yes");
                i.putExtra("Title", Title);
                i.putExtra("Type", Updated_Type);
                i.putExtra("Category", Updated_Category);
                i.putExtra("Amount", Amount);
                i.putExtra("DateCreated", Date);
                i.putExtra("model", model);
                startActivity(i);
            }
        });
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }


    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(mTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if(mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(mTitleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }
}
