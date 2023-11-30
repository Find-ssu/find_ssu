package com.example.find_ssu;

import static androidx.fragment.app.FragmentManager.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.paging.PagingConfig;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.find_ssu.databinding.FragmentFindBinding;
import com.example.find_ssu.databinding.FragmentHomeBinding;
import com.example.find_ssu.databinding.ItemviewBinding;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private FirestorePagingAdapter<HomePost, HomePostViewHolder> adapter;
    FragmentHomeBinding binding;
    private static final String TAG = "FINDSSU";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View rootView = binding.getRoot();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // FindPost 콜렉션 시간순으로 가져옴
        Query baseQuery = FirebaseFirestore.getInstance()
                .collection("InstagramData")
                .orderBy("timestamp", Query.Direction.DESCENDING);

        // 페이징
        PagingConfig config = new PagingConfig(4, 2, false);

        FirestorePagingOptions<HomePost> options = new FirestorePagingOptions.Builder<HomePost>()
                .setLifecycleOwner(getViewLifecycleOwner())
                .setQuery(baseQuery, config,HomePost.class)
                .build();

        // FirestorePagingAdapter
        adapter = new FirestorePagingAdapter<HomePost, HomePostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull HomePostViewHolder holder, int position, @NonNull HomePost model) {
                holder.bind(model);
                holder.position = position;
                holder.model = model;
            }

            @NonNull
            @Override
            public HomePostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                ItemviewBinding binding = ItemviewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
                return new HomePostViewHolder(binding);
            }
        };

        // 리사이클러뷰에 어댑터 연결
        RecyclerView recyclerView = binding.homeRv;
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