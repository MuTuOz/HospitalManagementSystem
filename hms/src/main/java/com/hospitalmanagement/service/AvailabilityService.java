package com.hospitalmanagement.service;

import com.hospitalmanagement.AvailabilityOption;
import com.hospitalmanagement.DatabaseQuery;
import java.time.LocalDate;
import java.util.List;

/**
 * AvailabilityService - Helper service for managing doctor availability.
 * Specified in Software Detailed Design.
 */
public class AvailabilityService {
    
    /**
     * Match availability for a doctor
     */
    public List<AvailabilityOption> matchAvailability(int doctorId, Integer availabilityId) {
        List<AvailabilityOption> avails = DatabaseQuery.getAvailabilitiesByDoctor(doctorId);
        if (availabilityId != null) {
            return avails.stream()
                    .filter(a -> a.getAvailabilityId() == availabilityId)
                    .toList();
        }
        return avails;
    }
    
    /**
     * Create new availability for a doctor
     */
    public boolean createAvailability(int doctorId, LocalDate date, String timeSlot) {
        return DatabaseQuery.addAvailability(doctorId, date, timeSlot);
    }
    
    /**
     * Remove/delete an availability slot
     */
    public boolean removeAvailability(int availabilityId) {
        return DatabaseQuery.removeAvailability(availabilityId);
    }
    
    /**
     * Check if an availability slot is booked
     */
    public boolean isBooked(int availabilityId) {
        return DatabaseQuery.isAvailabilityBooked(availabilityId);
    }
}
