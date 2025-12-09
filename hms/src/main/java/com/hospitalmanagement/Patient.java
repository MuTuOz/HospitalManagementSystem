package com.hospitalmanagement;

import java.sql.Date;

public class Patient {
    private int patientId;
    private int userId;
    private String bloodType;
    private Date dateOfBirth;
    private String insuranceNo;
    private String emergencyContact;
    private String name;
    private String email;
    private String phone;

    // Constructor
    public Patient(int patientId, int userId, String bloodType, Date dateOfBirth,
                   String insuranceNo, String emergencyContact, String name,
                   String email, String phone) {
        this.patientId = patientId;
        this.userId = userId;
        this.bloodType = bloodType;
        this.dateOfBirth = dateOfBirth;
        this.insuranceNo = insuranceNo;
        this.emergencyContact = emergencyContact;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // Getters and Setters
    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getBloodType() { return bloodType; }
    public void setBloodType(String bloodType) { this.bloodType = bloodType; }

    public Date getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(Date dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getInsuranceNo() { return insuranceNo; }
    public void setInsuranceNo(String insuranceNo) { this.insuranceNo = insuranceNo; }

    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() {
        return name;
    }
}
