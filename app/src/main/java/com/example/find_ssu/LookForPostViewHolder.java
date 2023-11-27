package com.example.find_ssu;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.find_ssu.databinding.ItemviewBinding;

public class LookForPostViewHolder extends RecyclerView.ViewHolder {
    ItemviewBinding binding;


    LookForPostViewHolder(@NonNull ItemviewBinding binding) {
        super(binding.getRoot());
        this.binding=binding;
        binding.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //프레그먼트 전환


            }
        });
    }

    void bind(@NonNull LookForPost lookforPost) {
        binding.itemNameTv.setText(lookforPost.getName());
        binding.itemDateInputTv.setText(lookforPost.getDate());
        // 예시 URL 문자열
        String imageUrl = lookforPost.getImage();
        if(imageUrl!=null){
            Glide.with(binding.getRoot().getContext()).load(imageUrl).into(binding.itemIv);}
    }

}
