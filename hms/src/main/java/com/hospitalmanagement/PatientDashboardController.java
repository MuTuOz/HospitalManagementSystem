package com.hospitalmanagement;

import com.hospitalmanagement.service.AppointmentManager;
import com.hospitalmanagement.service.HospitalManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;

import java.util.List;

public class PatientDashboardController {
    
    // Service layer instances
    private final AppointmentManager appointmentManager = new AppointmentManager();
    private final HospitalManager hospitalManager = new HospitalManager();
    
    @FXML
    private Label welcomeLabel;
    @FXML
    private Label lblName;
    @FXML
    private Label lblEmail;
    @FXML
    private Label lblPhone;
    @FXML
    private Label lblInsurance;
    @FXML
    private TableView<Appointment> appointmentsTable;

    @FXML
    private void initialize() {
        // Oturum açıldığında geçmiş randevuları güncelle
        try {
            int updated = DatabaseQuery.markPastAppointmentsCompleted();
            if (updated > 0) {
                System.out.println("Geçmiş randevular güncellendi: " + updated);
            }
        } catch (Exception e) {
            System.out.println("Randevu güncelleme hatası: " + e.getMessage());
        }

        welcomeLabel.setText("Hasta Dashboard'a Hoş Geldiniz!");
        setupAppointmentTableColumns();
        var user = Session.getCurrentUser();
        if (user != null) {
            var patient = DatabaseQuery.getPatientByUserId(user.getUserId());
            if (patient != null) {
                lblName.setText(patient.getName());
                lblEmail.setText(patient.getEmail());
                lblPhone.setText(patient.getPhone());
                lblInsurance.setText(patient.getInsuranceNo());
                refreshAppointments();
            } else {
                lblName.setText(user.getName());
                lblEmail.setText(user.getEmail());
                lblPhone.setText(user.getPhone());
                lblInsurance.setText("-");
            }
        }
    }

    private void setupAppointmentTableColumns() {
        if (appointmentsTable != null && appointmentsTable.getColumns().size() >= 5) {
            ((TableColumn<Appointment, Integer>) appointmentsTable.getColumns().get(0)).setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
            ((TableColumn<Appointment, String>) appointmentsTable.getColumns().get(1)).setCellValueFactory(new PropertyValueFactory<>("doctorName"));
            ((TableColumn<Appointment, String>) appointmentsTable.getColumns().get(2)).setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
            ((TableColumn<Appointment, String>) appointmentsTable.getColumns().get(3)).setCellValueFactory(new PropertyValueFactory<>("timeSlot"));
            ((TableColumn<Appointment, String>) appointmentsTable.getColumns().get(4)).setCellValueFactory(new PropertyValueFactory<>("status"));
        }
    }

