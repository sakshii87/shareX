package com.sentinal.controller;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sentinal.model.RegistrationModel;

public class RegistrationController {

    public void validateUser(RegistrationModel user, String confirmPassword) throws Exception {
        if (user.getName().isEmpty() || user.getEmail().isEmpty() || user.getPassword().isEmpty()) {
            throw new Exception("Please fill all fields.");
        }

        if (!user.getName().matches("^[a-zA-Z\\s]+$")) {
            throw new Exception("Name Field Should Contain Alphabets Only.");
        }

        if (!user.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) {
            throw new Exception("Please enter a valid email address.");
        }

        if (!user.getPassword().equals(confirmPassword)) {
            throw new Exception("Passwords do not match.");
        }
    }

    public int registerUser(RegistrationModel user) throws Exception {
        String firebaseUrl = "https://sentinal3-94a9e-default-rtdb.firebaseio.com/users.json";

        // 🔹 Step 1: Fetch existing users
        URL fetchUrl = new URL(firebaseUrl);
        HttpURLConnection fetchConn = (HttpURLConnection) fetchUrl.openConnection();
        fetchConn.setRequestMethod("GET");

        StringBuilder json = new StringBuilder();
        try (Scanner scanner = new Scanner(fetchConn.getInputStream())) {
            while (scanner.hasNext()) {
                json.append(scanner.nextLine());
            }
        }

        // 🔹 Step 2: Parse response
        Map<String, RegistrationModel> users = new Gson().fromJson(json.toString(),
                new TypeToken<Map<String, RegistrationModel>>() {
                }.getType());

        // 🔹 Step 3: Check if email already exists
        for (RegistrationModel existingUser : users.values()) {
            if (existingUser.getEmail().equalsIgnoreCase(user.getEmail())) {
                throw new Exception("Email already exists. Please try logging in.");
            }
        }

        // 🔹 Step 4: Prepare JSON for new user
        String userJson = String.format(
                "{\"name\":\"%s\",\"email\":\"%s\",\"password\":\"%s\"}",
                user.getName(), user.getEmail(), user.getPassword());

        // 🔹 Step 5: Register new user
        HttpURLConnection postConn = (HttpURLConnection) new URL(firebaseUrl).openConnection();
        postConn.setRequestMethod("POST");
        postConn.setRequestProperty("Content-Type", "application/json; utf-8");
        postConn.setDoOutput(true);

        try (OutputStream os = postConn.getOutputStream()) {
            byte[] input = userJson.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        return postConn.getResponseCode();
    }

}
