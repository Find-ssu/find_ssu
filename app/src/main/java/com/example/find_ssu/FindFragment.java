package com.example.find_ssu;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.paging.PagingConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.example.find_ssu.databinding.FragmentFindBinding;
import com.example.find_ssu.databinding.ItemviewBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class FindFragment extends Fragment {
    private FirestorePagingAdapter<FindPost,FindPostViewHolder> adapter;
    private static final String TAG = "FINDSSU";
    private FirebaseFirestore db;
    FragmentFindBinding findBinding;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        findBinding = FragmentFindBinding.inflate(inflater,container,false);
        View rootview = findBinding.getRoot();
        //글쓰기버튼 클릭이벤트
        findBinding.findFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 액티비티 전환
                Intent intent = new Intent(requireContext(), FindFabClickActivity.class);
                startActivity(intent);
            }
        });

        findBinding.findSearchIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = findBinding.findSearchWordEt.getText().toString().trim(); // EditText에서 검색어 가져오기

                // 검색어에 따라 Firestore 쿼리 업데이트
                Query query;
                if (!searchText.isEmpty()) {
                    // 검색어가 비어 있지 않으면, 검색어를 이용하여 Firestore 쿼리 수정
                    query = FirebaseFirestore.getInstance()
                            .collection("FindPost")
                            .orderBy("timestamp", Query.Direction.DESCENDING)
                            .whereEqualTo("name", searchText);

                } else {
                    // 검색어가 비어 있으면 기본 쿼리 사용
                    query = FirebaseFirestore.getInstance()
                            .collection("FindPost")
                            .orderBy("timestamp", Query.Direction.DESCENDING);
                }

                PagingConfig config = new PagingConfig(4, 2, false);
                FirestorePagingOptions<FindPost> updatedOptions = new FirestorePagingOptions.Builder<FindPost>()
                        .setLifecycleOwner(getViewLifecycleOwner())
                        .setQuery(query, config, FindPost.class)
                        .build();

                // FirestorePagingAdapter에 새로운 FirestorePagingOptions 설정
                adapter.updateOptions(updatedOptions);
                adapter.notifyDataSetChanged();
                Log.d(TAG, "옵션업데이트");
            }
        });

        //지도버튼 클릭이벤트
        findBinding.findMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), MapsActivity.class);
                startActivity(intent);
            }
        });
        return rootview;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //FindPost콜렉션 시간순으로 갖고옴
        Query baseQuery = FirebaseFirestore.getInstance()
                .collection("FindPost")
                .orderBy("timestamp", Query.Direction.DESCENDING);
        //페이징
        PagingConfig config = new PagingConfig( 4, 2,false);

        FirestorePagingOptions<FindPost> options = new FirestorePagingOptions.Builder<FindPost>()
                .setLifecycleOwner(getViewLifecycleOwner())
                .setQuery(baseQuery, config, FindPost.class)
                .build();

        //FirestorePagingAdapter
        adapter = new FirestorePagingAdapter<FindPost, FindPostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FindPostViewHolder holder, int position, @NonNull FindPost model) {
                holder.bind(model);
                holder.position=position;
                holder.model=model;
            }
            @NonNull
            @Override
            public FindPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                ItemviewBinding binding=ItemviewBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
                return new FindPostViewHolder(binding);
            }
        };
        //리사이클러뷰에 어뎁터 연결
        RecyclerView recyclerView = findBinding.findRv;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack(); // 이전 프래그먼트로 돌아가기
                    findBinding.findFab.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }




}