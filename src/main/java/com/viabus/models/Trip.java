package com.viabus.models;

import java.util.ArrayList;

public class Trip {
    private String destination;
    private String departure;
    private String duration;
    private ArrayList<Trip> trips;
    private int id;
    private static int nextId = 9000;

    public Trip(int id, String destination, String departure, String duration) {
        this.destination = destination;
        this.departure = departure;
        this.duration = duration;
        this.id = id;
        this.trips = new ArrayList();

        if (id >= nextId) {
            nextId = id + 1;
        }

    }

    public Trip(String destination, String departure, String duration) {
        this(nextId, destination, departure, duration);
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
