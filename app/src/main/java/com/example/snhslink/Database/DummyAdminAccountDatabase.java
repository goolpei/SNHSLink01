package com.example.snhslink.Database;

import java.util.HashMap;

public class DummyAdminAccountDatabase {
    // Hardcoded admin account (Replace this with real authentication later)
    private static HashMap<String, String> adminAccounts = new HashMap<>();

    static {
        adminAccounts.put("admin", "password123"); // Username: admin, Password: password123
    }

    // Check if login is valid
    public static boolean isValidAdmin(String username, String password) {
        return adminAccounts.containsKey(username) && adminAccounts.get(username).equals(password);
    }
}
