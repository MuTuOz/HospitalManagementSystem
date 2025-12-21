package com.hospitalmanagement;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class NotificationUtil {
    public static void showInfo(String title, String message) {
        Platform.runLater(() -> {
            Alert a = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
            a.setHeaderText(title);
            a.setResizable(true);
            a.getDialogPane().setMinWidth(400);
            a.getDialogPane().setMinHeight(javafx.scene.layout.Region.USE_PREF_SIZE);
            a.show();
        });
    }

    public static void showError(String title, String message) {
        Platform.runLater(() -> {
            Alert a = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
            a.setHeaderText(title);
            a.setResizable(true);
            a.getDialogPane().setMinWidth(400);
            a.getDialogPane().setMinHeight(javafx.scene.layout.Region.USE_PREF_SIZE);
            a.show();
        });
    }

    public static void showWarning(String title, String message) {
        Platform.runLater(() -> {
            Alert a = new Alert(Alert.AlertType.WARNING, message, ButtonType.OK);
            a.setHeaderText(title);
            a.setResizable(true);
            a.getDialogPane().setMinWidth(400);
            a.getDialogPane().setMinHeight(javafx.scene.layout.Region.USE_PREF_SIZE);
            a.show();
        });
    }
}
