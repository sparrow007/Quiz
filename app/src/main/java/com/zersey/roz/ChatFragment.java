package com.zersey.roz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment {

	private RecyclerView recyclerListGroups;
	//public FragGroupClickFloatButton onClickFloatButton;
	//private ArrayList<Group> listGroup;
	//private ListGroupsAdapter groupsAdapter;
	private SwipeRefreshLayout mSwipeRefreshLayout;
	public static final int CONTEXT_MENU_DELETE = 1;
	public static final int CONTEXT_MENU_EDIT = 2;
	public static final int CONTEXT_MENU_LEAVE = 3;
	public static final int REQUEST_EDIT_GROUP = 0;
	public static final String CONTEXT_MENU_KEY_INTENT_DATA_POS = "pos";

	public ChatFragment() {
		// Required empty public constructor
	}

	@Override public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference myRef = database.getReference("group_chats");
	}

	@Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View layout = inflater.inflate(R.layout.fragment_chat, container, false);
		FloatingActionButton addChat = layout.findViewById(R.id.add_chat);
		addChat.setOnClickListener(new View.OnClickListener() {
			@Override public void onClick(View view) {
				startActivity(new Intent(getContext(), ChatActivity.class));
			}
		});
		//listGroup = GroupDB.getInstance(getContext()).getListGroups();
		//recyclerListGroups = (RecyclerView) layout.findViewById(R.id.recycleListGroup);
		//mSwipeRefreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.swipeRefreshLayout);
		//mSwipeRefreshLayout.setOnRefreshListener(this);
		//GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
		//recyclerListGroups.setLayoutManager(layoutManager);
		//groupsAdapter = new ListGroupsAdapter(getContext(), listGroup);
		//recyclerListGroups.setAdapter(groupsAdapter);
		//onClickFloatButton = new FragGroupClickFloatButton();
		//progressDialog = new LovelyProgressDialog(getContext())
		//	.setCancelable(false)
		//	.setIcon(R.drawable.ic_dialog_delete_group)
		//	.setTitle("Deleting....")
		//	.setTopColorRes(R.color.colorAccent);
		//
		//waitingLeavingGroup = new LovelyProgressDialog(getContext())
		//	.setCancelable(false)
		//	.setIcon(R.drawable.ic_dialog_delete_group)
		//	.setTitle("Group leaving....")
		//	.setTopColorRes(R.color.colorAccent);
		//
		//if(listGroup.size() == 0){
		//	//Ket noi server hien thi group
		//	mSwipeRefreshLayout.setRefreshing(true);
		//	getListGroup();
		//}
		return layout;
	}

