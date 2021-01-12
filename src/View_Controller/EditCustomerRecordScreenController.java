/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View_Controller;

import Model.Customer;
import Model.CustomerDatabase;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author devve
 */
public class EditCustomerRecordScreenController implements Initializable {

    Stage stage;
    Parent scene;

    private int id;

    @FXML
    private TextField idTxt;
    @FXML
    private TextField nameTxt;
    @FXML
    private TextField addressTxt;
    @FXML
    private ChoiceBox<String> cityChoice;
    @FXML
    private TextField phoneTxt;
    @FXML
    private Button saveCustomerButton;
    @FXML
    private Button cancelCustomerButton;

    public void sendCustomerRecord(Customer customer) {

        idTxt.setText(String.valueOf(customer.getCustomerId()));
        nameTxt.setText(customer.getCustomerName());
        addressTxt.setText(customer.getCustomerAddress());
        cityChoice.getSelectionModel().select(customer.getCityName());
        phoneTxt.setText(customer.getCustomerPhone());
    }

    @FXML
    private void saveCustomerButton(MouseEvent event) throws Exception {

        String b = "";
        String c = "";
        String d = "";
        String f = "";
        String g = "";

        if (nameTxt.getText().length() == 0 || nameTxt.getText().trim().isEmpty()) {
            b = "Name";
        }
        if (addressTxt.getText().length() == 0 || addressTxt.getText().trim().isEmpty()) {
            c = "Address";
        }
        if (cityChoice.getValue() == null) {
            f = "City";
        }
        if (phoneTxt.getText().length() == 0 || phoneTxt.getText().trim().isEmpty()) {
            g = "Phone Number";
        }

        if ((nameTxt.getText().length() == 0 || nameTxt.getText().trim().isEmpty()) || (addressTxt.getText().length() == 0 || addressTxt.getText().trim().isEmpty()) || (cityChoice.getValue() == null) || (phoneTxt.getText().length() == 0 || phoneTxt.getText().trim().isEmpty())) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Missing Fields!");
            alert.setHeaderText("Please Enter the following: " + b + " " + c + " " + d + " " + f + " " + g);
            alert.showAndWait();
        } else {
            try {

                int customerCity = cityChoice.getSelectionModel().getSelectedIndex() + 1;
                CustomerDatabase.UpdateCustomerDatabase(Integer.parseInt(idTxt.getText()), nameTxt.getText(), addressTxt.getText(), customerCity, phoneTxt.getText());

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("CustomerRecordsScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();

            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void cancelCustomerButton(MouseEvent event) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("CustomerRecordsScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cityChoice.getItems().add("Dallas");
        cityChoice.getItems().add("Los Angeles");
        cityChoice.getItems().add("Miami");
        cityChoice.getItems().add("London");
    }

}
