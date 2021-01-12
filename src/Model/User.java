package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author andrew.daiza
 */
public class User {

    static String username;

    public static ObservableList<String> userLoggedIn = FXCollections.observableArrayList();

    public static ObservableList<String> getUserLoggedIn() {
        return userLoggedIn;
    }

    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    public static String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String privledgeLevel() {
        return "User Privledges";
    }

}
