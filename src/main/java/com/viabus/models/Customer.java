package com.viabus.models;

import java.util.ArrayList;

public class Customer {
    private String firstName;
    private String lastName;
    private int id;
    private String email;
    private String phoneNumber;
    private static int nextId = 1000;
    private ArrayList<Customer> customers;
    private int reservationCount = 0;

public Customer(int id, String firstName, String lastName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.customers = new ArrayList();

    if (id >= nextId) {
        nextId = id + 1;
        }
    }

    public Customer(String firstName, String lastName, String email, String phoneNumber) {
        this(nextId, firstName, lastName, email, phoneNumber);
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

    @Override
    public String toString() {
        return String.valueOf(this.id);
    }

    public void setReservationCount(int reservationCount) {
        this.reservationCount = reservationCount;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }


    public int getReservationCount() {
        return reservationCount;
    }

    public void incrementReservationCount() {
        this.reservationCount += 1;
    }

    public void setCustomers(ArrayList<Customer> customers) {
        this.customers = customers;
    }
}
