package com.hospitalmanagement;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DoctorDashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private void initialize() {
        welcomeLabel.setText("Doktor Dashboard'a Hoş Geldiniz!");
    }

    @FXML
    private void handleLogout() {

        try {
            System.out.println("Doktor çıkış yaptı.");
            App.setRoot("login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}