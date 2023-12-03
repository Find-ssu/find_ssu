package com.example.find_ssu.Home;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

public class HomePost {
    private String number;
    private String name;
    private String date;
    private String location;
    private String img;
    private String timestamp;

    public HomePost(String number, String name, String date, String location, String img, String timestamp) {
        this.number = number;
        this.name = name;
        this.date = date;
        this.location = location;
        this.img = img;
        this.timestamp = timestamp;
    }

    public String getNumber(){return number;}
    public String gethomeName(){return name;}
    public  String gethomeDate(){return date;}
    public String gethomeLocation(){return location;}
    public String getImg(){return img;}
    public String getTimestamp(){return timestamp;}
}
