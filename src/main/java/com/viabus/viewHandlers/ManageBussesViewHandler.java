package com.viabus.viewHandlers;

import com.viabus.controllers.ManageBussesController;
import com.viabus.service.BusService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ManageBussesViewHandler {
    private final BusService busService;

    public ManageBussesViewHandler(BusService busService) {
        this.busService = new BusService("files/Busses.txt");
    }

    public void showManageBussesWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/viabus/views/manage_busses_view.fxml"));
            Parent root = loader.load();

            ManageBussesController controller = loader.getController();
            controller.setBusService(busService);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
