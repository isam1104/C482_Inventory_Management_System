package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This is the class that creates the Add Part controller.
 * @author Isam Elder
 */
public class AddPart implements Initializable{

    private static  int partID = 200;
    Stage stage;
    Parent scene;


    @FXML
    private ToggleGroup InOrOut;

    @FXML
    private Label MachineorCompany;

    @FXML
    private TextField addpartCompanytxt;

    @FXML
    private Label addpartIDlabel;

    @FXML
    private RadioButton addpartInhousebutton;

    @FXML
    private TextField addpartInvtxt;

    @FXML
    private TextField addpartMaxtxt;

    @FXML
    private TextField addpartMintxt;

    @FXML
    private TextField addpartNametxt;

    @FXML
    private RadioButton addpartOutsourcedbutton;

    @FXML
    private TextField addpartPricetxt;

    /**
     * This method returns to the MainScreen when the user clicks the cancel button.
     * @param event user clicks the Cancel Button.
     */

    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will clear all values, do you want to continue?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * <p><b>
     * RUNTIME ERROR: Early iterations of the addPart controller could not handle blank fields or erroneous number formats (integers in price, doubles in min or max). My early attempts involved validating each field individually in order to stop the addPart process if an erroneous number was discovered. These efforts were completely futile in averting the NumberFormatException. I successfully introduced a try/catch argument after viewing the exceptions webinar, meeting with a course instructor, and discussing with a classmate, where if an improper number format was entered into a field, a WARNING warning was issued to inform the user to input accurate values into each field.
     * </b></p>
     * This is the method used to save a new part when a user clicks the Save button.
     * @param event The user clicks the Save button. The method creates a new part using the information inputted on the Add Part screen and saves the new part to the inventory.
     * @throws IOException
     * @throws NumberFormatException
     */

    @FXML
    void onActionSave(ActionEvent event) {
        try {
            int id = Integer.parseInt(addpartIDlabel.getText());
            String name = addpartNametxt.getText();
            int stock = Integer.parseInt(addpartInvtxt.getText());
            double price = Double.parseDouble(addpartPricetxt.getText());
            int min = Integer.parseInt(addpartMintxt.getText());
            int max = Integer.parseInt(addpartMaxtxt.getText());

            if (min > max) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING DIALOG");
                alert.setContentText("ERROR: The minimum must be less than or equal to the maximum");
                alert.showAndWait();
            }
            else if (stock > max || stock < min) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING DIALOG");
                alert.setContentText("ERROR: The stock value must be within the minimum and maximum range");
                alert.showAndWait();
            }
            else if (name.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING DIALOG");
                alert.setContentText("ERROR: The name must not be empty");
                alert.showAndWait();
            }
            else {
                //In-House radio button is selected
                if (addpartInhousebutton.isSelected()) {
                    int machineID = Integer.parseInt(addpartCompanytxt.getText());
                    Inventory.addPart(new InHouse(id, name, price, stock, min, max, machineID));
                }
                //Outsourced radio button is selected
                else if (addpartOutsourcedbutton.isSelected()) {
                    String companyName = addpartCompanytxt.getText();
                    Inventory.addPart(new Outsourced(id, name, price, stock, min, max, companyName));
                }
                //Return to Main Screen
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
        catch(NumberFormatException | IOException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please input valid values for all text fields");
            alert.showAndWait();
        }
    }

    /**
     * This is the method to get the new part id beginning at 200.
     * @return the part id
     */

    private int getPartIDCount() {
        partID++;
        return partID;
    }

    /**
     * This is the method to change the Machine ID/Company Name field to Machine ID when the user clicks the In-House radio button.
     * @param event user clicks In-House radio button
     */

    @FXML
    void onActionInhouse(ActionEvent event){
        MachineorCompany.setText("Machine ID");
        addpartOutsourcedbutton.setSelected(false);
    }

    /**
     * This is the method to change the Machine ID/Company Name field to Company Name when the user clicks the Outsourced radio button.
     * @param event user clicks Outsourced radio button
     */

    @FXML
    void onActionOutsourced(ActionEvent event) {
        MachineorCompany.setText("Company Name");
        addpartInhousebutton.setSelected(false);
    }

    /**
     * This is the initializable for Add Part controller
     * @param url
     * @param resourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addpartInhousebutton.setSelected(true);
        partID = getPartIDCount();
        addpartIDlabel.setText(String.valueOf(partID));
    }
}
