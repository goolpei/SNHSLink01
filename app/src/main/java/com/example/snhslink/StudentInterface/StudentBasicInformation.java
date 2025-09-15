package com.example.snhslink.StudentInterface;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.snhslink.BaseActivity;
import com.example.snhslink.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class StudentBasicInformation extends BaseActivity {

    // Declare UI components
    private TextView textViewBirthdate, textViewRegisterLink;
    private EditText editTextLastName, editTextFirstName, editTextMiddleName, editTextEmail, editTextMobileNumber;
    private Spinner spinnerStrand;
    private TextView usernameTextView, studentIdTextView;
    private Button buttonContinue;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userID; // Firebase UID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_basic_information);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize UI components
        textViewBirthdate = findViewById(R.id.textview_birthdate_student);
        editTextLastName = findViewById(R.id.edittext_last_name_student);
        editTextFirstName = findViewById(R.id.edittext_first_name_student);
        editTextMiddleName = findViewById(R.id.edittext_middle_name_student);
        editTextEmail = findViewById(R.id.edittext_email_student);
        editTextMobileNumber = findViewById(R.id.edittext_mobile_number_student);
        spinnerStrand = findViewById(R.id.spinner_strand);
        textViewRegisterLink = findViewById(R.id.textview_register_link);
        usernameTextView = findViewById(R.id.textview_user_username);
        studentIdTextView = findViewById(R.id.textview_user_unique_id);
        buttonContinue = findViewById(R.id.button_continue);

        // Retrieve student data from Firestore
        fetchStudentData();

        // Clear and Save buttons
        Button buttonClear = findViewById(R.id.button_clear);
        buttonClear.setOnClickListener(view -> clearAllEntries());


        // Birthdate picker
        textViewBirthdate.setOnClickListener(view -> showDatePickerDialog());

        // Register link
        textViewRegisterLink.setOnClickListener(view -> navigateToRegisterPage());

        buttonContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show confirmation dialog
                new AlertDialog.Builder(StudentBasicInformation.this) // Replace 'YourActivity' with your actual activity name
                        .setTitle("Confirm Information")
                        .setMessage("Please review your information carefully. Once you continue, you won’t be able to make any changes.")
                        .setPositiveButton("Yes, Continue", new DialogInterface.OnClickListener() {



                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //save student data
                                saveStudentData();

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Dismiss dialog, user can edit info
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });



    }


    private void fetchStudentData() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            // No logged-in user, send them back to login
            Toast.makeText(this, "Session expired. Please log in again.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(StudentBasicInformation.this, StudentLogin.class));
            finish();
            return;
        }

        userID = user.getUid(); // Get Firebase UID

        db.collection("students").document(userID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        String username = task.getResult().getString("username");
                        String studentID = task.getResult().getString("studentID");

                        // Display student data in UI
                        usernameTextView.setText("Welcome, " + username);
                        studentIdTextView.setText("Your Applicant ID: " + studentID);
                    } else {
                        Toast.makeText(this, "Failed to retrieve student data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void saveStudentData() {
        String lastName = editTextLastName.getText().toString().trim();
        String firstName = editTextFirstName.getText().toString().trim();
        String middleName = editTextMiddleName.getText().toString().trim();
        String birthdate = textViewBirthdate.getText().toString().trim();

        if (lastName.isEmpty() || firstName.isEmpty() || middleName.isEmpty() || birthdate.isEmpty()) {
            Toast.makeText(this, "Please fill out all required fields", Toast.LENGTH_SHORT).show();
            return;
        }

        String email =  getInputOrNA(editTextEmail);
        String mobileNumber =  getInputOrNA(editTextMobileNumber);
        String strand = spinnerStrand.getSelectedItem() != null ? spinnerStrand.getSelectedItem().toString() : "N/A";



        // Store data in Firestore under `students/{UID}/basic_information`
        Map<String, Object> studentData = new HashMap<>();
        studentData.put("lastName", lastName);
        studentData.put("firstName", firstName);
        studentData.put("middleName", middleName);
        studentData.put("email", email);
        studentData.put("mobileNumber", mobileNumber);
        studentData.put("birthdate", birthdate);
        studentData.put("strand", strand);

        db.collection("students").document(userID).collection("basic_information")
                .document("data")
                .set(studentData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(StudentBasicInformation.this, "Data saved successfully!", Toast.LENGTH_SHORT).show();

                    // Navigate to StudentPersonalInformation
                    Intent intent = new Intent(StudentBasicInformation.this, StudentPersonalInformation.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(StudentBasicInformation.this, "Failed to save data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void navigateToRegisterPage() {
        Intent intent = new Intent(StudentBasicInformation.this, StudentRegister.class);
        startActivity(intent);
    }

    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                StudentBasicInformation.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    textViewBirthdate.setText(selectedDate);
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }

    private void clearAllEntries() {
        editTextLastName.setText("");
        editTextFirstName.setText("");
        editTextMiddleName.setText("");
        editTextEmail.setText("");
        editTextMobileNumber.setText("");
        textViewBirthdate.setText("");
        spinnerStrand.setSelection(0);

        Toast.makeText(this, "All entries cleared", Toast.LENGTH_SHORT).show();
    }

    private String getInputOrNA(EditText editText) {
        return editText.getText().toString().trim().isEmpty() ? "N/A" : editText.getText().toString().trim();
    }
}
