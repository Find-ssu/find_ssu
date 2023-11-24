package com.example.find_ssu;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

public class LookForPost {
    private String name;
    private String location;
    private String date;
    private String more;
    private String image;
    @ServerTimestamp
    private Timestamp timestamp; // server timestamp

    public LookForPost() {}

    public LookForPost(String name, String location,String date, String more, String image) {
        this.name = name;
        this.location = location;
        this.date = date;
        this.more = more;
        this.image = image;
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
}
