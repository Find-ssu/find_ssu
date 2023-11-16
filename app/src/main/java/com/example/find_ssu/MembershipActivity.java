package com.example.find_ssu;

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
    private static final String TAG = "EmailPassword";
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
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String passwordcheck=passwordcheckEditText.getText().toString();
                if(!email.isEmpty()&&!password.isEmpty()&&!passwordcheck.isEmpty()){
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
    private void initFirebaseAuth() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }
    private void signUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "createUserWithEmail:failure", task.getException());
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
        }
    }


}