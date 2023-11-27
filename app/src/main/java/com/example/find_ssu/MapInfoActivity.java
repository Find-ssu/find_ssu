package com.example.find_ssu;

import static java.lang.Math.round;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowMetrics;
import android.widget.LinearLayout;

public class MapInfoActivity extends AppCompatActivity {

    //MapInfoAdapter adapter;

    RecyclerView FindPost_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_info);
/*
        FindPost_list = findViewById(R.id.map_info);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        FindPost_list.setLayoutManager(layoutManager);

        adapter = new MapInfoAdapter(getApplicationContext());
        FindPost_list.setAdapter(adapter);

        adapter.addItem(new MapInfoAdapter("ㅁㅁ"));

 */
    }

    @Override
    protected void onResume() {
        super.onResume();

        initLayout();
    }

    private void initLayout() {
        int width = 323;
        int height = 373;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) { // API Level 30 버전
            WindowMetrics windowMetrics = getWindowManager().getCurrentWindowMetrics();
            width = windowMetrics.getBounds().width();
            height = windowMetrics.getBounds().height();
        } else { // API Level 30 이전 버전
            Display display = getWindowManager().getDefaultDisplay();
            DisplayMetrics displayMetrics = new DisplayMetrics();
            display.getRealMetrics(displayMetrics);
            width = displayMetrics.widthPixels;
            height = displayMetrics.heightPixels;
        }
        getWindow().setLayout(width, height);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }
}