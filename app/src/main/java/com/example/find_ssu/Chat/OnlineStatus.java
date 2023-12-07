package com.example.find_ssu.Chat;

import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;

public class OnlineStatus {
    private static final String TAG = "OnlineStatus";
    private static final String COLLECTION_NAME = "user";

    private FirebaseFirestore db;

    public OnlineStatus() {
        db = FirebaseFirestore.getInstance();
    }

    public void setUserOnline(String userId) {
        DocumentReference userRef = db.collection(COLLECTION_NAME).document(userId);
        userRef.set(new HashMap<String, Object>() {{
                    put("status", "online");
                    put("lastSeen", null);
                }})
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "User is online");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error setting user online status", e);
                    }
                });
    }

    public void setUserOffline(String userId) {
        DocumentReference userRef = db.collection(COLLECTION_NAME).document(userId);
        userRef.update("status", "offline", "lastSeen", FieldValue.serverTimestamp())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "User is offline");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "Error setting user offline status", e);
                    }
                });
    }
}
