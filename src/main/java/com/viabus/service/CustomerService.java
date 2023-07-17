package com.viabus.service;

import com.viabus.models.BusType;
import com.viabus.models.Chauffeur;
import com.viabus.models.Customer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerService {
    private List<Customer> customerData;
    private String filePath;

    public CustomerService(String filePath) {
        this.filePath = filePath;
        this.customerData = new ArrayList<>();
    }

    public void addCustomer(Customer customer) {
        customerData.add(customer);
    }

    public List<Customer> getCustomerData() {
        return customerData;
    }

    public void deleteCustomer(Customer customer) {
        customerData.remove(customer);
        saveCustomerData();
    }

    public void saveCustomerData() {
        try {
            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath, false)));
            for (Customer customer : customerData) {
                String lineToAdd = customer.getId() + "," + customer.getFirstName() + "," + customer.getLastName() + "," + customer.getEmail() + "," + customer.getPhoneNumber();
                writer.println(lineToAdd);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCustomerData(){
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    int id = Integer.parseInt(parts[0]);
                    String firstName = String.valueOf(parts[1]);
                    String lastName = String.valueOf(parts[2]);
                    String email = String.valueOf(parts[3]);
                    String phoneNumber = String.valueOf(parts[4]);
                    Customer customer = new Customer(firstName, lastName, email, phoneNumber);
                    customerData.add(customer);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
