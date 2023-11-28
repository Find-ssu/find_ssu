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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.find_ssu.databinding.FragmentFindBinding;
import com.example.find_ssu.databinding.FragmentLookForBinding;
import com.example.find_ssu.databinding.ItemviewBinding;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class LookForFragment extends Fragment {
    private FirestorePagingAdapter<LookForPost,LookForPostViewHolder> adapter;
    private static final String TAG = "FINDSSU";
    private FirebaseFirestore db;
    FragmentLookForBinding lookForBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        lookForBinding=FragmentLookForBinding.inflate(inflater,container,false);
        View rootview = lookForBinding.getRoot();
        //글쓰기버튼 클릭이벤트
        lookForBinding.lookForFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 액티비티 전환
                Intent intent = new Intent(requireContext(), LookForFabClickActivity.class);
                startActivity(intent);
            }
        });

        return rootview;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //LookForPost콜렉션 시간순으로 갖고옴
        Query baseQuery = FirebaseFirestore.getInstance()
                .collection("LookForPost")
                .orderBy("timestamp", Query.Direction.DESCENDING);
        //페이징
        PagingConfig config = new PagingConfig( 4, 2,false);

        FirestorePagingOptions<LookForPost> options = new FirestorePagingOptions.Builder<LookForPost>()
                .setLifecycleOwner(getViewLifecycleOwner())
                .setQuery(baseQuery, config, LookForPost.class)
                .build();

        //FirestorePagingAdapter
        adapter=new FirestorePagingAdapter<LookForPost, LookForPostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull LookForPostViewHolder holder, int position, @NonNull LookForPost model) {
                holder.bind(model);
                holder.position=position;
                holder.model=model;
            }
            @NonNull
            @Override
            public LookForPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                ItemviewBinding binding=ItemviewBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
                return new LookForPostViewHolder(binding);
            }
        };
        //리사이클러뷰에 어뎁터 연결
        RecyclerView recyclerView = lookForBinding.lookForRv;
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
                    lookForBinding.lookForFab.setVisibility(View.VISIBLE);
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