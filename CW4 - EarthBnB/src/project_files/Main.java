package project_files;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("panelGUI.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }




@FXML
    private void leftButton(ActionEvent event){
        System.out.println("You have clicked the left button");


    }



    public void rightButton(ActionEvent event){
        System.out.println("You have clicked the right button");


    }

    public static void main(String[] args) {
        launch(args);
    }
}
