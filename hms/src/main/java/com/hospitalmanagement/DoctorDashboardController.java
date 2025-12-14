package com.hospitalmanagement;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.util.HashSet;
import java.util.Set;

import com.hospitalmanagement.service.AppointmentManager;
import com.hospitalmanagement.service.HospitalManager;

public class DoctorDashboardController {

    // Service layer instances
    private final AppointmentManager appointmentManager = new AppointmentManager();
    private final HospitalManager hospitalManager = new HospitalManager();

    @FXML
    private Label welcomeLabel;

    @FXML
    private void initialize() {
        welcomeLabel.setText("Welcome to Doctor Dashboard!");
    }

    @FXML
    private void handleLogout() {

        try {
            System.out.println("Doctor logged out.");
            App.setRoot("login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void loadAppointments() {
        System.out.println("Loading appointments...");
        var user = Session.getCurrentUser();
        if (user == null) {
            Alert a = new Alert(Alert.AlertType.WARNING, "No session found.", ButtonType.OK);
            a.setHeaderText("Error");
            a.showAndWait();
            return;
        }

        var doctor = DatabaseQuery.getDoctorByUserId(user.getUserId());
        if (doctor == null) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Doctor record not found.", ButtonType.OK);
            a.setHeaderText("Appointments");
            a.showAndWait();
            return;
        }

        var appts = appointmentManager.viewAppointmentsByDoctor(doctor.getDoctorId());
        if (appts.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "No appointments found.", ButtonType.OK);
            a.setHeaderText("Appointments");
            a.showAndWait();
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (var ap : appts) {
            sb.append(ap.getAppointmentDate()).append(" ").append(ap.getTimeSlot()).append(" - ")
              .append(ap.getPatientName()).append(" (Status: ").append(ap.getStatus()).append(")\n");
        }
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle("Appointments");
        a.setHeaderText("Your Current Appointments");
        TextArea ta = new TextArea(sb.toString());
        ta.setEditable(false);
        ta.setWrapText(true);
        a.getDialogPane().setContent(ta);
        a.showAndWait();
    }
    @FXML
    private void manageAvailability() {
        System.out.println("Managing availability hours...");
        var user = Session.getCurrentUser();
        if (user == null) {
            Alert a = new Alert(Alert.AlertType.WARNING, "No session found.", ButtonType.OK);
            a.setHeaderText("Error");
            a.showAndWait();
            return;
        }

        var doctor = DatabaseQuery.getDoctorByUserId(user.getUserId());
        if (doctor == null) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Doctor record not found.", ButtonType.OK);
            a.setHeaderText("Availability");
            a.showAndWait();
            return;
        }

        javafx.scene.control.TextInputDialog dateDialog = new javafx.scene.control.TextInputDialog();
        dateDialog.setTitle("Add Availability");
        dateDialog.setHeaderText("Enter date (YYYY-MM-DD)");
        DialogUtil.attachOkValidation(dateDialog, () -> {
            String txt = dateDialog.getEditor().getText();
            try {
                java.time.LocalDate d = java.time.LocalDate.parse(txt);
                if (d.isBefore(java.time.LocalDate.now())) {
                    ValidationUtil.showError("Date cannot be in the past.");
                    return false;
                }
                return true;
            } catch (Exception ex) {
                ValidationUtil.showError("Invalid date format. Please enter in YYYY-MM-DD format.");
                return false;
            }
        });
        var dateRes = dateDialog.showAndWait();
        if (dateRes.isEmpty()) return;
        String dateStr = dateRes.get();

        javafx.scene.control.TextInputDialog timeDialog = new javafx.scene.control.TextInputDialog();
        timeDialog.setTitle("Add Availability");
        timeDialog.setHeaderText("Enter time range (HH:MM-HH:MM)");
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
                    ValidationUtil.showError("Time range must be within working hours and in correct format (08:00-18:00).");
                    return false;
                }
                return true;
            } catch (Exception ex) {
                ValidationUtil.showError("Invalid time format. Enter in HH:MM-HH:MM format.");
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
                Alert success = new Alert(Alert.AlertType.INFORMATION, "Availability added successfully.", ButtonType.OK);
                success.setHeaderText("Success");
                success.showAndWait();
            } else {
                Alert err = new Alert(Alert.AlertType.ERROR, "Could not add availability. Check date/time format and working hours.", ButtonType.OK);
                err.setHeaderText("Error");
                err.showAndWait();
            }
        } catch (Exception ex) {
            Alert err = new Alert(Alert.AlertType.ERROR, "Invalid date format.", ButtonType.OK);
            err.setHeaderText("Error");
            err.showAndWait();
        }
    }
    @FXML
    private void viewPatients() {
        System.out.println("Showing patients...");
        // Simple: collect patient names from appointments
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
        a.setTitle("Patients");
        a.setHeaderText("Your Patients");
        TextArea ta = new TextArea(sb.toString());
        ta.setEditable(false);
        ta.setWrapText(true);
        a.getDialogPane().setContent(ta);
        a.showAndWait();
    }

    @FXML
    private void manageMedicalRecords() {
        System.out.println("Managing medical records...");

        var user = Session.getCurrentUser();
        if (user == null) {
            Alert a = new Alert(Alert.AlertType.WARNING, "No session found.", ButtonType.OK);
            a.setHeaderText("Error");
            a.showAndWait();
            return;
        }

        var doctor = DatabaseQuery.getDoctorByUserId(user.getUserId());
        if (doctor == null) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Doctor record not found.", ButtonType.OK);
            a.setHeaderText("Medical Records");
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
            Alert a = new Alert(Alert.AlertType.INFORMATION, "No patients found that you have treated.", ButtonType.OK);
            a.setHeaderText("Medical Records");
            a.showAndWait();
            return;
        }

        java.util.List<String> items = new java.util.ArrayList<>();
        for (var e : patientMap.entrySet()) items.add(e.getKey() + " - " + e.getValue());
        javafx.scene.control.ChoiceDialog<String> dialog = new javafx.scene.control.ChoiceDialog<>(items.get(0), items);
        dialog.setTitle("Select Patient");
        dialog.setHeaderText("Select the patient whose records you want to view");
        var res = dialog.showAndWait();
        if (res.isEmpty()) return;
        String sel = res.get();
        int patientId = Integer.parseInt(sel.split(" - ")[0].trim());

        // Verify access (doctor must have treated patient)
        boolean treated = DatabaseQuery.hasDoctorTreatedPatient(doctor.getDoctorId(), patientId);
        if (!treated) {
            Alert a = new Alert(Alert.AlertType.ERROR, "You do not have permission to view this patient's records.", ButtonType.OK);
            a.setHeaderText("Access Denied");
            a.showAndWait();
            return;
        }

        // Load records
        var records = hospitalManager.viewRecordsByPatient(patientId);
        if (records.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "No medical records found for this patient.", ButtonType.OK);
            a.setHeaderText("Medical Records");
            a.showAndWait();
            return;
        }

        // Let doctor select a record to view/edit
        java.util.List<String> recLabels = new java.util.ArrayList<>();
        for (var r : records) recLabels.add(r.getRecordId() + " - " + r.getRecordDate() + " - ID:" + r.getRecordId());
        javafx.scene.control.ChoiceDialog<String> rDialog = new javafx.scene.control.ChoiceDialog<>(recLabels.get(0), recLabels);
        rDialog.setTitle("Select Record");
        rDialog.setHeaderText("Select the record you want to view/edit");
        var rRes = rDialog.showAndWait();
        if (rRes.isEmpty()) return;
        String rsel = rRes.get();
        int recordId = Integer.parseInt(rsel.replaceAll(".*ID:", ""));
        MedicalRecord record = null;
        for (var r : records) if (r.getRecordId() == recordId) record = r;
        if (record == null) return;

        // Show details
        StringBuilder sb = new StringBuilder();
        sb.append("Date: ").append(record.getRecordDate()).append("\n");
        sb.append("Test Results:\n").append(record.getTestResults()).append("\n\n");
        sb.append("Medications:\n").append(record.getMedications()).append("\n\n");
        sb.append("Notes:\n").append(record.getNotes()).append("\n");

        javafx.scene.control.TextArea ta = new javafx.scene.control.TextArea(sb.toString());
        ta.setEditable(false);
        ta.setWrapText(true);
        Alert view = new Alert(Alert.AlertType.NONE);
        view.setTitle("Record Details");
        view.getDialogPane().setContent(ta);
        view.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.APPLY);
        var vres = view.showAndWait();
        if (vres.isPresent() && vres.get() == ButtonType.APPLY) {
            // Edit flow
            if (!DatabaseQuery.hasDoctorTreatedPatient(doctor.getDoctorId(), patientId)) {
                Alert a = new Alert(Alert.AlertType.ERROR, "You do not have permission to edit this record.", ButtonType.OK);
                a.setHeaderText("Access Denied");
                a.showAndWait();
                return;
            }
            // Prompt for new values
            javafx.scene.control.TextInputDialog tr = new javafx.scene.control.TextInputDialog(record.getTestResults());
            tr.setTitle("Test Results");
            tr.setHeaderText("Enter test results");
            DialogUtil.attachOkValidation(tr, () -> {
                if (tr.getEditor().getText() == null || tr.getEditor().getText().isBlank()) {
                    ValidationUtil.showError("Test results cannot be empty.");
                    return false;
                }
                return true;
            });
            var trRes = tr.showAndWait();
            if (trRes.isEmpty()) return;
            String newTest = trRes.get();

            javafx.scene.control.TextInputDialog med = new javafx.scene.control.TextInputDialog(record.getMedications());
            med.setTitle("Medications");
            med.setHeaderText("Enter medication list");
            DialogUtil.attachOkValidation(med, () -> {
                if (med.getEditor().getText() == null || med.getEditor().getText().isBlank()) {
                    ValidationUtil.showError("Medications cannot be empty.");
                    return false;
                }
                return true;
            });
            var medRes = med.showAndWait();
            if (medRes.isEmpty()) return;
            String newMed = medRes.get();

            javafx.scene.control.TextInputDialog notes = new javafx.scene.control.TextInputDialog(record.getNotes());
            notes.setTitle("Notes");
            notes.setHeaderText("Enter doctor notes");
            DialogUtil.attachOkValidation(notes, () -> {
                if (notes.getEditor().getText() == null || notes.getEditor().getText().isBlank()) {
                    ValidationUtil.showError("Notes cannot be empty.");
                    return false;
                }
                return true;
            });
            var notesRes = notes.showAndWait();
            if (notesRes.isEmpty()) return;
            String newNotes = notesRes.get();

            boolean ok = DatabaseQuery.updateMedicalRecord(record.getRecordId(), doctor.getDoctorId(), newTest, newMed, newNotes);
            if (ok) {
                Alert s = new Alert(Alert.AlertType.INFORMATION, "Record updated successfully.", ButtonType.OK);
                s.setHeaderText("Success");
                s.showAndWait();
            } else {
                Alert e = new Alert(Alert.AlertType.ERROR, "Could not update record.", ButtonType.OK);
                e.setHeaderText("Error");
                e.showAndWait();
            }
        }
    }
    @FXML
    private void handleChangePassword() {
        var user = Session.getCurrentUser();
        if (user == null) {
            NotificationUtil.showError("Error", "No session found.");
            return;
        }
        
        javafx.scene.control.Dialog<Void> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Change Password");
        dialog.setHeaderText("Update your password");
        
        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20));
        
        javafx.scene.control.PasswordField tfOldPassword = new javafx.scene.control.PasswordField();
        javafx.scene.control.PasswordField tfNewPassword = new javafx.scene.control.PasswordField();
        javafx.scene.control.PasswordField tfConfirm = new javafx.scene.control.PasswordField();
        
        grid.add(new javafx.scene.control.Label("Old Password:"), 0, 0);
        grid.add(tfOldPassword, 1, 0);
        grid.add(new javafx.scene.control.Label("New Password:"), 0, 1);
        grid.add(tfNewPassword, 1, 1);
        grid.add(new javafx.scene.control.Label("Confirm New Password:"), 0, 2);
        grid.add(tfConfirm, 1, 2);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(javafx.scene.control.ButtonType.OK, javafx.scene.control.ButtonType.CANCEL);
        
        DialogUtil.attachOkValidation(dialog, () -> {
            if (tfOldPassword.getText().isEmpty()) {
                ValidationUtil.showError("Validation", "Old password cannot be empty.");
                return false;
            }
            if (tfNewPassword.getText().isEmpty()) {
                ValidationUtil.showError("Validation", "New password cannot be empty.");
                return false;
            }
            if (!tfNewPassword.getText().equals(tfConfirm.getText())) {
                ValidationUtil.showError("Validation", "New passwords do not match.");
                return false;
            }
            if (!ValidationUtil.isValidPassword(tfNewPassword.getText())) {
                ValidationUtil.showError("Validation", "Password must be at least 8 characters and contain uppercase, lowercase, and numbers.");
                return false;
            }
            return true;
        });
        
        var result = dialog.showAndWait();
        if (result.isPresent()) {
            boolean ok = DatabaseQuery.changePassword(user.getUserId(), tfOldPassword.getText(), tfNewPassword.getText());
            if (ok) {
                NotificationUtil.showInfo("Success", "Password changed successfully.");
            } else {
                NotificationUtil.showError("Error", "Old password is incorrect or password could not be changed.");
            }
        }
    }
}