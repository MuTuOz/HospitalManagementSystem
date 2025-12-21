package com.hospitalmanagement;

import com.hospitalmanagement.service.AppointmentManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class ManagerCreateAppointmentController {
    
    private final AppointmentManager appointmentManager = new AppointmentManager();

    @FXML private TextField patientSearchField;
    @FXML private Label selectedPatientLabel;
    @FXML private ComboBox<String> cityCombo;
    @FXML private ComboBox<String> hospitalCombo;
    @FXML private ComboBox<String> specialtyCombo;
    @FXML private ComboBox<String> doctorCombo;
    @FXML private HBox dateTabsContainer;
    @FXML private Accordion timeSlotsAccordion;
    @FXML private Label selectedDateLabel;
    @FXML private Label selectedDoctorLabel;
    @FXML private Label hospitalInfoLabel;
    @FXML private Button createButton;
    @FXML private Button closeButton;
    @FXML private TextArea notesArea;

    private int selectedHospitalId = -1;
    private int selectedDoctorId = -1;
    private int selectedPatientId = -1;
    private LocalDate selectedDate = null;
    private AvailabilityOption selectedSlot = null;

    private final Map<Integer, AvailabilityOption> availabilityById = new HashMap<>();
    private List<AvailabilityOption> currentAvailabilities = new ArrayList<>();
    
    private Button lastSelectedDateButton = null;
    private Button lastSelectedSlotButton = null;

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
        createButton.setDisable(true);
    }

    @FXML
    private void searchPatient() {
        String searchTerm = patientSearchField.getText().trim();
        if (searchTerm.isEmpty()) {
            NotificationUtil.showError("Hata", "Lütfen hasta adı girin.");
            return;
        }

        // Search by name
        Patient patient = null;
        List<Patient> patients = DatabaseQuery.searchPatientsByName(searchTerm);
        
        if (patients.isEmpty()) {
            NotificationUtil.showError("Bulunamadı", "Bu isme sahip hasta bulunamadı.");
            return;
        } else if (patients.size() == 1) {
            patient = patients.get(0);
        } else {
            // Multiple patients found, show selection dialog
            patient = showPatientSelectionDialog(patients);
        }

        if (patient != null) {
            selectedPatientId = patient.getPatientId();
            selectedPatientLabel.setText(patient.getName() + " (" + patient.getEmail() + ")");
            updateCreateButtonState();
        }
    }

    private Patient showPatientSelectionDialog(List<Patient> patients) {
        ChoiceDialog<Patient> dialog = new ChoiceDialog<>(patients.get(0), patients);
        dialog.setTitle("Hasta Seçimi");
        dialog.setHeaderText("Birden fazla hasta bulundu. Lütfen birini seçin:");
        dialog.setContentText("Hasta:");
        
        // Custom converter to show patient info
        dialog.getItems().forEach(p -> {
            // This will be shown in the dialog
        });

        Optional<Patient> result = dialog.showAndWait();
        return result.orElse(null);
    }

    private void onCityChanged() {
        String city = cityCombo.getValue();
        if (city == null || city.isEmpty()) return;
        
        List<Hospital> hospitals = DatabaseQuery.getHospitalsByCity(city);
        List<String> names = hospitals.stream().map(Hospital::getName).collect(Collectors.toList());
        hospitalCombo.setItems(FXCollections.observableArrayList(names));
        hospitalCombo.setValue(null);
        doctorCombo.setValue(null);
        clearAvailabilities();
    }

    private void onHospitalChanged() {
        String hospitalName = hospitalCombo.getValue();
        if (hospitalName == null || hospitalName.isEmpty()) return;
        
        Hospital h = DatabaseQuery.getHospitalByName(hospitalName);
        if (h != null) {
            selectedHospitalId = h.getHospitalId();
            hospitalInfoLabel.setText(hospitalName);
            loadDoctorsForCurrentFilters();
        }
    }

    private void onSpecialtyChanged() {
        loadDoctorsForCurrentFilters();
    }

    private void loadDoctorsForCurrentFilters() {
        String specialty = specialtyCombo.getValue();
        if (selectedHospitalId == -1 || specialty == null || specialty.isEmpty()) return;
        
        List<Doctor> doctors = DatabaseQuery.getDoctorsByHospitalAndSpecialty(selectedHospitalId, specialty);
        List<String> names = doctors.stream().map(Doctor::getName).collect(Collectors.toList());
        doctorCombo.setItems(FXCollections.observableArrayList(names));
        doctorCombo.setValue(null);
        clearAvailabilities();
    }

    private void onDoctorChanged() {
        String doctorName = doctorCombo.getValue();
        if (doctorName == null || doctorName.isEmpty()) {
            clearAvailabilities();
            return;
        }
        
        Doctor doc = DatabaseQuery.getDoctorByName(doctorName);
        if (doc != null) {
            selectedDoctorId = doc.getDoctorId();
            selectedDoctorLabel.setText("Doktor: " + doctorName);
            loadAvailabilities();
        }
    }

    private void loadAvailabilities() {
        if (selectedDoctorId == -1 || selectedHospitalId == -1) return;
        
        List<AvailabilityOption> availabilities = DatabaseQuery.getAvailableSlots(selectedDoctorId, selectedHospitalId);
        currentAvailabilities = availabilities;
        
        if (availabilities.isEmpty()) {
            NotificationUtil.showInfo("Bilgi", "Bu doktor için uygun randevu bulunmamaktadır.");
            clearAvailabilities();
            return;
        }
        
        // Group by date
        Map<Date, List<AvailabilityOption>> dateMap = availabilities.stream()
            .collect(Collectors.groupingBy(AvailabilityOption::getDate, TreeMap::new, Collectors.toList()));
        
        // Create date tabs
        dateTabsContainer.getChildren().clear();
        dateTabsContainer.setSpacing(10);
        dateTabsContainer.setAlignment(Pos.CENTER_LEFT);
        
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy - EEEE", new Locale("tr"));
        
        for (Date sqlDate : dateMap.keySet()) {
            LocalDate date = sqlDate.toLocalDate();
            Button dateTab = new Button(date.format(dateFormatter));
            dateTab.getStyleClass().add("date-tab");
            dateTab.setPrefHeight(40);
            dateTab.setOnAction(e -> selectDate(date, dateTab));
            dateTabsContainer.getChildren().add(dateTab);
        }
        
        // Select first date by default
        if (!dateMap.isEmpty()) {
            LocalDate firstDate = dateMap.keySet().iterator().next().toLocalDate();
            Button firstButton = (Button) dateTabsContainer.getChildren().get(0);
            selectDate(firstDate, firstButton);
        }
    }

    private void selectDate(LocalDate date, Button clickedButton) {
        this.selectedDate = date;
        this.selectedSlot = null;
        
        // Update button styles
        if (lastSelectedDateButton != null) {
            lastSelectedDateButton.getStyleClass().remove("date-tab-selected");
        }
        clickedButton.getStyleClass().add("date-tab-selected");
        lastSelectedDateButton = clickedButton;
        
        // Filter availabilities for this date
        Date sqlDate = Date.valueOf(date);
        List<AvailabilityOption> dateSlots = currentAvailabilities.stream()
            .filter(a -> a.getDate().equals(sqlDate))
            .collect(Collectors.toList());
        
        // Group by hour
        Map<Integer, List<AvailabilityOption>> hourlySlots = dateSlots.stream()
            .collect(Collectors.groupingBy(
                a -> {
                    String time = a.getTimeSlot();
                    String[] parts = time.split(":");
                    return Integer.parseInt(parts[0]);
                },
                TreeMap::new,
                Collectors.toList()
            ));
        
        // Create accordion panels
        timeSlotsAccordion.getPanes().clear();
        
        for (Map.Entry<Integer, List<AvailabilityOption>> entry : hourlySlots.entrySet()) {
            int hour = entry.getKey();
            List<AvailabilityOption> slots = entry.getValue();
            
            TitledPane pane = new TitledPane();
            pane.setText(String.format("%02d:00", hour));
            pane.getStyleClass().add("time-hour-pane");
            
            FlowPane flowPane = new FlowPane();
            flowPane.setHgap(10);
            flowPane.setVgap(10);
            flowPane.setPadding(new Insets(15));
            flowPane.setAlignment(Pos.CENTER_LEFT);
            
            for (AvailabilityOption slot : slots) {
                Button slotBtn = new Button(slot.getTimeSlot());
                slotBtn.getStyleClass().add("time-slot-button");
                slotBtn.setPrefWidth(80);
                slotBtn.setPrefHeight(35);
                slotBtn.setOnAction(e -> selectSlot(slot, slotBtn));
                flowPane.getChildren().add(slotBtn);
            }
            
            pane.setContent(flowPane);
            timeSlotsAccordion.getPanes().add(pane);
        }
        
        // Expand first pane by default
        if (!timeSlotsAccordion.getPanes().isEmpty()) {
            timeSlotsAccordion.setExpandedPane(timeSlotsAccordion.getPanes().get(0));
        }
        
        // Clear selection display
        selectedDateLabel.setText("Tarih ve saat seçiniz");
        updateCreateButtonState();
    }

    private void selectSlot(AvailabilityOption slot, Button clickedButton) {
        this.selectedSlot = slot;
        
        // Update button styles
        if (lastSelectedSlotButton != null) {
            lastSelectedSlotButton.getStyleClass().remove("time-slot-selected");
        }
        clickedButton.getStyleClass().add("time-slot-selected");
        lastSelectedSlotButton = clickedButton;
        
        // Update selection display
        String displayText = slot.getDate().toLocalDate().format(
            DateTimeFormatter.ofPattern("dd.MM.yyyy - EEEE", new Locale("tr"))
        ) + " - " + slot.getTimeSlot();
        
        selectedDateLabel.setText(displayText);
        updateCreateButtonState();
    }

    private void clearAvailabilities() {
        dateTabsContainer.getChildren().clear();
        timeSlotsAccordion.getPanes().clear();
        selectedDateLabel.setText("Tarih ve saat seçiniz");
        selectedDoctorLabel.setText("Doktor: -");
        hospitalInfoLabel.setText("Hastane: -");
        selectedDate = null;
        selectedSlot = null;
        lastSelectedDateButton = null;
        lastSelectedSlotButton = null;
        updateCreateButtonState();
        currentAvailabilities.clear();
    }

    private void updateCreateButtonState() {
        createButton.setDisable(selectedPatientId == -1 || selectedSlot == null);
    }

    @FXML
    private void handleCreate() {
        if (selectedSlot == null) {
            NotificationUtil.showError("Hata", "Lütfen bir tarih ve saat seçiniz.");
            return;
        }

        if (selectedPatientId == -1) {
            NotificationUtil.showError("Hata", "Lütfen bir hasta seçiniz.");
            return;
        }

        String notes = notesArea.getText();
        boolean success = appointmentManager.createAppointment(
            selectedPatientId, 
            selectedDoctorId, 
            selectedSlot.getAvailabilityId(), 
            selectedHospitalId, 
            notes
        );

        if (success) {
            NotificationUtil.showInfo("Başarılı", "Randevu başarıyla oluşturuldu.");
            closeDialog();
        } else {
            NotificationUtil.showError("Hata", "Randevu oluşturulurken bir hata oluştu.");
        }
    }

    @FXML
    private void handleClose() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
