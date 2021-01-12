package View_Controller;

import Model.Appointment;
import Model.AppointmentDatabase;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author andrew.daiza
 */
public class AddAppointmentScreenController implements Initializable {

    Stage stage;
    Parent scene;

    @FXML
    private ChoiceBox<String> customerSelect;

    @FXML
    private ChoiceBox<String> consultantSelect;

    @FXML
    private ChoiceBox<String> typeSelect;

    @FXML
    private ChoiceBox<String> startTimeSelect;

    @FXML
    private ChoiceBox<String> endTimeSelect;

    @FXML
    private DatePicker dateSelect;

    @FXML
    private Button saveApptButton;

    @FXML
    private Button cancelApptButton;

    private ObservableList<String> startHours = FXCollections.observableArrayList();
    private ObservableList<String> startMinutes = FXCollections.observableArrayList();
    private ObservableList<String> endHours = FXCollections.observableArrayList();
    private ObservableList<String> endMinutes = FXCollections.observableArrayList();

    @FXML
    private void saveAppt(MouseEvent event) throws IOException, Exception {

        try {

            String a = "";
            String b = "";
            String c = "";
            String d = "";
            String f = "";
            String g = "";
            String h = "";

            if (customerSelect.getValue() == null) {
                a = "Customer";
            }
            if (consultantSelect.getValue() == null) {
                b = "Consultant";
            }
            if (typeSelect.getValue() == null) {
                c = "Type";
            }
            if (startTimeSelect.getValue() == null) {
                f = "Start Time";
            }
            if (endTimeSelect.getValue() == null) {
                g = "End Time";
            }
            if (dateSelect.getValue() == null) {
                h = "Date";
            }
            if ((customerSelect.getValue() == null) || (consultantSelect.getValue() == null) || (typeSelect.getValue() == null) || (startTimeSelect.getValue() == null) || (endTimeSelect.getValue() == null) || (dateSelect.getValue() == null)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Missing Fields!");
                alert.setHeaderText("Please select the following: " + a + " " + b + " " + c + " " + d + " " + f + " " + g + " " + h);
                alert.showAndWait();
            }

            LocalDate date = dateSelect.getValue();
            String start = startTimeSelect.getValue();
            String end = endTimeSelect.getValue();
            int consultantId = consultantSelect.getSelectionModel().getSelectedIndex() + 1;

            String[] startHourMinSplit = start.split(":");
            String[] endHourMinSplit = end.split(":");

            String timeArray[] = Appointment.StringtoTimeConversion(date, start, end);

            int customerIdSelect = customerSelect.getSelectionModel().getSelectedIndex();
            int customerId = AppointmentDatabase.allCustomerIds.get(customerIdSelect);
            int typeId = typeSelect.getSelectionModel().getSelectedIndex();

            if (((Integer.parseInt(startHourMinSplit[0])) < 8) || ((Integer.parseInt(endHourMinSplit[0])) > 16)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Missing Fields!");
                alert.setHeaderText("This time is outside our 8AM to 4PM work hours");
                alert.showAndWait();
            } else if (AppointmentDatabase.appointmentOverlap(timeArray[0], timeArray[1], consultantId)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Conflicting Appointment!");
                alert.setHeaderText("You already have an appointment with someone at this time!");
                alert.showAndWait();
            } else {

                String customerName = customerSelect.getValue();
                String consultantName = consultantSelect.getValue();
                String typeName = typeSelect.getValue();

                AppointmentDatabase.InsertIntoAppointmentDatabase(customerId, customerName, consultantId, consultantName, typeId, typeName, timeArray[0], timeArray[1]);

                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("AppointmentInterfaceScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void cancelAppt(MouseEvent event) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("AppointmentInterfaceScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AppointmentDatabase.customerList.clear();
        AppointmentDatabase.allConsultants.clear();
        AppointmentDatabase.allCustomerIds.clear();
        typeSelect.getItems().add("Informational");
        typeSelect.getItems().add("Consultation");
        typeSelect.getItems().add("Plan Purchase");
        typeSelect.getItems().add("Plan Upgrade");
        typeSelect.getItems().add("Technical Issues");

        try {
            AppointmentDatabase.getCustomerList();
            customerSelect.setItems(AppointmentDatabase.customerList);
            AppointmentDatabase.getConsultantList();
            consultantSelect.setItems(AppointmentDatabase.allConsultants);
        } catch (Exception ex) {
            Logger.getLogger(AddAppointmentScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }

        startHours.addAll("08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30");
        startTimeSelect.setItems(startHours);
        endHours.addAll("08:30", "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00");
        endTimeSelect.setItems(endHours);
    }
}
