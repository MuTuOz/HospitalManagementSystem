package com.hospitalmanagement;

import java.io.File;
import java.util.List;

public class DevRunner {
    public static void main(String[] args) {
        System.out.println("DevRunner: Starting sanity checks...");
        try {
            List<Hospital> hospitals = DatabaseQuery.getAllHospitals();
            System.out.println("Hospitals found: " + (hospitals == null ? 0 : hospitals.size()));

            List<User> users = DatabaseQuery.getAllUsers();
            System.out.println("Users found: " + (users == null ? 0 : users.size()));

            List<Doctor> doctors = DatabaseQuery.getAllDoctors();
            System.out.println("Doctors found: " + (doctors == null ? 0 : doctors.size()));

            List<Appointment> appointments = DatabaseQuery.getAllAppointments();
            System.out.println("Appointments found: " + (appointments == null ? 0 : appointments.size()));

            File out = new File("target/reports/appointments_export.csv");
            out.getParentFile().mkdirs();
            boolean ok = DatabaseQuery.exportAppointmentsPerDoctorCSV(out);
            System.out.println("CSV export to " + out.getAbsolutePath() + " -> " + ok);

            System.out.println("DevRunner: Sanity checks finished.");
        } catch (Exception e) {
            System.err.println("DevRunner: Error during checks: " + e.getMessage());
            e.printStackTrace();
            System.exit(2);
        }
    }
}
