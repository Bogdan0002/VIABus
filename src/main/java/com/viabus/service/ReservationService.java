package com.viabus.service;

import com.viabus.models.*;
import javafx.collections.ObservableList;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ReservationService {
    private List<Reservation> reservationData;
    private String filePath;
    private List<Bus> busData;
    private List<Chauffeur> chauffeurData;

    public ReservationService(String filePath) {
        this.filePath = filePath;
        reservationData = new ArrayList<>();
    }


    public List<Reservation> getReservationData() {
        return reservationData;
    }

    public void addReservation(Reservation reservation) {
        reservationData.add(reservation);
        reservation.getCustomer().incrementReservationCount(); // Increment the count
    }

    public void deleteReservation(Reservation reservation, ObservableList<Reservation> updatedReservationData) {
        reservationData = new ArrayList<>(updatedReservationData);
        saveReservationData();
    }

    /**
     * This method is used to save the reservation data to the file.
     */
    public void saveReservationData() {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath, false)))) {
            for (Reservation reservation : reservationData) {
                String lineToAdd = reservation.getId() + "," + reservation.getStartDate() + "," + reservation.getEndDate() + "," +
                        reservation.getTrip().getId() + "," + reservation.getChauffeur().getId() + "," +
                        reservation.getBus().getNumberPlate() + "," + reservation.getCustomer().getId();
                writer.println(lineToAdd);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is used to load the reservation data from the file to the reservationData list.
     * @param tripService for getting the trip object
     * @param chauffeurService for getting the chauffeur object
     * @param busService for getting the bus object
     * @param customerService  for getting the customer object
     */
    public void loadReservationData(TripService tripService, ChauffeurService chauffeurService, BusService busService, CustomerService customerService) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 7) {
                    int id = Integer.parseInt(parts[0]);
                    LocalDate startDate = LocalDate.parse(parts[1]);
                    LocalDate endDate = LocalDate.parse(parts[2]);
                    Trip trip = tripService.getTripById(Integer.parseInt(parts[3]));
                    Chauffeur chauffeur = chauffeurService.getChauffeurById(Integer.parseInt(parts[4]));
                    Bus bus = busService.getBusByNumberPlate(parts[5]);
                    Customer customer = customerService.getCustomerById(Integer.parseInt(parts[6]));
                    Reservation reservation = new Reservation(id, startDate, endDate, trip, chauffeur, bus, customer);
                    reservationData.add(reservation);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public List<Bus> getAvailableBuses(LocalDate startDate, LocalDate endDate) {
        return busData.stream()
                .filter(bus -> isAvailable(bus, startDate, endDate))
                .collect(Collectors.toList());
    }

    public List<Chauffeur> getAvailableChauffeurs(LocalDate startDate, LocalDate endDate) {
        return chauffeurData.stream()
                .filter(chauffeur -> isAvailable(chauffeur, startDate, endDate))
                .collect(Collectors.toList());
    }

    public void updateReservationData(ObservableList<Reservation> updatedReservationData){
        this.reservationData = new ArrayList<>(updatedReservationData);
    }

    private boolean isAvailable(Bus bus, LocalDate startDate, LocalDate endDate) {
        return bus.getReservations().stream()
                .noneMatch(reservation -> isOverlapping(reservation, startDate, endDate));
    }

    private boolean isAvailable(Chauffeur chauffeur, LocalDate startDate, LocalDate endDate) {
        return chauffeur.getReservations().stream()
                .noneMatch(reservation -> isOverlapping(reservation, startDate, endDate));
    }

    private boolean isOverlapping(Reservation reservation, LocalDate startDate, LocalDate endDate) {
        return !reservation.getEndDate().isBefore(startDate) && !reservation.getStartDate().isAfter(endDate);
    }


}
