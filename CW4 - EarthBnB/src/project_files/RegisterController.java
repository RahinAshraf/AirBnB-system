package project_files;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

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

    MainWindowController mainWindowController;





    public void validateRegister() {
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();

        String createSignup = "INSERT INTO account VALUES (NULL, '" + nameField.getText() + "', '" +  pwField.getText() + "', '" + emailField.getText() + "')";
        String checkSignup = "SELECT * FROM account WHERE username = '"+ nameField.getText() + "'";
        if (nameField.getText().length() != 0 && pwField.getText().length() != 0 && pwField.getText().length() != 0 && pwConfField.getText().length() != 0) {
            if ((pwField.getText().equals(pwConfField.getText()))) {
                try {
                    Statement statement = connectDB.createStatement();
                    ResultSet queryResult = statement.executeQuery(checkSignup);
                    if(queryResult.next()) {
                        feedbackLabel.setText("Account with this name exists!");
                        feedbackLabel.setTextFill(Color.RED);
                    } else {
                        statement.executeUpdate(createSignup);
                        feedbackLabel.setText("Successful registration!");
                        feedbackLabel.setTextFill(Color.GREEN);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    e.getCause();
                    feedbackLabel.setText("The account could not be created!");
                    feedbackLabel.setTextFill(Color.RED);
                }
            } else {
                feedbackLabel.setText("The two passwords don't match!");
                feedbackLabel.setTextFill(Color.RED);
            }
        } else {
            feedbackLabel.setText("Please fill in all fields!");
            feedbackLabel.setTextFill(Color.RED);
        }
    }

    public void setMainWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }


}
