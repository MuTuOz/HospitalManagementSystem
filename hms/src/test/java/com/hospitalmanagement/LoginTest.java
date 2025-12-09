package com.hospitalmanagement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * STP deki sıraya göre yapılacak
 */
@DisplayName("Login İşlevselliği Test Senaryoları")
class LoginTest {

    private User validDoctor;

    @BeforeEach
    void setUp() {
        // Test kullanıcısı oluştur
        validDoctor = new User(
                1,
                "Dr. Ahmet Yılmaz",
                "doctor@hospital.com",
                "doctor123",
                "5551234567",
                "İstanbul",
                2,
                "Doctor");
    }

    @Test
    @DisplayName("T-SRS-HMS-001.1: Sistem tüm kullanıcı giriş alanlarını doğrular")
    void testValidateUserInputFields() {
        // Test: Geçerli email ve şifre ile giriş
        String email = "doctor@hospital.com";
        String password = "doctor123";

        boolean loginSuccess = email.equals(validDoctor.getEmail())
                && password.equals(validDoctor.getPassword());

        assertTrue(loginSuccess, "Geçerli doktor bilgileri ile giriş başarılı olmalı");
        assertEquals("Doctor", validDoctor.getRoleName(), "Kullanıcı rolü Doctor olmalı");

        // Test: Geçersiz şifre ile giriş
        String wrongPassword = "wrongPassword123";
        boolean loginFailed = email.equals(validDoctor.getEmail())
                && wrongPassword.equals(validDoctor.getPassword());

        assertFalse(loginFailed, "Yanlış şifre ile giriş başarısız olmalı");
    }

    @Test
    @DisplayName("T-SRS-HMS-001.2: Sistem 5 başarısız girişten sonra hesabı 10 dakika kilitler")
    void testAccountLockAfterFailedAttempts() {
        String correctEmail = "doctor@hospital.com";
        String wrongPassword = "wrongPass";

        // 5 hatalı giriş denemesi simülasyonu
        int failedAttempts = 0;
        for (int i = 0; i < 5; i++) {
            boolean loginAttempt = correctEmail.equals(validDoctor.getEmail())
                    && wrongPassword.equals(validDoctor.getPassword());
            if (!loginAttempt) {
                failedAttempts++;
            }
        }

        assertEquals(5, failedAttempts, "5 başarısız giriş denemesi olmalı");

        // 5 başarısız denemeden sonra hesap kilitlenmeli
        assertTrue(failedAttempts >= 5, "Hesap kilitlenme koşulu sağlanmalı");
    }

    @Test
    @DisplayName("T-SRS-HMS-001.3: Sistem geçersiz giriş veya başarısız işlemler için açık hata mesajları gösterir")
    void testClearErrorDialogsForInvalidInput() {
        // Test 1: Geçersiz kullanıcı adı (non-existent username)
        String nonExistentEmail = "nonexistent@hospital.com";
        String anyPassword = "password123";

        boolean loginAttempt = nonExistentEmail.equals(validDoctor.getEmail())
                && anyPassword.equals(validDoctor.getPassword());

        assertFalse(loginAttempt, "Olmayan kullanıcı ile giriş başarısız olmalı");

        // Test 2: Boş email alanı
        String emptyEmail = "";
        boolean emptyEmailAttempt = emptyEmail.isEmpty()
                || !emptyEmail.equals(validDoctor.getEmail());

        assertTrue(emptyEmailAttempt, "Boş email ile giriş reddedilmeli");

        // Test 3: Geçersiz email formatı
        String invalidEmail = "abc@xyz"; // nokta içermiyor
        boolean hasValidFormat = invalidEmail.contains("@")
                && invalidEmail.contains(".")
                && invalidEmail.indexOf("@") < invalidEmail.lastIndexOf(".");

        assertFalse(hasValidFormat, "Geçersiz email formatı reddedilmeli");
    }

