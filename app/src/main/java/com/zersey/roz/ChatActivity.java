package com.zersey.roz;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

	EditText editWriteMessage;
	private long userId;
	private String fullname;
	private GroupChat mGroupChat;
	private DatabaseReference myRef;
	public static final int VIEW_TYPE_USER_MESSAGE = 0;
	public static final int VIEW_TYPE_FRIEND_MESSAGE = 1;
	private ListMessageAdapter adapter;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		editWriteMessage = findViewById(R.id.editWriteMessage);
		mGroupChat = new GroupChat();

		SharedPreferences prefs = getSharedPreferences("login", MODE_PRIVATE);
		userId = Long.parseLong(prefs.getString("userid", null));
		fullname = prefs.getString("fullname", null);

		mGroupChat.setGroupId(103);
		mGroupChat.setMessageList(new ArrayList<Message>());

		final RecyclerView recyclerChat = findViewById(R.id.recyclerChat);
		recyclerChat.setLayoutManager(new LinearLayoutManager(this));
		adapter = new ListMessageAdapter(mGroupChat, userId);

		myRef = FirebaseDatabase.getInstance().getReference("group_chats");
		myRef.child("103-planet-roz").addChildEventListener(new ChildEventListener() {
			@Override
			public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
				if (dataSnapshot.getValue() != null) {
					HashMap mapMessage = (HashMap) dataSnapshot.getValue();
					Message newMessage = new Message();
					newMessage.setIdSender((long) mapMessage.get("idSender"));
					newMessage.setText((String) mapMessage.get("text"));
					newMessage.setTimestamp((long) mapMessage.get("timestamp"));
					newMessage.setSenderName((String) mapMessage.get("senderName"));
					mGroupChat.getMessageList().add(newMessage);
					adapter.notifyDataSetChanged();
					recyclerChat.getLayoutManager()
						.scrollToPosition(mGroupChat.getMessageList().size() - 1);
				}
			}

			@Override
			public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

			}

			@Override public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

			}

			@Override
			public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

			}

			@Override public void onCancelled(@NonNull DatabaseError databaseError) {

			}
		});
		recyclerChat.setAdapter(adapter);
	}

	public void buttonSend(View view) {
		String content = editWriteMessage.getText().toString().trim();
		if (content.length() > 0) {
			editWriteMessage.setText("");
			Message newMessage = new Message();
			newMessage.setText(content);
			newMessage.setIdSender(userId);
			newMessage.setSenderName(fullname);
			newMessage.setTimestamp(System.currentTimeMillis());
			myRef.child("103-planet-roz").push().setValue(newMessage);
		}
	}
}
