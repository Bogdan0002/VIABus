package com.viabus.viewHandlers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWindowViewHandler {

    public void showMainWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/viabus/views/main_window_view.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("VIA Bus Management System");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
