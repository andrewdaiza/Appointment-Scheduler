package View_Controller;

import Model.Customer;
import Model.CustomerDatabase;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author andrew.daiza
 */
public class CustomerRecordsScreenController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private TableView<Customer> customerRecordTable;
    @FXML
    private TableColumn<Customer, Integer> customerIdColumn;
    @FXML
    private TableColumn<Customer, String> customerNameColumn;
    @FXML
    private TableColumn<Customer, String> customerAddressColumn;
    @FXML
    private TableColumn<Customer, String> customerCityColumn;
    @FXML
    private TableColumn<Customer, String> customerPhoneColumn;
    @FXML
    private Button addRecordButton;
    @FXML
    private Button modifyRecordButton;
    @FXML
    private Button deleteRecordButton;
    @FXML
    private Button mainMenuButton;

    @FXML
    private TextField customerSearchBox;

    @FXML
    private Button customerSearchButton;

    @FXML
    private void addRecord(MouseEvent event) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("AddCustomerRecordScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    private void modifyRecord(MouseEvent event) throws IOException {

        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("EditCustomerRecordScreen.fxml"));
            loader.load();

            EditCustomerRecordScreenController MPSController = loader.getController();

            MPSController.sendCustomerRecord(customerRecordTable.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (NullPointerException e) {
            System.out.println("Please select a Customer to modify");
        }

    }

    @FXML
    void deleteRecord(MouseEvent event) throws Exception {

        try {

            Customer selectedCustomer = customerRecordTable.getSelectionModel().getSelectedItem();
            int customerId = selectedCustomer.getCustomerId();

            int addressId = selectedCustomer.getAddressId();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Confirm Delete");
            alert.setContentText("Are you sure you want to delete this customer?");

            // LAMBDA EXPRESSION
            // To add efficiency to Alert for delete
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    Customer.deleteCustomer(selectedCustomer);

                    try {
                        CustomerDatabase.DeleteCustomerDatabase(customerId, addressId);
                    } catch (Exception ex) {
                        Logger.getLogger(CustomerRecordsScreenController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }));

        } catch (NullPointerException e) {
            System.out.println("Please select a Customer to delete");
        }
    }

    @FXML
    void mainMenu(MouseEvent event) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    @FXML
    void searchCustomer(MouseEvent event) throws Exception {
        // Search with Int
        try {
            customerRecordTable.setItems(searchIdCustomer(Integer.parseInt(customerSearchBox.getText())));
        } catch (NumberFormatException e) {
            customerRecordTable.setItems(filterCustomer(customerSearchBox.getText()));

        }

    }

    public ObservableList<Customer> searchIdCustomer(int id) throws Exception {
        if (!(Customer.lookupCustomerId(id).isEmpty())) {
            Customer.lookupCustomerId(id).clear();
        }

        int index = -1;
        for (Customer customer : Customer.getAllCustomers()) {
            index++;

            if (customer.getCustomerId() == id) {
                Customer.lookupCustomerId(id).add(customer);
            }
        }

        if (Customer.lookupCustomerId(id).isEmpty()) {
            // return all parts of filtered list
            return Customer.getAllCustomers();
        } else {
            return Customer.lookupCustomerId(id);
        }
    }

    public ObservableList<Customer> filterCustomer(String name) throws Exception {
        // If filter is not empty, clear 
        if (!(Customer.lookupCustomer(name).isEmpty())) {
            Customer.lookupCustomer(name).clear();
        }
        for (Customer customer : Customer.getAllCustomers()) {
            // If Name of Customer contains Paramater Name
            // Add Part to filtered Array
            if (customer.getCustomerName().contains(name)) {
                Customer.lookupCustomer(name).add(customer);
            }
        }
        if (Customer.lookupCustomer(name).isEmpty()) {
            // return all parts of filtered list
            return Customer.getAllCustomers();
        } else {
            // 
            return Customer.lookupCustomer(name);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            customerRecordTable.setItems(Customer.getAllCustomers());
        } catch (Exception ex) {
            Logger.getLogger(CustomerRecordsScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }

        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerNameColumn.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        customerAddressColumn.setCellValueFactory(new PropertyValueFactory<>("customerAddress"));
        customerCityColumn.setCellValueFactory(new PropertyValueFactory<>("cityName"));
        customerPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("customerPhone"));
    }

}
