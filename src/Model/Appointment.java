package Model;

import Utilities.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author andrew.daiza
 */
public class Appointment {

    String apptName;
    int apptId;
    int apptCustomerId;
    int apptUserId;
    String apptCustomer;
    String apptConsultant;
    String apptStart;
    String apptEnd;
    String apptDate;
    String apptType;

    public Appointment(int apptCustomerId, int apptId, String apptType, String apptStart, String apptEnd, String apptDate, int apptUserId, String apptName, String apptConsultant) {
        this.apptName = apptName;
        this.apptId = apptId;
        this.apptCustomerId = apptCustomerId;
        this.apptUserId = apptUserId;
        this.apptConsultant = apptConsultant;
        this.apptStart = apptStart;
        this.apptEnd = apptEnd;
        this.apptDate = apptDate;
        this.apptType = apptType;
    }

    public static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();

    public static ObservableList<Appointment> getAllAppointments() throws SQLException, Exception {

        allAppointments.clear();
        try {
            String selectStatement = "SELECT customer.customerId, appointment.userId, appointment.appointmentId, appointment.type, appointment.start, appointment.end, customer.customerName, user.userName FROM appointment, customer, user WHERE appointment.customerId = customer.customerId AND appointment.userId = user.userId";

            PreparedStatement ps1 = DBConnection.getConnection().prepareStatement(selectStatement);
            ps1.execute();

            ResultSet rs = ps1.executeQuery();

            while (rs.next()) {
                int customerId = rs.getInt("customerId");
                int userId = rs.getInt("userId");
                int appointmentId = rs.getInt("appointmentId");
                String type = rs.getString("type");
                Timestamp dbStart = rs.getTimestamp("start");
                Timestamp dbEnd = rs.getTimestamp("end");
                String customerName = rs.getString("customerName");
                String userName = rs.getString("userName");

                String dateTimeStringArray[] = timetoStringConversion(dbStart, dbEnd);

                Appointment appointment = new Appointment(customerId, appointmentId, type, dateTimeStringArray[0], dateTimeStringArray[1], dateTimeStringArray[2], userId, customerName, userName);

                Appointment.addAllAppointment(appointment);

            }
            return allAppointments;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ObservableList<Appointment> getMonthAppointments() {

        allAppointments.clear();

        LocalDate beginStamp = LocalDate.now();
        LocalDate endStamp = LocalDate.now().plusMonths(1);
        String beginStampString = beginStamp.toString();
        String endStampString = endStamp.toString();

        try {

            String selectStatement = "SELECT customer.customerId, appointment.userId, appointment.appointmentId, appointment.type, appointment.start, appointment.end, customer.customerName, user.userName FROM appointment, customer, user WHERE appointment.customerId = customer.customerId AND appointment.userId = user.userId AND start >=? AND start <=?";

            PreparedStatement ps1 = DBConnection.getConnection().prepareStatement(selectStatement);
            ps1.setString(1, beginStampString);
            ps1.setString(2, endStampString);
            ps1.execute();

            ResultSet rs = ps1.executeQuery();

            while (rs.next()) {
                int customerId = rs.getInt("customerId");
                int userId = rs.getInt("userId");
                int appointmentId = rs.getInt("appointmentId");
                String type = rs.getString("type"); // grabs VARCHAR rows
                Timestamp dbStart = rs.getTimestamp("start");
                Timestamp dbEnd = rs.getTimestamp("end");
                String customerName = rs.getString("customerName");
                String userName = rs.getString("userName");

                String dateTimeStringArray[] = timetoStringConversion(dbStart, dbEnd);

                Appointment appointment = new Appointment(customerId, appointmentId, type, dateTimeStringArray[0], dateTimeStringArray[1], dateTimeStringArray[2], userId, customerName, userName);

                Appointment.addAllAppointment(appointment);

            }
            return allAppointments;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ObservableList<Appointment> getWeekAppointments() {
        allAppointments.clear();

        try {
            LocalDate beginStamp = LocalDate.now();
            LocalDate endStamp = LocalDate.now().plusWeeks(1);
            String beginStampString = beginStamp.toString();
            String endStampString = endStamp.toString();

            String selectStatement = "SELECT customer.customerId, appointment.userId, appointment.appointmentId, appointment.type, appointment.start, appointment.end, customer.customerName, user.userName FROM appointment, customer, user WHERE appointment.customerId = customer.customerId AND appointment.userId = user.userId AND start >=? AND start <=?";

            PreparedStatement ps1 = DBConnection.getConnection().prepareStatement(selectStatement);
            ps1.setString(1, beginStampString);
            ps1.setString(2, endStampString);
            ps1.execute();

            ResultSet rs = ps1.executeQuery();

            while (rs.next()) {
                int customerId = rs.getInt("customerId");
                int userId = rs.getInt("userId");
                int appointmentId = rs.getInt("appointmentId");
                String type = rs.getString("type");
                Timestamp dbStart = rs.getTimestamp("start");
                Timestamp dbEnd = rs.getTimestamp("end");
                String customerName = rs.getString("customerName");
                String userName = rs.getString("userName");

                String dateTimeStringArray[] = timetoStringConversion(dbStart, dbEnd);

                Appointment appointment = new Appointment(customerId, appointmentId, type, dateTimeStringArray[0], dateTimeStringArray[1], dateTimeStringArray[2], userId, customerName, userName);

                Appointment.addAllAppointment(appointment);

            }
            return allAppointments;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String[] timetoStringConversion(Timestamp dbStart, Timestamp dbEnd) {
        LocalDateTime ldtStart = dbStart.toLocalDateTime();
        LocalDateTime ldtEnd = dbEnd.toLocalDateTime();

        System.out.println("Converting from UTC");

        System.out.println(ldtStart);

        ZonedDateTime utcStartZoneDateTime = ZonedDateTime.of(ldtStart, ZoneId.of("UTC"));
        ZonedDateTime utcEndZoneDateTime = ZonedDateTime.of(ldtEnd, ZoneId.of("UTC"));

        ZonedDateTime startZoneDateTime = utcStartZoneDateTime.withZoneSameInstant(ZoneId.systemDefault());
        ZonedDateTime endZoneDateTime = utcEndZoneDateTime.withZoneSameInstant(ZoneId.systemDefault());

        System.out.println(startZoneDateTime.toLocalDateTime());

        System.out.println("---------");

        DateTimeFormatter customTimeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter customDateFormatter = DateTimeFormatter.ofPattern("MM/dd/YYYY");
        String start = customTimeFormatter.format(startZoneDateTime.toLocalDateTime());
        String end = customTimeFormatter.format(endZoneDateTime.toLocalDateTime());
        String date = customDateFormatter.format(startZoneDateTime.toLocalDateTime());
        String dateTimeStringArray[] = {start, end, date};

        return dateTimeStringArray;
    }

    public static String[] StringtoTimeConversion(LocalDate date, String start, String end) {

        String[] startHourMinSplit = start.split(":");
        String[] endHourMinSplit = end.split(":");

        LocalDateTime startLocalDateTime = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), Integer.parseInt(startHourMinSplit[0]), Integer.parseInt(startHourMinSplit[1]));
        LocalDateTime endLocalDateTime = LocalDateTime.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth(), Integer.parseInt(endHourMinSplit[0]), Integer.parseInt(endHourMinSplit[1]));

        System.out.println("Converting to UTC");

        System.out.println("Start");
        System.out.println(startLocalDateTime);

        System.out.println("End");
        System.out.println(endLocalDateTime);

        ZonedDateTime startZoneDateTime = ZonedDateTime.of(startLocalDateTime, ZoneId.systemDefault());
        ZonedDateTime endZoneDateTime = ZonedDateTime.of(endLocalDateTime, ZoneId.systemDefault());

        ZonedDateTime utcStartZoneDateTime = startZoneDateTime.withZoneSameInstant(ZoneId.of("UTC"));
        ZonedDateTime utcEndZoneDateTime = endZoneDateTime.withZoneSameInstant(ZoneId.of("UTC"));

        System.out.println("Start");
        System.out.println(utcStartZoneDateTime.toLocalDateTime());
        System.out.println("End");
        System.out.println(utcEndZoneDateTime.toLocalDateTime());

        System.out.println("------------");

        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String startTimeUTC = customFormatter.format(utcStartZoneDateTime.toLocalDateTime());
        String endTimeUTC = customFormatter.format(utcEndZoneDateTime.toLocalDateTime());

        String dateTimeFormatArray[] = {startTimeUTC, endTimeUTC};
        return dateTimeFormatArray;
    }

    public int getApptId() {
        return apptId;
    }

    public void setApptId(int apptId) {
        this.apptId = apptId;
    }

    public void setApptCustomerId(int apptCustomerId) {
        this.apptCustomerId = apptCustomerId;
    }

    public int getApptCustomerId() {
        return apptCustomerId;
    }

    public static void addAllAppointment(Appointment appointment) {
        allAppointments.add(appointment);
    }

    public Appointment() {
    }

    public static void deleteAppointment(Appointment selectedAppointment) {
        allAppointments.remove(selectedAppointment);
    }

    public int getApptUserId() {
        return apptUserId;
    }

    public void setApptUserId(int apptUserId) {
        this.apptUserId = apptUserId;
    }

    public String getApptName() {
        return apptName;
    }

    public String getApptCustomer() {
        return apptCustomer;
    }

    public String getApptConsultant() {
        return apptConsultant;
    }

    public String getApptStart() {
        return apptStart;
    }

    public String getApptEnd() {
        return apptEnd;
    }

    public String getApptDate() {
        return apptDate;
    }

    public String getApptDateTime() {
        return apptDate;
    }

    public String getApptType() {
        return apptType;
    }

    public void setApptName(String apptName) {
        this.apptName = apptName;
    }

    public void setApptCustomer(String apptCustomer) {
        this.apptCustomer = apptCustomer;
    }

    public void setApptConsultant(String apptConsultant) {
        this.apptConsultant = apptConsultant;
    }

    public void setApptStart(String apptStart) {
        this.apptStart = apptStart;
    }

    public void setApptEnd(String apptEnd) {
        this.apptEnd = apptEnd;
    }

    public void setApptDate(String apptDate) {
        this.apptDate = apptDate;
    }

    public void setApptType(String apptType) {
        this.apptType = apptType;
    }

}
