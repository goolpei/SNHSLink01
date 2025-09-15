package com.example.snhslink.Database;

import java.util.HashMap;

public class DummyStudentDataManager {
    private static DummyStudentDataManager instance;

    // Stores StudentID -> (Key-Value Pairs of Student Data)
    private final HashMap<String, HashMap<String, String>> studentData = new HashMap<>();

    // Stores DocumentType -> Document URI
    private final HashMap<String, String> documentStorage = new HashMap<>();

    private DummyStudentDataManager() {}

    public static synchronized DummyStudentDataManager getInstance() {
        if (instance == null) {
            instance = new DummyStudentDataManager();
        }
        return instance;
    }

    // ✅ Ensure student profile exists before storing data
    private void ensureStudentProfileExists(String studentID) {
        studentData.putIfAbsent(studentID, new HashMap<>());
    }

    // ✅ Save student data under their Student ID
    public void saveData(String studentID, String key, String value) {
        ensureStudentProfileExists(studentID);
        studentData.get(studentID).put(key, value);
    }

    // ✅ Retrieve specific data for a student
    public String getData(String studentID, String key) {
        return studentData.containsKey(studentID) ? studentData.get(studentID).get(key) : null;
    }

    // ✅ Retrieve all data for a student
    public HashMap<String, String> getAllData(String studentID) {
        return studentData.get(studentID);
    }

    // ✅ Store a document in the storage
    public void storeDocument(String documentType, String documentUri) {
        if (documentUri == null) {
            documentStorage.remove(documentType); // ❌ Fix: Remove if null, instead of doing nothing
        } else {
            documentStorage.put(documentType, documentUri); // ✅ Store document directly
        }
    }

    // ✅ Check if a document is uploaded
    public boolean isDocumentUploaded(String documentType) {
        return documentStorage.containsKey(documentType) && documentStorage.get(documentType) != null;
    }

    // ✅ Retrieve a document URI
    public String getDocument(String documentType) {
        return documentStorage.get(documentType); // Returns null if not found
    }
}
