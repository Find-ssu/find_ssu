package com.example.find_ssu.Main;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.find_ssu.R;
import com.example.find_ssu.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "FINDSSU";

    ActivityLoginBinding binding;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private ActivityResultLauncher<IntentSenderRequest> oneTapUILauncher;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    static String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        configOneTapSignUpOrSignInClient();
        initFirebaseAuth();
        EditText emailEditText = binding.loginEmailEt;
        EditText passwordEditText = binding.loginPasswordEt;
        Button signInButton = binding.loginBtn;
        db = FirebaseFirestore.getInstance();
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if(!email.isEmpty()&&!password.isEmpty()){
                    signIn(email, password);
                }
                else{
                    Toast.makeText(getApplicationContext(), "이메일과 비밀번호를 모두 입력하세요",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView MembershipButton=binding.loginMembershipTv;
        MembershipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,MembershipActivity.class);
                startActivity(intent);

            }
        });
        ImageButton google_login_Button=binding.loginGoogleBtn;
        google_login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googlesignIn();
            }
        });



    }
    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            Log.d(TAG, "이미 로그인 했음");
            updateUI(currentUser); // Go to MainActivity
        } else {
            Log.d(TAG, "아직 로그인 안했음");
        }
    }
    private void configOneTapSignUpOrSignInClient() {
        oneTapClient = Identity.getSignInClient(this);

        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        //.setFilterByAuthorizedAccounts(true) // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(false) // Show all accounts on the device.
                        .build())
                .build();

        oneTapUILauncher = registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                try {
                    SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(result.getData());
                    String idToken = credential.getGoogleIdToken();
                    if (idToken != null) {
                        // Got an ID token from Google. Use it to authenticate
                        // with Firebase.
                        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                        mAuth.signInWithCredential(firebaseCredential)
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            // Sign in success, update UI with the signed-in user's information
                                            Log.d(TAG, "signInWithCredential:success");
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            updateUI(user);
                                        } else {
                                            // If sign in fails, display a message to the user.
                                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                                            updateUI(null);
                                        }
                                    }
                                });
                    }
                } catch (ApiException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void initFirebaseAuth() {
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Log.d(TAG, "signInWithEmail:failure");
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }
    private void googlesignIn(){
    // check whether the user has any saved credentials for your app. -> onSuccess or onFailure
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(this, new OnSuccessListener<BeginSignInResult>() {
        @Override
        public void onSuccess(BeginSignInResult beginSignInResult) {
            IntentSender intentSender = beginSignInResult.getPendingIntent().getIntentSender();
            IntentSenderRequest intentSenderRequest = new IntentSenderRequest.Builder(intentSender).build();
            oneTapUILauncher.launch(intentSenderRequest);
        }
    })
            .addOnFailureListener(this, new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            // No saved credentials found. Launch the One Tap sign-up flow, or
            // do nothing and continue presenting the signed-out UI.
            Log.d(TAG, e.getLocalizedMessage());
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