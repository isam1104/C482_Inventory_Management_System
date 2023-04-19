package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
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
 * This class creates the Main Screen controller.
 * @author Isam Elder
 */

public class MainScreen implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private Button btnSearchParts;

    @FXML
    private TableView<Part> mainmenuPartsTableView;

    @FXML
    private TableColumn<Part, Integer> mainmenupartsInventoryCol;

    @FXML
    private TableColumn<Part, Integer> mainmenupartsPartIDCol;

    @FXML
    private TableColumn<Part, String> mainmenupartsPartNameCol;

    @FXML
    private TableColumn<Part, Double> mainmenupartsPriceCol;

    @FXML
    private TextField mainmenupartsSearchtxt;

    @FXML
    private TableColumn<Product, Integer> mainmenuproductsInventoryCol;

    @FXML
    private TableColumn<Product, Double> mainmenuproductsPriceCol;

    @FXML
    private TableColumn<Product, String> mainmenuproductsProductCol;

    @FXML
    private TableColumn<Product, Integer> mainmenuproductsProductIDCol;

    @FXML
    private TextField mainmenuproductsSearchtxt;

    @FXML
    private TableView<Product> mainmenuproductsTableView;

    /**
     * This method loads the Add Part screen when the user clicks the Add button in the Parts section
     * @param event user clicks the Add button in the Parts section.
     * @throws IOException
     */

    @FXML
    void onActionAddParts(ActionEvent event) throws IOException {

        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AddPart.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * This method loads the Add Part screen when the user clicks the Add button in the Products section
     * @param event user clicks the Add button in the Products section.
     * @throws IOException
     */
    @FXML
    void onActionAddProducts(ActionEvent event) throws IOException {

        stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/AddProduct.fxml")));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     * The method used to delete a part from the inventory when the user clicks the Delete button in Parts section.
     * @param event user clicks the Delete button in the Parts section.
     * @throws IOException
     */

    @FXML
    void onActionDeleteParts(ActionEvent event) throws IOException {

        Part part = (Part) mainmenuPartsTableView.getSelectionModel().getSelectedItem();
        if (mainmenuPartsTableView.getSelectionModel().getSelectedItem() == null){

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING DIALOG");
            alert.setContentText("ERROR: NO PART SELECTED");
            alert.showAndWait();

        }else {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this part?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK){

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainScreen.fxml")));
                stage.setScene(new Scene(scene));
                stage.show();
                Inventory.deletePart(part);

            } else {
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainScreen.fxml")));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
    }

    /**
     * The method used to delete a part from the inventory when the user clicks the Delete button in Prouducts section.
     * @param event user clicks the Delete button in the Products section.
     * @throws IOException
     */
    @FXML
    void onActionDeleteProducts(ActionEvent event) throws IOException {
        Product product = (Product) mainmenuproductsTableView.getSelectionModel().getSelectedItem();
        if (mainmenuproductsTableView.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING DIALOG");
            alert.setContentText("ERROR: NO PRODUCT SELECTED");
            alert.showAndWait();
        } else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this part?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/MainScreen.fxml")));
                stage.setScene(new Scene(scene));
                stage.show();
                if (!product.getAllAssociatedParts().isEmpty()){
                    Alert asAlert = new Alert(Alert.AlertType.WARNING);
                    asAlert.setTitle("WARNING DIALOG");
                    asAlert.setContentText("The product has atleast 1 associated part");
                    asAlert.showAndWait();
                } else {
                    Inventory.deleteProduct(product);
                }
            }
        }
    }

    /**
     * This method loads the Modify Product screen when the user clicks the Modify button in the Parts section.
     * @param event user clicks the Modify button in the Product section
     * @throws IOException
     */
    @FXML
    void onActionModifyParts(ActionEvent event) throws IOException {
        if (mainmenuPartsTableView.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("ERROR: No part selected");
            alert.showAndWait();
        } else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyPart.fxml"));
            loader.load();

            ModifyPart MPController = loader.getController();
            MPController.sendPart(mainmenuPartsTableView.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * This method loads the Modify Product screen when the user clicks the Modify button in the Product section.
     * @param event user clicks the Modify button in the Product section
     * @throws IOException
     */
    @FXML
    void onActionModifyProducts(ActionEvent event) throws IOException {

        if (mainmenuproductsTableView.getSelectionModel().getSelectedItem() == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING DIALOG");
            alert.setContentText("ERROR: No product selected");
            alert.showAndWait();
        } else {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyProduct.fxml"));
            loader.load();

            ModifyProduct MProdController = loader.getController();
            MProdController.sendProduct(mainmenuproductsTableView.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        }
    }

    /**
     * This method searches the parts inventory when the user clicks the Search button in the Parts section. It searches parts by exact part ID or by exact or partial part name. It parses the current parts in the inventory and returns an exact id match if one exists or it returns an exact or partial name match if one or more exist.
     * @param event user clicks the Search button in the Parts section
     */
    @FXML
    void onActionSearchParts(ActionEvent event) {

        String searchfield = mainmenupartsSearchtxt.getText();
        try {
            ObservableList<Part> tempParts = Inventory.lookupPart(searchfield);
            if (tempParts.size() == 0) {
                int searchID = Integer.parseInt(searchfield);
                Part searchP = Inventory.lookupPart(searchID);
                tempParts.add(searchP);
                if (searchP == null) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("WARNING DIALOG");
                    alert.setContentText("No parts found by ID");
                    alert.showAndWait();
                }
            }
            mainmenuPartsTableView.setItems(tempParts);
        } catch (NumberFormatException e) {
            mainmenuPartsTableView.setItems(null);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING DIALOG");
            alert.setContentText("No parts found by name");
            alert.showAndWait();
        }
    }

    /**
     * This method searches the parts inventory when the user clicks the Search button in the Products section. It searches products by exact product ID or by exact or partial product name. It parses the current products in the inventory and returns an exact id match if one exists or it returns an exact or partial name match if one or more exist.
     * @param event
     */

    @FXML
    void onActionSearchProducts(ActionEvent event) {
        String searchProductsField = mainmenuproductsSearchtxt.getText();
        try {
            ObservableList<Product> tempProducts = Inventory.lookupProduct(searchProductsField);
            if (tempProducts.size() == 0){
                int searchProductID = Integer.parseInt(searchProductsField);
                Product searchProd = Inventory.lookupProduct(searchProductID);
                tempProducts.add(searchProd);
                if (searchProd == null){
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("WARNING DIALOG");
                    alert.setContentText("No products found by ID");
                    alert.showAndWait();
                }
            }
            mainmenuproductsTableView.setItems(tempProducts);
        }
        catch (NumberFormatException e){
            mainmenuproductsTableView.setItems(null);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING DIALOG");
            alert.setContentText("No products found by name");
            alert.showAndWait();
        }
    }

    /**
     * The method that stops the progran from running.
     * @param event user clicks on the Exit button.
     */
    @FXML
    void onActionExit(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            System.exit(0);
        }
    }

    @FXML
    void mainScreenPartSearchOnKeyTyped(KeyEvent event) {
        if (mainmenupartsSearchtxt.getText().isEmpty()) {
            mainmenuPartsTableView.setItems(Inventory.getAllParts());
        }
    }

    /**
     * This is the method to refresh the products table with all products in the inventory when the user deletes all text from the products search field.
     * @param event user deletes all text from the products search field
     */
    @FXML
    void mainScreenProductSearchOnKeyTyped(KeyEvent event) {
        if (mainmenuproductsSearchtxt.getText().isEmpty()) {
            mainmenuproductsTableView.setItems(Inventory.getAllProducts());
        }
    }

    /**
     * This is the method to set the all products table view and the all parts table view.
     * @param url
     * @param resourceBundle
     */

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mainmenuPartsTableView.setItems(Inventory.getAllParts());

        mainmenupartsPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        mainmenupartsPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        mainmenupartsInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        mainmenupartsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        mainmenuproductsTableView.setItems(Inventory.getAllProducts());

        mainmenuproductsProductIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        mainmenuproductsProductCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        mainmenuproductsInventoryCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        mainmenuproductsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }
}