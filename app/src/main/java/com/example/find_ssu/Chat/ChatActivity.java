package com.example.find_ssu.Chat;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.find_ssu.R;
import com.example.find_ssu.databinding.ActivityChatBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;
import com.google.firebase.messaging.FirebaseMessaging;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        Intent intent = getIntent();

        String where = intent.getStringExtra("where");
        String name = intent.getStringExtra("name");
        String documentId = intent.getStringExtra("documentId");

        String uid1 = intent.getStringExtra("uid");//유저 아이디 1 -> 글쓴이
        String uid2 = FirebaseAuth.getInstance().getCurrentUser().getUid(); //유저 아이디 2 -> 보내는이

        //글쓴이와 보내는이 아이디값 비교해서 문서(채팅방) 만들기 -> 절대값을 만들기 위해
        //유저 아아디 두개 더하고 게시글의 문서 id를 합침
        String chatroom = uid1.compareTo(uid2) < 0 ? uid1 + "-" + uid2 + documentId : uid2 + "-" + uid1 + documentId;

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
                            if(uid1.equals(uid2)) {
                                Toast.makeText(ChatActivity.this, "자신에게는 쪽지를 보낼 수 없습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                addDataFromCustomObject(uid1, uid2, chatroom, name, where, documentId);
                                db.collection("user")
                                        .whereEqualTo("status", "offline")
                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot value,
                                                                @Nullable FirebaseFirestoreException error) {
                                                for (QueryDocumentSnapshot document : value){
                                                    Log.d("am", "아무거나");
                                                    if(document.equals(uid1)){
                                                        String token = document.getString("token");
                                                        Log.d("token",token);
                                                        sendPushNotificationToUser(token);
                                                    }
                                                }
                                            }
                                        });
                                finish();
                            }
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

    public void addDataFromCustomObject(String uid1, String uid2, String chatroom, String name, String where, String documentId) {
        String message = binding.chatMessage.getText().toString();
        String supperdocumentId = chatroom; //글쓴이 + 쪽지 작성자 아이디
        String subdocumentId = FirebaseAuth.getInstance().getCurrentUser().getUid() + "_" + System.currentTimeMillis();

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        String timestamp = formatter.format(date);

        Log.d("name", name);
        Log.d("where", where);
        ChatItem chatItem = new ChatItem(uid1, uid2, message, documentId, timestamp, supperdocumentId, name, where, subdocumentId);
        Log.d("name", chatItem.getName());

        db.collection("Chat").document(supperdocumentId).set(chatItem);

        db.collection("Chat")
                .document(supperdocumentId)
                .collection("subchat")
                .document(subdocumentId)
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
                        Log.e("ChatActivity", "Error writing document", e);
                    }
                });
    }
    private void sendPushNotificationToUser(String userToken) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // FCM POST 요청 URL
                    URL url = new URL("https://fcm.googleapis.com/fcm/send");

                    // HttpURLConnection 객체 생성
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    // POST 요청 설정
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Authorization", "key=" + "BOH5Zwr1vm2-Obc1A53mpheVeCbcymsYyekp9V2KGV5wK4F14iDfvkO3vZNgqVZ5QVZKL35C9gboav76QebZLec"); // 여기에 FCM 서버 키를 넣어주세요.
                    conn.setRequestProperty("Content-Type", "application/json");

                    // JSON 메시지 작성
                    JSONObject message = new JSONObject();
                    message.put("to", userToken);
                    message.put("priority", "high");

                    JSONObject notification = new JSONObject();
                    notification.put("title", "New message");
                    notification.put("body", "You have a new message.");

                    message.put("notification", notification);

                    conn.setDoOutput(true);

                    // 메시지 전송
                    DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                    wr.writeBytes(message.toString());
                    wr.flush();
                    wr.close();

                    int responseCode = conn.getResponseCode();
                    Log.d("Response Code", "ResponseCode: " + responseCode);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }


}
