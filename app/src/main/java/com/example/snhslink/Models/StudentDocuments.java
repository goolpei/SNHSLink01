package com.example.snhslink.Models;

public class StudentDocuments {
    private String birthCertificateUrl;
    private String grade10ReportCardFrontUrl;
    private String grade10ReportCardBackUrl;
    private String idPictureUrl;

    // 🔹 Empty constructor (Needed for Firebase)
    public StudentDocuments() {}

    // 🔹 Constructor with all document URLs
    public StudentDocuments(String birthCertificateUrl, String grade10ReportCardFrontUrl,
                            String grade10ReportCardBackUrl, String idPictureUrl) {
        this.birthCertificateUrl = birthCertificateUrl;
        this.grade10ReportCardFrontUrl = grade10ReportCardFrontUrl;
        this.grade10ReportCardBackUrl = grade10ReportCardBackUrl;
        this.idPictureUrl = idPictureUrl;
    }

    // 🔹 Getters and Setters
    public String getBirthCertificateUrl() { return birthCertificateUrl; }
    public void setBirthCertificateUrl(String birthCertificateUrl) { this.birthCertificateUrl = birthCertificateUrl; }

    public String getGrade10ReportCardFrontUrl() { return grade10ReportCardFrontUrl; }
    public void setGrade10ReportCardFrontUrl(String grade10ReportCardFrontUrl) { this.grade10ReportCardFrontUrl = grade10ReportCardFrontUrl; }

    public String getGrade10ReportCardBackUrl() { return grade10ReportCardBackUrl; }
    public void setGrade10ReportCardBackUrl(String grade10ReportCardBackUrl) { this.grade10ReportCardBackUrl = grade10ReportCardBackUrl; }

    public String getIdPictureUrl() { return idPictureUrl; }
    public void setIdPictureUrl(String idPictureUrl) { this.idPictureUrl = idPictureUrl; }
}
