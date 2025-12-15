package com.hospitalmanagement;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class AccessControl {
    public static boolean requireRole(String role) {
        User u = Session.getCurrentUser();
        if (u == null) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Oturum bulunamadı.", ButtonType.OK);
            a.setHeaderText("Erişim reddedildi");
            a.showAndWait();
            return false;
        }
        if (u.getRoleName() == null || !u.getRoleName().equalsIgnoreCase(role)) {
            Alert a = new Alert(Alert.AlertType.ERROR, "Bu işlemi gerçekleştirmek için yetkiniz yok.", ButtonType.OK);
            a.setHeaderText("Erişim reddedildi");
            a.showAndWait();
            return false;
        }
        return true;
    }
}
