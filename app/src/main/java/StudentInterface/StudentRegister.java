package StudentInterface;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.snhslink.R;
import com.google.android.material.textfield.TextInputEditText;

import SystemInterface.DummyStudentEmailAndPassDatabase;

public class StudentRegister extends AppCompatActivity {

    private TextInputEditText emailInput, passwordInput;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_register);



        emailInput = findViewById(R.id.register_email_student);
        passwordInput = findViewById(R.id.register_password_student);
        registerButton = findViewById(R.id.btn_register_student);

        TextView loginNow = findViewById(R.id.login_now_student);
        loginNow.setOnClickListener(v -> {
            // Navigate to StudentLogin
            Intent intent = new Intent(StudentRegister.this, StudentLogin.class);
            startActivity(intent);
        });

        registerButton.setOnClickListener(v -> handleRegistration());
    }

    private void handleRegistration() {
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }



        if (DummyStudentEmailAndPassDatabase.registerStudent(email, password)) {
            Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();

            // Pass email to ProfileVerification
            Intent intent = new Intent(StudentRegister.this, ProfileVerification.class);
            intent.putExtra("USER_EMAIL", email); // Add the email to the intent
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Email already registered. Try logging in.", Toast.LENGTH_SHORT).show();
        }
    }
}
