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
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ViewController implements Initializable {

    private int currentPage = 0;


    @FXML
    Button nextPaneBtn;
    @FXML
    BorderPane rootPane;
    @FXML
    BorderPane bottomPane;
    @FXML
    BorderPane welcomePanel;


    @FXML
    private void setNextPane(ActionEvent event) throws IOException {


        if(currentPage == 0) {
            System.out.println(currentPage);
            BorderPane statsPageParent = FXMLLoader.load(getClass().getResource("welcomePanelGUI.fxml"));
            rootPane.setCenter(statsPageParent);


//            Scene statsPageScene = new Scene(statsPageParent);
//            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            //statsPageScene.setBottom(rootPane.getBottom());
//            statsPageParent.setBottom(rootPane.getBottom());
//            window.setScene(statsPageScene);
//            window.show();

    //            statsPageParent.setBottom(bottomPane);
    //            //statsPageParent.setCenter(welcomePanel.getCenter());
    //            rootPane.getChildren().setAll(statsPageParent);
    //            //System.out.println("asd");

            currentPage = 1;
        } else if (currentPage == 1) {
            VBox statsPageParent = FXMLLoader.load(getClass().getResource("statisticsGUI.fxml"));
            rootPane.setCenter(statsPageParent);


//            Scene statsPageScene = new Scene(statsPageParent);
//            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            //statsPageScene.setBottom(rootPane.getBottom());
//            window.setScene(statsPageScene);
//            window.show();
            currentPage = 2;
        } else {
            BorderPane root = FXMLLoader.load(getClass().getResource("MainFrameView.fxml"));

            rootPane.setCenter(root);

            currentPage = 0;
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
