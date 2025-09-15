package com.example.snhslink.StudentInterface;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.snhslink.AdminInterface.AdminLogin;
import com.example.snhslink.BaseActivity;
import com.example.snhslink.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class ChooseStudentOrAdminActivity extends BaseActivity {

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_choose_student_or_admin);



        // Initialize and set up the Student button
        Button studentButton = findViewById(R.id.student_btn);
        studentButton.setOnClickListener(v -> {
            // Navigate to StudentRegister
            Intent intent = new Intent(ChooseStudentOrAdminActivity.this, StudentRegister.class);
            startActivity(intent);
        });

        // Initialize and set up the Admin button
        Button adminButton = findViewById(R.id.admin_btn); // Initialize the admin button
        adminButton.setOnClickListener(v -> {
            // Navigate to AdminLogin
            Intent intent = new Intent(ChooseStudentOrAdminActivity.this, AdminLogin.class);
            startActivity(intent);
        });
    }
}
