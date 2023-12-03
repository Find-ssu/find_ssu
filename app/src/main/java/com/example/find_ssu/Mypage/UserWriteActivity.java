package com.example.find_ssu.Mypage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.paging.PagingConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.find_ssu.Find.FindPost;
import com.example.find_ssu.Find.FindPostViewHolder;
import com.example.find_ssu.LookFor.LookForPost;
import com.example.find_ssu.Main.MainActivity;
import com.example.find_ssu.databinding.ActivityUserWriteBinding;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserWriteActivity<T> extends AppCompatActivity {
    ActivityUserWriteBinding binding;
    private FirestorePagingAdapter<FindPost, FindPostViewHolder> adapter;
    private FirebaseFirestore db;
    private String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUserWriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // 액티비티 레이아웃 설정

        initializeCloudFirestore();
        db.collection("FindPost").whereEqualTo("uid",uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<T> dataList = new ArrayList<>();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        FindPost data = document.toObject(FindPost.class);
                        dataList.add((T) data);
                    }
                    Collections.reverse(dataList);
                    // 데이터를 받아온 후에 리스트로 관리하고 리사이클러뷰에 표시할 수 있는 작업을 수행합니다.
                    displayDataInRecyclerView(dataList);
                }

            }
        });

        ImageButton backButton = binding.userWriteClickBackIv;

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 현재 엑티비티 종료

                // 메인 엑티비티를 시작하는 Intent 생성
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // 메인 엑티비티가 이미 스택에 있으면 해당 엑티비티 위에 새로운 인스턴스를 생성하지 않도록 함
                v.getContext().startActivity(intent); // 메인 엑티비티 시작
            }
        });

        TextView find=binding.userWriteFind;
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializeCloudFirestore();
                db.collection("FindPost").whereEqualTo("uid",uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<T> dataList = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                FindPost data = document.toObject(FindPost.class);
                                dataList.add((T) data);
                            }
                            Collections.reverse(dataList);
                            // 데이터를 받아온 후에 리스트로 관리하고 리사이클러뷰에 표시할 수 있는 작업을 수행합니다.
                            displayDataInRecyclerView(dataList);
                        }

                    }
                });

            }
        });
        TextView lookfor=binding.userWriteLookfor;
        lookfor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializeCloudFirestore();
                db.collection("LookForPost").whereEqualTo("uid",uid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<T> dataList = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                LookForPost data = document.toObject(LookForPost.class);
                                dataList.add((T)data);
                            }
                            Collections.reverse(dataList);
                            // 데이터를 받아온 후에 리스트로 관리하고 리사이클러뷰에 표시할 수 있는 작업을 수행합니다.
                            displayDataInRecyclerView(dataList);
                        }

                    }
                });
            }
        });


    }
    private void displayDataInRecyclerView(List<T> dataList) {
        RecyclerView recyclerView = binding.userwriteRv;
        UserwriteAdapter adapter = new UserwriteAdapter(dataList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    private void initializeCloudFirestore() {
        db = FirebaseFirestore.getInstance();
    }
}