-- HOSPITAL MANAGEMENT SYSTEM - DDL 
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
    role_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES Role(role_id) ON DELETE RESTRICT ON UPDATE CASCADE,  
    -- arent tablosundaki primary key değeri değişirse, child tablosundaki foreign key değerleri de otomatik olarak güncellenir.
    INDEX idx_email (email),
    INDEX idx_role (role_id)
);

-- Department Tablosu
CREATE TABLE Department (
    dept_id INT PRIMARY KEY AUTO_INCREMENT,
    dept_name VARCHAR(100) NOT NULL UNIQUE,
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
    position VARCHAR(100) NOT NULL,
    hire_date DATE NOT NULL,
    salary DECIMAL(12,2),
    qualification TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    INDEX idx_position (position)
);

-- Doctor Tablosu
CREATE TABLE Doctor (
    doctor_id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL UNIQUE,
    specialty_id INT NOT NULL,
    dept_id INT,
    license_no VARCHAR(50) NOT NULL UNIQUE,
    experience INT DEFAULT 0,
    education TEXT,
    consultation_fee DECIMAL(10,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (specialty_id) REFERENCES Specialty(specialty_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (dept_id) REFERENCES Department(dept_id) ON DELETE SET NULL ON UPDATE CASCADE,
    INDEX idx_specialty (specialty_id),
    INDEX idx_department (dept_id)
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
    availability_id INT NOT NULL,  -- ZORUNLU hale getir
    status ENUM('scheduled', 'completed', 'cancelled', 'rescheduled') DEFAULT 'scheduled',
    notes TEXT,
    diagnosis TEXT,
    prescription TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (patient_id) REFERENCES Patient(patient_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY (availability_id) REFERENCES Availability(availability_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    INDEX idx_doctor (doctor_id),
    INDEX idx_patient (patient_id),
    INDEX idx_status (status),
    UNIQUE KEY unique_availability (availability_id)  -- Aynı slot'a iki randevu olmasın
);

-- Medical_Record Tablosu
CREATE TABLE Medical_Record (
    record_id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id INT NOT NULL,
    doctor_id INT,
    appointment_id INT,
    record_date DATE NOT NULL,
    test_results TEXT,
    medications TEXT,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES Patient(patient_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id) ON DELETE SET NULL ON UPDATE CASCADE,
    FOREIGN KEY (appointment_id) REFERENCES Appointment(appointment_id) ON DELETE SET NULL ON UPDATE CASCADE,
    INDEX idx_patient (patient_id),
    INDEX idx_record_date (record_date)
);

-- Review tablosunu yeniden oluştur
CREATE TABLE Review (
    review_id INT PRIMARY KEY AUTO_INCREMENT,
    patient_id INT NOT NULL,
    doctor_id INT NOT NULL,
    appointment_id INT,
    rating INT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    review_date DATE NOT NULL,
    is_anonymous BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES Patient(patient_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (doctor_id) REFERENCES Doctor(doctor_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (appointment_id) REFERENCES Appointment(appointment_id) ON DELETE SET NULL ON UPDATE CASCADE,
    INDEX idx_doctor (doctor_id),
    INDEX idx_rating (rating)
);