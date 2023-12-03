package com.example.find_ssu.LookFor;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.ServerTimestamp;

public class LookForPost {
    private String name;
    private String location;
    private String date;
    private String more;
    private String image;
    @ServerTimestamp
    private Timestamp timestamp; // server timestamp
    private String uid;
    private String documentuid;

    public LookForPost() {}

    public LookForPost(String name, String location,String date, String more, String image,String documentuid) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.more = more;
        this.image = image;
        this.uid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        this.documentuid=documentuid;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
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
