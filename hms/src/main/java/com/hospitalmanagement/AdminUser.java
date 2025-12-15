package com.hospitalmanagement;

/**
 * AdminUser - Represents a system administrator
 * Extends BaseUser with admin-specific functionality
 */
public class AdminUser extends BaseUser {

    public AdminUser(int userId, String name, String email, String password,
                     String phone, String address, int roleId, String roleName) {
        super(userId, name, email, password, phone, address, roleId, roleName);
    }

    /**
     * Manage user accounts (create, update, delete)
     */
    public void manageUsers() {
        // Implementation delegated to controller/service layer
    }

    /**
     * Manage system-wide settings and configurations
     */
    public void manageSystemSettings() {
        // Implementation delegated to controller/service layer
    }
}
