package com.viabus.service;

import com.viabus.models.Bus;
import com.viabus.models.BusType;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BusService {
        private List<Bus> busData;
        private String filePath;

        public BusService(String filePath) {
            this.filePath = filePath;
            busData = new ArrayList<>();
        }

        public List<Bus> getBusData() {
            return busData;
        }

        public void loadBusData() {
            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length == 3) {
                        String numberPlate = String.valueOf(parts[0]);
                        BusType busType = BusType.valueOf(parts[1]);
                        int seatCapacity = Integer.parseInt(parts[2]);
                        Bus bus = new Bus(numberPlate, seatCapacity, busType);
                        busData.add(bus);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void saveBusData() {
            try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath, false)))) {
                for (Bus bus : busData) {
                    String lineToAdd = bus.getNumberPlate() + "," + bus.getBusType() + "," + bus.getSeatCapacity();
                    writer.println(lineToAdd);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void addBus(Bus bus) {
            busData.add(bus);
        }

        public void deleteBus(Bus bus) {
            busData.remove(bus);
        }


    }

