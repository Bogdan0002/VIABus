module com.via.viabus {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.viabus to javafx.fxml;
    exports com.viabus;
    exports com.viabus.controllers;
    opens com.viabus.controllers to javafx.fxml;
}