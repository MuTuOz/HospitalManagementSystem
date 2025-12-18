package com.hospitalmanagement;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * GUIManager - Manages scene switching and navigation in the HMS application
 * Following UML Class Diagram specification from Software Detailed Design
 */
public class GUIManager {
    private Stage stage;
    private Scene scene;

    public GUIManager(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
        if (stage != null) {
            stage.setScene(scene);
        }
    }

    /**
     * Switch to login screen
     */
    public void switchToLogin() throws IOException {
        loadAndSwitch("primary", "Hospital Management System - Login");
    }

    /**
     * Switch to patient dashboard
     */
    public void switchToPatient() throws IOException {
        loadAndSwitch("patient-dashboard", "HMS - Patient Dashboard");
    }

    /**
     * Switch to doctor dashboard
     */
    public void switchToDoctor() throws IOException {
        loadAndSwitch("doctor-dashboard", "HMS - Doctor Dashboard");
    }

    /**
     * Switch to manager dashboard
     */
    public void switchToManager() throws IOException {
        loadAndSwitch("manager-dashboard", "HMS - Manager Dashboard");
    }

    /**
     * Switch to admin dashboard
     */
    public void switchToAdmin() throws IOException {
        loadAndSwitch("admin-dashboard", "HMS - Admin Dashboard");
    }

    /**
     * Generic method to load FXML and switch scene
     */
    private void loadAndSwitch(String fxmlName, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlName + ".fxml"));
        Parent root = loader.load();
        
        // Set minimum scene size for login/primary screen
        if ("primary".equals(fxmlName) || "login".equals(fxmlName)) {
            // If user is coming from a maximized dashboard, reset window state first.
            stage.setMaximized(false);
            stage.setFullScreen(false);
            scene = new Scene(root, 900, 600);
            applyTheme(scene);
            stage.setScene(scene);
            // Match the startup login window title exactly.
            stage.setTitle("Hospital Management System");
            stage.setResizable(false);
            stage.sizeToScene();
            stage.centerOnScreen();
        } else {
            // Dashboard screens - start maximized but allow resizing
            scene = new Scene(root, 1024, 768);
            applyTheme(scene);
            stage.setScene(scene);
            stage.setTitle(title);
            stage.setResizable(true);
            stage.setMaximized(true);
        }
        
        stage.show();
    }

    private void applyTheme(Scene scene) {
        try {
            scene.getStylesheets().clear();
            scene.getStylesheets().add(App.class.getResource("theme.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("Tema yüklenemedi: " + e.getMessage());
        }
    }
    /**
     * Switch scene based on user role
     */
    public void switchToUserDashboard(User user) throws IOException {
        String roleName = user.getRoleName();
        if (roleName == null) {
            throw new IOException("User role not found");
        }
        
        switch (roleName.toLowerCase()) {
            case "patient":
                switchToPatient();
                break;
            case "doctor":
                switchToDoctor();
                break;
            case "manager":
                switchToManager();
                break;
            case "admin":
                switchToAdmin();
                break;
            default:
                throw new IOException("Unknown user role: " + roleName);
        }
    }
}
