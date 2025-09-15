package com.example.snhslink.Models;

public class StudentApplication {
    private String userID;
    private String studentID;
    private String firstName;
    private String lastName;
    private String middleName;
    private String extensionName;
    private String birthdate;
    private String age;
    private String gender;
    private String placeOfBirth;
    private String religion;
    private String email;
    private String mobileNumber;
    private String strand;
    private String schoolYear;
    private String gradeLevel;
    private String lrnNumber;
    // IP Community
    private String ipCommunity;
    private String ipDetails;

    // Special Needs
    private boolean fourPs;
    private String fourPsNumber;
    private boolean learnerWithDisability;
    private String disabilityDetails;
    private boolean specialNeeds;
    private String specialNeedsDetails;
    private boolean pwdId;
    private String pwdIdDetails;
    // Current Address
    private String currentHouse;
    private String currentStreet;
    private String currentBarangay;
    private String currentCity;
    private String currentProvince;
    private String currentZip;

    // Permanent Address
    private String permanentHouse;
    private String permanentStreet;
    private String permanentBarangay;
    private String permanentCity;
    private String permanentProvince;
    private String permanentZip;
    // Parent/Guardian Information
    private String fatherName;
    private String fatherContact;
    private String motherName;
    private String motherContact;
    private String guardianName;
    private String guardianRelationship;
    private String guardianContact;
    // Previous School Information
    private String lastGradeCompleted;
    private String lastSchoolYearCompleted;
    private String lastSchoolAttended;
    private String schoolID;


    private String applicationStatus;

    // Empty constructor required for Firestore
    public StudentApplication() {}

    // Constructor with parameters


    public StudentApplication(String userID, String studentID, String firstName, String lastName,
                              String middleName, String extensionName, String birthdate, String age,
                              String gender, String placeOfBirth, String religion, String email,
                              String mobileNumber, String strand, String schoolYear, String gradeLevel,
                              String lrnNumber, String ipCommunity, String ipDetails, boolean fourPs,
                              String fourPsNumber, boolean learnerWithDisability, String disabilityDetails,
                              boolean specialNeeds, String specialNeedsDetails, boolean pwdId, String pwdIdDetails,
                              String currentHouse, String currentStreet, String currentBarangay, String currentCity,
                              String currentProvince, String currentZip, String permanentHouse, String permanentStreet,
                              String permanentBarangay, String permanentCity, String permanentProvince, String permanentZip,
                              String fatherName, String fatherContact, String motherName, String motherContact,
                              String guardianName, String guardianRelationship, String guardianContact,
                              String lastGradeCompleted, String lastSchoolYearCompleted, String lastSchoolAttended,
                              String schoolID, String applicationStatus) {

        this.userID = userID;
        this.studentID = studentID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleName = middleName;
        this.extensionName = extensionName;
        this.birthdate = birthdate;
        this.age = age;
        this.gender = gender;
        this.placeOfBirth = placeOfBirth;
        this.religion = religion;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.strand = strand;
        this.schoolYear = schoolYear;
        this.gradeLevel = gradeLevel;
        this.lrnNumber = lrnNumber;
        this.ipCommunity = ipCommunity;
        this.ipDetails = ipDetails;
        this.fourPs = fourPs;
        this.fourPsNumber = fourPsNumber;
        this.learnerWithDisability = learnerWithDisability;
        this.disabilityDetails = disabilityDetails;
        this.specialNeeds = specialNeeds;
        this.specialNeedsDetails = specialNeedsDetails;
        this.pwdId = pwdId;
        this.pwdIdDetails = pwdIdDetails;
        this.currentHouse = currentHouse;
        this.currentStreet = currentStreet;
        this.currentBarangay = currentBarangay;
        this.currentCity = currentCity;
        this.currentProvince = currentProvince;
        this.currentZip = currentZip;
        this.permanentHouse = permanentHouse;
        this.permanentStreet = permanentStreet;
        this.permanentBarangay = permanentBarangay;
        this.permanentCity = permanentCity;
        this.permanentProvince = permanentProvince;
        this.permanentZip = permanentZip;
        this.fatherName = fatherName;
        this.fatherContact = fatherContact;
        this.motherName = motherName;
        this.motherContact = motherContact;
        this.guardianName = guardianName;
        this.guardianRelationship = guardianRelationship;
        this.guardianContact = guardianContact;
        this.lastGradeCompleted = lastGradeCompleted;
        this.lastSchoolYearCompleted = lastSchoolYearCompleted;
        this.lastSchoolAttended = lastSchoolAttended;
        this.schoolID = schoolID;
        this.applicationStatus = applicationStatus;
    }

    @Override
    public String toString() {
        return "StudentApplication{" +
                "userID='" + userID + '\'' +
                ", studentID='" + studentID + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", applicationStatus='" + applicationStatus + '\'' +
                '}';
    }

