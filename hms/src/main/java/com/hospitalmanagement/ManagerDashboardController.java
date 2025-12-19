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
import javafx.scene.control.Spinner;
import javafx.stage.Stage;
import java.util.List;
import java.util.Optional;

public class ManagerDashboardController {
    
    // Service layer instances
    private final UserService userService = new UserService();
    private final HospitalManager hospitalManager = new HospitalManager();

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label managerNameLabel;

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
        var user = Session.getCurrentUser();
        if (user != null) {
            if (managerNameLabel != null) {
                managerNameLabel.setText(user.getName());
            }
            // Set hospital name
            int hospitalId = DatabaseQuery.getHospitalIdByManagerUserId(user.getUserId());
            if (hospitalId > 0) {
                Hospital h = DatabaseQuery.getHospitalById(hospitalId);
                if (h != null && hospitalLabel != null) {
                    hospitalLabel.setText(h.getName());
                }
            }
        }
        initializeTableColumns();
        loadStaff();
        loadAppointments();
        loadStatistics();
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
        var user = Session.getCurrentUser();
        if (user == null) return;
        
        int hospitalId = DatabaseQuery.getHospitalIdByManagerUserId(user.getUserId());
        if (hospitalId <= 0) {
            // Fallback if no hospital found (shouldn't happen for valid manager)
            return;
        }

