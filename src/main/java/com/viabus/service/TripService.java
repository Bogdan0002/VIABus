package com.viabus.service;

import com.viabus.models.Trip;
import javafx.collections.ObservableList;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class TripService {
    private static TripService instance;
    private List<Trip> tripData;
    private String filePath;

        public TripService(String filePath) {
            this.filePath = filePath;
            tripData = new ArrayList<>();
            loadTripData();
        }

        public List<Trip> getTripData() {
            return tripData;
        }

        public void addTrip(Trip trip) {
            tripData.add(trip);
        }

        public void deleteTrip(Trip trip, ObservableList<Trip> updatedTripData) {
            tripData = new ArrayList<>(updatedTripData);
            saveTripData();
        }

    /**
     * Singleton pattern
     * @param filePath
     * @return
     */
    public static TripService getInstance(String filePath) {
        if (instance == null) {
            instance = new TripService(filePath);
        }
        return instance;
    }

    /**
     * Saves the trip data to the file
     */
    public void saveTripData(){
            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath, false)))) {
                for (Trip trip : tripData) {
                    String lineToAdd = trip.getId() + "," + trip.getDestination() + "," + trip.getDeparture() + "," + trip.getDuration();
                    writer.println(lineToAdd);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    /**
     * Loads the trip data from the file to the tripData list
     */
    public void loadTripData(){

            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        int id = Integer.parseInt(parts[0]);
                        String destination = String.valueOf(parts[1]);
                        String departure = String.valueOf(parts[2]);
                        String duration = String.valueOf(parts[3]);
                        Trip trip = new Trip(id, destination, departure, duration);
                        tripData.add(trip);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    /**
     * Updates the trip data
     * @param updatedTripData
     */
        public void updateTripData(ObservableList<Trip> updatedTripData) {
            this.tripData = new ArrayList<>(updatedTripData);
        }

    public  Trip getTripById(int tripId) {
        if(tripData != null) {
            for (Trip trip : tripData) {
                if (trip.getId() == tripId) {
                    return trip;
                }
            }
        }
        throw new NoSuchElementException("No trip with id " + tripId + " found");
    }

    /**
     * Returns all the trips
     * @return
     */
    public List<Trip> getAll() {
        return new ArrayList<>(tripData);
    }

}

