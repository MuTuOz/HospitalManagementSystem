package com.hospitalmanagement.service;

import com.hospitalmanagement.User;
import com.hospitalmanagement.DatabaseQuery;
import java.util.List;
import java.util.stream.Collectors;

/**
 * UserService - Service layer for user management operations.
 * Implements the service pattern specified in Software Detailed Design.
 * Wraps DatabaseQuery to provide a clean service API.
 */
public class UserService {
    
    /**
     * Add a new user to the system
     */
    public boolean addUser(String name, String email, String hashedPassword, String phone, String address, int roleId) {
        return DatabaseQuery.createUser(name, email, hashedPassword, phone, address, roleId);
    }
    
    /**
     * Remove a user by ID
     */
    public boolean removeUser(int userId) {
        return DatabaseQuery.deleteUser(userId);
    }
    
    /**
     * Find a user by email (case-insensitive)
     */
    public User findUserByEmail(String email) {
        return DatabaseQuery.getUserByEmail(email);
    }
    
    /**
     * Get all users in the system
     */
    public List<User> getAllUsers() {
        return DatabaseQuery.getAllUsers();
    }
    
    /**
     * Get users filtered by role name
     */
    public List<User> getUsersByRole(String roleName) {
        return getAllUsers().stream()
                .filter(u -> u.getRoleName() != null && u.getRoleName().equalsIgnoreCase(roleName))
                .collect(Collectors.toList());
    }
    
    /**
     * Change password for a user
     */
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        return DatabaseQuery.changePassword(userId, oldPassword, newPassword);
    }
    
    /**
     * Update user profile information
     */
    public boolean updateUser(int userId, String name, String phone, String address, Integer roleId) {
        return DatabaseQuery.updateUser(userId, name, phone, address, roleId);
    }
    
    /**
     * Reset failed login attempts for a user (unlocks account)
     */
    public void resetFailedAttempts(int userId) {
        DatabaseQuery.resetFailedAttempts(userId);
    }
    
    /**
     * Increment failed login attempts and return new count
     */
    public int incrementFailedAttempts(int userId) {
        return DatabaseQuery.incrementFailedAttempts(userId);
    }
    
    /**
     * Set account lock until specified timestamp
     */
    public void setLockUntil(int userId, java.sql.Timestamp until) {
        DatabaseQuery.setLockUntil(userId, until);
    }
}