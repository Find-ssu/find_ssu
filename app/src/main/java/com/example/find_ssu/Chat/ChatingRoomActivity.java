package com.example.find_ssu.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.find_ssu.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.EventListener;

import java.util.ArrayList;


import io.reactivex.rxjava3.annotations.Nullable;

public class ChatingRoomActivity extends AppCompatActivity {
    private static final String TAG = "ChatingRoom";
    FirebaseFirestore db;
    public ArrayList<ChatItem> list = new ArrayList<>();
    private ChatAdapter adapter = new ChatAdapter(this,list);
    RecyclerView chating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chating_room);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        String superdocumentId = intent.getStringExtra("superdocumentId");
        String name = intent.getStringExtra("name");
        String where = intent.getStringExtra("where");

        String sender = FirebaseAuth.getInstance().getCurrentUser().getUid(); //현재사용자(보내는이)

        //받는 사람
        String RemoveSender = superdocumentId.replace(sender, "");
        String Removewhere = RemoveSender.replace(where, "");
        String receiver = Removewhere.replace("-","");

        //물품명
        TextView setname = findViewById(R.id.chat_name_tv);
        setname.setText(name);

        //어느 게시판에서 보냈는지
        TextView setwhere = findViewById(R.id.chat_where_tv);
        setwhere.setText(where);

        ImageView backBtn = findViewById(R.id.chating_room_back_btn);
        ImageView chatBtn = findViewById((R.id.chating_room_chat_btn));

        //백버튼 클릭시 종료
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //어댑터 리사이클러뷰 연결
        chating = findViewById(R.id.chating_room_rv);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        chating.setLayoutManager(layoutManager);
        adapter = new ChatAdapter(ChatingRoomActivity.this, list);
        chating.setAdapter(adapter);

        //쪽지 다시 보내기
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatingRoomActivity.this, ChatActivity.class);
                intent.putExtra("uid", receiver);
                intent.putExtra("name", name);
                intent.putExtra("where", where);
                startActivity(intent);
            }
        });

        //문서 불러오기
        getAllDocumentsInACollection(superdocumentId);

    }

    private void getAllDocumentsInACollection(String superdocumentId) {
        db.collection("Chat")
                .document(superdocumentId)
                .collection("subchat")
                .orderBy("timestamp", Query.Direction.DESCENDING)
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
                            String message = document.getString("message");
                            String timestamp = document.getString("timestamp");
                            String uid1 = document.getString("uid1");
                            String uid2 = document.getString("uid2");
                            String documentId = document.getString("documentId");
                            String superdocumentId = document.getString("superdocumentId");
                            String name = document.getString("name");
                            String where = document.getString("where");

                            ChatItem chatItem = new ChatItem(uid1, uid2, message, documentId, timestamp, superdocumentId, name, where);
                            adapter.addItem(chatItem);
                        }

                        adapter.notifyDataSetChanged();
                    }
                });
    }


}