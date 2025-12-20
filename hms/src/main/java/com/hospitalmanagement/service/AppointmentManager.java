package com.hospitalmanagement.service;

import com.hospitalmanagement.Appointment;
import com.hospitalmanagement.DatabaseQuery;
import java.util.List;
import java.util.stream.Collectors;

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
        // Check if appointment can be cancelled (not completed)
        Appointment appointment = DatabaseQuery.getAppointmentById(appointmentId);
        if (appointment == null) {
            System.out.println("Appointment not found: " + appointmentId);
            return false;
        }
        
        if ("completed".equalsIgnoreCase(appointment.getStatus())) {
            System.out.println("Cannot cancel completed appointment: " + appointmentId);
            return false;
        }
        
        return DatabaseQuery.cancelAppointment(appointmentId, cancelledByUserId);
    }
    
    /**
     * Reactivate a cancelled appointment if the time slot is still available
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

    /**
     * Filter appointments by doctor name
     */
    public List<Appointment> filterByDoctor(List<Appointment> appointments, String doctorName) {
        if (doctorName == null || doctorName.isEmpty()) {
            return appointments;
        }
        return appointments.stream()
                .filter(a -> a.getDoctorName().equalsIgnoreCase(doctorName))
                .collect(Collectors.toList());
    }

    /**
     * Filter appointments by hospital name
     */
    public List<Appointment> filterByHospital(List<Appointment> appointments, String hospitalName) {
        if (hospitalName == null || hospitalName.isEmpty()) {
            return appointments;
        }
        return appointments.stream()
                .filter(a -> a.getHospitalName() != null && a.getHospitalName().equalsIgnoreCase(hospitalName))
                .collect(Collectors.toList());
    }

    /**
     * Filter appointments by clinic name
     */
    public List<Appointment> filterByClinic(List<Appointment> appointments, String clinicName) {
        if (clinicName == null || clinicName.isEmpty()) {
            return appointments;
        }
        return appointments.stream()
                .filter(a -> a.getClinicName() != null && a.getClinicName().equalsIgnoreCase(clinicName))
                .collect(Collectors.toList());
    }

    /**
     * Get all unique doctor names from appointments
     */
    public List<String> getUniqueDoctorNames(List<Appointment> appointments) {
        return appointments.stream()
                .map(Appointment::getDoctorName)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Get all unique hospital names from appointments
     */
    public List<String> getUniqueHospitalNames(List<Appointment> appointments) {
        return appointments.stream()
                .map(Appointment::getHospitalName)
                .filter(name -> name != null && !name.isEmpty())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Get all unique clinic names from appointments
     */
    public List<String> getUniquClinicNames(List<Appointment> appointments) {
        return appointments.stream()
                .map(Appointment::getClinicName)
                .filter(name -> name != null && !name.isEmpty())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }
}
