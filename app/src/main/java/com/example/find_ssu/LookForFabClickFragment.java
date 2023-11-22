package com.example.find_ssu;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;


public class LookForFabClickFragment extends Fragment {

    private Spinner locationSpinner;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_look_for_fab_click, container, false);
        ImageButton lookForFabClickBackButton = view.findViewById(R.id.look_for_fab_click_back_iv);

        lookForFabClickBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // AlertDialog.Builder를 사용하여 다이얼로그 생성
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                LayoutInflater inflater = requireActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog, null); // dialog.xml을 inflate하여 다이얼로그에 설정
                Button cancelButton = dialogView.findViewById(R.id.dialog_cancel_btn);
                Button okayButton = dialogView.findViewById(R.id.dialog_okay_btn);

                builder.setView(dialogView);
                AlertDialog dialog = builder.create();

                // cancelButton의 클릭 이벤트 처리
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 다이얼로그 닫기
                        dialog.dismiss();
                    }
                });

                // okayButton의 클릭 이벤트 처리
                okayButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 다이얼로그 닫기
                        dialog.dismiss();

                        // 화면 전환을 위한 Fragment 객체 생성
                        Fragment lookForFragment = new LookForFragment();

                        // Fragment 전환
                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_look_for, lookForFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });

                dialog.show();
            }

        });

        // 스피너와 array 연결
        locationSpinner = view.findViewById(R.id.look_for_fab_click_location_sp);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.location_spinner_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);

        return view;

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}