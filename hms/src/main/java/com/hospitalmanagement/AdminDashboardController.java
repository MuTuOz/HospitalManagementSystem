package com.hospitalmanagement;

import com.hospitalmanagement.service.UserService;
import com.hospitalmanagement.service.HospitalManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import java.util.List;
import java.util.Optional;

public class AdminDashboardController {
    
    // Service layer instances
    private final UserService userService = new UserService();
    private final HospitalManager hospitalManager = new HospitalManager();

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label subtitleLabel;

    @FXML
    private TableView<Hospital> hospitalTableView;

    @FXML
    private TableView<User> usersTableView;

    @FXML
    private TableView<Doctor> doctorsTableView;

    @FXML
    private Label reportLabel;

    @FXML
    private void initialize() {
        welcomeLabel.setText("Admin Dashboard - Sistem Yönetimi");
        initColumns();
    }

    @SuppressWarnings("unchecked")
    private void initColumns() {
        if (hospitalTableView != null && hospitalTableView.getColumns().size() >= 5) {
            ((TableColumn<Hospital, Integer>) hospitalTableView.getColumns().get(0)).setCellValueFactory(new PropertyValueFactory<>("hospitalId"));
            ((TableColumn<Hospital, String>) hospitalTableView.getColumns().get(1)).setCellValueFactory(new PropertyValueFactory<>("name"));
            ((TableColumn<Hospital, String>) hospitalTableView.getColumns().get(2)).setCellValueFactory(new PropertyValueFactory<>("address"));
            ((TableColumn<Hospital, String>) hospitalTableView.getColumns().get(3)).setCellValueFactory(new PropertyValueFactory<>("phone"));
            ((TableColumn<Hospital, String>) hospitalTableView.getColumns().get(4)).setCellValueFactory(new PropertyValueFactory<>("city"));
        }

        if (usersTableView != null && usersTableView.getColumns().size() >= 4) {
            ((TableColumn<User, Integer>) usersTableView.getColumns().get(0)).setCellValueFactory(new PropertyValueFactory<>("userId"));
            ((TableColumn<User, String>) usersTableView.getColumns().get(1)).setCellValueFactory(new PropertyValueFactory<>("name"));
            ((TableColumn<User, String>) usersTableView.getColumns().get(2)).setCellValueFactory(new PropertyValueFactory<>("email"));
            ((TableColumn<User, String>) usersTableView.getColumns().get(3)).setCellValueFactory(new PropertyValueFactory<>("roleName"));
        }

        if (doctorsTableView != null && doctorsTableView.getColumns().size() >= 3) {
            ((TableColumn<Doctor, Integer>) doctorsTableView.getColumns().get(0)).setCellValueFactory(new PropertyValueFactory<>("doctorId"));
            ((TableColumn<Doctor, String>) doctorsTableView.getColumns().get(1)).setCellValueFactory(new PropertyValueFactory<>("name"));
            ((TableColumn<Doctor, String>) doctorsTableView.getColumns().get(2)).setCellValueFactory(new PropertyValueFactory<>("specialtyName"));
        }
    }

    @FXML
    private void loadHospitals() {
        var hospitals = hospitalManager.getAllHospitals();
        if (hospitals == null || hospitals.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Hiç hastane bulunamadı.", ButtonType.OK);
            a.setHeaderText("Hastaneler");
            a.showAndWait();
            return;
        }
        if (hospitalTableView != null) hospitalTableView.setItems(FXCollections.observableArrayList(hospitals));
    }

    @FXML
    private void refreshHospitals() { loadHospitals(); }

