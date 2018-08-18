package android.zersey.expense_manger;

import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.TextView;

public class Specific_Group extends AppCompatActivity implements
        Group_Balances.OnFragmentInteractionListener,Group_Transactions.OnFragmentInteractionListener,Group_About.OnFragmentInteractionListener{
    private ViewPager mViewPager;
    private TabLayout tab_layout;
    private PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_group);
       initTabLayout();
       initViewPager();
        tab_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
//				adapter.notifyDataSetChanged();
            }

            @Override public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void initTabLayout(){
        tab_layout = findViewById(R.id.Tab_Layout);
        tab_layout.addTab(tab_layout.newTab().setText("Transactions"));
        tab_layout.addTab(tab_layout.newTab().setText("Balances"));
        tab_layout.addTab(tab_layout.newTab().setText("About"));
        tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);

    }

    public void initViewPager(){
        mViewPager=findViewById(R.id.Group_Pager);
        adapter = new Specific_Group.PagerAdapter(getSupportFragmentManager(), tab_layout.getTabCount());
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layout));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset,
                                                 int positionOffsetPixels) {
                Fragment fragment =
                        ((FragmentPagerAdapter) mViewPager.getAdapter()).getItem(position);
                if (position == 1) {
                    fragment.onStart();
                    //adapter.notifyDataSetChanged();
                }
            }

            @Override public void onPageSelected(int position) {

            }

            @Override public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

/*TextView tv=(TextView) LayoutInflater.from(this).inflate(R.layout.custom_tabs,null);
        tv.setText("Transactions");
        TextView tv1=(TextView) LayoutInflater.from(this).inflate(R.layout.custom_tabs,null);
        tv1.setText("Balances");
        TextView tv2=(TextView) LayoutInflater.from(this).inflate(R.layout.custom_tabs,null);
        tv2.setText("About");
        mTypeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/Roboto-Regular.ttf");
        tv.setTypeface(mTypeface);
        tab_layout.getTabAt(0).setCustomView(tv);
        tab_layout.getTabAt(1).setCustomView(tv1);
        tab_layout.getTabAt(2).setCustomView(tv2);*/

    public class PagerAdapter extends FragmentPagerAdapter {
        int mnooftabes;

        PagerAdapter(FragmentManager fm, int Numberoftabes) {
            super(fm);
            mnooftabes = Numberoftabes;
        }

        @Override public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return new Group_Transactions();
                case 1:
                    return new Group_Balances();
                case 2:
                    return new Group_About();
                default:
                    return null;
            }

            //return PlaceholderFragment.newInstance(position + 1);
        }

        @Override public int getCount() {
            // Show 3 total pages.
            return mnooftabes;
        }

        @Override public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }
    }

}