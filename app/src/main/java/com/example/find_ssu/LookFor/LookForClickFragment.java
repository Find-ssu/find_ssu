package com.example.find_ssu.LookFor;

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
import com.example.find_ssu.databinding.FragmentLookForClickBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class LookForClickFragment extends Fragment {
    FragmentLookForClickBinding binding;

    static String name;
    static String location;
    static String date;
    static String more;
    static String image;
    private String uid;
    private String documentuid;

    public static LookForClickFragment newInstance(LookForPost lookForPost) {
        LookForClickFragment fragment = new LookForClickFragment();

        // 인스턴스 변수에 데이터 할당
        fragment.name = lookForPost.getName().toString();
        fragment.location = lookForPost.getLocation().toString();
        fragment.date = lookForPost.getDate().toString();
        fragment.more = lookForPost.getMore().toString();
        fragment.uid = lookForPost.getUid().toString();
        fragment.documentuid = lookForPost.getDocumentuid().toString();
        if(lookForPost.getImage()==null)
            fragment.image=null;
        else
            fragment.image = lookForPost.getImage().toString();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentLookForClickBinding.inflate(inflater,container,false);
        View rootview =binding.getRoot();
        ImageView back = binding.lookForClickBackIv;
        ImageView chatBtn = binding.lookForChatBtn;
        hideBottomNavigation(true);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), ChatActivity.class);
                intent.putExtra("uid", uid);
                intent.putExtra("name", name);
                intent.putExtra("where", "찾아요");
                intent.putExtra("documentId",documentuid);
                startActivity(intent);
            }
        });

        binding.lookForClickNameInputTv.setText(name);
        binding.lookForClickLocationInputTv.setText(location);
        binding.lookForClickDateInputTv.setText(date);
        binding.lookForClickMoreInputTv.setText(more);
        if(image!=null){
            Glide.with(binding.getRoot().getContext()).load(image).into(binding.lookForClickIv);}
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