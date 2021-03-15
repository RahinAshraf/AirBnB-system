package project_files;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.ArrayList;



public class MainWindow extends Application {

private ArrayList<AirbnbListing> listings = new ArrayList();

//Panel statsPanel = new S
    @Override
    public void start(Stage primaryStage) throws Exception {
        load("airbnb-london.csv");
        Parent root = FXMLLoader.load(getClass().getResource("panelGUI.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    private void load(String filename){
        AirbnbDataLoader loader = new AirbnbDataLoader();

        listings = loader.load(filename);


    }

    @FXML
    private void leftButton(ActionEvent event) {
        System.out.println("You have clicked the left button");


    }


    public void rightButton(ActionEvent event) {
        System.out.println("You have clicked the right button");


    }

    public static void main(String[] args) {
        launch(args);
    }
}
