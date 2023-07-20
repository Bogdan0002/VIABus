package com.viabus.controllers;

import com.viabus.models.Bus;
import com.viabus.models.BusType;
import com.viabus.models.Chauffeur;
import com.viabus.service.ChauffeurService;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.Duration;

import java.io.*;

public class AddChauffeurController {
    @FXML
    private ComboBox<BusType> preferenceComboBox;
    @FXML
    private ComboBox availabilityComboBox;
    @FXML
    private TextField fnTextField;
    @FXML
    private TextField lnTextField;
    @FXML
    private Label errorLabel;
    @FXML
    private Label infoSaved;
    private ObservableList<Chauffeur> chauffeurData;
    private Chauffeur chauffeur;
    private ChauffeurService chauffeurService;

    private String fileManager() {
        String folderPath = System.getProperty("user.dir") + File.separator + "files";
        String fileName = "Chauffeurs.txt";
        return folderPath + File.separator + fileName;
    }

    public AddChauffeurController() {
    }

    @FXML
    public void initialize(){
        System.out.println("Initializing AddChauffeurController...");
        this.chauffeurService = new ChauffeurService("files/Chauffeurs.txt");
        preferenceComboBox.setItems(FXCollections.observableArrayList(BusType.values()));
        availabilityComboBox.getItems().addAll(true, false);
        availabilityComboBox.getSelectionModel().selectFirst();
    }

    public void setChauffeurData(ObservableList<Chauffeur> chauffeurData) {
        this.chauffeurData = chauffeurData;
    }

    @FXML
    private void handleAddChauffeurButton(){
        try {
            // Get the input values from the text fields
            BusType busType = preferenceComboBox.getValue();
            String firstName = fnTextField.getText().trim();
            String lastName = lnTextField.getText().trim();
            boolean availability = (boolean) availabilityComboBox.getValue();

            // Validate the input
            if (busType == null || firstName.isEmpty()) {
                showError("Invalid First Name");
                return;
            } else if (firstName == null || lastName.isEmpty()) {
                showError("Invalid Last Name");
                return;
            }

            Chauffeur chauffeur = new Chauffeur(firstName, lastName, busType, availability);
            chauffeurService.addChauffeur(chauffeur);
            chauffeurData.add(chauffeur);
            clearInputFields();
            infoSaved("Chauffeur added successfully");
            writeChauffeurToFile(chauffeur); // Pass the 'chauffeur' to 'writeChauffeurToFile' method
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showError(String errorMessage) {
        errorLabel.setText(errorMessage);
        errorLabel.setVisible(true);
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished( event -> {
            errorLabel.setVisible(false);
        });
        delay.play();
    }

    private void infoSaved(String infoMessage) {
        infoSaved.setText(infoMessage);
        infoSaved.setVisible(true);
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished( event -> {
            infoSaved.setVisible(false);
        });
        delay.play();
    }

    public void setChauffeurService(ChauffeurService chauffeurService) {
        this.chauffeurService = chauffeurService;
    }

    private void clearInputFields() {
        preferenceComboBox.setValue(null);
        availabilityComboBox.setValue(null);
        fnTextField.clear();
        lnTextField.clear();
    }

    private void writeChauffeurToFile(Chauffeur chauffeur){
        String filePath = fileManager();
        System.out.println(filePath);
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath, true)))) {
            String lineToAdd = chauffeur.getId() + "," + chauffeur.getFirstName() + "," + chauffeur.getLastName() + "," + chauffeur.getChauffeurPreference() + "," + chauffeur.getAvailability();
            writer.println(lineToAdd);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
