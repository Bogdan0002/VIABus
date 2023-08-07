package com.viabus.controllers;

import com.viabus.models.*;
import com.viabus.service.*;
import com.viabus.viewHandlers.*;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import java.time.LocalDate;
import java.util.*;

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
    private BusService busService;
    private ChauffeurService chauffeurService;
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
        ManageBussesViewHandler viewHandler = new ManageBussesViewHandler(busService);
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
    private void handleEditReservationButton() {
        // Get the selected reservation from the table view
        Reservation selectedReservation = reservationTableView.getSelectionModel().getSelectedItem();

        if (selectedReservation != null) {
            // Create the custom dialog.
            Dialog<Map<String, Object>> dialog = new Dialog<>();
            dialog.setTitle("Edit Reservation");

            // Set the button types.
            ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

            // Create the fields, labels, and combo boxes and add them to a grid pane.
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            ComboBox<Trip> tripBox = new ComboBox<>();
            tripBox.getItems().addAll(tripService.getAll());
            tripBox.setValue(selectedReservation.getTrip());
            ComboBox<Customer> customerBox = new ComboBox<>();
            customerBox.getItems().addAll(customerService.getAll());
            customerBox.setValue(selectedReservation.getCustomer());
            ComboBox<Chauffeur> chauffeurBox = new ComboBox<>();
            chauffeurBox.getItems().addAll(chauffeurService.getAll());
            chauffeurBox.setValue(selectedReservation.getChauffeur());
            ComboBox<Bus> busBox = new ComboBox<>();
            busBox.getItems().addAll(busService.getAll());
            busBox.setValue(selectedReservation.getBus());
            DatePicker startDatePicker = new DatePicker(selectedReservation.getStartDate());
            DatePicker endDatePicker = new DatePicker(selectedReservation.getEndDate());

            grid.add(new Label("Trip:"), 0, 0);
            grid.add(tripBox, 1, 0);
            grid.add(new Label("Customer:"), 0, 1);
            grid.add(customerBox, 1, 1);
            grid.add(new Label("Chauffeur:"), 0, 2);
            grid.add(chauffeurBox, 1, 2);
            grid.add(new Label("Bus:"), 0, 3);
            grid.add(busBox, 1, 3);
            grid.add(new Label("Start Date:"), 0, 4);
            grid.add(startDatePicker, 1, 4);
            grid.add(new Label("End Date:"), 0, 5);
            grid.add(endDatePicker, 1, 5);

            dialog.getDialogPane().setContent(grid);

            // Handle save button action.
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == saveButtonType) {
                    Map<String, Object> result = new HashMap<>();
                    result.put("trip", tripBox.getValue());
                    result.put("customer", customerBox.getValue());
                    result.put("chauffeur", chauffeurBox.getValue());
                    result.put("bus", busBox.getValue());
                    result.put("startDate", startDatePicker.getValue()); // Add start date
                    result.put("endDate", endDatePicker.getValue()); // Add end date
                    return result;
                }
                return null;
            });

            Optional<Map<String, Object>> result = dialog.showAndWait();

            result.ifPresent(data -> {
                // Get the new data and update reservation.
                Trip newTrip = (Trip) data.get("trip");
                Customer newCustomer = (Customer) data.get("customer");
                Chauffeur newChauffeur = (Chauffeur) data.get("chauffeur");
                Bus newBus = (Bus) data.get("bus");
                LocalDate newStartDate = (LocalDate) data.get("startDate");
                LocalDate newEndDate = (LocalDate) data.get("endDate");

                selectedReservation.setTrip(newTrip);
                selectedReservation.setCustomer(newCustomer);
                selectedReservation.setChauffeur(newChauffeur);
                selectedReservation.setBus(newBus);
                selectedReservation.setStartDate(newStartDate);
                selectedReservation.setEndDate(newEndDate);

                reservationService.updateReservationData(reservationData);
                reservationService.saveReservationData();
                reservationTableView.refresh();
            });
        }
    }





    @FXML
    private void handleManageChauffeursButton() {
        ManageChauffeursViewHandler viewHandler = new ManageChauffeursViewHandler(chauffeurService);
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
