// package com.sentinal.service;

// import com.google.gson.Gson;
// import com.google.gson.reflect.TypeToken;
// import com.sentinal.model.RegistrationModel;

// import java.io.OutputStream;
// import java.net.HttpURLConnection;
// import java.net.URL;
// import java.util.Map;
// import java.util.Scanner;

// public class UserService {

//     private static final String FIREBASE_URL = "https://sentinal3-94a9e-default-rtdb.firebaseio.com/users.json";

//     // 🔹 Register a new user
//     public static boolean registerUser(RegistrationModel user) throws Exception {
//         URL url = new URL(FIREBASE_URL);
//         HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//         conn.setRequestMethod("POST");
//         conn.setDoOutput(true);

//         String json = new Gson().toJson(user);

//         try (OutputStream os = conn.getOutputStream()) {
//             os.write(json.getBytes());
//         }

//         int responseCode = conn.getResponseCode();
//         return responseCode == 200;
//     }

//     // 🔹 Fetch user by email (for login or profile)
//     public static RegistrationModel fetchUserByEmail(String email) throws Exception {
//         URL url = new URL(FIREBASE_URL);
//         HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//         conn.setRequestMethod("GET");

//         Scanner scanner = new Scanner(conn.getInputStream());
//         StringBuilder json = new StringBuilder();
//         while (scanner.hasNext()) {
//             json.append(scanner.nextLine());
//         }

//         Map<String, RegistrationModel> users = new Gson().fromJson(json.toString(),
//                 new TypeToken<Map<String, RegistrationModel>>() {
//                 }.getType());

//         for (Map.Entry<String, RegistrationModel> entry : users.entrySet()) {
//             RegistrationModel u = entry.getValue();
//             if (u.getEmail().equals(email)) {
//                 return u;
//             }
//         }
//         return null;
//     }

//     // 🔹 Login check
//     public static boolean validateLogin(String email, String password) throws Exception {
//         RegistrationModel user = fetchUserByEmail(email);
//         return user != null && user.getPassword().equals(password);
//     }

//     // 🔹 Update user using email as key match (safe update)
//     public static boolean updateUserInFirebaseUsingEmailKey(RegistrationModel updatedUser) throws Exception {
//         URL url = new URL(FIREBASE_URL);
//         HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//         conn.setRequestMethod("GET");

//         Scanner scanner = new Scanner(conn.getInputStream());
//         StringBuilder json = new StringBuilder();
//         while (scanner.hasNext()) {
//             json.append(scanner.nextLine());
//         }

//         Map<String, RegistrationModel> users = new Gson().fromJson(json.toString(),
//                 new TypeToken<Map<String, RegistrationModel>>() {
//                 }.getType());

//         for (Map.Entry<String, RegistrationModel> entry : users.entrySet()) {
//             String key = entry.getKey();
//             RegistrationModel user = entry.getValue();

//             if (user.getEmail().equals(updatedUser.getEmail())) {
//                 String updateUrlStr = "https://sentinal3-94a9e-default-rtdb.firebaseio.com/users/" + key + ".json";
//                 URL updateUrl = new URL(updateUrlStr);

//                 HttpURLConnection updateConn = (HttpURLConnection) updateUrl.openConnection();
//                 updateConn.setRequestMethod("PUT");
//                 updateConn.setDoOutput(true);

//                 String updatedJson = new Gson().toJson(updatedUser);
//                 try (OutputStream os = updateConn.getOutputStream()) {
//                     os.write(updatedJson.getBytes());
//                 }

//                 return updateConn.getResponseCode() == 200;
//             }
//         }

//         return false;
//     }
// }











 package com.sentinal.service;
import com.google.gson.Gson;
import com.sentinal.model.RegistrationModel;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserService {

    private static final String DATABASE_URL = "https://sentinal3-94a9e-default-rtdb.firebaseio.com"; // ✅ replace

    public static void updateUser(RegistrationModel user) throws Exception {
        URL url = new URL(DATABASE_URL + "/users/" + user.getEmail().replace(".", "_") + ".json");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("PUT");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");

        String json = new Gson().toJson(user);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes());
        }

        if (conn.getResponseCode() != 200) {
            throw new Exception("Failed to update user. Response: " + conn.getResponseCode());
        }
    }
}