    @FXML
    private void addHospital() {
        var dialog = new javafx.scene.control.Dialog<Hospital>();
        dialog.setTitle("Yeni Hastane Ekle");
        var grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        var nameField = new javafx.scene.control.TextField();
        var addressField = new javafx.scene.control.TextField();
        var phoneField = new javafx.scene.control.TextField();
        var cityField = new javafx.scene.control.TextField();
        grid.add(new javafx.scene.control.Label("Ad:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new javafx.scene.control.Label("Adres:"), 0, 1);
        grid.add(addressField, 1, 1);
        grid.add(new javafx.scene.control.Label("Telefon:"), 0, 2);
        grid.add(phoneField, 1, 2);
        grid.add(new javafx.scene.control.Label("Şehir:"), 0, 3);
        grid.add(cityField, 1, 3);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(javafx.scene.control.ButtonType.OK, javafx.scene.control.ButtonType.CANCEL);
        dialog.setResultConverter(bt -> {
            if (bt == javafx.scene.control.ButtonType.OK) {
                return new Hospital(0, nameField.getText(), addressField.getText(), phoneField.getText(), cityField.getText());
            }
            return null;
        });

        DialogUtil.attachOkValidation(dialog, () -> {
            if (!ValidationUtil.validateNotEmpty(nameField.getText(), "Hastane Adı")) return false;
            if (!ValidationUtil.validateAddress(addressField.getText())) return false;
            if (!ValidationUtil.validatePhone(phoneField.getText())) return false;
            if (!ValidationUtil.validateNotEmpty(cityField.getText(), "Şehir")) return false;
            return true;
        });

        var res = dialog.showAndWait();
        res.ifPresent(h -> {
            boolean ok = hospitalManager.createHospital(h.getName(), h.getAddress(), h.getPhone(), h.getCity());
            if (ok) {
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Hastane başarıyla oluşturuldu.", ButtonType.OK);
                a.showAndWait();
                loadHospitals();
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR, "Hastane oluşturulamadı.", ButtonType.OK);
                a.showAndWait();
            }
        });
    }

    @FXML
    private void editHospital() {
        Hospital sel = hospitalTableView.getSelectionModel().getSelectedItem();
        if (sel == null) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Düzenlemek için bir hastane seçin.", ButtonType.OK);
            a.showAndWait();
            return;
        }
        var dialog = new javafx.scene.control.Dialog<Hospital>();
        dialog.setTitle("Hastane Düzenle");
        var grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        var nameField = new javafx.scene.control.TextField(sel.getName());
        var addressField = new javafx.scene.control.TextField(sel.getAddress());
        var phoneField = new javafx.scene.control.TextField(sel.getPhone());
        var cityField = new javafx.scene.control.TextField(sel.getCity());
        grid.add(new javafx.scene.control.Label("Ad:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new javafx.scene.control.Label("Adres:"), 0, 1);
        grid.add(addressField, 1, 1);
        grid.add(new javafx.scene.control.Label("Telefon:"), 0, 2);
        grid.add(phoneField, 1, 2);
        grid.add(new javafx.scene.control.Label("Şehir:"), 0, 3);
        grid.add(cityField, 1, 3);
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(javafx.scene.control.ButtonType.OK, javafx.scene.control.ButtonType.CANCEL);
        dialog.setResultConverter(bt -> {
            if (bt == javafx.scene.control.ButtonType.OK) {
                return new Hospital(sel.getHospitalId(), nameField.getText(), addressField.getText(), phoneField.getText(), cityField.getText());
            }
            return null;
        });

        DialogUtil.attachOkValidation(dialog, () -> {
            if (!ValidationUtil.validateNotEmpty(nameField.getText(), "Hastane Adı")) return false;
            if (!ValidationUtil.validateAddress(addressField.getText())) return false;
            if (!ValidationUtil.validatePhone(phoneField.getText())) return false;
            if (!ValidationUtil.validateNotEmpty(cityField.getText(), "Şehir")) return false;
            return true;
        });

        var res = dialog.showAndWait();
        res.ifPresent(h -> {
            boolean ok = hospitalManager.updateHospital(h.getHospitalId(), h.getName(), h.getAddress(), h.getPhone(), h.getCity());
            if (ok) {
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Hastane başarıyla güncellendi.", ButtonType.OK);
                a.showAndWait();
                loadHospitals();
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR, "Hastane güncellenemedi.", ButtonType.OK);
                a.showAndWait();
            }
        });
    }

    @FXML
    private void deleteHospital() {
        Hospital sel = hospitalTableView.getSelectionModel().getSelectedItem();
        if (sel == null) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Silmek için bir hastane seçin.", ButtonType.OK);
            a.showAndWait();
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Hastaneyi silmek istediğinize emin misiniz?", ButtonType.YES, ButtonType.NO);
        var r = confirm.showAndWait();
        if (r.isPresent() && r.get() == ButtonType.YES) {
            boolean ok = hospitalManager.deleteHospital(sel.getHospitalId());
            if (ok) loadHospitals();
            else {
                Alert a = new Alert(Alert.AlertType.ERROR, "Hastane silinemedi.", ButtonType.OK);
                a.showAndWait();
            }
        }
    }

    @FXML
    private void loadUsers() {
        var users = userService.getAllUsers();
        if (users == null || users.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Hiç kullanıcı bulunamadı.", ButtonType.OK);
            a.setHeaderText("Kullanıcılar");
            a.showAndWait();
            return;
        }
        if (usersTableView != null) usersTableView.setItems(FXCollections.observableArrayList(users));
    }

    @FXML
    private void refreshUsers() { loadUsers(); }

    @FXML
    private void addUser() {
        var dialog = new javafx.scene.control.Dialog<Boolean>();
        dialog.setTitle("Yeni Kullanıcı Ekle");
        var grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        var nameField = new TextField();
        var emailField = new TextField();
        var phoneField = new TextField();
        var addressField = new TextField();
        var passwordField = new PasswordField();
        var roleBox = new ComboBox<String>();
        List<String> roles = DatabaseQuery.getAllRoleNames();
        roleBox.getItems().addAll(roles);
        if (!roles.isEmpty()) roleBox.getSelectionModel().select(0);

        grid.add(new javafx.scene.control.Label("Ad:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new javafx.scene.control.Label("E-posta:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new javafx.scene.control.Label("Telefon:"), 0, 2);
        grid.add(phoneField, 1, 2);
        grid.add(new javafx.scene.control.Label("Adres:"), 0, 3);
        grid.add(addressField, 1, 3);
        grid.add(new javafx.scene.control.Label("Parola:"), 0, 4);
        grid.add(passwordField, 1, 4);
        grid.add(new javafx.scene.control.Label("Rol:"), 0, 5);
        grid.add(roleBox, 1, 5);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResultConverter(bt -> bt == ButtonType.OK);

        DialogUtil.attachOkValidation(dialog, () -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String addr = addressField.getText();
            String pass = passwordField.getText();
            String roleName = roleBox.getSelectionModel().getSelectedItem();
            if (!ValidationUtil.validateNotEmpty(name, "Ad")) return false;
            if (!ValidationUtil.validateEmail(email)) return false;
            if (!ValidationUtil.validatePhone(phone)) return false;
            if (!ValidationUtil.validateAddress(addr)) return false;
            if (!ValidationUtil.validatePassword(pass)) return false;
            if (roleName == null) {
                ValidationUtil.showError("Rol seçmelisiniz.");
                return false;
            }
            return true;
        });

        Optional<Boolean> res = dialog.showAndWait();
        if (res.isPresent() && res.get()) {
            String name = nameField.getText();
            String email = emailField.getText();
            String phone = phoneField.getText();
            String addr = addressField.getText();
            String pass = passwordField.getText();
            String roleName = roleBox.getSelectionModel().getSelectedItem();
            
            // Validation
            if (!ValidationUtil.validateNotEmpty(name, "Ad")) return;
            if (!ValidationUtil.validateEmail(email)) return;
            if (!ValidationUtil.validatePhone(phone)) return;
            if (!ValidationUtil.validateAddress(addr)) return;
            if (!ValidationUtil.validatePassword(pass)) return;
            if (roleName == null) {
                ValidationUtil.showError("Rol seçmelisiniz.");
                return;
            }
            
            String hashed = PasswordUtil.hashPassword(pass);
            Integer roleId = DatabaseQuery.getRoleIdByName(roleName);
            boolean ok = userService.addUser(name, email, hashed, phone, addr, roleId == null ? 4 : roleId);
            if (ok) {
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Kullanıcı başarıyla oluşturuldu.", ButtonType.OK);
                a.showAndWait();
                loadUsers();
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR, "Kullanıcı oluşturulamadı (e-posta zaten kayıtlı olabilir).", ButtonType.OK);
                a.showAndWait();
            }
        }
    }

