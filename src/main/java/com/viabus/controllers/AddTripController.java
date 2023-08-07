package com.viabus.controllers;

import com.viabus.models.Trip;
import com.viabus.service.TripService;
import javafx.animation.PauseTransition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

public class AddTripController {
    @FXML
    private TextField puTextField;
    @FXML
    private TextField doTextField;
    @FXML
    private TextField durationTextField;
    @FXML
    private Label errorLabel;
    @FXML
    private Label infoSaved;
    private ObservableList<Trip> tripData;
    private TripService tripService;

    private String fileManager(){
        String folderPath = System.getProperty("user.dir") + java.io.File.separator + "files";
        String fileName = "Trips.txt";
        return folderPath + java.io.File.separator + fileName;
    }

    public AddTripController(){
    }

    @FXML
    public void initialize(){
        System.out.println("Initializing AddTripController...");
        this.tripService = new TripService("files/Trips.txt");
    }

    public void setTripData(ObservableList<Trip> tripData) {
        this.tripData = tripData;
    }

    @FXML
    private void handleAddTripButton(){
        try{
            String pu = puTextField.getText().trim();
            String doo = doTextField.getText().trim();
            String duration = durationTextField.getText().trim();

            if(pu.isEmpty() || doo.isEmpty() || duration.isEmpty()){
               showError("Please fill in all fields.");
                return;
            }

            Trip trip = new Trip(pu, doo, duration);
            tripService.addTrip(trip);
            tripData.add(trip);
            clearInputFields();
            infoSaved("Trip added successfully.");
            writeTripToFile(trip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTripService(TripService tripService){
        this.tripService = tripService;
    }

    private void clearInputFields(){
        puTextField.clear();
        doTextField.clear();
        durationTextField.clear();
    }

    private void writeTripToFile(Trip trip){
        String filePath = fileManager();
        System.out.println(filePath);
        try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath, true)))){
            String lineToAdd = trip.getId() + "," + trip.getDeparture() + "," + trip.getDestination() + "," + trip.getDuration();
            writer.println(lineToAdd);
        } catch (Exception e) {
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
