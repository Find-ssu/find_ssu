package com.example.find_ssu.Find;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.find_ssu.Chat.ChatActivity;
import com.example.find_ssu.R;
import com.example.find_ssu.databinding.FragmentFindClickBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class FindClickFragment extends Fragment {
    FragmentFindClickBinding binding;

     static String name;
     static String location;
     static String location_detail;
     static String date;
     static String more;
     static String image;
     static String uid;
     static String documentuid;

    public static FindClickFragment newInstance(FindPost findPost) {
        FindClickFragment fragment = new FindClickFragment();

        // 인스턴스 변수에 데이터 할당
        fragment.name = findPost.getName().toString();
        fragment.location = findPost.getLocation().toString();
        fragment.location_detail = findPost.getLocation_detail().toString();
        fragment.date = findPost.getDate().toString();
        fragment.more = findPost.getMore().toString();
        fragment.uid = findPost.getUid().toString();
        fragment.documentuid = findPost.getDocumentuid().toString();
        if(findPost.getImage()==null)
            fragment.image=null;
        else
            fragment.image = findPost.getImage().toString();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentFindClickBinding.inflate(inflater,container,false);
        View rootview =binding.getRoot();
        ImageView back = binding.findClickBackIv;
        hideBottomNavigation(true);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        binding.findChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), ChatActivity.class);
                intent.putExtra("uid", uid);
                intent.putExtra("name", name);
                intent.putExtra("where", "찾아가세요");
                intent.putExtra("documentId",documentuid);
                startActivity(intent);
            }
        });

        binding.findClickNameInputTv.setText(name);
        binding.findClickLocationInputTv.setText(location);
        binding.findClickLocationDetailInputTv.setText(location_detail);
        binding.findClickDateInputTv.setText(date);
        binding.findClickMoreInputTv.setText(more);
        if(image!=null){
            Glide.with(binding.getRoot().getContext()).load(image).into(binding.findClickIv);}
        return rootview;

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideBottomNavigation(false);
    }

    public void hideBottomNavigation(Boolean bool) {
        BottomNavigationView bottomNavigation = getActivity().findViewById(R.id.navigationView);
        if(bottomNavigation!=null) {
            if (bool == true)
                bottomNavigation.setVisibility(View.GONE);
            else
                bottomNavigation.setVisibility(View.VISIBLE);
        }
    }
}