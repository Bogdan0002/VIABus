package com.viabus.controllers;

import com.viabus.models.Bus;
import com.viabus.models.BusType;
import com.viabus.service.BusService;
import com.viabus.viewHandlers.AddBusViewHandler;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
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
    private ObservableList<Bus> busData;
    private BusService busService;


    public ManageBussesController() {
        busService = new BusService("files/Busses.txt");
    }

    @FXML
    private void handleAddButton() {
        AddBusViewHandler viewHandler = new AddBusViewHandler(busService);
        viewHandler.showAddBussesWindow(busData);
    }


    @FXML
    private void initialize() {
        busData = FXCollections.observableArrayList();
        busTableView.setItems(busData);
        busNumberPlateColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getNumberPlate()).asString());
        busTypeColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getBusType().toString()));
        capacityColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getSeatCapacity()));

        busService.loadBusData();
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
                busService.deleteBus(selectedBus);
                busService.saveBusData();
            }
        }
    }

    private void saveBusData() {
        //
        String filePath = "files/Busses.txt";
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath, false)))) {
            for (Bus bus : busData) {
                String lineToAdd = bus.getNumberPlate() + "," + bus.getBusType() + "," + bus.getSeatCapacity();
                writer.println(lineToAdd);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleEditButton() {

        String newLine =System.getProperty("line.separator");
        // Get the selected bus from the table view
        Bus selectedBus = busTableView.getSelectionModel().getSelectedItem();

        if (selectedBus != null) {
            // Open a dialog to edit the bus
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Edit Bus");
            dialog.setHeaderText("Edit the Bus details");

            // Get all bus types
            BusType[] busTypes = BusType.values();
            StringBuilder busTypesStr = new StringBuilder();
            for (BusType busType : busTypes) {
                busTypesStr.append(busType.name()).append(", ");
            }
            // Remove the last comma and space
            if (busTypesStr.length() > 0) {
                busTypesStr.setLength(busTypesStr.length() - 2);
            }

            dialog.setContentText("Enter new values as: Number Plate, But Type, SeatCapacity" + newLine
                    + "Example: AF 85 671,HANDICAP,20" + newLine
                    + "Bus Types: " + busTypesStr);

            Optional<String> result = dialog.showAndWait();
            result.ifPresent(busDetails -> {
                // Parse the new values and update the selected bus
                String[] busDetailsParts = busDetails.split(",");
                selectedBus.setNumberPlate(busDetailsParts[0].trim());
                selectedBus.setBusType(BusType.valueOf(busDetailsParts[1].trim().toUpperCase()));
                selectedBus.setSeatCapacity(Integer.parseInt(busDetailsParts[2].trim()));

                // Save the updated bus data to the .txt file
                saveBusData();
                busTableView.refresh();
            });
        }
    }




    private void loadBusData() {
        try {
            Path path = Paths.get("files/Busses.txt");
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (String line : lines) {
                String[] parts = line.split(",");
                String numberPlate = parts[0];
                BusType busType = BusType.valueOf(parts[1]);
                int capacity = Integer.parseInt(parts[2]);

                Bus bus = new Bus(numberPlate, capacity, busType);
                busData.add(bus);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
