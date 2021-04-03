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
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.regex.*;

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





    public void validateRegister() {
        if (nameField.getText().length() != 0 && pwField.getText().length() != 0 && pwField.getText().length() != 0 && pwConfField.getText().length() != 0) {
            if ((pwField.getText().equals(pwConfField.getText()))) {
                if(validateEmail(emailField.getText())) {
                    if(mainFrameController.isUsingDatabase()) {
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

    public void validateOfflineRegister() {
        ArrayList<Account> accounts = mainFrameController.getOfflineAccounts();
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
                Account newAccount = new Account(mainFrameController.getOfflineAccounts().size() + 1, nameField.getText(), hashPW(pwField.getText()), emailField.getText());
                accounts.add(newAccount);
                createFeedback("Successful registration!", 1);
            } catch (Exception e) {

            }
        }
    }


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

    private String hashPW(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        messageDigest.reset();
        messageDigest.update(password.getBytes(StandardCharsets.UTF_8));

        return String.format("%0128x", new BigInteger(1, messageDigest.digest()));
    }

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


    private boolean validateEmail(String email) {
        Pattern pattern = Pattern.compile("^.+@.+\\..+$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void setMainWindowController(MainFrameController mainFrameController) {
        this.mainFrameController = mainFrameController;
    }


}
