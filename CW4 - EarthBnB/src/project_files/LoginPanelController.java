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


/**
 * This class is the controller for the loginPanelView. It is opened when the log in button is pressed in the welcome
 * panel. It allows the user to log in to an account. A log in can be validated through either the database, or through
 * the Account arraylist.
 *
 * @author  Valentin Magis, Rahin Ashraf, Vandad Vafai Tabrizi, Barnabas Szalai
 * @version 1.0
 * @since   2021-03-11
 */
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


    /**
     * This method is called when the users clicks on the back button. It hides the login panel stage. It also sends
     * the newly created Account object to the instantiated MainFrameController object.
     */
    public void goBack() {
        try {
            if(user != null) {

                mainFrameController.setCurrentUser(user);
                mainFrameController.setLoggedIn(true);
            }
            loginpagePane.getScene().getWindow().hide();
        } catch (Exception e) {
            System.out.println("mainWindowController is null");
        }
    }

    /**
     * A method to initialize the MainFrameController. It is called when the class is instantiated in order for it to
     * have access to the main window.
     * @param   mainFrameController the MainFrameController object that is open in the background.
     */
    public void setMainWindowController(MainFrameController mainFrameController) {
        this.mainFrameController = mainFrameController;
    }


    /**
     * This method is called when the user clicks on the sign up button. It creates a new registerView and sets is into
     * the center of the current panel. When the user is on the register panel, the center of the pane is switched
     * back to the login panel.
     */
    public void loginRegisterNavigator(javafx.event.ActionEvent actionEvent) throws IOException {

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

    /**
     * This method generates a hashed password using the SHA-512 cryptographic hash function. It instantiates an
     * object of type MessageDigest, which provides the functionality of creating one-way algorithms.
     * @param password  the password string that is to be hashed
     * @return  String  the generated hashed password
     */
    public String hashPW(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        messageDigest.reset();
        messageDigest.update(password.getBytes(StandardCharsets.UTF_8));

        return String.format("%0128x", new BigInteger(1, messageDigest.digest()));
    }


    /**
     * This methods checks whether there exists an Account with the username and password combination entered by the
     * user. If the application is using a database, this information is queried from the database. If it is not
     * using the database, than the combination is compared to the Account objects that have been saved in an
     * ArrayList.
     */
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


    /**
     * This method initializes some of the controller's instance variables and button states.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentPanel = false;
        user = null;
        backToSignIn.setVisible(false);
    }

}
