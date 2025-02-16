package StudentInterface;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.snhslink.R;

import java.util.Calendar;

public class ProfileVerification extends AppCompatActivity {

    // Declare UI components
    private TextView textViewBirthdate, textViewRegisterLink;
    private EditText editTextLastName, editTextFirstName, editTextMiddleName, editTextLastSchool;
    private Spinner spinnerStrand;
    private TextView emailTextView;
    private Button saveProfileButton;

    private String studentEmail; // Store email passed from login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_verification);



        // Initialize UI components
        textViewBirthdate = findViewById(R.id.textview_birthdate_student);
        editTextLastName = findViewById(R.id.edittext_last_name_student);
        editTextFirstName = findViewById(R.id.edittext_first_name_student);
        editTextMiddleName = findViewById(R.id.edittext_middle_name_student);
        editTextLastSchool = findViewById(R.id.edittext_last_school_student);
        spinnerStrand = findViewById(R.id.spinner_strand);
        textViewRegisterLink = findViewById(R.id.textview_register_link);
        emailTextView = findViewById(R.id.textview_user_email);




        // Receive the email from the intent
        Intent intent = getIntent();
        String userEmail = intent.getStringExtra("USER_EMAIL");

        // If email is received, set it to the TextView and show a Toast
        if (userEmail != null) {
            emailTextView.setText("Welcome, " + userEmail);  // Display email in TextView
            Toast.makeText(this, "Welcome, " + userEmail, Toast.LENGTH_SHORT).show();
        }

        // Clear and Continue buttons
        Button buttonClear = findViewById(R.id.button_clear);
        buttonClear.setOnClickListener(view -> clearAllEntries());

        Button buttonContinue = findViewById(R.id.button_continue);
        buttonContinue.setOnClickListener(view -> navigateToPersonalInformationPage());

        // Birthdate picker
        textViewBirthdate.setOnClickListener(view -> showDatePickerDialog());

        // Register link
        textViewRegisterLink.setOnClickListener(view -> navigateToRegisterPage());
    }


    private void navigateToRegisterPage() {
        // Intent to navigate to the StudentRegister activity
        Intent intent = new Intent(ProfileVerification.this, StudentRegister.class);
        startActivity(intent);
    }

    private void navigateToPersonalInformationPage() {
        // Intent to navigate to the StudentPersonalInformation activity
        Intent intent = new Intent(ProfileVerification.this, StudentPersonalInformation.class);

        // Pass student details
        intent.putExtra("LAST_NAME", editTextLastName.getText().toString());
        intent.putExtra("FIRST_NAME", editTextFirstName.getText().toString());
        intent.putExtra("MIDDLE_NAME", editTextMiddleName.getText().toString());
        intent.putExtra("BIRTHDATE", textViewBirthdate.getText().toString());

        startActivity(intent);
    }

    private void showDatePickerDialog() {
        // Get the current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create and show a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                ProfileVerification.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Set the selected date on the TextView
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
        // Clear all EditTexts
        editTextLastName.setText("");
        editTextFirstName.setText("");
        editTextMiddleName.setText("");
        editTextLastSchool.setText("");

        // Reset the birthdate TextView
        textViewBirthdate.setText("");

        // Reset the Spinner to the first option
        spinnerStrand.setSelection(0);

        // Optional: Show a message
        Toast.makeText(this, "All entries cleared", Toast.LENGTH_SHORT).show();
    }

}