module com.hospitalmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;

    opens com.hospitalmanagement to javafx.fxml;

    exports com.hospitalmanagement;
}