        var doctors = DatabaseQuery.getDoctorsByHospital(hospitalId);
        if (staffTableView != null && doctors != null) {
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
        grid.setPadding(new javafx.geometry.Insets(20));
        
        var nameField = new TextField();
        nameField.setPromptText("Örn: Dr. Ahmet Yılmaz");
        var emailField = new TextField();
        emailField.setPromptText("Örn: ahmet.yilmaz@hospital.com");
        var phoneField = new TextField();
        phoneField.setPromptText("Örn: +90 555 123 4567");
        var passwordField = new PasswordField();
        passwordField.setPromptText("En az 8 karakter, büyük/küçük harf ve sayı");
        var tcField = new TextField();
        tcField.setPromptText("11 haneli TC kimlik no");
        var licenseField = new TextField();
        licenseField.setPromptText("Örn: LIC-123456");
        var experienceSpinner = new Spinner<Integer>(0, 50, 0);
        var educationField = new TextField();
        educationField.setPromptText("Örn: İstanbul Tıp Fakültesi");
        var consultationFeeSpinner = new Spinner<Double>(0.0, 5000.0, 200.0, 50.0);
        
        var specialtyBox = new ComboBox<String>();
        List<String> specs = DatabaseQuery.getAllSpecialtyNames();
        specialtyBox.getItems().addAll(specs);
        if (!specs.isEmpty()) specialtyBox.getSelectionModel().select(0);

        grid.add(new javafx.scene.control.Label("Ad Soyad:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new javafx.scene.control.Label("E-posta:"), 0, 1);
        grid.add(emailField, 1, 1);
        grid.add(new javafx.scene.control.Label("Telefon:"), 0, 2);
        grid.add(phoneField, 1, 2);
        grid.add(new javafx.scene.control.Label("TC Kimlik:"), 0, 3);
        grid.add(tcField, 1, 3);
        grid.add(new javafx.scene.control.Label("Parola:"), 0, 4);
        grid.add(passwordField, 1, 4);
        grid.add(new javafx.scene.control.Label("Lisans No:"), 0, 5);
        grid.add(licenseField, 1, 5);
        grid.add(new javafx.scene.control.Label("Tecrübe (yıl):"), 0, 6);
        grid.add(experienceSpinner, 1, 6);
        grid.add(new javafx.scene.control.Label("Eğitim:"), 0, 7);
        grid.add(educationField, 1, 7);
        grid.add(new javafx.scene.control.Label("Muayene Ücreti:"), 0, 8);
        grid.add(consultationFeeSpinner, 1, 8);
        grid.add(new javafx.scene.control.Label("Uzmanlık:"), 0, 9);
        grid.add(specialtyBox, 1, 9);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResultConverter(bt -> bt == ButtonType.OK);

        DialogUtil.attachOkValidation(dialog, () -> {
            if (!ValidationUtil.validateNotEmpty(nameField.getText(), "Ad Soyad")) return false;
            if (!ValidationUtil.validateEmail(emailField.getText())) return false;
            if (!ValidationUtil.validatePhone(phoneField.getText())) return false;
            if (!ValidationUtil.validateNotEmpty(tcField.getText(), "TC Kimlik")) return false;
            if (!ValidationUtil.validatePassword(passwordField.getText())) return false;
            if (!ValidationUtil.validateNotEmpty(licenseField.getText(), "Lisans No")) return false;
            if (!ValidationUtil.validateNotEmpty(educationField.getText(), "Eğitim")) return false;
            if (specialtyBox.getSelectionModel().getSelectedItem() == null) {
                ValidationUtil.showError("Uzmanlık seçmelisiniz.");
                return false;
            }
            return true;
        });

        Optional<Boolean> res = dialog.showAndWait();
        if (res.isPresent() && res.get()) {
            try {
                var user = Session.getCurrentUser();
                if (user == null) return;
                
                // Get manager's hospital_id from the session or default to 1
                // Since we don't have getManagerByUserId, we'll use hospital_id = 1 as default
                int managerHospitalId = 1;
                
                // 1. Create User
                Integer doctorRoleId = DatabaseQuery.getRoleIdByName("Doctor");
                if (doctorRoleId == null) {
                    NotificationUtil.showError("Hata", "Doctor rolü bulunamadı.");
                    return;
                }
                
                int userId = DatabaseQuery.createUserAndReturnId(
                    nameField.getText(),
                    emailField.getText(),
                    passwordField.getText(),
                    tcField.getText(),
                    phoneField.getText(),
                    "",
                    doctorRoleId
                );
                
                if (userId <= 0) {
                    NotificationUtil.showError("Hata", "Kullanıcı oluşturulamadı.");
                    return;
                }
                
                // 2. Create Doctor
                Integer specialtyId = DatabaseQuery.getSpecialtyIdByName(
                    specialtyBox.getSelectionModel().getSelectedItem()
                );
                
                boolean created = DatabaseQuery.createDoctor(
                    userId,
                    specialtyId != null ? specialtyId : 1,
                    null,
                    managerHospitalId,
                    licenseField.getText(),
                    experienceSpinner.getValue(),
                    educationField.getText(),
                    consultationFeeSpinner.getValue()
                );
                
                if (created) {
                    NotificationUtil.showInfo("Başarılı", "Doktor başarıyla oluşturuldu.");
                    loadStaff();
                } else {
                    NotificationUtil.showError("Hata", "Doktor kaydı oluşturulamadı.");
                }
            } catch (Exception e) {
                e.printStackTrace();
                NotificationUtil.showError("Hata", "Doktor oluşturulurken hata: " + e.getMessage());
            }
        }
    }

    @FXML
    private void editStaff() {
        Doctor sel = staffTableView.getSelectionModel().getSelectedItem();
        if (sel == null) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Düzenlemek için bir personel seçin.", ButtonType.OK);
            a.showAndWait();
            return;
        }
        var dialog = new javafx.scene.control.Dialog<Boolean>();
        dialog.setTitle("Personel Düzenle");
        var grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20));
        
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
            
            Integer specId = DatabaseQuery.getSpecialtyIdByName(specialty);
            if (specId == null) specId = sel.getSpecialtyId();
            
            boolean success = DatabaseQuery.updateDoctor(
                sel.getDoctorId(),
                specId,
                sel.getHospitalId(),
                license,
                experience,
                education,
                fee
            );
            
