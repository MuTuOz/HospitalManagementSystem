package com.hospitalmanagement;

import com.hospitalmanagement.service.AppointmentManager;
import com.hospitalmanagement.service.HospitalManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.util.List;

public class CreateAppointmentController {
    
    // Service layer instances
    private final AppointmentManager appointmentManager = new AppointmentManager();
    private final HospitalManager hospitalManager = new HospitalManager();

    @FXML private ComboBox<String> cityCombo;
    @FXML private ComboBox<String> hospitalCombo;
    @FXML private ComboBox<String> specialtyCombo;
    @FXML private ComboBox<String> doctorCombo;
    @FXML private ListView<String> availabilityList;
    @FXML private Button filterButton;
    @FXML private Button createButton;
    @FXML private Button closeButton;
    @FXML private TextArea notesArea;

    private int selectedHospitalId = -1;
    private int selectedDoctorId = -1;

    @FXML
    private void initialize() {
        List<String> cities = DatabaseQuery.getAllCities();
        cityCombo.setItems(FXCollections.observableArrayList(cities));
        List<String> specs = DatabaseQuery.getAllSpecialtyNames();
        specialtyCombo.setItems(FXCollections.observableArrayList(specs));

        cityCombo.setOnAction(e -> onCityChanged());
        hospitalCombo.setOnAction(e -> onHospitalChanged());
        specialtyCombo.setOnAction(e -> onSpecialtyChanged());
        doctorCombo.setOnAction(e -> onDoctorChanged());

        filterButton.setOnAction(e -> onFilter());
        createButton.setOnAction(e -> onCreate());
        closeButton.setOnAction(e -> ((Stage)closeButton.getScene().getWindow()).close());
    }

    private void onCityChanged() {
        String city = cityCombo.getValue();
        if (city == null) return;
        var hospitals = DatabaseQuery.getHospitalsByCity(city);
        hospitalCombo.getItems().clear();
        for (var h : hospitals) hospitalCombo.getItems().add(h.getName() + " - id:" + h.getHospitalId());
    }

    private void onHospitalChanged() {
        String sel = hospitalCombo.getValue();
        if (sel == null) return;
        try { selectedHospitalId = Integer.parseInt(sel.replaceAll(".*id:", "")); } catch (Exception ex) { selectedHospitalId = -1; }
    }

    private void onSpecialtyChanged() {
        // load doctors for specialty if hospital selected
        if (selectedHospitalId <= 0) return;
        String spec = specialtyCombo.getValue();
        if (spec == null) return;
        Integer specId = DatabaseQuery.getSpecialtyIdByName(spec);
        doctorCombo.getItems().clear();
        if (specId == null) return;
        var doctors = DatabaseQuery.getDoctorsBySpecialtyAndHospital(specId, selectedHospitalId);
        for (var d : doctors) doctorCombo.getItems().add(d.getName() + " - id:" + d.getDoctorId());
    }

    private void onDoctorChanged() {
        String sel = doctorCombo.getValue();
        if (sel == null) return;
        try { selectedDoctorId = Integer.parseInt(sel.replaceAll(".*id:", "")); } catch (Exception ex) { selectedDoctorId = -1; }
        loadAvailabilities();
    }

    private void onFilter() {
        loadAvailabilities();
    }

    private void loadAvailabilities() {
        availabilityList.getItems().clear();
        System.out.println("=== AVAILABILITY YÜKLEME ===");
        System.out.println("selectedDoctorId: " + selectedDoctorId);
        System.out.println("selectedHospitalId: " + selectedHospitalId);
        
        if (selectedDoctorId <= 0 || selectedHospitalId <= 0) {
            System.out.println("Doctor veya Hospital seçilmemiş!");
            return;
        }
        
        var avails = DatabaseQuery.getAvailabilitiesByDoctor(selectedDoctorId);
        System.out.println("Toplam " + avails.size() + " availability bulundu");
        
        int count = 0;
        for (var ao : avails) {
            System.out.println("Availability: ID=" + ao.getAvailabilityId() + 
                             ", HospitalID=" + ao.getHospitalId() + 
                             ", Date=" + ao.getDate() + 
                             ", Time=" + ao.getTimeSlot());
            if (ao.getHospitalId() == selectedHospitalId) {
                availabilityList.getItems().add(ao.toString());
                count++;
            }
        }
        System.out.println("Listeye " + count + " availability eklendi");
    }

    private void onCreate() {
        System.out.println("=== RANDEVU OLUŞTURMA ===");
        String sel = availabilityList.getSelectionModel().getSelectedItem();
        System.out.println("Seçili availability: " + sel);
        
        if (sel == null) {
            NotificationUtil.showWarning("Seçim", "Lütfen bir uygunluk seçin.");
            return;
        }
        int availabilityId = -1;
        try { 
            availabilityId = Integer.parseInt(sel.split(" - ")[0].trim()); 
            System.out.println("Availability ID: " + availabilityId);
        } catch (Exception ex) { 
            System.out.println("Availability ID parse hatası: " + ex.getMessage());
        }
        
        if (availabilityId <= 0) {
            NotificationUtil.showError("Hata", "Seçili uygunluk geçersiz.");
            return;
        }
        
        var user = Session.getCurrentUser();
        if (user == null) { 
            System.out.println("Session user null!");
            NotificationUtil.showError("Hata", "Oturum bulunamadı."); 
            return; 
        }
        
        System.out.println("User ID: " + user.getUserId());
        var patient = DatabaseQuery.getPatientByUserId(user.getUserId());
        
        if (patient == null) { 
            System.out.println("Patient kaydı bulunamadı! UserID: " + user.getUserId());
            NotificationUtil.showError("Hata", "Hasta kaydı bulunamadı."); 
            return; 
        }
        
        System.out.println("Patient ID: " + patient.getPatientId());
        System.out.println("Doctor ID: " + selectedDoctorId);
        System.out.println("Hospital ID: " + selectedHospitalId);

        // First check if patient has a cancelled appointment for this availability
        int cancelledAppointmentId = DatabaseQuery.getCancelledAppointmentByAvailability(
            patient.getPatientId(), availabilityId);
        
        boolean ok = false;
        if (cancelledAppointmentId > 0) {
            // Reactivate the cancelled appointment instead of creating new one
            System.out.println("İptal edilmiş randevu bulundu, tekrar aktifleştiriliyor: " + cancelledAppointmentId);
            ok = appointmentManager.reactivateAppointment(cancelledAppointmentId, user.getUserId());
            if (ok) {
                NotificationUtil.showInfo("Randevu", "İptal ettiğiniz randevunuz tekrar aktifleştirildi.");
            }
        } else {
            // Create new appointment
            ok = appointmentManager.createAppointment(patient.getPatientId(), selectedDoctorId, availabilityId, selectedHospitalId, notesArea.getText());
            if (ok) {
                NotificationUtil.showInfo("Randevu", "Randevunuz oluşturuldu.");
            }
        }
        
        System.out.println("Randevu oluşturma/aktifleştirme sonucu: " + ok);
        
        if (ok) {
            ((Stage)createButton.getScene().getWindow()).close();
        } else {
            NotificationUtil.showError("Randevu Hatası", "Randevu oluşturulamadı. Lütfen tekrar deneyin.");
        }
    }
}
