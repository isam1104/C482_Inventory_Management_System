package com.example.inventorymanagementsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
/**
 * <b>Inventory Management System (C482 - Software I)</b>
 * This is the that class creates an app that manages an inventory of parts and products.
 * <p><b>
 * FUTURE ENHANCEMENT: Extend functionality to the next version.
 * </b></p>
 * RUNTIME ERROR can be found at onActionSave in the AddPart.java
 * The Javadocs files can be found at /javadoc
 * @author Isam Elder
 */
public class MainForm extends Application {

    /**
     * Loads MainScreen.fxml starting the GUI and displaying the initial MainScreen.
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainForm.class.getResource("/view/MainScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 600);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main method, lauches the program.
     * @param args
     */
    public static void main(String[] args) {
        launch();
    }
}