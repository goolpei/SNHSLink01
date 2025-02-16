package StudentInterface;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.snhslink.R;

public class StudentPersonalInformation extends AppCompatActivity {

    // Student personal info fields
    private EditText etLastName, etFirstName, etMiddleName, etBirthDate;

    // YES/NO Checkbox groups and associated detail fields
    private CheckBox cbIPCommunityYes, cbIPCommunityNo, cb4psYes, cb4psNo;
    private CheckBox cbDisabilityYes, cbDisabilityNo, cbSpecialNeedsYes, cbSpecialNeedsNo;
    private CheckBox cbPwdIdYes, cbPwdIdNo;
    private EditText etIpDetails, et4psNumber, etDisabilityDetails, etSpecialNeedsDetails, etPwdIdDetails;
    private LinearLayout groupSpecialNeedsPwd;

    // Semester selection checkboxes
    private CheckBox cb1stSemester, cb2ndSemester;

    // "Same Address" checkbox and address fields
    private CheckBox cbSameAddress;
    private EditText etCurrentHouse, etCurrentStreet, etCurrentBarangay, etCurrentCity, etCurrentProvince, etCurrentZip;
    private EditText etPermanentHouse, etPermanentStreet, etPermanentBarangay, etPermanentCity, etPermanentProvince, etPermanentZip;

