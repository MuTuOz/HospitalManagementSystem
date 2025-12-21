package com.hospitalmanagement;

import com.hospitalmanagement.service.AppointmentManager;
import com.hospitalmanagement.service.HospitalManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

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
    private Label hospitalLabel;

    @FXML
    private TableView<Appointment> appointmentTableView;

    @FXML
    private TableView<AvailabilityOption> availabilityTableView;

    @FXML
    private void initialize() {
        var user = Session.getCurrentUser();
        if (user != null) {
            doctorNameLabel.setText(user.getName());
            
            // Set hospital name
            Doctor d = DatabaseQuery.getDoctorByUserId(user.getUserId());
            if (d != null) {
                Hospital h = DatabaseQuery.getHospitalById(d.getHospitalId());
                if (h != null && hospitalLabel != null) {
                    hospitalLabel.setText(h.getName());
                }
            }
        }
        
        // Configure columns
        if (appointmentTableView != null) {
            ObservableList<TableColumn<Appointment, ?>> columns = appointmentTableView.getColumns();
            if (columns.size() >= 6) {
                columns.get(0).setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
                columns.get(1).setCellValueFactory(new PropertyValueFactory<>("doctorName"));
                columns.get(2).setCellValueFactory(new PropertyValueFactory<>("patientName"));
                columns.get(3).setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
                columns.get(4).setCellValueFactory(new PropertyValueFactory<>("timeSlot"));
                columns.get(5).setCellValueFactory(new PropertyValueFactory<>("status"));
            }
        }

        if (availabilityTableView != null) {
            ObservableList<TableColumn<AvailabilityOption, ?>> columns = availabilityTableView.getColumns();
            if (columns.size() >= 3) {
                ((TableColumn<AvailabilityOption, java.sql.Date>) columns.get(0)).setCellValueFactory(new PropertyValueFactory<>("date"));
                ((TableColumn<AvailabilityOption, String>) columns.get(1)).setCellValueFactory(new PropertyValueFactory<>("timeSlot"));
                ((TableColumn<AvailabilityOption, String>) columns.get(2)).setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty("Müsait"));
            }
        }

        loadAppointments();
        loadAvailability();
    }

    @FXML
    private void refreshAppointments() {
        loadAppointments();
    }

    @FXML
    private void refreshAvailability() {
        loadAvailability();
    }

    @FXML
    private void loadAvailability() {
        var user = Session.getCurrentUser();
        if (user == null) return;
        var doctor = DatabaseQuery.getDoctorByUserId(user.getUserId());
        if (doctor == null) return;

        var availabilities = DatabaseQuery.getAvailabilitiesByDoctor(doctor.getDoctorId());
        if (availabilityTableView != null) {
            availabilityTableView.setItems(FXCollections.observableArrayList(availabilities));
        }
    }

    @FXML
    private void loadAppointments() {
        System.out.println("Randevular yükleniyor...");
        var user = Session.getCurrentUser();
        if (user == null) return;

        var doctor = DatabaseQuery.getDoctorByUserId(user.getUserId());
        if (doctor == null) return;

        var appts = appointmentManager.viewAppointmentsByDoctor(doctor.getDoctorId());
        
        if (appointmentTableView != null) {
            appointmentTableView.setItems(FXCollections.observableArrayList(appts));
        }
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

        // Custom dialog
        javafx.scene.control.Dialog<javafx.util.Pair<java.time.LocalDate, String>> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Uygunluk Ekle");
        dialog.setHeaderText("Yeni müsaitlik zamanı ekleyin");

        // Buttons
        ButtonType addButtonType = new ButtonType("Ekle", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        // Layout
        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20, 150, 10, 10));

        javafx.scene.control.DatePicker datePicker = new javafx.scene.control.DatePicker();
        datePicker.setValue(java.time.LocalDate.now().plusDays(1));
        datePicker.setEditable(false);
        
        javafx.scene.control.ComboBox<String> timeCombo = new javafx.scene.control.ComboBox<>();
        for (int hour = 8; hour < 18; hour++) {
            for (int minute = 0; minute < 60; minute += 15) {
                timeCombo.getItems().add(String.format("%02d:%02d", hour, minute));
            }
        }
        timeCombo.getSelectionModel().selectFirst();

        grid.add(new Label("Tarih:"), 0, 0);
        grid.add(datePicker, 1, 0);
        grid.add(new Label("Saat:"), 0, 1);
        grid.add(timeCombo, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Convert result
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new javafx.util.Pair<>(datePicker.getValue(), timeCombo.getValue());
            }
            return null;
        });

        // Validation inside dialog
        final javafx.scene.control.Button btOk = (javafx.scene.control.Button) dialog.getDialogPane().lookupButton(addButtonType);
        btOk.addEventFilter(javafx.event.ActionEvent.ACTION, event -> {
            java.time.LocalDate d = datePicker.getValue();
            if (d == null) {
                ValidationUtil.showError("Tarih seçmelisiniz.");
                event.consume();
                return;
            }
            if (d.isBefore(java.time.LocalDate.now())) {
                ValidationUtil.showError("Geçmiş tarih seçemezsiniz.");
                event.consume();
                return;
            }
            if (d.getDayOfWeek() == java.time.DayOfWeek.SATURDAY || d.getDayOfWeek() == java.time.DayOfWeek.SUNDAY) {
                ValidationUtil.showError("Hafta sonu seçemezsiniz.");
                event.consume();
                return;
            }
        });

        var result = dialog.showAndWait();
        
        result.ifPresent(pair -> {
            java.time.LocalDate date = pair.getKey();
            String startTime = pair.getValue();
            
            // Calculate end time (15 mins later)
            String[] parts = startTime.split(":");
            int h = Integer.parseInt(parts[0]);
            int m = Integer.parseInt(parts[1]);
            java.time.LocalTime start = java.time.LocalTime.of(h, m);
            String timeSlot = String.format("%02d:%02d", start.getHour(), start.getMinute());

            boolean added = DatabaseQuery.addAvailability(
                doctor.getDoctorId(), 
                date, 
                timeSlot
            );
            
            if (added) {
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Müsaitlik başarıyla eklendi.", ButtonType.OK);
                a.showAndWait();
                loadAvailability();
            } else {
                Alert a = new Alert(Alert.AlertType.ERROR, "Müsaitlik eklenemedi (zaten mevcut olabilir).", ButtonType.OK);
                a.showAndWait();
            }
        });
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
        // DatabaseQuery.ensureMedicalRecordSchema(); // REMOVED: DDL mismatch

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
    private void handleCreateAppointment() {
        var user = Session.getCurrentUser();
        if (user == null) {
            NotificationUtil.showError("Hata", "Oturum bulunamadı.");
            return;
        }

        var doctor = DatabaseQuery.getDoctorByUserId(user.getUserId());
        if (doctor == null) {
            NotificationUtil.showError("Hata", "Doktor kaydı bulunamadı.");
            return;
        }

        try {
            // Create a dialog for appointment creation
            javafx.scene.control.Dialog<ButtonType> dialog = new javafx.scene.control.Dialog<>();
            dialog.setTitle("Yeni Randevu Oluştur");
            dialog.setHeaderText("Hasta için yeni randevu oluşturun");
            
            // Create dialog content
            javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new javafx.geometry.Insets(20));
            
            // Patient search field
            javafx.scene.control.TextField patientSearchField = new javafx.scene.control.TextField();
            patientSearchField.setPromptText("Hasta adını girin...");
            grid.add(new javafx.scene.control.Label("Hasta:"), 0, 0);
            grid.add(patientSearchField, 1, 0);
            
            // Date picker
            javafx.scene.control.DatePicker datePicker = new javafx.scene.control.DatePicker();
            datePicker.setValue(java.time.LocalDate.now());
            grid.add(new javafx.scene.control.Label("Tarih:"), 0, 1);
            grid.add(datePicker, 1, 1);
            
            // Time slot
            javafx.scene.control.ComboBox<String> timeSlotCombo = new javafx.scene.control.ComboBox<>();
            timeSlotCombo.setItems(FXCollections.observableArrayList(
                "09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00"
            ));
            grid.add(new javafx.scene.control.Label("Saat:"), 0, 2);
            grid.add(timeSlotCombo, 1, 2);
            
            // Notes
            javafx.scene.control.TextArea notesArea = new javafx.scene.control.TextArea();
            notesArea.setPromptText("Randevu notları (isteğe bağlı)...");
            notesArea.setPrefRowCount(3);
            grid.add(new javafx.scene.control.Label("Notlar:"), 0, 3);
            grid.add(notesArea, 1, 3);
            
            // Label for selected patient
            javafx.scene.control.Label selectedPatientLabel = new javafx.scene.control.Label("Hasta seçilmedi");
            selectedPatientLabel.setStyle("-fx-text-fill: #666; -fx-font-style: italic;");
            grid.add(selectedPatientLabel, 1, 4);
            
            // Store selected patient ID
            final int[] selectedPatientId = {-1};
            
            // Patient search button
            javafx.scene.control.Button searchButton = new javafx.scene.control.Button("Hasta Ara");
            searchButton.setOnAction(e -> {
                String searchTerm = patientSearchField.getText().trim();
                if (searchTerm.isEmpty()) {
                    NotificationUtil.showError("Hata", "Lütfen hasta adı girin.");
                    return;
                }
                
                // Search patient
                Patient patient = null;
                
                java.util.List<Patient> patients = DatabaseQuery.searchPatientsByName(searchTerm);
                if (patients.isEmpty()) {
                    NotificationUtil.showError("Bulunamadı", "Bu isme sahip hasta bulunamadı.");
                    return;
                } else if (patients.size() == 1) {
                    patient = patients.get(0);
                } else {
                    // Show selection dialog for multiple results
                    var patientOptions = patients.stream().map(p -> p.getName() + " (" + p.getEmail() + ")").collect(java.util.stream.Collectors.toList());
                    var selectionDialog = new javafx.scene.control.ChoiceDialog<>(patientOptions.get(0), patientOptions);
                    selectionDialog.setTitle("Hasta Seçim");
                    selectionDialog.setHeaderText("Birden fazla hasta bulundu");
                    selectionDialog.setContentText("Hastayı seçin:");
                    
                    var result = selectionDialog.showAndWait();
                    if (result.isPresent()) {
                        int selectedIndex = patientOptions.indexOf(result.get());
                        patient = patients.get(selectedIndex);
                    } else {
                        return;
                    }
                }
                
                if (patient != null) {
                    selectedPatientId[0] = patient.getPatientId();
                    selectedPatientLabel.setText(patient.getName() + " (" + patient.getEmail() + ")");
                }
            });
            
            javafx.scene.layout.HBox searchBox = new javafx.scene.layout.HBox(10);
            searchBox.getChildren().addAll(patientSearchField, searchButton);
            grid.add(searchBox, 1, 0);
            grid.getChildren().remove(patientSearchField);
            
            dialog.getDialogPane().setContent(grid);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            
            // Handle OK button
            var result = dialog.showAndWait();
            result.ifPresent(response -> {
                if (response == ButtonType.OK) {
                    if (selectedPatientId[0] == -1) {
                        NotificationUtil.showError("Hata", "Lütfen bir hasta seçin.");
                        return;
                    }
                    if (datePicker.getValue() == null) {
                        NotificationUtil.showError("Hata", "Lütfen bir tarih seçin.");
                        return;
                    }
                    if (timeSlotCombo.getValue() == null) {
                        NotificationUtil.showError("Hata", "Lütfen bir saat seçin.");
                        return;
                    }
                    
                    // Create appointment
                    java.sql.Date appointmentDate = java.sql.Date.valueOf(datePicker.getValue());
                    String timeSlot = timeSlotCombo.getValue();
                    String notes = notesArea.getText().trim();
                    
                    boolean created = appointmentManager.createAppointment(
                        doctor.getDoctorId(),
                        selectedPatientId[0],
                        appointmentDate,
                        timeSlot,
                        "scheduled",
                        notes.isEmpty() ? null : notes
                    );
                    
                    if (created) {
                        NotificationUtil.showInfo("Başarılı", "Randevu başarıyla oluşturuldu.");
                        loadAppointments();
                    } else {
                        NotificationUtil.showError("Hata", "Randevu oluşturulamadı. Lütfen tekrar deneyin.");
                    }
                }
            });
        } catch (Exception e) {
            NotificationUtil.showError("Hata", "Randevu oluşturulurken bir hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void completeAppointment() {
        var user = Session.getCurrentUser();
        if (user == null) {
            NotificationUtil.showError("Hata", "Oturum bulunamadı.");
            return;
        }

        var doctor = DatabaseQuery.getDoctorByUserId(user.getUserId());
        if (doctor == null) {
            NotificationUtil.showError("Hata", "Doktor kaydı bulunamadı.");
            return;
        }

        // Bugünün randevularını al
        java.time.LocalDate today = java.time.LocalDate.now();
        var allAppointments = appointmentManager.viewAppointmentsByDoctor(doctor.getDoctorId());
        
        // Bugün olanları ve "scheduled" durumundakileri filtrele
        var todayScheduledAppointments = allAppointments.stream()
            .filter(a -> "scheduled".equalsIgnoreCase(a.getStatus()))
            .filter(a -> {
                try {
                    java.time.LocalDate appointmentDate = a.getAppointmentDate().toLocalDate();
                    return !appointmentDate.isAfter(today);
                } catch (Exception e) {
                    return false;
                }
            })
            .collect(java.util.stream.Collectors.toList());
        
        if (todayScheduledAppointments.isEmpty()) {
            NotificationUtil.showInfo("Bilgi", "Bugün veya daha önceki tarihlerde tamamlanmamış randevu bulunamadı.");
            return;
        }

        // Randevu seçimi için ChoiceDialog
        var appointmentOptions = todayScheduledAppointments.stream()
            .map(a -> "Randevu #" + a.getAppointmentId() + " - " + a.getPatientName() + " (" + 
                     a.getAppointmentDate() + " " + a.getTimeSlot() + ")")
            .collect(java.util.stream.Collectors.toList());
        
        var dialog = new javafx.scene.control.ChoiceDialog<>(appointmentOptions.get(0), appointmentOptions);
        dialog.setTitle("Randevu Tamamla");
        dialog.setHeaderText("Tamamlanacak randevuyu seçin");
        dialog.setContentText("Randevu:");
        
        var result = dialog.showAndWait();
        result.ifPresent(selectedOption -> {
            // Seçilen randevunun ID'sini çıkar
            int selectedIndex = appointmentOptions.indexOf(selectedOption);
            var selectedAppointment = todayScheduledAppointments.get(selectedIndex);
            
            // Detayları al
            javafx.scene.control.Dialog<Boolean> detailsDialog = new javafx.scene.control.Dialog<>();
            detailsDialog.setTitle("Randevu Detayları");
            detailsDialog.setHeaderText("Randevu sonuçlarını girin");
            
            javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new javafx.geometry.Insets(20));
            
            javafx.scene.control.TextArea diagnosisArea = new javafx.scene.control.TextArea();
            diagnosisArea.setPromptText("Tanı...");
            diagnosisArea.setPrefRowCount(3);
            
            javafx.scene.control.TextArea prescriptionArea = new javafx.scene.control.TextArea();
            prescriptionArea.setPromptText("Reçete...");
            prescriptionArea.setPrefRowCount(3);
            
            javafx.scene.control.TextArea notesArea = new javafx.scene.control.TextArea();
            notesArea.setPromptText("Notlar...");
            notesArea.setPrefRowCount(3);

            javafx.scene.control.TextArea testResultsArea = new javafx.scene.control.TextArea();
            testResultsArea.setPromptText("Test Sonuçları...");
            testResultsArea.setPrefRowCount(3);
            
            grid.add(new javafx.scene.control.Label("Tanı:"), 0, 0);
            grid.add(diagnosisArea, 1, 0);
            grid.add(new javafx.scene.control.Label("Reçete:"), 0, 1);
            grid.add(prescriptionArea, 1, 1);
            grid.add(new javafx.scene.control.Label("Notlar:"), 0, 2);
            grid.add(notesArea, 1, 2);
            grid.add(new javafx.scene.control.Label("Test Sonuçları:"), 0, 3);
            grid.add(testResultsArea, 1, 3);
            
            detailsDialog.getDialogPane().setContent(grid);
            detailsDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            detailsDialog.setResultConverter(bt -> bt == ButtonType.OK);
            
            var detailsResult = detailsDialog.showAndWait();
            if (detailsResult.isPresent() && detailsResult.get()) {
                String diagnosis = diagnosisArea.getText();
                String prescription = prescriptionArea.getText();
                String notes = notesArea.getText();
                String testResults = testResultsArea.getText();
                
                boolean detailsUpdated = DatabaseQuery.updateAppointmentDetails(
                    selectedAppointment.getAppointmentId(), 
                    diagnosis, 
                    prescription, 
                    notes
                );
                
                if (detailsUpdated) {
                    boolean statusUpdated = DatabaseQuery.updateAppointmentStatus(selectedAppointment.getAppointmentId(), "completed");
                    if (statusUpdated) {
                        // Create Medical Record automatically
                        String medicalNotes = "Tanı: " + diagnosis + "\n" + notes;
                        boolean recordCreated = DatabaseQuery.createMedicalRecord(
                            selectedAppointment.getPatientId(),
                            doctor.getDoctorId(),
                            selectedAppointment.getAppointmentId(),
                            selectedAppointment.getHospitalId(),
                            testResults, // test_results
                            prescription, // medications
                            medicalNotes // notes
                        );

                        if (recordCreated) {
                            NotificationUtil.showInfo("Başarılı", "Randevu tamamlandı ve tıbbi kayıt oluşturuldu.");
                        } else {
                            NotificationUtil.showInfo("Uyarı", "Randevu tamamlandı fakat tıbbi kayıt oluşturulamadı.");
                        }
                        
                        loadAppointments(); // Listeyi yenile
                    } else {
                        NotificationUtil.showError("Hata", "Randevu durumu güncellenemedi.");
                    }
                } else {
                    NotificationUtil.showError("Hata", "Randevu detayları kaydedilemedi.");
                }
            }
        });
    }

    @FXML
    private void handleLogout() {
        try {
            Session.clear();
            Stage currentStage = (Stage) doctorNameLabel.getScene().getWindow();
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

    // SRS-HMS-004: View reviews and ratings
    @FXML
    private void handleViewReviews() {
        var user = Session.getCurrentUser();
        if (user == null) {
            NotificationUtil.showError("Hata", "Oturum bulunamadı.");
            return;
        }
        
        var doctor = DatabaseQuery.getDoctorByUserId(user.getUserId());
        if (doctor == null) {
            NotificationUtil.showError("Hata", "Doktor kaydı bulunamadı.");
            return;
        }
        
        // Get reviews for this doctor
        var reviews = DatabaseQuery.getReviewsByDoctor(doctor.getDoctorId());
        double avgRating = DatabaseQuery.getAverageRatingForDoctor(doctor.getDoctorId());
        
        if (reviews.isEmpty()) {
            NotificationUtil.showInfo("Bilgi", "Henüz değerlendirme bulunmamaktadır.");
            return;
        }
        
        // Display reviews
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Ortalama Puan: %.2f / 5.0 (%d değerlendirme)\n", avgRating, reviews.size()));
        sb.append("=".repeat(50)).append("\n\n");
        
        for (var review : reviews) {
            sb.append("Tarih: ").append(review.getReviewDate()).append("\n");
            sb.append("Hasta: ").append(review.getPatientName()).append("\n");
            sb.append("Puan: ").append("★".repeat(review.getRating()))
              .append("☆".repeat(5 - review.getRating()))
              .append(" (").append(review.getRating()).append("/5)\n");
            sb.append("Yorum: ").append(review.getComment()).append("\n");
            sb.append("-".repeat(50)).append("\n\n");
        }
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Hasta Değerlendirmeleri");
        alert.setHeaderText("Sizin hakkınızdaki değerlendirmeler");
        
        TextArea ta = new TextArea(sb.toString());
        ta.setEditable(false);
        ta.setWrapText(true);
        ta.setPrefRowCount(15);
        
        alert.getDialogPane().setContent(ta);
        alert.showAndWait();
    }

    /**
     * Create a new medical record for a patient
     * SRS-HMS-007: Doctors create medical records for patients they have treated
     */
    @FXML
    private void handleCreateMedicalRecord() {
        var user = Session.getCurrentUser();
        if (user == null) {
            NotificationUtil.showError("Hata", "Oturum bulunamadı.");
            return;
        }

        var doctor = DatabaseQuery.getDoctorByUserId(user.getUserId());
        if (doctor == null) {
            NotificationUtil.showError("Hata", "Doktor kaydı bulunamadı.");
            return;
        }

        // Get completed appointments
        var appts = appointmentManager.viewAppointmentsByDoctor(doctor.getDoctorId());
        java.util.List<Appointment> completedAppts = new java.util.ArrayList<>();
        for (var ap : appts) {
            if ("completed".equalsIgnoreCase(ap.getStatus())) {
                completedAppts.add(ap);
            }
        }
        
        if (completedAppts.isEmpty()) {
            NotificationUtil.showInfo("Bilgi", "Henüz tamamlanmış randevunuz bulunmamaktadır.");
            return;
        }

        // Select appointment
        java.util.List<String> items = new java.util.ArrayList<>();
        for (var ap : completedAppts) {
            items.add(ap.getAppointmentId() + " - " + ap.getAppointmentDate() + " " + ap.getTimeSlot() + " - " + ap.getPatientName());
        }
        
        javafx.scene.control.ChoiceDialog<String> apptDialog = 
            new javafx.scene.control.ChoiceDialog<>(items.get(0), items);
        apptDialog.setTitle("Randevu Seç");
        apptDialog.setHeaderText("Tıbbi kayıt oluşturmak istediğiniz randevuyu seçin");
        var apptRes = apptDialog.showAndWait();
        if (apptRes.isEmpty()) return;
        
        int appointmentId = Integer.parseInt(apptRes.get().split(" - ")[0].trim());
        Appointment selectedAppt = completedAppts.stream().filter(a -> a.getAppointmentId() == appointmentId).findFirst().orElse(null);
        if (selectedAppt == null) return;

        // Create medical record dialog
        javafx.scene.control.Dialog<Boolean> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Yeni Tıbbi Kayıt");
        dialog.setHeaderText("Hasta için tıbbi kayıt oluşturun");
        
        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new javafx.geometry.Insets(20));
        
        javafx.scene.control.TextArea diagnosisArea = new javafx.scene.control.TextArea();
        diagnosisArea.setPromptText("Tanı bilgisi...");
        diagnosisArea.setPrefRowCount(2);
        diagnosisArea.setWrapText(true);
        
        javafx.scene.control.TextArea testArea = new javafx.scene.control.TextArea();
        testArea.setPromptText("Test sonuçları...");
        testArea.setPrefRowCount(3);
        testArea.setWrapText(true);
        
        javafx.scene.control.TextArea medArea = new javafx.scene.control.TextArea();
        medArea.setPromptText("Reçete edilen ilaçlar...");
        medArea.setPrefRowCount(3);
        medArea.setWrapText(true);
        
        javafx.scene.control.TextArea notesArea = new javafx.scene.control.TextArea();
        notesArea.setPromptText("Doktor notları...");
        notesArea.setPrefRowCount(3);
        notesArea.setWrapText(true);
        
        grid.add(new javafx.scene.control.Label("Tanı:"), 0, 0);
        grid.add(diagnosisArea, 1, 0);
        grid.add(new javafx.scene.control.Label("Test Sonuçları:"), 0, 1);
        grid.add(testArea, 1, 1);
        grid.add(new javafx.scene.control.Label("İlaçlar:"), 0, 2);
        grid.add(medArea, 1, 2);
        grid.add(new javafx.scene.control.Label("Notlar:"), 0, 3);
        grid.add(notesArea, 1, 3);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResultConverter(bt -> bt == ButtonType.OK);
        
        DialogUtil.attachOkValidation(dialog, () -> {
            if (testArea.getText().trim().isEmpty()) {
                ValidationUtil.showError("Doğrulama", "Test sonuçları boş olamaz.");
                return false;
            }
            if (medArea.getText().trim().isEmpty()) {
                ValidationUtil.showError("Doğrulama", "İlaç bilgisi boş olamaz.");
                return false;
            }
            if (notesArea.getText().trim().isEmpty()) {
                ValidationUtil.showError("Doğrulama", "Notlar boş olamaz.");
                return false;
            }
            return true;
        });
        
        var result = dialog.showAndWait();
        if (result.isPresent() && result.get()) {
            String diagnosis = diagnosisArea.getText().trim();
            String testResults = testArea.getText().trim();
            String medications = medArea.getText().trim();
            String notes = notesArea.getText().trim();
            
            // Update appointment with diagnosis and prescription
            DatabaseQuery.updateAppointmentDetails(appointmentId, diagnosis, medications, notes);

            // Create medical record
            boolean created = hospitalManager.assignRecord(
                selectedAppt.getPatientId(), 
                doctor.getDoctorId(), 
                appointmentId,
                doctor.getHospitalId(),
                testResults,
                medications,
                notes
            );
            
            if (created) {
                NotificationUtil.showInfo("Başarı", "Tıbbi kayıt başarıyla oluşturuldu.");
            } else {
                NotificationUtil.showError("Hata", "Tıbbi kayıt oluşturulamadı.");
            }
        }
    }
}
