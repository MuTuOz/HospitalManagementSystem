package com.hospitalmanagement;

import com.hospitalmanagement.service.UserService;
import com.hospitalmanagement.service.HospitalManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import java.util.List;
import java.util.Optional;

public class ManagerDashboardController {
    
    // Service layer instances
    private final UserService userService = new UserService();
    private final HospitalManager hospitalManager = new HospitalManager();

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label hospitalLabel;

    @FXML
    private Label statisticsLabel;

    @FXML
    private TableView<Doctor> staffTableView;

    @FXML
    private TableView<Appointment> appointmentTableView;

    @FXML
    private void initialize() {
        welcomeLabel.setText("Hastane Yönetim Dashboard");
        initializeTableColumns();
    }

    @SuppressWarnings("unchecked")
    private void initializeTableColumns() {
        // Staff Table
        if (staffTableView != null && staffTableView.getColumns().size() > 0) {
            ((TableColumn<Doctor, Integer>) staffTableView.getColumns().get(0)).setCellValueFactory(new PropertyValueFactory<>("doctorId"));
            ((TableColumn<Doctor, String>) staffTableView.getColumns().get(1)).setCellValueFactory(new PropertyValueFactory<>("name"));
            ((TableColumn<Doctor, String>) staffTableView.getColumns().get(2)).setCellValueFactory(new PropertyValueFactory<>("email"));
            ((TableColumn<Doctor, String>) staffTableView.getColumns().get(3)).setCellValueFactory(new PropertyValueFactory<>("specialtyName"));
        }

        // Appointment Table
        if (appointmentTableView != null && appointmentTableView.getColumns().size() > 0) {
            ((TableColumn<Appointment, Integer>) appointmentTableView.getColumns().get(0)).setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
            ((TableColumn<Appointment, String>) appointmentTableView.getColumns().get(1)).setCellValueFactory(new PropertyValueFactory<>("doctorName"));
            ((TableColumn<Appointment, String>) appointmentTableView.getColumns().get(2)).setCellValueFactory(new PropertyValueFactory<>("patientName"));
            ((TableColumn<Appointment, String>) appointmentTableView.getColumns().get(3)).setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
            ((TableColumn<Appointment, String>) appointmentTableView.getColumns().get(4)).setCellValueFactory(new PropertyValueFactory<>("timeSlot"));
            ((TableColumn<Appointment, String>) appointmentTableView.getColumns().get(5)).setCellValueFactory(new PropertyValueFactory<>("status"));
        }
    }

    @FXML
    private void loadStaff() {
        System.out.println("Personel yükleniyor...");
        var doctors = DatabaseQuery.getAllDoctors();
        if (doctors.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Hiç personel bulunamadı.", ButtonType.OK);
            a.setHeaderText("Personel");
            a.showAndWait();
            return;
        }
        if (staffTableView != null) {
            staffTableView.setItems(FXCollections.observableArrayList(doctors));
        }
    }

    @FXML
    private void refreshStaff() {
        loadStaff();
    }

