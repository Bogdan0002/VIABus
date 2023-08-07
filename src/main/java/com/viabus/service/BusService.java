package com.viabus.service;

import com.viabus.models.Bus;
import com.viabus.models.BusType;
import com.viabus.models.Chauffeur;
import com.viabus.models.Trip;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class BusService {
        private List<Bus> busData;
        private String filePath;

        public BusService(String filePath) {
            this.filePath = filePath;
            busData = new ArrayList<>();
            loadBusData();
        }


        public List<Bus> getBusData() {
            return busData;
        }

        public void loadBusData() {
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 4) {
                        String numberPlate = String.valueOf(parts[0]);
                        BusType busType = BusType.valueOf(parts[1]);
                        int seatCapacity = Integer.parseInt(parts[2]);
                        boolean availability = Boolean.parseBoolean(parts[3]);
                        Bus bus = new Bus(numberPlate, seatCapacity, busType, availability);
                        bus.setAvailability(availability);
                        busData.add(bus);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public  void saveBusData() {
            try { PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath, false)));
                for (Bus bus : busData) {
                    String lineToAdd = bus.getNumberPlate() + "," + bus.getBusType() + "," + bus.getSeatCapacity() + "," + bus.isAvailable();
                    writer.println(lineToAdd);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    public List<Bus> getAvailableBuses() {
        return busData.stream().filter(Bus::isAvailable).collect(Collectors.toList());
    }

        public void addBus(Bus bus) {
            busData.add(bus);
        }

    public Bus getBusByNumberPlate(String numberPlate) {
        if(busData != null) {
            for (Bus bus : busData) {
                if (bus.getNumberPlate().equals(numberPlate)) {
                    return bus;
                }
            }
        }
        throw new NoSuchElementException("Bus with number plate " + numberPlate + " not found.");
    }


    public void deleteBus(Bus bus, ObservableList<Bus> updatedBusData) {
        busData = new ArrayList<>(updatedBusData);
        saveBusData();
    }

    public void updateBusData(ObservableList<Bus> updatedBusData) {
        this.busData = new ArrayList<>(updatedBusData);
    }
    public List<Bus> getAll() {
        return new ArrayList<>(busData);
    }

    public List<Bus> getAvailable() {
        return busData.stream().filter(Bus::isAvailable).collect(Collectors.toList());

    }
}


