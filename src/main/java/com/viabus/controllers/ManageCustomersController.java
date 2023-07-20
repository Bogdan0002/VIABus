package com.viabus.controllers;

import com.viabus.models.BusType;
import com.viabus.models.Chauffeur;
import com.viabus.models.Customer;
import com.viabus.service.ChauffeurService;
import com.viabus.service.CustomerService;
import com.viabus.viewHandlers.AddCustomerViewHandler;
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

public class ManageCustomersController {
    @FXML
    private TableView<Customer> customerTableView;
    @FXML
    private TableColumn<Customer, String> customerFirstNameColumn;
    @FXML
    private TableColumn<Customer, String> customerLastNameColumn;
    @FXML
    private TableColumn<Customer, String> customerEmailColumn;
    @FXML
    private TableColumn<Customer, String> customerPhoneColumn;
    @FXML
    private TableColumn<Customer, Integer> customerIdColumn;
    private ObservableList<Customer> customerData;
    private CustomerService customerService = new CustomerService("files/Customers.txt");

    @FXML
    private void handleAddCustomerButton(){
        if (customerService == null) {
            System.out.println("CustomerService is null");
            return;
        }

        AddCustomerViewHandler viewHandler = new AddCustomerViewHandler();
        viewHandler.showAddCustomerWindow(customerData, customerService);
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @FXML
    private void initialize(){
        customerData = FXCollections.observableArrayList();
        customerTableView.setItems(customerData);
        customerFirstNameColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getFirstName()).asString());
        customerLastNameColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getLastName()).asString());
        customerEmailColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getEmail()).asString());
        customerPhoneColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getPhoneNumber()).asString());
        customerIdColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getId()));

        customerService.loadCustomerData();
        customerData.addAll(customerService.getCustomerData());
    }

    @FXML
    private void handleDeleteCustomerButton(){
        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();

        if (selectedCustomer != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Customer");
            alert.setHeaderText("Are you sure you want to delete this customer?");
            alert.setContentText("This action cannot be undone.");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                customerData.remove(selectedCustomer);
                customerService.deleteCustomer(selectedCustomer, customerData);
            }
        }
    }

    @FXML
    private void handleEditCustomerButton(){
        // Get the selected customer from the table view
        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();

        if (selectedCustomer != null) {
            // Create the custom dialog.
            Dialog<Pair<String, String>> dialog = new Dialog<>();
            dialog.setTitle("Edit Customer");

            // Set the button types.
            ButtonType saveButtonType = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

            // Create the fields and labels and add them to a grid pane.
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(20, 150, 10, 10));

            TextField firstName = new TextField();
            firstName.setText(selectedCustomer.getFirstName());
            TextField lastName = new TextField();
            lastName.setText(selectedCustomer.getLastName());
            TextField email = new TextField();
            email.setText(selectedCustomer.getEmail());
            TextField phoneNumber = new TextField();
            phoneNumber.setText(selectedCustomer.getPhoneNumber());

            grid.add(new Label("First Name:"), 0, 0);
            grid.add(firstName, 1, 0);
            grid.add(new Label("Last Name:"), 0, 1);
            grid.add(lastName, 1, 1);
            grid.add(new Label("Email:"), 0, 2);
            grid.add(email, 1, 2);
            grid.add(new Label("Phone:"), 0, 3);
            grid.add(phoneNumber, 1, 3);

            dialog.getDialogPane().setContent(grid);

            // Enable/Disable save button depending on whether fields are empty.
            Node saveButton = dialog.getDialogPane().lookupButton(saveButtonType);
            saveButton.setDisable(true);

            // Logic to enable save button only when fields are not empty
            firstName.textProperty().addListener((observable, oldValue, newValue) -> saveButton.setDisable(newValue.trim().isEmpty()));
            lastName.textProperty().addListener((observable, oldValue, newValue) -> saveButton.setDisable(newValue.trim().isEmpty()));
            email.textProperty().addListener((observable, oldValue, newValue) -> saveButton.setDisable(newValue.trim().isEmpty()));
            phoneNumber.textProperty().addListener((observable, oldValue, newValue) -> saveButton.setDisable(newValue.trim().isEmpty()));

            // Handle save button action.
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == saveButtonType) {
                    return new Pair<>(firstName.getText(), lastName.getText());
                }
                return null;
            });

            Optional<Pair<String, String>> result = dialog.showAndWait();

            result.ifPresent(data -> {
                // Get the new data and update customer.
                String newFirstName = data.getKey();
                String newLastName = data.getValue();
                String newEmail = email.getText();
                String newPhoneNumber = phoneNumber.getText();

                selectedCustomer.setFirstName(newFirstName);
                selectedCustomer.setLastName(newLastName);
                selectedCustomer.setEmail(newEmail);
                selectedCustomer.setPhoneNumber(newPhoneNumber);

                customerService.updateCustomerData(customerData);

                customerService.saveCustomerData();
                customerTableView.refresh();
            });
        }
    }


}
