package com.example.find_ssu;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.find_ssu.databinding.ItemviewBinding;

public class FindPostViewHolder extends RecyclerView.ViewHolder {
    ItemviewBinding binding;
    FindPost model;
    int position;

    FindPostViewHolder(@NonNull ItemviewBinding binding){
        super(binding.getRoot());
        this.binding = binding;

        binding.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //프레그먼트 전환
                // 전환할 프래그먼트 인스턴스 생성
                FragmentManager fragmentManager = ((AppCompatActivity)view.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.home_navi, FindClickFragment.newInstance(model));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
        );};


    void bind(@NonNull FindPost findPost) {
        binding.itemNameTv.setText(findPost.getName());
        binding.itemDateInputTv.setText(findPost.getDate());
        // 예시 URL 문자열
        String imageUrl = findPost.getImage();
        if(imageUrl!=null){
        Glide.with(binding.getRoot().getContext()).load(imageUrl).into(binding.itemIv);}
    }

}



