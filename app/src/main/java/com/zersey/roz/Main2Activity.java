package com.zersey.roz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.JsonObject;
import com.zersey.roz.Data.TransactionDbHelper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Main2Activity extends BaseActivity
	implements Graphs.OnFragmentInteractionListener, Groups.OnFragmentInteractionListener {

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
	private boolean Added = false;
	private ViewPager mViewPager;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private String[] tags = new String[3];
	private List<GroupModel> list;
	private TransactionDbHelper mDbHelper;

	public FragmentRefreshListener getFragmentRefreshListener() {
		return fragmentRefreshListener;
	}

	public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
		this.fragmentRefreshListener = fragmentRefreshListener;
	}

	private FragmentRefreshListener fragmentRefreshListener;

	public interface FragmentRefreshListener {
		void onRefresh(List<GroupModel> list);
	}

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		list = new ArrayList<>();
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		ActionBar actionbar = getSupportActionBar();
		if (actionbar != null) {
			actionbar.setDisplayHomeAsUpEnabled(true);
			actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
		}
		mDbHelper = TransactionDbHelper.getInstance(this);
		Fetch_Contacts();
		mDrawerLayout = findViewById(R.id.drawer_layout);
		mDrawerToggle =
			new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open,
				R.string.navigation_drawer_close);
		mDrawerLayout.addDrawerListener(mDrawerToggle);
		mDrawerLayout.post(new Runnable() {
			@Override public void run() {
				mDrawerToggle.syncState();
			}
		});

		NavigationView navigationView = findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(
			new NavigationView.OnNavigationItemSelectedListener() {
				@Override public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
					// set item as selected to persist highlight
					menuItem.setChecked(true);
					if (menuItem.getItemId() == R.id.nav_gallery) {
						Intent i = new Intent(Main2Activity.this, Contact_List_Activity.class);
						startActivity(i);
					}
					// close drawer when item is tapped
					mDrawerLayout.closeDrawers();

					// Add code here to update the UI based on the item selected
					// For example, swap UI fragments here

					return true;
				}
			});

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the activity.
		/*      mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

		 */  // Set up the ViewPager with the sections adapter.
        /*Category_text=getIntent().getStringExtra("Category");
        Date_text=getIntent().getStringExtra("Date");
        Amount_text=getIntent().getStringExtra("Amount");
        Notes_text=getIntent().getStringExtra("Notes");*/
		//        Image_uri=Uri.parse(getIntent().getStringExtra("Image"));

		TabLayout tab_layout = findViewById(R.id.Tab_layout);

		tab_layout.addTab(tab_layout.newTab().setText("Dashboard"));
		tab_layout.addTab(tab_layout.newTab().setText("Chat"));
		tab_layout.setTabGravity(TabLayout.GRAVITY_FILL);

		mViewPager = findViewById(R.id.container);
		adapter = new PagerAdapter(getSupportFragmentManager(), tab_layout.getTabCount());
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

		Transaction_linearLayout = mViewPager.findViewById(R.id.Transaction_linearLayout);

		FloatingActionButton fab = findViewById(R.id.fab);
		fab.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
				Intent intent = new Intent(Main2Activity.this, MainActivity.class);
				startActivity(intent);
			}
		});
		//if(!TextUtils.isEmpty(Category_text)){ addTransaction();}

		//TransactionDbHelper dbHelper = new TransactionDbHelper(this);
		//if (NetworkUtil.hasInternetConnection(this) && dbHelper.getEntriesCount() == 0) {
		//	showProgress("Getting your notes...");
		//	Call<JsonObject> result = NetworkUtil.getRestAdapter(this).fetchAllUserEntry();
		//	result.enqueue(new Callback<JsonObject>() {
		//		@Override public void onResponse(@NonNull Call<JsonObject> call,
		//			@NonNull Response<JsonObject> response) {
		//			JsonObject obj = response.body();
		//			if (obj != null && obj.has("success")) {
		//				String access = obj.get("success").getAsString();
		//				if (access.equals("UnauthorizedAccess") || access.equals(
		//					"Unauthorized Access")) {
		//					Toast.makeText(Main2Activity.this, "Unauthorized access",
		//						Toast.LENGTH_SHORT).show();
		//					return;
		//				}
		//			}
		//			Util.getNotesList(Main2Activity.this, response);
		//		}
		//
		//		@Override
		//		public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
		//			t.printStackTrace();
		//		}
		//	});
		//}
		//
		//NetworkChangeReceiver br = new NetworkChangeReceiver();
		//IntentFilter netFilter = new IntentFilter();
		//netFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
		//registerReceiver(br, netFilter);

		SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);

		final TransactionDbHelper dbHelper = TransactionDbHelper.getInstance(this);
		if (NetworkUtil.hasInternetConnection(this) && dbHelper.getGroupsCount() == 0) {
			showProgress("Getting your groups...");
			Call<JsonObject> result =
				NetworkUtil.getRestAdapter(this).fetchGroups(prefs.getString("userid", null), null);
			result.enqueue(new Callback<JsonObject>() {
				@Override
				public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
					list.addAll(Util.parseGroupResponse(response.body()));
					dbHelper.addGroups(list);
					list.clear();
					list.addAll(dbHelper.getGroups(0));
					getFragmentRefreshListener().onRefresh(list);
				}

				@Override public void onFailure(Call<JsonObject> call, Throwable t) {

				}
			});
		} else {
			list.addAll(dbHelper.getGroups(0));
		}

		if (NetworkUtil.hasInternetConnection(this) && dbHelper.getEntriesCount() == 0) {
			Call<JsonObject> result = NetworkUtil.getRestAdapter(this).fetchAllUserEntry();
			result.enqueue(new Callback<JsonObject>() {
				@Override
				public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
					Util.getNotesList(Main2Activity.this, response);
					Groups.adapter.addAll(dbHelper.getAllEntries());
				}

				@Override public void onFailure(Call<JsonObject> call, Throwable t) {

				}
			});
			dismissProgress();
		}
	}

	public void Fetch_Contacts() {
		thread.start();
	}

	Thread thread = new Thread(new Runnable() {
		@Override public void run() {
			int count = mDbHelper.getContactCount();
			List<ContactModel> contactModelList = new ArrayList<>();
			if (count <= 0) {
				Cursor phones =
					getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null, null, null, null);
				while (phones.moveToNext()) {
					String name = phones.getString(
						phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
					String hasPhone = phones.getString(
						phones.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

					String cNumber = null, code = null;
					if (hasPhone.equalsIgnoreCase("1")) {

						cNumber = phones.getString(
							phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
						cNumber = cNumber.replace(" ", "").replace("+", "");
						if (cNumber.length() > 10) {
							code = cNumber.substring(0, cNumber.length() - 10);
							cNumber = cNumber.substring(cNumber.length() - 10);
						} else {
							code = "91";
						}
					}

					ContactModel model = new ContactModel();
					model.setName(name);
					model.setNumber(cNumber);
					contactModelList.add(model);
					long rowId = mDbHelper.addContact(model);
					if (rowId != -1) {
						new ServerUtil(Main2Activity.this).getUserIdFromServer(rowId, cNumber,
							code);
					}
				}
				phones.close();
			}
		}
	});

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

	@Override public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main2, menu);
		return true;
	}

	@Override public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_settings:
				return true;

			case android.R.id.home:
				mDrawerLayout.openDrawer(GravityCompat.START);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override public void onFragmentInteraction(Uri uri) {

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

		PagerAdapter(FragmentManager fm, int Numberoftabes) {
			super(fm);
			mnooftabes = Numberoftabes;
		}

		@Override public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class below).
			switch (position) {

				case 0:
					Fragment fragment = new Groups();
					Bundle bundle = new Bundle();
					bundle.putSerializable("groupList", (Serializable) list);
					fragment.setArguments(bundle);
					return fragment;

				case 1:
					return new ChatFragment();

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

		@NonNull @Override public Object instantiateItem(ViewGroup container, int position) {

			Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
			tags[position] = createdFragment.getTag();
			return createdFragment;
		}
	}

	@Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Fragment fragment = getSupportFragmentManager().findFragmentByTag(tags[2]);
		fragment.onActivityResult(requestCode, resultCode, data);
	}
}


