package com.example.snhslink.StudentInterface;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.snhslink.BaseActivity;
import com.example.snhslink.R;

public class DonePleaseWaitForConfirmation extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_please_wait_for_confirmation);


        // Initialize UI Components
        TextView textViewSelectUserLink = findViewById(R.id.textview_go_to_select_user);
        Button exitButton = findViewById(R.id.exit_btn);
        Button goToLoginButton = findViewById(R.id.go_to_login_btn);


        // Set up Select User Link
        textViewSelectUserLink.setOnClickListener(view -> navigateToSelectUserPage());
        // Exit button
        exitButton.setOnClickListener(v -> {
            // Exit the app
            finishAffinity(); // Closes all activities and exits the app
            System.exit(0);
        });
        // Login button
        goToLoginButton.setOnClickListener(v -> {
            // Navigate to the login page
            Intent intent = new Intent(DonePleaseWaitForConfirmation.this, StudentLogin.class);
            startActivity(intent);
        });
    }
    private void navigateToSelectUserPage() {
        // Intent to navigate to the SelectUser activity
        Intent intent = new Intent(DonePleaseWaitForConfirmation.this, ChooseStudentOrAdminActivity.class);
        startActivity(intent);
    }
}
