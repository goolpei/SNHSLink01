package StudentInterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.snhslink.R;
import com.google.android.material.textfield.TextInputEditText;

import SystemInterface.DummyStudentEmailAndPassDatabase;

public class StudentLogin extends AppCompatActivity {

    private TextInputEditText emailInput, passwordInput;
    private ProgressBar progressBar;
    private Button loginButton;
    private EditText emailEditText, passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_login);



        // Initialize views
        emailInput = findViewById(R.id.login_email_student);
        passwordInput = findViewById(R.id.login_password_student);
        progressBar = findViewById(R.id.progressBar);
        loginButton = findViewById(R.id.btn_login_student);
        TextView registerNow = findViewById(R.id.register_now_student);

        // Initialize views
        emailEditText = findViewById(R.id.login_email_student);
        passwordEditText = findViewById(R.id.login_password_student);
        loginButton = findViewById(R.id.btn_login_student);


        // Login button functionality
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();


            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(StudentLogin.this, "Please enter both fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (DummyStudentEmailAndPassDatabase.validateStudent(email, password)) {
                Toast.makeText(StudentLogin.this, "Login successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(StudentLogin.this, ProfileVerification.class);
                intent.putExtra("USER_EMAIL", email); // Pass the email to the next screen
                startActivity(intent);
            } else {
                Toast.makeText(StudentLogin.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
            }
        });


        // Navigate to registration page
        registerNow.setOnClickListener(v -> {
            Intent intent = new Intent(StudentLogin.this, StudentRegister.class);
            startActivity(intent);
        });
    }


    // Dummy login validation
    private boolean isValidLogin(String email, String password) {
        // Example validation for email and password (replace with your real logic)
        return !password.isEmpty();
    }

    private void loginUser() {
        String email = emailInput.getText() != null ? emailInput.getText().toString().trim() : "";
        String password = passwordInput.getText() != null ? passwordInput.getText().toString().trim() : "";

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show progress bar and disable interactions
        progressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);

        // Validate login details
        boolean isValid = DummyStudentEmailAndPassDatabase.validateStudent(email, password);

        // Hide progress bar and re-enable interactions
        progressBar.setVisibility(View.GONE);
        loginButton.setEnabled(true);

        if (isValid) {
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();
            // Proceed to the main activity or dashboard
            // Intent intent = new Intent(this, MainDashboard.class); // Uncomment and define your dashboard activity
            // startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }
}
