package com.sentinal.service;

import java.net.HttpURLConnection;
import java.net.URL;

public class FirebaseDeleteService {
    public static void deleteHistoryItem(String userEmail, String historyKey) {
        try {
            String formattedEmail = userEmail.replace(".", "_");
            String firebaseUrl = "https://sentinal3-94f5e-default-rtdb.firebaseio.com/history/" 
                    + formattedEmail + "/" + historyKey + ".json";

            HttpURLConnection conn = (HttpURLConnection) new URL(firebaseUrl).openConnection();
            conn.setRequestMethod("DELETE");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                System.out.println("✅ Deleted successfully from database");
            } else {
                System.err.println("❌ Failed to delete. Code: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

