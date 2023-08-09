package com.viabus.controllers;

import com.viabus.models.Bus;
import com.viabus.models.BusType;
import com.viabus.service.BusService;
import com.viabus.viewHandlers.AddBusViewHandler;
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


public class ManageBussesController {

    @FXML
    private TableView<Bus> busTableView;
    @FXML
    private TableColumn<Bus, String> busNumberPlateColumn;
    @FXML
    private TableColumn<Bus, String> busTypeColumn;
    @FXML
    private TableColumn<Bus, Integer> capacityColumn;
    @FXML
    private TableColumn<Bus, Boolean> busAvailabilityColumn;
    private ObservableList<Bus> busData;
    private BusService busService = new BusService("files/Busses.txt");

    public void setBusService(BusService busService) {
        this.busService = busService;
    }


    @FXML
    private void handleAddButton() {
        if (busService == null) {
            System.out.println("BusService is null");
            // throw new IllegalArgumentException("busService cannot be null");
            return;
        }
        AddBusViewHandler viewHandler = new AddBusViewHandler();
        viewHandler.showAddBussesWindow(busData, busService);
    }



    @FXML
    private void initialize() {
        busData = FXCollections.observableArrayList();
        busTableView.setItems(busData);
        busNumberPlateColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getNumberPlate()).asString());
        busTypeColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getBusType().toString()));
        capacityColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getSeatCapacity()));
        busAvailabilityColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().isAvailable()));

        busData.addAll(busService.getBusData());
    }


    @FXML
    private void handleDeleteButton() {
        Bus selectedBus = busTableView.getSelectionModel().getSelectedItem();

        if (selectedBus != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirm Deletion");
            confirmationAlert.setHeaderText("Delete Bus");
            confirmationAlert.setContentText("Are you sure you want to delete this bus?");
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                busData.remove(selectedBus);
                busService.deleteBus(selectedBus, busData);
            }
        }
    }




    /**
     * Called when the user clicks the edit button. Opens a dialog to edit
     */
    @FXML
    private void handleEditButton(){
        // Get the selected chauffeur from the table view
        Bus selectedBus = busTableView.getSelectionModel().getSelectedItem();

        if (selectedBus != null) {
            // Create the custom dialog.
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Edit Bus");

            // Set the button types.
            ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

            // Create the fields and labels and add them to a grid pane.
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField numberPlates = new TextField();
            numberPlates.setText(selectedBus.getNumberPlate());
            TextField seatCapacity = new TextField();
            seatCapacity.setText(String.valueOf(selectedBus.getSeatCapacity()));
            ComboBox<BusType> busType = new ComboBox<>();
            busType.getItems().setAll(BusType.values());
            busType.setValue(selectedBus.getBusType());
            CheckBox available = new CheckBox("Available");
            available.setSelected(selectedBus.isAvailable());

            grid.add(new Label("Number Plates:"), 0, 0);
            grid.add(numberPlates, 1, 0);
            grid.add(new Label("Seat Capacity:"), 0, 1);
            grid.add(seatCapacity, 1, 1);
            grid.add(new Label("Bus Type:"), 0, 2);
            grid.add(busType, 1, 2);
            grid.add(available, 1, 3);

            dialog.getDialogPane().setContent(grid);

            // Enable/Disable save button depending on whether fields are empty.
            Node saveButton = dialog.getDialogPane().lookupButton(saveButtonType);
            saveButton.setDisable(true);

            // Logic to enable save button only when fields are not empty
            numberPlates.textProperty().addListener((observable, oldValue, newValue) -> {
                saveButton.setDisable(newValue.trim().isEmpty());
            });

            seatCapacity.textProperty().addListener((observable, oldValue, newValue) -> {
                ((Node) saveButton).setDisable(newValue.trim().isEmpty());
            });

            // Handle save button action.
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == saveButtonType) {
                    return new Pair<>(numberPlates.getText(), seatCapacity.getText());
                }
                return null;
            });

            Optional<Pair<String, String>> result = dialog.showAndWait();

            result.ifPresent(data -> {
                // Get the new data and update chauffeur.
                String newNumberPlates = data.getKey();
                int newSeatCapacity = Integer.parseInt(data.getValue());
                BusType newBusType = busType.getValue();
                boolean newAvailability = available.isSelected();

                selectedBus.setNumberPlate(newNumberPlates);
                selectedBus.setSeatCapacity(newSeatCapacity);
                selectedBus.setBusType(newBusType);
                selectedBus.setAvailability(newAvailability);

                busService.updateBusData(busData);

                busService.saveBusData();
                busTableView.refresh();
            });
        }
    }



}
