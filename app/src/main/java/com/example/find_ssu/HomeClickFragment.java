package com.example.find_ssu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.find_ssu.databinding.FragmentHomeBinding;
import com.example.find_ssu.databinding.FragmentHomeClickBinding;

public class HomeClickFragment extends Fragment {
    FragmentHomeClickBinding binding;
    private String number;
    private String name;
    private String date;
    private String location;
    private String img;
    private String timestamp;

    public static HomeClickFragment newInstance(HomePost homePost) {
        HomeClickFragment fragment = new HomeClickFragment();

        // 인스턴스 변수에 데이터 할당
        fragment.name = homePost.gethomeName().toString();
        fragment.location = homePost.gethomeLocation().toString();
        fragment.number = homePost.getNumber().toString();
        fragment.date = homePost.gethomeDate().toString();
        fragment.timestamp = homePost.getTimestamp().toString();
        if(homePost.getImg()==null)
            fragment.img=null;
        else
            fragment.img = homePost.getImg().toString();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeClickBinding.inflate(inflater,container,false);
        View rootview = binding.getRoot();
        ImageView back = binding.homeClickBackIv;

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {requireActivity().onBackPressed();}
        });
        binding.homeClickFeatureTv.setText(name);
        binding.homeClickSerialNumTv.setText(number);
        binding.homeClickLocationTv.setText(location);
        binding.homeClickDateTv.setText(date);
        if(img!=null){
            Glide.with(binding.getRoot().getContext()).load(img).into(binding.homeClickIv);}
        return rootview;

    }
}