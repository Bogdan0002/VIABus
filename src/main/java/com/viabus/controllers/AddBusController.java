package com.viabus.controllers;

import com.viabus.models.Bus;
import com.viabus.models.BusType;
import com.viabus.service.BusService;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.io.*;

public class AddBusController {

    @FXML
    private ComboBox<BusType> busTypeComboBox;
    @FXML
    private TextField capacityTextField;
    @FXML
    private TextField numberPlateTextField;
    @FXML
    private ComboBox availabilityComboBox;
    @FXML
    private Label errorLabel;
    @FXML
    private Label infoSaved;
    private ObservableList<Bus> busData;
    private Bus bus;
    private BusService busService;

    private String fileManager() {
        String folderPath = System.getProperty("user.dir") + File.separator + "files";
        String fileName = "Busses.txt";
        return folderPath + File.separator + fileName;
    }



    public AddBusController() {
    }

    @FXML
    public void initialize() {
        System.out.println("Initializing AddBusController...");
        this.busService = new BusService("files/Busses.txt");
        busTypeComboBox.setItems(FXCollections.observableArrayList(BusType.values()));
        availabilityComboBox.getItems().addAll(true, false);
        availabilityComboBox.getSelectionModel().selectFirst();
    }


    public void setBusData(ObservableList<Bus> busData) {
        this.busData = busData;
    }

    @FXML
    private void handleAddBusButton() {
        try {
            // Get the input values from the text fields
            BusType busType = busTypeComboBox.getValue();
            String capacityText = capacityTextField.getText().trim();
            String numberPlate = numberPlateTextField.getText().trim();
            boolean availability = (boolean) availabilityComboBox.getValue();

            // Validate the input
            if (busType == null || capacityText.isEmpty()) {
                showError("Invalid capacity");
                return;
            } else if (numberPlate == null) {
                showError("Invalid number plate");
                return;
            }

            int capacity;
            try {
                capacity = Integer.parseInt(capacityText);
                if (capacity <= 0) {
                    showError("Invalid type");
                    return;
                }
            } catch (NumberFormatException e) {
                showError("Invalid Input");
                return;
            }

            Bus bus = new Bus(numberPlate, capacity, busType, availability);
            busService.addBus(bus);
            busData.add(bus);
            clearInputFields();
            infoSaved("Bus added successfully");
            writeBusToFile(bus); // Pass the 'bus' to 'writeBusToFile' method
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void clearInputFields() {
        capacityTextField.clear();
        numberPlateTextField.clear();
        busTypeComboBox.setValue(null);
        availabilityComboBox.setValue(null);
    }

    private void writeBusToFile(Bus bus) {
        String filePath = fileManager();
        System.out.println(filePath);
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath, true)))) {
            String lineToAdd = bus.getNumberPlate() + "," + bus.getBusType() + "," + bus.getSeatCapacity() + "," + bus.isAvailable();
            writer.println(lineToAdd);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




    private void showError(String errorMessage) {
        errorLabel.setText(errorMessage);
        errorLabel.setVisible(true);
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished( event -> {
            errorLabel.setVisible(false);
        });
        delay.play();
    }

    private void infoSaved(String infoMessage) {
        infoSaved.setText(infoMessage);
        infoSaved.setVisible(true);
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished( event -> {
            infoSaved.setVisible(false);
        });
        delay.play();
    }

    public void setBusService(BusService busService) {
        System.out.println("Setting busService..." + busService);
        this.busService = busService;
    }
}
