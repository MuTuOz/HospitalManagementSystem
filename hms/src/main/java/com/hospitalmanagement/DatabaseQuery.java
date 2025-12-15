package com.hospitalmanagement;

import java.sql.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class DatabaseQuery {

    // Doktor sorgularıfı
    public static List<Doctor> getDoctorsBySpecialty(int specialtyId) {
        List<Doctor> doctors = new ArrayList<>();
        String query = "SELECT d.*, u.name, s.name as specialty_name FROM Doctor d " +
                      "JOIN User u ON d.user_id = u.user_id " +
                      "JOIN Specialty s ON d.specialty_id = s.specialty_id " +
                      "WHERE d.specialty_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, specialtyId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Doctor doctor = new Doctor(
                    rs.getInt("doctor_id"),
                    rs.getInt("user_id"),
                    rs.getInt("specialty_id"),
                    rs.getInt("clinic_id"),
                    rs.getInt("hospital_id"),
                    rs.getString("license_no"),
                    rs.getInt("experience"),
                    rs.getString("education"),
                    rs.getDouble("consultation_fee"),
                    rs.getString("name"),
                    rs.getString("specialty_name")
                );
                doctors.add(doctor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return doctors;
    }

    // Hasta sorgularıfı
    public static Patient getPatientByUserId(int userId) {
        String query = "SELECT p.*, u.name, u.email, u.phone FROM Patient p " +
                      "JOIN User u ON p.user_id = u.user_id " +
                      "WHERE p.user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new Patient(
                    rs.getInt("patient_id"),
                    rs.getInt("user_id"),
                    rs.getString("blood_type"),
                    rs.getDate("date_of_birth"),
                    rs.getString("insurance_no"),
                    rs.getString("emergency_contact"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    // Hastaneleri getir
    public static List<Hospital> getAllHospitals() {
        List<Hospital> hospitals = new ArrayList<>();
        String query = "SELECT * FROM Hospital ORDER BY city, name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            ResultSet rs = stmt.executeQuery(query);
            
            while (rs.next()) {
                Hospital hospital = new Hospital(
                    rs.getInt("hospital_id"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("phone"),
                    rs.getString("city")
                );
                hospitals.add(hospital);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return hospitals;
    }

    // Tüm kullanıcıları getir (basit liste)
    public static List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String query = "SELECT u.*, r.name as role_name FROM User u LEFT JOIN Role r ON u.role_id = r.role_id ORDER BY u.name";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                User u = UserFactory.createUserFromResultSet(rs);
                users.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    // Tüm doktorları getir (isim + uzmanlık)
    public static List<Doctor> getAllDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        String query = "SELECT d.*, u.name, s.name as specialty_name FROM Doctor d JOIN User u ON d.user_id = u.user_id LEFT JOIN Specialty s ON d.specialty_id = s.specialty_id ORDER BY u.name";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Doctor doctor = new Doctor(
                    rs.getInt("doctor_id"),
                    rs.getInt("user_id"),
                    rs.getInt("specialty_id"),
                    rs.getInt("clinic_id"),
                    rs.getInt("hospital_id"),
                    rs.getString("license_no"),
                    rs.getInt("experience"),
                    rs.getString("education"),
                    rs.getDouble("consultation_fee"),
                    rs.getString("name"),
                    rs.getString("specialty_name")
                );
                doctors.add(doctor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    // Doktoru user_id ile getir
    public static Doctor getDoctorByUserId(int userId) {
        String query = "SELECT d.*, u.name, s.name as specialty_name FROM Doctor d JOIN User u ON d.user_id = u.user_id LEFT JOIN Specialty s ON d.specialty_id = s.specialty_id WHERE d.user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Doctor(
                    rs.getInt("doctor_id"),
                    rs.getInt("user_id"),
                    rs.getInt("specialty_id"),
                    rs.getInt("clinic_id"),
                    rs.getInt("hospital_id"),
                    rs.getString("license_no"),
                    rs.getInt("experience"),
                    rs.getString("education"),
                    rs.getDouble("consultation_fee"),
                    rs.getString("name"),
                    rs.getString("specialty_name")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Doktorun uygunluklarını getir
    public static java.util.List<AvailabilityOption> getAvailabilitiesByDoctor(int doctorId) {
        java.util.List<AvailabilityOption> list = new java.util.ArrayList<>();
        String q = "SELECT av.availability_id, d.hospital_id, av.date, av.time_slot, h.name as hospital_name " +
                   "FROM Availability av " +
                   "JOIN Doctor d ON av.doctor_id = d.doctor_id " +
                   "LEFT JOIN Hospital h ON d.hospital_id = h.hospital_id " +
                   "WHERE av.doctor_id = ? AND av.date >= CURDATE() AND av.is_booked = FALSE " +
                   "ORDER BY av.date, av.time_slot";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(q)) {
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                AvailabilityOption ao = new AvailabilityOption(
                    rs.getInt("availability_id"),
                    rs.getInt("hospital_id"),
                    rs.getDate("date"),
                    rs.getString("time_slot"),
                    rs.getString("hospital_name")
                );
                list.add(ao);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Randevuları getir (doktor tarafından)
    public static List<Appointment> getAppointmentsByDoctorId(int doctorId) {
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT a.*, av.date, av.time_slot, d.*, u1.name as doctor_name, u2.name as patient_name " +
                      "FROM Appointment a " +
                      "JOIN Availability av ON a.availability_id = av.availability_id " +
                      "JOIN Doctor d ON a.doctor_id = d.doctor_id " +
                      "JOIN User u1 ON d.user_id = u1.user_id " +
                      "JOIN User u2 ON a.patient_id = u2.user_id " +
                      "WHERE a.doctor_id = ? ORDER BY av.date, av.time_slot";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Appointment appointment = new Appointment(
                    rs.getInt("appointment_id"),
                    rs.getInt("doctor_id"),
                    rs.getInt("patient_id"),
                    rs.getInt("availability_id"),
                    rs.getInt("hospital_id"),
                    rs.getString("status"),
                    rs.getString("notes"),
                    rs.getString("diagnosis"),
                    rs.getString("prescription"),
                    rs.getDate("date"),
                    rs.getString("time_slot"),
                    rs.getString("doctor_name"),
                    rs.getString("patient_name")
                );
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return appointments;
    }

    // Hastanın randevularını getir
    public static List<Appointment> getAppointmentsByPatientId(int patientId) {
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT a.*, av.date, av.time_slot, u.name as doctor_name FROM Appointment a " +
                      "JOIN Availability av ON a.availability_id = av.availability_id " +
                      "JOIN Doctor d ON a.doctor_id = d.doctor_id " +
                      "JOIN User u ON d.user_id = u.user_id " +
                      "WHERE a.patient_id = ? ORDER BY av.date, av.time_slot";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Appointment appointment = new Appointment(
                    rs.getInt("appointment_id"),
                    rs.getInt("doctor_id"),
                    rs.getInt("patient_id"),
                    rs.getInt("availability_id"),
                    rs.getInt("hospital_id"),
                    rs.getString("status"),
                    rs.getString("notes"),
                    rs.getString("diagnosis"),
                    rs.getString("prescription"),
                    rs.getDate("date"),
                    rs.getString("time_slot"),
                    rs.getString("doctor_name"),
                    patientId + ""
                );
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return appointments;
    }

    // Bir availability id'sinin zaten dolu (booked) olup olmadığını kontrol et
    public static boolean isAvailabilityBooked(int availabilityId) {
        String query = "SELECT COUNT(*) as cnt FROM Appointment WHERE availability_id = ? AND status <> 'Cancelled'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, availabilityId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("cnt") > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Yeni randevu oluştur (basit): availability boş ise insert yapar
    public static boolean createAppointment(int doctorId, int patientId, int availabilityId, int hospitalId, String notes) {
        if (isAvailabilityBooked(availabilityId)) return false;

        String insert = "INSERT INTO Appointment (doctor_id, patient_id, availability_id, hospital_id, status, notes) VALUES (?,?,?,?,?,?)";
        String updateAvailability = "UPDATE Availability SET is_booked = TRUE WHERE availability_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insert);
             PreparedStatement updateStmt = conn.prepareStatement(updateAvailability)) {
            stmt.setInt(1, doctorId);
            stmt.setInt(2, patientId);
            stmt.setInt(3, availabilityId);
            stmt.setInt(4, hospitalId);
            stmt.setString(5, "Scheduled");
            stmt.setString(6, notes == null ? "" : notes);
            int rows = stmt.executeUpdate();
            
            if (rows > 0) {
                // Mark availability as booked
                updateStmt.setInt(1, availabilityId);
                updateStmt.executeUpdate();
                
                // Log activity for patient and doctor
                try {
                    logUserActivity(patientId, "AppointmentCreated", "DoctorId=" + doctorId + ", AvailabilityId=" + availabilityId);
                } catch (Exception ignored) {}
                try {
                    logUserActivity(doctorId, "AppointmentBookedForDoctor", "PatientId=" + patientId + ", AvailabilityId=" + availabilityId);
                } catch (Exception ignored) {}
            }
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Tarihi geçmiş randevuları 'Completed' olarak işaretle (gün bazında)
    public static int markPastAppointmentsCompleted() {
        String update = "UPDATE Appointment a JOIN Availability av ON a.availability_id = av.availability_id " +
                        "SET a.status = 'Completed' WHERE a.status <> 'Completed' AND av.date < CURDATE()";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(update)) {
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Doktor için yeni availability ekle (basit doğrulamalar içerir)
    public static boolean addAvailability(int doctorId, LocalDate date, String timeSlot) {
        // Geçmiş tarih olamaz
        if (date.isBefore(LocalDate.now())) return false;
        // Hafta sonu kontrolü (Cumartesi=P, Pazar)
        DayOfWeek dow = date.getDayOfWeek();
        if (dow == DayOfWeek.SATURDAY || dow == DayOfWeek.SUNDAY) return false;
        // timeSlot format: HH:mm-HH:mm
        try {
            String[] parts = timeSlot.split("-");
            LocalTime start = LocalTime.parse(parts[0]);
            LocalTime end = LocalTime.parse(parts[1]);
            // Hastane çalışma saatleri 08:00 - 18:00
            LocalTime workStart = LocalTime.of(8,0);
            LocalTime workEnd = LocalTime.of(18,0);
            if (start.isBefore(workStart) || end.isAfter(workEnd) || !start.isBefore(end)) return false;
        } catch (Exception e) {
            return false;
        }

        String insert = "INSERT INTO Availability (doctor_id, date, time_slot) VALUES (?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insert)) {
            stmt.setInt(1, doctorId);
            stmt.setDate(2, java.sql.Date.valueOf(date));
            stmt.setString(3, timeSlot);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Remove an availability slot (only if not booked)
    public static boolean removeAvailability(int availabilityId) {
        // First check if it's already booked
        if (isAvailabilityBooked(availabilityId)) {
            System.out.println("Cannot remove availability " + availabilityId + " - already booked");
            return false;
        }
        
        String delete = "DELETE FROM Availability WHERE availability_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(delete)) {
            stmt.setInt(1, availabilityId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Kullanıcı parolasını güncelle
    public static boolean updateUserPassword(int userId, String hashedPassword) {
        String update = "UPDATE User SET password = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(update)) {
            stmt.setString(1, hashedPassword);
            stmt.setInt(2, userId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Yeni kullanıcı oluştur (parola önceden hashlenmiş olarak gönderilmeli)
    public static boolean createUser(String name, String email, String hashedPassword, String phone, String address, int roleId) {
        // Basit benzersiz eposta kontrolü
        String check = "SELECT COUNT(*) as cnt FROM User WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement chk = conn.prepareStatement(check)) {
            chk.setString(1, email);
            ResultSet rs = chk.executeQuery();
            if (rs.next() && rs.getInt("cnt") > 0) return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        String insert = "INSERT INTO User (name, email, password, phone, address, role_id) VALUES (?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insert)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, hashedPassword);
            stmt.setString(4, phone);
            stmt.setString(5, address);
            stmt.setInt(6, roleId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update user profile (name, phone, address, roleId)
    public static boolean updateUser(int userId, String name, String phone, String address, Integer roleId) {
        String upd = "UPDATE User SET name = ?, phone = ?, address = ?, role_id = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(upd)) {
            stmt.setString(1, name);
            stmt.setString(2, phone);
            stmt.setString(3, address);
            stmt.setInt(4, roleId == null ? 4 : roleId);
            stmt.setInt(5, userId);
            int r = stmt.executeUpdate();
            return r > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Increment failed login attempts and return new value
    public static int incrementFailedAttempts(int userId) {
        String upd = "UPDATE User SET failed_attempts = COALESCE(failed_attempts,0) + 1 WHERE user_id = ?";
        String sel = "SELECT failed_attempts FROM User WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(upd)) {
                stmt.setInt(1, userId);
                stmt.executeUpdate();
            }
            try (PreparedStatement s = conn.prepareStatement(sel)) {
                s.setInt(1, userId);
                ResultSet rs = s.executeQuery();
                if (rs.next()) return rs.getInt("failed_attempts");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static boolean resetFailedAttempts(int userId) {
        String upd = "UPDATE User SET failed_attempts = 0, locked_until = NULL WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(upd)) {
            stmt.setInt(1, userId);
            int r = stmt.executeUpdate();
            return r > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean setLockUntil(int userId, java.sql.Timestamp until) {
        String upd = "UPDATE User SET locked_until = ? WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(upd)) {
            stmt.setTimestamp(1, until);
            stmt.setInt(2, userId);
            int r = stmt.executeUpdate();
            return r > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static java.sql.Timestamp getLockUntil(int userId) {
        String q = "SELECT locked_until FROM User WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(q)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getTimestamp("locked_until");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Delete user (admins protected)
    public static boolean deleteUser(int userId) {
        // Prevent deleting admin users
        String q = "SELECT r.name as role_name FROM User u JOIN Role r ON u.role_id = r.role_id WHERE u.user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(q)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role_name");
                if (role != null && role.equalsIgnoreCase("admin")) {
                    System.out.println("Attempt to delete admin blocked.");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        String del = "DELETE FROM User WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(del)) {
            stmt.setInt(1, userId);
            int r = stmt.executeUpdate();
            return r > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Hospital CRUD
    public static boolean createHospital(String name, String address, String phone, String city) {
        String ins = "INSERT INTO Hospital (name, address, phone, city) VALUES (?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(ins)) {
            stmt.setString(1, name);
            stmt.setString(2, address);
            stmt.setString(3, phone);
            stmt.setString(4, city);
            int r = stmt.executeUpdate();
            return r > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Create a Patient record for a user
    public static boolean createPatient(int userId, java.sql.Date dateOfBirth, String bloodType, String insuranceNo, String emergencyContact) {
        String ins = "INSERT INTO Patient (user_id, blood_type, date_of_birth, insurance_no, emergency_contact) VALUES (?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(ins)) {
            stmt.setInt(1, userId);
            stmt.setString(2, bloodType);
            stmt.setDate(3, dateOfBirth);
            stmt.setString(4, insuranceNo);
            stmt.setString(5, emergencyContact);
            int r = stmt.executeUpdate();
            return r > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateHospital(int hospitalId, String name, String address, String phone, String city) {
        String upd = "UPDATE Hospital SET name = ?, address = ?, phone = ?, city = ? WHERE hospital_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(upd)) {
            stmt.setString(1, name);
            stmt.setString(2, address);
            stmt.setString(3, phone);
            stmt.setString(4, city);
            stmt.setInt(5, hospitalId);
            int r = stmt.executeUpdate();
            return r > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteHospital(int hospitalId) {
        String del = "DELETE FROM Hospital WHERE hospital_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(del)) {
            stmt.setInt(1, hospitalId);
            int r = stmt.executeUpdate();
            return r > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Integer getRoleIdByName(String roleName) {
        String q = "SELECT role_id FROM Role WHERE LOWER(name) = LOWER(?) LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(q)) {
            stmt.setString(1, roleName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("role_id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Return all role names (for UI dropdowns)
    public static List<String> getAllRoleNames() {
        List<String> roles = new ArrayList<>();
        String q = "SELECT name FROM Role ORDER BY name";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(q)) {
            while (rs.next()) {
                roles.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }

    // Return all specialty names (for UI dropdowns)
    public static List<String> getAllSpecialtyNames() {
        List<String> specs = new ArrayList<>();
        String q = "SELECT name FROM Specialty ORDER BY name";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(q)) {
            while (rs.next()) {
                specs.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return specs;
    }

    // Get specialty ID by name
    public static Integer getSpecialtyIdByName(String specialtyName) {
        String q = "SELECT specialty_id FROM Specialty WHERE LOWER(name) = LOWER(?) LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(q)) {
            stmt.setString(1, specialtyName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("specialty_id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Create doctor (requires user_id and specialty_id)
    public static boolean createDoctor(int userId, int specialtyId, int clinicId, int hospitalId, 
                                        String licenseNo, int experience, String education, double consultationFee) {
        String ins = "INSERT INTO Doctor (user_id, specialty_id, clinic_id, hospital_id, license_no, experience, education, consultation_fee) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(ins)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, specialtyId);
            stmt.setInt(3, clinicId);
            stmt.setInt(4, hospitalId);
            stmt.setString(5, licenseNo);
            stmt.setInt(6, experience);
            stmt.setString(7, education);
            stmt.setDouble(8, consultationFee);
            int r = stmt.executeUpdate();
            return r > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update doctor profile
    public static boolean updateDoctor(int doctorId, int specialtyId, String licenseNo, int experience, String education, double consultationFee) {
        String upd = "UPDATE Doctor SET specialty_id = ?, license_no = ?, experience = ?, education = ?, consultation_fee = ? WHERE doctor_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(upd)) {
            stmt.setInt(1, specialtyId);
            stmt.setString(2, licenseNo);
            stmt.setInt(3, experience);
            stmt.setString(4, education);
            stmt.setDouble(5, consultationFee);
            stmt.setInt(6, doctorId);
            int r = stmt.executeUpdate();
            return r > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Delete doctor
    public static boolean deleteDoctor(int doctorId) {
        String del = "DELETE FROM Doctor WHERE doctor_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(del)) {
            stmt.setInt(1, doctorId);
            int r = stmt.executeUpdate();
            return r > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Get user by user_id
    public static User getUserById(int userId) {
        String q = "SELECT u.*, r.name as role_name FROM User u LEFT JOIN Role r ON u.role_id = r.role_id WHERE u.user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(q)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User u = UserFactory.createUserFromResultSet(rs);
                try {
                    u.setFailedAttempts(rs.getInt("failed_attempts"));
                } catch (SQLException ex) {
                    // column may not exist in older schema
                }
                try {
                    u.setLockedUntil(rs.getTimestamp("locked_until"));
                } catch (SQLException ex) {
                }
                return u;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get user by email
    public static User getUserByEmail(String email) {
        String q = "SELECT u.*, r.name as role_name FROM User u LEFT JOIN Role r ON u.role_id = r.role_id WHERE LOWER(u.email) = LOWER(?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(q)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User u = UserFactory.createUserFromResultSet(rs);
                try { u.setFailedAttempts(rs.getInt("failed_attempts")); } catch (SQLException ex) {}
                try { u.setLockedUntil(rs.getTimestamp("locked_until")); } catch (SQLException ex) {}
                return u;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Password reset token helpers
    public static void ensurePasswordResetTable() {
        String create = "CREATE TABLE IF NOT EXISTS Password_Reset_Token (" +
                "token VARCHAR(64) PRIMARY KEY, " +
                "user_id INT NOT NULL, " +
                "expires_at TIMESTAMP NOT NULL" +
                ")";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(create);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean createPasswordResetToken(int userId, String token, java.sql.Timestamp expiresAt) {
        ensurePasswordResetTable();
        String insert = "REPLACE INTO Password_Reset_Token (token, user_id, expires_at) VALUES (?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insert)) {
            stmt.setString(1, token);
            stmt.setInt(2, userId);
            stmt.setTimestamp(3, expiresAt);
            int r = stmt.executeUpdate();
            return r > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Verify token matches user and not expired. If valid, consume (delete) it and return true.
    public static boolean verifyAndConsumePasswordResetToken(int userId, String token) {
        ensurePasswordResetTable();
        String q = "SELECT expires_at FROM Password_Reset_Token WHERE token = ? AND user_id = ?";
        String del = "DELETE FROM Password_Reset_Token WHERE token = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(q);
             PreparedStatement delStmt = conn.prepareStatement(del)) {
            stmt.setString(1, token);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                java.sql.Timestamp expires = rs.getTimestamp("expires_at");
                if (expires != null && expires.after(new java.sql.Timestamp(System.currentTimeMillis()))) {
                    delStmt.setString(1, token);
                    delStmt.executeUpdate();
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Activity logging helper
    public static void ensureUserActivityTable() {
        String create = "CREATE TABLE IF NOT EXISTS User_Activity (" +
                "activity_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "user_id INT, " +
                "action VARCHAR(100), " +
                "details TEXT, " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(create);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void logUserActivity(int userId, String action, String details) {
        ensureUserActivityTable();
        String insert = "INSERT INTO User_Activity (user_id, action, details) VALUES (?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insert)) {
            stmt.setInt(1, userId);
            stmt.setString(2, action);
            stmt.setString(3, details == null ? "" : details);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Generate simple CSV report: number of appointments per doctor
    public static boolean exportAppointmentsPerDoctorCSV(java.io.File file) {
        String q = "SELECT d.doctor_id, u.name as doctor_name, COUNT(a.appointment_id) as total FROM Doctor d " +
                   "LEFT JOIN User u ON d.user_id = u.user_id LEFT JOIN Appointment a ON d.doctor_id = a.doctor_id " +
                   "GROUP BY d.doctor_id, u.name ORDER BY total DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(q);
             java.io.FileWriter fw = new java.io.FileWriter(file)) {
            fw.write("DoctorId,DoctorName,TotalAppointments\n");
            while (rs.next()) {
                fw.write(rs.getInt("doctor_id") + ",\"" + rs.getString("doctor_name") + "\"," + rs.getInt("total") + "\n");
            }
            fw.flush();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Ensure medical record schema additions exist (last_edited_by, audit table)
    public static void ensureMedicalRecordSchema() {
        String addCol = "ALTER TABLE Medical_Record ADD COLUMN IF NOT EXISTS last_edited_by INT NULL";
        String createAudit = "CREATE TABLE IF NOT EXISTS Medical_Record_Audit (" +
                "audit_id INT PRIMARY KEY AUTO_INCREMENT, " +
                "record_id INT NOT NULL, " +
                "edited_by INT, " +
                "edited_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "old_test_results TEXT, " +
                "old_medications TEXT, " +
                "old_notes TEXT, " +
                "FOREIGN KEY (record_id) REFERENCES Medical_Record(record_id) ON DELETE CASCADE" +
                ")";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            try {
                stmt.execute(addCol);
            } catch (SQLException ignored) {
                // Some MySQL versions don't support IF NOT EXISTS for ADD COLUMN; ignore failures
            }
            try {
                stmt.execute(createAudit);
            } catch (SQLException ignored) {
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get medical records for a patient
    public static List<MedicalRecord> getMedicalRecordsByPatient(int patientId) {
        List<MedicalRecord> list = new ArrayList<>();
        String q = "SELECT * FROM Medical_Record WHERE patient_id = ? ORDER BY record_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(q)) {
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                MedicalRecord mr = new MedicalRecord(
                    rs.getInt("record_id"),
                    rs.getInt("patient_id"),
                    rs.getInt("doctor_id"),
                    rs.getInt("appointment_id"),
                    rs.getInt("hospital_id"),
                    rs.getDate("record_date"),
                    rs.getString("test_results"),
                    rs.getString("medications"),
                    rs.getString("notes"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at"),
                    null  // last_edited_by column doesn't exist in table
                );
                list.add(mr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Fallback: get medical records related to a user. This handles cases where
    // records were stored with a raw user id in patient_id by mistake, or when
    // there is no Patient row yet. First tries proper patient mapping, then
    // falls back to searching raw patient_id = userId or patient_id in
    // (SELECT patient_id FROM Patient WHERE user_id = ?).
    public static List<MedicalRecord> getMedicalRecordsForUser(int userId) {
        List<MedicalRecord> list = new ArrayList<>();
        // Try normal patient lookup first
        Patient p = getPatientByUserId(userId);
        if (p != null) {
            return getMedicalRecordsByPatient(p.getPatientId());
        }

        String q = "SELECT * FROM Medical_Record WHERE patient_id = ? OR patient_id IN (SELECT patient_id FROM Patient WHERE user_id = ?) ORDER BY record_date DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(q)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                MedicalRecord mr = new MedicalRecord(
                    rs.getInt("record_id"),
                    rs.getInt("patient_id"),
                    rs.getInt("doctor_id"),
                    rs.getInt("appointment_id"),
                    rs.getInt("hospital_id"),
                    rs.getDate("record_date"),
                    rs.getString("test_results"),
                    rs.getString("medications"),
                    rs.getString("notes"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at"),
                    null  // last_edited_by column doesn't exist in table
                );
                list.add(mr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Return distinct list of cities from Hospital table
    public static List<String> getAllCities() {
        List<String> cities = new ArrayList<>();
        String q = "SELECT DISTINCT city FROM Hospital WHERE city IS NOT NULL AND city <> '' ORDER BY city";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(q)) {
            while (rs.next()) cities.add(rs.getString("city"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cities;
    }

    // Get hospitals by city
    public static List<Hospital> getHospitalsByCity(String city) {
        List<Hospital> hospitals = new ArrayList<>();
        String q = "SELECT * FROM Hospital WHERE LOWER(city) = LOWER(?) ORDER BY name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(q)) {
            stmt.setString(1, city);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                hospitals.add(new Hospital(
                    rs.getInt("hospital_id"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("phone"),
                    rs.getString("city")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hospitals;
    }

    // Get doctors by specialty and hospital
    public static List<Doctor> getDoctorsBySpecialtyAndHospital(int specialtyId, int hospitalId) {
        List<Doctor> doctors = new ArrayList<>();
        String q = "SELECT d.*, u.name, s.name as specialty_name FROM Doctor d JOIN User u ON d.user_id = u.user_id LEFT JOIN Specialty s ON d.specialty_id = s.specialty_id WHERE d.specialty_id = ? AND d.hospital_id = ? ORDER BY u.name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(q)) {
            stmt.setInt(1, specialtyId);
            stmt.setInt(2, hospitalId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                doctors.add(new Doctor(
                    rs.getInt("doctor_id"),
                    rs.getInt("user_id"),
                    rs.getInt("specialty_id"),
                    rs.getInt("clinic_id"),
                    rs.getInt("hospital_id"),
                    rs.getString("license_no"),
                    rs.getInt("experience"),
                    rs.getString("education"),
                    rs.getDouble("consultation_fee"),
                    rs.getString("name"),
                    rs.getString("specialty_name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    // Check if doctor treated patient (has any non-cancelled appointment)
    public static boolean hasDoctorTreatedPatient(int doctorId, int patientId) {
        String q = "SELECT COUNT(*) as cnt FROM Appointment WHERE doctor_id = ? AND patient_id = ? AND status <> 'Cancelled'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(q)) {
            stmt.setInt(1, doctorId);
            stmt.setInt(2, patientId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("cnt") > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Update a medical record with audit logging
    public static boolean updateMedicalRecord(int recordId, int editingDoctorId, String testResults, String medications, String notes) {
        // Read old values
        String sel = "SELECT test_results, medications, notes FROM Medical_Record WHERE record_id = ?";
        String insertAudit = "INSERT INTO Medical_Record_Audit (record_id, edited_by, old_test_results, old_medications, old_notes) VALUES (?,?,?,?,?)";
        String upd = "UPDATE Medical_Record SET test_results = ?, medications = ?, notes = ?, last_edited_by = ? WHERE record_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement selStmt = conn.prepareStatement(sel);
             PreparedStatement auditStmt = conn.prepareStatement(insertAudit);
             PreparedStatement updStmt = conn.prepareStatement(upd)) {
            conn.setAutoCommit(false);
            selStmt.setInt(1, recordId);
            ResultSet rs = selStmt.executeQuery();
            String oldTest = null, oldMed = null, oldNotes = null;
            if (rs.next()) {
                oldTest = rs.getString("test_results");
                oldMed = rs.getString("medications");
                oldNotes = rs.getString("notes");
            }
            auditStmt.setInt(1, recordId);
            auditStmt.setInt(2, editingDoctorId);
            auditStmt.setString(3, oldTest);
            auditStmt.setString(4, oldMed);
            auditStmt.setString(5, oldNotes);
            auditStmt.executeUpdate();

            updStmt.setString(1, testResults);
            updStmt.setString(2, medications);
            updStmt.setString(3, notes);
            updStmt.setInt(4, editingDoctorId);
            updStmt.setInt(5, recordId);
            int rows = updStmt.executeUpdate();
            conn.commit();
            conn.setAutoCommit(true);
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Create a new medical record
    public static boolean createMedicalRecord(int patientId, int doctorId, int appointmentId, int hospitalId, String testResults, String medications, String notes) {
        String insert = "INSERT INTO Medical_Record (patient_id, doctor_id, appointment_id, hospital_id, record_date, test_results, medications, notes, last_edited_by) VALUES (?, ?, ?, ?, CURDATE(), ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insert)) {
            stmt.setInt(1, patientId);
            stmt.setInt(2, doctorId);
            stmt.setInt(3, appointmentId);
            stmt.setInt(4, hospitalId);
            stmt.setString(5, testResults);
            stmt.setString(6, medications);
            stmt.setString(7, notes);
            stmt.setInt(8, doctorId); // last_edited_by is the creating doctor
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Prevent deletion via app - do not provide delete helper. For safety, provide a method that always returns false.
    public static boolean deleteMedicalRecord(int recordId) {
        // Medical records are immutable per SRS; do not delete.
        System.out.println("Delete attempt blocked for medical record: " + recordId);
        return false;
    }

    // Get all appointments (for manager dashboard)
    public static List<Appointment> getAllAppointments() {
        List<Appointment> appointments = new ArrayList<>();
        // Join Availability to obtain the actual appointment date/time stored in Availability
        String query = "SELECT a.*, av.date AS appointment_date, av.time_slot, u.name as patient_name, u2.name as doctor_name " +
                      "FROM Appointment a " +
                      "LEFT JOIN Availability av ON a.availability_id = av.availability_id " +
                      "LEFT JOIN Patient p ON a.patient_id = p.patient_id " +
                      "LEFT JOIN User u ON p.user_id = u.user_id " +
                      "LEFT JOIN Doctor d ON a.doctor_id = d.doctor_id " +
                      "LEFT JOIN User u2 ON d.user_id = u2.user_id " +
                      "ORDER BY av.date DESC, av.time_slot DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Date apptDate = rs.getDate("appointment_date");
                Appointment apt = new Appointment(
                    rs.getInt("appointment_id"),
                    rs.getInt("doctor_id"),
                    rs.getInt("patient_id"),
                    rs.getInt("availability_id"),
                    rs.getInt("hospital_id"),
                    rs.getString("status"),
                    rs.getString("notes"),
                    rs.getString("diagnosis"),
                    rs.getString("prescription"),
                    apptDate,
                    rs.getString("time_slot"),
                    rs.getString("doctor_name"),
                    rs.getString("patient_name")
                );
                appointments.add(apt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return appointments;
    }

    // Cancel appointment
    public static boolean cancelAppointment(int appointmentId, int cancelledByUserId) {
        System.out.println("=== cancelAppointment Called ===");
        System.out.println("appointmentId: " + appointmentId);
        System.out.println("cancelledByUserId: " + cancelledByUserId);
        
        String getAvailabilityQuery = "SELECT availability_id FROM Appointment WHERE appointment_id = ?";
        String updateQuery = "UPDATE Appointment SET status = ? WHERE appointment_id = ?";
        String updateAvailability = "UPDATE Availability SET is_booked = FALSE WHERE availability_id = ?";
        String auditQuery = "INSERT INTO Audit_Log (user_id, action, table_name, record_id, timestamp) VALUES (?, ?, ?, ?, NOW())";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("Database connection: " + conn);
            
            // Get availability_id first
            int availabilityId = -1;
            try (PreparedStatement getStmt = conn.prepareStatement(getAvailabilityQuery)) {
                getStmt.setInt(1, appointmentId);
                ResultSet rs = getStmt.executeQuery();
                if (rs.next()) {
                    availabilityId = rs.getInt("availability_id");
                    System.out.println("Found availability_id: " + availabilityId);
                } else {
                    System.out.println("No availability_id found for appointment: " + appointmentId);
                }
            }
            
            // Update appointment status
            try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                stmt.setString(1, "Cancelled");
                stmt.setInt(2, appointmentId);
                int updated = stmt.executeUpdate();
                System.out.println("Appointment status updated. Rows affected: " + updated);
            }
            
            // Free up the availability slot
            if (availabilityId > 0) {
                try (PreparedStatement updateStmt = conn.prepareStatement(updateAvailability)) {
                    updateStmt.setInt(1, availabilityId);
                    int updated = updateStmt.executeUpdate();
                    System.out.println("Availability freed. Rows affected: " + updated);
                }
            }
            
            // Log to audit
            try (PreparedStatement auditStmt = conn.prepareStatement(auditQuery)) {
                auditStmt.setInt(1, cancelledByUserId);
                auditStmt.setString(2, "CANCEL_APPOINTMENT");
                auditStmt.setString(3, "Appointment");
                auditStmt.setInt(4, appointmentId);
                auditStmt.executeUpdate();
                System.out.println("Audit log created");
            } catch (SQLException auditEx) {
                System.out.println("Audit log failed (table may not exist): " + auditEx.getMessage());
                // Don't fail the whole operation if audit fails
            }
            
            System.out.println("cancelAppointment SUCCESS");
            return true;
        } catch (SQLException e) {
            System.out.println("cancelAppointment FAILED: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Reactivate a cancelled appointment if the time slot is still available
    public static boolean reactivateAppointment(int appointmentId, int reactivatedByUserId) {
        System.out.println("=== reactivateAppointment Called ===");
        System.out.println("appointmentId: " + appointmentId);
        System.out.println("reactivatedByUserId: " + reactivatedByUserId);
        
        String getAppointmentQuery = "SELECT a.availability_id, av.date, av.is_booked " +
                                    "FROM Appointment a " +
                                    "JOIN Availability av ON a.availability_id = av.availability_id " +
                                    "WHERE a.appointment_id = ? AND a.status = 'Cancelled'";
        String updateQuery = "UPDATE Appointment SET status = ? WHERE appointment_id = ?";
        String updateAvailability = "UPDATE Availability SET is_booked = TRUE WHERE availability_id = ?";
        String auditQuery = "INSERT INTO Audit_Log (user_id, action, table_name, record_id, timestamp) VALUES (?, ?, ?, ?, NOW())";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("Database connection: " + conn);
            
            // Check if appointment exists, is cancelled, and time slot is available
            int availabilityId = -1;
            java.sql.Date appointmentDate = null;
            boolean isBooked = false;
            
            try (PreparedStatement getStmt = conn.prepareStatement(getAppointmentQuery)) {
                getStmt.setInt(1, appointmentId);
                ResultSet rs = getStmt.executeQuery();
                if (rs.next()) {
                    availabilityId = rs.getInt("availability_id");
                    appointmentDate = rs.getDate("date");
                    isBooked = rs.getBoolean("is_booked");
                    System.out.println("Found availability_id: " + availabilityId);
                    System.out.println("Appointment date: " + appointmentDate);
                    System.out.println("Is booked: " + isBooked);
                } else {
                    System.out.println("Appointment not found or not cancelled");
                    return false;
                }
            }
            
            // Check if the time slot is already booked
            if (isBooked) {
                System.out.println("Time slot is already booked by another patient");
                return false;
            }
            
            // Check if the appointment date has not passed (allow today and future dates)
            java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
            System.out.println("Current date: " + currentDate);
            System.out.println("Comparing: appointmentDate=" + appointmentDate + " vs currentDate=" + currentDate);
            
            // Use calendar to compare only dates (not time)
            java.util.Calendar appointmentCal = java.util.Calendar.getInstance();
            appointmentCal.setTime(appointmentDate);
            appointmentCal.set(java.util.Calendar.HOUR_OF_DAY, 0);
            appointmentCal.set(java.util.Calendar.MINUTE, 0);
            appointmentCal.set(java.util.Calendar.SECOND, 0);
            appointmentCal.set(java.util.Calendar.MILLISECOND, 0);
            
            java.util.Calendar currentCal = java.util.Calendar.getInstance();
            currentCal.setTime(currentDate);
            currentCal.set(java.util.Calendar.HOUR_OF_DAY, 0);
            currentCal.set(java.util.Calendar.MINUTE, 0);
            currentCal.set(java.util.Calendar.SECOND, 0);
            currentCal.set(java.util.Calendar.MILLISECOND, 0);
            
            if (appointmentCal.before(currentCal)) {
                System.out.println("Appointment date has passed (is in the past)");
                return false;
            }
            System.out.println("Appointment date is valid (today or future)");
            
            // Reactivate appointment
            try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                stmt.setString(1, "Scheduled");
                stmt.setInt(2, appointmentId);
                int updated = stmt.executeUpdate();
                System.out.println("Appointment reactivated. Rows affected: " + updated);
            }
            
            // Mark availability as booked
            if (availabilityId > 0) {
                try (PreparedStatement updateStmt = conn.prepareStatement(updateAvailability)) {
                    updateStmt.setInt(1, availabilityId);
                    int updated = updateStmt.executeUpdate();
                    System.out.println("Availability marked as booked. Rows affected: " + updated);
                }
            }
            
            // Log to audit
            try (PreparedStatement auditStmt = conn.prepareStatement(auditQuery)) {
                auditStmt.setInt(1, reactivatedByUserId);
                auditStmt.setString(2, "REACTIVATE_APPOINTMENT");
                auditStmt.setString(3, "Appointment");
                auditStmt.setInt(4, appointmentId);
                auditStmt.executeUpdate();
                System.out.println("Audit log created");
            } catch (SQLException auditEx) {
                System.out.println("Audit log failed (table may not exist): " + auditEx.getMessage());
            }
            
            System.out.println("reactivateAppointment SUCCESS");
            return true;
        } catch (SQLException e) {
            System.out.println("reactivateAppointment FAILED: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    // Get a single appointment by ID
    public static Appointment getAppointmentById(int appointmentId) {
        String query = "SELECT a.*, " +
                      "d.name AS doctor_name, " +
                      "p.name AS patient_name, " +
                      "av.time_slot " +
                      "FROM Appointment a " +
                      "LEFT JOIN Doctor d ON a.doctor_id = d.doctor_id " +
                      "LEFT JOIN Patient p ON a.patient_id = p.patient_id " +
                      "LEFT JOIN Availability av ON a.availability_id = av.availability_id " +
                      "WHERE a.appointment_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, appointmentId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Date apptDate = rs.getDate("appointment_date");
                return new Appointment(
                    rs.getInt("appointment_id"),
                    rs.getInt("doctor_id"),
                    rs.getInt("patient_id"),
                    rs.getInt("availability_id"),
                    rs.getInt("hospital_id"),
                    rs.getString("status"),
                    rs.getString("notes"),
                    rs.getString("diagnosis"),
                    rs.getString("prescription"),
                    apptDate,
                    rs.getString("time_slot"),
                    rs.getString("doctor_name"),
                    rs.getString("patient_name")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    // Get cancelled appointment ID for a patient by availability
    public static int getCancelledAppointmentByAvailability(int patientId, int availabilityId) {
        String query = "SELECT appointment_id FROM Appointment " +
                      "WHERE patient_id = ? AND availability_id = ? AND status = 'Cancelled'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, patientId);
            stmt.setInt(2, availabilityId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                int appointmentId = rs.getInt("appointment_id");
                System.out.println("Found cancelled appointment: " + appointmentId);
                return appointmentId;
            }
        } catch (SQLException e) {
            System.out.println("getCancelledAppointmentByAvailability error: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    // Update patient information
    public static boolean updatePatient(int patientId, String name, String phone, String email, String bloodType, String insuranceNo, String emergencyContact) {
        String query = "UPDATE Patient SET blood_type = ?, insurance_no = ?, emergency_contact = ? WHERE patient_id = ?";
        String userQuery = "UPDATE User SET name = ?, phone = ?, email = ? WHERE user_id = (SELECT user_id FROM Patient WHERE patient_id = ?)";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Update User table and capture affected rows
            int userUpdated = 0;
            try (PreparedStatement stmt = conn.prepareStatement(userQuery)) {
                stmt.setString(1, name);
                stmt.setString(2, phone);
                stmt.setString(3, email);
                stmt.setInt(4, patientId);
                userUpdated = stmt.executeUpdate();
            }
            
            // Update Patient table and capture affected rows
            int patientUpdated = 0;
            try (PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setString(1, bloodType);
                stmt.setString(2, insuranceNo);
                stmt.setString(3, emergencyContact);
                stmt.setInt(4, patientId);
                patientUpdated = stmt.executeUpdate();
            }
            
            // Return true only if at least one table row was actually modified
            return (userUpdated > 0) || (patientUpdated > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Change password for any user
    public static boolean changePassword(int userId, String oldPassword, String newPassword) {
        String getQuery = "SELECT password_hash FROM User WHERE user_id = ?";
        String updateQuery = "UPDATE User SET password_hash = ? WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Get current password hash
            String currentHash = null;
            try (PreparedStatement stmt = conn.prepareStatement(getQuery)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    currentHash = rs.getString("password_hash");
                } else {
                    return false; // User not found
                }
            }
            
            // Verify old password
            if (currentHash == null || !PasswordUtil.checkPassword(oldPassword, currentHash)) {
                return false; // Old password incorrect
            }
            
            // Hash new password
            String newHash = PasswordUtil.hashPassword(newPassword);
            
            // Update password
            try (PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
                stmt.setString(1, newHash);
                stmt.setInt(2, userId);
                stmt.executeUpdate();
            }
            
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
