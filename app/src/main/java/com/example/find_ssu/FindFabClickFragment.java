package com.example.find_ssu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.UUID;

public class FindFabClickFragment extends Fragment {
    private static final String TAG = "FINDSSU";
    FragmentFindFabClickBinding binding;
    private Spinner locationSpinner;

    private static final int PICK_IMAGE_REQUEST = 1;
    private FirebaseFirestore db;
    Uri image;

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

        // 스피너와 array 연결
        locationSpinner = binding.findFabClickLocationSp;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.find_fab_click_location_spinner_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);

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

        //이미지 추가 버튼
        ImageButton findFabClickImageButton=binding.findFabClickImgBtn;
        //이미지 추가 버튼 클릭이벤트 처리
        findFabClickImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //갤러리 앱 호출
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        return rootview;

    }

    private void initializeCloudFirestore() {
        db = FirebaseFirestore.getInstance();
    }

    public void addDataFromCustomObject() {
        //사용자 입력값
        String name = binding.findFabClickNameEt.getText().toString();
        String location = binding.findFabClickLocationSp.getSelectedItem().toString();
        String location_detail = binding.findFabClickLocationDetailEt.getText().toString();
        String date = binding.findFabClickDateEt.getText().toString();
        String more = binding.findFabClickMoreEt.getText().toString();
        String Image=image.toString();
        String uid = getUidOfCurrentUser();
        String DocumentId =uid + "_" + System.currentTimeMillis();
        //사용자 입력 값으로 FindPost객체 생성
        FindPost findPost = new FindPost(name, location, location_detail, date, more,Image);
        //firestore업로드
        db.collection("FindPost").document(DocumentId).set(findPost)
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            // 이미지 선택 결과 처리
            Uri selectedImageUri = data.getData();
            // 이미지 업로드 및 URL 가져오기
            uploadImageAndGetData(selectedImageUri);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//
    public void uploadImageAndGetData(Uri imageUri) {
        // 이미지를 업로드할 버킷 선택 (Firebase Console에서 확인 가능)
        String bucketName = "gs://findssu-f23d6.appspot.com";

        // 이미지를 업로드할 경로 설정 (원하는 경로로 변경)
        String imagePath = getPath("jpg");

        // Storage에 이미지 업로드
        uploadImageToStorage(imageUri, bucketName, imagePath, new OnImageUploadListener() {
            @Override
            public void onImageUploadSuccess(String imageUrl) {
                image=imageUri;
            }

            @Override
            public void onImageUploadFailure(Exception e) {
                // 이미지 업로드 실패 처리
                Toast.makeText(requireContext(), "이미지 업로드 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // firebase Storage에 이미지 업로드
    public void uploadImageToStorage(Uri imageUri, String bucketName, String imagePath, OnImageUploadListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child(imagePath);

        UploadTask uploadTask = imageRef.putFile(imageUri);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // 이미지 업로드 성공 후 다운로드 URL 가져오기
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                listener.onImageUploadSuccess(imageUrl);
            }).addOnFailureListener(e -> {
                // 다운로드 URL 가져오기 실패
                listener.onImageUploadFailure(e);
            });
        }).addOnFailureListener(e -> {
            // 이미지 업로드 실패
            listener.onImageUploadFailure(e);
        });
    }
//이미지 경로 이름 규칙(사용자 uid+업로드 시간)
    private String getPath(String extension) {
        String uid = getUidOfCurrentUser();

        String dir = uid ;

        String fileName =uid + "_" + System.currentTimeMillis() + "." + extension;
        return dir + "/" + fileName;
    }
    //사용자 Uid반환 함수
    private String getUidOfCurrentUser() {
        return  FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

private interface OnImageUploadListener {
    void onImageUploadSuccess(String imageUrl);

    void onImageUploadFailure(Exception e);
}
}
