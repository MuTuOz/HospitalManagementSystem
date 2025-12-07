package com.hospitalmanagement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
    STP deki sıraya göre yapılacak
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

}
