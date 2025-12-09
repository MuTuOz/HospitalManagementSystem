package com.hospitalmanagement;

public class Doctor {
    private int doctorId;
    private int userId;
    private int specialtyId;
    private int clinicId;
    private int hospitalId;
    private String licenseNo;
    private int experience;
    private String education;
    private double consultationFee;
    private String name;
    private String specialtyName;

    // Constructor
    public Doctor(int doctorId, int userId, int specialtyId, int clinicId, int hospitalId,
                  String licenseNo, int experience, String education, double consultationFee,
                  String name, String specialtyName) {
        this.doctorId = doctorId;
        this.userId = userId;
        this.specialtyId = specialtyId;
        this.clinicId = clinicId;
        this.hospitalId = hospitalId;
        this.licenseNo = licenseNo;
        this.experience = experience;
        this.education = education;
        this.consultationFee = consultationFee;
        this.name = name;
        this.specialtyName = specialtyName;
    }

    // Getters and Setters
    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getSpecialtyId() { return specialtyId; }
    public void setSpecialtyId(int specialtyId) { this.specialtyId = specialtyId; }

    public int getClinicId() { return clinicId; }
    public void setClinicId(int clinicId) { this.clinicId = clinicId; }

    public int getHospitalId() { return hospitalId; }
    public void setHospitalId(int hospitalId) { this.hospitalId = hospitalId; }

    public String getLicenseNo() { return licenseNo; }
    public void setLicenseNo(String licenseNo) { this.licenseNo = licenseNo; }

    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = experience; }

    public String getEducation() { return education; }
    public void setEducation(String education) { this.education = education; }

    public double getConsultationFee() { return consultationFee; }
    public void setConsultationFee(double consultationFee) { this.consultationFee = consultationFee; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialtyName() { return specialtyName; }
    public void setSpecialtyName(String specialtyName) { this.specialtyName = specialtyName; }

    @Override
    public String toString() {
        return name + " - " + specialtyName;
    }
}