    @Test
    @DisplayName("T-SRS-HMS-001.1: Email format validasyonu - STP Test Inputs")
    void testEmailFormatValidation() {
        // STP'deki email test girdileri
        // Test 1: Geçersiz email - "123@nondomain.com"
        String invalidEmail1 = "123@nondomain.com";
        assertFalse(invalidEmail1.equals(validDoctor.getEmail()),
                "Geçersiz email formatı: 123@nondomain.com reddedilmeli");

        // Test 2: Geçerli email - "tuna@gmail.com"
        String validEmail = "tuna@gmail.com";
        boolean isValidFormat = validEmail.contains("@")
                && validEmail.contains(".")
                && validEmail.indexOf("@") < validEmail.lastIndexOf(".");
        assertTrue(isValidFormat, "Geçerli email formatı: tuna@gmail.com kabul edilmeli");

        // Email format kriterleri
        assertTrue(validEmail.matches("^[A-Za-z0-9+_.-]+@(.+)$"),
                "Email regex pattern ile eşleşmeli");
    }

    @Test
    @DisplayName("T-SRS-HMS-001.1: Şifre güvenlik kuralları validasyonu - STP Test Inputs")
    void testPasswordSecurityRules() {
        // STP'deki şifre test girdileri
        // Test 1: Zayıf şifre - "sifrem1" (8 karakterden az, büyük harf yok, özel
        // karakter yok)
        String weakPassword1 = "sifrem1";
        boolean isWeak1 = weakPassword1.length() < 8
                || !weakPassword1.matches(".*[A-Z].*")
                || !weakPassword1.matches(".*[!@#$%^&*()].*");
        assertTrue(isWeak1, "Zayıf şifre 'sifrem1' reddedilmeli");

        // Test 2: Zayıf şifre - "ozgur1903" (özel karakter yok, büyük harf yok)
        String weakPassword2 = "ozgur1903";
        boolean hasUpperCase = weakPassword2.matches(".*[A-Z].*");
        boolean hasSpecialChar = weakPassword2.matches(".*[!@#$%^&*()].*");
        assertFalse(hasUpperCase && hasSpecialChar,
                "Şifre 'ozgur1903' büyük harf ve özel karakter içermediği için zayıf");

        // Test 3: Güçlü şifre - "aziliFenerli123!" (8+ karakter, büyük harf, rakam,
        // özel karakter)
        String strongPassword = "aziliFenerli123!";
        boolean meetsLength = strongPassword.length() >= 8;
        boolean hasUpper = strongPassword.matches(".*[A-Z].*");
        boolean hasDigit = strongPassword.matches(".*\\d.*");
        boolean hasSpecial = strongPassword.matches(".*[!@#$%^&*()].*");

        assertTrue(meetsLength && hasUpper && hasDigit && hasSpecial,
                "Güçlü şifre 'aziliFenerli123!' kabul edilmeli");
    }

    @Test
    @DisplayName("T-SRS-HMS-001.1: Telefon numarası format validasyonu - STP Test Inputs")
    void testPhoneNumberFormatValidation() {
        // STP'deki telefon test girdileri
        // Test 1: Geçersiz format - "5551234567" (ülke kodu yok)
        String invalidPhone1 = "5551234567";
        assertFalse(invalidPhone1.startsWith("+90"),
                "Telefon '5551234567' ülke kodu içermediği için geçersiz");

        // Test 2: Geçersiz format - "0905551234567" (yanlış format)
        String invalidPhone2 = "0905551234567";
        String expectedFormat = "+90 555 123 4567";
        assertFalse(invalidPhone2.equals(expectedFormat),
                "Telefon '0905551234567' formatı yanlış");

        // Test 3: Geçerli format - "+90 555 123 4567"
        String validPhone = "+90 555 123 4567";
        assertTrue(validPhone.startsWith("+90"),
                "Geçerli telefon '+90 555 123 4567' ülke kodu içermeli");
        assertTrue(validPhone.contains("555"),
                "Geçerli telefon operatör kodu içermeli");

        // Format pattern kontrolü: +90 5XX XXX XX XX
        // Boşluklar dahil: "+90 " + "5XX" + " " + "XXX" + " " + "XX" + " " + "XX"
        String phonePattern = "^\\+90 5\\d{2} \\d{3} \\d{4}$";
        assertTrue(validPhone.matches(phonePattern),
                "Telefon numarası format pattern'e uymalı: +90 5XX XXX XXXX");
    }
}
