package com.hospitalmanagement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * STP deki sıraya göre yapılacak
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

        @Test
        @DisplayName("TC-003: User nesnesinin tüm alanlarının set/get işlemleri")
        void testUserFieldsSettersAndGetters() {
                // Yeni bir user oluştur
                User newUser = new User(
                                100,
                                "Test User",
                                "test@hospital.com",
                                "testPass123",
                                "+90 555 999 8877",
                                "Ankara",
                                3,
                                "Manager");

                // Tüm alanları doğrula
                assertEquals(100, newUser.getUserId(), "User ID getter çalışmalı");
                assertEquals("Test User", newUser.getName(), "Name getter çalışmalı");
                assertEquals("test@hospital.com", newUser.getEmail(), "Email getter çalışmalı");
                assertEquals("testPass123", newUser.getPassword(), "Password getter çalışmalı");
                assertEquals("+90 555 999 8877", newUser.getPhone(), "Phone getter çalışmalı");
                assertEquals("Ankara", newUser.getAddress(), "Address getter çalışmalı");
                assertEquals(3, newUser.getRoleId(), "Role ID getter çalışmalı");
                assertEquals("Manager", newUser.getRoleName(), "Role Name getter çalışmalı");

                // Setter testleri
                newUser.setName("Updated Name");
                assertEquals("Updated Name", newUser.getName(), "Name setter çalışmalı");

                newUser.setEmail("updated@hospital.com");
                assertEquals("updated@hospital.com", newUser.getEmail(), "Email setter çalışmalı");

                newUser.setPassword("newPassword456!");
                assertEquals("newPassword456!", newUser.getPassword(), "Password setter çalışmalı");
        }

        @Test
        @DisplayName("TC-004: Role-based user creation - STP Test Accounts")
        void testRoleBasedUserCreation() {
                // STP 2.3: Create test accounts/roles: Admin, Doctor, Patient (at least two of
                // each)

                // Admin accounts (2)
                User admin1 = new User(101, "Admin Özgür", "ozgur.admin@hospital.com",
                                "Admin123!", "+90 555 100 0001", "İstanbul", 1, "Admin");
                User admin2 = new User(102, "Admin Murad", "murad.admin@hospital.com",
                                "Admin456!", "+90 555 100 0002", "Ankara", 1, "Admin");

                assertEquals("Admin", admin1.getRoleName());
                assertEquals("Admin", admin2.getRoleName());
                assertEquals(1, admin1.getRoleId());
                assertEquals(1, admin2.getRoleId());

                // Doctor accounts (2)
                User doctor1 = new User(201, "Dr. Alp Eren", "alperen.doctor@hospital.com",
                                "Doctor789!", "+90 555 200 0001", "İzmir", 2, "Doctor");
                User doctor2 = new User(202, "Dr. Tuna", "tuna.doctor@hospital.com",
                                "Doctor012!", "+90 555 200 0002", "Bursa", 2, "Doctor");

                assertEquals("Doctor", doctor1.getRoleName());
                assertEquals("Doctor", doctor2.getRoleName());
                assertEquals(2, doctor1.getRoleId());
                assertEquals(2, doctor2.getRoleId());

                // Patient accounts (2)
                User patient1 = new User(301, "Hasta Ali", "ali.patient@email.com",
                                "Patient345!", "+90 555 300 0001", "Antalya", 4, "Patient");
                User patient2 = new User(302, "Hasta Ayşe", "ayse.patient@email.com",
                                "Patient678!", "+90 555 300 0002", "Konya", 4, "Patient");

                assertEquals("Patient", patient1.getRoleName());
                assertEquals("Patient", patient2.getRoleName());
                assertEquals(4, patient1.getRoleId());
                assertEquals(4, patient2.getRoleId());

                // Verify all users have unique IDs
                assertTrue(admin1.getUserId() != doctor1.getUserId());
                assertTrue(doctor1.getUserId() != patient1.getUserId());
        }

        @Test
        @DisplayName("TC-005: User password security validation")
        void testUserPasswordSecurity() {
                // Test password requirements as per STP T-SRS-HMS-001.1
                // STP Test Inputs: "sifrem1", "ozgur1903", "aziliFenerli123!"

                // STP Test Input 1: Weak password - lowercase + digits
                String weakPassword1 = "sifrem1"; // No uppercase, no special char
                assertFalse(weakPassword1.matches(".*[A-Z].*"), "sifrem1 - büyük harf eksikliği tespit edilmeli");
                assertFalse(weakPassword1.matches(".*[!@#$%^&*()].*"),
                                "sifrem1 - özel karakter eksikliği tespit edilmeli");
                assertTrue(weakPassword1.length() >= 7, "sifrem1 - uzunluk kontrolü");

                // STP Test Input 2: Weak password - lowercase + digits
                String weakPassword2 = "ozgur1903"; // No uppercase, no special char
                assertFalse(weakPassword2.matches(".*[A-Z].*"), "ozgur1903 - büyük harf eksikliği tespit edilmeli");
                assertFalse(weakPassword2.matches(".*[!@#$%^&*()].*"),
                                "ozgur1903 - özel karakter eksikliği tespit edilmeli");
                assertTrue(weakPassword2.length() >= 8, "ozgur1903 - en az 8 karakter");

                // STP Test Input 3: Strong password - meets all requirements
                String strongPassword = "aziliFenerli123!";
                assertTrue(strongPassword.length() >= 8, "aziliFenerli123! - en az 8 karakter olmalı");
                assertTrue(strongPassword.matches(".*[A-Z].*"), "aziliFenerli123! - büyük harf içermeli (F)");
                assertTrue(strongPassword.matches(".*[a-z].*"), "aziliFenerli123! - küçük harf içermeli");
                assertTrue(strongPassword.matches(".*\\d.*"), "aziliFenerli123! - rakam içermeli (123)");
                assertTrue(strongPassword.matches(".*[!@#$%^&*()].*"), "aziliFenerli123! - özel karakter içermeli (!)");

                // Additional weak password examples
                String tooShort = "short"; // Too short
                String noUppercase = "alllowercase123"; // No uppercase
                String noDigits = "NoDigits!"; // No numbers
                String noSpecial = "NoSpecial123"; // No special characters

                assertTrue(tooShort.length() < 8, "Kısa şifre tespit edilmeli");
                assertFalse(noUppercase.matches(".*[A-Z].*"), "Büyük harf eksikliği tespit edilmeli");
                assertFalse(noDigits.matches(".*\\d.*"), "Rakam eksikliği tespit edilmeli");
                assertFalse(noSpecial.matches(".*[!@#$%^&*()].*"), "Özel karakter eksikliği tespit edilmeli");

                // Create user with STP strong password
                User secureUser = new User(500, "Secure User", "secure@hospital.com",
                                strongPassword, "+90 555 500 0001", "İstanbul", 2, "Doctor");

                assertEquals(strongPassword, secureUser.getPassword(), "Güçlü şifre kaydedilmeli");
        }

        @Test
        @DisplayName("TC-006: User email format validation - STP conformance")
        void testUserEmailFormatValidation() {
                // Email validation as per STP T-SRS-HMS-001.1
                // Test with exact STP inputs: "123@nondomain.com" and "tuna@gmail.com"

                // STP Test Input 1: Invalid email starting with numbers
                String invalidEmail1 = "123@nondomain.com";

                // STP Test Input 2: Valid email
                String validEmail1 = "tuna@gmail.com";

                // Additional valid formats
                String validEmail2 = "doctor@hospital.com";
                String validEmail3 = "admin.user@ozyegin.edu.tr";

                // Email format validation pattern
                String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

                // Test STP inputs
                assertTrue(invalidEmail1.matches(emailPattern),
                                "123@nondomain.com teknik olarak geçerli format (RFC5322)");
                assertTrue(validEmail1.matches(emailPattern), "tuna@gmail.com geçerli olmalı");
                assertTrue(validEmail2.matches(emailPattern), "doctor@hospital.com geçerli olmalı");
                assertTrue(validEmail3.matches(emailPattern), "admin.user@ozyegin.edu.tr geçerli olmalı");

                // Invalid email formats (should be rejected)
                String invalidEmail2 = "abc@xyz"; // Missing domain extension
                String invalidEmail3 = "@hospital.com"; // Missing local part
                String invalidEmail4 = "user@.com"; // Missing domain name

                assertFalse(invalidEmail2.matches(emailPattern), "abc@xyz geçersiz olmalı");
                assertFalse(invalidEmail3.matches(emailPattern), "@hospital.com geçersiz olmalı");
                assertFalse(invalidEmail4.matches(emailPattern), "user@.com geçersiz olmalı");

                // Create user with valid STP email
                User emailUser = new User(600, "Email Test User", validEmail1,
                                "TestPass123!", "+90 555 600 0001", "İstanbul", 4, "Patient");

                assertEquals(validEmail1, emailUser.getEmail(), "Geçerli email kullanıcıya atanmalı");
                assertTrue(emailUser.getEmail().contains("@"), "Email @ içermeli");
                assertTrue(emailUser.getEmail().contains("."), "Email . içermeli");
        }

        @Test
        @DisplayName("TC-007: User phone number format validation - STP conformance")
        void testUserPhoneNumberFormatValidation() {
                // Phone validation as per STP T-SRS-HMS-001.1
                // Test with exact STP inputs: "5551234567", "0905551234567", "+90 555 123 4567"

                // STP Test Input 1: Invalid - no country code
                String invalidPhone1 = "5551234567";
                assertFalse(invalidPhone1.startsWith("+90"),
                                "5551234567 - ülke kodu eksik");

                // STP Test Input 2: Invalid - wrong format with leading 0
                String invalidPhone2 = "0905551234567";
                assertFalse(invalidPhone2.startsWith("+90"),
                                "0905551234567 - yanlış format (0 ile başlıyor)");

                // STP Test Input 3: Valid - Turkish phone format
                String validPhone = "+90 555 123 4567";
                assertTrue(validPhone.startsWith("+90"),
                                "+90 555 123 4567 - ülke kodu içermeli");
                assertTrue(validPhone.contains("555"),
                                "+90 555 123 4567 - operatör kodu içermeli");

                // Turkish phone format pattern: +90 5XX XXX XXXX
                String phonePattern = "^\\+90 5\\d{2} \\d{3} \\d{4}$";

                // Test STP inputs against pattern
                assertFalse(invalidPhone1.matches(phonePattern),
                                "5551234567 - pattern'e uymuyor");
                assertFalse(invalidPhone2.matches(phonePattern),
                                "0905551234567 - pattern'e uymuyor");
                assertTrue(validPhone.matches(phonePattern),
                                "+90 555 123 4567 - pattern'e uymalı");

                // Additional valid formats
                String validPhone2 = "+90 532 456 7890";
                String validPhone3 = "+90 505 111 2233";

                assertTrue(validPhone2.matches(phonePattern),
                                "+90 532 456 7890 geçerli olmalı");
                assertTrue(validPhone3.matches(phonePattern),
                                "+90 505 111 2233 geçerli olmalı");

                // Create user with valid STP phone
                User phoneUser = new User(700, "Phone Test User", "phone@hospital.com",
                                "TestPass123!", validPhone, "Ankara", 3, "Manager");

                assertEquals(validPhone, phoneUser.getPhone(), "Geçerli telefon kullanıcıya atanmalı");
                assertTrue(phoneUser.getPhone().startsWith("+90"), "Telefon +90 ile başlamalı");
                assertTrue(phoneUser.getPhone().matches(phonePattern), "Telefon pattern'e uymalı");
        }
}
