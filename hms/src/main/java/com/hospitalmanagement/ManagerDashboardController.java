package com.hospitalmanagement;

import com.hospitalmanagement.service.HospitalManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;

public class ManagerDashboardController {

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
        if (staffTableView != null && staffTableView.getColumns().size() > 0) {
            ((TableColumn<Doctor, Integer>) staffTableView.getColumns().get(0))
                    .setCellValueFactory(new PropertyValueFactory<>("doctorId"));
            ((TableColumn<Doctor, String>) staffTableView.getColumns().get(1))
                    .setCellValueFactory(new PropertyValueFactory<>("name"));
            ((TableColumn<Doctor, String>) staffTableView.getColumns().get(2))
                    .setCellValueFactory(new PropertyValueFactory<>("email"));
            ((TableColumn<Doctor, String>) staffTableView.getColumns().get(3))
                    .setCellValueFactory(new PropertyValueFactory<>("specialtyName"));
        }

        if (appointmentTableView != null && appointmentTableView.getColumns().size() > 0) {
            ((TableColumn<Appointment, Integer>) appointmentTableView.getColumns().get(0))
                    .setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
            ((TableColumn<Appointment, String>) appointmentTableView.getColumns().get(1))
                    .setCellValueFactory(new PropertyValueFactory<>("doctorName"));
            ((TableColumn<Appointment, String>) appointmentTableView.getColumns().get(2))
                    .setCellValueFactory(new PropertyValueFactory<>("patientName"));
            ((TableColumn<Appointment, String>) appointmentTableView.getColumns().get(3))
                    .setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));
            ((TableColumn<Appointment, String>) appointmentTableView.getColumns().get(4))
                    .setCellValueFactory(new PropertyValueFactory<>("timeSlot"));
            ((TableColumn<Appointment, String>) appointmentTableView.getColumns().get(5))
                    .setCellValueFactory(new PropertyValueFactory<>("status"));
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
        String stats = String.format("Toplam Hastane: %d\nToplam Doktor/Personel: %d", hospitals.size(),
                doctors.size());
        if (statisticsLabel != null) {
            statisticsLabel.setText(stats);
        }
    }

    @FXML
    private void handleLogout() {
        try {
            Session.clear();
            App.setRoot("login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}