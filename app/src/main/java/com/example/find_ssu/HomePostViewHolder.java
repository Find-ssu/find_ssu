package com.example.find_ssu;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.find_ssu.databinding.ItemviewBinding;

public class HomePostViewHolder extends RecyclerView.ViewHolder {
    ItemviewBinding binding;
    HomePost model;
    int position;

    HomePostViewHolder(@NonNull ItemviewBinding binding){
        super(binding.getRoot());
        this.binding = binding;

        binding.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //프레그먼트 전환
                // 전환할 프래그먼트 인스턴스 생성
                FragmentManager fragmentManager = ((AppCompatActivity)view.getContext()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.home_navi, HomeClickFragment.newInstance(model));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
        );};


    void bind(@NonNull HomePost homePost) {
        this.model = homePost;
        binding.itemNameTv.setText(homePost.gethomeName());
        binding.itemDataTv.setText(homePost.gethomeDate());
        // 예시 URL 문자열
        String imageUrl = homePost.getImg();
        if(imageUrl!=null){
            Glide.with(binding.getRoot().getContext()).load(imageUrl).into(binding.itemIv);}
    }
}
