package com.sentinal.view;

import com.day.cq.workflow.exec.HistoryItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sentinal.model.HistoryModel;

import javafx.scene.control.ListView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class FirebaseHistoryFetcher {

    public static List<HistoryItem> fetchHistory(String userEmail, ListView<HistoryModel> listView) {
        List<HistoryItem> historyList = new ArrayList<>();
        try {
            String formattedEmail = userEmail.replace(".", "_");
            String firebaseUrl = "https://sentinal3-94f5e-default-rtdb.firebaseio.com/history/" + formattedEmail + ".json";

            URL url = new URL(firebaseUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) response.append(line);
            reader.close();

            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, HistoryModel>>() {}.getType();
            Map<String, HistoryModel> data = gson.fromJson(response.toString(), type);

            if (data != null) {
                for (Map.Entry<String, HistoryModel> entry : data.entrySet()) {
                    HistoryModel item = entry.getValue();
                    item.setKey(entry.getKey()); // set the Firebase key
                    List<HistoryModel> list = new ArrayList<>();
                    list.add(item);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return historyList;
    }
}

