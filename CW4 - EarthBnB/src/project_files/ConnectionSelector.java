package project_files;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This is the controller class for the connectionSelectorView. It is the first window that opens up when the program
 * is launched. The user can select whether to use the database connected to the program or not. This selection is
 * later stored as a boolean value.
 *
 * @author  Valentin Magis, Rahin Ashraf, Vandad Vafai Tabrizi, Barnabas Szalai
 * @version 1.0
 * @since   2021-03-11
 */
public class ConnectionSelector extends Application {

    @FXML
    private Button dbSelectButton, offlineSelectButton;

    /**
     * Start the program with displaying the connectionSelector.
     * The user here is able to choose whether to use the program with or without the online database.
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("connectionSelectorView.fxml"));
        stage.setTitle("EarthBnB");
        stage.setScene(new Scene(root, 600, 300));
        stage.setResizable(true);
        stage.show();
    }


    /**
     * This method is called when the user selects one of the two options. It closes the current window and
     * instantiates and shows a new stage with the MainFrameView.
     */
    @FXML
    private void selectDBConnection(ActionEvent e) throws IOException {
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

    /**
     * Launch the program.
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }

}
