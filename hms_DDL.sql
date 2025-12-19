-- HOSPITAL MANAGEMENT SYSTEM - DDL (Düzeltilmiş)
-- Veritabanı Oluşturma
USE hospital_management_system;

-- Role Tablosu
CREATE TABLE Role (
                      role_id INT PRIMARY KEY AUTO_INCREMENT,
                      name VARCHAR(50) NOT NULL UNIQUE,
                      description TEXT,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- User Tablosu
CREATE TABLE User (
                      user_id INT PRIMARY KEY AUTO_INCREMENT,
                      name VARCHAR(100) NOT NULL,
                      email VARCHAR(100) NOT NULL UNIQUE,
                      password VARCHAR(255) NOT NULL,
                      phone VARCHAR(20),
                      address TEXT,
                      failed_attempts INT DEFAULT 0,
                      locked_until TIMESTAMP NULL,
                      role_id INT NOT NULL,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      FOREIGN KEY (role_id) REFERENCES Role(role_id) ON DELETE RESTRICT ON UPDATE CASCADE,
                      INDEX idx_email (email),
                      INDEX idx_role (role_id)
);

-- Hospital Tablosu
CREATE TABLE Hospital (
                          hospital_id INT PRIMARY KEY AUTO_INCREMENT,
                          name VARCHAR(150) NOT NULL UNIQUE,
                          address TEXT,
                          phone VARCHAR(20),
                          city VARCHAR(100),
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Clinic Tablosu
CREATE TABLE Clinic (
                        clinic_id INT PRIMARY KEY AUTO_INCREMENT,
                        clinic_name VARCHAR(100) NOT NULL UNIQUE,
                        location VARCHAR(100),
                        phone VARCHAR(20),
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Specialty Tablosu
CREATE TABLE Specialty (
                           specialty_id INT PRIMARY KEY AUTO_INCREMENT,
                           name VARCHAR(100) NOT NULL UNIQUE,
                           description TEXT,
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Manager Tablosu
CREATE TABLE Manager (
                         manager_id INT PRIMARY KEY AUTO_INCREMENT,
                         user_id INT NOT NULL UNIQUE,
                         hospital_id INT NOT NULL,
                         position VARCHAR(100) NOT NULL,
                         hire_date DATE NOT NULL,
                         salary DECIMAL(12,2),
                         qualification TEXT,
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
                         FOREIGN KEY (hospital_id) REFERENCES Hospital(hospital_id) ON DELETE CASCADE ON UPDATE CASCADE,
                         INDEX idx_position (position)
);

-- Doctor Tablosu
CREATE TABLE Doctor (
                        doctor_id INT PRIMARY KEY AUTO_INCREMENT,
                        user_id INT NOT NULL UNIQUE,
                        specialty_id INT NOT NULL,
                        clinic_id INT,
                        hospital_id INT NOT NULL,
                        license_no VARCHAR(50) NOT NULL UNIQUE,
                        experience INT DEFAULT 0,
                        education TEXT,
                        consultation_fee DECIMAL(10,2) DEFAULT 0.00,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
                        FOREIGN KEY (specialty_id) REFERENCES Specialty(specialty_id) ON DELETE RESTRICT ON UPDATE CASCADE,
                        FOREIGN KEY (clinic_id) REFERENCES Clinic(clinic_id) ON DELETE SET NULL ON UPDATE CASCADE,
                        FOREIGN KEY (hospital_id) REFERENCES Hospital(hospital_id) ON DELETE CASCADE ON UPDATE CASCADE,
                        INDEX idx_specialty (specialty_id),
                        INDEX idx_clinic (clinic_id)
);

-- Patient Tablosu
CREATE TABLE Patient (
                         patient_id INT PRIMARY KEY AUTO_INCREMENT,
                         user_id INT NOT NULL UNIQUE,
                         blood_type VARCHAR(5),
                         date_of_birth DATE NOT NULL,
                         insurance_no VARCHAR(50),
                         emergency_contact VARCHAR(100),
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
                         INDEX idx_blood_type (blood_type)
);

-- Availability Tablosu
CREATE TABLE Availability (
                              availability_id INT PRIMARY KEY AUTO_INCREMENT,
                              doctor_id INT NOT NULL,
                              date DATE NOT NULL,
                              time_slot TIME NOT NULL,
                              is_booked BOOLEAN DEFAULT FALSE,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id) ON DELETE CASCADE ON UPDATE CASCADE,
                              UNIQUE KEY unique_availability (doctor_id, date, time_slot),
                              INDEX idx_doctor_date (doctor_id, date),
                              INDEX idx_available (is_booked)
);

-- Appointment Tablosu
CREATE TABLE Appointment (
                             appointment_id INT PRIMARY KEY AUTO_INCREMENT,
                             doctor_id INT NOT NULL,
                             patient_id INT NOT NULL,
                             availability_id INT NOT NULL,
                             hospital_id INT NOT NULL,
                             status ENUM('scheduled', 'completed', 'cancelled', 'rescheduled') DEFAULT 'scheduled',
                             notes TEXT,
                             diagnosis TEXT,
                             prescription TEXT,
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id) ON DELETE RESTRICT ON UPDATE CASCADE,
                             FOREIGN KEY (patient_id) REFERENCES Patient(patient_id) ON DELETE RESTRICT ON UPDATE CASCADE,
                             FOREIGN KEY (availability_id) REFERENCES Availability(availability_id) ON DELETE RESTRICT ON UPDATE CASCADE,
                             FOREIGN KEY (hospital_id) REFERENCES Hospital(hospital_id) ON DELETE CASCADE ON UPDATE CASCADE,
                             INDEX idx_doctor (doctor_id),
                             INDEX idx_patient (patient_id),
                             INDEX idx_status (status),
                             UNIQUE KEY unique_availability (availability_id)
);

-- Medical_Record Tablosu
CREATE TABLE Medical_Record (
                                record_id INT PRIMARY KEY AUTO_INCREMENT,
                                patient_id INT NOT NULL,
                                doctor_id INT,
                                appointment_id INT,
                                hospital_id INT NOT NULL,
                                record_date DATE NOT NULL,
                                test_results TEXT,
                                medications TEXT,
                                notes TEXT,
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                FOREIGN KEY (patient_id) REFERENCES Patient(patient_id) ON DELETE CASCADE ON UPDATE CASCADE,
                                FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id) ON DELETE SET NULL ON UPDATE CASCADE,
                                FOREIGN KEY (appointment_id) REFERENCES Appointment(appointment_id) ON DELETE SET NULL ON UPDATE CASCADE,
                                FOREIGN KEY (hospital_id) REFERENCES Hospital(hospital_id) ON DELETE CASCADE ON UPDATE CASCADE,
                                INDEX idx_patient (patient_id),
                                INDEX idx_record_date (record_date)
);

-- Review Tablosu
CREATE TABLE Review (
                        review_id INT PRIMARY KEY AUTO_INCREMENT,
                        patient_id INT,
                        doctor_id INT NOT NULL,
                        appointment_id INT,
                        hospital_id INT NOT NULL,
                        rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
                        comment TEXT,
                        review_date DATE NOT NULL,
                        is_anonymous BOOLEAN DEFAULT FALSE,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (patient_id) REFERENCES Patient(patient_id) ON DELETE CASCADE ON UPDATE CASCADE,
                        FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id) ON DELETE CASCADE ON UPDATE CASCADE,
                        FOREIGN KEY (appointment_id) REFERENCES Appointment(appointment_id) ON DELETE SET NULL ON UPDATE CASCADE,
                        FOREIGN KEY (hospital_id) REFERENCES Hospital(hospital_id) ON DELETE CASCADE ON UPDATE CASCADE,
                        INDEX idx_doctor (doctor_id),
                        INDEX idx_rating (rating)
);

-- User_Activity Tablosu (Kullanıcı aktivite logu)
CREATE TABLE User_Activity (
                               activity_id INT PRIMARY KEY AUTO_INCREMENT,
                               user_id INT NOT NULL,
                               activity_type VARCHAR(50) NOT NULL,
                               description TEXT,
                               ip_address VARCHAR(45),
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
                               INDEX idx_user_id (user_id),
                               INDEX idx_activity_type (activity_type),
                               INDEX idx_created_at (created_at)
);