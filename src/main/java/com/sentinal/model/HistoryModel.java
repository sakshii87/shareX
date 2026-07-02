package com.sentinal.model;

public class HistoryModel {
    private String fileName;
    private String status;
    private String firebaseKey;
     private String key;


    public HistoryModel(String fileName, String status) {
        this.fileName = fileName;
        this.status = status;
    }

    public String getFileName() {
        return fileName;
    }

    public String getStatus() {
        return status;
    }
    public void setFirebaseKey(String key) {
    this.firebaseKey = key;
}

public String getFirebaseKey() {
    return firebaseKey;
}

public String getKey() {
        return key; // ✅ So you can call item.getKey()
    }
    public void setKey(String key) {
        this.key = key;
    }
}