    // Getters and Setters (Only showing a few, but all fields should have them)
    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }

    public String getStudentID() { return studentID; }
    public void setStudentID(String studentID) { this.studentID = studentID; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getExtensionName() { return extensionName; }
    public void setExtensionName(String extensionName) { this.extensionName = extensionName; }

    public String getBirthdate() { return birthdate; }
    public void setBirthdate(String birthdate) { this.birthdate = birthdate; }

    public String getAge() { return age; }
    public void setAge(String age) { this.age = age; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getPlaceOfBirth() { return placeOfBirth; }
    public void setPlaceOfBirth(String placeOfBirth) { this.placeOfBirth = placeOfBirth; }

    public String getReligion() { return religion; }
    public void setReligion(String religion) { this.religion = religion; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getStrand() { return strand; }
    public void setStrand(String strand) { this.strand = strand; }

    public String getSchoolYear() { return schoolYear; }
    public void setSchoolYear(String schoolYear) { this.schoolYear = schoolYear; }

    public String getGradeLevel() { return gradeLevel; }
    public void setGradeLevel(String gradeLevel) { this.gradeLevel = gradeLevel; }

    public String getLrnNumber() { return lrnNumber; }
    public void setLrnNumber(String lrnNumber) { this.lrnNumber = lrnNumber; }

    public String getIpCommunity() { return ipCommunity; }
    public void setIpCommunity(String ipCommunity) { this.ipCommunity = ipCommunity; }

    public String getIpDetails() { return ipDetails; }
    public void setIpDetails(String ipDetails) { this.ipDetails = ipDetails; }

    public boolean isFourPs() { return fourPs; }
    public void setFourPs(boolean fourPs) { this.fourPs = fourPs; }

    public String getFourPsNumber() { return fourPsNumber; }
    public void setFourPsNumber(String fourPsNumber) { this.fourPsNumber = fourPsNumber; }

    public boolean isLearnerWithDisability() { return learnerWithDisability; }
    public void setLearnerWithDisability(boolean learnerWithDisability) { this.learnerWithDisability = learnerWithDisability; }

    public String getDisabilityDetails() { return disabilityDetails; }
    public void setDisabilityDetails(String disabilityDetails) { this.disabilityDetails = disabilityDetails; }

    public boolean isSpecialNeeds() { return specialNeeds; }
    public void setSpecialNeeds(boolean specialNeeds) { this.specialNeeds = specialNeeds; }

    public String getSpecialNeedsDetails() { return specialNeedsDetails; }
    public void setSpecialNeedsDetails(String specialNeedsDetails) { this.specialNeedsDetails = specialNeedsDetails; }

    public boolean isPwdId() { return pwdId; }
    public void setPwdId(boolean pwdId) { this.pwdId = pwdId; }

    public String getPwdIdDetails() { return pwdIdDetails; }
    public void setPwdIdDetails(String pwdIdDetails) { this.pwdIdDetails = pwdIdDetails; }

    public String getCurrentHouse() { return currentHouse; }
    public void setCurrentHouse(String currentHouse) { this.currentHouse = currentHouse; }

    public String getCurrentStreet() { return currentStreet; }
    public void setCurrentStreet(String currentStreet) { this.currentStreet = currentStreet; }

    public String getCurrentBarangay() { return currentBarangay; }
    public void setCurrentBarangay(String currentBarangay) { this.currentBarangay = currentBarangay; }

    public String getCurrentCity() { return currentCity; }
    public void setCurrentCity(String currentCity) { this.currentCity = currentCity; }

    public String getCurrentProvince() { return currentProvince; }
    public void setCurrentProvince(String currentProvince) { this.currentProvince = currentProvince; }

    public String getCurrentZip() { return currentZip; }
    public void setCurrentZip(String currentZip) { this.currentZip = currentZip; }

    public String getPermanentHouse() { return permanentHouse; }
    public void setPermanentHouse(String permanentHouse) { this.permanentHouse = permanentHouse; }

    public String getPermanentStreet() { return permanentStreet; }
    public void setPermanentStreet(String permanentStreet) { this.permanentStreet = permanentStreet; }

    public String getPermanentBarangay() { return permanentBarangay; }
    public void setPermanentBarangay(String permanentBarangay) { this.permanentBarangay = permanentBarangay; }

    public String getPermanentCity() { return permanentCity; }
    public void setPermanentCity(String permanentCity) { this.permanentCity = permanentCity; }

    public String getPermanentProvince() { return permanentProvince; }
    public void setPermanentProvince(String permanentProvince) { this.permanentProvince = permanentProvince; }

    public String getPermanentZip() { return permanentZip; }
    public void setPermanentZip(String permanentZip) { this.permanentZip = permanentZip; }

    public String getFatherName() { return fatherName; }
    public void setFatherName(String fatherName) { this.fatherName = fatherName; }

    public String getFatherContact() { return fatherContact; }
    public void setFatherContact(String fatherContact) { this.fatherContact = fatherContact; }

    public String getMotherName() { return motherName; }
    public void setMotherName(String motherName) { this.motherName = motherName; }

    public String getMotherContact() { return motherContact; }
    public void setMotherContact(String motherContact) { this.motherContact = motherContact; }

    public String getGuardianName() { return guardianName; }
    public void setGuardianName(String guardianName) { this.guardianName = guardianName; }

    public String getGuardianRelationship() { return guardianRelationship; }
    public void setGuardianRelationship(String guardianRelationship) { this.guardianRelationship = guardianRelationship; }

    public String getGuardianContact() { return guardianContact; }
    public void setGuardianContact(String guardianContact) { this.guardianContact = guardianContact; }

    public String getLastGradeCompleted() { return lastGradeCompleted; }
    public void setLastGradeCompleted(String lastGradeCompleted) { this.lastGradeCompleted = lastGradeCompleted; }

    public String getLastSchoolYearCompleted() { return lastSchoolYearCompleted; }
    public void setLastSchoolYearCompleted(String lastSchoolYearCompleted) { this.lastSchoolYearCompleted = lastSchoolYearCompleted; }

    public String getLastSchoolAttended() { return lastSchoolAttended; }
    public void setLastSchoolAttended(String lastSchoolAttended) { this.lastSchoolAttended = lastSchoolAttended; }

    public String getSchoolID() { return schoolID; }
    public void setSchoolID(String schoolID) { this.schoolID = schoolID; }

    public String getApplicationStatus() { return applicationStatus; }
    public void setApplicationStatus(String applicationStatus) { this.applicationStatus = applicationStatus; }

}

