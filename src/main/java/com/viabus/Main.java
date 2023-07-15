package com.viabus;

import com.viabus.viewHandlers.MainWindowViewHandler;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        MainWindowViewHandler mainViewHandler = new MainWindowViewHandler();
        mainViewHandler.showMainWindow();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

