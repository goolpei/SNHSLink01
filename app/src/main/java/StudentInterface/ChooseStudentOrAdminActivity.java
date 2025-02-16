package StudentInterface;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.snhslink.R;

public class ChooseStudentOrAdminActivity extends AppCompatActivity {

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
            Intent intent = new Intent(ChooseStudentOrAdminActivity.this, AdminInterface.AdminLogin.class);
            startActivity(intent);
        });
    }
}
