package project_files;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class LoginPanelController implements Initializable {

    @FXML
    TextField usernameTextField;

    @FXML
    TextField passwordTextField;

    public void loginButtonClicked(javafx.event.ActionEvent actionEvent) {
        validateLogin();

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
            System.out.println(queryResult.getInt("accountID"));
        } else {
           System.out.println("Incorrect Login Details");
       }


    } catch (Exception e) {
        e.printStackTrace();
        e.getCause();
    }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
