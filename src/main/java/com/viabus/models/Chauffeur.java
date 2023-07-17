package com.viabus.models;

import java.util.ArrayList;

public class Chauffeur {
    private String firstName;
    private String lastName;
    private int id;
    private BusType chauffeurPreference;
    private boolean availability;
    private ArrayList<Chauffeur> chauffeurs;
    // Static integer that is shared among all instances of Chauffeur
    private static int nextId = 0;


    public Chauffeur(String firstName, String lastName, BusType chauffeurPreference) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = nextId++;
        this.chauffeurPreference = chauffeurPreference;
        this.availability = true;
        this.chauffeurs = new ArrayList();
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
}
