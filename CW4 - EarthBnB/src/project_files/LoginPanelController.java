package project_files;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;


import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class LoginPanelController implements Initializable {

    private Account user;

    private MainFrameController mainFrameController;

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

    @FXML
    Button backToSignIn;

    Parent registerPanel;

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
                //mainWindowController.initializeListings(listings, user);
                mainFrameController.setCurrentUser(user);
                mainFrameController.setLoggedIn(true);
            }
            loginpagePane.getScene().getWindow().hide();
        } catch (Exception e) {
            System.out.println("mainWindowController is null");
        }
    }

    public void setMainWindowController(MainFrameController mainFrameController) {
        this.mainFrameController = mainFrameController;
    }

    public void navigateToRegister(javafx.event.ActionEvent actionEvent) throws IOException {

        if(((Button) actionEvent.getSource()).getId().equals("signupMenu")) {
            if(registerPanel == null) {
                FXMLLoader registerLoader = new FXMLLoader(getClass().getResource("registerView.fxml"));
                registerPanel = registerLoader.load();
                RegisterController registerController = registerLoader.getController();
                registerController.setMainWindowController(mainFrameController);
            }
            loginpagePane.setCenter(registerPanel);
            backToSignIn.setVisible(true);
        } else {
            loginpagePane.setCenter(mainPane);
            backToSignIn.setVisible(false);
        }

    }
    public String hashPW(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        messageDigest.reset();
        messageDigest.update(password.getBytes(StandardCharsets.UTF_8));

        return String.format("%0128x", new BigInteger(1, messageDigest.digest()));
    }


    public void validateLogin() {
        if(MainFrameController.isUsingDatabase()) {
            DatabaseConnection connection = new DatabaseConnection();
            Connection connectDB = connection.getConnection();

            try {
                String verifyLogin = "SELECT * FROM account WHERE username = '" + usernameTextField.getText() + "' AND password = '" + hashPW(passwordTextField.getText()) + "'";

                Statement statement = connectDB.createStatement();
                ResultSet queryResult = statement.executeQuery(verifyLogin);

                if (queryResult.next()) {
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
        } else {
            ArrayList<Account> accounts = OfflineData.getAccounts();
            boolean found = false;
            for(int i=0; i<accounts.size(); i++) {
                try {
                    if (accounts.get(i).getUsername().equals(usernameTextField.getText()) && accounts.get(i).getPassword().equals(hashPW(passwordTextField.getText()))) {
                        user = accounts.get(i);
                        found = true;
                    }
                } catch(Exception e) {

                }
            }
            if (found) {
                System.out.println("Logged In");
                goBack();
            } else {
                System.out.println("Incorrect Login Details");
            }

        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentPanel = false;
        //signinMenu.setStyle("-fx-background-color: #FF5733");
        user = null;
        backToSignIn.setVisible(false);
    }

}
