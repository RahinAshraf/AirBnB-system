package project_files;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This class is the controller of the registerView. It allows the user to create an account. The account is stored
 * either in the database, or in an arraylist of type Accounts if the database is not utilized.
 *
 * @author  Valentin Magis, Rahin Ashraf, Vandad Vafai Tabrizi, Barnabas Szalai
 * @version 1.0
 * @since   2021-03-11
 */
public class RegisterController {

    @FXML
    TextField nameField;
    @FXML
    TextField emailField;
    @FXML
    PasswordField pwField;
    @FXML
    PasswordField pwConfField;
    @FXML
    Label feedbackLabel;

    MainFrameController mainFrameController;

    /**
     * Checks whether the entered register information's syntax is valid. Also calls either the database or offline
     * register validation to validate, create and store the newly created Account.
     */
    public void validateRegister() {
        if (nameField.getText().length() != 0 && pwField.getText().length() != 0 && pwField.getText().length() != 0 && pwConfField.getText().length() != 0) {
            if ((pwField.getText().equals(pwConfField.getText()))) {
                if(validateEmail(emailField.getText())) {
                    if(MainFrameController.isUsingDatabase()) {
                        validateDatabaseRegister();
                    } else {
                        validateOfflineRegister();
                    }
                } else {
                    createFeedback("Wrong email address format!", 2);
                }
            } else {
                createFeedback("The two passwords don't match!", 2);
            }
            } else {
                createFeedback("Please fill in all fields!", 2);
            }
    }


    /**
     * Checks whether an account with the entered username exists. If not, it creates an Account object and stores it
     * in an arraylist.
     */
    public void validateOfflineRegister() {
        ArrayList<Account> accounts = OfflineData.getAccounts();
        boolean found = false;
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getUsername().equals(nameField.getText())) {
                found = true;
            }
        }
        if (found) {
            createFeedback("Account already exists!", 2);
        } else {
            try {
                Account newAccount = new Account(OfflineData.getAccounts().size() + 1, nameField.getText(), hashPW(pwField.getText()), emailField.getText());
                accounts.add(newAccount); // add new account to the offline data
                createFeedback("Successful registration!", 1);
            } catch (Exception e) {

            }
        }
    }


    /**
     * Checks whether an account with the entered username exists in the database. If not, then the account information are stored in the database.
     */
    public void validateDatabaseRegister() {
        try {
            DatabaseConnection connection = new DatabaseConnection();
            Connection connectDB = connection.getConnection();
            Statement statement = connectDB.createStatement();
            String checkSignup = "SELECT * FROM account WHERE username = '" + nameField.getText() + "'";
            ResultSet queryResult = statement.executeQuery(checkSignup);
            if (queryResult.next()) {
                createFeedback("Account with this name exists!", 2);
            } else {
                String createSignup = "INSERT INTO account VALUES (NULL, '" + nameField.getText() + "', '" + hashPW(pwField.getText()) + "', '" + emailField.getText() + "')";

                statement.executeUpdate(createSignup);
                createFeedback("Successful registration!", 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
            createFeedback("The account could not be created!", 2);
        }
    }

    /**
     * This method generates a hashed password using the SHA-512 cryptographic hash function. It instantiates an
     * object of type MessageDigest, which provides the functionality of creating one-way algorithms.
     * @param password  the password string that is to be hashed
     * @return  String  the generated hashed password
     */
    private String hashPW(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        messageDigest.reset();
        messageDigest.update(password.getBytes(StandardCharsets.UTF_8));

        return String.format("%0128x", new BigInteger(1, messageDigest.digest()));
    }

    /**
     * Changes the color and text of the feedback label based on the result of a register attemption.
     */
    private void createFeedback(String text, int type) {
        Color color;
        switch (type) {
            case 1:
                color = Color.GREEN;
                break;
            case 2:
                color = Color.RED;
                break;
            default:
                color = Color.GREEN;
        }
        feedbackLabel.setText(text);
        feedbackLabel.setTextFill(color);
    }


    /**
     * Checks whether the email address entered by the user contains a @ and a dot.
     * @param email the email address entered by the user
     * @return  boolean whether the email address is a valid email address
     */

    private boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile("^.+@.+\\..+$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Initializes the mainFrameController so that the class can call its methods.
     * @param mainFrameController   the instantiated MainFramController object running in the background
     */
    public void setMainWindowController(MainFrameController mainFrameController) {
        this.mainFrameController = mainFrameController;
    }


}
