package com.viabus.viewHandlers;

import com.viabus.controllers.ManageChauffeursController;
import com.viabus.service.ChauffeurService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ManageChauffeursViewHandler {

    private final ChauffeurService chauffeurService;
    public ManageChauffeursViewHandler(ChauffeurService chauffeurService) {
        this.chauffeurService = new ChauffeurService("files/Chauffeurs.txt");
    }

    public void showManageChauffeursWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/viabus/views/manage_chauffeurs_view.fxml"));
            Parent root = loader.load();

            ManageChauffeursController controller = loader.getController();
            controller.setChauffeurService(chauffeurService);

            Stage stage = new Stage();
            stage.setTitle("Manage Chauffeurs");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