    @FXML
    private void addDoctor() {
        var dialog = new javafx.scene.control.Dialog<Boolean>();
        dialog.setTitle("Yeni Doktor Ekle");
        var grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        var nameField = new TextField();
        var licenseField = new TextField();
        var experienceSpinner = new Spinner<Integer>(0, 100, 0);
        var educationField = new TextField();
        var consultationFeeSpinner = new Spinner<Double>(0.0, 10000.0, 100.0);
        var specialtyBox = new ComboBox<String>();
        List<String> specs = DatabaseQuery.getAllSpecialtyNames();
        specialtyBox.getItems().addAll(specs);
        if (!specs.isEmpty()) specialtyBox.getSelectionModel().select(0);

        grid.add(new javafx.scene.control.Label("Ad:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new javafx.scene.control.Label("Lisans No:"), 0, 1);
        grid.add(licenseField, 1, 1);
        grid.add(new javafx.scene.control.Label("Tecrübe (yıl):"), 0, 2);
        grid.add(experienceSpinner, 1, 2);
        grid.add(new javafx.scene.control.Label("Eğitim:"), 0, 3);
        grid.add(educationField, 1, 3);
        grid.add(new javafx.scene.control.Label("Muayene Ücreti:"), 0, 4);
        grid.add(consultationFeeSpinner, 1, 4);
        grid.add(new javafx.scene.control.Label("Uzmanlık:"), 0, 5);
        grid.add(specialtyBox, 1, 5);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResultConverter(bt -> bt == ButtonType.OK);

        DialogUtil.attachOkValidation(dialog, () -> {
            String name = nameField.getText();
            String license = licenseField.getText();
            int experience = experienceSpinner.getValue();
            String education = educationField.getText();
            double fee = consultationFeeSpinner.getValue();
            String specialty = specialtyBox.getSelectionModel().getSelectedItem();
            if (!ValidationUtil.validateNotEmpty(name, "Ad")) return false;
            if (!ValidationUtil.validateLicenseNumber(license)) return false;
            if (!ValidationUtil.validateInteger(String.valueOf(experience), "Tecrübe", 0, 100)) return false;
            if (!ValidationUtil.validateNotEmpty(education, "Eğitim")) return false;
            if (!ValidationUtil.validateDouble(fee, "Muayene Ücreti", 0.0, 10000.0)) return false;
            if (specialty == null) {
                ValidationUtil.showError("Uzmanlık seçmelisiniz.");
                return false;
            }
            return true;
        });

        Optional<Boolean> res = dialog.showAndWait();
        if (res.isPresent() && res.get()) {
            String name = nameField.getText();
            String license = licenseField.getText();
            int experience = experienceSpinner.getValue();
            String education = educationField.getText();
            double fee = consultationFeeSpinner.getValue();
            String specialty = specialtyBox.getSelectionModel().getSelectedItem();
            
            // Validation
            if (!ValidationUtil.validateNotEmpty(name, "Ad")) return;
            if (!ValidationUtil.validateLicenseNumber(license)) return;
            if (!ValidationUtil.validateInteger(String.valueOf(experience), "Tecrübe", 0, 100)) return;
            if (!ValidationUtil.validateNotEmpty(education, "Eğitim")) return;
            if (!ValidationUtil.validateDouble(fee, "Muayene Ücreti", 0.0, 10000.0)) return;
            if (specialty == null) {
                ValidationUtil.showError("Uzmanlık seçmelisiniz.");
                return;
            }
            
            Integer specId = DatabaseQuery.getSpecialtyIdByName(specialty);
            if (specId == null) specId = 1;
            boolean ok = DatabaseQuery.createDoctor(0, specId, 0, 0, license, experience, education, fee);
            if (ok) {
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Doktor başarıyla oluşturuldu.", ButtonType.OK);
                a.showAndWait();
                loadStaff();
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR, "Doktor oluşturulamadı.", ButtonType.OK);
                a.showAndWait();
            }
        }
    }

    @FXML
    private void editDoctor() {
        Doctor sel = staffTableView.getSelectionModel().getSelectedItem();
        if (sel == null) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Düzenlemek için bir doktor seçin.", ButtonType.OK);
            a.showAndWait();
            return;
        }
        var dialog = new javafx.scene.control.Dialog<Boolean>();
        dialog.setTitle("Doktor Düzenle");
        var grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        var nameLabel = new javafx.scene.control.Label(sel.getName());
        var licenseField = new TextField(sel.getLicenseNo() == null ? "" : sel.getLicenseNo());
        var experienceSpinner = new Spinner<Integer>(0, 100, sel.getExperience());
        var educationField = new TextField(sel.getEducation() == null ? "" : sel.getEducation());
        var consultationFeeSpinner = new Spinner<Double>(0.0, 10000.0, sel.getConsultationFee());
        var specialtyBox = new ComboBox<String>();
        List<String> specs = DatabaseQuery.getAllSpecialtyNames();
        specialtyBox.getItems().addAll(specs);
        if (sel.getSpecialtyName() != null) specialtyBox.getSelectionModel().select(sel.getSpecialtyName());

        grid.add(new javafx.scene.control.Label("Ad:"), 0, 0);
        grid.add(nameLabel, 1, 0);
        grid.add(new javafx.scene.control.Label("Lisans No:"), 0, 1);
        grid.add(licenseField, 1, 1);
        grid.add(new javafx.scene.control.Label("Tecrübe (yıl):"), 0, 2);
        grid.add(experienceSpinner, 1, 2);
        grid.add(new javafx.scene.control.Label("Eğitim:"), 0, 3);
        grid.add(educationField, 1, 3);
        grid.add(new javafx.scene.control.Label("Muayene Ücreti:"), 0, 4);
        grid.add(consultationFeeSpinner, 1, 4);
        grid.add(new javafx.scene.control.Label("Uzmanlık:"), 0, 5);
        grid.add(specialtyBox, 1, 5);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResultConverter(bt -> bt == ButtonType.OK);

        DialogUtil.attachOkValidation(dialog, () -> {
            String license = licenseField.getText();
            int experience = experienceSpinner.getValue();
            String education = educationField.getText();
            double fee = consultationFeeSpinner.getValue();
            String specialty = specialtyBox.getSelectionModel().getSelectedItem();
            if (!ValidationUtil.validateLicenseNumber(license)) return false;
            if (!ValidationUtil.validateInteger(String.valueOf(experience), "Tecrübe", 0, 100)) return false;
            if (!ValidationUtil.validateNotEmpty(education, "Eğitim")) return false;
            if (!ValidationUtil.validateDouble(fee, "Muayene Ücreti", 0.0, 10000.0)) return false;
            if (specialty == null) {
                ValidationUtil.showError("Uzmanlık seçmelisiniz.");
                return false;
            }
            return true;
        });

        Optional<Boolean> res = dialog.showAndWait();
        if (res.isPresent() && res.get()) {
            String license = licenseField.getText();
            int experience = experienceSpinner.getValue();
            String education = educationField.getText();
            double fee = consultationFeeSpinner.getValue();
            String specialty = specialtyBox.getSelectionModel().getSelectedItem();
            
            // Validation
            if (!ValidationUtil.validateLicenseNumber(license)) return;
            if (!ValidationUtil.validateInteger(String.valueOf(experience), "Tecrübe", 0, 100)) return;
            if (!ValidationUtil.validateNotEmpty(education, "Eğitim")) return;
            if (!ValidationUtil.validateDouble(fee, "Muayene Ücreti", 0.0, 10000.0)) return;
            if (specialty == null) {
                ValidationUtil.showError("Uzmanlık seçmelisiniz.");
                return;
            }
            
            Integer specId = DatabaseQuery.getSpecialtyIdByName(specialty);
            if (specId == null) specId = sel.getSpecialtyId();
            
            boolean ok = DatabaseQuery.updateDoctor(sel.getDoctorId(), specId, license, experience, education, fee);
            if (ok) {
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Doktor başarıyla güncellendi.", ButtonType.OK);
                a.showAndWait();
                loadStaff();
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR, "Doktor güncellenemedi.", ButtonType.OK);
                a.showAndWait();
            }
        }
    }

    @FXML
    private void deleteDoctor() {
        Doctor sel = staffTableView.getSelectionModel().getSelectedItem();
        if (sel == null) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Silmek için bir doktor seçin.", ButtonType.OK);
            a.showAndWait();
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Doktoru silmek istediğinize emin misiniz?", ButtonType.YES, ButtonType.NO);
        var r = confirm.showAndWait();
        if (r.isPresent() && r.get() == ButtonType.YES) {
            boolean ok = DatabaseQuery.deleteDoctor(sel.getDoctorId());
            if (ok) loadStaff();
            else {
                Alert a = new Alert(Alert.AlertType.ERROR, "Doktor silinemedi.", ButtonType.OK);
                a.showAndWait();
            }
        }
    }

    @FXML
    private void loadAppointments() {
        System.out.println("Randevular yükleniyor...");
        var appointments = DatabaseQuery.getAllAppointments();
        if (appointments.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Hiç randevu bulunamadı.", ButtonType.OK);
            a.setHeaderText("Randevular");
            a.showAndWait();
            return;
        }
        if (appointmentTableView != null) {
            appointmentTableView.setItems(FXCollections.observableArrayList(appointments));
        }
    }

    @FXML
    private void refreshAppointments() {
        loadAppointments();
    }

    @FXML
    private void loadStatistics() {
        System.out.println("İstatistikler yükleniyor...");
        var hospitals = hospitalManager.getAllHospitals();
        var doctors = DatabaseQuery.getAllDoctors();
        String stats = String.format("Toplam Hastane: %d\nToplam Doktor/Personel: %d", hospitals.size(), doctors.size());
        if (statisticsLabel != null) {
            statisticsLabel.setText(stats);
        }
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
        
        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20));
        
        javafx.scene.control.PasswordField tfOldPassword = new javafx.scene.control.PasswordField();
        javafx.scene.control.PasswordField tfNewPassword = new javafx.scene.control.PasswordField();
        javafx.scene.control.PasswordField tfConfirm = new javafx.scene.control.PasswordField();
        
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
}
