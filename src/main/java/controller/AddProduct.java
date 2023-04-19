package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * The class that creates the Add product controller.
 * @author Isam Elder
 */

public class AddProduct implements Initializable {

    private static int productID = 400;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private Product newProduct =  new Product(0,"",0.00,0,0,0,null);

    Stage stage;
    Parent scene;

    @FXML
    private Label addproductIDlabel;

    @FXML
    private TextField addproductInvtxt;

    @FXML
    private TextField addproductMaxtxt;

    @FXML
    private TextField addproductMintxt;

    @FXML
    private TextField addproductNametxt;

    @FXML
    private TextField addproductPricetxt;

    @FXML
    private TableColumn<Part, Integer> addproductaddInventoryCol;

    @FXML
    private TableColumn<Part, Integer> addproductaddPartIDCol;

    @FXML
    private TableColumn<Part, String> addproductaddPartNameCol;

    @FXML
    private TableColumn<Part, Double> addproductaddPriceCol;

    @FXML
    private TextField addproductaddSearchtxt;

    @FXML
    private TableView<Part> addproductaddTableView;

    @FXML
    private TableColumn<Part, Integer> addproductdeleteInventoryCol;

    @FXML
    private TableColumn<Part, Integer> addproductdeletePartIDCol;

    @FXML
    private TableColumn<Part, String> addproductdeletePartNameCol;

    @FXML
    private TableColumn<Part, Double> addproductdeletePriceCol;

    @FXML
    private TableView<Part> addproductdeleteTableView;

    /**
     * The method that adds a new part to the new product being created when user clicks the Add button. It takes a part from the inventory in the top table and copies the part to the associated parts table below.
     * @param event user clicks the Add button
     */

    @FXML
    void onActionAdd(ActionEvent event) {
        Part selectedPart = addproductaddTableView.getSelectionModel().getSelectedItem();
        associatedParts.add(selectedPart);
        addproductdeleteTableView.setItems(associatedParts);
    }

    /**
     * The method to return to the main screen when the user clicks the Cancel button.
     * @param event user clicks the Cancel button
     * @throws IOException
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
     * The method to remove an associated part from the new product being created when the USer clicks the Remove Associated Part button.
     * @param event
     */

    @FXML
    void onActionDelete(ActionEvent event) {
        Part delPart = addproductdeleteTableView.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this part?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.show();
            associatedParts.remove(delPart);
            addproductdeleteTableView.setItems(associatedParts);
        }
    }

    /**
     * When the user selects the Save button, this is the mechanism used to save a new product. This method generates a new product from the information entered on the New Product screen and adds it to the inventory.
     * @param event
     * @throws IOException
     */

    @FXML
    void onActionSave(ActionEvent event) throws IOException {
        try {
            newProduct.setId(Integer.parseInt(addproductIDlabel.getText()));
            newProduct.setName(addproductNametxt.getText());
            newProduct.setPrice(Double.parseDouble(addproductPricetxt.getText()));
            newProduct.setStock(Integer.parseInt(addproductInvtxt.getText()));
            newProduct.setMin(Integer.parseInt(addproductMintxt.getText()));
            newProduct.setMax(Integer.parseInt(addproductMaxtxt.getText()));
            newProduct.setAssociatedParts(associatedParts);

            if (Integer.parseInt(addproductMintxt.getText()) > Integer.parseInt(addproductMaxtxt.getText())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("ERROR: The minimum must be less than or equal to the maximum");
                alert.showAndWait();
            } else if (Integer.parseInt(addproductInvtxt.getText()) > Integer.parseInt(addproductMaxtxt.getText()) || Integer.parseInt(addproductInvtxt.getText()) < Integer.parseInt(addproductMintxt.getText())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("ERROR: The stock value must be within the minimum and maximum range");
                alert.showAndWait();
            } else if (addproductNametxt.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning Dialog");
                alert.setContentText("ERROR: The name must not be empty");
                alert.showAndWait();
            }
            else {
                Inventory.addProduct(newProduct);
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainScreen.fxml")));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please input valid values for all text fields");
            alert.showAndWait();
        }
    }
    /**
     * The method to search parts inventory when the user clicks the Search button. It searches both exact part IDs and full or partial part names.
     * @param event user clicks the Search button
     */
    @FXML
    void onActionSearch(ActionEvent event) {
        String searchField = addproductaddSearchtxt.getText();
        try {
            ObservableList<Part> tempParts = Inventory.lookupPart(searchField);
            if (tempParts.size() == 0) {
                int searchID = Integer.parseInt(searchField);
                Part searchP = Inventory.lookupPart(searchID);
                tempParts.add(searchP);
                if (searchP == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("WARNING DIALOG");
                    alert.setContentText("No parts found by ID");
                    alert.showAndWait();
                }
            }
            addproductaddTableView.setItems(tempParts);
        }
        catch (NumberFormatException e){
            addproductaddTableView.setItems(null);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING DIALOG");
            alert.setContentText("No parts found by name");
            alert.showAndWait();
        }
    }

    /**
     * This is the method to get the new id. ID count begins at 400.
     * @return the product ID.
     */

    private int getProductIDCount() {
        productID++;
        return productID;
    }

    /**
     * This function is used to configure the top all parts table view and the bottom associated parts table view, as well as to obtain the new product id and assign it to the uneditable id column.
     * @param url
     * @param resourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addproductaddTableView.setItems(Inventory.getAllParts());
        addproductaddPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        addproductaddPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addproductaddInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addproductaddPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        addproductdeletePartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        addproductdeletePartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addproductdeleteInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addproductdeletePriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productID = getProductIDCount();
        addproductIDlabel.setText(String.valueOf(productID));
    }


}
