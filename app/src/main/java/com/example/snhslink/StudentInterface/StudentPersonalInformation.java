package com.example.snhslink.StudentInterface;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.snhslink.BaseActivity;
import com.example.snhslink.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class StudentPersonalInformation extends BaseActivity {

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userID; // Firebase UID

    // UI Components
    private TextView studentIdTextView;
    private EditText etLastName, etFirstName, etMiddleName, etBirthDate, etAge;
    private EditText etSchoolYear, etLrnNumber, etExtensionName, etPlaceOfBirth, etReligion;
    private Spinner spGradeLevel;
    private RadioButton rbMale, rbFemale;

    // Checkboxes & EditTexts for Special Conditions
    private CheckBox cbIpYes, cbIpNo, cb4psYes, cb4psNo, cbDisabilityYes, cbDisabilityNo, cbSpecialNeedsYes, cbSpecialNeedsNo, cbPwdIdYes, cbPwdIdNo;
    private EditText etIpDetails, et4psNumber, etDisabilityDetails, etSpecialNeedsDetails, etPwdIdDetails;
    private LinearLayout groupSpecialNeedsPwd;

    // Address Fields
    private CheckBox cbSameAddress;
    private EditText etCurrentHouse, etCurrentStreet, etCurrentBarangay, etCurrentCity, etCurrentProvince, etCurrentZip;
    private EditText etPermanentHouse, etPermanentStreet, etPermanentBarangay, etPermanentCity, etPermanentProvince, etPermanentZip;

    // Family & Guardian
    private EditText etFatherName, etFatherContact, etMotherName, etMotherContact, etGuardianName, etGuardianRelationship, etGuardianContact;

    // Returning Learner
    private EditText etLastGradeCompleted, etLastSchoolYearCompleted, etLastSchoolAttended, etSchoolID;
    private EditText etGWA;
    private Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_personal_information);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        etGWA = findViewById(R.id.et_gwa);

        // Initialize UI Components
        studentIdTextView = findViewById(R.id.textview_student_id);
        etLastName = findViewById(R.id.et_student_last_name);
        etFirstName = findViewById(R.id.et_student_first_name);
        etMiddleName = findViewById(R.id.et_student_middle_name);
        etBirthDate = findViewById(R.id.et_birth_date);
        etAge = findViewById(R.id.et_age);

        etSchoolYear = findViewById(R.id.et_school_year);
        etLrnNumber = findViewById(R.id.et_lrn_number);
        etExtensionName = findViewById(R.id.et_extension_name);
        etPlaceOfBirth = findViewById(R.id.et_place_of_birth);
        etReligion = findViewById(R.id.et_religion);
        spGradeLevel = findViewById(R.id.sp_grade_level);
        rbMale = findViewById(R.id.rb_male);
        rbFemale = findViewById(R.id.rb_female);

        cbSameAddress = findViewById(R.id.cb_same_address);
        cbIpYes = findViewById(R.id.cb_ip_YES);
        cbIpNo = findViewById(R.id.cb_ip_NO);
        cb4psYes = findViewById(R.id.cb_4ps_YES);
        cb4psNo = findViewById(R.id.cb_4ps_NO);
        cbDisabilityYes = findViewById(R.id.cb_disability_yes);
        cbDisabilityNo = findViewById(R.id.cb_disability_no);
        cbSpecialNeedsYes = findViewById(R.id.cb_special_needs_yes);
        cbSpecialNeedsNo = findViewById(R.id.cb_special_needs_no);
        cbPwdIdYes = findViewById(R.id.cb_pwd_id_yes);
        cbPwdIdNo = findViewById(R.id.cb_pwd_id_no);

        etIpDetails = findViewById(R.id.et_ip_details);
        et4psNumber = findViewById(R.id.et_4ps_number);
        etDisabilityDetails = findViewById(R.id.et_disability_details);
        etSpecialNeedsDetails = findViewById(R.id.et_special_needs_details);
        etPwdIdDetails = findViewById(R.id.et_pwd_id_details);

        groupSpecialNeedsPwd = findViewById(R.id.group_special_needs_pwd);

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

        etFatherName = findViewById(R.id.et_father_s_name);
        etFatherContact = findViewById(R.id.et_father_contact);
        etMotherName = findViewById(R.id.et_mother_s_maiden_name);
        etMotherContact = findViewById(R.id.et_mother_contact);
        etGuardianName = findViewById(R.id.et_guardian_name);
        etGuardianRelationship = findViewById(R.id.et_guardian_relationship);
        etGuardianContact = findViewById(R.id.et_guardian_contact);

        etLastGradeCompleted = findViewById(R.id.et_last_grade_level_completed);
        etLastSchoolYearCompleted = findViewById(R.id.et_last_school_year_completed);
        etLastSchoolAttended = findViewById(R.id.et_last_school_attended);
        etSchoolID = findViewById(R.id.et_school_id);

        // Setup listeners
        setupSameAddressCheckbox();
        fetchStudentData();
        setupCheckboxListeners();

        // Save button
        buttonSubmit = findViewById(R.id.btn_admission_form_continue);


        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show confirmation dialog
                new AlertDialog.Builder(StudentPersonalInformation.this) // Replace 'YourActivity' with your actual activity name
                        .setTitle("Confirm Information")
                        .setMessage("Please review your information carefully. Once you continue, you won’t be able to make any changes.")
                        .setPositiveButton("Yes, Continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //save student data
                                saveStudentPersonalInformation();

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Dismiss dialog, user can edit info
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });
    }



    private void fetchStudentData() {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user == null) {
            Toast.makeText(this, "Session expired. Please log in again.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(StudentPersonalInformation.this, StudentLogin.class));
            finish();
            return;
        }

        userID = user.getUid();

        // First, fetch the Student ID from the correct document
        db.collection("students").document(userID).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists()) {
                        String studentId = task.getResult().getString("studentID");
                        if (studentId != null) {
                            studentIdTextView.setText("Your Applicant ID: " + studentId);
                        } else {
                            studentIdTextView.setText("Your Applicant ID: Not Found");
                        }
                    } else {
                        Log.e("FirestoreError", "Failed to fetch Student ID", task.getException());
                        studentIdTextView.setText("Your Applicant ID: Not Found");
                    }
                });

        // Second, fetch personal details from "basic_information/data"
        db.collection("students").document(userID)
                .collection("basic_information").document("data")  // Access the correct nested document
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            etLastName.setText(document.getString("lastName"));
                            etFirstName.setText(document.getString("firstName"));
                            etMiddleName.setText(document.getString("middleName"));
                            etBirthDate.setText(document.getString("birthdate"));

                            calculateAndDisplayAge(document.getString("birthdate"));
                        } else {
                            Log.e("FirestoreError", "Document does not exist at path: students/" + userID + "/basic_information/data");
                            Toast.makeText(this, "No personal information found.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("FirestoreError", "Error fetching document: ", task.getException());
                        Toast.makeText(this, "Failed to retrieve student data. Check Logcat for details.", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void calculateAndDisplayAge(String birthdate) {
        if (birthdate == null || birthdate.isEmpty()) {
            etAge.setText("N/A");
            return;
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            Date birthDateObj = dateFormat.parse(birthdate);

            Calendar today = Calendar.getInstance();
            Calendar birthDate = Calendar.getInstance();
            birthDate.setTime(birthDateObj);

            int age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

            if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }

            etAge.setText(String.valueOf(age));

        } catch (ParseException e) {
            etAge.setText("N/A");
        }
    }

    private void setupSameAddressCheckbox() {
        cbSameAddress.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                copyCurrentToPermanent();
                setPermanentFieldsEnabled(false);
            } else {
                setPermanentFieldsEnabled(true);
            }
        });
    }

    private void copyCurrentToPermanent() {
        etPermanentHouse.setText(etCurrentHouse.getText().toString());
        etPermanentStreet.setText(etCurrentStreet.getText().toString());
        etPermanentBarangay.setText(etCurrentBarangay.getText().toString());
        etPermanentCity.setText(etCurrentCity.getText().toString());
        etPermanentProvince.setText(etCurrentProvince.getText().toString());
        etPermanentZip.setText(etCurrentZip.getText().toString());
    }

    private void setPermanentFieldsEnabled(boolean enabled) {
        etPermanentHouse.setEnabled(enabled);
        etPermanentStreet.setEnabled(enabled);
        etPermanentBarangay.setEnabled(enabled);
        etPermanentCity.setEnabled(enabled);
        etPermanentProvince.setEnabled(enabled);
        etPermanentZip.setEnabled(enabled);
    }

    private void setupCheckboxListeners() {
        setupCheckboxListener(cbIpYes, cbIpNo, etIpDetails);
        setupCheckboxListener(cb4psYes, cb4psNo, et4psNumber);
        setupCheckboxListener(cbDisabilityYes, cbDisabilityNo, etDisabilityDetails, groupSpecialNeedsPwd);
        setupCheckboxListener(cbSpecialNeedsYes, cbSpecialNeedsNo, etSpecialNeedsDetails);
        setupCheckboxListener(cbPwdIdYes, cbPwdIdNo, etPwdIdDetails);
    }

    /**
     * Handles enabling/disabling EditText based on Yes/No checkbox selection.
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




    private void saveStudentPersonalInformation() {


        String schoolYear = getInputOrNA(etSchoolYear);
        String gradeLevel = spGradeLevel.getSelectedItem() != null ? spGradeLevel.getSelectedItem().toString() : "N/A";
        String lrnNumber = getInputOrNA(etLrnNumber);

        String GWA = getInputOrNA(etGWA);

        String extensionName = getInputOrNA(etExtensionName);
        String age = etAge.getText().toString().trim().isEmpty() ? "N/A" : etAge.getText().toString();
        String gender = rbMale.isChecked() ? "Male" : (rbFemale.isChecked() ? "Female" : "N/A");
        String placeOfBirth = getInputOrNA(etPlaceOfBirth);
        String religion = getInputOrNA(etReligion);

        // Special conditions (YES/NO fields)
        String ipCommunity = cbIpYes.isChecked() ? "YES" : (cbIpNo.isChecked() ? "NO" : "N/A");
        String ipDetails = cbIpYes.isChecked() ? getInputOrNA(etIpDetails) : "N/A";

        String fourPs = cb4psYes.isChecked() ? "YES" : (cb4psNo.isChecked() ? "NO" : "N/A");
        String fourPsNumber = cb4psYes.isChecked() ? getInputOrNA(et4psNumber) : "N/A";

        String disability = cbDisabilityYes.isChecked() ? "YES" : (cbDisabilityNo.isChecked() ? "NO" : "N/A");
        String disabilityDetails = cbDisabilityYes.isChecked() ? getInputOrNA(etDisabilityDetails) : "N/A";

        String specialNeeds = cbSpecialNeedsYes.isChecked() ? "YES" : (cbSpecialNeedsNo.isChecked() ? "NO" : "N/A");
        String specialNeedsDetails = cbSpecialNeedsYes.isChecked() ? getInputOrNA(etSpecialNeedsDetails) : "N/A";

        String pwdId = cbPwdIdYes.isChecked() ? "YES" : (cbPwdIdNo.isChecked() ? "NO" : "N/A");
        String pwdIdDetails = cbPwdIdYes.isChecked() ? getInputOrNA(etPwdIdDetails) : "N/A";

        // Address
        String currentHouse = getInputOrNA(etCurrentHouse);
        String currentStreet = getInputOrNA(etCurrentStreet);
        String currentBarangay = getInputOrNA(etCurrentBarangay);
        String currentCity = getInputOrNA(etCurrentCity);
        String currentProvince = getInputOrNA(etCurrentProvince);
        String currentZip = getInputOrNA(etCurrentZip);

        String permanentHouse = cbSameAddress.isChecked() ? currentHouse : getInputOrNA(etPermanentHouse);
        String permanentStreet = cbSameAddress.isChecked() ? currentStreet : getInputOrNA(etPermanentStreet);
        String permanentBarangay = cbSameAddress.isChecked() ? currentBarangay : getInputOrNA(etPermanentBarangay);
        String permanentCity = cbSameAddress.isChecked() ? currentCity : getInputOrNA(etPermanentCity);
        String permanentProvince = cbSameAddress.isChecked() ? currentProvince : getInputOrNA(etPermanentProvince);
        String permanentZip = cbSameAddress.isChecked() ? currentZip : getInputOrNA(etPermanentZip);

        // Family
        String fatherName = getInputOrNA(etFatherName);
        String fatherContact = getInputOrNA(etFatherContact);
        String motherName = getInputOrNA(etMotherName);
        String motherContact = getInputOrNA(etMotherContact);
        String guardianName = getInputOrNA(etGuardianName);
        String guardianRelationship = getInputOrNA(etGuardianRelationship);
        String guardianContact = getInputOrNA(etGuardianContact);

        // Returning Learner
        String lastGradeCompleted = getInputOrNA(etLastGradeCompleted);
        String lastSchoolYearCompleted = getInputOrNA(etLastSchoolYearCompleted);
        String lastSchoolAttended = getInputOrNA(etLastSchoolAttended);
        String schoolID = getInputOrNA(etSchoolID);

        // Store in Firestore
        Map<String, Object> studentPersonalData = new HashMap<>();
        studentPersonalData.put("schoolYear", schoolYear);
        studentPersonalData.put("gradeLevel", gradeLevel);
        studentPersonalData.put("lrnNumber", lrnNumber);

        studentPersonalData.put("GWA", GWA);

        studentPersonalData.put("extensionName", extensionName);
        studentPersonalData.put("age", age);
        studentPersonalData.put("gender", gender);
        studentPersonalData.put("placeOfBirth", placeOfBirth);
        studentPersonalData.put("religion", religion);

        studentPersonalData.put("ipCommunity", ipCommunity);
        studentPersonalData.put("ipDetails", ipDetails);

        studentPersonalData.put("fourPs", fourPs);
        studentPersonalData.put("fourPsNumber", fourPsNumber);

        studentPersonalData.put("learnerWithDisability", disability);
        studentPersonalData.put("disabilityDetails", disabilityDetails);

        studentPersonalData.put("specialNeeds", specialNeeds);
        studentPersonalData.put("specialNeedsDetails", specialNeedsDetails);

        studentPersonalData.put("pwdId", pwdId);
        studentPersonalData.put("pwdIdDetails", pwdIdDetails);

        studentPersonalData.put("currentHouse", currentHouse);
        studentPersonalData.put("currentStreet", currentStreet);
        studentPersonalData.put("currentBarangay", currentBarangay);
        studentPersonalData.put("currentCity", currentCity);
        studentPersonalData.put("currentProvince", currentProvince);
        studentPersonalData.put("currentZip", currentZip);

        studentPersonalData.put("permanentHouse", permanentHouse);
        studentPersonalData.put("permanentStreet", permanentStreet);
        studentPersonalData.put("permanentBarangay", permanentBarangay);
        studentPersonalData.put("permanentCity", permanentCity);
        studentPersonalData.put("permanentProvince", permanentProvince);
        studentPersonalData.put("permanentZip", permanentZip);

        studentPersonalData.put("fatherName", fatherName);
        studentPersonalData.put("fatherContact", fatherContact);
        studentPersonalData.put("motherName", motherName);
        studentPersonalData.put("motherContact", motherContact);
        studentPersonalData.put("guardianName", guardianName);
        studentPersonalData.put("guardianRelationship", guardianRelationship);
        studentPersonalData.put("guardianContact", guardianContact);

        studentPersonalData.put("lastGradeCompleted", lastGradeCompleted);
        studentPersonalData.put("lastSchoolYearCompleted", lastSchoolYearCompleted);
        studentPersonalData.put("lastSchoolAttended", lastSchoolAttended);
        studentPersonalData.put("schoolID", schoolID);

        // Save data to Firestore
        db.collection("students").document(userID).collection("personal_information").document("data")
                .set(studentPersonalData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(StudentPersonalInformation.this, "Data saved successfully!", Toast.LENGTH_SHORT).show();

                    // Navigate to Upload Student Documents
                    Intent intent = new Intent(StudentPersonalInformation.this, UploadStudentDocuments.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Failed to save data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private String getInputOrNA(EditText editText) {
        return editText.getText().toString().trim().isEmpty() ? "N/A" : editText.getText().toString().trim();
    }
}
