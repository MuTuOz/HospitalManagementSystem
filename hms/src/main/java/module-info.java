module com.hospitalmanagement {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.hospitalmanagement to javafx.fxml;
    exports com.hospitalmanagement;
}
