package com.viabus.controllers;

import com.viabus.models.BusType;
import com.viabus.models.Chauffeur;
import com.viabus.service.ChauffeurService;
import com.viabus.viewHandlers.AddChauffeurViewHandler;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

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
                chauffeurService.deleteChauffeur(selectedChauffeur, chauffeurData);
            }
        }
    }

    @FXML
    private void handleEditChauffeurButton(){
        // Get the selected chauffeur from the table view
        Chauffeur selectedChauffeur = chauffeurTableView.getSelectionModel().getSelectedItem();

        if (selectedChauffeur != null) {
            // Create the custom dialog.
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Edit Chauffeur");

            // Set the button types.
            ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

            // Create the fields and labels and add them to a grid pane.
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField firstName = new TextField();
            firstName.setText(selectedChauffeur.getFirstName());
            TextField lastName = new TextField();
            lastName.setText(selectedChauffeur.getLastName());
            ComboBox<BusType> preference = new ComboBox<>();
            preference.getItems().setAll(BusType.values());
            preference.setValue(selectedChauffeur.getChauffeurPreference());
            CheckBox available = new CheckBox("Available");
            available.setSelected(selectedChauffeur.getAvailability());

            grid.add(new Label("First Name:"), 0, 0);
            grid.add(firstName, 1, 0);
            grid.add(new Label("Last Name:"), 0, 1);
            grid.add(lastName, 1, 1);
            grid.add(new Label("Chauffeur Preference:"), 0, 2);
            grid.add(preference, 1, 2);
            grid.add(available, 1, 3);

            dialog.getDialogPane().setContent(grid);

            // Enable/Disable save button depending on whether fields are empty.
            Node saveButton = dialog.getDialogPane().lookupButton(saveButtonType);
            saveButton.setDisable(true);

            // Logic to enable save button only when fields are not empty
            firstName.textProperty().addListener((observable, oldValue, newValue) -> {
                saveButton.setDisable(newValue.trim().isEmpty());
            });

            lastName.textProperty().addListener((observable, oldValue, newValue) -> {
                ((Node) saveButton).setDisable(newValue.trim().isEmpty());
            });

            // Handle save button action.
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == saveButtonType) {
                    return new Pair<>(firstName.getText(), lastName.getText());
                }
                return null;
            });

            Optional<Pair<String, String>> result = dialog.showAndWait();

            result.ifPresent(data -> {
                // Get the new data and update chauffeur.
                String newFirstName = data.getKey();
                String newLastName = data.getValue();
                BusType newPreference = preference.getValue();
                boolean newAvailability = available.isSelected();

                selectedChauffeur.setFirstName(newFirstName);
                selectedChauffeur.setLastName(newLastName);
                selectedChauffeur.setChauffeurPreference(newPreference);
                selectedChauffeur.setAvailability(newAvailability);

                chauffeurService.updateChauffeurData(chauffeurData);

                chauffeurService.saveChauffeurData();
                chauffeurTableView.refresh();
            });
        }
    }

}









