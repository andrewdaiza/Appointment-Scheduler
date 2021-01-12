package Model;

import Utilities.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author andrew.daiza
 */
public class CustomerDatabase {

    public static int addressId;

    public static void InsertIntoCustomerDatabase(String customerName, String customerAddress, int cityId, String cityName, String customerPhone) throws SQLException, Exception {

        try {
            Connection conn = DBConnection.getConnection();

            String active = "0";
            String createdBy = User.userLoggedIn.get(0);
            String lastUpdateBy = User.userLoggedIn.get(0);
            String address2 = "0";
            String postalCode = "0";

            String insertStatementAddress = "INSERT INTO address SET address=?, phone=?, cityId=?, address2=?, postalCode=?, createDate=NOW(), createdBy=?, lastUpdateBy=?";
            PreparedStatement ps1 = conn.prepareStatement(insertStatementAddress, Statement.RETURN_GENERATED_KEYS); // Getting prepared statement for controller

            ps1.setString(1, customerAddress);
            ps1.setString(2, customerPhone);
            ps1.setInt(3, cityId);
            ps1.setString(4, address2);
            ps1.setString(5, postalCode);
            ps1.setString(6, createdBy);
            ps1.setString(7, lastUpdateBy);

            ps1.execute();
            ResultSet rs1 = ps1.getGeneratedKeys();
            rs1.next();
            int addressId = rs1.getInt(1);

            String insertStatementCustomer = "INSERT INTO customer SET customerName=?, addressId=?, active=?, createDate=NOW(), createdBy=?, lastUpdateBy=?";
            PreparedStatement ps2 = conn.prepareStatement(insertStatementCustomer, Statement.RETURN_GENERATED_KEYS); // Getting prepared statement for controller    

            ps2.setString(1, customerName);
            ps2.setInt(2, addressId);
            ps2.setString(3, active);
            ps2.setString(4, createdBy);
            ps2.setString(5, lastUpdateBy);

            ps2.execute();
            ResultSet rs2 = ps2.getGeneratedKeys();
            rs2.next();
            int customerId = rs2.getInt(1);

            Customer customer = new Customer();
            customer.setCustomerId(customerId);
            customer.setCustomerName(customerName);
            customer.setCustomerAddress(customerAddress);
            customer.setCityName(cityName);
            customer.setCustomerPhone(customerPhone);

            Customer.addCustomer(customer);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void UpdateCustomerDatabase(int id, String customerName, String customerAddress, int customerCity, String customerPhone) throws SQLException, Exception {

        Connection conn = DBConnection.getConnection();

        String updateStatement1 = "UPDATE address SET address=?, cityId=?, phone=? WHERE addressId=?";
        PreparedStatement ps1 = conn.prepareStatement(updateStatement1);

        ps1.setString(1, customerAddress);
        ps1.setInt(2, customerCity);
        ps1.setString(3, customerPhone);
        ps1.setInt(4, id);

        ps1.execute();

        String updateStatement2 = "UPDATE customer SET customerName=?, addressId=? WHERE customerId=?";
        PreparedStatement ps2 = conn.prepareStatement(updateStatement2);

        ps2.setString(1, customerName);
        ps2.setInt(2, id);
        ps2.setInt(3, id);

        ps2.execute();

    }

    public static void DeleteCustomerDatabase(int customerId, int addressId) throws SQLException, Exception {

        Connection conn = DBConnection.getConnection();

        try {

            String deleteStatement3 = "DELETE FROM appointment WHERE customerId = ?";
            PreparedStatement ps3 = DBConnection.getConnection().prepareStatement(deleteStatement3);

            ps3.setInt(1, customerId);
            ps3.execute();

            String deleteStatement1 = "DELETE FROM customer WHERE customerId = ? AND addressId=?";
            PreparedStatement ps1 = conn.prepareStatement(deleteStatement1);

            ps1.setInt(1, customerId);
            ps1.setInt(2, addressId);
            ps1.execute();

            String deleteStatement2 = "DELETE FROM address WHERE addressId = ?";
            PreparedStatement ps2 = DBConnection.getConnection().prepareStatement(deleteStatement2);

            ps2.setInt(1, addressId);
            ps2.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static int getAddressId(int customerId) throws SQLException, Exception {

        try {
            String selectStatement = "SELECT customerId, address.addressId, FROM customer, address WHERE customer.addressId = address.addressId";

            PreparedStatement ps1 = DBConnection.getConnection().prepareStatement(selectStatement);
            ps1.execute();

            ResultSet rs = ps1.executeQuery();

            while (rs.next()) {
                addressId = rs.getInt("addressId");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return addressId;
    }
}
