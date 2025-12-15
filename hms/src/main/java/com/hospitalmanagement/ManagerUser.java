package com.hospitalmanagement;

/**
 * ManagerUser - Represents a hospital manager
 * Extends BaseUser with manager-specific functionality
 */
public class ManagerUser extends BaseUser {
    private Integer managerId;

    public ManagerUser(int userId, String name, String email, String password,
                       String phone, String address, int roleId, String roleName) {
        super(userId, name, email, password, phone, address, roleId, roleName);
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    /**
     * Generate hospital performance reports
     */
    public void generateReports() {
        // Implementation delegated to controller/service layer
    }

    /**
     * View hospital statistics and metrics
     */
    public void viewHospitalStatistics() {
        // Implementation delegated to controller/service layer
    }
}
