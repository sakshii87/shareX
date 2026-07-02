package com.sentinal.view;

import java.net.HttpURLConnection;
import java.net.URL;

import com.sentinal.session.HistorySession;


public class HistoryDeleter {

    public static void deleteHistory(String keyToDelete) {
        try {
            String userEmail = HistorySession.getCurrentUserId().replace(".", "_"); // Firebase-safe path
            String firebaseUrl = "https://sentinal3-94f5e-default-rtdb.firebaseio.com/history/" 
                                 + userEmail + "/" + keyToDelete + ".json";

            HttpURLConnection conn = (HttpURLConnection) new URL(firebaseUrl).openConnection();
            conn.setRequestMethod("DELETE");
            conn.connect();

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                System.out.println("✅ Deleted successfully from Firebase Realtime Database");
            } else {
                System.err.println("❌ Failed to delete. HTTP Response Code: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


