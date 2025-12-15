package com.hospitalmanagement;

/**
 * DoctorUser - Represents a doctor in the hospital management system
 * Extends BaseUser with doctor-specific functionality
 */
public class DoctorUser extends BaseUser {
    private Integer doctorId;
    private String specialization;

    public DoctorUser(int userId, String name, String email, String password,
                      String phone, String address, int roleId, String roleName) {
        super(userId, name, email, password, phone, address, roleId, roleName);
    }

    public Integer getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Integer doctorId) {
        this.doctorId = doctorId;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    /**
     * View today's appointments for this doctor
     */
    public void viewTodayAppointments() {
        // Implementation delegated to controller/service layer
    }
}
