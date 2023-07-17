package com.viabus.controllers;

import com.viabus.service.BusService;
import com.viabus.service.ChauffeurService;
import com.viabus.service.CustomerService;
import com.viabus.viewHandlers.ManageBussesViewHandler;
import com.viabus.service.BusService; // Import the BusService class

import com.viabus.viewHandlers.ManageChauffeursViewHandler;
import com.viabus.viewHandlers.ManageCustomersViewHandler;
import javafx.fxml.FXML;

public class MainWindowController {
    private BusService busService; // Update the type to BusService
    private ChauffeurService chauffeurService; // Update the type to ChauffeurService
    private CustomerService customerService;

    public MainWindowController() {
    }

    @FXML
    private void handleManageBussesButton() {
        ManageBussesViewHandler viewHandler = new ManageBussesViewHandler(busService); // Pass the busService instance
        viewHandler.showManageBussesWindow();
    }

    @FXML
    private void handleManageChauffeursButton() {
        ManageChauffeursViewHandler viewHandler = new ManageChauffeursViewHandler(chauffeurService); // Pass the busService instance
        viewHandler.showManageChauffeursWindow();
    }

    @FXML
    private void handleManageCustomersButton(){
        ManageCustomersViewHandler viewHandler = new ManageCustomersViewHandler(customerService);
        viewHandler.showManageCustomersWindow();
    }
}
