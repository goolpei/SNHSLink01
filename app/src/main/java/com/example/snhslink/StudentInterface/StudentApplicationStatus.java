package com.example.snhslink.StudentInterface;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.snhslink.BaseActivity;
import com.example.snhslink.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class StudentApplicationStatus extends BaseActivity {

    private TextView statusTextView, studentIdTextView;
    private Button exitButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_application_status);

        mAuth = FirebaseAuth.getInstance();

        statusTextView = findViewById(R.id.textview_application_status);
        studentIdTextView = findViewById(R.id.textview_user_unique_id);
        exitButton = findViewById(R.id.button_exit);
        db = FirebaseFirestore.getInstance();

        // Get the logged-in student's userID from intent
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");

        if (userID == null) {
            Toast.makeText(this, "Error: User ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Fetch application status from Firestore
        fetchStudentData();
        loadApplicationStatus();

        // Exit button functionality
        exitButton.setOnClickListener(v -> finish());
    }

    private void fetchStudentData() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            // No logged-in user, send them back to login
            Toast.makeText(this, "Session expired. Please log in again.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(StudentApplicationStatus.this, StudentLogin.class));
            finish();
            return;
        }

        userID = user.getUid(); // Get Firebase UID

        db.collection("students").document(userID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {

                        String studentID = task.getResult().getString("studentID");

                        // Display student data in UI

                        studentIdTextView.setText("Your Applicant ID: " + studentID);
                    } else {
                        Toast.makeText(this, "Failed to retrieve student data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadApplicationStatus() {
        DocumentReference statusRef = db.collection("students")
                .document(userID)
                .collection("application_status")  // Fetch from correct subcollection
                .document("status");

        statusRef.get(com.google.firebase.firestore.Source.SERVER) // Force fresh data from Firestore
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String status = documentSnapshot.getString("applicationStatus");
                        if (status != null) {
                            if (status.equalsIgnoreCase("Approved")) {
                                statusTextView.setText("Your application status: " + status + "\n\n" +
                                        "📅 Entrance Exam Date: February 10, 2026\n" +
                                        "📍 Location: Sagay National High School Senior High Building, Room 1\n" +
                                        "📝 Things to Bring: Admission Confirmation (Screenshot), Ballpen, Pencil, Eraser, and School ID");
                            } else {
                                statusTextView.setText("Your application status: " + status);
                            }
                            Toast.makeText(this, "Status retrieved: " + status, Toast.LENGTH_SHORT).show();
                        } else {
                            statusTextView.setText("Your application status: Pending");
                        }
                    } else {
                        statusTextView.setText("There is no application status yet...");
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to retrieve status", Toast.LENGTH_SHORT).show();
                });
    }




}