    @FXML
    private void editUser() {
        User sel = usersTableView.getSelectionModel().getSelectedItem();
        if (sel == null) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Düzenlemek için bir kullanıcı seçin.", ButtonType.OK);
            a.showAndWait();
            return;
        }
        var dialog = new javafx.scene.control.Dialog<Boolean>();
        dialog.setTitle("Kullanıcı Düzenle");
        var grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        var nameField = new TextField(sel.getName());
        var emailLabel = new javafx.scene.control.Label(sel.getEmail());
        var phoneField = new TextField(sel.getPhone() == null ? "" : sel.getPhone());
        var addressField = new TextField(sel.getAddress() == null ? "" : sel.getAddress());
        var changePass = new CheckBox("Parolayı değiştir");
        var passwordField = new PasswordField();
        passwordField.setDisable(true);
        changePass.selectedProperty().addListener((obs, o, n) -> passwordField.setDisable(!n));
        var roleBox = new ComboBox<String>();
        List<String> roles = DatabaseQuery.getAllRoleNames();
        roleBox.getItems().addAll(roles);
        if (sel.getRoleName() != null) roleBox.getSelectionModel().select(sel.getRoleName());

        grid.add(new javafx.scene.control.Label("Ad:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new javafx.scene.control.Label("E-posta:"), 0, 1);
        grid.add(emailLabel, 1, 1);
        grid.add(new javafx.scene.control.Label("Telefon:"), 0, 2);
        grid.add(phoneField, 1, 2);
        grid.add(new javafx.scene.control.Label("Adres:"), 0, 3);
        grid.add(addressField, 1, 3);
        grid.add(changePass, 0, 4);
        grid.add(passwordField, 1, 4);
        grid.add(new javafx.scene.control.Label("Rol:"), 0, 5);
        grid.add(roleBox, 1, 5);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResultConverter(bt -> bt == ButtonType.OK);

        DialogUtil.attachOkValidation(dialog, () -> {
            String name = nameField.getText();
            String phone = phoneField.getText();
            String addr = addressField.getText();
            String roleName = roleBox.getSelectionModel().getSelectedItem();
            if (!ValidationUtil.validateNotEmpty(name, "Ad")) return false;
            if (!ValidationUtil.validatePhone(phone)) return false;
            if (!ValidationUtil.validateAddress(addr)) return false;
            if (roleName == null) {
                ValidationUtil.showError("Rol seçmelisiniz.");
                return false;
            }
            return true;
        });

        Optional<Boolean> res = dialog.showAndWait();
        if (res.isPresent() && res.get()) {
            String name = nameField.getText();
            String phone = phoneField.getText();
            String addr = addressField.getText();
            String roleName = roleBox.getSelectionModel().getSelectedItem();
            
            // Validation
            if (!ValidationUtil.validateNotEmpty(name, "Ad")) return;
            if (!ValidationUtil.validatePhone(phone)) return;
            if (!ValidationUtil.validateAddress(addr)) return;
            if (roleName == null) {
                ValidationUtil.showError("Rol seçmelisiniz.");
                return;
            }
            
            Integer roleId = DatabaseQuery.getRoleIdByName(roleName);
            boolean ok = userService.updateUser(sel.getUserId(), name, phone, addr, roleId == null ? 4 : roleId);
            if (!ok) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Kullanıcı güncellenemedi.", ButtonType.OK);
                a.showAndWait();
                return;
            }
            if (changePass.isSelected() && !passwordField.getText().isBlank()) {
                if (!ValidationUtil.validatePassword(passwordField.getText())) return;
                String hashed = PasswordUtil.hashPassword(passwordField.getText());
                DatabaseQuery.updateUserPassword(sel.getUserId(), hashed);
            }
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Kullanıcı başarıyla güncellendi.", ButtonType.OK);
            a.showAndWait();
            loadUsers();
        }
    }

