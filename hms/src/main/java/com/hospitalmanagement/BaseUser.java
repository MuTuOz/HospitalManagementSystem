package com.hospitalmanagement;

/**
 * BaseUser - Abstract base class implementing User interface
 * Provides common functionality for all user types
 */
public abstract class BaseUser implements User {
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

    public BaseUser(int userId, String name, String email, String password,
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
    @Override
    public int getUserId() { return userId; }
    
    @Override
    public String getName() { return name; }
    
    @Override
    public String getEmail() { return email; }
    
    @Override
    public String getPassword() { return password; }
    
    @Override
    public String getPhone() { return phone; }
    
    @Override
    public String getAddress() { return address; }
    
    @Override
    public int getRoleId() { return roleId; }
    
    @Override
    public String getRoleName() { return roleName; }
    
    @Override
    public int getFailedAttempts() { return failedAttempts; }
    
    @Override
    public java.sql.Timestamp getLockedUntil() { return lockedUntil; }

    // Setters
    @Override
    public void setUserId(int userId) { this.userId = userId; }
    
    @Override
    public void setName(String name) { this.name = name; }
    
    @Override
    public void setEmail(String email) { this.email = email; }
    
    @Override
    public void setPassword(String password) { this.password = password; }
    
    @Override
    public void setPhone(String phone) { this.phone = phone; }
    
    @Override
    public void setAddress(String address) { this.address = address; }
    
    @Override
    public void setRoleId(int roleId) { this.roleId = roleId; }
    
    @Override
    public void setRoleName(String roleName) { this.roleName = roleName; }
    
    @Override
    public void setFailedAttempts(int failedAttempts) { this.failedAttempts = failedAttempts; }
    
    @Override
    public void setLockedUntil(java.sql.Timestamp lockedUntil) { this.lockedUntil = lockedUntil; }
}
