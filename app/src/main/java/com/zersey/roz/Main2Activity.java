package com.zersey.roz;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.gson.JsonObject;
import com.zersey.roz.Data.TransactionDbHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Main2Activity extends BaseActivity {

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private String[] tags = new String[3];
	private List<GroupModel> list;
	private TransactionDbHelper mDbHelper;
	private List<TaskModel> taskList;
	private List<BillModel> billList;
	private Animator animator;
	private int radiusEnd;

	public FragmentRefreshListener getFragmentRefreshListener() {
		return fragmentRefreshListener;
	}

	public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
		this.fragmentRefreshListener = fragmentRefreshListener;
	}

	private FragmentRefreshListener fragmentRefreshListener;
	private ChatFragmentRefreshListener chatFragmentRefreshListener;

	public ChatFragmentRefreshListener getChatFragmentRefreshListener() {
		return chatFragmentRefreshListener;
	}

	public void setChatFragmentRefreshListener(ChatFragmentRefreshListener
			                                           chatFragmentRefreshListener) {
		this.chatFragmentRefreshListener = chatFragmentRefreshListener;
	}

	public interface FragmentRefreshListener {
		void onRefresh(List<GroupModel> list);

		void onTaskRefresh(List<TaskModel> taskList);

		void onBillsRefresh(List<BillModel> billsList);
	}

	public interface ChatFragmentRefreshListener {
		void onRefresh(List<GroupModel> list);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		list = new ArrayList<>();
		taskList = new ArrayList<>();
		billList = new ArrayList<>();
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		ActionBar actionbar = getSupportActionBar();
		if (actionbar != null) {
			actionbar.setDisplayHomeAsUpEnabled(true);
			actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
		}
		mDbHelper = TransactionDbHelper.getInstance(this);
		fetchContacts();
		mDrawerLayout = findViewById(R.id.drawer_layout);

		mDrawerToggle =
				new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string
						.navigation_drawer_open,
						R.string.navigation_drawer_close);
		mDrawerLayout.addDrawerListener(mDrawerToggle);
		mDrawerLayout.post(new Runnable() {
			@Override
			public void run() {
				mDrawerToggle.syncState();
			}
		});

		NavigationView navigationView = findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(
				new NavigationView.OnNavigationItemSelectedListener() {
					@Override
					public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
						menuItem.setChecked(true);
						switch (menuItem.getItemId()) {
							case R.id.nav_gallery:
								Intent i = new Intent(Main2Activity.this, Contact_List_Activity
										.class);
								startActivity(i);
								return true;

							case R.id.nav_logout:
								logout();
								return true;
						}

						mDrawerLayout.closeDrawers();
						return true;
					}
				});

		TabLayout tabLayout = findViewById(R.id.Tab_layout);
		final FloatingActionsMenu fab = findViewById(R.id.fab);

		tabLayout.addTab(tabLayout.newTab().setText("Dashboard"));
		tabLayout.addTab(tabLayout.newTab().setText("Chat"));
		tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

		ViewPager viewPager = findViewById(R.id.container);
		PagerAdapter adapter =
				new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
		viewPager.setAdapter(adapter);
		tabLayout.setupWithViewPager(viewPager);

		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset,
			                           int positionOffsetPixels) {
				if (position == 1) {
					fab.setVisibility(View.GONE);
				} else {
					fab.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onPageSelected(int position) {
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});

		final View layer = findViewById(R.id.layer);
		final View root = findViewById(R.id.root);
		root.getViewTreeObserver()
				.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						root.getViewTreeObserver().removeOnGlobalLayoutListener(this);
						radiusEnd = (int) Math.hypot(root.getWidth(), root.getHeight());
					}
				});

		fab.setOnFloatingActionsMenuUpdateListener(
				new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
					@Override
					public void onMenuExpanded() {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
							animator = ViewAnimationUtils.createCircularReveal(layer,
									(int) fab.getX() + fab.getWidth(), (int) fab.getY() + fab
											.getHeight(),
									0, radiusEnd);
							animator.start();
						}
						layer.setVisibility(VISIBLE);
					}

					@Override
					public void onMenuCollapsed() {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
							animator = ViewAnimationUtils.createCircularReveal(layer,
									(int) fab.getX() + fab.getWidth(), (int) fab.getY() + fab
											.getHeight(),
									radiusEnd, 0);
							animator.addListener(new Animator.AnimatorListener() {
								@Override
								public void onAnimationStart(Animator animation) {

								}

								@Override
								public void onAnimationEnd(Animator animation) {
									layer.setVisibility(GONE);
								}

								@Override
								public void onAnimationCancel(Animator animation) {

								}

								@Override
								public void onAnimationRepeat(Animator animation) {

								}
							});
							animator.start();
						} else {
							layer.setVisibility(GONE);
						}
					}
				});

		SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);

		if (!prefs.getBoolean("dataFetched", false)) {
			prefs.edit().putBoolean("dataFetched", true).apply();
			fetchGroupsFromServer(prefs);
			fetchTasksFromServer();
			fetchBillsFromServer();
		} else {
			list.addAll(mDbHelper.getGroups(0));
			taskList.addAll(mDbHelper.getTask(-1));
			billList.addAll(mDbHelper.getAllEntries());
		}
	}

	private void fetchBillsFromServer() {
		if (NetworkUtil.hasInternetConnection(this)) {
			Call<JsonObject> result =
					NetworkUtil.getRestAdapter(this).fetchAllUserEntry(null, null);
			result.enqueue(new Callback<JsonObject>() {
				@Override
				public void onResponse(@NonNull Call<JsonObject> call,
				                       @NonNull Response<JsonObject> response) {
					Util.getNotesList(Main2Activity.this, response, false);
					//GroupsFragment.billRecyclerAdapter.addAll(mDbHelper.getAllEntries());
					getFragmentRefreshListener().onBillsRefresh(mDbHelper.getAllEntries());
				}

				@Override
				public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
					t.printStackTrace();
				}
			});
			dismissProgress();
		}
	}

	private void fetchTasksFromServer() {
		Call<JsonObject> taskResult =
				NetworkUtil.getRestAdapter(Main2Activity.this).fetchGroupNotes(null, null);
		taskResult.enqueue(new Callback<JsonObject>() {
			@Override
			public void onResponse(@NonNull Call<JsonObject> call,
			                       @NonNull Response<JsonObject> response) {
				taskList.addAll(Util.parseTaskResponse(response.body()));
				mDbHelper.addTasks(taskList);
				//taskList.clear();
				//taskList.addAll(dbHelper.getTasks());
				getFragmentRefreshListener().onTaskRefresh(taskList);
			}

			@Override
			public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {

			}
		});
	}

	private void fetchGroupsFromServer(SharedPreferences prefs) {
		if (NetworkUtil.hasInternetConnection(this)) {
			showProgress("Getting your groups...");
			Call<JsonObject> result =
					NetworkUtil.getRestAdapter(this).fetchGroups(prefs.getString("userid", null),
							null);
			result.enqueue(new Callback<JsonObject>() {
				@Override
				public void onResponse(@NonNull Call<JsonObject> call,
				                       @NonNull Response<JsonObject> response) {
					list.addAll(Util.parseGroupResponse(response.body()));
					mDbHelper.addGroups(list);
					for (GroupModel groupModel : list) {
						Call<JsonObject> groupResult =
								NetworkUtil.getRestAdapter(Main2Activity.this)
										.fetchAllUserEntry(null, Long.toString(groupModel
												.getGroupId()));
						groupResult.enqueue(new Callback<JsonObject>() {
							@Override
							public void onResponse(@NonNull Call<JsonObject> call,
							                       @NonNull Response<JsonObject> response) {
								Util.getNotesList(Main2Activity.this, response, true);
							}

							@Override
							public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
								t.printStackTrace();
							}
						});
					}

					list.clear();
					list.addAll(mDbHelper.getGroups(0));

					getFragmentRefreshListener().onRefresh(list);
					getChatFragmentRefreshListener().onRefresh(list);
				}

				@Override
				public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
					t.printStackTrace();
				}
			});
		}
	}

	private void fetchContacts() {
		thread.start();
	}

	Thread thread = new Thread(new Runnable() {
		@Override
		public void run() {
			String phone = getSharedPreferences("login", MODE_PRIVATE).getString("phone", null);
			if (phone != null) {
				phone = phone.substring(phone.length() - 10, phone.length());
			}
			int count = mDbHelper.getContactCount();
			List<String> contactModelList = new ArrayList<>();
			if (count == 0) {
				Cursor phoneCursor =
						getContentResolver().query(ContactsContract.CommonDataKinds.Phone
										.CONTENT_URI,
								null, null, null, null);
				if (phoneCursor != null) {
					while (phoneCursor.moveToNext()) {
						String name = phoneCursor.getString(phoneCursor.getColumnIndex(
								ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
						String hasPhone = phoneCursor.getString(phoneCursor.getColumnIndex(
								ContactsContract.CommonDataKinds.Phone.HAS_PHONE_NUMBER));

						String cNumber = null;
						//String code = null;
						if (hasPhone.equalsIgnoreCase("1")) {

							cNumber = phoneCursor.getString(phoneCursor.getColumnIndex(
									ContactsContract.CommonDataKinds.Phone.NUMBER));
							cNumber = cNumber.replaceAll("[^0-9]", "");
							if (cNumber.length() > 10) {
								//code = cNumber.substring(0, cNumber.length() - 10);
								cNumber = cNumber.substring(cNumber.length() - 10);
							}
						}

						if (cNumber != null && !cNumber.equals(phone)) {
							ContactModel model = new ContactModel();
							model.setName(name);
							model.setNumber(cNumber);

							if (contactModelList.size() == 200) {
								new ServerUtil(Main2Activity.this).verifyContacts(
										contactModelList.toArray(new String[0]));
								contactModelList.clear();
							}

							contactModelList.add(cNumber);
							mDbHelper.addContact(model);
						}
					}
					new ServerUtil(Main2Activity.this).verifyContacts(
							contactModelList.toArray(new String[0]));
					phoneCursor.close();
				}
			}
		}
	});

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main2, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_settings:
				Intent i = new Intent(getApplicationContext(), OnBoarding_Screens.class);
				startActivity(i);
				return true;

			case android.R.id.home:
				mDrawerLayout.openDrawer(GravityCompat.START);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public class PagerAdapter extends FragmentPagerAdapter {
		int numberOfTabs;

		PagerAdapter(FragmentManager fm, int numberOfTabs) {
			super(fm);
			this.numberOfTabs = numberOfTabs;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {

				case 0:
					Fragment fragment = new GroupsFragment();
					Bundle bundle = new Bundle();
					bundle.putSerializable("groupList", (Serializable) list);
					bundle.putSerializable("taskList", (Serializable) taskList);
					bundle.putSerializable("billList", (Serializable) billList);
					fragment.setArguments(bundle);
					return fragment;

				case 1:
					return new ChatFragment();

				default:
					return null;
			}
		}

		@Nullable
		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
				case 0:
					return "Dashboard";
				case 1:
					return "Chats";
			}
			return null;
		}

		@Override
		public int getCount() {
			return numberOfTabs;
		}

		@Override
		public int getItemPosition(@NonNull Object object) {
			return POSITION_NONE;
		}

		@NonNull
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
			tags[position] = createdFragment.getTag();
			return createdFragment;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Fragment fragment = getSupportFragmentManager().findFragmentByTag(tags[0]);
		fragment.onActivityResult(requestCode, resultCode, data);
	}
}


