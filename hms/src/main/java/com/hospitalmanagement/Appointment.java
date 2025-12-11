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

    public Appointment(int appointmentId, int doctorId, int patientId, int availabilityId, int hospitalId,
                       String status, String notes, String diagnosis, String prescription,
                       Date appointmentDate, String timeSlot, String doctorName, String patientName) {
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
    
    public int getAppointmentId() {
        return appointmentId;
    }
    public int getDoctorId() {
        return doctorId;
    }
    public int getPatientId() {
        return patientId;
    }
    public int getAvailabilityId() {
        return availabilityId;
    }
    public int getHospitalId() {
        return hospitalId;
    }
    public String getStatus() {
        return status;
    }
    public String getNotes() {
        return notes;
    }
    public String getDiagnosis() {
        return diagnosis;
    }
    public String getPrescription() {
        return prescription;
    }
    public Date getAppointmentDate() {
        return appointmentDate;
    }
    public String getTimeSlot() {
        return timeSlot;
    }
    public String getDoctorName() {
        return doctorName;
    }
    public String getPatientName() {
        return patientName;
    }
    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }
    @Override
    public String toString(){
        return appointmentDate + " " + timeSlot + " - Dr. " + doctorName;
    }

}
