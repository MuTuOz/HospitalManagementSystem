package com.hospitalmanagement;

import com.hospitalmanagement.service.AppointmentManager;
import com.hospitalmanagement.service.HospitalManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.HashSet;
import java.util.Set;

public class DoctorDashboardController {
    
    // Service layer instances
    private final AppointmentManager appointmentManager = new AppointmentManager();
    private final HospitalManager hospitalManager = new HospitalManager();

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label doctorNameLabel;

    @FXML
    private void initialize() {
        welcomeLabel.setText("Doktor Dashboard");
        var user = Session.getCurrentUser();
        if (user != null) {
            doctorNameLabel.setText(user.getName());
        }
    }

    @FXML
    private void loadAppointments() {
        System.out.println("Randevular yükleniyor...");
        var user = Session.getCurrentUser();
        if (user == null) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Oturum bulunamadı.", ButtonType.OK);
            a.setHeaderText("Hata");
            a.showAndWait();
            return;
        }

        var doctor = DatabaseQuery.getDoctorByUserId(user.getUserId());
        if (doctor == null) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Doktor kaydı bulunamadı.", ButtonType.OK);
            a.setHeaderText("Randevular");
            a.showAndWait();
            return;
        }

        var appts = appointmentManager.viewAppointmentsByDoctor(doctor.getDoctorId());
        if (appts.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Hiç randevu bulunamadı.", ButtonType.OK);
            a.setHeaderText("Randevular");
            a.showAndWait();
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (var ap : appts) {
            sb.append(ap.getAppointmentDate()).append(" ").append(ap.getTimeSlot()).append(" - ")
              .append(ap.getPatientName()).append(" (Durum: ").append(ap.getStatus()).append(")\n");
        }
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Randevular");
        a.setHeaderText("Mevcut Randevularınız");
        TextArea ta = new TextArea(sb.toString());
        ta.setEditable(false);
        ta.setWrapText(true);
        a.getDialogPane().setContent(ta);
        a.showAndWait();
    }

    @FXML
    private void manageAvailability() {
        System.out.println("Uygunluk saatleri yönetiliyor...");
        var user = Session.getCurrentUser();
        if (user == null) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Oturum yok.", ButtonType.OK);
            a.setHeaderText("Hata");
            a.showAndWait();
            return;
        }

        var doctor = DatabaseQuery.getDoctorByUserId(user.getUserId());
        if (doctor == null) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Doktor kaydı bulunamadı.", ButtonType.OK);
            a.setHeaderText("Uygunluk");
            a.showAndWait();
            return;
        }

        javafx.scene.control.TextInputDialog dateDialog = new javafx.scene.control.TextInputDialog();
        dateDialog.setTitle("Uygunluk Ekle");
        dateDialog.setHeaderText("Tarih giriniz (YYYY-MM-DD)");
        DialogUtil.attachOkValidation(dateDialog, () -> {
            String txt = dateDialog.getEditor().getText();
            try {
                java.time.LocalDate d = java.time.LocalDate.parse(txt);
                if (d.isBefore(java.time.LocalDate.now())) {
                    ValidationUtil.showError("Tarih geçmiş olamaz.");
                    return false;
                }
                return true;
            } catch (Exception ex) {
                ValidationUtil.showError("Geçersiz tarih formatı. Lütfen YYYY-MM-DD şeklinde girin.");
                return false;
            }
        });
        var dateRes = dateDialog.showAndWait();
        if (dateRes.isEmpty()) return;
        String dateStr = dateRes.get();

        javafx.scene.control.TextInputDialog timeDialog = new javafx.scene.control.TextInputDialog();
        timeDialog.setTitle("Uygunluk Ekle");
        timeDialog.setHeaderText("Saat aralığını giriniz (HH:MM-HH:MM)");
        DialogUtil.attachOkValidation(timeDialog, () -> {
            String txt = timeDialog.getEditor().getText();
            try {
                String[] parts = txt.split("-");
                if (parts.length != 2) throw new IllegalArgumentException();
                java.time.LocalTime s = java.time.LocalTime.parse(parts[0]);
                java.time.LocalTime e = java.time.LocalTime.parse(parts[1]);
                java.time.LocalTime workStart = java.time.LocalTime.of(8,0);
                java.time.LocalTime workEnd = java.time.LocalTime.of(18,0);
                if (!s.isBefore(e) || s.isBefore(workStart) || e.isAfter(workEnd)) {
                    ValidationUtil.showError("Saat aralığı çalışma saatleri içinde ve doğru formatta olmalıdır (08:00-18:00).");
                    return false;
                }
                return true;
            } catch (Exception ex) {
                ValidationUtil.showError("Geçersiz saat formatı. HH:MM-HH:MM şeklinde girin.");
                return false;
            }
        });
        var timeRes = timeDialog.showAndWait();
        if (timeRes.isEmpty()) return;
        String timeSlot = timeRes.get();

        try {
            java.time.LocalDate date = java.time.LocalDate.parse(dateStr);
            boolean ok = DatabaseQuery.addAvailability(doctor.getDoctorId(), date, timeSlot);
            if (ok) {
                Alert success = new Alert(Alert.AlertType.INFORMATION, "Uygunluk başarıyla eklendi.", ButtonType.OK);
                success.setHeaderText("Başarılı");
                success.showAndWait();
            } else {
                Alert err = new Alert(Alert.AlertType.ERROR, "Uygunluk eklenemedi. Tarih/saat formatını ve çalışma saatlerini kontrol edin.", ButtonType.OK);
                err.setHeaderText("Hata");
                err.showAndWait();
            }
        } catch (Exception ex) {
            Alert err = new Alert(Alert.AlertType.ERROR, "Geçersiz tarih formatı.", ButtonType.OK);
            err.setHeaderText("Hata");
            err.showAndWait();
        }
    }

    @FXML
    private void viewPatients() {
        System.out.println("Hastalar gösteriliyor...");
        // Basit: randevulardan hasta isimlerini topla
        var user = Session.getCurrentUser();
        if (user == null) return;
        var doctor = DatabaseQuery.getDoctorByUserId(user.getUserId());
        if (doctor == null) return;
        var appts = appointmentManager.viewAppointmentsByDoctor(doctor.getDoctorId());
        Set<String> patients = new HashSet<>();
        for (var ap : appts) patients.add(ap.getPatientName());
        StringBuilder sb = new StringBuilder();
        for (var p : patients) sb.append(p).append("\n");
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Hastalar");
        a.setHeaderText("Hastalarınız");
        TextArea ta = new TextArea(sb.toString());
        ta.setEditable(false);
        ta.setWrapText(true);
        a.getDialogPane().setContent(ta);
        a.showAndWait();
    }

    @FXML
    private void manageMedicalRecords() {
        System.out.println("Tıbbi kayıtlar yönetiliyor...");

        var user = Session.getCurrentUser();
        if (user == null) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Oturum bulunamadı.", ButtonType.OK);
            a.setHeaderText("Hata");
            a.showAndWait();
            return;
        }

        var doctor = DatabaseQuery.getDoctorByUserId(user.getUserId());
        if (doctor == null) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Doktor kaydı bulunamadı.", ButtonType.OK);
            a.setHeaderText("Tıbbi Kayıtlar");
            a.showAndWait();
            return;
        }

        // Ensure schema (adds last_edited_by and audit table if missing)
        DatabaseQuery.ensureMedicalRecordSchema();

        // Build patient list from appointments
        var appts = appointmentManager.viewAppointmentsByDoctor(doctor.getDoctorId());
        java.util.Map<Integer, String> patientMap = new java.util.LinkedHashMap<>();
        for (var ap : appts) {
            try { patientMap.put(ap.getPatientId(), ap.getPatientName()); } catch (Exception ignored) {}
        }
        if (patientMap.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Henüz tedavi ettiğiniz hasta bulunamadı.", ButtonType.OK);
            a.setHeaderText("Tıbbi Kayıtlar");
            a.showAndWait();
            return;
        }

        java.util.List<String> items = new java.util.ArrayList<>();
        for (var e : patientMap.entrySet()) items.add(e.getKey() + " - " + e.getValue());
        javafx.scene.control.ChoiceDialog<String> dialog = new javafx.scene.control.ChoiceDialog<>(items.get(0), items);
        dialog.setTitle("Hasta Seç");
        dialog.setHeaderText("Kayıtları görüntülemek istediğiniz hastayı seçin");
        var res = dialog.showAndWait();
        if (res.isEmpty()) return;
        String sel = res.get();
        int patientId = Integer.parseInt(sel.split(" - ")[0].trim());

        // Verify access (doctor must have treated patient)
        boolean treated = DatabaseQuery.hasDoctorTreatedPatient(doctor.getDoctorId(), patientId);
        if (!treated) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Bu hastanın kayıtlarını görüntüleme yetkiniz yok.", ButtonType.OK);
            a.setHeaderText("Erişim Reddedildi");
            a.showAndWait();
            return;
        }

        // Load records
        var records = hospitalManager.viewRecordsByPatient(patientId);
        if (records.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Bu hasta için tıbbi kayıt bulunamadı.", ButtonType.OK);
            a.setHeaderText("Tıbbi Kayıtlar");
            a.showAndWait();
            return;
        }

        // Let doctor select a record to view/edit
        java.util.List<String> recLabels = new java.util.ArrayList<>();
        for (var r : records) recLabels.add(r.getRecordId() + " - " + r.getRecordDate() + " - ID:" + r.getRecordId());
        javafx.scene.control.ChoiceDialog<String> rDialog = new javafx.scene.control.ChoiceDialog<>(recLabels.get(0), recLabels);
        rDialog.setTitle("Kayıt Seç");
        rDialog.setHeaderText("Görüntülemek/Düzenlemek istediğiniz kaydı seçin");
        var rRes = rDialog.showAndWait();
        if (rRes.isEmpty()) return;
        String rsel = rRes.get();
        int recordId = Integer.parseInt(rsel.replaceAll(".*ID:", ""));
        MedicalRecord record = null;
        for (var r : records) if (r.getRecordId() == recordId) record = r;
        if (record == null) return;

        // Show details
        StringBuilder sb = new StringBuilder();
        sb.append("Tarih: ").append(record.getRecordDate()).append("\n");
        sb.append("Test Sonuçları:\n").append(record.getTestResults()).append("\n\n");
        sb.append("İlaçlar:\n").append(record.getMedications()).append("\n\n");
        sb.append("Notlar:\n").append(record.getNotes()).append("\n");

        javafx.scene.control.TextArea ta = new javafx.scene.control.TextArea(sb.toString());
        ta.setEditable(false);
        ta.setWrapText(true);
        Alert view = new Alert(Alert.AlertType.NONE);
        view.setTitle("Kayıt Detayı");
        view.getDialogPane().setContent(ta);
        view.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.APPLY);
        var vres = view.showAndWait();
        if (vres.isPresent() && vres.get() == ButtonType.APPLY) {
            // Edit flow
            if (!DatabaseQuery.hasDoctorTreatedPatient(doctor.getDoctorId(), patientId)) {
                Alert a = new Alert(Alert.AlertType.ERROR, "Bu kaydı düzenleme yetkiniz yok.", ButtonType.OK);
                a.setHeaderText("Erişim Reddedildi");
                a.showAndWait();
                return;
            }
            // Prompt for new values
            javafx.scene.control.TextInputDialog tr = new javafx.scene.control.TextInputDialog(record.getTestResults());
            tr.setTitle("Test Sonuçları");
            tr.setHeaderText("Test sonuçlarını girin");
            DialogUtil.attachOkValidation(tr, () -> {
                if (tr.getEditor().getText() == null || tr.getEditor().getText().isBlank()) {
                    ValidationUtil.showError("Test sonuçları boş olamaz.");
                    return false;
                }
                return true;
            });
            var trRes = tr.showAndWait();
            if (trRes.isEmpty()) return;
            String newTest = trRes.get();

            javafx.scene.control.TextInputDialog med = new javafx.scene.control.TextInputDialog(record.getMedications());
            med.setTitle("İlaçlar");
            med.setHeaderText("İlaç listesini girin");
            DialogUtil.attachOkValidation(med, () -> {
                if (med.getEditor().getText() == null || med.getEditor().getText().isBlank()) {
                    ValidationUtil.showError("İlaçlar boş olamaz.");
                    return false;
                }
                return true;
            });
            var medRes = med.showAndWait();
            if (medRes.isEmpty()) return;
            String newMed = medRes.get();

            javafx.scene.control.TextInputDialog notes = new javafx.scene.control.TextInputDialog(record.getNotes());
            notes.setTitle("Notlar");
            notes.setHeaderText("Doktor notlarını girin");
            DialogUtil.attachOkValidation(notes, () -> {
                if (notes.getEditor().getText() == null || notes.getEditor().getText().isBlank()) {
                    ValidationUtil.showError("Notlar boş olamaz.");
                    return false;
                }
                return true;
            });
            var notesRes = notes.showAndWait();
            if (notesRes.isEmpty()) return;
            String newNotes = notesRes.get();

            boolean ok = DatabaseQuery.updateMedicalRecord(record.getRecordId(), doctor.getDoctorId(), newTest, newMed, newNotes);
            if (ok) {
                Alert s = new Alert(Alert.AlertType.INFORMATION, "Kayıt başarıyla güncellendi.", ButtonType.OK);
                s.setHeaderText("Başarılı");
                s.showAndWait();
            } else {
                Alert e = new Alert(Alert.AlertType.ERROR, "Kayıt güncellenemedi.", ButtonType.OK);
                e.setHeaderText("Hata");
                e.showAndWait();
            }
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
}