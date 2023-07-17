package com.viabus.controllers;

import com.viabus.models.Customer;
import com.viabus.service.CustomerService;
import com.viabus.viewHandlers.AddCustomerViewHandler;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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
    private TableColumn<Customer, String> customerIdColumn;
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

    @FXML
    private void initialize(){
        customerData = FXCollections.observableArrayList();
        customerTableView.setItems(customerData);
        customerFirstNameColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getFirstName()).asString());
        customerLastNameColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getLastName()).asString());
        customerEmailColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getEmail()).asString());
        customerPhoneColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getPhoneNumber()).asString());
        customerIdColumn.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().getId()).asString());

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
                customerService.deleteCustomer(selectedCustomer);
            }
        }
    }

    @FXML
    private void handleEditCustomerButton(){
        String newLine = System.getProperty("line.separator");

        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem();

        if(selectedCustomer != null) {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Edit Customer");
            dialog.setHeaderText("Edit Customer");
            dialog.setContentText("Enter new customer information below:" + newLine + newLine +
                    "First Name: " + newLine +
                    "Last Name: " + newLine +
                    "Email: " + newLine +
                    "Phone Number: ");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String[] customerInfo = result.get().split(newLine);
                if (customerInfo.length == 4) {
                    selectedCustomer.setFirstName(customerInfo[0].trim());
                    selectedCustomer.setLastName(customerInfo[1].trim());
                    selectedCustomer.setEmail(customerInfo[2].trim());
                    selectedCustomer.setPhoneNumber(customerInfo[3].trim());
                    customerService.saveCustomerData();
                    customerTableView.refresh();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Invalid Input");
                    alert.setContentText("Please enter the customer information in the correct format.");
                    alert.showAndWait();
                }
            }

        }
    }

}
