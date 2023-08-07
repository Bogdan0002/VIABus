package com.viabus.controllers;

import com.viabus.models.*;
import com.viabus.service.*;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.util.Duration;
import java.io.*;
import java.time.LocalDate;
import java.util.List;


public class CreateReservationController {
    @FXML
    private DatePicker startTimePicker;
    @FXML
    private DatePicker endTimePicker;
    @FXML
    private ComboBox<Trip> tripIdComboBox;
    @FXML
    private ComboBox<Bus> busNpComboBox;
    @FXML
    private ComboBox<Chauffeur> chauffeurIdComboBox;
    @FXML
    private ComboBox<Customer> customerIdComboBox;
    @FXML
    private Label errorLabel;
    @FXML
    private Label infoSaved;
    private ObservableList<Reservation> reservationData;
    private ReservationService reservationService;
    private TripService tripService;
    private CustomerService customerService;
    private ChauffeurService chauffeurService;
    private BusService busService;


    private String fileManager(){
        String folderPath = System.getProperty("user.dir") + File.separator + "files";
        String fileName = "Reservations.txt";
        return folderPath + File.separator + fileName;
    }

    public CreateReservationController() {
    }

    @FXML
    public void initialize() {
        System.out.println("Initializing CreateReservationController...");
        tripService = new TripService("files/Trips.txt");
        customerService = new CustomerService("files/Customers.txt");
        chauffeurService = new ChauffeurService("files/Chauffeurs.txt");
        busService = new BusService("files/Busses.txt");

        // reservationData and ComboBox population will be handled in setReservationService
    }


    private void populateComboBoxes() {
        List<Trip> tripList = tripService.getAll();
        ObservableList<Trip> tripObservableList = FXCollections.observableArrayList(tripList);
        tripIdComboBox.setItems(tripObservableList);

        List<Customer> customerList = customerService.getAll();
        ObservableList<Customer> customerObservableList = FXCollections.observableArrayList(customerList);
        customerIdComboBox.setItems(customerObservableList);

        List<Chauffeur> chauffeurList = chauffeurService.getAll();
        ObservableList<Chauffeur> chauffeurObservableList = FXCollections.observableArrayList(chauffeurList);
        chauffeurIdComboBox.setItems(chauffeurObservableList);

        List<Bus> busList = busService.getAll();
        ObservableList<Bus> busObservableList = FXCollections.observableArrayList(busList);
        busNpComboBox.setItems(busObservableList);
    }

    public void setReservationData(ObservableList<Reservation> reservationData) {
        this.reservationData = reservationData;
    }

    @FXML
    private void handleSaveReservationButton() {
        try {
            // Get the input values from the UI elements
            LocalDate startDate = startTimePicker.getValue();
            LocalDate endDate = endTimePicker.getValue();
            Trip trip = tripIdComboBox.getValue();
            Chauffeur chauffeur = chauffeurIdComboBox.getValue();
            Bus bus = busNpComboBox.getValue();
            Customer customer = customerIdComboBox.getValue();

            // Validate the input
            if (startDate == null || endDate == null) {
                showError("Invalid date");
                return;
            } else if (trip == null) {
                showError("Invalid trip");
                return;
            } else if (chauffeur == null) {
                showError("Invalid chauffeur");
                return;
            } else if (bus == null) {
                showError("Invalid bus");
                return;
            } else if (customer == null) {
                showError("Invalid customer");
                return;
            }

            // Create a new Reservation
            Reservation reservation = new Reservation(startDate, endDate, trip, chauffeur, bus, customer);

            if (reservationData == null) {
                System.out.println("reservationData is null");
                return;
            }

            // Add the reservation using the ReservationService
            reservationService.addReservation(reservation);

            // Add the reservation to the list of reservations in the UI
            reservationData.add(reservation);

            // Show a confirmation message
            infoSaved("Reservation added successfully");
            clearInputFields();

            // Write the reservation to the file
            writeReservationToFile(reservation);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void clearInputFields() {
        startTimePicker.setValue(null);
        endTimePicker.setValue(null);
        tripIdComboBox.setValue(null);
        chauffeurIdComboBox.setValue(null);
        busNpComboBox.setValue(null);
        customerIdComboBox.setValue(null);
    }

    private void writeReservationToFile(Reservation reservation){
        String filePath = fileManager();
        try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath, true)))) {
           String lineToAdd = reservation.getId() + "," + reservation.getStartDate() + "," + reservation.getEndDate() + "," + reservation.getTrip().getId() + "," + reservation.getChauffeur().getId() + "," + reservation.getBus().getNumberPlate() + "," + reservation.getCustomer().getId();
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

    private void updateAvailableBusesAndChauffeurs() {
        LocalDate startDate = startTimePicker.getValue();
        LocalDate endDate = endTimePicker.getValue();

        List<Bus> availableBuses = reservationService.getAvailableBuses(startDate, endDate);
        List<Chauffeur> availableChauffeurs = reservationService.getAvailableChauffeurs(startDate, endDate);

        ObservableList<Bus> busObservableList = FXCollections.observableArrayList(availableBuses);
        busNpComboBox.setItems(busObservableList);

        ObservableList<Chauffeur> chauffeurObservableList = FXCollections.observableArrayList(availableChauffeurs);
        chauffeurIdComboBox.setItems(chauffeurObservableList);
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



    public void setReservationService(ReservationService reservationService) {
        System.out.println("Setting reservationService..." + reservationService);
        this.reservationService = reservationService;

        reservationData = FXCollections.observableArrayList();
        reservationData.addAll(this.reservationService.getReservationData());

        populateComboBoxes();
    }





}
