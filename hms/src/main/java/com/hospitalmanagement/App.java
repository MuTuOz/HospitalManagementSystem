package com.hospitalmanagement;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static Stage stage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        stage = primaryStage;
        scene = new Scene(loadFXML("login"), 900, 600);
        try {
            scene.getStylesheets().add(App.class.getResource("theme.css").toExternalForm());
        } catch (Exception e) {
            System.out.println("Tema yüklenemedi: " + e.getMessage());
        }
        
        // Set application icon
        try {
            stage.getIcons().add(new javafx.scene.image.Image(
                App.class.getResourceAsStream("icon.png")));
        } catch (Exception e) {
            System.out.println("Icon yüklenemedi: " + e.getMessage());
        }
        
        stage.setTitle("Hospital Management System");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        // Start scheduled backup (if configured)
        try {
            BackupScheduler.start();
        } catch (Exception e) {
            System.out.println("BackupScheduler başlatılamadı: " + e.getMessage());
        }
    }

    static void setRoot(String fxml) throws IOException {
        System.out.println("App.setRoot() çağrılıyor: " + fxml);
        Parent root = loadFXML(fxml);
        scene.setRoot(root);
        System.out.println("Root değiştirildi: " + fxml);
    }
    
    static Scene getScene() {
        return scene;
    }
    
    static Stage getStage() {
        return stage;
    }

    private static Parent loadFXML(String fxml) throws IOException {
        System.out.println("FXML yükleniyor: " + fxml + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        Parent parent = fxmlLoader.load();
        System.out.println("FXML başarıyla yüklendi: " + fxml);
        return parent;
    }

    public static void main(String[] args) {
        launch();
    }

}