    @FXML
    private void refreshAppointments() {
        var user = Session.getCurrentUser();
        if (user == null) return;
        var patient = DatabaseQuery.getPatientByUserId(user.getUserId());
        if (patient == null) return;
        List<Appointment> appts = appointmentManager.viewAppointmentsByPatient(patient.getPatientId());
        if (appointmentsTable != null) appointmentsTable.setItems(FXCollections.observableArrayList(appts));
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
    private void handleCreateAppointment() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("create_appointment.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Randevu Oluştur");
            stage.initOwner(App.getStage());
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
            // After the appointment window closes, refresh list in case of changes
            refreshAppointments();
        } catch (Exception e) {
            e.printStackTrace();
            NotificationUtil.showError("Hata", "Randevu oluşturma ekranı açılamadı.");
        }
    }

    @FXML
    private void handleViewMedicalRecords() {
        var user = Session.getCurrentUser();
        if (user == null) {
            showAlert("Oturum bulunamadı.", "Hata", Alert.AlertType.ERROR);
            return;
        }
        var patient = DatabaseQuery.getPatientByUserId(user.getUserId());
        if (patient == null) {
            showAlert("Hasta kaydı bulunamadı.", "Hata", Alert.AlertType.ERROR);
            return;
        }

        var records = hospitalManager.viewRecordsByUser(user.getUserId());
        if (records.isEmpty()) {
            showAlert("Tıbbi kayıt bulunamadı.", "Bilgi", Alert.AlertType.INFORMATION);
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (var r : records) {
            sb.append("Tarih: ").append(r.getRecordDate()).append("\n");
            sb.append("Test sonuçları:\n").append(r.getTestResults()).append("\n");
            sb.append("İlaçlar:\n").append(r.getMedications()).append("\n");
            sb.append("Notlar:\n").append(r.getNotes()).append("\n");
            sb.append("- - - - - - - - -\n");
        }

        // Show records in a dialog area but keep user in same dashboard (inline)
        TextArea ta = new TextArea(sb.toString());
        ta.setEditable(false);
        ta.setWrapText(true);
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Tıbbi Kayıtlar");
        a.setHeaderText("Kayıtlarınız");
        a.getDialogPane().setContent(ta);
        a.showAndWait();
    }

    @FXML
    private void handleCancelAppointment() {
        System.out.println("=== RANDEVU İPTAL ===");
        var selected = appointmentsTable.getSelectionModel().getSelectedItem();
        System.out.println("Selected: " + selected);
        if (selected == null) {
            System.out.println("Hiç randevu seçilmemiş!");
            NotificationUtil.showWarning("Seçim", "Lütfen iptal etmek istediğiniz randevuyu seçin.");
            return;
        }
        
        System.out.println("Appointment ID: " + selected.getAppointmentId());
        System.out.println("Status: " + selected.getStatus());
        
        if ("Cancelled".equals(selected.getStatus())) {
            System.out.println("Randevu zaten iptal edilmiş!");
            NotificationUtil.showInfo("İptal", "Bu randevu zaten iptal edilmiş.");
            return;
        }
        
        if ("Completed".equals(selected.getStatus())) {
            System.out.println("Tamamlanmış randevu iptal edilemez!");
            NotificationUtil.showWarning("İptal Edilemez", "Tamamlanmış randevular iptal edilemez.");
            return;
        }
        
        javafx.scene.control.Alert confirmAlert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Randevu İptali");
        confirmAlert.setHeaderText("Randevuyu iptal etmek istiyor musunuz?");
        confirmAlert.setContentText("Bu işlem geri alınamaz.");
        
        var result = confirmAlert.showAndWait();
        System.out.println("Confirmation result: " + result);
        if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
            var user = Session.getCurrentUser();
            System.out.println("Current user: " + user);
            System.out.println("User ID: " + (user != null ? user.getUserId() : "null"));
            
            boolean deleteResult = appointmentManager.deleteAppointment(selected.getAppointmentId(), user.getUserId());
            System.out.println("Delete result: " + deleteResult);
            
            if (user != null && deleteResult) {
                NotificationUtil.showInfo("İptal", "Randevu başarıyla iptal edildi.");
                refreshAppointments();
            } else {
                NotificationUtil.showError("Hata", "Randevu iptali başarısız.");
            }
        }
    }

    @FXML
    private void handleReactivateAppointment() {
        System.out.println("=== RANDEVU TEKRAR AKTIFLEŞTIRME ===");
        var selected = appointmentsTable.getSelectionModel().getSelectedItem();
        System.out.println("Selected: " + selected);
        
        if (selected == null) {
            System.out.println("Hiç randevu seçilmemiş!");
            NotificationUtil.showWarning("Seçim", "Lütfen tekrar aktifleştirmek istediğiniz randevuyu seçin.");
            return;
        }
        
        System.out.println("Appointment ID: " + selected.getAppointmentId());
        System.out.println("Status: " + selected.getStatus());
        
        if (!"Cancelled".equals(selected.getStatus())) {
            System.out.println("Sadece iptal edilmiş randevular tekrar aktifleştirilebilir!");
            NotificationUtil.showWarning("Geçersiz İşlem", "Sadece iptal edilmiş randevular tekrar aktifleştirilebilir.");
            return;
        }
        
        javafx.scene.control.Alert confirmAlert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Randevuyu Tekrar Aktifleştir");
        confirmAlert.setHeaderText("Bu randevuyu tekrar aktifleştirmek istiyor musunuz?");
        confirmAlert.setContentText("Randevu tarihi geçmemiş ve saat uygunsa randevunuz yeniden aktif olacaktır.");
        
        var result = confirmAlert.showAndWait();
        System.out.println("Confirmation result: " + result);
        
        if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
            var user = Session.getCurrentUser();
            System.out.println("Current user: " + user);
            System.out.println("User ID: " + (user != null ? user.getUserId() : "null"));
            
            boolean reactivateResult = appointmentManager.reactivateAppointment(selected.getAppointmentId(), user.getUserId());
            System.out.println("Reactivate result: " + reactivateResult);
            
            if (user != null && reactivateResult) {
                NotificationUtil.showInfo("Başarılı", "Randevu başarıyla tekrar aktifleştirildi.");
                refreshAppointments();
            } else {
                NotificationUtil.showError("Hata", "Randevu tekrar aktifleştirilemedi. Tarih geçmiş veya saat başkası tarafından alınmış olabilir.");
            }
        }
    }

    @FXML
    private void handleEditProfile() {
        var user = Session.getCurrentUser();
        if (user == null) {
            NotificationUtil.showError("Hata", "Oturum bulunamadı.");
            return;
        }
        
        var patient = DatabaseQuery.getPatientByUserId(user.getUserId());
        if (patient == null) {
            NotificationUtil.showError("Hata", "Hasta kaydı bulunamadı.");
            return;
        }
        
        javafx.scene.control.Dialog<Void> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Profili Düzenle");
        dialog.setHeaderText("Hesap bilgilerinizi güncelleyin");
        
        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20));
        
        javafx.scene.control.TextField tfName = new javafx.scene.control.TextField(patient.getName());
        javafx.scene.control.TextField tfEmail = new javafx.scene.control.TextField(patient.getEmail());
        javafx.scene.control.TextField tfPhone = new javafx.scene.control.TextField(patient.getPhone());
        javafx.scene.control.TextField tfBlood = new javafx.scene.control.TextField(patient.getBloodType() == null ? "" : patient.getBloodType());
        javafx.scene.control.TextField tfInsurance = new javafx.scene.control.TextField(patient.getInsuranceNo());
        javafx.scene.control.TextField tfEmergency = new javafx.scene.control.TextField(patient.getEmergencyContact() == null ? "" : patient.getEmergencyContact());
        
        grid.add(new javafx.scene.control.Label("Ad:"), 0, 0);
        grid.add(tfName, 1, 0);
        grid.add(new javafx.scene.control.Label("E-posta:"), 0, 1);
        grid.add(tfEmail, 1, 1);
        grid.add(new javafx.scene.control.Label("Telefon:"), 0, 2);
        grid.add(tfPhone, 1, 2);
        grid.add(new javafx.scene.control.Label("Kan Grubu:"), 0, 3);
        grid.add(tfBlood, 1, 3);
        grid.add(new javafx.scene.control.Label("Sigorta No:"), 0, 4);
        grid.add(tfInsurance, 1, 4);
        grid.add(new javafx.scene.control.Label("Acil Durum İletişim:"), 0, 5);
        grid.add(tfEmergency, 1, 5);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(javafx.scene.control.ButtonType.OK, javafx.scene.control.ButtonType.CANCEL);
        
        // Attach validation
        DialogUtil.attachOkValidation(dialog, () -> {
            if (tfName.getText().trim().isEmpty()) {
                ValidationUtil.showError("Doğrulama", "Ad boş olamaz.");
                return false;
            }
            if (!ValidationUtil.isValidEmail(tfEmail.getText().trim())) {
                ValidationUtil.showError("Doğrulama", "Geçerli bir e-posta adresi girin.");
                return false;
            }
            if (!ValidationUtil.isValidPhone(tfPhone.getText().trim())) {
                ValidationUtil.showError("Doğrulama", "Geçerli bir telefon numarası girin.");
                return false;
            }
            if (!ValidationUtil.isValidInsuranceNo(tfInsurance.getText().trim())) {
                ValidationUtil.showError("Doğrulama", "Sigorta numarası geçersiz. 6-20 alfanümerik karakter olmalıdır.");
                return false;
            }
            return true;
        });
        
        var result = dialog.showAndWait();
        if (result.isPresent()) {
            boolean ok = DatabaseQuery.updatePatient(
                patient.getPatientId(),
                tfName.getText().trim(),
                tfPhone.getText().trim(),
                tfEmail.getText().trim(),
                tfBlood.getText().trim(),
                tfInsurance.getText().trim(),
                tfEmergency.getText().trim()
            );
            
            if (ok) {
                NotificationUtil.showInfo("Başarı", "Profil başarıyla güncellendi.");
                // Refresh UI
                lblName.setText(tfName.getText());
                lblEmail.setText(tfEmail.getText());
                lblPhone.setText(tfPhone.getText());
                lblInsurance.setText(tfInsurance.getText());
                // Update session user
                Session.getCurrentUser().setName(tfName.getText());
                Session.getCurrentUser().setEmail(tfEmail.getText());
                Session.getCurrentUser().setPhone(tfPhone.getText());
            } else {
                NotificationUtil.showError("Hata", "Profil güncellemesi başarısız.");
            }
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
        dialog.getDialogPane().getButtonTypes().addAll(javafx.scene.control.ButtonType.OK, javafx.scene.control.ButtonType.CANCEL);
        
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
            boolean ok = DatabaseQuery.changePassword(user.getUserId(), tfOldPassword.getText(), tfNewPassword.getText());
            if (ok) {
                NotificationUtil.showInfo("Başarı", "Şifre başarıyla değiştirildi.");
            } else {
                NotificationUtil.showError("Hata", "Eski şifre yanlış veya şifre değiştirilemedi.");
            }
        }
    }

    private void showAlert(String msg, String header, Alert.AlertType type) {
        Alert a = new Alert(type, msg, ButtonType.OK);
        a.setHeaderText(header);
        a.showAndWait();
    }
}
