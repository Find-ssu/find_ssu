package com.example.find_ssu.LookFor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.find_ssu.DatePickerFragment;
import com.example.find_ssu.Find.FindFabClickActivity;
import com.example.find_ssu.Main.MainActivity;
import com.example.find_ssu.R;
import com.example.find_ssu.databinding.ActivityLookForFabClickBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import io.reactivex.rxjava3.annotations.NonNull;

public class LookForFabClickActivity extends AppCompatActivity {
    private static final String TAG = "FINDSSU";
    ActivityLookForFabClickBinding binding;
    private TextView selectedDateTextView;
    private static final int PICK_IMAGE_REQUEST = 1;
    private FirebaseFirestore db;
    String imageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding= ActivityLookForFabClickBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        selectedDateTextView = binding.lookForFabClickDateEt;

        //Cloud Firestore 인스턴스 초기화
        initializeCloudFirestore();

        //날짜 버튼 클릭리스너
        Button dateButton = binding.lookForFabClickDateBtn;
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        //백버튼 클릭리스너
        ImageButton lookForFabClickBackButton = binding.lookForFabClickBackIv;
        lookForFabClickBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LookForFabClickActivity.this);
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
                        finish();
                    }
                });

                dialog.show();
            }
        });
        //업로드 버튼 클릭리스너
        AppCompatButton lookForFabClickUploadButton = binding.lookForFabClickUploadBtn;
        lookForFabClickUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.lookForFabClickNameEt.getText().toString().isEmpty())
                    Toast.makeText(LookForFabClickActivity.this, "물품명을 입력해주세요", Toast.LENGTH_SHORT).show();
                else if (binding.lookForFabClickLocationDetailEt.getText().toString().isEmpty())
                    Toast.makeText(LookForFabClickActivity.this, "상세 습득장소를 입력해주세요", Toast.LENGTH_SHORT).show();
                else if (binding.lookForFabClickDateEt.getText().toString().isEmpty())
                    Toast.makeText(LookForFabClickActivity.this, "습득일자를 입력해주세요", Toast.LENGTH_SHORT).show();
                else if (binding.lookForFabClickMoreEt.getText().toString().isEmpty())
                    Toast.makeText(LookForFabClickActivity.this, "세부사항을 입력해주세요", Toast.LENGTH_SHORT).show();
                else
                    addDataFromCustomObject();
            }
        });
        //이미지 업로드 버튼 클릭리스너
        ImageButton lookForFabClickImageButton = binding.lookForFabClickImgBtn;
        lookForFabClickImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
                Log.d(TAG,"이미지업로드 클릭리스너");
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LookForFabClickActivity.this);
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
                LookForFabClickActivity.super.onBackPressed();
            }
        });

        dialog.show();
    }

    //Cloud Firestore 인스턴스 초기화
    private void initializeCloudFirestore() {
        db = FirebaseFirestore.getInstance();
    }
    //피이어스토어에 사용자 입력값 객체로 업로드
    public void addDataFromCustomObject() {
        String name = binding.lookForFabClickNameEt.getText().toString();
        String location_detail = binding.lookForFabClickLocationDetailEt.getText().toString();
        String date = binding.lookForFabClickDateEt.getText().toString();
        String more = binding.lookForFabClickMoreEt.getText().toString();
        String uid = getUidOfCurrentUser();
        String DocumentId = uid + "_" + System.currentTimeMillis();
        LookForPost lookForPost = new LookForPost(name,location_detail, date, more,imageUrl,DocumentId);
        db.collection("LookForPost").document(DocumentId).set(lookForPost)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        finish();
                        Log.d(TAG, "업로드 후 프레그먼트 전환");
                        Toast.makeText(LookForFabClickActivity.this, "게시물 업로드 완료", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@io.reactivex.rxjava3.annotations.NonNull Exception e) {
                        Toast.makeText(LookForFabClickActivity.this, "게시물 업로드 실패", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG,"Activityresult");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            uploadImageAndGetData(selectedImageUri);
        } else if (data == null && data.getData() == null)
            Toast.makeText(LookForFabClickActivity.this, "선택된 이미지 없음", Toast.LENGTH_SHORT).show();
    }

    //스토리지 이미지 업로드&image Uri반환 함수
    public void uploadImageAndGetData(Uri imageUri) {
        Log.d(TAG,"스토리지 업로드");
        String bucketName = "gs://findssu-f23d6.appspot.com";
        String imagePath = getPath("jpg");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child(imagePath);
        UploadTask uploadTask = imageRef.putFile(imageUri);
        uploadTask.addOnCompleteListener(this, new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@io.reactivex.rxjava3.annotations.NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    // 업로드가 성공한 경우, 이미지의 다운로드 URL을 가져옴
                    imageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                if (downloadUri != null) {
                                    imageUrl = downloadUri.toString();
                                }
                            }
                        }
                    });
                } else {
                    // 업로드가 실패한 경우 에러 처리
                    Exception exception = task.getException();
                    if (exception != null) {
                        Toast.makeText(LookForFabClickActivity.this, "이미지 업로드 실패", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }


    //경로 지정
    private String getPath(String extension) {
        String uid = getUidOfCurrentUser();
        String dir = uid;
        String fileName = uid + "_" + System.currentTimeMillis() + "." + extension;
        return "LookFor/" + dir + "/" + fileName;
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
