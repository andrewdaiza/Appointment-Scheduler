package View_Controller;

import Model.Appointment;
import Model.AppointmentDatabase;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author devve
 */
public class AppointmentInterfaceScreenController implements Initializable {

    Stage stage;
    Parent scene;

    public static ObservableList<Appointment> appointmentsTableList = FXCollections.observableArrayList();

    @FXML
    private TableView<Appointment> allAppointmentsTable;

    @FXML
    private TableColumn<Appointment, Integer> customerIdColumn;

    @FXML
    private TableColumn<Appointment, Integer> userIdColumn;

    @FXML
    private TableColumn<Appointment, String> appointmentTypeColumn;

    @FXML
    private TableColumn<Appointment, String> appointmentDateColumn;

    @FXML
    private TableColumn<Appointment, String> startTimeColumn;

    @FXML
    private TableColumn<Appointment, String> endTimeColumn;

    @FXML
    private Button addAppointmentButton;

    @FXML
    private Button modifyAppointmentButton;

    @FXML
    private Button deleteAppointmentButton;

    @FXML
    private Button mainMenuButton;

    @FXML
    private RadioButton allAppointmentsRadio;

    @FXML
    private ToggleGroup appointmentsGroup;

    @FXML
    private RadioButton monthAppointmentsRadio;

    @FXML
    private RadioButton weekAppointmentsRadio;

    private void populateAllAppointmentsMethod() throws Exception {
        if (appointmentsGroup.getSelectedToggle().equals(monthAppointmentsRadio)) {
            appointmentsTableList.clear();
            appointmentsTableList = Appointment.getMonthAppointments();
            setAppointmentColumns();
        }
        if (appointmentsGroup.getSelectedToggle().equals(allAppointmentsRadio)) {
            appointmentsTableList.clear();
            appointmentsTableList = Appointment.getAllAppointments();
            setAppointmentColumns();
        }

        if (appointmentsGroup.getSelectedToggle().equals(weekAppointmentsRadio)) {
            appointmentsTableList.clear();
            appointmentsTableList = Appointment.getWeekAppointments();
            setAppointmentColumns();
        }

    }

    private void setAppointmentColumns() {
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("apptCustomerId"));
        userIdColumn.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("apptUserId"));
        appointmentTypeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("apptType"));
        appointmentDateColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("apptDate"));
        startTimeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("apptStart"));
        endTimeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("apptEnd"));

        allAppointmentsTable.getItems().setAll(appointmentsTableList);
    }

    @FXML
    void populateAppointments(ActionEvent event) throws Exception {
        populateAllAppointmentsMethod();
    }

    @FXML
    private void addAppointment(MouseEvent event) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("AddAppointmentScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @FXML
    private void modifyAppointment(MouseEvent event) throws IOException {

        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("EditAppointmentScreen.fxml"));
            loader.load();
            EditAppointmentScreenController MPSController = loader.getController();
            MPSController.sendAppointment(allAppointmentsTable.getSelectionModel().getSelectedItem());

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
        } catch (NullPointerException e) {
            System.out.println("Please select a Appointment to Modify");
        }
    }

    @FXML
    private void deleteAppointment(MouseEvent event) throws Exception {

        try {

            Appointment selectedAppointment = allAppointmentsTable.getSelectionModel().getSelectedItem();
            int appointmentId = selectedAppointment.getApptId();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText("Confirm Delete");
            alert.setContentText("Are you sure you want to delete this appointment?");

            // LAMBDA EXPRESSION
            // To speed up Alert close for delete function
            alert.showAndWait().ifPresent((response -> {
                if (response == ButtonType.OK) {
                    deleteAppointment(selectedAppointment);
                    try {
                        AppointmentDatabase.DeleteAppointmentDatabase(appointmentId);
                        populateAllAppointmentsMethod();
                    } catch (Exception ex) {
                        Logger.getLogger(AppointmentInterfaceScreenController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }));
        } catch (NullPointerException e) {
            System.out.println("Please select a Appointment to Delete");
        }

    }

    public static void deleteAppointment(Appointment selectedAppointment) {
        appointmentsTableList.remove(selectedAppointment);
    }

    @FXML
    void mainMenu(MouseEvent event) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            populateAllAppointmentsMethod();
        } catch (Exception ex) {
            Logger.getLogger(AppointmentInterfaceScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
