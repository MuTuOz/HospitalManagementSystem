package com.hospitalmanagement.service;

import com.hospitalmanagement.Appointment;
import com.hospitalmanagement.AvailabilityOption;
import com.hospitalmanagement.DatabaseQuery;
import java.util.List;

/**
 * AppointmentManager - Service layer for appointment operations.
 * Implements the service pattern specified in Software Detailed Design.
 * Wraps DatabaseQuery to provide appointment management API.
 */
public class AppointmentManager {
    
    private final AvailabilityService availabilityService;
    
    public AppointmentManager() {
        this.availabilityService = new AvailabilityService();
    }
    
    /**
     * Create a new appointment
     * @return true if appointment was created successfully
     */
    public boolean createAppointment(int patientId, int doctorId, int availabilityId, int hospitalId, String notes) {
        // Check if availability is still free
        if (DatabaseQuery.isAvailabilityBooked(availabilityId)) {
            return false;
        }
        return DatabaseQuery.createAppointment(doctorId, patientId, availabilityId, hospitalId, notes);
    }
    
    /**
     * Delete/cancel an appointment
     */
    public boolean deleteAppointment(int appointmentId, int cancelledByUserId) {
        return DatabaseQuery.cancelAppointment(appointmentId, cancelledByUserId);
    }
    
    /**
     * Reactivate a cancelled appointment
     */
    public boolean reactivateAppointment(int appointmentId, int reactivatedByUserId) {
        return DatabaseQuery.reactivateAppointment(appointmentId, reactivatedByUserId);
    }
    
    /**
     * View appointments for a specific patient
     */
    public List<Appointment> viewAppointmentsByPatient(int patientId) {
        return DatabaseQuery.getAppointmentsByPatientId(patientId);
    }
    
    /**
     * View appointments for a specific doctor
     */
    public List<Appointment> viewAppointmentsByDoctor(int doctorId) {
        return DatabaseQuery.getAppointmentsByDoctorId(doctorId);
    }
    
    /**
     * Get availability service for managing doctor schedules
     */
    public AvailabilityService getAvailabilityService() {
        return availabilityService;
    }
}
