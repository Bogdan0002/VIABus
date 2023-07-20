package com.viabus.controllers;

import com.viabus.models.BusType;
import com.viabus.models.Chauffeur;
import com.viabus.models.Trip;
import com.viabus.service.TripService;
import com.viabus.viewHandlers.AddTripViewHandler;
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

public class ManageTripsController {
    @FXML
    private TableView<Trip> tripTableView;
    @FXML
    private TableColumn<Trip, String> tripPickupLocationColumn;
    @FXML
    private TableColumn<Trip, Integer> tripIdColumn;
    @FXML
    private TableColumn<Trip, String> tripDropoffLocationColumn;
    @FXML
    private TableColumn<Trip, String> tripDurationColumn;
    private ObservableList<Trip> tripData;

    private TripService tripService = new TripService("files/Trips.txt");

    public void setTripService(TripService tripService) {
        this.tripService = tripService;
    }

    @FXML
    private void handleAddTripButton(){
        if (tripService == null) {
            System.out.println("TripService is null");
            return;
        }

        AddTripViewHandler viewHandler = new AddTripViewHandler();
        viewHandler.showAddTripWindow(tripData, tripService);
    }

    @FXML
    private void initialize(){
        tripData = FXCollections.observableArrayList();
        tripTableView.setItems(tripData);
        tripPickupLocationColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getDeparture()).asString());
        tripDropoffLocationColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getDestination()).asString());
        tripDurationColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getDuration()).asString());
        tripIdColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getId()));

        tripService.loadTripData();
        tripData.addAll(tripService.getTripData());

    }

    @FXML
    private void handleDeleteTripButton(){
        Trip selectedTrip = tripTableView.getSelectionModel().getSelectedItem();
        if (selectedTrip != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirm Deletion");
            confirmationAlert.setHeaderText("Delete Trip");
            confirmationAlert.setContentText("Are you sure you want to delete this trip?");
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                tripData.remove(selectedTrip);
                tripService.deleteTrip(selectedTrip, tripData);
            }
        }
    }

    @FXML
    private void handleEditTripButton(){
        // Get the selected trip from the table view
        Trip selectedTrip = tripTableView.getSelectionModel().getSelectedItem();

        if (selectedTrip != null) {
            // Create the custom dialog.
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Edit Trip");

            // Set the button types.
            ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

            // Create the fields and labels and add them to a grid pane.
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField departure = new TextField();
            departure.setText(selectedTrip.getDeparture());
            TextField destination = new TextField();
            destination.setText(selectedTrip.getDestination());
            TextField duration = new TextField();
            duration.setText(selectedTrip.getDuration());

            grid.add(new Label("Departure:"), 0, 0);
            grid.add(departure, 1, 0);
            grid.add(new Label("Destination:"), 0, 1);
            grid.add(destination, 1, 1);
            grid.add(new Label("Duration:"), 0, 2);
            grid.add(duration, 1, 2);

            dialog.getDialogPane().setContent(grid);

            // Enable/Disable save button depending on whether fields are empty.
            Node saveButton = dialog.getDialogPane().lookupButton(saveButtonType);
            saveButton.setDisable(true);

            // Logic to enable save button only when fields are not empty
            departure.textProperty().addListener((observable, oldValue, newValue) -> {
                saveButton.setDisable(newValue.trim().isEmpty());
            });

            destination.textProperty().addListener((observable, oldValue, newValue) -> {
                ((Node) saveButton).setDisable(newValue.trim().isEmpty());
            });

            // Handle save button action.
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == saveButtonType) {
                    return new Pair<>(departure.getText(), destination.getText());
                }
                return null;
            });

            Optional<Pair<String, String>> result = dialog.showAndWait();

            result.ifPresent(data -> {
                // Get the new data and update trip.
                String newDeparture = data.getKey();
                String newDestination = data.getValue();

                selectedTrip.setDeparture(newDeparture);
                selectedTrip.setDestination(newDestination);

                tripService.updateTripData(tripData);

                tripService.saveTripData();
                tripTableView.refresh();
            });
        }
    }



}
