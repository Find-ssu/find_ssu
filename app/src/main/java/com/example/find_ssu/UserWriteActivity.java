package com.example.find_ssu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.paging.PagingConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.find_ssu.databinding.ActivityUserWriteBinding;
import com.example.find_ssu.databinding.ItemviewBinding;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserWriteActivity<T> extends AppCompatActivity {
    ActivityUserWriteBinding binding;
    private FirestorePagingAdapter<FindPost,FindPostViewHolder> adapter;
    private FirebaseFirestore db;
    private String uid=FirebaseAuth.getInstance().getCurrentUser().getUid();


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUserWriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot()); // 액티비티 레이아웃 설정

        ImageButton backButton = binding.userWriteClickBackIv;

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        TextView find=binding.userwriteFind;
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializeCloudFirestore();
                db.collection("FindPost").whereEqualTo("uid",uid).orderBy("timestamp", Query.Direction.DESCENDING).get().
                addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<T> dataList = new ArrayList<>();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                FindPost data = document.toObject(FindPost.class);
                                dataList.add((T) data);
                            }
                            // 데이터를 받아온 후에 리스트로 관리하고 리사이클러뷰에 표시할 수 있는 작업을 수행합니다.
                            displayDataInRecyclerView(dataList);
                        }

                    }
                });

            }
        });
        TextView lookfor=binding.userwriteLookfor;
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