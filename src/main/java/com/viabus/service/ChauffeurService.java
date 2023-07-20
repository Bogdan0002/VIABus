package com.viabus.service;

import com.viabus.models.BusType;
import com.viabus.models.Chauffeur;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ChauffeurService {
    private List<Chauffeur> chauffeurData;
    private String filePath;

    public ChauffeurService(String filePath) {
        this.filePath = filePath;
        chauffeurData = new ArrayList<>();
    }

    public List<Chauffeur> getChauffeurData() {
        return chauffeurData;
    }

    public void addChauffeur(Chauffeur chauffeur) {
        chauffeurData.add(chauffeur);
    }

    public void deleteChauffeur(Chauffeur chauffeur, ObservableList<Chauffeur> updatedChauffeurData) {
        chauffeurData = new ArrayList<>(updatedChauffeurData);
        saveChauffeurData();

    }

    public void saveChauffeurData() {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath, false)))) {
            for (Chauffeur chauffeur : chauffeurData) {
                String lineToAdd = chauffeur.getId() + "," + chauffeur.getFirstName() + "," + chauffeur.getLastName() + "," + chauffeur.getChauffeurPreference() + "," + chauffeur.getAvailability();
                writer.println(lineToAdd);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void loadChauffeurData() {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    int id = Integer.parseInt(parts[0]);
                    String firstName = String.valueOf(parts[1]);
                    String lastName = String.valueOf(parts[2]);
                    BusType chauffeurPreference = BusType.valueOf(parts[3]);
                    boolean availability = Boolean.parseBoolean(parts[4]);
                    Chauffeur chauffeur = new Chauffeur(id, firstName, lastName, chauffeurPreference, availability);
                    chauffeur.setAvailability(availability);
                    chauffeurData.add(chauffeur);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateChauffeurData(ObservableList<Chauffeur> updatedChauffeurData) {
        this.chauffeurData = new ArrayList<>(updatedChauffeurData);
    }



    public void updateChauffeurAvailability(int chauffeurId, boolean isAvailable) {
        for (Chauffeur chauffeur : chauffeurData) {
            if (chauffeur.getId() == chauffeurId) {
                chauffeur.setAvailability(isAvailable);
                break;
            }
        }
        saveChauffeurData();
    }




}
