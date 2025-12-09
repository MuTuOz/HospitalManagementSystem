package com.hospitalmanagement;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ManagerDashboardController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private void initialize() {
        welcomeLabel.setText("Manager Dashboard'a Hoş Geldiniz!");
    }

    @FXML
    private void handleLogout() {
        try {
            App.setRoot("login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}