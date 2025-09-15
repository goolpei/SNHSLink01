package com.example.snhslink.Database;

import java.util.HashMap;
import java.util.UUID;

public class DummyStudentAccountDatabase {

    private static final HashMap<String, String> studentAccounts = new HashMap<>(); // Stores username -> password
    private static final HashMap<String, String> studentIDs = new HashMap<>(); // Stores username -> studentID

    // Generate a shorter Student ID
    private static String generateShortStudentID() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
    }
    // Register a new student and assign a unique Student ID
    public static String registerStudent(String username, String password) {
        if (studentAccounts.containsKey(username)) {
            return null; // Username already exists
        }

        String studentID = generateShortStudentID(); // Generate shorter Student ID
        studentAccounts.put(username, password);
        studentIDs.put(username, studentID);

        return studentID; // Return the generated Student ID
    }

    // Validate login and return Student ID if successful
    public static String loginStudent(String username, String password) {
        if (studentAccounts.containsKey(username) && studentAccounts.get(username).equals(password)) {
            return studentIDs.get(username); // Return Student ID
        }
        return null; // Invalid login
    }

    // Retrieve Student ID by username (if needed for other operations)
    public static String getStudentID(String username) {
        return studentIDs.get(username);
    }
}