/*	private void getListGroup(){
		FirebaseDatabase.getInstance().getReference().child("user/"+ StaticConfig.UID+"/group").addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				if(dataSnapshot.getValue() != null) {
					HashMap mapListGroup = (HashMap) dataSnapshot.getValue();
					Iterator iterator = mapListGroup.keySet().iterator();
					while (iterator.hasNext()){
						String idGroup = (String) mapListGroup.get(iterator.next().toString());
						Group newGroup = new Group();
						newGroup.id = idGroup;
						listGroup.add(newGroup);
					}
					getGroupInfo(0);
				}else{
					mSwipeRefreshLayout.setRefreshing(false);
					groupsAdapter.notifyDataSetChanged();
				}
			}

			@Override
			public void onCancelled(DatabaseError databaseError) {
				mSwipeRefreshLayout.setRefreshing(false);
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == REQUEST_EDIT_GROUP && resultCode == Activity.RESULT_OK) {
			listGroup.clear();
			ListGroupsAdapter.listFriend = null;
			GroupDB.getInstance(getContext()).dropDB();
			getListGroup();
		}
	}

	@Override public void onRefresh() {

	}
	public class FragGroupClickFloatButton implements View.OnClickListener{

		Context context;
		public FragGroupClickFloatButton getInstance(Context context){
			this.context = context;
			return this;
		}

		@Override
		public void onClick(View view) {
			startActivity(new Intent(getContext(), AddGroupActivity.class));
		}
	}*/
}
/*class ListGroupsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private ArrayList<Group> listGroup;
	public static ListFriend listFriend = null;
	private Context context;

	public ListGroupsAdapter(Context context,ArrayList<Group> listGroup){
		this.context = context;
		this.listGroup = listGroup;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(context).inflate(R.layout.rc_item_group, parent, false);
		return new ItemGroupViewHolder(view);
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
		final String groupName = listGroup.get(position).groupInfo.get("name");
		if(groupName != null && groupName.length() > 0) {
			((ItemGroupViewHolder) holder).txtGroupName.setText(groupName);
			((ItemGroupViewHolder) holder).iconGroup.setText((groupName.charAt(0) + "").toUpperCase());
		}
		((ItemGroupViewHolder) holder).btnMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				view.setTag(new Object[]{groupName, position});
				view.getParent().showContextMenuForChild(view);
			}
		});
		((RelativeLayout)((ItemGroupViewHolder) holder).txtGroupName.getParent()).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(listFriend == null){
					listFriend = FriendDB.getInstance(context).getListFriend();
				}
				Intent intent = new Intent(context, ChatActivity.class);
				intent.putExtra(StaticConfig.INTENT_KEY_CHAT_FRIEND, groupName);
				ArrayList<CharSequence> idFriend = new ArrayList<>();
				ChatActivity.bitmapAvataFriend = new HashMap<>();
				for(String id : listGroup.get(position).member) {
					idFriend.add(id);
					String avata = listFriend.getAvataById(id);
					if(!avata.equals(StaticConfig.STR_DEFAULT_BASE64)) {
						byte[] decodedString = Base64.decode(avata, Base64.DEFAULT);
						ChatActivity.bitmapAvataFriend.put(id, BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length));
					}else if(avata.equals(StaticConfig.STR_DEFAULT_BASE64)) {
						ChatActivity.bitmapAvataFriend.put(id, BitmapFactory.decodeResource(context.getResources(), R.drawable.default_avata));
					}else {
						ChatActivity.bitmapAvataFriend.put(id, null);
					}
				}
				intent.putCharSequenceArrayListExtra(StaticConfig.INTENT_KEY_CHAT_ID, idFriend);
				intent.putExtra(StaticConfig.INTENT_KEY_CHAT_ROOM_ID, listGroup.get(position).id);
				context.startActivity(intent);
			}
		});
	}

	@Override
	public int getItemCount() {
		return listGroup.size();
	}
}

class ItemGroupViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
	public TextView iconGroup, txtGroupName;
	public ImageButton btnMore;
	public ItemGroupViewHolder(View itemView) {
		super(itemView);
		itemView.setOnCreateContextMenuListener(this);
		iconGroup = (TextView) itemView.findViewById(R.id.icon_group);
		txtGroupName = (TextView) itemView.findViewById(R.id.txtName);
		btnMore = (ImageButton) itemView.findViewById(R.id.btnMoreAction);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
		menu.setHeaderTitle((String) ((Object[])btnMore.getTag())[0]);
		Intent data = new Intent();
		data.putExtra(GroupFragment.CONTEXT_MENU_KEY_INTENT_DATA_POS, (Integer) ((Object[])btnMore.getTag())[1]);
		menu.add(Menu.NONE, GroupFragment.CONTEXT_MENU_EDIT, Menu.NONE, "Edit group").setIntent(data);
		menu.add(Menu.NONE, GroupFragment.CONTEXT_MENU_DELETE, Menu.NONE, "Delete group").setIntent(data);
		menu.add(Menu.NONE, GroupFragment.CONTEXT_MENU_LEAVE, Menu.NONE, "Leave group").setIntent(data);
	}
}
*/