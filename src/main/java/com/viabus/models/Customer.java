package com.viabus.models;

import java.util.ArrayList;

public class Customer {
    private String firstName;
    private String lastName;
    private int id;
    private String email;
    private String phoneNumber;
    private static int nextId = 0;
    private ArrayList<Customer> customers;

public Customer(String firstName, String lastName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = nextId++;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.customers = new ArrayList();
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public static int getNextId() {
        return nextId;
    }

    public static void setNextId(int nextId) {
        Customer.nextId = nextId;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public void setCustomers(ArrayList<Customer> customers) {
        this.customers = customers;
    }
}
