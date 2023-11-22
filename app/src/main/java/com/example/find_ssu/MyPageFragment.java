package com.example.find_ssu;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class MyPageFragment extends Fragment {


    private static final String TAG = "FINDSSU";

    private TextView my_page_logout_tv;
    private TextView my_page_email_tv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my_page, container, false);

        TextView logoutButton = view.findViewById(R.id.my_page_logout_tv);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //파이어베이스 로그아웃
                signOut();
                getActivity().finish();

                // 로그아웃 버튼을 클릭하면 LoginActivity로 이동
                //Intent intent = new Intent(requireContext(), LoginActivity.class);
                //startActivity(intent);
                //requireActivity().getSupportFragmentManager().beginTransaction().remove(MyPageFragment.this).commit();
            }
        });
        TextView EmailView=view.findViewById(R.id.my_page_email_tv);

        return view;
    }

    private void signOut() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signOut();

        // Check if there is no current user.
        if (firebaseAuth.getCurrentUser() == null)
            Log.d(TAG, "signOut:success");
        else
            Log.d(TAG, "signOut:failure");
    }
}