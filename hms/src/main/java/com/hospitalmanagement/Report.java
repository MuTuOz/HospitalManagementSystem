package com.hospitalmanagement;

import java.util.List;

/**
 * Report - Represents a hospital report containing various statistics.
 * Part of Hospital Management layer as specified in Software Detailed Design.
 */
public class Report {
    private int reportId;
    private List<Doctor> doctorList;
    private List<Patient> patientList;
    private List<Appointment> appointmentList;
    private String reportType;
    private String generatedDate;
    private int hospitalId;
    
    // Statistics fields
    private int totalAppointments;
    private int cancelledAppointments;
    private int completedAppointments;
    private double averageSatisfaction;
    
    // Default constructor
    public Report() {
    }
    
    // Constructor for basic report
    public Report(int reportId, String reportType, String generatedDate, int hospitalId) {
        this.reportId = reportId;
        this.reportType = reportType;
        this.generatedDate = generatedDate;
        this.hospitalId = hospitalId;
    }
    
    // Full constructor
    public Report(int reportId, List<Doctor> doctorList, List<Patient> patientList, 
                  List<Appointment> appointmentList, String reportType, String generatedDate, int hospitalId) {
        this.reportId = reportId;
        this.doctorList = doctorList;
        this.patientList = patientList;
        this.appointmentList = appointmentList;
        this.reportType = reportType;
        this.generatedDate = generatedDate;
        this.hospitalId = hospitalId;
    }

    // Getters and setters
    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public List<Doctor> getDoctorList() {
        return doctorList;
    }

    public void setDoctorList(List<Doctor> doctorList) {
        this.doctorList = doctorList;
    }

    public List<Patient> getPatientList() {
        return patientList;
    }

    public void setPatientList(List<Patient> patientList) {
        this.patientList = patientList;
    }

    public List<Appointment> getAppointmentList() {
        return appointmentList;
    }

    public void setAppointmentList(List<Appointment> appointmentList) {
        this.appointmentList = appointmentList;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getGeneratedDate() {
        return generatedDate;
    }

    public void setGeneratedDate(String generatedDate) {
        this.generatedDate = generatedDate;
    }

    public int getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(int hospitalId) {
        this.hospitalId = hospitalId;
    }

    public int getTotalAppointments() {
        return totalAppointments;
    }

    public void setTotalAppointments(int totalAppointments) {
        this.totalAppointments = totalAppointments;
    }

    public int getCancelledAppointments() {
        return cancelledAppointments;
    }

    public void setCancelledAppointments(int cancelledAppointments) {
        this.cancelledAppointments = cancelledAppointments;
    }

    public int getCompletedAppointments() {
        return completedAppointments;
    }

    public void setCompletedAppointments(int completedAppointments) {
        this.completedAppointments = completedAppointments;
    }

    public double getAverageSatisfaction() {
        return averageSatisfaction;
    }

    public void setAverageSatisfaction(double averageSatisfaction) {
        this.averageSatisfaction = averageSatisfaction;
    }
    
    @Override
    public String toString() {
        return "Report{" +
                "reportId=" + reportId +
                ", reportType='" + reportType + '\'' +
                ", generatedDate='" + generatedDate + '\'' +
                ", hospitalId=" + hospitalId +
                ", totalAppointments=" + totalAppointments +
                ", cancelledAppointments=" + cancelledAppointments +
                ", completedAppointments=" + completedAppointments +
                ", averageSatisfaction=" + averageSatisfaction +
                '}';
    }
}