    // Navigation elements
    private TextView textViewProfileVerificationLink;
    private Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_personal_information);

        // Initialize all views
        initializeViews();

        // Retrieve passed data from the previous activity
        String lastName = getIntent().getStringExtra("LAST_NAME");
        String firstName = getIntent().getStringExtra("FIRST_NAME");
        String middleName = getIntent().getStringExtra("MIDDLE_NAME");
        String birthdate = getIntent().getStringExtra("BIRTHDATE");

        // Set the retrieved data into the student info fields
        etLastName.setText(lastName);
        etFirstName.setText(firstName);
        etMiddleName.setText(middleName);
        etBirthDate.setText(birthdate);

        // Set up listeners
        setClickListeners();
        setupCheckboxListeners();
        setupSemesterCheckboxListeners();
    }

    private void initializeViews() {
        // Student personal info fields
        etLastName = findViewById(R.id.et_student_last_name);
        etFirstName = findViewById(R.id.et_student_first_name);
        etMiddleName = findViewById(R.id.et_student_middle_name);
        etBirthDate = findViewById(R.id.et_birth_date);

        // YES/NO Checkbox groups
        cbIPCommunityYes = findViewById(R.id.cb_ip_YES);
        cbIPCommunityNo = findViewById(R.id.cb_ip_NO);
        etIpDetails = findViewById(R.id.et_ip_details);

        cb4psYes = findViewById(R.id.cb_4ps_YES);
        cb4psNo = findViewById(R.id.cb_4ps_NO);
        et4psNumber = findViewById(R.id.et_4ps_number);

        cbDisabilityYes = findViewById(R.id.cb_disability_yes);
        cbDisabilityNo = findViewById(R.id.cb_disability_no);
        etDisabilityDetails = findViewById(R.id.et_disability_details);

        cbSpecialNeedsYes = findViewById(R.id.cb_special_needs_yes);
        cbSpecialNeedsNo = findViewById(R.id.cb_special_needs_no);
        etSpecialNeedsDetails = findViewById(R.id.et_special_needs_details);

        cbPwdIdYes = findViewById(R.id.cb_pwd_id_yes);
        cbPwdIdNo = findViewById(R.id.cb_pwd_id_no);
        etPwdIdDetails = findViewById(R.id.et_pwd_id_details);

        // Semester checkboxes
        cb1stSemester = findViewById(R.id.cb_1st_semester);
        cb2ndSemester = findViewById(R.id.cb_2nd_semester);

        // Same address and address fields
        cbSameAddress = findViewById(R.id.cb_same_address);

        etCurrentHouse = findViewById(R.id.et_current_house_number);
        etCurrentStreet = findViewById(R.id.et_current_street_name);
        etCurrentBarangay = findViewById(R.id.et_current_barangay);
        etCurrentCity = findViewById(R.id.et_current_city);
        etCurrentProvince = findViewById(R.id.et_current_province);
        etCurrentZip = findViewById(R.id.et_current_zip_code);

        etPermanentHouse = findViewById(R.id.et_permanent_house_number);
        etPermanentStreet = findViewById(R.id.et_permanent_street_name);
        etPermanentBarangay = findViewById(R.id.et_permanent_barangay);
        etPermanentCity = findViewById(R.id.et_permanent_city);
        etPermanentProvince = findViewById(R.id.et_permanent_province);
        etPermanentZip = findViewById(R.id.et_permanent_zip_code);

        // Optional layout group for special needs/PWD extra details
        groupSpecialNeedsPwd = findViewById(R.id.group_special_needs_pwd);

        // Navigation elements
        textViewProfileVerificationLink = findViewById(R.id.textview_back_to_profile_verification);
        buttonSubmit = findViewById(R.id.btn_admission_form_continue);

        // Set listener for the "Same Address" checkbox
        cbSameAddress.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                copyCurrentToPermanent();
                setPermanentFieldsEnabled(false);
            } else {
                setPermanentFieldsEnabled(true);
            }
        });
    }

    /**
     * Copies current address fields to permanent address fields.
     */
    private void copyCurrentToPermanent() {
        etPermanentHouse.setText(etCurrentHouse.getText().toString());
        etPermanentStreet.setText(etCurrentStreet.getText().toString());
        etPermanentBarangay.setText(etCurrentBarangay.getText().toString());
        etPermanentCity.setText(etCurrentCity.getText().toString());
        etPermanentProvince.setText(etCurrentProvince.getText().toString());
        etPermanentZip.setText(etCurrentZip.getText().toString());
    }

    /**
     * Enables or disables the permanent address fields.
     */
    private void setPermanentFieldsEnabled(boolean enabled) {
        etPermanentHouse.setEnabled(enabled);
        etPermanentStreet.setEnabled(enabled);
        etPermanentBarangay.setEnabled(enabled);
        etPermanentCity.setEnabled(enabled);
        etPermanentProvince.setEnabled(enabled);
        etPermanentZip.setEnabled(enabled);
    }

    private void setClickListeners() {
        textViewProfileVerificationLink.setOnClickListener(view -> navigateToProfileVerificationPage());
        buttonSubmit.setOnClickListener(view -> navigateToUploadStudentDocumentsPage());
    }

    private void setupCheckboxListeners() {
        setupCheckboxListener(cbIPCommunityYes, cbIPCommunityNo, etIpDetails);
        setupCheckboxListener(cb4psYes, cb4psNo, et4psNumber);
        setupCheckboxListener(cbDisabilityYes, cbDisabilityNo, etDisabilityDetails, groupSpecialNeedsPwd);
        setupCheckboxListener(cbSpecialNeedsYes, cbSpecialNeedsNo, etSpecialNeedsDetails);
        setupCheckboxListener(cbPwdIdYes, cbPwdIdNo, etPwdIdDetails);
    }

    /**
     * Sets up a YES/NO checkbox pair with an optional associated EditText and layout.
     */
    private void setupCheckboxListener(CheckBox yesCheckbox, CheckBox noCheckbox, EditText editText, @Nullable LinearLayout layout) {
        yesCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                noCheckbox.setChecked(false);
                if (editText != null) editText.setVisibility(View.VISIBLE);
                if (layout != null) layout.setVisibility(View.VISIBLE);
            }
        });
        noCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                yesCheckbox.setChecked(false);
                if (editText != null) editText.setVisibility(View.GONE);
                if (layout != null) layout.setVisibility(View.GONE);
            }
        });
    }

    private void setupCheckboxListener(CheckBox yesCheckbox, CheckBox noCheckbox, EditText editText) {
        setupCheckboxListener(yesCheckbox, noCheckbox, editText, null);
    }

    private void setupSemesterCheckboxListeners() {
        cb1stSemester.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) cb2ndSemester.setChecked(false);
        });
        cb2ndSemester.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) cb1stSemester.setChecked(false);
        });
    }

    private void navigateToUploadStudentDocumentsPage() {
        Intent intent = new Intent(this, UploadStudentDocuments.class);
        startActivity(intent);
    }



    private void navigateToProfileVerificationPage() {
        Intent intent = new Intent(this, ProfileVerification.class);
        startActivity(intent);
    }
}
