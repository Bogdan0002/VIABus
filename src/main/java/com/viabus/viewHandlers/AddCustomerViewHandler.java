package com.viabus.viewHandlers;

import com.viabus.controllers.AddCustomerController;
import com.viabus.models.Customer;
import com.viabus.service.CustomerService;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AddCustomerViewHandler {
    public void showAddCustomerWindow(ObservableList<Customer> customerData, CustomerService customerService) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/viabus/views/add_customer_view.fxml"));
            Parent root = loader.load();

            AddCustomerController controller = loader.getController();
            controller.setCustomerData(customerData);
            controller.setCustomerService(customerService);

            Stage stage = new Stage();
            stage.setTitle("Add Customer");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
