package com.example.find_ssu.Find;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.paging.PagingConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.find_ssu.Map.MapsActivity;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.example.find_ssu.databinding.FragmentFindBinding;
import com.example.find_ssu.databinding.ItemviewBinding;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


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

        // EditText에서 텍스트가 변경될 때 호출되는 메서드
        findBinding.findSearchWordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            Query originalQuery = FirebaseFirestore.getInstance()
                    .collection("FindPost")
                    .orderBy("timestamp", Query.Direction.DESCENDING);
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = charSequence.toString().trim();

                if (searchText.isEmpty()) {
                    PagingConfig config = new PagingConfig(4, 2, false);
                    FirestorePagingOptions<FindPost> newOptions = new FirestorePagingOptions.Builder<FindPost>()
                            .setLifecycleOwner(getViewLifecycleOwner())
                            .setQuery(originalQuery, config, FindPost.class)
                            .build();

                    adapter.updateOptions(newOptions); // 어댑터에 새로운 옵션을 업데이트

                } else {
                    Query filteredQuery = FirebaseFirestore.getInstance()
                            .collection("FindPost")
                            .orderBy("timestamp", Query.Direction.DESCENDING)
                            .whereEqualTo("name", searchText); // 여기서 "name"은 검색하려는 필드명입니다.

                    PagingConfig config = new PagingConfig(4, 2, false);
                    FirestorePagingOptions<FindPost> newOptions = new FirestorePagingOptions.Builder<FindPost>()
                            .setLifecycleOwner(getViewLifecycleOwner())
                            .setQuery(filteredQuery, config, FindPost.class)
                            .build();

                    adapter.updateOptions(newOptions); // 어댑터에 새로운 옵션을 업데이트
                }
            }


            @Override
            public void afterTextChanged(Editable editable) {

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
        Log.d(TAG, baseQuery.toString());

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
                Log.d(TAG, "onCreateViewHolder()");

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
        Log.d(TAG, "onStart");

        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }




}