package com.example.find_ssu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class UserWriteActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_write); // 액티비티 레이아웃 설정

        ImageButton backButton = findViewById(R.id.user_write_click_back_iv);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

//                // 화면 전환을 위한 액티비티 객체 생성
//                Intent intent = new Intent(UserWriteActivity.this, MyPageFragment.class);
//                startActivity(intent);
//                finish(); // 이전 액티비티를 스택에서 제거하여 뒤로가기 버튼을 눌렀을 때 이전 화면이 나오지 않도록 함
            }
        });
    }
}