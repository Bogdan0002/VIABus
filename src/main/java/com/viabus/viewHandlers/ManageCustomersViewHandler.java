package com.viabus.viewHandlers;

import com.viabus.controllers.ManageCustomersController;
import com.viabus.service.CustomerService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ManageCustomersViewHandler {
    private final CustomerService customerService;

    public ManageCustomersViewHandler(CustomerService customerService) {
        this.customerService = new CustomerService("files/Customers.txt");
    }

    public void showManageCustomersWindow() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/viabus/views/manage_customers_view.fxml"));
            Parent root = loader.load();

            ManageCustomersController controller = loader.getController();
            controller.setCustomerService(customerService);

            Stage stage = new Stage();
            stage.setTitle("Manage Customers");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
