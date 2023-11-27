package com.example.find_ssu;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
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
import com.example.find_ssu.databinding.FragmentFindFabClickBinding;
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

        findBinding.findRv.setLayoutManager(new LinearLayoutManager(requireContext()));
       // findBinding.findRv.setAdapter(new MyAdapter(list));

        findBinding.findFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 새로운 Fragment 객체 생성
                Fragment findFabClickFragment = new FindFabClickFragment();

                // Fragment 전환
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_find, findFabClickFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                findBinding.findFab.setVisibility(View.GONE);
            }
        });

        findBinding.findMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(requireActivity(), MapsActivity.class);
                startActivity(intent);
            }
        });
//        initializeCloudFirestore();
//        getAllDocumentsInACollection();
        Query baseQuery = FirebaseFirestore.getInstance()
                .collection("FindPost")
                .orderBy("timestamp", Query.Direction.DESCENDING);

        PagingConfig config = new PagingConfig(/* page size */ 4, /* prefetchDistance */ 2,
                /* enablePlaceHolders */ false);

        FirestorePagingOptions<FindPost> options = new FirestorePagingOptions.Builder<FindPost>()
                .setLifecycleOwner(this) // an activity or a fragment
                .setQuery(baseQuery, config, FindPost.class)
                .build();
        adapter=new FirestorePagingAdapter<FindPost, FindPostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FindPostViewHolder holder, int position, @NonNull FindPost model) {
                holder.bind((model));
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


        return findBinding.getRoot();

    }
    private void getAllDocumentsInACollection() {
        db.collection("FindPost")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    private void initializeCloudFirestore() {
        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();
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

//    private class MyViewHolder extends RecyclerView.ViewHolder{
//        private ItemviewBinding binding;
//
//        private MyViewHolder(ItemviewBinding binding){
//            super(binding.getRoot());
//            this.binding = binding;
//        }
//
//        private void bind(String text){
//            binding.itemNameTv.setText(text);
//        }
//    }
//
//    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
//        private List<String> list;
//
//        private MyAdapter(List<String> list) {this.list = list;}
//
//        @Override
//        @NonNull
//        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
//            ItemviewBinding binding = ItemviewBinding.inflate(
//                    LayoutInflater.from(parent.getContext()),
//                    parent,
//                    false
//            );
//            return new MyViewHolder(binding);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//            String text = list.get(position);
//            holder.bind(text);
//        }
//
//        @Override
//        public int getItemCount() {
//            return list.size();
//        }
//    }



}