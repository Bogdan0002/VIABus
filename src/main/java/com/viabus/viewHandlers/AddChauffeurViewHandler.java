package com.viabus.viewHandlers;

import com.viabus.controllers.AddBusController;
import com.viabus.controllers.AddChauffeurController;
import com.viabus.models.Chauffeur;
import com.viabus.service.ChauffeurService;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class AddChauffeurViewHandler {

    public void showAddChauffeurWindow(ObservableList<Chauffeur> chauffeurData, ChauffeurService chauffeurService) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/viabus/views/add_chauffeur_view.fxml"));
            Parent root = loader.load();

            AddChauffeurController addChauffeurController = loader.getController();
            addChauffeurController.setChauffeurData(chauffeurData);
            addChauffeurController.setChauffeurService(chauffeurService);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
