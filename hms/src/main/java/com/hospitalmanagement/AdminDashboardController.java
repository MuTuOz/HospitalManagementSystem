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

public class AdminDashboardController {

    private final UserService userService = new UserService();
    private final HospitalManager hospitalManager = new HospitalManager();

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label subtitleLabel;

    @FXML
    private TableView<Hospital> hospitalTableView;

    @FXML
    private TableView<User> usersTableView;

    @FXML
    private TableView<Doctor> doctorsTableView;

    @FXML
    private Label reportLabel;

    @FXML
    private void initialize() {
        welcomeLabel.setText("Admin Dashboard - Sistem Yönetimi");
        initColumns();
    }

    @SuppressWarnings("unchecked")
    private void initColumns() {
        if (hospitalTableView != null && hospitalTableView.getColumns().size() >= 5) {
            ((TableColumn<Hospital, Integer>) hospitalTableView.getColumns().get(0))
                    .setCellValueFactory(new PropertyValueFactory<>("hospitalId"));
            ((TableColumn<Hospital, String>) hospitalTableView.getColumns().get(1))
                    .setCellValueFactory(new PropertyValueFactory<>("name"));
            ((TableColumn<Hospital, String>) hospitalTableView.getColumns().get(2))
                    .setCellValueFactory(new PropertyValueFactory<>("address"));
            ((TableColumn<Hospital, String>) hospitalTableView.getColumns().get(3))
                    .setCellValueFactory(new PropertyValueFactory<>("phone"));
            ((TableColumn<Hospital, String>) hospitalTableView.getColumns().get(4))
                    .setCellValueFactory(new PropertyValueFactory<>("city"));
        }

        if (usersTableView != null && usersTableView.getColumns().size() >= 4) {
            ((TableColumn<User, Integer>) usersTableView.getColumns().get(0))
                    .setCellValueFactory(new PropertyValueFactory<>("userId"));
            ((TableColumn<User, String>) usersTableView.getColumns().get(1))
                    .setCellValueFactory(new PropertyValueFactory<>("name"));
            ((TableColumn<User, String>) usersTableView.getColumns().get(2))
                    .setCellValueFactory(new PropertyValueFactory<>("email"));
            ((TableColumn<User, String>) usersTableView.getColumns().get(3))
                    .setCellValueFactory(new PropertyValueFactory<>("roleName"));
        }

        if (doctorsTableView != null && doctorsTableView.getColumns().size() >= 3) {
            ((TableColumn<Doctor, Integer>) doctorsTableView.getColumns().get(0))
                    .setCellValueFactory(new PropertyValueFactory<>("doctorId"));
            ((TableColumn<Doctor, String>) doctorsTableView.getColumns().get(1))
                    .setCellValueFactory(new PropertyValueFactory<>("name"));
            ((TableColumn<Doctor, String>) doctorsTableView.getColumns().get(2))
                    .setCellValueFactory(new PropertyValueFactory<>("specialtyName"));
        }
    }

    @FXML
    private void loadHospitals() {
        var hospitals = hospitalManager.getAllHospitals();
        if (hospitals == null || hospitals.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Hiç hastane bulunamadı.", ButtonType.OK);
            a.setHeaderText("Hastaneler");
            a.showAndWait();
            return;
        }
        if (hospitalTableView != null)
            hospitalTableView.setItems(FXCollections.observableArrayList(hospitals));
    }

    @FXML
    private void refreshHospitals() {
        loadHospitals();
    }

    @FXML
    private void loadUsers() {
        var users = userService.getAllUsers();
        if (users == null || users.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Hiç kullanıcı bulunamadı.", ButtonType.OK);
            a.setHeaderText("Kullanıcılar");
            a.showAndWait();
            return;
        }
        if (usersTableView != null)
            usersTableView.setItems(FXCollections.observableArrayList(users));
    }

    @FXML
    private void refreshUsers() {
        loadUsers();
    }

    @FXML
    private void loadDoctors() {
        var doctors = DatabaseQuery.getAllDoctors();
        if (doctors == null || doctors.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.INFORMATION, "Hiç doktor bulunamadı.", ButtonType.OK);
            a.setHeaderText("Doktorlar");
            a.showAndWait();
            return;
        }
        if (doctorsTableView != null)
            doctorsTableView.setItems(FXCollections.observableArrayList(doctors));
    }

    @FXML
    private void refreshDoctors() {
        loadDoctors();
    }

    @FXML
    private void generateReports() {
        var hospitals = hospitalManager.getAllHospitals();
        var doctors = DatabaseQuery.getAllDoctors();
        var users = userService.getAllUsers();
        String report = String.format("Toplam Hastane: %d\nToplam Doktor: %d\nToplam Kullanıcı: %d", hospitals.size(),
                doctors.size(), users.size());
        if (reportLabel != null)
            reportLabel.setText(report);
        Alert a = new Alert(Alert.AlertType.INFORMATION, report, ButtonType.OK);
        a.setTitle("Sistem Raporu");
        a.setHeaderText("Kısa Sistem Özeti");
        a.showAndWait();
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
}