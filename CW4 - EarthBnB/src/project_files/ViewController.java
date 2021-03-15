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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ViewController implements Initializable {


    @FXML
    Button nextPaneBtn;
    @FXML
    BorderPane rootPane;
    @FXML
    BorderPane bottomPane;
    @FXML
    BorderPane welcomePane;


    @FXML
    private void setNextPane(ActionEvent event) throws IOException {



        BorderPane statsPageParent = FXMLLoader.load(getClass().getResource("welcomePanelGUI.fxml"));
//


//        Scene statsPageScene = new Scene(borderPane);
//        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
//        statsPageScene.setRoot(rootPane);
//        window.setScene(statsPageScene);
//        window.show();

        statsPageParent.setBottom(bottomPane);
        rootPane.getChildren().setAll(statsPageParent);
        System.out.println("asd");

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
