package com.example.find_ssu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.find_ssu.databinding.FragmentFindClickBinding;

public class FindClickActivity extends AppCompatActivity {
    static String name;
    static String location;
    static String location_detail;
    static String date;
    static String more;
    static String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentFindClickBinding binding = FragmentFindClickBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.findClickBackIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("name");
            location = extras.getString("location");
            location_detail = extras.getString("location_detail");
            date = extras.getString("date");
            more = extras.getString("more");
            image = extras.getString("image");
        }

        binding.findClickNameInputTv.setText(name);
        binding.findClickLocationInputTv.setText(location);
        binding.findClickLocationDetailInputTv.setText(location_detail);
        binding.findClickDateInputTv.setText(date);
        binding.findClickMoreInputTv.setText(more);
        if (image != null) {
            Glide.with(binding.getRoot().getContext()).load(image).into(binding.findClickIv);
        }
    }
}