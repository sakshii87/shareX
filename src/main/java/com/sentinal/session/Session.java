// package com.sentinal.session;

// import com.sentinal.model.RegistrationModel;

// public class Session {
//     private static RegistrationModel currentUser;

//     public static void setCurrentUser(RegistrationModel user) {
//         currentUser = user;
//     }

//     public static RegistrationModel getCurrentUser() {
//         return currentUser;
//     }

//     public static String getLoggedInEmail() {
//         return currentUser != null ? currentUser.getEmail() : null;
//     }

//     public static void setLoggedInEmail(String email) {
//         if (currentUser != null) {
//             currentUser.setEmail(email);
//         }

//     }
// }







package com.sentinal.session;

import com.sentinal.model.RegistrationModel;

public class Session {

    private static RegistrationModel currentUser = null;

    // 🔹 Store the current user session
    public static void setCurrentUser(RegistrationModel user) {
        currentUser = user;
    }

    // 🔹 Get the current logged in user
    public static RegistrationModel getCurrentUser() {
        return currentUser;
    }

    // 🔹 Optionally store logged in email
    private static String loggedInEmail = null;

    public static void setLoggedInEmail(String email) {
        loggedInEmail = email;
    }

    public static String getLoggedInEmail() {
        return loggedInEmail;
    }

    public static void clearSession() {
        currentUser = null;
        loggedInEmail = null;
    }
}
