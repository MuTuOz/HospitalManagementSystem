module com.hospitalmanagement {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive java.sql;
    requires java.prefs;

    opens com.hospitalmanagement to javafx.fxml;
    exports com.hospitalmanagement;
}
