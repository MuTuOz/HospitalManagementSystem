module com.hospitalmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    requires java.prefs;

    opens com.hospitalmanagement to javafx.fxml;

    exports com.hospitalmanagement;
}
