package SystemInterface;

import java.util.HashMap;

public class DummyStudentEmailAndPassDatabase {

    private static final HashMap<String, String> studentDatabase = new HashMap<>();

    // Register a new student
    public static boolean registerStudent(String email, String password) {
        if (studentDatabase.containsKey(email)) {
            return false; // Email already exists
        }
        studentDatabase.put(email, password);
        return true;
    }

    // Validate login details
    public static boolean validateStudent(String email, String password) {
        return studentDatabase.containsKey(email) && studentDatabase.get(email).equals(password);
    }
}
