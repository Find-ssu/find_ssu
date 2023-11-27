package com.example.find_ssu;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class FindFragment extends Fragment {
    private static final String TAG = "FINDSSU";
    private FirebaseFirestore db;

    private FloatingActionButton findFab;
    private ImageButton Mapsbtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find, container, false);

        findFab = view.findViewById(R.id.find_fab);
        Mapsbtn = view.findViewById(R.id.find_map_btn);


        findFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 새로운 Fragment 객체 생성
                Fragment findFabClickFragment = new FindFabClickFragment();

                // Fragment 전환
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_find, findFabClickFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                findFab.setVisibility(View.GONE);
            }
        });

        Mapsbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(requireActivity(), MapsActivity.class);
                startActivity(intent);
            }
        });
        initializeCloudFirestore();
        getAllDocumentsInACollection();

        return view;
    }
    private void getAllDocumentsInACollection() {
        db.collection("FindPost")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }
    private void initializeCloudFirestore() {
        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack(); // 이전 프래그먼트로 돌아가기
                    findFab.setVisibility(View.VISIBLE);
                }
            }
        });
    }

}