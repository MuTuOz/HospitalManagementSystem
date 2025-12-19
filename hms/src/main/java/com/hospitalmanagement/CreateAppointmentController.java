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

public class CreateAppointmentController {

    private final AppointmentManager appointmentManager = new AppointmentManager();

    @FXML
    private ComboBox<String> cityCombo;
    @FXML
    private ComboBox<String> hospitalCombo;
    @FXML
    private ComboBox<String> specialtyCombo;
    @FXML
    private ComboBox<String> doctorCombo;
    @FXML
    private HBox dateTabsContainer;
    @FXML
    private ScrollPane dateScrollPane;
    @FXML
    private Button prevDateButton;
    @FXML
    private Button nextDateButton;
    @FXML
    private Accordion timeSlotsAccordion;
    @FXML
    private Label selectedDateLabel;
    @FXML
    private Label selectedDoctorLabel;
    @FXML
    private Label hospitalInfoLabel;
    @FXML
    private Button createButton;
    @FXML
    private Button closeButton;
    @FXML
    private TextArea notesArea;

    private int selectedHospitalId = -1;
    private int selectedDoctorId = -1;
    private LocalDate selectedDate = null;
    private AvailabilityOption selectedSlot = null;

    private final Map<Integer, AvailabilityOption> availabilityById = new HashMap<>();
    private List<AvailabilityOption> currentAvailabilities = new ArrayList<>();

    private Button lastSelectedDateButton = null;
    private Button lastSelectedSlotButton = null;

    @FXML
    private void initialize() {
        // Şehir için "Farketmez" seçeneği ekle
        List<String> cities = DatabaseQuery.getAllCities();
        cities.add(0, "Farketmez (Tüm Şehirler)");
        cityCombo.setItems(FXCollections.observableArrayList(cities));

        // Hastane için varsayılan "Farketmez" seçeneği
        hospitalCombo.setPromptText("Farketmez (Tüm Hastaneler)");

        // Uzmanlık için "Farketmez" seçeneği ekle
        List<String> specs = DatabaseQuery.getAllSpecialtyNames();
        specs.add(0, "Farketmez (Tüm Uzmanlıklar)");
        specialtyCombo.setItems(FXCollections.observableArrayList(specs));

        cityCombo.setOnAction(e -> onCityChanged());
        hospitalCombo.setOnAction(e -> onHospitalChanged());
        specialtyCombo.setOnAction(e -> onSpecialtyChanged());
        doctorCombo.setOnAction(e -> onDoctorChanged());
        createButton.setDisable(true);
    }

    private void onCityChanged() {
        String city = cityCombo.getValue();
        if (city == null || city.isEmpty())
            return;

        List<String> names;
        if (city.equals("Farketmez (Tüm Şehirler)")) {
            // Tüm hastaneleri getir
            List<Hospital> hospitals = DatabaseQuery.getAllHospitals();
            names = hospitals.stream().map(Hospital::getName).collect(Collectors.toList());
        } else {
            // Sadece seçili şehirdeki hastaneleri getir
            List<Hospital> hospitals = DatabaseQuery.getHospitalsByCity(city);
            names = hospitals.stream().map(Hospital::getName).collect(Collectors.toList());
        }

        // "Farketmez" seçeneğini listenin başına ekle
        names.add(0, "Farketmez (Tüm Hastaneler)");

        hospitalCombo.setItems(FXCollections.observableArrayList(names));
        hospitalCombo.setValue("Farketmez (Tüm Hastaneler)");
        doctorCombo.setValue(null);
        clearAvailabilities();
    }

