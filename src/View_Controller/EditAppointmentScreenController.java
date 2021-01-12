package View_Controller;

import Model.Appointment;
import Model.AppointmentDatabase;
import Model.CustomerDatabase;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author andrew.daiza
 */
public class EditAppointmentScreenController implements Initializable {
    
    Stage stage;
    Parent scene;
    
    Appointment selectedAppt;

 
    @FXML
    private ComboBox<String> customerSelect;

    @FXML
    private ComboBox<String> consultantSelect;

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
    
    private int appointmentId;
    
    private ObservableList<String> startHours = FXCollections.observableArrayList();
    private ObservableList<String> endHours = FXCollections.observableArrayList();

    @FXML
    private void saveAppt(MouseEvent event) throws IOException, Exception {

            try {
            LocalDate date = dateSelect.getValue();
            String start = startTimeSelect.getValue();
            String end = endTimeSelect.getValue();
            int consultantId = consultantSelect.getSelectionModel().getSelectedIndex() + 1;
            
            String[] startHourMinSplit = start.split(":");
            String[] endHourMinSplit = end.split(":");
            
            String timeArray[] = Appointment.StringtoTimeConversion(date,start,end);      
            int typeId = typeSelect.getSelectionModel().getSelectedIndex();
        
            if (((Integer.parseInt(startHourMinSplit[0])) < 8)||((Integer.parseInt(endHourMinSplit[0])) > 16)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Missing Fields!");
                    alert.setHeaderText("This time is outside our 8AM to 4PM work hours" );
                    alert.showAndWait();
            } 
            else if (AppointmentDatabase.appointmentOverlapEdit(timeArray[0], timeArray[1], consultantId, appointmentId)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Conflicting Appointment!");
                        alert.setHeaderText("You already have an appointment with someone at this time!" );
                        alert.showAndWait();
            }
        
            else {

            String customerName = customerSelect.getValue();
            String consultantName = consultantSelect.getValue();
            String typeName = typeSelect.getValue();

            AppointmentDatabase.UpdateAppointmentDatabase(appointmentId,customerName,consultantId,consultantName,typeId,typeName,timeArray[0],timeArray[1]);
                                       

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("AppointmentInterfaceScreen.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
            }
        }
             
            catch(NullPointerException e) {
            e.printStackTrace();
        }
    }
    
        

    @FXML
    private void cancelAppt(MouseEvent event) throws IOException {
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("AppointmentInterfaceScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }
    
     public void sendAppointment(Appointment appointment){
        
        appointmentId = appointment.getApptId();
        customerSelect.getSelectionModel().select(appointment.getApptCustomer());
        consultantSelect.getSelectionModel().select(appointment.getApptConsultant());
        
        typeSelect.getSelectionModel().select(appointment.getApptType());
        startTimeSelect.getSelectionModel().select(appointment.getApptStart());
        endTimeSelect.getSelectionModel().select(appointment.getApptEnd());
        
        String appt = appointment.getApptDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate localDate = LocalDate.parse(appt, formatter);
   
        dateSelect.setValue(localDate);

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
       
        startHours.addAll("08:00","08:30","09:00","09:30","10:00","10:30","11:00","11:30","12:00","12:30","13:00","13:30","14:00","14:30","15:00","15:30");
        startTimeSelect.setItems(startHours);
          endHours.addAll("08:30","09:00","09:30","10:00","10:30","11:00","11:30","12:00","12:30","13:00","13:30","14:00","14:30","15:00","15:30","16:00");
        endTimeSelect.setItems(endHours);
        
        
    }    
    
}
