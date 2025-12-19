package com.hospitalmanagement;

import java.sql.Date;

public class Review {
    private int reviewId;
    private int patientId;
    private int doctorId;
    private Integer appointmentId;
    private int hospitalId;
    private int rating; // 1-5
    private String comment;
    private Date reviewDate;
    private boolean isAnonymous;
    
    // Additional fields for display
    private String patientName;
    private String doctorName;

    public Review(int reviewId, int patientId, int doctorId, Integer appointmentId,
                  int hospitalId, int rating, String comment, Date reviewDate,
                  boolean isAnonymous) {
        this.reviewId = reviewId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentId = appointmentId;
        this.hospitalId = hospitalId;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
        this.isAnonymous = isAnonymous;
    }

    // Getters and Setters
    public int getReviewId() { return reviewId; }
    public void setReviewId(int reviewId) { this.reviewId = reviewId; }

    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }

    public int getDoctorId() { return doctorId; }
    public void setDoctorId(int doctorId) { this.doctorId = doctorId; }

    public Integer getAppointmentId() { return appointmentId; }
    public void setAppointmentId(Integer appointmentId) { this.appointmentId = appointmentId; }

    public int getHospitalId() { return hospitalId; }
    public void setHospitalId(int hospitalId) { this.hospitalId = hospitalId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Date getReviewDate() { return reviewDate; }
    public void setReviewDate(Date reviewDate) { this.reviewDate = reviewDate; }

    public boolean isAnonymous() { return isAnonymous; }
    public void setAnonymous(boolean anonymous) { isAnonymous = anonymous; }

    public String getPatientName() { return patientName; }
    public void setPatientName(String patientName) { this.patientName = patientName; }

    public String getDoctorName() { return doctorName; }
    public void setDoctorName(String doctorName) { this.doctorName = doctorName; }

    @Override
    public String toString() {
        return "Review{" +
                "reviewId=" + reviewId +
                ", doctorName='" + doctorName + '\'' +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", reviewDate=" + reviewDate +
                '}';
    }
}
