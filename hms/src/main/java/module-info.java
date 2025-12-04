module com.hospitalmanagement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.hospitalmanagement to javafx.fxml;
    exports com.hospitalmanagement;
}
