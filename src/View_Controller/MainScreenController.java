package View_Controller;

import Model.AppointmentDatabase;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author andrew.daiza
 */
public class MainScreenController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private Button appointmentsScreenButton;
    @FXML
    private Button customerRecordsScreenButton;
    @FXML
    private Button reportsScreenButton;
    @FXML
    private Button signOutButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    private void appointmentsScreen(MouseEvent event) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("AppointmentInterfaceScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    private void customerRecordsScreen(MouseEvent event) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("CustomerRecordsScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    private void reportsScreen(MouseEvent event) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("ReportScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    private void signOut(MouseEvent event) throws IOException {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Alert!");
        alert.setHeaderText("Are you sure you want to sign out?");

        // LAMBDA EXPRESSION
        // To add efficiency to Alert and change to next scene
        alert.showAndWait().ifPresent((response -> {
            if (response == ButtonType.OK) {
                try {
                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("LoginScreen.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();

                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }));
    }
}
