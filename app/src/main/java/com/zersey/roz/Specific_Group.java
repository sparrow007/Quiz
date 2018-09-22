package com.zersey.roz;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Specific_Group extends AppCompatActivity
	implements Group_Balances.OnFragmentInteractionListener,
	Group_Transactions.OnFragmentInteractionListener, Group_About.OnFragmentInteractionListener,Task_Fragment.OnFragmentInteractionListener {
	private ViewPager mViewPager;
	private TabLayout tab_layout;
	private PagerAdapter adapter;
	private TextView Group_Name;
	private LinearLayout Search_Layout;
	private EditText Search_Edit;
	private ImageButton Back,Search;
	private FloatingActionButton fab;
	public GroupModel model;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_specific_group);
		Group_Name = findViewById(R.id.Group_Name_TextView);
		Back=findViewById(R.id.back_Button_group);
		Search=findViewById(R.id.Search_Icon_group);
        Search_Edit=findViewById(R.id.Search_Edit);
        Search_Layout=findViewById(R.id.Search_Layout);
        Search_Layout.setVisibility(View.GONE);
        model = (GroupModel) getIntent().getSerializableExtra("group");
        //fab=findViewById(R.id.fab_Transaction);
        Search.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Search_Layout.setVisibility(View.VISIBLE);
				Search.setVisibility(View.GONE);
			}
		});
        Back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Search_Layout.setVisibility(View.GONE);
				Search.setVisibility(View.VISIBLE);
			}
		});
		Group_Name.setText(model.getGroupName());
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
		Search_Edit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				Group_Transactions.adapter.Search(s.toString());
			}

			@Override
			public void afterTextChanged(Editable s) {
				Group_Transactions.adapter.Search(s.toString());
			}
		});

	}

	public void initTabLayout() {
		tab_layout = findViewById(R.id.Tab_Layout);
		tab_layout.addTab(tab_layout.newTab().setText("Items"));
		tab_layout.addTab(tab_layout.newTab().setText("Todo-Tasks"));
		tab_layout.addTab(tab_layout.newTab().setText("Balances"));
		tab_layout.addTab(tab_layout.newTab().setText("About"));
		tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);
	}

	public void initViewPager() {
		mViewPager = findViewById(R.id.Group_Pager);
		adapter =
			new Specific_Group.PagerAdapter(getSupportFragmentManager(), tab_layout.getTabCount());
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
/*				if (position == 0 && positionOffset == 0)
					fab.show();
				else if (position ==2 || position==3)
					fab.hide();
*/
			}

			@Override public void onPageSelected(int position) {

			}

			@Override public void onPageScrollStateChanged(int state) {

			}
		});
	}

	@Override public void onFragmentInteraction(Uri uri) {

	}

	public void addTransactionInGroup() {
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("group", model);
		startActivityForResult(intent, 123);
	}

	@Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	/*		if (resultCode == Activity.RESULT_OK){
				Group_Transactions.adapter.addItem((IncomeModel) data.getSerializableExtra("model"));
			}*/

	}

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
					Fragment frag = new Task_Fragment();
				Bundle args = new Bundle();
				args.putSerializable("group", model);
				frag.setArguments(args);
				return frag;
				case 2:
					return new Group_Balances();
				case 3:
					Fragment frag1 = new Group_About();
					Bundle args1 = new Bundle();
					args1.putSerializable("group", model);
					Log.d( "getItem: ",model+"");
					frag1.setArguments(args1);
					return frag1;
				default:
					return null;
			}

			//return PlaceholderFragment.newInstance(position + 1);
		}

		@Override public int getCount() {
			return mnooftabes;
		}

		@Override public int getItemPosition(@NonNull Object object) {
			return POSITION_NONE;
		}
	}
}
