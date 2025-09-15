package com.example.snhslink.StudentInterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.snhslink.BaseActivity;
import com.example.snhslink.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class StudentRegister extends BaseActivity {

    private TextInputEditText usernameInput, passwordInput;
    private Button registerButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_register);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        usernameInput = findViewById(R.id.register_username_student);
        passwordInput = findViewById(R.id.register_password_student);
        registerButton = findViewById(R.id.btn_register_student);
        progressBar = findViewById(R.id.progressBar);

        TextView loginNow = findViewById(R.id.login_now_student);
        loginNow.setOnClickListener(v -> {
            Intent intent = new Intent(StudentRegister.this, StudentLogin.class);
            startActivity(intent);
        });

        registerButton.setOnClickListener(v -> handleRegistration());
    }

    private void handleRegistration() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        registerButton.setEnabled(false);

        // Firebase Authentication requires an email, so we generate a fake email
        String fakeEmail = username + "@snhslink.com";

        // Register user with Firebase Authentication
        mAuth.createUserWithEmailAndPassword(fakeEmail, password)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE);
                    registerButton.setEnabled(true);

                    if (task.isSuccessful()) {
                        // Get Firebase UID
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            String firebaseUID = user.getUid();  // Unique Firebase User ID

                            // Generate a custom Student ID (6-digit number)
                            String customStudentID = generateCustomStudentID();

                            // Store student data in Firestore
                            saveUserToFirestore(firebaseUID, username, customStudentID);
                        }
                    } else {
                        Toast.makeText(StudentRegister.this, "Registration failed: " +
                                task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String generateCustomStudentID() {
        return "SNHS" + (100000 + new Random().nextInt(900000)); // Generates SNHS + 6-digit number
    }

    private void saveUserToFirestore(String firebaseUID, String username, String customStudentID) {
        Map<String, Object> studentData = new HashMap<>();
        studentData.put("username", username);
        studentData.put("firebaseUID", firebaseUID); // Store Firebase UID
        studentData.put("studentID", customStudentID); // Store custom Student ID

        db.collection("students").document(firebaseUID)
                .set(studentData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(StudentRegister.this, "Registration successful!\nYour Student ID: " + customStudentID, Toast.LENGTH_LONG).show();

                    // Navigate to StudentBasicInformation WITHOUT passing data
                    Intent intent = new Intent(StudentRegister.this, StudentBasicInformation.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(StudentRegister.this, "Failed to save data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
