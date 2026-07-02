package com.sentinal.view;

import com.google.gson.Gson;
import com.sentinal.model.HistoryModel;
import com.sentinal.session.HistorySession;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HistoryLogger {

    public static void logHistory(String fileName, String status) {
        try {
            String userId = HistorySession.getCurrentUserId();
            if (userId == null || userId.isEmpty()) {
                System.out.println("User ID is null or empty.");
                return;
            }

            userId = userId.replace("@", "_").replace(".", "_");
            String firebaseUrl = "https://sentinal3-94a9e-default-rtdb.firebaseio.com/history/" + userId + ".json";

            HistoryModel model = new HistoryModel(fileName, status);
            Gson gson = new Gson();
            String json = gson.toJson(model);

            URL url = new URL(firebaseUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes());
                os.flush();
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                // Print Firebase response to get key
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                System.out.println("History logged successfully. Firebase response: " + response.toString());
            } else {
                System.out.println("Failed to log history. Response code: " + responseCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}