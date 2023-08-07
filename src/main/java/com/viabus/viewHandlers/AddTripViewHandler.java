package com.viabus.viewHandlers;

import com.viabus.controllers.AddTripController;
import com.viabus.models.Trip;
import com.viabus.service.TripService;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AddTripViewHandler {

    public void showAddTripWindow(ObservableList<Trip> tripData, TripService tripService){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/viabus/views/add_trip_view.fxml"));
            Parent root = loader.load();

            AddTripController addTripController = loader.getController();
            addTripController.setTripData(tripData);
            addTripController.setTripService(tripService);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
