package com.sentinal.session;

public class HistorySession {

    private static String currentUserId;

    public static void setCurrentUserId(String userId) {
        currentUserId = userId;
    }

    public static String getCurrentUserId() {
        return currentUserId;
    }
    
     public static void clear() {
        currentUserId = null;
    }
}
