package com.example.find_ssu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.find_ssu.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;
    FirebaseFirestore db;
    @ServerTimestamp
    private Timestamp time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();

        String uid1 = intent.getStringExtra("uid");//유저 아이디 1 -> 글쓴이
        String uid2 = FirebaseAuth.getInstance().getCurrentUser().getUid(); //유저 아이디 2 -> 보내는이

        //글쓴이와 보내는이 아이디값 비교해서 문서(채팅방) 만들기 -> 절대값을 만들기 위해
        String chatroom = uid1.compareTo(uid2) < 0 ? uid1 + "-" + uid2 : uid2 + "-" + uid1;

        //Cloud Firestore 인스턴스 초기화
        initializeCloudFirestore();

        ImageView backBtn = findViewById(R.id.chat_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView sendBtn = findViewById(R.id.chat_send_btn);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Chat")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            addDataFromCustomObject(uid1, uid2, chatroom);
                        } else {
                            Log.w("chat", "Error getting documents.", task.getException());
                        }
                    }
                });
            }
        });

    }

    private void initializeCloudFirestore() {
        db = FirebaseFirestore.getInstance();
    }

    public void addDataFromCustomObject(String uid1, String uid2, String chatroom) {
        String message = binding.chatMessage.getText().toString();
        String supperdocumentId = chatroom; //글쓴이 + 쪽지 작성자 아이디
        String subdocument = FirebaseAuth.getInstance().getCurrentUser().getUid() + "_" + System.currentTimeMillis();

        String documentId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String timestamp = formatter.format(date);

        ChatItem chatItem = new ChatItem(uid1, uid2, message, documentId, timestamp, supperdocumentId);

        db.collection("Chat").document(supperdocumentId).set(chatItem);

        db.collection("Chat")
                .document(supperdocumentId)
                .collection("subchat")
                .document(subdocument)
                .set(chatItem)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@io.reactivex.rxjava3.annotations.NonNull Exception e) {
                        Toast.makeText(ChatActivity.this, "쪽지 전송 실패", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
