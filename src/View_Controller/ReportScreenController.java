package View_Controller;

import Model.Appointment;
import Model.AppointmentDatabase;
import Model.Report;
import Utilities.DBConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author devve
 */
public class ReportScreenController implements Initializable {

    public static ObservableList<String> reportsTableList = FXCollections.observableArrayList();
    public ObservableList<String> reportType = FXCollections.observableArrayList();

    Stage stage;
    Parent scene;

    @FXML
    private Button mainMenuButton;

    @FXML
    private RadioButton typeMonth;

    @FXML
    private ToggleGroup reportsGroup;

    @FXML
    private RadioButton consultantSchedule;

    @FXML
    private RadioButton reportChoice;

    @FXML
    private TextArea reportsTxt;

    @FXML
    private ComboBox<String> consultantSelect;

    @FXML
    void populateReports(ActionEvent event) throws Exception {
        populateAllReportsMethod();
    }

    @FXML
    void populateReportsClick(MouseEvent event) throws Exception {
        populateAllReportsMethod();
    }

    private void populateAllReportsMethod() throws Exception {

        if (reportsGroup.getSelectedToggle().equals(typeMonth)) {
            reportsTxt.clear();
            reportsTableList.clear();
            getReportTypeMonthList();

        }

        if (reportsGroup.getSelectedToggle().equals(consultantSchedule)) {
            reportsTxt.clear();
            reportsTableList.clear();
            int consultantId = consultantSelect.getSelectionModel().getSelectedIndex() + 1;
            getReportConsultantSchedule(consultantId);

        }

        if (reportsGroup.getSelectedToggle().equals(reportChoice)) {
            reportsTxt.clear();
            reportsTableList.clear();
            int consultantId = consultantSelect.getSelectionModel().getSelectedIndex() + 1;
            getReportConsultantAppointmentAmount(consultantId);

        }
    }

    public void getReportTypeMonthList() throws SQLException, Exception {

        // REPORT
        //   Number of scheduled appointments for this month by Appointment Type
        try {

            LocalDate beginStamp = LocalDate.now();
            LocalDate endStamp = LocalDate.now().plusMonths(1);
            String beginStampString = beginStamp.toString();
            String endStampString = endStamp.toString();

            String selectStatement = "SELECT type, COUNT(*) as NUM FROM appointment WHERE start>=? AND end<? GROUP BY type";

            PreparedStatement ps1 = DBConnection.getConnection().prepareStatement(selectStatement);

            ps1.setString(1, beginStampString);
            ps1.setString(2, endStampString);
            ps1.execute();

            ResultSet rs = ps1.executeQuery();

            StringBuilder newBuilder = new StringBuilder();

            newBuilder.append(String.format("%20s%n", "Appointment Types This Month"));
            newBuilder.append(String.format("%s %n", " "));
            newBuilder.append(String.format("%-16s %-20s%n", "Type", "Number"));
            newBuilder.append(String.format("%-20s %-20s%n", "-----", "-----"));

            while (rs.next()) {

                String type = rs.getString("type");
                String num = rs.getString("NUM");

                Report.monthTypeReport.add(type);
                Report.monthTypeReport.add(num);

                newBuilder.append(String.format("%-20s %-20s%n", type, num));
            }

            reportsTxt.setText(newBuilder.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getReportConsultantSchedule(int userId) throws SQLException, Exception {

        // REPORT
        //  Appointments list for each consultant, so you can track what appointments they have, what time/date and what type
        try {

            String selectStatement = "SELECT type, start, end FROM appointment WHERE userId=?";

            PreparedStatement ps1 = DBConnection.getConnection().prepareStatement(selectStatement);

            ps1.setInt(1, userId);
            ps1.execute();

            ResultSet rs = ps1.executeQuery();

            StringBuilder newBuilder = new StringBuilder();
            newBuilder.append(String.format("%20s%n", "Schedule by Consultant"));
            newBuilder.append(String.format("%-30s %-28s %-28s%n", "Type", "Start", "End"));
            newBuilder.append(String.format("%-28s %-30s %-30s%n", "------", "------", "------"));

            while (rs.next()) {

                String type = rs.getString("type");
                String start = rs.getString("start");
                String end = rs.getString("end");

                newBuilder.append(String.format("%s %n", " "));

                newBuilder.append(String.format("%-10s %-10s %-1s %-10s", type, start, "-", end));

            }

            reportsTxt.setText(newBuilder.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getReportConsultantAppointmentAmount(int userId) throws SQLException, Exception {

        // Number of appointments for each consultant
        try {

            String selectStatement = "SELECT COUNT(*) as NUM FROM appointment WHERE userId=?";

            PreparedStatement ps1 = DBConnection.getConnection().prepareStatement(selectStatement);

            ps1.setInt(1, userId);
            ps1.execute();

            ResultSet rs = ps1.executeQuery();

            StringBuilder newBuilder = new StringBuilder();
            newBuilder.append(String.format("%20s%n", "Number of Appointments for Consultant"));
            newBuilder.append(String.format("%-28s%n", "Type", "Start", "End"));
            newBuilder.append(String.format("%-30s%n", "------"));

            while (rs.next()) {

                String num = rs.getString("NUM");

                newBuilder.append(String.format("%-10s%n", num));
            }
            reportsTxt.setText(newBuilder.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        reportsTableList.clear();
        try {
            populateAllReportsMethod();
            AppointmentDatabase.getConsultantList();
            consultantSelect.setItems(AppointmentDatabase.allConsultants);
        } catch (Exception ex) {
            Logger.getLogger(AppointmentInterfaceScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void mainMenu(MouseEvent event) throws IOException {

        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

}
