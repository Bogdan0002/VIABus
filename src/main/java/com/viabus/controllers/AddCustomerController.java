package com.viabus.controllers;

import com.viabus.models.Customer;
import com.viabus.service.CustomerService;
import javafx.animation.PauseTransition;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.io.*;

public class AddCustomerController {
    @FXML
    private TextField fnTextField;
    @FXML
    private TextField lnTextField;
    @FXML
    private Label errorLabel;
    @FXML
    private Label infoSaved;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField phoneNumberTextField;
    private CustomerService customerService;
    private ObservableList<Customer> customerData;

    public AddCustomerController() {
    }

    private String fileManager(){
        String folderPath = System.getProperty("user.dir") + File.separator + "files";
        String fileName = "Customers.txt";
        return folderPath + File.separator + fileName;
    }

    @FXML
    private void initialize(){
        System.out.println("Initializing AddCustomerController...");
        this.customerService = new CustomerService("files/Customers.txt");
    }

    public void setCustomerData(ObservableList<Customer> customerData) {
        this.customerData = customerData;
    }

    @FXML
    private void handleAddCustomerButton(){
        try {
            // Get the input values from the text fields
            String firstName = fnTextField.getText().trim();
            String lastName = lnTextField.getText().trim();
            String email = emailTextField.getText().trim();
            String phoneNumber = phoneNumberTextField.getText().trim();

            // Validate the input values
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
                showError("Please fill in all the fields");
                return;
            }

            // Create a new customer object
            Customer customer = new Customer(firstName, lastName, email, phoneNumber);

            // Add the customer to the customer data
            customerData.add(customer);

            // Add the customer to the customer service
            customerService.addCustomer(customer);
            infoSaved("Customer added successfully");
            writeCustomerToFile(customer);
            clearInputFields();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void writeCustomerToFile(Customer customer) {
        String filePath = fileManager();
        System.out.println(filePath);
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath, true)))) {
            String lineToAdd = customer.getId() + "," + customer.getFirstName() + "," + customer.getLastName() + "," + customer.getEmail() + "," + customer.getPhoneNumber() + "," + customer.getReservationCount();
            writer.println(lineToAdd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * This method displays an error message to the user when the input is invalid
     * @param errorMessage to be displayed to the user when the input is invalid.
     */
    private void showError(String errorMessage) {
        errorLabel.setText(errorMessage);
        errorLabel.setVisible(true);
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished( event -> {
            errorLabel.setVisible(false);
        });
        delay.play();
    }

    /**
     * This method displays a message to the user when the object is added successfully
     * @param infoMessage to be displayed to the user when the object is added successfully.
     */
    private void infoSaved(String infoMessage) {
        infoSaved.setText(infoMessage);
        infoSaved.setVisible(true);
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished( event -> {
            infoSaved.setVisible(false);
        });
        delay.play();
    }

    private void clearInputFields() {
        fnTextField.clear();
        lnTextField.clear();
        emailTextField.clear();
        phoneNumberTextField.clear();
    }

    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }


}
