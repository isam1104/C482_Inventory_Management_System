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
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The class that creates the Modify Part Controller
 * @author Isam Elder
 */

public class ModifyPart implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private ToggleGroup InOrOut;

    @FXML
    private Label MachineIDorCompanyName;

    @FXML
    private TextField modifypartCompanytxt;

    @FXML
    private Label modifypartIDlabel;

    @FXML
    private RadioButton modifypartInhousebutton;

    @FXML
    private TextField modifypartInvtxt;

    @FXML
    private TextField modifypartMaxtxt;

    @FXML
    private TextField modifypartMintxt;

    @FXML
    private TextField modifypartNametxt;

    @FXML
    private RadioButton modifypartOutsourcedbutton;

    @FXML
    private TextField modifypartPricetxt;

    /**
     * The method to return to the main screen when the user clicks the Cancel button.
     * @param event user clicks the Cancel button
     * @throws IOException
     */
    @FXML
    void onActionCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This clears all changes, do you want to continue?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * When the user clicks the Save button, this is the mechanism used to alter an existing part. This technique uses the information entered on the Change Part page to update an existing part and saves the modified item to the inventory. The approach uses the component ID to replace the existing part that is being modified in the inventory.
     * @param event user clicks the Save button
     * @throws IOException
     */
    @FXML
    void onActionSave(ActionEvent event) throws IOException {
        try {
            int id = Integer.parseInt(modifypartIDlabel.getText());
            String name = modifypartNametxt.getText();
            int stock = Integer.parseInt(modifypartInvtxt.getText());
            double price = Double.parseDouble(modifypartPricetxt.getText());
            int min = Integer.parseInt(modifypartMintxt.getText());
            int max = Integer.parseInt(modifypartMaxtxt.getText());

            if (min > max) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING DIALOG");
                alert.setContentText("ERROR: The minimum must be less than or equal to the maximum");
                alert.showAndWait();
            } else if (stock > max || stock < min) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING DIALOG");
                alert.setContentText("ERROR: The stock value must be within the minimum and maximum range");
                alert.showAndWait();
            } else if (name.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING DIALOG");
                alert.setContentText("ERROR: The name must not be empty");
                alert.showAndWait();
            } else {
                //In-House radio button is selected
                if (modifypartInhousebutton.isSelected()) {
                    int machineID = Integer.parseInt(modifypartCompanytxt.getText());
                    Inventory.updatePart(id, new InHouse(id, name, price, stock, min, max, machineID));
                }
                //Outsourced radio button is selected
                else if (modifypartOutsourcedbutton.isSelected()) {
                    String companyName = modifypartCompanytxt.getText();
                    Inventory.updatePart(id, new Outsourced(id, name, price, stock, min, max, companyName));
                }
                //Return to Main Screen
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainScreen.fxml")));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING DIALOG");
            alert.setContentText("Please input valid values for all text fields");
            alert.showAndWait();
        }

    }
    /**
     * This method takes the part selected on the Main Screen to be modified and sends the part data to the fields on the Modify Part screen.
     * @param part the part selected on the Main Screen to be modified
     */
    public void sendPart(Part part) {
        modifypartIDlabel.setText(String.valueOf(part.getId()));
        modifypartNametxt.setText(part.getName());
        modifypartInvtxt.setText(String.valueOf(part.getStock()));
        modifypartPricetxt.setText(String.valueOf(part.getPrice()));
        modifypartMaxtxt.setText(String.valueOf(part.getMax()));
        modifypartMintxt.setText(String.valueOf(part.getMin()));

        //Get In-House or Outsourced radio button
        if(part instanceof InHouse) {
            modifypartInhousebutton.setSelected(true);
            MachineIDorCompanyName.setText("Machine ID");
            modifypartCompanytxt.setText(String.valueOf(((InHouse) part).getMachineId()));
        }
        else {
            modifypartOutsourcedbutton.setSelected(true);
            MachineIDorCompanyName.setText("Company Name");
            modifypartCompanytxt.setText(String.valueOf(((Outsourced) part).getCompanyName()));
        }
    }

    /**
     * This is the method to change the Machine ID/Company Name field to Machine ID when the user clicks the In-House radio button.
     * @param event user clicks In-House radio button
     */
    @FXML
    void onActionInhouse(ActionEvent event) {
        MachineIDorCompanyName.setText("Machine ID");
        modifypartOutsourcedbutton.setSelected(false);
    }

    /**
     * This is the method to change the Machine ID/Company Name field to Company Name when the user clicks the Outsourced radio button.
     * @param event user clicks Outsourced radio button
     */
    @FXML
    void onActionOutsourced(ActionEvent event) {
        MachineIDorCompanyName.setText("Company Name");
        modifypartInhousebutton.setSelected(false);
    }


    /**
     * This method initializes the Modify Parts class
     * @param url
     * @param resourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
