package com.viabus.viewHandlers;

import com.viabus.controllers.ManageBussesController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ManageBussesViewHandler {
    private ManageBussesController controller;

    public ManageBussesViewHandler(ManageBussesController controller) {
        this.controller = controller;
    }

    public void showManageBussesWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/viabus/views/manage_busses_view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
