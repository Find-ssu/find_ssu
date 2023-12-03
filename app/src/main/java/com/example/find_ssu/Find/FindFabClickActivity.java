package com.example.find_ssu.Find;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.find_ssu.Main.MainActivity;
import com.example.find_ssu.R;
import com.example.find_ssu.databinding.ActivityFindFabClickBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import io.reactivex.rxjava3.annotations.NonNull;

public class FindFabClickActivity extends AppCompatActivity {
    private static final String TAG = "FINDSSU";
    ActivityFindFabClickBinding binding;
    private TextView selectedDateTextView;
    private Spinner locationSpinner;
    private static final int PICK_IMAGE_REQUEST = 1;
    private FirebaseFirestore db;
    Uri image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding=ActivityFindFabClickBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        selectedDateTextView = binding.findFabClickDateEt;

        //Cloud Firestore 인스턴스 초기화
        initializeCloudFirestore();

        //날짜 버튼 클릭리스너
        Button dateButton = binding.findFabClickDateBtn;
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        //백버튼 클릭리스너
        ImageButton findFabClickBackButton = binding.findFabClickBackIv;
        findFabClickBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(FindFabClickActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog, null);
                Button cancelButton = dialogView.findViewById(R.id.dialog_cancel_btn);
                Button okayButton = dialogView.findViewById(R.id.dialog_okay_btn);

                builder.setView(dialogView);
                AlertDialog dialog = builder.create();

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                okayButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        onBackPressed();
//                        Intent intent = new Intent(FindFabClickActivity.this, MainActivity.class);
//                        startActivity(intent);
                    }
                });

                dialog.show();
            }
        });

        //습득장소 스피너
        locationSpinner = binding.findFabClickLocationSp;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.location_spinner_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);

        //업로드 버튼 클릭리스너
        AppCompatButton findFabClickUploadButton = binding.findFabClickUploadBtn;
        findFabClickUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.findFabClickNameEt.getText().toString().isEmpty())
                    Toast.makeText(FindFabClickActivity.this, "물품명을 입력해주세요", Toast.LENGTH_SHORT).show();
                else if (binding.findFabClickLocationDetailEt.getText().toString().isEmpty())
                    Toast.makeText(FindFabClickActivity.this, "상세 습득장소를 입력해주세요", Toast.LENGTH_SHORT).show();
                else if (binding.findFabClickDateEt.getText().toString().isEmpty())
                    Toast.makeText(FindFabClickActivity.this, "습득일자를 입력해주세요", Toast.LENGTH_SHORT).show();
                else if (binding.findFabClickMoreEt.getText().toString().isEmpty())
                    Toast.makeText(FindFabClickActivity.this, "세부사항을 입력해주세요", Toast.LENGTH_SHORT).show();
                else
                    addDataFromCustomObject();
            }
        });

        //이미지 업로드 버튼 클릭리스너
        ImageButton findFabClickImageButton = binding.findFabClickImgBtn;
        findFabClickImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
    }

    //Cloud Firestore 인스턴스 초기화
    private void initializeCloudFirestore() {
        db = FirebaseFirestore.getInstance();
    }
    //피이어스토어에 사용자 입력값 객체로 업로드
    public void addDataFromCustomObject() {
        String name = binding.findFabClickNameEt.getText().toString();
        String location = binding.findFabClickLocationSp.getSelectedItem().toString();
        String location_detail = binding.findFabClickLocationDetailEt.getText().toString();
        String date = binding.findFabClickDateEt.getText().toString();
        String more = binding.findFabClickMoreEt.getText().toString();
        String Image;
        if (image != null) {
            Image = image.toString();
        } else {
            Image = null;
        }

        String uid = getUidOfCurrentUser();
        String DocumentId = uid + "_" + System.currentTimeMillis();
        FindPost findPost = new FindPost(name, location, location_detail, date, more, Image,DocumentId);
        db.collection("FindPost").document(DocumentId).set(findPost)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Intent intent = new Intent(FindFabClickActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        Log.d(TAG, "업로드 후 프레그먼트 전환");
                        Toast.makeText(FindFabClickActivity.this, "게시물 업로드 완료", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(FindFabClickActivity.this, "게시물 업로드 실패", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            uploadImageAndGetData(selectedImageUri);
        }
    }
//스토리지 이미지 업로드&image Uri반환 함수
    public void uploadImageAndGetData(Uri imageUri) {
        String bucketName = "gs://findssu-f23d6.appspot.com";
        String imagePath = getPath("jpg");
        uploadImageToStorage(imageUri, bucketName, imagePath, new OnImageUploadListener() {
            @Override
            public void onImageUploadSuccess(String imageUrl) {
                image = imageUri;
            }

            @Override
            public void onImageUploadFailure(Exception e) {
                Toast.makeText(FindFabClickActivity.this, "이미지 업로드 실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
//스토리지 이미지 업로드
    public void uploadImageToStorage(Uri imageUri, String bucketName, String imagePath, OnImageUploadListener listener) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child(imagePath);
        UploadTask uploadTask = imageRef.putFile(imageUri);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                listener.onImageUploadSuccess(imageUrl);
            }).addOnFailureListener(e -> {
                listener.onImageUploadFailure(e);
            });
        }).addOnFailureListener(e -> {
            listener.onImageUploadFailure(e);
        });
    }
    private interface OnImageUploadListener {
        void onImageUploadSuccess(String imageUrl);

        void onImageUploadFailure(Exception e);
    }

    //경로 지정
    private String getPath(String extension) {
        String uid = getUidOfCurrentUser();
        String dir = uid;
        String fileName = uid + "_" + System.currentTimeMillis() + "." + extension;
        return "Find/" + dir + "/" + fileName;
    }
    //사용자 Uid
    private String getUidOfCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
//달력띄우기
    private void showDatePickerDialog() {
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setOnDateSelectedListener(new DatePickerFragment.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int year, int month, int day) {
                String selectedDate = year + "년 " + (month + 1) + "월 " + day + "일";
                selectedDateTextView.setText(selectedDate);
                selectedDateTextView.setVisibility(View.VISIBLE);
            }
        });
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }
}

