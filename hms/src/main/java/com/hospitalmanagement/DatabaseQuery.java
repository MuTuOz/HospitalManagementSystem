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

    public static int getHospitalIdByManagerUserId(int userId) {
        String query = "SELECT hospital_id FROM Manager WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("hospital_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static Hospital getHospitalById(int hospitalId) {
        String query = "SELECT * FROM Hospital WHERE hospital_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, hospitalId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Hospital(
                    rs.getInt("hospital_id"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("phone"),
                    rs.getString("city")
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
        String q = "SELECT av.availability_id, d.doctor_id, d.hospital_id, av.date, av.time_slot, " +
                   "h.name as hospital_name, u.name as doctor_name " +
                   "FROM Availability av " +
                   "JOIN Doctor d ON av.doctor_id = d.doctor_id " +
                   "JOIN User u ON d.user_id = u.user_id " +
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
                    rs.getInt("doctor_id"),
                    rs.getInt("hospital_id"),
                    rs.getDate("date"),
                    rs.getString("time_slot"),
                    rs.getString("hospital_name"),
                    rs.getString("doctor_name")
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
                      "JOIN Patient p ON a.patient_id = p.patient_id " +
                      "JOIN User u2 ON p.user_id = u2.user_id " +
                      "WHERE a.doctor_id = ? AND a.status IN ('scheduled', 'completed') " +
                      "ORDER BY av.date, av.time_slot";
        
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
    // Sadece completed ve scheduled randevuları dolu sayar, cancelled olanları sayar
    public static boolean isAvailabilityBooked(int availabilityId) {
        String query = "SELECT COUNT(*) as cnt FROM Appointment WHERE availability_id = ? AND LOWER(status) IN ('scheduled', 'completed')";
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

        // Check if there is a cancelled appointment for this availability
        String checkCancelled = "SELECT appointment_id FROM Appointment WHERE availability_id = ? AND status = 'cancelled'";
        int existingAppointmentId = -1;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(checkCancelled)) {
            stmt.setInt(1, availabilityId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                existingAppointmentId = rs.getInt("appointment_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (existingAppointmentId != -1) {
            // Update existing cancelled appointment
            String update = "UPDATE Appointment SET patient_id = ?, status = 'scheduled', notes = ?, updated_at = CURRENT_TIMESTAMP WHERE appointment_id = ?";
            String updateAvailability = "UPDATE Availability SET is_booked = TRUE WHERE availability_id = ?";
            
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(update);
                 PreparedStatement updateStmt = conn.prepareStatement(updateAvailability)) {
                
                stmt.setInt(1, patientId);
                stmt.setString(2, notes == null ? "" : notes);
                stmt.setInt(3, existingAppointmentId);
                
                int rows = stmt.executeUpdate();
                
                if (rows > 0) {
                    updateStmt.setInt(1, availabilityId);
                    updateStmt.executeUpdate();
                    
                    // Log activity
                    try {
                        logUserActivity(patientId, "AppointmentCreated", "DoctorId=" + doctorId + ", AvailabilityId=" + availabilityId + " (Reused ApptId=" + existingAppointmentId + ")");
                    } catch (Exception ignored) {}
                    try {
                        logUserActivity(doctorId, "AppointmentBookedForDoctor", "PatientId=" + patientId + ", AvailabilityId=" + availabilityId);
                    } catch (Exception ignored) {}
                }
                return rows > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }

        String insert = "INSERT INTO Appointment (doctor_id, patient_id, availability_id, hospital_id, status, notes) VALUES (?,?,?,?,?,?)";
        String updateAvailability = "UPDATE Availability SET is_booked = TRUE WHERE availability_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insert);
             PreparedStatement updateStmt = conn.prepareStatement(updateAvailability)) {
            stmt.setInt(1, doctorId);
            stmt.setInt(2, patientId);
            stmt.setInt(3, availabilityId);
            stmt.setInt(4, hospitalId);
            stmt.setString(5, "scheduled");
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
        
        // timeSlot format: HH:mm-HH:mm veya HH:mm
        LocalTime startTime;
        try {
            if (timeSlot.contains("-")) {
                String[] parts = timeSlot.split("-");
                startTime = LocalTime.parse(parts[0]);
                LocalTime endTime = LocalTime.parse(parts[1]);
                // Hastane çalışma saatleri 08:00 - 18:00
                LocalTime workStart = LocalTime.of(8,0);
                LocalTime workEnd = LocalTime.of(18,0);
                if (startTime.isBefore(workStart) || endTime.isAfter(workEnd) || !startTime.isBefore(endTime)) return false;
            } else {
                startTime = LocalTime.parse(timeSlot);
                // Hastane çalışma saatleri 08:00 - 18:00
                LocalTime workStart = LocalTime.of(8,0);
                LocalTime workEnd = LocalTime.of(18,0);
                if (startTime.isBefore(workStart) || startTime.isAfter(workEnd.minusMinutes(15))) return false;
            }
        } catch (Exception e) {
            return false;
        }

        String insert = "INSERT INTO Availability (doctor_id, date, time_slot) VALUES (?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insert)) {
            stmt.setInt(1, doctorId);
            stmt.setDate(2, java.sql.Date.valueOf(date));
            stmt.setTime(3, java.sql.Time.valueOf(startTime));
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

    // Create user and return the generated user_id
    public static int createUserAndReturnId(String name, String email, String hashedPassword, String tcNo, String phone, String address, int roleId) {
        // Basit benzersiz eposta kontrolü
        String check = "SELECT COUNT(*) as cnt FROM User WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement chk = conn.prepareStatement(check)) {
            chk.setString(1, email);
            ResultSet rs = chk.executeQuery();
            if (rs.next() && rs.getInt("cnt") > 0) return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }

        String insert = "INSERT INTO User (name, email, password, tc_no, phone, address, role_id) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insert, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, hashedPassword);
            stmt.setString(4, tcNo);
            stmt.setString(5, phone);
            stmt.setString(6, address);
            stmt.setInt(7, roleId);
            int rows = stmt.executeUpdate();
            if (rows > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
        } catch (SQLException e) {
            // If tc_no column is missing, try without it
            if (e.getMessage().contains("Unknown column 'tc_no'")) {
                String insertNoTc = "INSERT INTO User (name, email, password, phone, address, role_id) VALUES (?,?,?,?,?,?)";
                try (Connection conn = DatabaseConnection.getConnection();
                     PreparedStatement stmt = conn.prepareStatement(insertNoTc, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                    stmt.setString(1, name);
                    stmt.setString(2, email);
                    stmt.setString(3, hashedPassword);
                    stmt.setString(4, phone);
                    stmt.setString(5, address);
                    stmt.setInt(6, roleId);
                    int rows = stmt.executeUpdate();
                    if (rows > 0) {
                        ResultSet keys = stmt.getGeneratedKeys();
                        if (keys.next()) {
                            return keys.getInt(1);
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } else {
                e.printStackTrace();
            }
        }
        return -1;
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

    public static boolean updateDoctor(int doctorId, int specialtyId, int hospitalId, String licenseNo, int experience, String education, double consultationFee) {
        String query = "UPDATE Doctor SET specialty_id = ?, hospital_id = ?, license_no = ?, experience = ?, education = ?, consultation_fee = ? WHERE doctor_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, specialtyId);
            stmt.setInt(2, hospitalId);
            stmt.setString(3, licenseNo);
            stmt.setInt(4, experience);
            stmt.setString(5, education);
            stmt.setDouble(6, consultationFee);
            stmt.setInt(7, doctorId);
            int rows = stmt.executeUpdate();
            return rows > 0;
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
    public static boolean createDoctor(int userId, int specialtyId, Integer clinicId, int hospitalId, 
                                        String licenseNo, int experience, String education, double consultationFee) {
        String ins = "INSERT INTO Doctor (user_id, specialty_id, clinic_id, hospital_id, license_no, experience, education, consultation_fee) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(ins)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, specialtyId);
            if (clinicId != null) {
                stmt.setInt(3, clinicId);
            } else {
                stmt.setNull(3, java.sql.Types.INTEGER);
            }
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
    // REMOVED: The DDL does not support these columns/tables.
    public static void ensureMedicalRecordSchema() {
        // No-op to respect the provided DDL
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

    // Get doctors by hospital
    public static List<Doctor> getDoctorsByHospital(int hospitalId) {
        List<Doctor> doctors = new ArrayList<>();
        String q = "SELECT d.*, u.name, s.name as specialty_name FROM Doctor d JOIN User u ON d.user_id = u.user_id LEFT JOIN Specialty s ON d.specialty_id = s.specialty_id WHERE d.hospital_id = ? ORDER BY u.name";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(q)) {
            stmt.setInt(1, hospitalId);
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

    // Update a medical record
    public static boolean updateMedicalRecord(int recordId, int editingDoctorId, String testResults, String medications, String notes) {
        // Removed audit logging and last_edited_by to match DDL
        String upd = "UPDATE Medical_Record SET test_results = ?, medications = ?, notes = ? WHERE record_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(upd)) {
            stmt.setString(1, testResults);
            stmt.setString(2, medications);
            stmt.setString(3, notes);
            stmt.setInt(4, recordId);
            
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Create a new medical record
    public static boolean createMedicalRecord(int patientId, int doctorId, Integer appointmentId, int hospitalId, String testResults, String medications, String notes) {
        String insert = "INSERT INTO Medical_Record (patient_id, doctor_id, appointment_id, hospital_id, record_date, test_results, medications, notes) VALUES (?, ?, ?, ?, CURDATE(), ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insert)) {
            stmt.setInt(1, patientId);
            stmt.setInt(2, doctorId);
            if (appointmentId != null) {
                stmt.setInt(3, appointmentId);
            } else {
                stmt.setNull(3, java.sql.Types.INTEGER);
            }
            stmt.setInt(4, hospitalId);
            stmt.setString(5, testResults);
            stmt.setString(6, medications);
            stmt.setString(7, notes);
            // last_edited_by removed
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

    // Get a single appointment by ID
    public static Appointment getAppointmentById(int appointmentId) {
        String query = "SELECT a.*, " +
                      "u_doctor.name AS doctor_name, " +
                      "u_patient.name AS patient_name, " +
                      "av.time_slot, " +
                      "av.date AS appointment_date " +
                      "FROM Appointment a " +
                      "LEFT JOIN Doctor d ON a.doctor_id = d.doctor_id " +
                      "LEFT JOIN User u_doctor ON d.user_id = u_doctor.user_id " +
                      "LEFT JOIN Patient p ON a.patient_id = p.patient_id " +
                      "LEFT JOIN User u_patient ON p.user_id = u_patient.user_id " +
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

    // Cancel appointment
    public static boolean cancelAppointment(int appointmentId, int cancelledByUserId) {
        System.out.println("=== cancelAppointment Called ===");
        System.out.println("appointmentId: " + appointmentId);
        System.out.println("cancelledByUserId: " + cancelledByUserId);
        
        String checkStatusQuery = "SELECT status FROM Appointment WHERE appointment_id = ?";
        String getAvailabilityQuery = "SELECT availability_id FROM Appointment WHERE appointment_id = ?";
        String updateQuery = "UPDATE Appointment SET status = ? WHERE appointment_id = ?";
        String updateAvailability = "UPDATE Availability SET is_booked = FALSE WHERE availability_id = ?";
        String auditQuery = "INSERT INTO Audit_Log (user_id, action, table_name, record_id, timestamp) VALUES (?, ?, ?, ?, NOW())";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("Database connection: " + conn);
            
            // Check if appointment is completed
            try (PreparedStatement checkStmt = conn.prepareStatement(checkStatusQuery)) {
                checkStmt.setInt(1, appointmentId);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    String status = rs.getString("status");
                    if ("completed".equalsIgnoreCase(status)) {
                        System.out.println("Cannot cancel completed appointment: " + appointmentId);
                        return false;
                    }
                }
            }
            
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
                stmt.setString(1, "cancelled");
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
                stmt.setString(1, "scheduled");
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
        String getQuery = "SELECT password FROM User WHERE user_id = ?";
        String updateQuery = "UPDATE User SET password = ? WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Get current password hash
            String currentHash = null;
            try (PreparedStatement stmt = conn.prepareStatement(getQuery)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    currentHash = rs.getString("password");
                } else {
                    return false; // User not found
                }
            }
            
            // Verify old password - basit karşılaştırma (hash yoksa)
            if (currentHash == null) {
                return false; // User not found
            }
            
            // Eski şifre kontrolü (düz metin)
            if (!oldPassword.equals(currentHash)) {
                return false; // Old password incorrect
            }
            
            // Yeni şifreyi düz metin olarak sakla (hash yok)
            String newHash = newPassword;
            
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

    // ========== REVIEW/RATING METHODS (SRS-HMS-004) ==========
    
    /**
     * Create a review for a doctor after completed appointment
     */
    public static boolean createReview(int patientId, int doctorId, Integer appointmentId,
                                       int hospitalId, int rating, String comment, boolean isAnonymous) {
        String query = "INSERT INTO Review (patient_id, doctor_id, appointment_id, hospital_id, " +
                      "rating, comment, review_date, is_anonymous) VALUES (?, ?, ?, ?, ?, ?, CURDATE(), ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            if (isAnonymous) {
                stmt.setNull(1, Types.INTEGER);
            } else {
                stmt.setInt(1, patientId);
            }
            stmt.setInt(2, doctorId);
            if (appointmentId != null) {
                stmt.setInt(3, appointmentId);
            } else {
                stmt.setNull(3, Types.INTEGER);
            }
            stmt.setInt(4, hospitalId);
            stmt.setInt(5, rating);
            stmt.setString(6, comment);
            stmt.setBoolean(7, isAnonymous);
            
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get all reviews for a specific doctor
     */
    public static List<Review> getReviewsByDoctor(int doctorId) {
        List<Review> reviews = new ArrayList<>();
        String query = "SELECT r.*, u.name as patient_name FROM Review r " +
                      "LEFT JOIN Patient p ON r.patient_id = p.patient_id " +
                      "LEFT JOIN User u ON p.user_id = u.user_id " +
                      "WHERE r.doctor_id = ? ORDER BY r.review_date DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Review review = new Review(
                    rs.getInt("review_id"),
                    rs.getInt("patient_id"),
                    rs.getInt("doctor_id"),
                    rs.getObject("appointment_id") != null ? rs.getInt("appointment_id") : null,
                    rs.getInt("hospital_id"),
                    rs.getInt("rating"),
                    rs.getString("comment"),
                    rs.getDate("review_date"),
                    rs.getBoolean("is_anonymous")
                );
                // Set patient name only if not anonymous
                if (!review.isAnonymous()) {
                    review.setPatientName(rs.getString("patient_name"));
                } else {
                    review.setPatientName("Anonim");
                }
                reviews.add(review);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return reviews;
    }

    /**
     * Get average rating for a doctor
     */
    public static double getAverageRatingForDoctor(int doctorId) {
        String query = "SELECT AVG(rating) as avg_rating FROM Review WHERE doctor_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("avg_rating");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    /**
     * Check if patient has already reviewed an appointment
     */
    public static boolean hasReviewedAppointment(int patientId, int appointmentId) {
        String query = "SELECT COUNT(*) FROM Review WHERE appointment_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setInt(1, appointmentId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Get completed appointments for a patient that haven't been reviewed yet
     */
    public static List<Appointment> getCompletedAppointmentsWithoutReview(int patientId) {
        List<Appointment> appointments = new ArrayList<>();
        String query = "SELECT a.*, d.*, u.name as doctor_name, av.date, av.time_slot, " +
                      "h.name as hospital_name " +
                      "FROM Appointment a " +
                      "JOIN Doctor d ON a.doctor_id = d.doctor_id " +
                      "JOIN User u ON d.user_id = u.user_id " +
                      "JOIN Availability av ON a.availability_id = av.availability_id " +
                      "JOIN Hospital h ON a.hospital_id = h.hospital_id " +
                      "WHERE a.patient_id = ? AND a.status = 'completed' " +
                      "AND NOT EXISTS (SELECT 1 FROM Review r WHERE r.appointment_id = a.appointment_id) " +
                      "ORDER BY av.date DESC";
        
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
                    null, // diagnosis
                    null, // prescription
                    rs.getDate("date"),
                    rs.getTime("time_slot").toString(),
                    rs.getString("doctor_name"),
                    null // patient_name - not in this query
                );
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return appointments;
    }

    // Get hospital by name
    public static Hospital getHospitalByName(String name) {
        String query = "SELECT * FROM Hospital WHERE name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Hospital(
                    rs.getInt("hospital_id"),
                    rs.getString("name"),
                    rs.getString("address"),
                    rs.getString("phone"),
                    rs.getString("city")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get doctors by hospital and specialty name
    public static List<Doctor> getDoctorsByHospitalAndSpecialty(int hospitalId, String specialtyName) {
        List<Doctor> doctors = new ArrayList<>();
        String query = "SELECT d.*, u.name, s.name as specialty_name FROM Doctor d " +
                      "JOIN User u ON d.user_id = u.user_id " +
                      "JOIN Specialty s ON d.specialty_id = s.specialty_id " +
                      "WHERE d.hospital_id = ? AND s.name = ? ORDER BY u.name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, hospitalId);
            stmt.setString(2, specialtyName);
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

    // Get doctor by name
    public static Doctor getDoctorByName(String name) {
        String query = "SELECT d.*, u.name, s.name as specialty_name FROM Doctor d " +
                      "JOIN User u ON d.user_id = u.user_id " +
                      "LEFT JOIN Specialty s ON d.specialty_id = s.specialty_id " +
                      "WHERE u.name = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
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

    // Get available slots for a doctor at a hospital
    public static List<AvailabilityOption> getAvailableSlots(int doctorId, int hospitalId) {
        List<AvailabilityOption> slots = new ArrayList<>();
        String query = "SELECT a.*, h.name as hospital_name, u.name as doctor_name " +
                      "FROM Availability a " +
                      "JOIN Doctor d ON a.doctor_id = d.doctor_id " +
                      "JOIN User u ON d.user_id = u.user_id " +
                      "JOIN Hospital h ON h.hospital_id = ? " +
                      "LEFT JOIN Appointment app ON a.availability_id = app.availability_id AND LOWER(app.status) IN ('scheduled', 'completed', 'rescheduled') " +
                      "WHERE a.doctor_id = ? AND d.hospital_id = ? " +
                      "AND app.appointment_id IS NULL " +
                      "AND a.date >= CURDATE() " +
                      "ORDER BY a.date, a.time_slot";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, hospitalId);
            stmt.setInt(2, doctorId);
            stmt.setInt(3, hospitalId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                slots.add(new AvailabilityOption(
                    rs.getInt("availability_id"),
                    rs.getInt("doctor_id"),
                    hospitalId,
                    rs.getDate("date"),
                    rs.getTime("time_slot").toString(),
                    rs.getString("hospital_name"),
                    rs.getString("doctor_name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slots;
    }

    // Get all available slots for a doctor (across all hospitals)
    public static List<AvailabilityOption> getAllAvailableSlotsForDoctor(int doctorId) {
        List<AvailabilityOption> slots = new ArrayList<>();
        String query = "SELECT a.*, h.name as hospital_name, u.name as doctor_name, d.hospital_id " +
                      "FROM Availability a " +
                      "JOIN Doctor d ON a.doctor_id = d.doctor_id " +
                      "JOIN User u ON d.user_id = u.user_id " +
                      "JOIN Hospital h ON d.hospital_id = h.hospital_id " +
                      "LEFT JOIN Appointment app ON a.availability_id = app.availability_id AND LOWER(app.status) IN ('scheduled', 'completed', 'rescheduled') " +
                      "WHERE a.doctor_id = ? " +
                      "AND app.appointment_id IS NULL " +
                      "AND a.date >= CURDATE() " +
                      "ORDER BY a.date, a.time_slot";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, doctorId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                slots.add(new AvailabilityOption(
                    rs.getInt("availability_id"),
                    rs.getInt("doctor_id"),
                    rs.getInt("hospital_id"),
                    rs.getDate("date"),
                    rs.getTime("time_slot").toString(),
                    rs.getString("hospital_name"),
                    rs.getString("doctor_name")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return slots;
    }

    // Get user by TC number
    public static User getUserByTcNo(String tcNo) {
        String query = "SELECT u.*, r.name as role_name FROM User u LEFT JOIN Role r ON u.role_id = r.role_id WHERE u.tc_no = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, tcNo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new User(
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Search patients by name (partial match)
    public static List<Patient> searchPatientsByName(String namePattern) {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT p.*, u.name, u.email, u.phone FROM Patient p " +
                      "JOIN User u ON p.user_id = u.user_id " +
                      "WHERE LOWER(u.name) LIKE LOWER(?) ORDER BY u.name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + namePattern + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                patients.add(new Patient(
                    rs.getInt("patient_id"),
                    rs.getInt("user_id"),
                    rs.getString("blood_type"),
                    rs.getDate("date_of_birth"),
                    rs.getString("insurance_no"),
                    rs.getString("emergency_contact"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phone")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    // Manager için randevu durumunu güncelle
    public static boolean updateAppointmentDetails(int appointmentId, String diagnosis, String prescription, String notes) {
        String sql = "UPDATE Appointment SET diagnosis = ?, prescription = ?, notes = ? WHERE appointment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, diagnosis);
            stmt.setString(2, prescription);
            stmt.setString(3, notes);
            stmt.setInt(4, appointmentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean updateAppointmentStatus(int appointmentId, String newStatus) {
        String update = "UPDATE Appointment SET status = ? WHERE appointment_id = ?";
        String getAvailabilityId = "SELECT availability_id FROM Appointment WHERE appointment_id = ?";
        String updateAvailability = "UPDATE Availability SET is_booked = ? WHERE availability_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Önce availability_id'yi al
            int availabilityId = -1;
            try (PreparedStatement getStmt = conn.prepareStatement(getAvailabilityId)) {
                getStmt.setInt(1, appointmentId);
                ResultSet rs = getStmt.executeQuery();
                if (rs.next()) {
                    availabilityId = rs.getInt("availability_id");
                }
            }
            
            // Appointment durumunu güncelle
            try (PreparedStatement stmt = conn.prepareStatement(update)) {
                stmt.setString(1, newStatus);
                stmt.setInt(2, appointmentId);
                stmt.executeUpdate();
            }
            
            // Eğer cancelled yapıldıysa, availability'yi serbest bırak (is_booked = 0)
            if (availabilityId != -1 && "cancelled".equalsIgnoreCase(newStatus)) {
                try (PreparedStatement availStmt = conn.prepareStatement(updateAvailability)) {
                    availStmt.setInt(1, 0); // is_booked = 0 (boş)
                    availStmt.setInt(2, availabilityId);
                    availStmt.executeUpdate();
                }
            }
            
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Availability ekleme (hospital_id kolonu olmayan şema için)
    public static boolean addAvailability(int doctorId, java.sql.Date date, String timeSlot) {
        String insert = "INSERT INTO Availability (doctor_id, date, time_slot) VALUES (?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(insert)) {
            stmt.setInt(1, doctorId);
            stmt.setDate(2, date);
            stmt.setString(3, timeSlot);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }}