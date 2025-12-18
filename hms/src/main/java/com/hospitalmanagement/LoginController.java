package com.hospitalmanagement;

import com.hospitalmanagement.service.UserService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class LoginController {
    
    // Service layer instance
    private final UserService userService = new UserService();

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;
    
    @FXML
    private TextField passwordTextField;
    
    @FXML
    private CheckBox showPasswordCheckBox;

    @FXML
    private Button loginButton;

    @FXML
    private Label errorLabel;

    @FXML
    private ImageView logoImageView;

    @FXML
    private Hyperlink forgotPasswordLink;

    // Login attempt tracking (DB-backed)
    private static final int MAX_ATTEMPTS = 5;
    private static final Duration LOCK_DURATION = Duration.ofMinutes(10);

    @FXML
    private void initialize() {
        // Logo yükleme - static klasöründen
        if (logoImageView != null) {
            try {
                var imageStream = getClass().getResourceAsStream("/static/alplogo.png");
                if (imageStream != null) {
                    logoImageView.setImage(new Image(imageStream));
                } else {
                    System.out.println("Logo dosyası bulunamadı: /static/alplogo.png");
                }
            } catch (Exception e) {
                System.out.println("Logo yüklenemedi: " + e.getMessage());
            }
        }

        // Enter tuşu ile login
        passwordField.setOnAction(e -> handleLogin());

        // Error label'ını başlangıçta gizle
        errorLabel.setVisible(false);
        
        // Bind text fields for show/hide password
        passwordTextField.textProperty().bindBidirectional(passwordField.textProperty());
    }
    
    @FXML
    private void togglePasswordVisibility() {
        if (showPasswordCheckBox.isSelected()) {
            passwordTextField.setVisible(true);
            passwordField.setVisible(false);
        } else {
            passwordField.setVisible(true);
            passwordTextField.setVisible(false);
        }
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = showPasswordCheckBox.isSelected() ? 
                          passwordTextField.getText() : passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Kullanıcı adı ve şifre boş olamaz!");
            return;
        }

        // Basit e-posta format kontrolü (use ValidationUtil)
        if (!ValidationUtil.validateEmail(username)) return;

        // Fetch user from DB
        User user = null;
        try {
            user = userService.findUserByEmail(username);
        } catch (Exception e) {
            e.printStackTrace();
            showError("Veritabanı hatası: " + e.getMessage());
            return;
        }

        if (user == null) {
            showError("Kullanıcı bulunamadı.");
            return;
        }

        // Check DB lock
        try {
            java.sql.Timestamp lockedUntil = user.getLockedUntil();
            if (lockedUntil != null) {
                long millisLeft = lockedUntil.getTime() - System.currentTimeMillis();
                if (millisLeft > 0) {
                    long minutes = (millisLeft / 60000) + 1;
                    showError("Hesap kilitli. Lütfen " + minutes + " dakika sonra tekrar deneyin.");
                    return;
                } else {
                    // lock expired - reset
                    userService.resetFailedAttempts(user.getUserId());
                    user.setFailedAttempts(0);
                    user.setLockedUntil(null);
                }
            }
        } catch (Exception ex) {
            // ignore and proceed
        }

        // Authenticate
        User authed = authenticateUser(username, password);
        if (authed != null) {
            // Success - reset counters
            userService.resetFailedAttempts(authed.getUserId());
            showSuccess("Giriş başarılı! Hoş geldiniz " + authed.getName());
            Session.setCurrentUser(authed);
            redirectToUserDashboard(authed);
        } else {
            // Failed - increment DB counter
            try {
                int attempts = userService.incrementFailedAttempts(user.getUserId());
                if (attempts >= MAX_ATTEMPTS) {
                    java.sql.Timestamp until = new java.sql.Timestamp(System.currentTimeMillis() + LOCK_DURATION.toMillis());
                    userService.setLockUntil(user.getUserId(), until);
                    showError("Çok fazla başarısız giriş. Hesap 10 dakika kilitlendi.");
                } else {
                    showError("Kullanıcı adı veya şifre hatalı! (" + attempts + "/" + MAX_ATTEMPTS + ")");
                }
            } catch (Exception e) {
                e.printStackTrace();
                showError("Giriş sırasında hata oluştu.");
            }
        }
    }

    // DB-backed lock handling used instead of in-memory lock map

    @FXML
    private void handleForgotPassword() {
        // 1) Ask for email
        TextInputDialog emailDialog = new TextInputDialog();
        emailDialog.setTitle("Şifre Sıfırlama");
        emailDialog.setHeaderText("Şifre Sıfırlama - E-posta girin");
        emailDialog.setContentText("Kayıtlı e-posta:");
        DialogUtil.attachOkValidation(emailDialog, () -> {
            String txt = emailDialog.getEditor().getText();
            if (txt == null || txt.isBlank()) {
                ValidationUtil.showError("E-posta boş olamaz.");
                return false;
            }
            if (!ValidationUtil.validateEmail(txt)) {
                ValidationUtil.showError("Geçersiz e-posta formatı.");
                return false;
            }
            return true;
        });
        var eRes = emailDialog.showAndWait();
        if (eRes.isEmpty() || eRes.get().isBlank()) return;
        String email = eRes.get().trim();

        User user = userService.findUserByEmail(email);
        if (user == null) {
            showError("Bu e-posta ile kayıtlı kullanıcı bulunamadı.");
            return;
        }

        // 2) Directly ask for new password (no email verification needed)
        try {
            // Prompt for new password
            Dialog<String> pwdDialog = new Dialog<>();
            pwdDialog.setTitle("Yeni Şifre");
            pwdDialog.setHeaderText("Yeni şifrenizi girin");
            var grid = new javafx.scene.layout.GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            var p1 = new PasswordField();
            var p2 = new PasswordField();
            p1.setPromptText("Yeni şifre");
            p2.setPromptText("Yeni şifre (tekrar)");
            grid.add(new javafx.scene.control.Label("Yeni şifre:"), 0, 0);
            grid.add(p1, 1, 0);
            grid.add(new javafx.scene.control.Label("Tekrar:"), 0, 1);
            grid.add(p2, 1, 1);
            pwdDialog.getDialogPane().setContent(grid);
            pwdDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            pwdDialog.setResultConverter(btn -> {
                if (btn == ButtonType.OK) return p1.getText();
                return null;
            });
            DialogUtil.attachOkValidation(pwdDialog, () -> {
                String a = p1.getText();
                String b = p2.getText();
                if (a == null || a.isBlank() || b == null || b.isBlank()) {
                    ValidationUtil.showError("Her iki şifre alanı da dolu olmalıdır.");
                    return false;
                }
                if (!a.equals(b)) {
                    ValidationUtil.showError("Girilen şifreler uyuşmuyor.");
                    return false;
                }
                if (!ValidationUtil.validatePassword(a)) {
                    return false;
                }
                return true;
            });
            var pwdRes = pwdDialog.showAndWait();
            if (pwdRes.isEmpty() || pwdRes.get() == null) return;
            String newPwd = pwdRes.get();

            // Save plaintext password
            boolean updated = DatabaseQuery.updateUserPassword(user.getUserId(), newPwd);
            if (updated) {
                NotificationUtil.showInfo("Şifre", "Şifreniz başarıyla güncellendi.");
            } else {
                showError("Şifre güncellenirken hata oluştu.");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            showError("Şifre sıfırlama sırasında hata: " + ex.getMessage());
        }
    }

    @FXML
    private void handleRegister() {
        Dialog<java.util.Map<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Kayıt Ol");
        dialog.setHeaderText("Yeni hasta kaydı oluşturun");

        var grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        var nameField = new javafx.scene.control.TextField();
        var emailField = new javafx.scene.control.TextField();
        var passwordField = new javafx.scene.control.PasswordField();
        var phoneField = new javafx.scene.control.TextField();
        var addressField = new javafx.scene.control.TextField();
        var dobField = new javafx.scene.control.TextField();
        dobField.setPromptText("YYYY-MM-DD");
        var insuranceField = new javafx.scene.control.TextField();
        insuranceField.setPromptText("Sigorta No (isteğe bağlı)");

        nameField.setPromptText("Ad Soyad");
        emailField.setPromptText("E-posta");
        passwordField.setPromptText("Şifre");
        phoneField.setPromptText("Telefon");
        addressField.setPromptText("Adres");

        grid.add(new javafx.scene.control.Label("Ad Soyad:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new javafx.scene.control.Label("E-posta:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new javafx.scene.control.Label("Şifre:"), 0, 2);
        grid.add(passwordField, 1, 2);
        grid.add(new javafx.scene.control.Label("Telefon:"), 0, 3);
        grid.add(phoneField, 1, 3);
        grid.add(new javafx.scene.control.Label("Adres:"), 0, 4);
        grid.add(addressField, 1, 4);
        grid.add(new javafx.scene.control.Label("Doğum Tarihi (YYYY-MM-DD):"), 0, 5);
        grid.add(dobField, 1, 5);
        grid.add(new javafx.scene.control.Label("Sigorta No:"), 0, 6);
        grid.add(insuranceField, 1, 6);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                var map = new java.util.HashMap<String, String>();
                map.put("name", nameField.getText());
                map.put("email", emailField.getText());
                map.put("password", passwordField.getText());
                map.put("phone", phoneField.getText());
                map.put("address", addressField.getText());
                return map;
            }
            return null;
        });

        DialogUtil.attachOkValidation(dialog, () -> {
            String n = nameField.getText();
            String em = emailField.getText();
            String pw = passwordField.getText();
            String ph = phoneField.getText();
            String dobTxt = dobField.getText();
            if (!ValidationUtil.validateNotEmpty(n, "Ad Soyad")) return false;
            if (!ValidationUtil.validateEmail(em)) return false;
            if (!ValidationUtil.validatePassword(pw)) return false;
            if (!ValidationUtil.validatePhone(ph)) return false;
            if (dobTxt != null && !dobTxt.isBlank()) {
                try {
                    java.time.LocalDate.parse(dobTxt.trim());
                } catch (Exception ex) {
                    ValidationUtil.showError("Doğum tarihi formatı geçersiz. YYYY-MM-DD şeklinde girin.");
                    return false;
                }
            }
            String ins = insuranceField.getText();
            if (ins != null && !ins.isBlank() && !ValidationUtil.isValidInsuranceNo(ins)) {
                ValidationUtil.showError("Sigorta numarası geçersiz. 6-20 alfanümerik karakter olmalıdır.");
                return false;
            }
            return true;
        });

        var res = dialog.showAndWait();
        if (res.isEmpty() || res.get() == null) return;
        var data = res.get();

        // Validate inputs
        String name = data.get("name");
        String email = data.get("email");
        String pwd = data.get("password");
        String phone = data.get("phone");
        String address = data.get("address");

        if (name == null || name.isBlank() || email == null || email.isBlank() || pwd == null || pwd.isBlank()) {
            showError("Lütfen gerekli alanları doldurun (Ad, E-posta, Şifre).");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            showError("Geçersiz e-posta formatı.");
            return;
        }

        // Password strength: min 8 chars, at least one uppercase, one digit, one special char
        if (!pwd.matches("^(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9]).{8,}$")) {
            showError("Şifre en az 8 karakter, bir büyük harf, bir rakam ve bir özel karakter içermelidir.");
            return;
        }

        if (phone == null || phone.replaceAll("[^0-9]", "").length() < 10) {
            showError("Geçersiz telefon numarası.");
            return;
        }
        // Parse DOB
        java.sql.Date dob = null;
        try {
            if (dobField.getText() != null && !dobField.getText().isBlank()) {
                dob = java.sql.Date.valueOf(dobField.getText().trim());
            }
        } catch (IllegalArgumentException ex) {
            showError("Doğum tarihi formatı geçersiz. YYYY-MM-DD şeklinde girin.");
            return;
        }

        // Find patient role id
        Integer roleId = DatabaseQuery.getRoleIdByName("patient");
        if (roleId == null) {
            // Try capitalized
            roleId = DatabaseQuery.getRoleIdByName("Patient");
        }
        if (roleId == null) roleId = 4; // fallback

        // Save plaintext password
        boolean created = DatabaseQuery.createUser(name, email, pwd, phone, address, roleId);
        if (created) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Kayıt başarılı. Giriş yapılıyor...", ButtonType.OK);
            a.setHeaderText("Başarılı");
            a.showAndWait();
            // Auto-login the new user
            User user = authenticateUser(email, pwd);
            if (user != null) {
                // Ensure Patient row exists
                try {
                    var patient = DatabaseQuery.getPatientByUserId(user.getUserId());
                    if (patient == null) {
                        boolean pcreated = DatabaseQuery.createPatient(user.getUserId(), dob == null ? new java.sql.Date(System.currentTimeMillis()) : dob, null, insuranceField.getText(), null);
                        if (pcreated) System.out.println("Patient kaydı oluşturuldu.");
                    }
                } catch (Exception ex) {
                    System.out.println("Patient kaydı oluşturulurken hata: " + ex.getMessage());
                }
                Session.setCurrentUser(user);
                redirectToUserDashboard(user);
            }
        } else {
            showError("Kayıt başarısız. E-posta zaten kayıtlı olabilir.");
        }
    }

    private User authenticateUser(String username, String password) {
        System.out.println("Login denemesi - Email: " + username); // Debug için

        // Use UserService to fetch user (case-insensitive)
        try {
            User dbUser = userService.findUserByEmail(username);
            if (dbUser == null) {
                System.out.println("Kullanıcı bulunamadı (getUserByEmail).");
                return null;
            }
            String storedPassword = dbUser.getPassword();
            // Plaintext password comparison
            boolean verified = password.equals(storedPassword);
            if (verified) {
                System.out.println("Kullanıcı doğrulandı: " + dbUser.getName());
                return dbUser;
            } else {
                System.out.println("Parola doğrulanamadı.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Veritabanı hatası: " + e.getMessage());
        }
        return null;
    }

    private void redirectToUserDashboard(User user) {
        try {
            // Use GUIManager for scene switching (following UML architecture)
            Stage stage = (Stage) loginButton.getScene().getWindow();
            GUIManager guiManager = new GUIManager(stage);
            
            // Polymorphic dispatch based on user type (instanceof checks)
            guiManager.switchToUserDashboard(user);
            
        } catch (Exception e) {
            e.printStackTrace();
            showError("Sayfa yüklenirken hata oluştu: " + e.getMessage());
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setStyle("-fx-text-fill: #dc3545;");
    }

    private void showSuccess(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.setStyle("-fx-text-fill: #28a745;");
    }
}