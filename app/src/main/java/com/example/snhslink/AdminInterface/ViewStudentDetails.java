package com.example.snhslink.AdminInterface;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.snhslink.BaseActivity;
import com.example.snhslink.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ViewStudentDetails extends BaseActivity {

    private TextView fullNameTextView, emailTextView, mobileTextView, strandTextView, applicationStatusTextView,
            birthdateTextView, schoolYearTextView, gradeLevelTextView, ageTextView, genderTextView, lrnNumberTextView,
            placeOfBirthTextView, religionTextView, ipCommunityTextView, ipDetailsTextView, fourPsTextView, fourPsNumberTextView,
            learnerWithDisabilityTextView, disabilityDetailsTextView, specialNeedsTextView, specialNeedsDetailsTextView, pwdIdTextView,
            pwdIdDetailsTextView, currentAddressTextView, currentZipTextView, permanentAddressTextView, permanentZipTextView,
            fatherNameTextView, fatherContactTextView, motherNameTextView, motherContactTextView, guardianNameTextView, guardianRelationshipTextView, guardianContactTextView,
            lastGradeCompletedTextView, lastSchoolYearCompletedTextView, lastSchoolAttendedTextView, schoolIDTextView, gwaTextView;
    private ImageView birthCertificateImageView, reportCardFrontImageView, reportCardBackImageView, idPictureImageView;
    private Button approveButton, rejectButton, backButton;
    private FirebaseFirestore db;


    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_student_details);

        db = FirebaseFirestore.getInstance();

        gwaTextView = findViewById(R.id.textview_gwa);


        //basic_information
        fullNameTextView = findViewById(R.id.textview_full_name);
        emailTextView = findViewById(R.id.textview_email);
        mobileTextView = findViewById(R.id.textview_mobile);
        strandTextView = findViewById(R.id.textview_strand);
        applicationStatusTextView = findViewById(R.id.textview_application_status);

        //personal_information
        birthdateTextView = findViewById(R.id.textview_birthdate);

        schoolYearTextView = findViewById(R.id.textview_school_year);
                gradeLevelTextView = findViewById(R.id.textview_grade_level);
        ageTextView = findViewById(R.id.textview_age);
                genderTextView = findViewById(R.id.textview_gender);
        lrnNumberTextView = findViewById(R.id.textview_lrn);
                placeOfBirthTextView = findViewById(R.id.textview_birth_place);
        religionTextView = findViewById(R.id.textview_religion);

                ipCommunityTextView = findViewById(R.id.textview_ip_community);
        ipDetailsTextView = findViewById(R.id.textview_ip_details);
                fourPsTextView = findViewById(R.id.textview_fourps);
        fourPsNumberTextView = findViewById(R.id.textview_fourps_number);
                learnerWithDisabilityTextView = findViewById(R.id.textview_learner_with_disability);
        disabilityDetailsTextView = findViewById(R.id.textview_disability_details);
                specialNeedsTextView = findViewById(R.id.textview_special_needs);
        specialNeedsDetailsTextView = findViewById(R.id.textview_special_needs_details);
                pwdIdTextView = findViewById(R.id.textview_pwd_id);
        pwdIdDetailsTextView = findViewById(R.id.textview_pwd_id_number);

        //address

                currentAddressTextView = findViewById(R.id.textview_current_address);
        currentZipTextView = findViewById(R.id.textview_current_zip);
                permanentAddressTextView = findViewById(R.id.textview_permanent_address);
        permanentZipTextView = findViewById(R.id.textview_permanent_zip);

        //family_information

                fatherNameTextView = findViewById(R.id.textview_father_name);
        fatherContactTextView = findViewById(R.id.textview_father_contact);
                motherNameTextView = findViewById(R.id.textview_mother_name);
        motherContactTextView = findViewById(R.id.textview_mother_contact);
                guardianNameTextView = findViewById(R.id.textview_guardian_name);
        guardianRelationshipTextView = findViewById(R.id.textview_guardian_relationship);
                guardianContactTextView = findViewById(R.id.textview_guardian_contact);

                //transferee/returning_learner

        lastGradeCompletedTextView = findViewById(R.id.textview_last_grade_completed);
                lastSchoolYearCompletedTextView = findViewById(R.id.textview_last_school_year_completed);
        lastSchoolAttendedTextView = findViewById(R.id.textview_last_school_attended);
                schoolIDTextView = findViewById(R.id.textview_school_id);







        //uploaded_documents
        birthCertificateImageView = findViewById(R.id.imageview_birth_certificate);
        reportCardFrontImageView = findViewById(R.id.imageview_report_card_front);
        reportCardBackImageView = findViewById(R.id.imageview_report_card_back);
        idPictureImageView = findViewById(R.id.imageview_id_picture);

        approveButton = findViewById(R.id.button_approve);
        rejectButton = findViewById(R.id.button_reject);
        backButton = findViewById(R.id.button_back);

        // Get userID from Intent
        userID = getIntent().getStringExtra("userID");
        if (userID == null || userID.isEmpty()) {
            Log.e("ViewStudentDetails", "Error: Received userID is NULL");
            Toast.makeText(this, "Error: User ID not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Back press handler
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(ViewStudentDetails.this, AdminDashboard.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clears previous activities
                startActivity(intent);

                finish(); // This should properly close the activity
            }
        });




        loadStudentBasicInformation();
        loadstudentPersonalInformation();
        loadStudentDocuments();
        loadApplicationStatus();

        approveButton.setOnClickListener(v -> updateApplicationStatus("Approved"));
        rejectButton.setOnClickListener(v -> updateApplicationStatus("Rejected"));

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ViewStudentDetails.this, AdminDashboard.class);
            startActivity(intent);
            finish(); // Finish current activity so it doesn’t stay in memory
        });
    }


    private void loadstudentPersonalInformation() {
        db.collection("students").document(userID)
                .collection("personal_information").document("data")
                .get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {


                        String GWA = documentSnapshot.getString("GWA");

                        //personal_information
                        String schoolYear = documentSnapshot.getString("schoolYear");
                        String gradeLevel = documentSnapshot.getString("gradeLevel");
                        // --birthday--
                        String age = documentSnapshot.getString("age");
                        String gender = documentSnapshot.getString("gender");
                        String lrnNumber = documentSnapshot.getString("lrnNumber");
                        String placeOfBirth = documentSnapshot.getString("placeOfBirth");
                        String religion = documentSnapshot.getString("religion");

                        //Y/N info
                        String ipCommunity = documentSnapshot.getString("ipCommunity");
                        String ipDetails = documentSnapshot.getString("ipDetails");
                        String fourPs = documentSnapshot.getString("fourPs");
                        String fourPsNumber = documentSnapshot.getString("fourPsNumber");
                        String learnerWithDisability = documentSnapshot.getString("learnerWithDisability");
                        String disabilityDetails = documentSnapshot.getString("disabilityDetails");
                        String specialNeeds = documentSnapshot.getString("specialNeeds");
                        String specialNeedsDetails = documentSnapshot.getString("specialNeedsDetails");
                        String pwdId = documentSnapshot.getString("pwdId");
                        String pwdIdDetails = documentSnapshot.getString("pwdIdDetails");

                        //address
                        String currentHouse = documentSnapshot.getString("currentHouse");
                        String currentStreet = documentSnapshot.getString("currentStreet");
                        String currentBarangay = documentSnapshot.getString("currentBarangay");
                        String currentCity = documentSnapshot.getString("currentCity");
                        String currentProvince = documentSnapshot.getString("currentProvince");
                        String currentZip = documentSnapshot.getString("currentZip");

                        String permanentHouse = documentSnapshot.getString("permanentHouse");
                        String permanentStreet = documentSnapshot.getString("permanentStreet");
                        String permanentBarangay = documentSnapshot.getString("permanentBarangay");
                        String permanentCity = documentSnapshot.getString("permanentCity");
                        String permanentProvince = documentSnapshot.getString("permanentProvince");
                        String permanentZip = documentSnapshot.getString("permanentZip");

                        //family_information
                        String fatherName = documentSnapshot.getString("fatherName");
                        String fatherContact = documentSnapshot.getString("fatherContact");
                        String motherName = documentSnapshot.getString("motherName");
                        String motherContact = documentSnapshot.getString("motherContact");
                        String guardianName = documentSnapshot.getString("guardianName");
                        String guardianRelationship = documentSnapshot.getString("guardianRelationship");
                        String guardianContact = documentSnapshot.getString("guardianContact");

                        //transferee/returning_learner
                        String lastGradeCompleted = documentSnapshot.getString("lastGradeCompleted");
                        String lastSchoolYearCompleted = documentSnapshot.getString("lastSchoolYearCompleted");
                        String lastSchoolAttended = documentSnapshot.getString("lastSchoolAttended");
                        String schoolID = documentSnapshot.getString("schoolID");


                        gwaTextView.setText(GWA != null ? GWA : "N/A");


                        schoolYearTextView.setText(schoolYear != null ? schoolYear : "N/A");
                        gradeLevelTextView.setText(gradeLevel != null ? gradeLevel : "N/A");
                        ageTextView.setText(age != null ? age : "N/A");
                        genderTextView.setText(gender != null ? gender : "N/A");
                        lrnNumberTextView.setText(lrnNumber != null ? lrnNumber : "N/A");
                        placeOfBirthTextView.setText(placeOfBirth != null ? placeOfBirth : "N/A");
                        religionTextView.setText(religion != null ? religion : "N/A");

                        ipCommunityTextView.setText(ipCommunity != null ? ipCommunity : "N/A");
                        ipDetailsTextView.setText(ipDetails != null ? ipDetails : "N/A");
                        fourPsTextView.setText(fourPs != null ? fourPs : "N/A");
                        fourPsNumberTextView.setText(fourPsNumber != null ? fourPsNumber : "N/A");
                        learnerWithDisabilityTextView.setText(learnerWithDisability != null ? learnerWithDisability : "N/A");
                        disabilityDetailsTextView.setText(disabilityDetails != null ? disabilityDetails : "N/A");
                        specialNeedsTextView.setText(specialNeeds != null ? specialNeeds : "N/A");
                        specialNeedsDetailsTextView.setText(specialNeedsDetails != null ? specialNeedsDetails : "N/A");
                        pwdIdTextView.setText(pwdId != null ? pwdId : "N/A");
                        pwdIdDetailsTextView.setText(pwdIdDetails != null ? pwdIdDetails : "N/A");

                        currentAddressTextView.setText((currentHouse != null ? currentHouse : "") + ", " +
                                (currentStreet != null ? currentStreet : "") + ", " +
                                (currentBarangay != null ? currentBarangay : "") + ", " +
                                (currentCity != null ? currentCity : "") + ", " +
                                (currentProvince != null ? currentProvince : ""));
                        currentZipTextView.setText(currentZip != null ? currentZip : "N/A");


                        permanentAddressTextView.setText((permanentHouse != null ? permanentHouse : "") + ", " +
                                (permanentStreet != null ? permanentStreet : "") + ", " +
                                (permanentBarangay != null ? permanentBarangay : "") + ", " +
                                (permanentCity != null ? permanentCity : "") + ", " +
                                (permanentProvince != null ? permanentProvince : ""));
                        permanentZipTextView.setText(permanentZip != null ? permanentZip : "N/A");

                        fatherNameTextView.setText(fatherName != null ? fatherName : "N/A");
                        fatherContactTextView.setText(fatherContact != null ? fatherContact : "N/A");
                        motherNameTextView.setText(motherName != null ? motherName : "N/A");
                        motherContactTextView.setText(motherContact != null ? motherContact : "N/A");
                        guardianNameTextView.setText(guardianName != null ? guardianName : "N/A");
                        guardianRelationshipTextView.setText(guardianRelationship != null ? guardianRelationship : "N/A");
                        guardianContactTextView.setText(guardianContact != null ? guardianContact : "N/A");

                        lastGradeCompletedTextView.setText(lastGradeCompleted != null ? lastGradeCompleted : "N/A");
                        lastSchoolYearCompletedTextView.setText(lastSchoolYearCompleted != null ? lastSchoolYearCompleted : "N/A");
                        lastSchoolAttendedTextView.setText(lastSchoolAttended != null ? lastSchoolAttended : "N/A");
                        schoolIDTextView.setText(schoolID != null ? schoolID : "N/A");

                    } else {
                        Toast.makeText(ViewStudentDetails.this, "Student details not found.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadStudentBasicInformation() {
        db.collection("students").document(userID)
                .collection("basic_information").document("data")
                .get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String lastName = documentSnapshot.getString("lastName");
                        String firstName = documentSnapshot.getString("firstName");
                        String middleName = documentSnapshot.getString("middleName");
                        String email = documentSnapshot.getString("email");
                        String mobileNumber = documentSnapshot.getString("mobileNumber");
                        String strand = documentSnapshot.getString("strand");
                        String birthdate = documentSnapshot.getString("birthdate");

                        birthdateTextView.setText(birthdate != null ? birthdate : "N/A");

                        fullNameTextView.setText((lastName != null ? lastName : "") + ", " + (firstName != null ? firstName : "") + " " + (middleName != null ? middleName : ""));
                        emailTextView.setText(email != null ? email : "N/A");
                        mobileTextView.setText(mobileNumber != null ? mobileNumber : "N/A");
                        strandTextView.setText(strand != null ? strand : "N/A");
                    } else {
                        Toast.makeText(ViewStudentDetails.this, "Student details not found.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadStudentDocuments() {
        loadDocument("Birth Certificate", birthCertificateImageView);
        loadDocument("Grade 10 Report Card (front)", reportCardFrontImageView);
        loadDocument("Grade 10 Report Card (back)", reportCardBackImageView);
        loadDocument("I.D. Picture", idPictureImageView);
    }


    private void loadDocument(String docID, ImageView imageView) {
        db.collection("students").document(userID)
                .collection("documents").document(docID)
                .get().addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        String imageUrl = doc.getString("imageUrl");
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            Glide.with(this).load(imageUrl).into(imageView);
                        } else {
                            Toast.makeText(this, "No imageUrl in " + docID, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Document not found: students/" + userID + "/documents/" + docID, Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }



    private void loadApplicationStatus() {
        db.collection("students").document(userID)
                .collection("application_status").document("status")
                .get().addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        String status = doc.getString("applicationStatus");
                        applicationStatusTextView.setText(status != null ? status : "Pending");
                    } else {
                        applicationStatusTextView.setText("Pending");
                    }
                });
    }

    private void updateApplicationStatus(String status) {
        // Update the existing Firestore path
        db.collection("students").document(userID)
                .collection("application_status").document("status")
                .set(new ApplicationStatus(status))
                .addOnSuccessListener(unused -> {
                    // ALSO update status inside the main students document
                    db.collection("students").document(userID)
                            .update("application_status", status)
                            .addOnSuccessListener(unused2 -> {
                                applicationStatusTextView.setText(status);
                                Toast.makeText(ViewStudentDetails.this, "Application " + status, Toast.LENGTH_SHORT).show();

                                // Redirect back to AdminDashboard
                                Intent intent = new Intent(ViewStudentDetails.this, AdminDashboard.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(ViewStudentDetails.this, "Failed to copy status: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(ViewStudentDetails.this, "Failed to update status: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }





    // Helper class for application status
    public static class ApplicationStatus {
        private String applicationStatus;

        public ApplicationStatus() {} // Empty constructor required for Firestore

        public ApplicationStatus(String applicationStatus) {
            this.applicationStatus = applicationStatus;
        }

        public String getApplicationStatus() {
            return applicationStatus;
        }
    }



}
