package com.viabus.viewHandlers;

import com.viabus.controllers.MainWindowController;
import com.viabus.service.ReservationService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWindowViewHandler {
    private final ReservationService reservationService;
    private MainWindowController mainWindowController;

    public MainWindowViewHandler() {
        this.mainWindowController = new MainWindowController();
        this.reservationService = new ReservationService("files/Reservations.txt");
        mainWindowController.setReservationService(reservationService);
    }



    public void showMainWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/viabus/views/main_window_view.fxml"));

            Parent root = loader.load(); // view must be loaded before getting the controller

            MainWindowController controller = loader.getController(); // get the controller after the view is loaded
            controller.setReservationService(reservationService);

            Stage stage = new Stage();
            stage.setTitle("VIA Bus Management System");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
