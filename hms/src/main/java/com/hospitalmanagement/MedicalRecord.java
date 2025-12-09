package com.hospitalmanagement;

import java.sql.Date;
import java.sql.Timestamp;

public class MedicalRecord {
    private final int recordId;
    private final int patientId;
    private final Integer doctorId;
    private final Integer appointmentId;
    private final int hospitalId;
    private final Date recordDate;
    private final String testResults;
    private final String medications;
    private final String notes;
    private final Timestamp createdAt;
    private final Timestamp updatedAt;
    private final Integer lastEditedBy;

    public MedicalRecord(int recordId, int patientId, Integer doctorId, Integer appointmentId, int hospitalId,
                         Date recordDate, String testResults, String medications, String notes,
                         Timestamp createdAt, Timestamp updatedAt, Integer lastEditedBy) {
        this.recordId = recordId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentId = appointmentId;
        this.hospitalId = hospitalId;
        this.recordDate = recordDate;
        this.testResults = testResults;
        this.medications = medications;
        this.notes = notes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.lastEditedBy = lastEditedBy;
    }

    public int getRecordId() { return recordId; }
    public int getPatientId() { return patientId; }
    public Integer getDoctorId() { return doctorId; }
    public Integer getAppointmentId() { return appointmentId; }
    public int getHospitalId() { return hospitalId; }
    public Date getRecordDate() { return recordDate; }
    public String getTestResults() { return testResults; }
    public String getMedications() { return medications; }
    public String getNotes() { return notes; }
    public Timestamp getCreatedAt() { return createdAt; }
    public Timestamp getUpdatedAt() { return updatedAt; }
    public Integer getLastEditedBy() { return lastEditedBy; }
}
