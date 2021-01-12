package Model;

import Utilities.DBConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author andrew.daiza
 */
public class Customer {

    int customerId;
    int addressId;
    String customerName;
    String customerAddress;
    String cityName;
    String customerPhone;

    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private static ObservableList<Customer> filteredCustomers = FXCollections.observableArrayList();

    public static ObservableList<Customer> getAllCustomers() throws SQLException, Exception {

        allCustomers.clear();
        try {
            String selectStatement = "SELECT customer.customerId, customerName, address.addressId, address.address, phone, city.city FROM customer, address, city WHERE customer.addressId = address.addressId AND address.cityId = city.cityId";

            PreparedStatement ps1 = DBConnection.getConnection().prepareStatement(selectStatement);
            ps1.execute();

            ResultSet rs = ps1.executeQuery();

            while (rs.next()) {
                int customerId = rs.getInt("customerId");
                String customerName = rs.getString("customerName"); // grabs VARCHAR rows
                String address = rs.getString("address");
                int addressId = rs.getInt("addressId");
                String phone = rs.getString("phone");
                String city = rs.getString("city");

                Customer customer = new Customer(customerId, addressId, customerName, address, city, phone);

                Customer.addCustomer(customer);

            }
            return allCustomers;
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
            return null;
        }
    }

    public Customer() {
    }

    public Customer(int customerId, int addressId, String customerName, String customerAddress, String cityName, String customerPhone) {
        this.customerId = customerId;
        this.addressId = addressId;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.cityName = cityName;
        this.customerPhone = customerPhone;
    }

    public static void addCustomer(Customer customer) {
        allCustomers.add(customer);
    }

    public static void deleteCustomer(Customer selectedCustomer) {
        Customer.allCustomers.remove(selectedCustomer);
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getAddressId() {
        return addressId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerAddressId(int addressId) {
        this.addressId = addressId;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public static ObservableList<Customer> lookupCustomerId(int PartId) {
        return filteredCustomers;
    }

    public static ObservableList<Customer> lookupCustomer(String partName) {
        return filteredCustomers;
    }

    public static Customer lookupCustomer(int partId) throws Exception {
        int index = -1;
        for (Customer customer : allCustomers) {
            index++;

            if (customer.getCustomerId() == partId) {
                Customer.getAllCustomers().set(index, customer);
                return customer;
            }
        }
        return null;
    }

}
