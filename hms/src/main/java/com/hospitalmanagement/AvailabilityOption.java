package com.hospitalmanagement;

import java.sql.Date;

public class AvailabilityOption {
    private final int availabilityId;
    private final int hospitalId;
    private final Date date;
    private final String timeSlot;
    private final String hospitalName;

    public AvailabilityOption(int availabilityId, int hospitalId, Date date, String timeSlot, String hospitalName) {
        this.availabilityId = availabilityId;
        this.hospitalId = hospitalId;
        this.date = date;
        this.timeSlot = timeSlot;
        this.hospitalName = hospitalName;
    }

    public int getAvailabilityId() { return availabilityId; }
    public int getHospitalId() { return hospitalId; }
    public Date getDate() { return date; }
    public String getTimeSlot() { return timeSlot; }
    public String getHospitalName() { return hospitalName; }

    @Override
    public String toString() {
        return availabilityId + " - " + date.toString() + " " + timeSlot + " - " + hospitalName;
    }
}
