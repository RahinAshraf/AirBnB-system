package project_files;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

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
        if(mainFrameController.isUsingDatabase()) {
            DatabaseConnection connection = new DatabaseConnection();
            Connection connectDB = connection.getConnection();

            String createSignup = "INSERT INTO account VALUES (NULL, '" + nameField.getText() + "', '" + pwField.getText() + "', '" + emailField.getText() + "')";
            String checkSignup = "SELECT * FROM account WHERE username = '" + nameField.getText() + "'";
            if (nameField.getText().length() != 0 && pwField.getText().length() != 0 && pwField.getText().length() != 0 && pwConfField.getText().length() != 0) {
                if ((pwField.getText().equals(pwConfField.getText()))) {
                    if(validateEmail(emailField.getText())) {
                        try {
                            Statement statement = connectDB.createStatement();
                            ResultSet queryResult = statement.executeQuery(checkSignup);
                            if (queryResult.next()) {
                                createFeedback("Account with this name exists!", 2);
                            } else {
                                statement.executeUpdate(createSignup);
                                createFeedback("Successful registration!", 1);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            e.getCause();
                            createFeedback("The account could not be created!", 2);
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
        } else {
            ArrayList<Account> accounts = mainFrameController.getOfflineAccounts();
            boolean found = false;
            for(int i=0; i<accounts.size(); i++) {
                if(accounts.get(i).getUsername().equals(nameField.getText())) {
                    found = true;
                }
            }
            if(found) {
                createFeedback("Account already exists!", 2);
            } else {
                Account newAccount = new Account(mainFrameController.getOfflineAccounts().size()+1, nameField.getText(), pwField.getText(), emailField.getText());
                accounts.add(newAccount);
                createFeedback("Successful registration!", 1);
            }
        }
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
