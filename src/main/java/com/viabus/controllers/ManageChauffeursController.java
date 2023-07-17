package com.viabus.controllers;

import com.viabus.models.BusType;
import com.viabus.models.Chauffeur;
import com.viabus.service.ChauffeurService;
import com.viabus.viewHandlers.AddChauffeurViewHandler;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;

public class ManageChauffeursController {
    @FXML
    private TableView<Chauffeur> chauffeurTableView;
    @FXML
    private TableColumn<Chauffeur, String> chauffeurFirstNameColumn;
    @FXML
    private TableColumn<Chauffeur, String> chauffeurLastNameColumn;
    @FXML
    private TableColumn<Chauffeur, BusType> chauffeurPreferenceColumn;
    @FXML
    private TableColumn<Chauffeur, Integer> chauffeurIdColumn;
    private ObservableList<Chauffeur> chauffeurData;
    @FXML
    private TableColumn<Chauffeur, Boolean> chauffeurAvailabilityColumn;

    private ChauffeurService chauffeurService = new ChauffeurService("files/Chauffeurs.txt");

    public void setChauffeurService(ChauffeurService chauffeurService) {
        this.chauffeurService = chauffeurService;
    }

    @FXML
    private void handleAddChauffeurButton(){
        if (chauffeurService == null) {
            System.out.println("ChauffeurService is null");
            // throw new IllegalArgumentException("busService cannot be null");
            return;
        }

        AddChauffeurViewHandler viewHandler = new AddChauffeurViewHandler();
        viewHandler.showAddChauffeurWindow(chauffeurData, chauffeurService);
    }

    @FXML
    private void initialize(){
        chauffeurData = FXCollections.observableArrayList();
        chauffeurTableView.setItems(chauffeurData);
        chauffeurFirstNameColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getFirstName()).asString());
        chauffeurLastNameColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getLastName()).asString());
        chauffeurIdColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getId()));
        chauffeurPreferenceColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getChauffeurPreference()));
        chauffeurAvailabilityColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getAvailability()));

        chauffeurService.loadChauffeurData();
        chauffeurData.addAll(chauffeurService.getChauffeurData());

    }

    @FXML
    private void handleDeleteChauffeurButton(){
        Chauffeur selectedChauffeur = chauffeurTableView.getSelectionModel().getSelectedItem();

        if (selectedChauffeur != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirm Deletion");
            confirmationAlert.setHeaderText("Delete Chauffeur");
            confirmationAlert.setContentText("Are you sure you want to delete this chauffeur?");
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                chauffeurData.remove(selectedChauffeur);
                chauffeurService.deleteChauffeur(selectedChauffeur);
            }
        }
    }

    @FXML
    private void handleEditChauffeurButton(){
        String newLine =System.getProperty("line.separator");
        // Get the selected chauffeur from the table view
        Chauffeur selectedChauffeur = chauffeurTableView.getSelectionModel().getSelectedItem();

        if (selectedChauffeur != null) {
            // Open a dialog to edit the chauffeur
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Edit Chauffeur");
            dialog.setHeaderText("Edit the Chauffeur details");

            // Get all bus types (they become the chauffeur preferences)
            BusType[] busTypes = BusType.values();
            StringBuilder busTypesStr = new StringBuilder();
            for (BusType busType : busTypes) {
                busTypesStr.append(busType.name()).append(", ");
            }
            // Remove the last comma and space
            if (busTypesStr.length() > 0) {
                busTypesStr.setLength(busTypesStr.length() - 2);
            }

            dialog.setContentText("Enter new values as: First Name, Last Name, Chauffeur Preference" + newLine
                    + "Example: John, Doe, HANDICAP" + newLine
                    + "Bus Types (Chauffeur Preferences): " + busTypesStr);

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(chauffeurDetails -> {
                // Parse the new values and update the selected chauffeur
                String[] chauffeurDetailsParts = chauffeurDetails.split(",");
                String newFirstName = chauffeurDetailsParts[0].trim();
                String newLastName = chauffeurDetailsParts[1].trim();
                BusType chauffeurPreference = BusType.valueOf(chauffeurDetailsParts[2].trim().toUpperCase());

                // Search for the chauffeur in chauffeurData list in ChauffeurService and update it
                for (Chauffeur chauffeur : chauffeurService.getChauffeurData()) {
                    if (chauffeur.getId() == selectedChauffeur.getId()) {
                        chauffeur.setFirstName(newFirstName);
                        chauffeur.setLastName(newLastName);
                        chauffeur.setChauffeurPreference(chauffeurPreference);
                        break;
                    }
                }

                // Save the updated chauffeur data to the .txt file
                chauffeurService.saveChauffeurData();
                chauffeurTableView.refresh();
            });
        }
    }





}