    @FXML
    private void deleteUser() {
        User sel = usersTableView.getSelectionModel().getSelectedItem();
        if (sel == null) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Silmek için bir kullanıcı seçin.", ButtonType.OK);
            a.showAndWait();
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Kullanıcıyı silmek istediğinize emin misiniz?", ButtonType.YES, ButtonType.NO);
        var r = confirm.showAndWait();
        if (r.isPresent() && r.get() == ButtonType.YES) {
            boolean ok = userService.removeUser(sel.getUserId());
            if (ok) loadUsers();
            else {
                Alert a = new Alert(Alert.AlertType.ERROR, "Kullanıcı silinemedi (admin kullanıcıları silinemez).", ButtonType.OK);
                a.showAndWait();
            }
        }
    }

    @FXML
    private void loadDoctors() {
        var doctors = DatabaseQuery.getAllDoctors();
        if (doctors == null || doctors.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Hiç doktor bulunamadı.", ButtonType.OK);
            a.setHeaderText("Doktorlar");
            a.showAndWait();
            return;
        }
        if (doctorsTableView != null) doctorsTableView.setItems(FXCollections.observableArrayList(doctors));
    }

    @FXML
    private void refreshDoctors() { loadDoctors(); }

    @FXML
    private void generateReports() {
        var hospitals = hospitalManager.getAllHospitals();
        var doctors = DatabaseQuery.getAllDoctors();
        var users = userService.getAllUsers();
        String report = String.format("Toplam Hastane: %d\nToplam Doktor: %d\nToplam Kullanıcı: %d", hospitals.size(), doctors.size(), users.size());
        if (reportLabel != null) reportLabel.setText(report);
        Alert a = new Alert(Alert.AlertType.INFORMATION, report, ButtonType.OK);
        a.setTitle("Sistem Raporu");
        a.setHeaderText("Kısa Sistem Özeti");
        a.showAndWait();
    }

