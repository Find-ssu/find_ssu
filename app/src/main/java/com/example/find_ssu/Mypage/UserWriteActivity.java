package com.example.find_ssu.Mypage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.find_ssu.Find.FindPost;
import com.example.find_ssu.LookFor.LookForPost;
import com.example.find_ssu.R;
import com.example.find_ssu.databinding.ActivityUserWriteBinding;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserWriteActivity<T> extends AppCompatActivity {
    private static final String TAG = "FINDSSU";
    ActivityUserWriteBinding binding;
    private FirebaseFirestore db;
    private String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUserWriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initializeCloudFirestore();
        ImageButton backButton = binding.userWriteClickBackIv;
        TextView find=binding.userWriteFind;
        TextView lookfor=binding.userWriteLookfor;

        db.collection("FindPost").whereEqualTo("uid",uid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException error) {
                        List<T> UserwriteList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : value) {
                            FindPost Item = document.toObject(FindPost.class);
                            UserwriteList.add((T) Item);
                        }
                        Collections.reverse(UserwriteList);
                        // 데이터를 받아온 후에 리스트로 관리하고 리사이클러뷰에 표시할 수 있는 작업을 수행합니다.
                        displayDataInRecyclerView(UserwriteList);
                    }
                });
        //백버튼 클릭리스너
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //찾아가세요 리스트
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                find.setTextColor(ContextCompat.getColor(view.getContext(), R.color.blue));
                lookfor.setTextColor(ContextCompat.getColor(view.getContext(), R.color.gray));
                initializeCloudFirestore();
                db.collection("FindPost").whereEqualTo("uid",uid).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException error) {
                        List<T> UserwriteList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : value) {
                            FindPost Item = document.toObject(FindPost.class);
                            UserwriteList.add((T) Item);
                        }
                        Collections.reverse(UserwriteList);
                        // 데이터를 받아온 후에 리스트로 관리하고 리사이클러뷰에 표시할 수 있는 작업을 수행합니다.
                        displayDataInRecyclerView(UserwriteList);
                    }
                });
            }
        });
        //찾아요 리스트
        lookfor.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                find.setTextColor(ContextCompat.getColor(view.getContext(), R.color.gray));
                lookfor.setTextColor(ContextCompat.getColor(view.getContext(), R.color.blue));
                initializeCloudFirestore();
                db.collection("LookForPost").whereEqualTo("uid",uid).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException error) {
                        List<T> UserwriteList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : value) {
                            LookForPost Item = document.toObject(LookForPost.class);
                            UserwriteList.add((T) Item);
                        }
                        Collections.reverse(UserwriteList);
                        // 데이터를 받아온 후에 리스트로 관리하고 리사이클러뷰에 표시할 수 있는 작업을 수행합니다.
                        displayDataInRecyclerView(UserwriteList);
                    }
                });
            }
        });
    }
    //리사이클러뷰 어뎁터 연결
    private void displayDataInRecyclerView(List<T> UserwriteList) {
        RecyclerView recyclerView = binding.userwriteRv;
        UserwriteAdapter adapter = new UserwriteAdapter(UserwriteList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    private void initializeCloudFirestore() {
        db = FirebaseFirestore.getInstance();
    }
}