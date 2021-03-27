package project_files;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LoginPanelController implements Initializable {

    private ArrayList<AirbnbListing> listings = new ArrayList<>();

    private Account user;

    private MainWindowController mainWindowController;

    @FXML
    TextField usernameTextField;

    @FXML
    PasswordField passwordTextField;

    @FXML
    BorderPane mainPane;

    @FXML
    BorderPane loginpagePane;

    @FXML
    Button signinMenu;

    @FXML
    Button signupMenu;

    private int accountID;

    private boolean currentPanel;

    public void loginButtonClicked(javafx.event.ActionEvent actionEvent) {
        validateLogin();

    }

    public void createUser(Account user) {
        this.user = user;
    }


    public void goBack() {
        try {
            if(user != null) {
                mainWindowController.initializeListings(listings, user);
                mainWindowController.setCurrentUser(user);
                mainWindowController.setLoggedIn(true);
            }
            usernameTextField.getScene().getWindow().hide();
        } catch (Exception e) {

        }
    }

    public void setMainWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    public void navigateToRegister(javafx.event.ActionEvent actionEvent) throws IOException {
        if(((Button) actionEvent.getSource()).getId().equals("signupMenu")) {
            Parent nextPanel;
            FXMLLoader statsLoader = new FXMLLoader(getClass().getResource("registerView.fxml"));
            nextPanel = statsLoader.load();
            loginpagePane.setCenter(nextPanel);
            signinMenu.setStyle(signupMenu.getStyle());
            signupMenu.setStyle("-fx-background-color: #FF5733");
        } else {
            Parent root = FXMLLoader.load(getClass().getResource("loginPanel.fxml"));
            Stage newStage = new Stage();
            newStage.setTitle("Login Panel");
            newStage.setScene(new Scene(root, 600, 500));
            newStage.setResizable(false);
            newStage.show();
            loginpagePane.getScene().getWindow().hide();
        }
    }


    public void validateLogin() {
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();

        String verifyLogin = "SELECT * FROM account WHERE username = '" + usernameTextField.getText() + "' AND password = '" + passwordTextField.getText() + "'";
    try {
        Statement statement = connectDB.createStatement();
        ResultSet queryResult = statement.executeQuery(verifyLogin);

       if(queryResult.next()) {
            System.out.println("Logged In");
            accountID = queryResult.getInt(1);
            user = new Account(
                    queryResult.getInt(1),
                    queryResult.getString(2),
                    queryResult.getString(3),
                    queryResult.getString(4)
            );
           System.out.println("Email: " + user.getEmailAddress());
           goBack();
        } else {
           System.out.println("Incorrect Login Details");
       }


    } catch (Exception e) {
        e.printStackTrace();
        e.getCause();
    }
    }

    public void initializeListings(ArrayList<AirbnbListing> listings) {
        this.listings = listings;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentPanel = false;
        signinMenu.setStyle("-fx-background-color: #FF5733");
        user = null;
    }

}
