package android.zersey.expense_manger;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

public class Main2Activity extends AppCompatActivity implements Transactions.OnFragmentInteractionListener,Graphs.OnFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    //private SectionsPagerAdapter mSectionsPagerAdapter;
    private PagerAdapter adapter;
    //private String Category_text,Notes_text,Amount_text,Date_text;
    //private Uri Image_uri;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewGroup Transaction_linearLayout;
    private boolean Added=false;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
  /*      mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

      */  // Set up the ViewPager with the sections adapter.
        /*Category_text=getIntent().getStringExtra("Category");
        Date_text=getIntent().getStringExtra("Date");
        Amount_text=getIntent().getStringExtra("Amount");
        Notes_text=getIntent().getStringExtra("Notes");*/
//        Image_uri=Uri.parse(getIntent().getStringExtra("Image"));

        TabLayout tab_layout=(TabLayout)findViewById(R.id.Tab_layout);
        tab_layout.addTab(tab_layout.newTab().setText("Transactions"));
        tab_layout.addTab(tab_layout.newTab().setText("Graphs"));
        tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);

        mViewPager = (ViewPager) findViewById(R.id.container);
        adapter=new PagerAdapter(getSupportFragmentManager(),tab_layout.getTabCount());
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tab_layout));

        tab_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        Transaction_linearLayout=(ViewGroup) mViewPager.findViewById(R.id.Transaction_linearLayout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent=new Intent(Main2Activity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        //if(!TextUtils.isEmpty(Category_text)){ addTransaction();}
    }

    @Override
    public void onBackPressed(){

    }

    /*public void addTransaction(){
        TextView Transaction_Category_text_view,Transaction_Notes_text_view,
                Transaction_Amount_text_view,Transaction_Date_text_view;
        View layout2=LayoutInflater.from(this).inflate(R.layout.transaction_list_layout,Transaction_linearLayout,false);
        Transaction_Category_text_view=(TextView)layout2.findViewById(R.id.Transaction_Category);
        Transaction_Date_text_view=(TextView)layout2.findViewById(R.id.Transaction_Date);
        Transaction_Amount_text_view=(TextView)layout2.findViewById(R.id.Transaction_Amount);
        Transaction_Notes_text_view=(TextView)layout2.findViewById(R.id.Transaction_Notes);
        Transaction_Category_text_view.setText(Category_text);
        Transaction_Amount_text_view.setText(Amount_text);
        Transaction_Date_text_view.setText(Date_text);
        if(!TextUtils.isEmpty(Notes_text)){
            Transaction_Notes_text_view.setText(Notes_text);
        }
        if(Image_uri!=null){

        }
        Transaction_linearLayout.addView(layout2);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    /*public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        //private static final String ARG_SECTION_NUMBER = "section_number";

       // public PlaceholderFragment() {
       // }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
       /* public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main2, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }*/

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class PagerAdapter extends FragmentPagerAdapter {
        int mnooftabes;

        public PagerAdapter(FragmentManager fm,int Numberoftabes) {
            super(fm);
            mnooftabes=Numberoftabes;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch(position){
                case 0:
                    Transactions transactions=new Transactions();
                    return transactions;
                case 1:
                    Graphs graphs=new Graphs();
                    return graphs;
                default: return null;

            }

            //return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return mnooftabes;
        }
    }
}
