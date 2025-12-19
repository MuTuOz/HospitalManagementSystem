package com.hospitalmanagement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
  STP deki sıraya göre yapılacak
 */
@DisplayName("User Sınıfı Test Senaryoları")
class UserTest {

    private User testUser;

    @BeforeEach
    void setUp() {
        // Her test öncesi yeni bir User nesnesi oluştur
        testUser = new User(
                1,
                "Dr. Ahmet Yılmaz",
                "ahmet.yilmaz@hospital.com",
                "pass123",
                "5551234567",
                "İstanbul, Türkiye",
                2,
                "Doctor");
    }

    @Test
    @DisplayName("TC-001: User nesnesi oluşturma ve temel alanları doğrulama testi")
    void testUserCreationAndValidation() {
        // User nesnesi oluşturma kontrolü
        assertNotNull(testUser, "User nesnesi null olmamalı");
        assertEquals(1, testUser.getUserId(), "User ID doğru değil");
        assertEquals("Dr. Ahmet Yılmaz", testUser.getName(), "İsim doğru değil");
        assertEquals("ahmet.yilmaz@hospital.com", testUser.getEmail(), "Email doğru değil");

        // Email format kontrolü
        String email = testUser.getEmail();
        assertTrue(email.contains("@"), "Email @ karakteri içermeli");
        assertTrue(email.contains("."), "Email . karakteri içermeli");
        assertFalse(email.isEmpty(), "Email boş olmamalı");

        // Rol doğrulama
        assertEquals("Doctor", testUser.getRoleName(), "Kullanıcı rolü Doctor olmalı");
        assertEquals(2, testUser.getRoleId(), "Doctor rol ID'si 2 olmalı");
    }

    @Test
    @DisplayName("TC-002: Farklı kullanıcı rolleri oluşturma ve doğrulama testi")
    void testDifferentUserRoles() {
        // Admin kullanıcısı
        User adminUser = new User(
                10,
                "Admin User",
                "admin@hospital.com",
                "admin123",
                "5551111111",
                "Merkez",
                1,
                "Admin");

        assertEquals("Admin", adminUser.getRoleName(), "Admin rolü doğru olmalı");
        assertEquals(1, adminUser.getRoleId(), "Admin rol ID'si 1 olmalı");

        // Patient kullanıcısı
        User patientUser = new User(
                20,
                "Hasta Ayşe",
                "ayse@email.com",
                "patient123",
                "5552222222",
                "İzmir",
                4,
                "Patient");

        assertEquals("Patient", patientUser.getRoleName(), "Patient rolü doğru olmalı");
        assertEquals(4, patientUser.getRoleId(), "Patient rol ID'si 4 olmalı");

        // Şifre güvenlik kontrolü
        assertNotNull(testUser.getPassword(), "Şifre null olmamalı");
        assertFalse(testUser.getPassword().isEmpty(), "Şifre boş olmamalı");
    }
}
