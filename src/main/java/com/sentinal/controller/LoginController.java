package com.sentinal.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sentinal.model.LoginModel;
import com.sentinal.model.RegistrationModel;
import com.sentinal.session.HistorySession;
import com.sentinal.session.Session;

import java.lang.reflect.Type;
import java.util.Map;

public class LoginController {

    private final String firebaseUrl = "https://sentinal3-94a9e-default-rtdb.firebaseio.com/users.json";

    public void validateLogin(LoginModel user) throws Exception {

        if (user.getEmail().isEmpty() || user.getPassword().isEmpty()) {
            throw new Exception("Email and Password cannot be empty.");
        }

        if (!user.getEmail().matches("^\\S+@\\S+\\.\\S+$")) {
            throw new Exception("Invalid email format.");
        }

        if (user.getPassword().length() < 6) {
            throw new Exception("Password must be at least 6 characters.");
        }
    }
                    
    public boolean performLogin(LoginModel user) throws Exception {
        URL url = new URL(firebaseUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;

        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        Gson gson = new Gson();
        Type userType = new TypeToken<Map<String, Map<String, String>>>() {
        }.getType();
        Map<String, Map<String, String>> users = gson.fromJson(response.toString(), userType);

        if (users != null) {
            for (Map<String, String> userData : users.values()) {
                if (user.getEmail().equals(userData.get("email")) &&
                        user.getPassword().equals(userData.get("password"))) {

                    RegistrationModel regUser = new RegistrationModel(
                            userData.get("name"),
                            userData.get("email"),
                            userData.get("password"));

                    Session.setCurrentUser(regUser); // Set full user
                    Session.setLoggedInEmail(user.getEmail()); // Set email for ProfilePage
                    HistorySession.setCurrentUserId(user.getEmail());

                    return true;
                }

            }

        }

        return false;
    }

}
