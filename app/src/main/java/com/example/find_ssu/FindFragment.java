package com.example.find_ssu;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.find_ssu.databinding.FragmentFindBinding;
import com.example.find_ssu.databinding.FragmentFindFabClickBinding;
import com.example.find_ssu.databinding.ItemviewBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class FindFragment extends Fragment {
    private static final String TAG = "FINDSSU";

    FragmentFindBinding findBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        findBinding = FragmentFindBinding.inflate(inflater,container,false);
        View rootview = findBinding.getRoot();

        List<String> list = new ArrayList<>();
        for(int i=0; i<20; i++){
            list.add("Item=" + i);
        }

        findBinding.findRv.setLayoutManager(new LinearLayoutManager(requireContext()));
        findBinding.findRv.setAdapter(new MyAdapter(list));

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
                // 버튼을 클릭하면 다른 프래그먼트로 전환
                MapsFragment MapsFragment = new MapsFragment();

                // Fragment 전환
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_find, MapsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                findBinding.findFab.setVisibility(View.GONE);
            }
        });
        return findBinding.getRoot();
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

    private class MyViewHolder extends RecyclerView.ViewHolder{
        private ItemviewBinding binding;

        private MyViewHolder(ItemviewBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }

        private void bind(String text){
            binding.itemNameTv.setText(text);
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
        private List<String> list;

        private MyAdapter(List<String> list) {this.list = list;}

        @Override
        @NonNull
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
            ItemviewBinding binding = ItemviewBinding.inflate(
                    LayoutInflater.from(parent.getContext()),
                    parent,
                    false
            );
            return new MyViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            String text = list.get(position);
            holder.bind(text);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }



}