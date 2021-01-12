package View_Controller;

import Model.Administrator;
import Model.AppointmentDatabase;
import Model.User;
import Utilities.DBConnection;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author andrew.daiza
 */
public class LoginScreenController implements Initializable {

    Stage stage;
    Parent scene;

    ResourceBundle rb = ResourceBundle.getBundle("Lang/Words", Locale.getDefault());

    @FXML
    private Label userName;

    @FXML
    private Label password;

    @FXML
    private Label title;
    @FXML
    private TextField passwordTxt;
    @FXML
    private TextField usernameTxt;
    @FXML
    private Button signInButton;
    @FXML
    private Button exitButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // LANGUAGES
        // SPANISH AND ENGLISH
        rb = ResourceBundle.getBundle("Lang/Words", Locale.getDefault());
        userName.setText(rb.getString("Username"));
        password.setText(rb.getString("Password"));
        title.setText(rb.getString("Title"));
        signInButton.setText(rb.getString("Signin"));
        exitButton.setText(rb.getString("Exit"));

    }

    @FXML
    private void signIn(MouseEvent event) throws IOException, Exception {

        if (usernameTxt.getText().length() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            if (Locale.getDefault().getLanguage().equals("es")) {
                alert.setContentText(rb.getString("Please") + " " + rb.getString("Enter") + " " + rb.getString("a") + " " + rb.getString("Username"));
                alert.showAndWait();

            } else {
                alert.setContentText("Please Enter a Username");
                alert.showAndWait();
            }
        } else if (passwordTxt.getText().length() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            if (Locale.getDefault().getLanguage().equals("es")) {
                alert.setContentText(rb.getString("Please") + " " + rb.getString("Enter") + " " + rb.getString("a") + " " + rb.getString("Password"));
                alert.showAndWait();

            } else {
                alert.setContentText("Please Enter a Password");
                alert.showAndWait();
            }
        } else {
            String user = usernameTxt.getText();
            String pass = passwordTxt.getText();

            if (checkLogin(user, pass)) {
                User.userLoggedIn.clear();
                User.userLoggedIn.add(user);
                LocalDateTime beginStamp = LocalDateTime.now();
                String beginStampString = beginStamp.toString();
                BufferedWriter writer = new BufferedWriter(new FileWriter("LoginTimestamp.txt", true));
                String toWriter = "User " + User.userLoggedIn.get(0) + " logged in at " + beginStampString;
                writer.append(toWriter);
                writer.newLine();
                writer.close();

                if (AppointmentDatabase.getAppointmentsUser15min() == true) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText(rb.getString("You") + " " + rb.getString("have") + " " + rb.getString("an") + " " + rb.getString("appointment") + " " + rb.getString("within") + " " + "15" + " " + rb.getString("minutes"));
                    alert.showAndWait();
                }
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                if (Locale.getDefault().getLanguage().equals("es")) {
                    alert.setContentText(rb.getString("Invalid") + " " + rb.getString("Username") + "/" + rb.getString("Password"));
                    alert.showAndWait();

                } else {
                    alert.setContentText("Invalid Username/Password");
                    alert.showAndWait();
                }
            }
            // -------------------- //
            // POLYMORPHISM EXAMPLE
            // -------------------- //
            if (user.equals("admin")) {

                User admin1 = new Administrator();
                System.out.println(admin1.privledgeLevel());
            } else {
                User user1 = new User();
                System.out.println(user1.privledgeLevel());
            }
        }

    }

    @FXML
    private void exit(MouseEvent event) {
        System.exit(0);
    }

    private boolean checkLogin(String username, String password) {
        try {
            String selectStatement = "Select * from user where username=? and password=?";

            PreparedStatement ps1 = DBConnection.makeConnection().prepareStatement(selectStatement);
            ps1.setString(1, username);
            ps1.setString(2, password);

            ResultSet rs = ps1.executeQuery();

            if (rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
