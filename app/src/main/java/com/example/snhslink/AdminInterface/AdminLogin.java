package com.example.snhslink.AdminInterface;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.snhslink.BaseActivity;
import com.example.snhslink.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class AdminLogin extends BaseActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        usernameEditText = findViewById(R.id.edittext_admin_username);
        passwordEditText = findViewById(R.id.edittext_admin_password);
        loginButton = findViewById(R.id.button_admin_login);
        progressBar = findViewById(R.id.progressBar);

        loginButton.setOnClickListener(v -> {
            String email = usernameEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(AdminLogin.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            authenticateAdmin(email, password);
            progressBar.setVisibility(View.VISIBLE);
            loginButton.setEnabled(false);



        });
    }

    private void authenticateAdmin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {


                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            verifyAdminRole(user.getUid());
                        }
                    } else {
                        Toast.makeText(AdminLogin.this, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void verifyAdminRole(String userId) {
        db.collection("admins").document(userId).get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    loginButton.setEnabled(true);

                    if (task.isSuccessful() && task.getResult().exists()) {
                        DocumentSnapshot document = task.getResult();
                        String role = document.getString("role");
                        if ("superadmin".equals(role)) {
                            Toast.makeText(AdminLogin.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AdminLogin.this, AdminDashboard.class));
                            finish();
                        } else {
                            Toast.makeText(AdminLogin.this, "Access Denied!", Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().signOut();
                        }
                    } else {
                        Toast.makeText(AdminLogin.this, "Access Denied!", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }
                });
    }
}
