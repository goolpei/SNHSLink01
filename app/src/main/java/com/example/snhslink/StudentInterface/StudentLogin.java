package com.example.snhslink.StudentInterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.snhslink.BaseActivity;
import com.example.snhslink.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

public class StudentLogin extends BaseActivity {

    private TextInputEditText usernameInput, passwordInput;
    private ProgressBar progressBar;
    private Button loginButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize UI components
        usernameInput = findViewById(R.id.login_username_student);
        passwordInput = findViewById(R.id.login_password_student);
        progressBar = findViewById(R.id.progressBar);
        loginButton = findViewById(R.id.btn_login_student);
        TextView registerNow = findViewById(R.id.register_now_student);

        loginButton.setOnClickListener(v -> loginUser());

        registerNow.setOnClickListener(v -> {
            Intent intent = new Intent(StudentLogin.this, StudentRegister.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show progress bar and disable interactions
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);

        // Convert username to fake email format
        String fakeEmail = username + "@snhslink.com";

        // Authenticate user with Firebase
        mAuth.signInWithEmailAndPassword(fakeEmail, password)
                .addOnCompleteListener(this, task -> {
                    progressBar.setVisibility(View.GONE);
                    loginButton.setEnabled(true);

                    if (task.isSuccessful()) {
                        String userID = mAuth.getCurrentUser().getUid(); // Get Firebase User ID

                        Toast.makeText(StudentLogin.this, "Login successful!", Toast.LENGTH_SHORT).show();

                        // Navigate to StudentApplicationStatus & pass userID
                        Intent intent = new Intent(StudentLogin.this, StudentApplicationStatus.class);
                        intent.putExtra("userID", userID); // Pass userID
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(StudentLogin.this, "Login failed: " +
                                task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
