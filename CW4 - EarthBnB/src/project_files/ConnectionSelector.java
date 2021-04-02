package project_files;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ConnectionSelector {

    @FXML
    Button dbSelectButton, offlineSelectButton;

    @FXML
    public void selectDBConnection(ActionEvent e) throws IOException {
        boolean usingDatabase = false;
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("MainFrameView.fxml"));
        Parent root = mainLoader.load();
        Stage newStage = new Stage();
        newStage.setTitle("EarthBnB");
        newStage.setScene(new Scene(root, 700, 550));
        newStage.setResizable(true);
        newStage.show();
        if(((Button)e.getSource()).getId().equals("dbSelectButton")) {
            usingDatabase = true;
        }
        MainFrameController mainFrameController = mainLoader.getController();
        mainFrameController.setUsingDatabase(usingDatabase);
        dbSelectButton.getScene().getWindow().hide();
    }

}
