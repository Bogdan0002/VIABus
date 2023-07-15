package com.viabus.models;

public class Trip {
    private String destination;
    private String departure;
    private int duration;
    private String schedule;

    public Trip(String destination, String departure, int duration, String schedule) {
        this.destination = destination;
        this.departure = departure;
        this.duration = duration;
        this.schedule = schedule;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
}