            if (success) {
                NotificationUtil.showInfo("Başarılı", "Personel güncellendi.");
                loadStaff();
            } else {
                NotificationUtil.showError("Hata", "Personel güncellenemedi.");
            }
        }
    }



    @FXML
    private void deleteStaff() {
        Doctor sel = staffTableView.getSelectionModel().getSelectedItem();
        if (sel == null) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Silmek için bir personel seçin.", ButtonType.OK);
            a.showAndWait();
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Personeli silmek istediğinize emin misiniz?", ButtonType.YES, ButtonType.NO);
        var r = confirm.showAndWait();
        if (r.isPresent() && r.get() == ButtonType.YES) {
            boolean ok = DatabaseQuery.deleteDoctor(sel.getDoctorId());
            if (ok) loadStaff();
            else {
                Alert a = new Alert(Alert.AlertType.ERROR, "Personel silinemedi.", ButtonType.OK);
                a.showAndWait();
            }
        }
    }

    @FXML
    private void loadAppointments() {
        System.out.println("Randevular yükleniyor...");
        var appointments = DatabaseQuery.getAllAppointments();
        if (appointmentTableView != null && appointments != null) {
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
            Stage currentStage = (Stage) staffTableView.getScene().getWindow();
            GUIManager guiManager = new GUIManager(currentStage);
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

        javafx.scene.control.Label lblInfo = new javafx.scene.control.Label("Şifre en az 8 karakter, büyük harf, küçük harf ve sayı içermelidir.");
        lblInfo.setWrapText(true);
        lblInfo.setMaxWidth(300);
        lblInfo.setStyle("-fx-text-fill: gray; -fx-font-size: 11px;");
        grid.add(lblInfo, 0, 3, 2, 1);
        
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
    private void createAppointment() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("manager_create_appointment.fxml"));
            javafx.scene.Scene scene = new javafx.scene.Scene(loader.load());
            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Hasta Adına Randevu Oluştur");
            stage.initOwner(App.getStage());
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
            refreshAppointments();
        } catch (Exception e) {
            e.printStackTrace();
            NotificationUtil.showError("Hata", "Randevu oluşturma ekranı açılamadı: " + e.getMessage());
        }
    }

    @FXML
    private void modifyAppointment() {
        Appointment selected = appointmentTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            NotificationUtil.showError("Hata", "Lütfen düzenlemek için bir randevu seçiniz.");
            return;
        }

        if (!"scheduled".equalsIgnoreCase(selected.getStatus())) {
            NotificationUtil.showError("Hata", "Yalnızca 'Planlanmış' durumdaki randevular düzenlenebilir.");
            return;
        }

        // Randevu tarihi bugün veya geçmişse "completed" yapılabilir
        java.time.LocalDate appointmentDate = selected.getAppointmentDate().toLocalDate();
        java.time.LocalDate today = java.time.LocalDate.now();
        
        java.util.List<String> options = new java.util.ArrayList<>();
        options.add("scheduled");
        options.add("cancelled");
        
        // Bugün veya geçmiş tarihse completed ekle
        if (!appointmentDate.isAfter(today)) {
            options.add("completed");
        }
        
        var dialog = new javafx.scene.control.ChoiceDialog<>("scheduled", options);
        dialog.setTitle("Randevu Durumu Güncelle");
        dialog.setHeaderText("Randevu #" + selected.getAppointmentId() + " durumunu değiştir");
        dialog.setContentText("Yeni durum:");
        
        var result = dialog.showAndWait();
        result.ifPresent(status -> {
            boolean updated = DatabaseQuery.updateAppointmentStatus(selected.getAppointmentId(), status);
            if (updated) {
                NotificationUtil.showInfo("Başarı", "Randevu durumu güncellendi.");
                refreshAppointments();
            } else {
                NotificationUtil.showError("Hata", "Randevu güncellenemedi.");
            }
        });
    }

    @FXML
    private void manageAvailability() {
        // Doktor seçimi
        var allDoctors = DatabaseQuery.getAllDoctors();
        if (allDoctors.isEmpty()) {
            NotificationUtil.showError("Hata", "Sistemde doktor bulunamadı.");
            return;
        }
        
        // Custom dialog oluştur
        var dialog = new javafx.scene.control.Dialog<javafx.util.Pair<Doctor, java.util.Map<String, Object>>>();
        dialog.setTitle("Müsaitlik Ekle");
        dialog.setHeaderText("Doktor için yeni müsaitlik zamanı ekleyin");
        
        // Dialog buttonları
        var addButtonType = new javafx.scene.control.ButtonType("Ekle", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, javafx.scene.control.ButtonType.CANCEL);
        
        // Grid layout
        var grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));
        
        // Doktor Seçimi Bileşenleri
        var searchField = new javafx.scene.control.TextField();
        searchField.setPromptText("İsim ile ara...");
        
        var doctorCombo = new javafx.scene.control.ComboBox<Doctor>();
        doctorCombo.setPrefWidth(300);
        
        // ComboBox gösterim formatı
        doctorCombo.setConverter(new javafx.util.StringConverter<Doctor>() {
            @Override
            public String toString(Doctor d) {
                if (d == null) return "";
                return d.getName() + " (" + d.getSpecialtyName() + ")";
            }
            @Override
            public Doctor fromString(String string) {
                return null; 
            }
        });

        // Filtreleme mantığı
        javafx.collections.ObservableList<Doctor> observableDoctors = javafx.collections.FXCollections.observableArrayList(allDoctors);
        javafx.collections.transformation.FilteredList<Doctor> filteredDoctors = new javafx.collections.transformation.FilteredList<>(observableDoctors, p -> true);
        doctorCombo.setItems(filteredDoctors);

        searchField.textProperty().addListener((obs, oldValue, newValue) -> {
            filteredDoctors.setPredicate(doctor -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return doctor.getName().toLowerCase().contains(lowerCaseFilter);
            });
            doctorCombo.getSelectionModel().selectFirst();
        });

        // Tablodan seçim varsa otomatik seç
        if (staffTableView != null) {
            Doctor selectedInTable = staffTableView.getSelectionModel().getSelectedItem();
            if (selectedInTable != null) {
                doctorCombo.getSelectionModel().select(selectedInTable);
                // Eğer tablodan seçildiyse arama kutusuna da ismini yazabiliriz ama gerek yok, direkt seçili gelsin.
            } else {
                doctorCombo.getSelectionModel().selectFirst();
            }
        } else {
            doctorCombo.getSelectionModel().selectFirst();
        }
        
        // DatePicker
        var datePicker = new javafx.scene.control.DatePicker();
        datePicker.setValue(java.time.LocalDate.now().plusDays(1));
        datePicker.setPrefWidth(300);
        
        // Saat ComboBox (08:00 - 17:00 arası 15'er dakika)
        var timeCombo = new javafx.scene.control.ComboBox<String>();
        for (int hour = 8; hour < 18; hour++) {
            for (int minute = 0; minute < 60; minute += 15) {
                timeCombo.getItems().add(String.format("%02d:%02d", hour, minute));
            }
        }
        timeCombo.getSelectionModel().selectFirst();
        timeCombo.setPrefWidth(300);
        
        // Grid'e ekle
        grid.add(new javafx.scene.control.Label("Doktor Ara:"), 0, 0);
        grid.add(searchField, 1, 0);
        grid.add(new javafx.scene.control.Label("Doktor Seç:"), 0, 1);
        grid.add(doctorCombo, 1, 1);
        grid.add(new javafx.scene.control.Label("Tarih:"), 0, 2);
        grid.add(datePicker, 1, 2);
        grid.add(new javafx.scene.control.Label("Saat:"), 0, 3);
        grid.add(timeCombo, 1, 3);
        
        dialog.getDialogPane().setContent(grid);
        
        // Result converter
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                var map = new java.util.HashMap<String, Object>();
                map.put("date", datePicker.getValue());
                map.put("time", timeCombo.getValue());
                return new javafx.util.Pair<>(doctorCombo.getSelectionModel().getSelectedItem(), map);
            }
            return null;
        });
        
        // Dialog göster
        var result = dialog.showAndWait();
        
        result.ifPresent(pair -> {
            Doctor selectedDoctor = pair.getKey();
            if (selectedDoctor == null) {
                 NotificationUtil.showError("Hata", "Lütfen bir doktor seçiniz.");
                 return;
            }
            var data = pair.getValue();
            java.time.LocalDate selectedDate = (java.time.LocalDate) data.get("date");
            String selectedTime = (String) data.get("time");
            
            // Tarih doğrulama
            if (selectedDate.isBefore(java.time.LocalDate.now())) {
                NotificationUtil.showError("Hata", "Geçmiş tarih seçemezsiniz!");
                return;
            }
            
            try {
                boolean added = DatabaseQuery.addAvailability(
                    selectedDoctor.getDoctorId(), 
                    java.sql.Date.valueOf(selectedDate), 
                    selectedTime
                );
                
                if (added) {
                    NotificationUtil.showInfo("Başarı", 
                        "Dr. " + selectedDoctor.getName() + " için müsaitlik eklendi.\n" +
                        "Tarih: " + selectedDate.format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy")) + 
                        "\nSaat: " + selectedTime);
                } else {
                    NotificationUtil.showError("Hata", "Müsaitlik eklenemedi. Bu tarih ve saat zaten mevcut olabilir.");
                }
            } catch (Exception e) {
                NotificationUtil.showError("Hata", "Müsaitlik eklenirken hata: " + e.getMessage());
            }
        });    }
}