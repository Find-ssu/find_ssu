package com.example.find_ssu.Mypage;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.find_ssu.Chat.ChatListActivity;
import com.example.find_ssu.Main.LoginActivity;
import com.example.find_ssu.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MyPageFragment extends Fragment {

    private static final String TAG = "FINDSSU";
    //사용자 이메일 정보 파이어베이스에서 get
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String email = user.getEmail();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);


        TextView logoutButton = view.findViewById(R.id.my_page_logout_tv);
        TextView writeListButton = view.findViewById(R.id.my_page_write_list_tv);
        TextView chatBtn = view.findViewById((R.id.my_page_chat_tv));
        TextView instagram = view.findViewById(R.id.my_page_instagram);

        Linkify.TransformFilter linktest = new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher match, String url) {
                return "";
            }
        };
        Pattern pattern = Pattern.compile("@sainpae_find");
        Linkify.addLinks(instagram, pattern, "https://www.instagram.com/sainpae_find/", null, linktest);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //파이어베이스 로그아웃 후 액티비티 종료
                Intent intent = new Intent(requireContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                signOut();
            }
        });



        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireContext(), ChatListActivity.class);
                startActivity(intent);
            }
        });

        writeListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 화면 전환을 위한 액티비티 객체 생성
                Intent intent = new Intent(getActivity(), UserWriteActivity.class);
                startActivity(intent);
            }
        });


        //사용자 이메일정보 띄우기
        TextView EmailView=view.findViewById(R.id.my_page_email_tv);
        EmailView.setText(email);
        return view;
    }

    //파이어베이스 로그아웃 함수
    private void signOut() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();

        // Check if there is no current user.
        if (firebaseAuth.getCurrentUser() == null)
            Log.d(TAG, "signOut:success");
        else
            Log.d(TAG, "signOut:failure");
    }

}