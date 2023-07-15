package com.viabus.viewHandlers;

import com.viabus.controllers.AddBusController;
import com.viabus.models.Bus;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AddBusViewHandler {
    private AddBusController addBusController;

    public void showAddBussesWindow(ObservableList<Bus> busData) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/viabus/views/add_bus_view.fxml"));
            Parent root = loader.load();

            addBusController = loader.getController();
            addBusController.setBusData(busData);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
