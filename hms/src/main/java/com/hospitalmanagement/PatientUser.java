package com.hospitalmanagement;

/**
 * PatientUser - Represents a patient in the hospital management system
 * Extends BaseUser with patient-specific functionality
 */
public class PatientUser extends BaseUser {
    private Integer patientId;

    public PatientUser(int userId, String name, String email, String password,
                       String phone, String address, int roleId, String roleName) {
        super(userId, name, email, password, phone, address, roleId, roleName);
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    /**
     * View appointments for this patient
     */
    public void viewAppointments() {
        // Implementation delegated to controller/service layer
    }
}
