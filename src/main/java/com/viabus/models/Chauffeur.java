package com.viabus.models;

import java.util.ArrayList;
import java.util.List;

public class Chauffeur {
    private String firstName;
    private String lastName;
    private int id;
    private BusType chauffeurPreference;
    private boolean availability;
    private List<Reservation> reservations;
    private ArrayList<Chauffeur> chauffeurs;
    private static int nextId = 0; // Static integer that is shared among all instances of Chauffeur


    public Chauffeur(int id, String firstName, String lastName, BusType chauffeurPreference, boolean availability) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.chauffeurPreference = chauffeurPreference;
        this.availability = true;
        this.chauffeurs = new ArrayList();
        this.reservations = new ArrayList();

        if (id >= nextId) {
            nextId = id + 1;
        }
    }
    public Chauffeur(String firstName, String lastName, BusType chauffeurPreference, boolean availability) {
        this(nextId, firstName, lastName, chauffeurPreference, availability);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public BusType getChauffeurPreference() {
        return chauffeurPreference;
    }

    public void setChauffeurPreference(BusType chauffeurPreference) {
        this.chauffeurPreference = chauffeurPreference;
    }

    public boolean getAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public ArrayList<Chauffeur> getChauffeurs() {
        return chauffeurs;
    }

    public void setChauffeurs(ArrayList<Chauffeur> chauffeurs) {
        this.chauffeurs = chauffeurs;
    }

    public static int getNextId() {
        return nextId;
    }

    public static void setNextId(int nextId) {
        Chauffeur.nextId = nextId;
    }
    public List<Reservation> getReservations() {
        return reservations;
    }
    @Override
    public String toString() {
        return String.valueOf(this.id);
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
