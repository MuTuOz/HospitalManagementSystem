package com.hospitalmanagement;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * UserFactory - Factory for creating role-specific User instances
 * Implements Factory Pattern based on user role
 */
public class UserFactory {
    
    /**
     * Create appropriate User subclass based on role
     */
    public static User createUser(int userId, String name, String email, String password,
                                  String phone, String address, int roleId, String roleName) {
        if (roleName == null) {
            roleName = "";
        }
        
        // Simply return a User instance with role info
        // Actual Patient, Doctor, Manager, Admin objects are fetched separately from database
        return new User(userId, name, email, password, phone, address, roleId, roleName);
    }
    
    /**
     * Create User from ResultSet
     */
    public static User createUserFromResultSet(ResultSet rs) throws SQLException {
        return createUser(
            rs.getInt("user_id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("phone"),
            rs.getString("address"),
            rs.getInt("role_id"),
            rs.getString("role_name")
        );
    }
}
