package com.viabus.controllers;

import com.viabus.models.Bus;
import com.viabus.models.Reservation;
import com.viabus.models.Trip;
import com.viabus.service.*;
import com.viabus.viewHandlers.*;


import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MainWindowController {
    @FXML
    private TableView<Reservation> reservationTableView;
    @FXML
    private TableColumn<Reservation, Integer> reservationIdColumn;
    @FXML
    private TableColumn<Reservation, Integer> tripIdColumn;
    @FXML
    private TableColumn<Reservation, Integer> customerIdColumn;
    @FXML
    private TableColumn<Reservation, Integer> chauffeurIdColumn;
    @FXML
    private TableColumn<Reservation, String> busIdColumn;
    @FXML
    private TableColumn<Reservation,String> scheduleColumn;
    @FXML
    private TableColumn<Reservation,String> destinationColumn;
    @FXML
    private TableColumn<Reservation,String> departureColumn;
    private BusService busService; // Update the type to BusService
    private ChauffeurService chauffeurService; // Update the type to ChauffeurService
    private CustomerService customerService;
    private TripService tripService;
    private ReservationService reservationService;
    private ObservableList<Reservation> reservationData;

    public MainWindowController() {
        this.reservationService = new ReservationService("files/Reservations.txt");
        this.busService = new BusService("files/Busses.txt");
        this.tripService = TripService.getInstance("files/Trips.txt");
        this.chauffeurService = new ChauffeurService("files/Chauffeurs.txt");
        this.customerService = new CustomerService("files/Customers.txt");
    }

    @FXML
    private void initialize(){
        reservationData = FXCollections.observableArrayList();
        reservationTableView.setItems(reservationData);
        reservationIdColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getId()));
        tripIdColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getTrip().getId()));
        customerIdColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getCustomer().getId()));
        chauffeurIdColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getChauffeur().getId()));
        busIdColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getBus().getNumberPlate()));
        scheduleColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getSchedule()));
        destinationColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getTrip().getDestination()));
        departureColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getTrip().getDeparture()));

        reservationService.loadReservationData(tripService, chauffeurService, busService, customerService);
        reservationData.addAll(reservationService.getReservationData());

    }


    @FXML
    private void handleManageBussesButton() {
        ManageBussesViewHandler viewHandler = new ManageBussesViewHandler(busService); // Pass the busService instance
        viewHandler.showManageBussesWindow();
    }

    @FXML
    private void handleDeleteReservationButton(){
        Reservation selectedReservation = reservationTableView.getSelectionModel().getSelectedItem();

        if (selectedReservation != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirm Deletion");
            confirmationAlert.setHeaderText("Delete Reservation");
            confirmationAlert.setContentText("Are you sure you want to delete this reservation?");
            Optional<ButtonType> result = confirmationAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                reservationData.remove(selectedReservation);
                reservationService.deleteReservation(selectedReservation, reservationData);
            }
        }
    }

    @FXML
    private void handleManageChauffeursButton() {
        ManageChauffeursViewHandler viewHandler = new ManageChauffeursViewHandler(chauffeurService); // Pass the busService instance
        viewHandler.showManageChauffeursWindow();
    }
//    @FXML
//    private void handleRefreshButton(){
//        ObservableList<Reservation> updatedData = getUpdatedReservations();
//        reservationTableView.setItems(updatedData);
//        reservationTableView.refresh();
//    }


//    private ObservableList<Reservation> getUpdatedReservations() {
//        // Assuming you have a service method that returns the updated reservations as a List
//        List<Reservation> updatedReservations = reservationService.getReservationData();
//
//        // Convert that List to an ObservableList
//        ObservableList<Reservation> observableReservations = FXCollections.observableArrayList(updatedReservations);
//
//        return observableReservations;
//    }


    @FXML
    private void handleManageCustomersButton(){
        ManageCustomersViewHandler viewHandler = new ManageCustomersViewHandler(customerService);
        viewHandler.showManageCustomersWindow();
    }

    @FXML
    private void handleManageTripsButton(){
        ManageTripsViewHandler viewHandler = new ManageTripsViewHandler(tripService);
        viewHandler.showManageTripsWindow();
    }

    @FXML
    private void handleCreateReservationButton(){
        if (reservationService == null) {
            System.out.println("ReservationService is null");
            return;
        }

        CreateReservationViewHandler viewHandler = new CreateReservationViewHandler();
        viewHandler.showCreateReservationWindow(reservationData, reservationService);
    }


    public void setReservationService(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public void setReservationData(ObservableList<Reservation> reservationData) {
        this.reservationData = reservationData;
    }

}
