package com.hospitalmanagement;

/**
 * User - Base class for all user types in HMS
 */
public class User {
    protected int userId;
    protected String name;
    protected String email;
    protected String password;
    protected String phone;
    protected String address;
    protected int roleId;
    protected String roleName;
    protected int failedAttempts;
    protected java.sql.Timestamp lockedUntil;

    public User(int userId, String name, String email, String password,
                String phone, String address, int roleId, String roleName) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
        this.roleId = roleId;
        this.roleName = roleName;
        this.failedAttempts = 0;
        this.lockedUntil = null;
    }

    // Getters
    public int getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public int getRoleId() { return roleId; }
    public String getRoleName() { return roleName; }
    public int getFailedAttempts() { return failedAttempts; }
    public java.sql.Timestamp getLockedUntil() { return lockedUntil; }

    // Setters
    public void setUserId(int userId) { this.userId = userId; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
    public void setRoleId(int roleId) { this.roleId = roleId; }
    public void setRoleName(String roleName) { this.roleName = roleName; }
    public void setFailedAttempts(int failedAttempts) { this.failedAttempts = failedAttempts; }
    public void setLockedUntil(java.sql.Timestamp lockedUntil) { this.lockedUntil = lockedUntil; }
}