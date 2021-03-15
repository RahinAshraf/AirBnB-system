package project_files;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ViewController implements Initializable {


    @FXML
    Button nextPaneBtn;


    @FXML
    private void setNextPane(ActionEvent event) throws IOException {
        Parent statsPageParent = FXMLLoader.load(getClass().getResource("statisticsGUI.fxml"));
        Scene statsPageScene = new Scene(statsPageParent);

        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(statsPageScene);
        window.show();

        System.out.println("asd");

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
