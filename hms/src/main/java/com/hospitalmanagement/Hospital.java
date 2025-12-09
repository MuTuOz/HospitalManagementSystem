package com.hospitalmanagement;

public class Hospital {
    private int hospitalId;
    private String name;
    private String address;
    private String phone;
    private String city;

    // Constructor
    public Hospital(int hospitalId, String name, String address, String phone, String city) {
        this.hospitalId = hospitalId;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.city = city;
    }

    // Getters and Setters
    public int getHospitalId() { return hospitalId; }
    public void setHospitalId(int hospitalId) { this.hospitalId = hospitalId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    @Override
    public String toString() {
        return name + " - " + city;
    }
}
