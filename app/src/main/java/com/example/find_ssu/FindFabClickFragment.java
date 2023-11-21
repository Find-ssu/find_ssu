package com.example.find_ssu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.find_ssu.databinding.FragmentFindFabClickBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

public class FindFabClickFragment extends Fragment {
    private static final String TAG = "FINDSSU";
    FragmentFindFabClickBinding binding;
    private FirebaseFirestore db;
    private Spinner locationSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Cloud Firestore 인스턴스 초기화
        initializeCloudFirestore();
        binding=FragmentFindFabClickBinding.inflate(inflater,container,false);
        View rootview = binding.getRoot();

        ImageButton findFabClickBackButton = binding.findFabClickBackIv;
        findFabClickBackButton.setOnClickListener(new View.OnClickListener() {
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
                        Fragment findFragment = new FindFragment();

                        // Fragment 전환
                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_find, findFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });

                dialog.show();
            }

        });

        //업로드 버튼
        AppCompatButton findFabClickUploadButton = binding.findFabClickUploadBtn;
        //업로드 버튼 클릭이벤트 처리
        findFabClickUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //findpost객체 생성 및 파이어베이스 업로드 후 프레그먼트 전환
                addDataFromCustomObject();
            }
        });

        // 스피너와 array 연결
        locationSpinner = binding.findFabClickLocationSp;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.find_fab_click_location_spinner_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);

        return rootview;

    }

    private void initializeCloudFirestore() {
        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();
    }

    private void addDataFromCustomObject() {
        //사용자 입력값
        String name = binding.findFabClickNameEt.getText().toString();
        String location = binding.findFabClickLocationSp.getSelectedItem().toString();
        String location_detail = binding.findFabClickLocationDetailEt.getText().toString();
        String date = binding.findFabClickDateEt.getText().toString();
        String more = binding.findFabClickMoreEt.getText().toString();
        //String image=binding.findFabClickImgBtn
        //사용자 입력 값으로 FindPost객체 생성
        FindPost findPost = new FindPost(name, location, location_detail, date, more, "이미지 URL");
        //firestore업로드
        db.collection("FindPost").document(name).set(findPost)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    // Firestore에 데이터 저장 성공

                    @Override
                    public void onSuccess(Void aVoid) {
                        // 새로운 Fragment 객체 생성
                        Fragment findFragment = new FindFragment();
                        // Fragment 전환
                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_find, findFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        //업로드 완료 토스트
                        Log.d(TAG, "업로드 후 프레그먼트 전환");
                        Toast.makeText(requireContext(),"게시물 업로드 완료",Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    // Firestore에 데이터 저장 실패
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //업로드 실패 토스트
                        Toast.makeText(requireContext(), "게시물 업로드 실패", Toast.LENGTH_SHORT).show();
                    }
                });
    }





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}