    private void onHospitalChanged() {
        String hospitalName = hospitalCombo.getValue();
        if (hospitalName == null || hospitalName.isEmpty() || hospitalName.equals("Farketmez (Tüm Hastaneler)")) {
            // Hastane seçilmediyse veya "Farketmez" seçiliyse varsayılanı -1 yap
            selectedHospitalId = -1;
            hospitalInfoLabel.setText("Tüm Hastaneler");
            loadDoctorsForCurrentFilters();
            return;
        }

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
        if (specialty == null || specialty.isEmpty())
            return;

        System.out.println("loadDoctorsForCurrentFilters çağrıldı - specialty: " + specialty + ", hospitalId: "
                + selectedHospitalId);

        List<Doctor> doctors;
        boolean allSpecialties = specialty.equals("Farketmez (Tüm Uzmanlıklar)");

        if (selectedHospitalId == -1) {
            // Hastane seçilmemişse - şehir bazlı filtreleme yap
            String city = cityCombo.getValue();
            if (city != null && !city.isEmpty() && !city.equals("Farketmez (Tüm Şehirler)")) {
                // Belirli şehirdeki tüm hastaneleri al
                List<Hospital> hospitalsInCity = DatabaseQuery.getHospitalsByCity(city);
                doctors = new ArrayList<>();
                for (Hospital hospital : hospitalsInCity) {
                    List<Doctor> hospitalDoctors;
                    if (allSpecialties) {
                        // Tüm uzmanlıklar
                        hospitalDoctors = DatabaseQuery.getDoctorsByHospital(hospital.getHospitalId());
                    } else {
                        // Belirli uzmanlık
                        hospitalDoctors = DatabaseQuery.getDoctorsByHospitalAndSpecialty(hospital.getHospitalId(),
                                specialty);
                    }
                    for (Doctor d : hospitalDoctors) {
                        // Duplicate kontrolü
                        if (doctors.stream().noneMatch(existing -> existing.getDoctorId() == d.getDoctorId())) {
                            doctors.add(d);
                        }
                    }
                }
            } else {
                // Şehir de "Farketmez" seçiliyse tüm doktorları göster
                if (allSpecialties) {
                    doctors = DatabaseQuery.getAllDoctors();
                } else {
                    doctors = DatabaseQuery.getAllDoctors().stream()
                            .filter(d -> d.getSpecialtyName().equals(specialty))
                            .collect(Collectors.toList());
                }
            }
        } else {
            // Belirli hastane seçiliyse
            if (allSpecialties) {
                doctors = DatabaseQuery.getDoctorsByHospital(selectedHospitalId);
            } else {
                doctors = DatabaseQuery.getDoctorsByHospitalAndSpecialty(selectedHospitalId, specialty);
            }
        }

        System.out.println("Toplam bulunan doktor sayısı: " + doctors.size());

        // Sadece müsaitliği olan doktorları göster
        List<Doctor> doctorsWithAvailability = doctors.stream()
                .filter(d -> {
                    List<AvailabilityOption> slots;
                    if (selectedHospitalId == -1) {
                        slots = DatabaseQuery.getAllAvailableSlotsForDoctor(d.getDoctorId());
                    } else {
                        slots = DatabaseQuery.getAvailableSlots(d.getDoctorId(), selectedHospitalId);
                    }
                    boolean hasSlots = !slots.isEmpty();
                    System.out.println("  Doktor: " + d.getName() + " - Müsaitlik var mı: " + hasSlots
                            + " (slot sayısı: " + slots.size() + ")");
                    return hasSlots;
                })
                .collect(Collectors.toList());

        System.out.println("Müsaitliği olan doktor sayısı: " + doctorsWithAvailability.size());

        List<String> names = doctorsWithAvailability.stream().map(Doctor::getName).collect(Collectors.toList());
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
        if (selectedDoctorId == -1)
            return;

        List<AvailabilityOption> availabilities;
        if (selectedHospitalId == -1) {
            // Hastane seçilmemişse tüm hastanelerdeki slotları getir
            System.out.println("Hastane seçilmedi, tüm hastaneler aranacak. Doctor ID: " + selectedDoctorId);
            availabilities = DatabaseQuery.getAllAvailableSlotsForDoctor(selectedDoctorId);
            System.out.println("Bulunan availability sayısı: " + availabilities.size());
        } else {
            System.out.println(
                    "Hastane seçildi. Hospital ID: " + selectedHospitalId + ", Doctor ID: " + selectedDoctorId);
            availabilities = DatabaseQuery.getAvailableSlots(selectedDoctorId, selectedHospitalId);
            System.out.println("Bulunan availability sayısı: " + availabilities.size());
        }

        // Limit to next 30 days (1 month)
        LocalDate maxDate = LocalDate.now().plusDays(30);
        availabilities = availabilities.stream()
                .filter(a -> !a.getDate().toLocalDate().isAfter(maxDate))
                .collect(Collectors.toList());

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
                        Collectors.toList()));

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
        createButton.setDisable(true);
    }

    private void selectSlot(AvailabilityOption slot, Button clickedButton) {
        System.out.println("=== SLOT SEÇİLDİ ===");
        System.out.println("Slot: " + slot);
        System.out.println("Availability ID: " + slot.getAvailabilityId());
        System.out.println("Time: " + slot.getTimeSlot());

        this.selectedSlot = slot;

        // Update button styles
        if (lastSelectedSlotButton != null) {
            lastSelectedSlotButton.getStyleClass().remove("time-slot-selected");
        }
        clickedButton.getStyleClass().add("time-slot-selected");
        lastSelectedSlotButton = clickedButton;

        // Update selection display
        String displayText = slot.getDate().toLocalDate().format(
                DateTimeFormatter.ofPattern("dd.MM.yyyy - EEEE", new Locale("tr"))) + " - " + slot.getTimeSlot();

        selectedDateLabel.setText(displayText);
        createButton.setDisable(false);

        System.out.println("Buton aktif edildi, displayText: " + displayText);
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
        createButton.setDisable(true);
        currentAvailabilities.clear();
    }

    @FXML
    private void handleCreate() {
        System.out.println("=== RANDEVU OLUŞTUR BASILDI ===");
        System.out.println("selectedSlot: " + selectedSlot);
        System.out.println("selectedDoctorId: " + selectedDoctorId);
        System.out.println("selectedHospitalId: " + selectedHospitalId);

        if (selectedSlot == null) {
            System.out.println("HATA: Slot seçilmemiş!");
            NotificationUtil.showError("Hata", "Lütfen bir tarih ve saat seçiniz.");
            return;
        }

        User currentUser = Session.getCurrentUser();
        if (currentUser == null) {
            NotificationUtil.showError("Hata", "Oturum bilgisi bulunamadı.");
            return;
        }

        Patient patient = DatabaseQuery.getPatientByUserId(currentUser.getUserId());
        if (patient == null) {
            NotificationUtil.showError("Hata", "Hasta bilgisi bulunamadı.");
            return;
        }

        String notes = notesArea.getText();

        // Eğer hastane seçilmemişse, slot'un hastane ID'sini kullan
        int hospitalIdToUse = selectedHospitalId != -1 ? selectedHospitalId : selectedSlot.getHospitalId();

        System.out.println("Randevu oluşturma parametreleri:");
        System.out.println("  Patient ID: " + patient.getPatientId());
        System.out.println("  Doctor ID: " + selectedDoctorId);
        System.out.println("  Availability ID: " + selectedSlot.getAvailabilityId());
        System.out.println("  Hospital ID: " + hospitalIdToUse);
        System.out.println("  Notes: " + notes);

        boolean success = appointmentManager.createAppointment(
                patient.getPatientId(),
                selectedDoctorId,
                selectedSlot.getAvailabilityId(),
                hospitalIdToUse,
                notes);

        System.out.println("Randevu oluşturma sonucu: " + success);

        if (success) {
            NotificationUtil.showInfo("Başarılı", "Randevunuz başarıyla oluşturuldu.");
            // Parent dashboard'ı yenile (PatientDashboard ise)
            try {
                Stage stage = (Stage) createButton.getScene().getWindow();
                Object userData = stage.getScene().getRoot().getUserData();
                if (userData instanceof PatientDashboardController) {
                    PatientDashboardController pdc = (PatientDashboardController) userData;
                    pdc.refreshAppointments();
                }
            } catch (Exception e) {
                System.err.println("Dashboard yenileme hatası: " + e.getMessage());
            }
            closeDialog();
        } else {
            NotificationUtil.showError("Hata", "Randevu oluşturulurken bir hata oluştu.");
        }
    }

    @FXML
    private void handleClose() {
        closeDialog();
    }

    @FXML
    private void scrollDatesPrevious() {
        if (dateScrollPane != null) {
            double currentHValue = dateScrollPane.getHvalue();
            dateScrollPane.setHvalue(Math.max(0, currentHValue - 0.2));
        }
    }

    @FXML
    private void scrollDatesNext() {
        if (dateScrollPane != null) {
            double currentHValue = dateScrollPane.getHvalue();
            dateScrollPane.setHvalue(Math.min(1, currentHValue + 0.2));
        }
    }

    private void closeDialog() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }
}
