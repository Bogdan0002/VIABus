package com.viabus.viewHandlers;

import com.viabus.controllers.CreateReservationController;
import com.viabus.models.Reservation;
import com.viabus.service.ReservationService;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateReservationViewHandler {

    public void showCreateReservationWindow(ObservableList<Reservation> reservationData, ReservationService reservationService) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/viabus/views/create_reservation_view.fxml"));
            Parent root = loader.load();

            CreateReservationController controller = loader.getController();
         //   controller.setReservationData(reservationData);


            // Print reservationService to check if it is null
            System.out.println("ReservationService passed to CreateReservationViewHandler: " + reservationService);

            controller.setReservationService(reservationService);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

