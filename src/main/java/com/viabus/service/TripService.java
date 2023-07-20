package com.viabus.service;

import com.viabus.models.Trip;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TripService {
    private List<Trip> tripData;
    private String filePath;

        public TripService(String filePath) {
            this.filePath = filePath;
            tripData = new ArrayList<>();
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

        public void updateTripData(ObservableList<Trip> updatedTripData) {
            this.tripData = new ArrayList<>(updatedTripData);
        }

}

