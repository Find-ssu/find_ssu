package com.example.find_ssu.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.find_ssu.databinding.ActivityMembershipBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MembershipActivity extends AppCompatActivity {
    private static final String TAG = "FINDSSU";
    ActivityMembershipBinding binding;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMembershipBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initFirebaseAuth();

        EditText emailEditText = binding.membershipEmailEt;
        EditText passwordEditText = binding.membershipPasswordEt;
        EditText passwordcheckEditText = binding.membershipPasswordCheckEt;
        Button signUpButton = binding.membershipBtn;

        //회원가입버튼 클릭이벤트
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String passwordcheck=passwordcheckEditText.getText().toString();
                //1차로 email,password,passwordcheck가 비었는지 확인
                if(!email.isEmpty()&&!password.isEmpty()&&!passwordcheck.isEmpty()){
                    //2차로 password와 passwordcheck의 일치여부 확인
                    if(password.equals(passwordcheck)){
                        signUp(email, password);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "비밀번호가 일치하지않습니다.", Toast.LENGTH_LONG).show();
                    }
                }

                else{
                    Toast.makeText(getApplicationContext(), "이메일과 비밀번호를 모두 입력하세요",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    // Firebase Auth 초기화
    private void initFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
    }
    //파이어베이스 회원가입
    private void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //회원가입 성공
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            //회원가입 실패
                            Log.d(TAG, "createUserWithEmail:failure");
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("USER_PROFILE", "email: " + user.getEmail() + "\n" + "uid: " + user.getUid());
            startActivity(intent);
            MembershipActivity.this.finish();
        }
    }
}