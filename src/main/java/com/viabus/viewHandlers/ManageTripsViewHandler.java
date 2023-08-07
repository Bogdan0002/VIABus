package com.viabus.viewHandlers;

import com.viabus.controllers.ManageChauffeursController;
import com.viabus.controllers.ManageTripsController;
import com.viabus.service.TripService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ManageTripsViewHandler {

    private final TripService tripService;

    public ManageTripsViewHandler(TripService tripService) {
        this.tripService =  tripService;
    }

    public void showManageTripsWindow(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/viabus/views/manage_trips_view.fxml"));
            Parent root = loader.load();

            ManageTripsController controller = loader.getController();
            controller.setTripService(tripService);

            Stage stage = new Stage();
            stage.setTitle("Manage Trips");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
