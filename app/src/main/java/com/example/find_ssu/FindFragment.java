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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        findBinding.findFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 액티비티 전환
                Intent intent = new Intent(requireContext(), FindFabClickActivity.class);
                startActivity(intent);
                findBinding.findFab.setVisibility(View.GONE);
            }
        });


        findBinding.findMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                findBinding.findFab.setVisibility(View.GONE);
                Intent intent = new Intent(requireActivity(), MapsActivity.class);
                startActivity(intent);
            }
        });
//        initializeCloudFirestore();
//        getAllDocumentsInACollection();
        return rootview;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Query baseQuery = FirebaseFirestore.getInstance()
                .collection("FindPost")
                .orderBy("timestamp", Query.Direction.DESCENDING);

        PagingConfig config = new PagingConfig(/* page size */ 4, /* prefetchDistance */ 2,
                /* enablePlaceHolders */ false);

        FirestorePagingOptions<FindPost> options = new FirestorePagingOptions.Builder<FindPost>()
                .setLifecycleOwner(getViewLifecycleOwner())
                .setQuery(baseQuery, config, FindPost.class)
                .build();
        adapter=new FirestorePagingAdapter<FindPost, FindPostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FindPostViewHolder holder, int position, @NonNull FindPost model) {
                holder.bind(model);
            }

            @NonNull
            @Override
            public FindPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                ItemviewBinding binding=ItemviewBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
                return new FindPostViewHolder(binding);
            }
        };
        RecyclerView recyclerView = findBinding.findRv;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

    }
    //    private void getAllDocumentsInACollection() {
//        db.collection("FindPost")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                Log.d(TAG, document.getId() + " => " + document.getData());
//                            }
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
//    }
//    private void initializeCloudFirestore() {
//        // Access a Cloud Firestore instance from your Activity
//        db = FirebaseFirestore.getInstance();
//    }

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