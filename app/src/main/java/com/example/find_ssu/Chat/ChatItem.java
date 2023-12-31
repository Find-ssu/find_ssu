package com.example.find_ssu.Chat;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.ServerTimestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ChatItem {
    String uid1;
    String uid2;
    String message;
    String documentId;
    String timestamp;
    String superdocumentId;
    String name;
    String where;
    @ServerTimestamp
    private Timestamp time;
    String subdocumentId;

    public ChatItem(String uid1, String uid2, String message, String documentId, String timestamp, String superdocumentId, String name, String where, String subdocumentId){
        this.uid1 = uid1;
        this.uid2 = uid2;
        this.message = message;
        this.documentId = documentId;
        this.timestamp= timestamp;
        this.superdocumentId = superdocumentId;
        this.name = name;
        this.where = where;
        this.subdocumentId = subdocumentId;
    }

    public String getUid1(){return uid1;}
    public String getUid2(){return uid2;}
    public String getMessage(){return message;}
    public String getDocumentId(){return documentId;}
    public String getTimestamp() {
        return timestamp;
    }
    public String getSuperdocumentId(){return superdocumentId;}
    public String getName(){return name;}
    public String getWhere(){return where;}
    public Timestamp getTime(){return time;}
    public String getSubdocumentId(){return subdocumentId;}
}
