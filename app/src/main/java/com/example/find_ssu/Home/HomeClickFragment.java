package com.example.find_ssu.Home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.find_ssu.R;
import com.example.find_ssu.databinding.FragmentHomeClickBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        hideBottomNavigation(true);
        TextView instagram = binding.homeClickInstagramLinkTv;

        Linkify.TransformFilter linktest = new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher match, String url) {
                return "";
            }
        };
        Pattern pattern = Pattern.compile("인스타 DM 연락 링크");
        Linkify.addLinks(instagram, pattern, "https://www.instagram.com/direct/t/17846604498103682", null, linktest);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {requireActivity().onBackPressed();}
        });
        binding.homeClickFeatureInputTv.setText(name);
        binding.homeClickSerialNumTv.setText(number);
        binding.homeClickLocationTv.setText(location);
        binding.homeClickDateTv.setText(date);
        if(img!=null){
            Glide.with(binding.getRoot().getContext()).load(img).into(binding.homeClickIv);}
        return rootview;

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideBottomNavigation(false);
    }

    public void hideBottomNavigation(Boolean bool) {
        BottomNavigationView bottomNavigation = getActivity().findViewById(R.id.navigationView);
        if (bool == true)
            bottomNavigation.setVisibility(View.GONE);
        else
            bottomNavigation.setVisibility(View.VISIBLE);
    }
}