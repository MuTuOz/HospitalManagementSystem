package com.hospitalmanagement;

import java.sql.Date;

public class AvailabilityOption {
    private final int availabilityId;
    private final int doctorId;
    private final int hospitalId;
    private final Date date;
    private final String timeSlot;
    private final String hospitalName;
    private final String doctorName;

    public AvailabilityOption(int availabilityId, int doctorId, int hospitalId, Date date, String timeSlot, String hospitalName, String doctorName) {
        this.availabilityId = availabilityId;
        this.doctorId = doctorId;
        this.hospitalId = hospitalId;
        this.date = date;
        this.timeSlot = timeSlot;
        this.hospitalName = hospitalName;
        this.doctorName = doctorName;
    }

    public int getAvailabilityId() { return availabilityId; }
    public int getDoctorId() { return doctorId; }
    public int getHospitalId() { return hospitalId; }
    public Date getDate() { return date; }
    public String getTimeSlot() { return timeSlot; }
    public String getHospitalName() { return hospitalName; }
    public String getDoctorName() { return doctorName; }

    @Override
    public String toString() {
        return availabilityId + " - " + date.toString() + " " + timeSlot + " - " + doctorName + " - " + hospitalName;
    }
}
