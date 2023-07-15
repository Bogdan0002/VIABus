package com.viabus.controllers;

import com.viabus.viewHandlers.ManageBussesViewHandler;
import javafx.fxml.FXML;

public class MainWindowController {
    private ManageBussesController manageBussesController;

    public MainWindowController() {
        manageBussesController = new ManageBussesController();
    }

    @FXML
    private void handleManageBussesButton() {
        ManageBussesViewHandler viewHandler = new ManageBussesViewHandler(manageBussesController);
        viewHandler.showManageBussesWindow();
    }
}
