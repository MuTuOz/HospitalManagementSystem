package com.hospitalmanagement;

import javafx.scene.control.Alert;

public class ValidationUtil {

    // Validate non-empty string
    public static boolean validateNotEmpty(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            showError(fieldName + " boş olamaz.");
            return false;
        }
        return true;
    }

    // Validate email format
    public static boolean validateEmail(String email) {
        if (email == null || email.isBlank()) {
            showError("E-posta boş olamaz.");
            return false;
        }
        // Simple regex for email validation
        String emailRegex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";
        if (!email.matches(emailRegex)) {
            showError("Geçerli bir e-posta adresi girin (örn: user@example.com).");
            return false;
        }
        return true;
    }

    // Validate phone format (optional field, but if provided should be valid)
    // SRS-HMS-001.1: Phone number format (e.g., +90 555 123 4567)
    public static boolean validatePhone(String phone) {
        if (phone == null || phone.isBlank()) {
            return true; // Phone is optional
        }
        // Accept +90 format with spaces
        if (!phone.matches("^\\+90\\s?[0-9]{3}\\s?[0-9]{3}\\s?[0-9]{4}$")) {
            showError("Telefon numarası geçersiz. Format: +90 555 123 4567");
            return false;
        }
        return true;
    }

    // Validate integer in range
    public static boolean validateInteger(String value, String fieldName, int minVal, int maxVal) {
        if (value == null || value.isBlank()) {
            showError(fieldName + " boş olamaz.");
            return false;
        }
        try {
            int intVal = Integer.parseInt(value);
            if (intVal < minVal || intVal > maxVal) {
                showError(fieldName + " değeri " + minVal + " ile " + maxVal + " arasında olmalıdır.");
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            showError(fieldName + " bir sayı olmalıdır.");
            return false;
        }
    }

    // Validate double in range
    public static boolean validateDouble(double value, String fieldName, double minVal, double maxVal) {
        if (value < minVal || value > maxVal) {
            showError(fieldName + " değeri " + minVal + " ile " + maxVal + " arasında olmalıdır.");
            return false;
        }
        return true;
    }

    // Validate password strength (min 8 chars, at least one uppercase, one lowercase, one digit, one special char)
    // SRS-HMS-001.1: minimum 8 characters, at least one uppercase letter, one number, and one special character
    public static boolean validatePassword(String password) {
        if (password == null || password.isBlank()) {
            showError("Parola boş olamaz.");
            return false;
        }
        if (password.length() < 8) {
            showError("Parola en az 8 karakter olmalıdır.");
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            showError("Parola en az bir büyük harf içermelidir.");
            return false;
        }
        if (!password.matches(".*[a-z].*")) {
            showError("Parola en az bir küçük harf içermelidir.");
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            showError("Parola en az bir rakam içermelidir.");
            return false;
        }
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            showError("Parola en az bir özel karakter içermelidir (!@#$%^&* vb.).");
            return false;
        }
        return true;
    }

    // Validate address (non-empty, reasonable length)
    public static boolean validateAddress(String address) {
        if (address == null || address.isBlank()) {
            showError("Adres boş olamaz.");
            return false;
        }
        if (address.length() < 5) {
            showError("Adres en az 5 karakter olmalıdır.");
            return false;
        }
        return true;
    }

    // Validate license number format (basic: alphanumeric, 5-20 chars)
    public static boolean validateLicenseNumber(String license) {
        if (license == null || license.isBlank()) {
            showError("Lisans No boş olamaz.");
            return false;
        }
        if (!license.matches("^[A-Za-z0-9]{5,20}$")) {
            showError("Lisans No geçersiz (5-20 alfanümerik karakter).");
            return false;
        }
        return true;
    }

    // Show error alert
    public static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Geçersiz Giriş");
        alert.setHeaderText("Lütfen giriş değerlerini kontrol edin");
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Show error alert with custom title
    public static void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText("Doğrulama Hatası");
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Check if email is valid format
    public static boolean isValidEmail(String email) {
        if (email == null || email.isBlank()) return false;
        String emailRegex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";
        return email.matches(emailRegex);
    }

    // Check if phone is valid format
    public static boolean isValidPhone(String phone) {
        if (phone == null || phone.isBlank()) return false;
        return phone.matches("^[\\d\\s\\-()]{10,15}$");
    }

    // Check if password is valid format
    public static boolean isValidPassword(String password) {
        if (password == null || password.isBlank()) return false;
        if (password.length() < 8) return false;
        if (!password.matches(".*[A-Z].*")) return false;
        if (!password.matches(".*[a-z].*")) return false;
        if (!password.matches(".*\\d.*")) return false;
        return true;
    }

    // Validate insurance number (alphanumeric, 6-20 chars)
    public static boolean isValidInsuranceNo(String ins) {
        if (ins == null || ins.isBlank()) return true; // optional field
        // Allow common separators (space, dash, slash) and slightly wider length
        return ins.matches("^[A-Za-z0-9\\-\\s/]{3,30}$");
    }

    // Validate insurance number with error message (SRS-HMS-003.2 - show only)
    public static boolean validateInsuranceNo(String insuranceNo) {
        if (insuranceNo == null || insuranceNo.isBlank()) {
            return true; // Optional field
        }
        // Relaxed validation - allow common formats
        if (insuranceNo.length() < 3 || insuranceNo.length() > 30) {
            showError("Sigorta numarası 3-30 karakter arasında olmalıdır.");
            return false;
        }
        if (!insuranceNo.matches("^[A-Za-z0-9\\-\\s/]+$")) {
            showError("Sigorta numarası geçersiz karakter içeriyor.");
            return false;
        }
        return true;
    }

    // Validate rating (1-5)
    public static boolean validateRating(int rating) {
        if (rating < 1 || rating > 5) {
            showError("Değerlendirme 1-5 arasında olmalıdır.");
            return false;
        }
        return true;
    }
}
