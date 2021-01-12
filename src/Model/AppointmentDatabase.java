package Model;

import Utilities.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author andrew.daiza
 */
public class AppointmentDatabase {

    public static int addressId;
    public static String reportTypeMonth;

    public static ObservableList<String> customerList = FXCollections.observableArrayList();
    public static ObservableList<String> allConsultants = FXCollections.observableArrayList();
    public static ObservableList<Integer> allCustomerIds = FXCollections.observableArrayList();

    public static void InsertIntoAppointmentDatabase(int customerId, String customerName, int consultantId, String consultantName, int typeId, String typeName, String startTimeUTC, String endTimeUTC) throws SQLException, Exception {

        try {
            Connection conn = DBConnection.getConnection();

            String title = "Meeting";
            String description = "Meeting";
            String location = "Office";
            String lastUpdateBy = User.userLoggedIn.get(0);
            String url = "0";
            String createdBy = User.userLoggedIn.get(0);

            String insertStatementAddress = "INSERT INTO appointment SET customerId=?, title=?, userId=?, type=?, url=?, start=?, end=?, description=?, location=?, contact=?, createDate=NOW(), createdBy=?, lastUpdate=NOW(), lastUpdateBy=?";
            PreparedStatement ps1 = conn.prepareStatement(insertStatementAddress);

            ps1.setInt(1, customerId);
            ps1.setString(2, title);
            ps1.setInt(3, consultantId);
            ps1.setString(4, typeName);
            ps1.setString(5, url);
            ps1.setString(6, startTimeUTC);
            ps1.setString(7, endTimeUTC);
            ps1.setString(8, description);
            ps1.setString(9, location);
            ps1.setString(10, consultantName);
            ps1.setString(11, createdBy);
            ps1.setString(12, lastUpdateBy);

            ps1.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void UpdateAppointmentDatabase(int appointmentId, String customerName, int consultantId, String consultantName, int typeId, String typeName, String startTimeUTC, String endTimeUTC) throws SQLException, Exception {

        Connection conn = DBConnection.getConnection();

        String updateStatement1 = "UPDATE appointment SET userId=?, type=?, contact=?, start=?, end=? WHERE appointmentId=?";
        PreparedStatement ps1 = conn.prepareStatement(updateStatement1);

        ps1.setInt(1, consultantId);
        ps1.setString(2, typeName);
        ps1.setString(3, consultantName);
        ps1.setString(4, startTimeUTC);
        ps1.setString(5, endTimeUTC);
        ps1.setInt(6, appointmentId);

        ps1.execute();

    }

    public static void DeleteAppointmentDatabase(int appointmentId) throws SQLException, Exception {

        try {

            String deleteStatement1 = "DELETE FROM appointment WHERE appointmentId = ?";
            PreparedStatement ps1 = DBConnection.getConnection().prepareStatement(deleteStatement1);

            ps1.setInt(1, appointmentId);
            ps1.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getCustomerList() throws SQLException, Exception {

        try {

            String selectStatement = "SELECT customerName, customerId FROM customer WHERE customerId > 0";

            PreparedStatement ps1 = DBConnection.getConnection().prepareStatement(selectStatement);
            ps1.execute();

            ResultSet rs = ps1.executeQuery();

            while (rs.next()) {

                String customerName = rs.getString("customerName");
                int customerId = rs.getInt("customerId");
                customerList.add(customerName);
                allCustomerIds.add(customerId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void getConsultantList() throws SQLException, Exception {

        allConsultants.clear();
        try {

            String selectStatement = "SELECT userName FROM user WHERE userId > 0";

            PreparedStatement ps1 = DBConnection.getConnection().prepareStatement(selectStatement);
            ps1.execute();

            ResultSet rs = ps1.executeQuery();

            while (rs.next()) {

                String consultantName = rs.getString("userName");
                allConsultants.add(consultantName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean appointmentOverlap(String start, String end, int userId) throws SQLException, Exception {

        try {

            String selectStatement = "SELECT userId, start, end, appointmentId FROM appointment WHERE ((start BETWEEN ? AND ?) OR (end BETWEEN ? AND ?) OR (? and ? BETWEEN start AND end)) AND userId=?";

            PreparedStatement ps1 = DBConnection.getConnection().prepareStatement(selectStatement);

            ps1.setString(1, start);
            ps1.setString(2, end);
            ps1.setString(3, start);
            ps1.setString(4, end);
            ps1.setString(5, start);
            ps1.setString(6, end);
            ps1.setInt(7, userId);

            ps1.execute();

            ResultSet rs = ps1.executeQuery();

            if (!(rs.next())) {
                return false;
            } else {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean appointmentOverlapEdit(String start, String end, int userId, int appointmentId) throws SQLException, Exception {

        try {

            String selectStatement = "SELECT userId, start, end, appointmentId FROM appointment WHERE ((start BETWEEN ? AND ?) OR (end BETWEEN ? AND ?) OR (? and ? BETWEEN start AND end)) AND userId=? AND NOT appointmentId=?";

            PreparedStatement ps1 = DBConnection.getConnection().prepareStatement(selectStatement);

            ps1.setString(1, start);
            ps1.setString(2, end);
            ps1.setString(3, start);
            ps1.setString(4, end);
            ps1.setString(5, start);
            ps1.setString(6, end);
            ps1.setInt(7, userId);
            ps1.setInt(8, appointmentId);

            ps1.execute();

            ResultSet rs = ps1.executeQuery();

            if (!(rs.next())) {
                return false;
            } else {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean getAppointmentsUser15min() throws SQLException, Exception {

        try {

            LocalDateTime beginStamp = LocalDateTime.now(ZoneOffset.UTC);
            LocalDateTime beginStamp15 = beginStamp.plusMinutes(15);

            DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String startTimeUTC = customFormatter.format(beginStamp);
            String startTimeUTC2 = customFormatter.format(beginStamp15);
            String user = User.userLoggedIn.get(0);
            System.out.println(user);

            String selectStatement = "SELECT userId, start FROM appointment WHERE start BETWEEN ? AND ? AND contact=?";

            PreparedStatement ps1 = DBConnection.getConnection().prepareStatement(selectStatement);

            ps1.setString(1, startTimeUTC);
            ps1.setString(2, startTimeUTC2);
            ps1.setString(3, user);
            ps1.execute();

            ResultSet rs = ps1.executeQuery();

            if (!(rs.next())) {
                return false;
            } else {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
