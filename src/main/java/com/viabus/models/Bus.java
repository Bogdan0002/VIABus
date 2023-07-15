package com.viabus.models;

import java.util.ArrayList;
import java.util.Iterator;

public class Bus {
    private int busId;
    private int seatCapacity;
    private BusType busType;
    private String numberPlate;
    private boolean availability;

    private ArrayList<Bus> busses;

    public Bus(String numberPlate, int seatCapacity, BusType busType) {
        this.numberPlate = numberPlate;
        this.seatCapacity = seatCapacity;
        this.busType = busType;
        this.availability = true;
        this.busses = new ArrayList();
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }

    public int getSeatCapacity() {
        return seatCapacity;
    }

    public void setSeatCapacity(int seatCapacity) {
        this.seatCapacity = seatCapacity;
    }

    public BusType getBusType() {
        return busType;
    }

    public void setBusType(BusType busType) {
        this.busType = busType;
    }

    private boolean isAvailable()  {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

}
