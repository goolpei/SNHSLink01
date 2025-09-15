package com.example.snhslink.AdminInterface;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snhslink.BaseActivity;
import com.example.snhslink.R;
import com.example.snhslink.Adapters.StudentAdapter;
import com.example.snhslink.Models.StudentApplication;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class AdminDashboard extends BaseActivity {

    private EditText searchEditText;
    private RecyclerView recyclerView;
    private Button refreshButton, logoutButton;
    private StudentAdapter adapter;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_admin_dashboard);



        db = FirebaseFirestore.getInstance();


        searchEditText = findViewById(R.id.edittext_search);
        recyclerView = findViewById(R.id.recyclerview_student_applications);
        refreshButton = findViewById(R.id.button_refresh);
        logoutButton = findViewById(R.id.button_logout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadStudentApplications("");

        // Search functionality
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loadStudentApplications(charSequence.toString().trim());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        refreshButton.setOnClickListener(v -> {
            if (adapter != null) {
                adapter.stopListening(); // Stop listening before refreshing
                adapter = null; // Reset the adapter
            }

            searchEditText.setText(""); // Clear search bar
            loadStudentApplications(""); // Reload data
        });



        logoutButton.setOnClickListener(v -> showLogoutConfirmation());

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showLogoutConfirmation();
            }
        });
    }




    private void loadStudentApplications(String searchQuery) {
        Query firestoreQuery;

        if (searchQuery.isEmpty()) {
            // Load all students if no search query
            firestoreQuery = db.collection("students");
        } else {
            // Ensure query works correctly (adjust as needed)
            firestoreQuery = db.collection("students")
                    .whereEqualTo("basic_information.data.firstName", searchQuery);
        }

        FirestoreRecyclerOptions<StudentApplication> options = new FirestoreRecyclerOptions.Builder<StudentApplication>()
                .setQuery(firestoreQuery, snapshot -> {
                    String userID = snapshot.getId();
                    StudentApplication student = new StudentApplication();
                    student.setUserID(userID);

                    // Fetch basic information
                    DocumentSnapshot basicInfo = snapshot.get("basic_information.data", DocumentSnapshot.class);
                    if (basicInfo != null) {
                        student.setFirstName(basicInfo.getString("firstName"));
                        student.setLastName(basicInfo.getString("lastName"));
                    }

                    // Fetch application status
                    DocumentSnapshot appStatus = snapshot.get("application_status.status", DocumentSnapshot.class);
                    if (appStatus != null) {
                        student.setApplicationStatus(appStatus.getString("applicationStatus"));
                    } else {
                        student.setApplicationStatus("Pending"); // Default if not set
                    }

                    return student;
                })
                .build();

        if (adapter == null) {
            adapter = new StudentAdapter(options, this);
            recyclerView.setAdapter(adapter);
            adapter.startListening();
        } else {
            // Prevent crashes by safely updating the adapter
            runOnUiThread(() -> {
                try {
                    adapter.updateOptions(options);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }


    private void showLogoutConfirmation() {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Intent intent = new Intent(AdminDashboard.this, AdminLogin.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
        }
    }
}