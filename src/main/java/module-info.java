module com.via.viabus {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.via.viabus to javafx.fxml;
    exports com.via.viabus;
}