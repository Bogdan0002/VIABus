package com.viabus.controllers;

import com.viabus.models.Bus;
import com.viabus.models.BusType;
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
    private Label errorLabel;
    @FXML
    private Label infoSaved;
    private ObservableList<Bus> busData;
    private String bussesFilePath;
    private Bus bus;

    private String fileManager() {
        String folderPath = System.getProperty("user.dir") + File.separator + "files";
        String fileName = "Busses.txt";
        return folderPath + File.separator + fileName;
    }



    public AddBusController() {
    }

    @FXML
    public void initialize() {
        try {
            String filePath = fileManager();

            File file = new File(filePath);
            if (!file.exists()) {
                file.getParentFile().mkdirs(); // Create the parent folder if it doesn't exist
                file.createNewFile(); // Create the file if it doesn't exist
            } else {
                // File exists, read the existing data and populate the busData list
                busData = FXCollections.observableArrayList();
                try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        String[] parts = line.split(",");
                        if (parts.length == 3) {
                            String numberPlate = String.valueOf(parts[0]);
                            BusType busType = BusType.valueOf(parts[1]);
                            int seatCapacity = Integer.parseInt(parts[2]);
                            Bus bus = new Bus(numberPlate, seatCapacity, busType);
                            busData.add(bus);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Populate the ComboBox with bus types
            busTypeComboBox.setItems(FXCollections.observableArrayList(BusType.values()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setBusData(ObservableList<Bus> busData) {
        this.busData = busData;
    }

    @FXML
    private void handleAddBusButton() {
        // Get the input values from the text fields
        BusType busType = busTypeComboBox.getValue();
        String capacityText = capacityTextField.getText().trim();
        String numberPlate = numberPlateTextField.getText().trim();

        // Validate the input
        if (busType == null || capacityText.isEmpty()) {
            showError("Invalid capacity");
            return;
        }

        else if (numberPlate == null || capacityText.isEmpty()) {
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

        // Assign the newly created bus to the bus member variable
        bus = new Bus(numberPlate, capacity, busType);
        writeBusToFile();

        // Add the new bus object to the busData list
        busData.add(bus);

        // Clear the input fields
        clearInputFields();

        // Save the updated bus data to the .txt file
        infoSaved("Bus added successfully");
    }





    private void clearInputFields() {
        capacityTextField.clear();
        numberPlateTextField.clear();
        busTypeComboBox.setValue(null);
    }

    private void writeBusToFile() {
        String filePath = fileManager();
        System.out.println(filePath);
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath, true)))) {
                String lineToAdd = bus.getNumberPlate() + "," + bus.getBusType() + "," + bus.getSeatCapacity();
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
}
