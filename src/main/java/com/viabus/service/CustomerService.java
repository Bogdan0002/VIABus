package com.viabus.service;

import com.viabus.models.BusType;
import com.viabus.models.Chauffeur;
import com.viabus.models.Customer;
import com.viabus.models.Trip;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class CustomerService {
    private static List<Customer> customerData;
    private String filePath;

    public CustomerService(String filePath) {
        this.filePath = filePath;
        customerData = new ArrayList<>();
        loadCustomerData();
    }

    public void addCustomer(Customer customer) {
        customerData.add(customer);
    }

    public List<Customer> getCustomerData() {
        return customerData;
    }

    public void deleteCustomer(Customer customer, ObservableList<Customer> updatedCustomerData) {
        customerData = new ArrayList<>(updatedCustomerData);
        saveCustomerData();
    }

    public void saveCustomerData() {
        try { PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath, false)));
            for (Customer customer : customerData) {
                String lineToAdd = customer.getId() + "," + customer.getFirstName() + "," + customer.getLastName() + "," + customer.getEmail() + "," + customer.getPhoneNumber() + "," + customer.getReservationCount();
                writer.println(lineToAdd);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCustomerData(){
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 6) { // Expecting six parts now
                    int id = Integer.parseInt(parts[0]);
                    String firstName = String.valueOf(parts[1]);
                    String lastName = String.valueOf(parts[2]);
                    String email = String.valueOf(parts[3]);
                    String phoneNumber = String.valueOf(parts[4]);
                    int reservationCount = Integer.parseInt(parts[5]); // Parsing the reservation count
                    Customer customer = new Customer(id, firstName, lastName, email, phoneNumber);
                    customer.setReservationCount(reservationCount); // Set the reservation count for the customer
                    customerData.add(customer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Customer getCustomerById(int id) {
        return customerData.stream().filter(customer -> customer.getId() == id).findFirst().orElseThrow(NoSuchElementException::new);
    }

    public void updateCustomerData(ObservableList<Customer> updatedCustomerData){
        this.customerData = new ArrayList<>(updatedCustomerData);
    }

    public List<Customer> getAll() {
        return new ArrayList<>(customerData);
    }





}