    @FXML
    private void exportReportsCSV() {
        javafx.stage.FileChooser fc = new javafx.stage.FileChooser();
        fc.setTitle("CSV Dışa Aktar");
        fc.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        java.io.File file = fc.showSaveDialog(null);
        if (file == null) return;
        boolean ok = DatabaseQuery.exportAppointmentsPerDoctorCSV(file);
        if (ok) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Rapor başarıyla kaydedildi: " + file.getAbsolutePath(), ButtonType.OK);
            a.setHeaderText("Başarılı");
            a.showAndWait();
        } else {
            Alert a = new Alert(Alert.AlertType.ERROR, "Rapor kaydedilemedi.", ButtonType.OK);
            a.setHeaderText("Hata");
            a.showAndWait();
        }
    }

    @FXML
    private void handleLogout() {
        try {
            Session.clear();
            GUIManager guiManager = new GUIManager(App.getStage());
            guiManager.switchToLogin();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleChangePassword() {
        var user = Session.getCurrentUser();
        if (user == null) {
            NotificationUtil.showError("Hata", "Oturum bulunamadı.");
            return;
        }
        
        javafx.scene.control.Dialog<Void> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Şifre Değiştir");
        dialog.setHeaderText("Şifrenizi güncelleyin");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20));
        
        PasswordField tfOldPassword = new PasswordField();
        PasswordField tfNewPassword = new PasswordField();
        PasswordField tfConfirm = new PasswordField();
        
        grid.add(new javafx.scene.control.Label("Eski Şifre:"), 0, 0);
        grid.add(tfOldPassword, 1, 0);
        grid.add(new javafx.scene.control.Label("Yeni Şifre:"), 0, 1);
        grid.add(tfNewPassword, 1, 1);
        grid.add(new javafx.scene.control.Label("Yeni Şifre Tekrar:"), 0, 2);
        grid.add(tfConfirm, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        DialogUtil.attachOkValidation(dialog, () -> {
            if (tfOldPassword.getText().isEmpty()) {
                ValidationUtil.showError("Doğrulama", "Eski şifre boş olamaz.");
                return false;
            }
            if (tfNewPassword.getText().isEmpty()) {
                ValidationUtil.showError("Doğrulama", "Yeni şifre boş olamaz.");
                return false;
            }
            if (!tfNewPassword.getText().equals(tfConfirm.getText())) {
                ValidationUtil.showError("Doğrulama", "Yeni şifreler eşleşmiyor.");
                return false;
            }
            if (!ValidationUtil.isValidPassword(tfNewPassword.getText())) {
                ValidationUtil.showError("Doğrulama", "Şifre en az 8 karakter, büyük harf, küçük harf ve sayı içermelidir.");
                return false;
            }
            return true;
        });
        
        var result = dialog.showAndWait();
        if (result.isPresent()) {
            boolean ok = userService.changePassword(user.getUserId(), tfOldPassword.getText(), tfNewPassword.getText());
            if (ok) {
                NotificationUtil.showInfo("Başarı", "Şifre başarıyla değiştirildi.");
            } else {
                NotificationUtil.showError("Hata", "Eski şifre yanlış veya şifre değiştirilemedi.");
            }
        }
    }

    @FXML
    private void handleRestoreBackup() {
        javafx.stage.FileChooser fc = new javafx.stage.FileChooser();
        fc.setTitle("Yedekten Geri Yükle - SQL Dosyası Seçin");
        fc.getExtensionFilters().add(new javafx.stage.FileChooser.ExtensionFilter("SQL Files", "*.sql"));
        java.io.File file = fc.showOpenDialog(null);
        if (file == null) return;
        boolean ok = BackupUtil.restoreFrom(file);
        if (ok) NotificationUtil.showInfo("Restore", "Veritabanı başarıyla geri yüklendi.");
        else NotificationUtil.showError("Restore Hatası", "Yedekten geri yükleme başarısız. Logları kontrol edin.");
    }
}
