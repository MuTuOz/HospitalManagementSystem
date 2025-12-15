package com.hospitalmanagement;

import java.sql.Date;

public class Appointment {
    private int appointmentId;
    private int doctorId;
    private int patientId;
    private int availabilityId;
    private int hospitalId;
    private String status;
    private String notes;
    private String diagnosis;
    private String prescription;
    private Date appointmentDate;
    private String timeSlot;
    private String doctorName;
    private String patientName;

    // Constructor
    public Appointment(int appointmentId, int doctorId, int patientId, int availabilityId,
                      int hospitalId, String status, String notes, String diagnosis,
                      String prescription, Date appointmentDate, String timeSlot,
                      String doctorName, String patientName) {
        this.appointmentId = appointmentId;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.availabilityId = availabilityId;
        this.hospitalId = hospitalId;
        this.status = status;
        this.notes = notes;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.appointmentDate = appointmentDate;
        this.timeSlot = timeSlot;
        this.doctorName = doctorName;
        this.patientName = patientName;
    }

    // Getters and Setters
    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public int getAvailabilityId() { return availabilityId; }
    public void setAvailabilityId(int availabilityId) { this.availabilityId = availabilityId; }

    public int getHospitalId() { return hospitalId; }
    public void setHospitalId(int hospitalId) { this.hospitalId = hospitalId; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public String getDiagnosis() { return diagnosis; }
    public void setDiagnosis(String diagnosis) { this.diagnosis = diagnosis; }

    public String getPrescription() { return prescription; }
    public void setPrescription(String prescription) { this.prescription = prescription; }

    public Date getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(Date appointmentDate) { this.appointmentDate = appointmentDate; }

    public String getTimeSlot() { return timeSlot; }
    public void setTimeSlot(String timeSlot) { this.timeSlot = timeSlot; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    @Override
    public String toString() {
        return appointmentDate + " " + timeSlot + " - Dr. " + doctorName;
    }
}
