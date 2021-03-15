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

private Panel statisticsPanel;
private Panel welcomePanel;

private ArrayList<Panel> panels;

//Panel statsPanel = new S
    @Override
    public void start(Stage primaryStage) throws Exception {
        load("airbnb-london.csv");

        buildPanels();

        Parent root = FXMLLoader.load(getClass().getResource("MainFrameView.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }

    private void load(String filename){
        AirbnbDataLoader loader = new AirbnbDataLoader();
        listings = loader.load(filename);
    }

    /**
     * Build the panels. Add here for more panels.
     */
    private void buildPanels()
    {
        welcomePanel = new WelcomePanel();
        statisticsPanel = new StatisticsPanel();

        panels = new ArrayList<>();

        //Put in in the right order.
        panels.add(welcomePanel);
        panels.add(statisticsPanel);
    }


    @FXML
    private void setNextPane(ActionEvent e)
    {
        //setPanel(panel);
    }

    @FXML
    private void setPrevPane(ActionEvent e)
    {
        //setPanel(panel);
    }

    /**
     * Set a panel to be shown
     * @param panel The panel
     */
    private void setPanel(Panel panel)
    {

    }

    public static void main(String[] args) {
        launch(args);
    }
}
