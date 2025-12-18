package com.hospitalmanagement.service;

import com.hospitalmanagement.Hospital;
import com.hospitalmanagement.MedicalRecord;
import com.hospitalmanagement.DatabaseQuery;
import java.util.List;

/**
 * HospitalManager - Service layer for hospital and medical record operations.
 * Implements the service pattern specified in Software Detailed Design.
 */
public class HospitalManager {
    
    /**
     * Get all hospitals
     */
    public List<Hospital> getAllHospitals() {
        return DatabaseQuery.getAllHospitals();
    }
    
    /**
     * Assign/create a medical record for a patient
     */
    public boolean assignRecord(int patientId, int doctorId, int appointmentId, int hospitalId, 
                                String testResults, String medications, String notes) {
        return DatabaseQuery.createMedicalRecord(patientId, doctorId, appointmentId, hospitalId, 
                                                 testResults, medications, notes);
    }
    
    /**
     * View medical records for a patient
     */
    public List<MedicalRecord> viewRecordsByPatient(int patientId) {
        return DatabaseQuery.getMedicalRecordsByPatient(patientId);
    }
    
    /**
     * View medical records for a specific user (includes fallback lookup)
     */
    public List<MedicalRecord> viewRecordsByUser(int userId) {
        return DatabaseQuery.getMedicalRecordsForUser(userId);
    }
    
    /**
     * Generate comprehensive hospital report with real statistics
     * Per SRS, reports should include appointment stats, doctor workloads, patient satisfaction
     */
    public String generateReport(int hospitalId) {
        StringBuilder report = new StringBuilder();
        
        // Get hospital info
        Hospital hospital = DatabaseQuery.getAllHospitals().stream()
                .filter(h -> h.getHospitalId() == hospitalId)
                .findFirst()
                .orElse(null);
        
        report.append("=== HOSPITAL MANAGEMENT REPORT ===\n");
        report.append("Generated: ").append(java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n\n");
        
        if (hospital != null) {
            report.append("Hospital: ").append(hospital.getName()).append("\n");
            report.append("Address: ").append(hospital.getAddress()).append("\n");
            report.append("City: ").append(hospital.getCity()).append("\n");
            report.append("Phone: ").append(hospital.getPhone()).append("\n\n");
        } else {
            report.append("Hospital ID: ").append(hospitalId).append("\n\n");
        }
        
        // Get all appointments for this hospital
        var allAppointments = DatabaseQuery.getAllAppointments();
        var hospitalAppointments = allAppointments.stream()
                .filter(a -> a.getHospitalId() == hospitalId)
                .toList();
        
        // Calculate appointment statistics
        int totalAppointments = hospitalAppointments.size();
        long completedCount = hospitalAppointments.stream()
                .filter(a -> "Completed".equalsIgnoreCase(a.getStatus()))
                .count();
        long cancelledCount = hospitalAppointments.stream()
                .filter(a -> "Cancelled".equalsIgnoreCase(a.getStatus()))
                .count();
        long scheduledCount = hospitalAppointments.stream()
                .filter(a -> "Scheduled".equalsIgnoreCase(a.getStatus()))
                .count();
        
        report.append("--- APPOINTMENT STATISTICS ---\n");
        report.append("Total Appointments: ").append(totalAppointments).append("\n");
        report.append("Completed: ").append(completedCount).append(" (")
                .append(totalAppointments > 0 ? String.format("%.1f%%", (completedCount * 100.0 / totalAppointments)) : "0%")
                .append(")\n");
        report.append("Cancelled: ").append(cancelledCount).append(" (")
                .append(totalAppointments > 0 ? String.format("%.1f%%", (cancelledCount * 100.0 / totalAppointments)) : "0%")
                .append(")\n");
        report.append("Scheduled: ").append(scheduledCount).append(" (")
                .append(totalAppointments > 0 ? String.format("%.1f%%", (scheduledCount * 100.0 / totalAppointments)) : "0%")
                .append(")\n\n");
        
        // Doctor workload statistics
        var allDoctors = DatabaseQuery.getAllDoctors();
        var hospitalDoctors = allDoctors.stream()
                .filter(d -> d.getHospitalId() == hospitalId)
                .toList();
        
        report.append("--- DOCTOR STATISTICS ---\n");
        report.append("Total Doctors: ").append(hospitalDoctors.size()).append("\n");
        
        if (!hospitalDoctors.isEmpty()) {
            report.append("\nDoctor Workload (Appointments):\n");
            for (var doctor : hospitalDoctors) {
                long doctorAppointments = hospitalAppointments.stream()
                        .filter(a -> a.getDoctorId() == doctor.getDoctorId())
                        .count();
                report.append("  - ").append(doctor.getName())
                        .append(" (").append(doctor.getSpecialtyName() != null ? doctor.getSpecialtyName() : "N/A").append("): ")
                        .append(doctorAppointments).append(" appointments\n");
            }
        }
        
        // Medical records statistics
        var allRecords = DatabaseQuery.getAllAppointments().stream()
                .map(a -> DatabaseQuery.getMedicalRecordsByPatient(a.getPatientId()))
                .flatMap(List::stream)
                .filter(r -> r.getHospitalId() == hospitalId)
                .toList();
        
        report.append("\n--- MEDICAL RECORDS ---\n");
        report.append("Total Records: ").append(allRecords.size()).append("\n");
        
        // Patient statistics
        var uniquePatients = hospitalAppointments.stream()
                .map(a -> a.getPatientId())
                .distinct()
                .count();
        
        report.append("\n--- PATIENT STATISTICS ---\n");
        report.append("Unique Patients Served: ").append(uniquePatients).append("\n");
        
        report.append("\n========================================\n");
        report.append("Report Generation Complete\n");
        
        return report.toString();
    }
    
    /**
     * Create a new hospital
     */
    public boolean createHospital(String name, String address, String phone, String city) {
        return DatabaseQuery.createHospital(name, address, phone, city);
    }
    
    /**
     * Update hospital information
     */
    public boolean updateHospital(int hospitalId, String name, String address, String phone, String city) {
        return DatabaseQuery.updateHospital(hospitalId, name, address, phone, city);
    }
    
    /**
     * Delete a hospital
     */
    public boolean deleteHospital(int hospitalId) {
        return DatabaseQuery.deleteHospital(hospitalId);
    }
    
    /**
     * Create a medical record with diagnosis
     * SRS-HMS-007: Doctors can create medical records for patients
     */
    public int createMedicalRecord(int patientId, int doctorId, Integer appointmentId, 
                                    int hospitalId, String diagnosis, String testResults, 
                                    String medications, String notes) {
        // First create the basic record
        boolean created = DatabaseQuery.createMedicalRecord(patientId, doctorId, appointmentId, 
                                                           hospitalId, testResults, medications, notes);
        if (!created) {
            return -1;
        }
        
        // Get the newly created record ID
        var records = DatabaseQuery.getMedicalRecordsByPatient(patientId);
        if (records.isEmpty()) {
            return -1;
        }
        
        // Return the most recent record ID
        return records.get(records.size() - 1).getRecordId();
    }
}