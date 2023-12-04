package com.example.find_ssu.Chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.find_ssu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ChatListActivity extends AppCompatActivity {
    private static final String TAG = "ChatList";
    FirebaseFirestore db;
    public ArrayList<ChatItem> list = new ArrayList<>();
    private ChatListAdapter adapter = new ChatListAdapter(this,list);
    RecyclerView chatlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        String user = FirebaseAuth.getInstance().getCurrentUser().getUid(); //현재 사용자 아이디

        db = FirebaseFirestore.getInstance();

        ImageView backBtn = findViewById(R.id.chatlist_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        chatlist = findViewById(R.id.chatlist_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        chatlist.setLayoutManager(layoutManager);
        adapter = new ChatListAdapter(ChatListActivity.this, list);
        chatlist.setAdapter(adapter);


        adapter.setOnItemClickListener(new ChatListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ChatItem chatItem) {
                Intent intent = new Intent(ChatListActivity.this, ChatingRoomActivity.class);
                intent.putExtra("superdocumentId", chatItem.getSuperdocumentId());
                intent.putExtra("uid1", chatItem.getUid1());
                intent.putExtra("name",chatItem.getName());
                intent.putExtra("where",chatItem.getWhere());
                intent.putExtra("documentId", chatItem.getDocumentId());
                startActivity(intent);
            }
        });

        getAllDocumentsInACollection(user);

    }

    private void getAllDocumentsInACollection(String user) {
        db.collection("Chat")
                .orderBy("time", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }

                        list.clear();
                        for (QueryDocumentSnapshot document : value) {
                            String documentName = document.getId();

                            if (containsWord(documentName, user)) {
                                String message = document.getString("message");
                                String timestamp = document.getString("timestamp");
                                String uid1 = document.getString("uid1");
                                String uid2 = document.getString("uid2");
                                String documentId = document.getString("documentId");
                                String superdocumentId = document.getString("superdocumentId");
                                String name = document.getString("name");
                                String where = document.getString("where");
                                String subdocumentId = document.getString("subdocumentId");

                                ChatItem chatItem = new ChatItem(uid1, uid2, message, documentId, timestamp, superdocumentId, name, where, subdocumentId);
                                adapter.addItem(chatItem);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
    private static boolean containsWord(String input, String targetWord) {
        return input.contains(targetWord);
    }
}