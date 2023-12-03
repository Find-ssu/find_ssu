package com.example.find_ssu.Find;

import android.net.Uri;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.List;

public class FindPost {
    private String name;

    private String location;
    private String location_detail;
    private String date;
    private String more;
    private String image;
    private String uid;
    private String documentuid;
    @ServerTimestamp
    private Timestamp timestamp; // server timestamp

    public FindPost() {}

    public FindPost(String name, String location, String location_detail, String date, String more, String image, String documentuid, String uid) {
        this.name = name;
        this.location = location;
        this.location_detail = location_detail;
        this.date = date;
        this.more = more;
        this.image = image;
        this.uid= uid;
        this.documentuid=documentuid;
        };


    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getLocation_detail() {
        return location_detail;
    }

    public String getDate() {
        return date;
    }

    public String getMore() { return more;}

    public String getImage() {
        return image;
    }

    public Timestamp getTimestamp() { return timestamp; }
    public String getUid(){return uid;}
    public String getDocumentuid(){return documentuid;}
}
