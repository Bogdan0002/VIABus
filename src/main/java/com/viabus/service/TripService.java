package com.viabus.service;

import com.viabus.models.Trip;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TripService {
    private List<Trip> trips = new ArrayList<>();
    private final String fileName = "trips.txt";

    public void addTrip(Trip trip) {
        trips.add(trip);
        saveData();
    }

    public void deleteTrip(Trip trip) {
        trips.remove(trip);
        saveData();
    }

    public List<Trip> getAllTrips() {
        return trips;
    }

    // Other methods for updating and retrieving trip data

    private void saveData() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (Trip trip : trips) {
                writer.println(trip.getDestination() + "," + trip.getDuration() + "," + trip.getSchedule());
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }
}

