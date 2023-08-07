package com.viabus.models;

import java.time.LocalDate;

public class Reservation {
    private static int nextId = 5000;
    private int id;
    private LocalDate startDate;
    private LocalDate endDate;
    private Trip trip;
    private Chauffeur chauffeur;
    private Bus bus;
    private Customer customer;

    public Reservation(int id, LocalDate startDate, LocalDate endDate, Trip trip, Chauffeur chauffeur, Bus bus, Customer customer) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.trip = trip;
        this.chauffeur = chauffeur;
        this.bus = bus;
        this.customer = customer;

        //set availability to false since they're now reserved
        this.chauffeur.setAvailability(false);
        this.bus.setAvailability(false);

        if (id >= nextId) {
            nextId = id + 1;
        }
    }

    public Reservation(LocalDate startDate, LocalDate endDate, Trip trip, Chauffeur chauffeur, Bus bus, Customer customer) {
        this(nextId, startDate, endDate, trip, chauffeur, bus, customer);
    }

    public static int getNextId() {
        return nextId;
    }

    public static void setNextId(int nextId) {
        Reservation.nextId = nextId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }
    public String getSchedule(){
        return startDate + " - " + endDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Chauffeur getChauffeur() {
        return chauffeur;
    }

    public void setChauffeur(Chauffeur chauffeur) {
        this.chauffeur = chauffeur;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }
    public void setSchedule(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }


    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
