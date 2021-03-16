package project_files;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.ArrayList;

public class StatisticsPanel extends Panel {
    Statistic accommodationType;
    Statistic availableProperties;
    Statistic averageNumReviews;
    Statistic mostExpensiveBorough;


    @FXML
    Label statLbl1;

    @FXML
    Label statLbl2;

    @FXML
    Label statLbl3;

    @FXML
    Label statLbl4;

    @FXML
    Label statNameLbl1;

    @FXML
    Label statNameLbl2;

    @FXML
    Label statNameLbl3;

    @FXML
    Label statNameLbl4;

    //@FXML
    //private Button prevBtn1, prevBtn2, prevBtn3, prevBtn4, nextBtn1, nextBtn2, nextBtn3, nextBtn4;

    @FXML
    Button prevBtn1;

    @FXML
    Button prevBtn2;

    @FXML
    Button prevBtn3;

    @FXML
    Button prevBtn4;

    @FXML
    Button nextBtn1;

    @FXML
    Button nextBtn2;

    @FXML
    Button nextBtn3;

    @FXML
    Button nextBtn4;


    public StatisticsPanel(ArrayList<AirbnbListing> listings) {
        /*
        accommodationType = new StatAccommodationType(listings);
        availableProperties = new StatAvailableProperties(listings);
        averageNumReviews = new StatAverageNumReviews(listings);
        mostExpensiveBorough = new StatMostExpensiveBorough(listings);

         */
    }


    @FXML
    private void leftButton1(ActionEvent event) { System.out.println("Hello world");}

    @FXML
    public void rightButton1(ActionEvent event) {
        System.out.println("You have clicked the right button");
    }

    @FXML
    private void leftButton2(ActionEvent event) {
        System.out.println("You have clicked the left button");
    }

    @FXML
    public void rightButton2(ActionEvent event) {
        System.out.println("You have clicked the right button");
    }

    @FXML
    private void leftButton3(ActionEvent event) {
        System.out.println("You have clicked the left button");
    }

    @FXML
    public void rightButton3(ActionEvent event) {
        System.out.println("You have clicked the right button");
    }

    @FXML
    private void leftButton4(ActionEvent event) {
        System.out.println("You have clicked the left button");
    }

    @FXML
    public void rightButton4(ActionEvent event) {
        System.out.println("You have clicked the right button");
    }

    private void displaySinglePanel(Label titleLbl, Label textLbl, Statistic stat) // Get correct stat at runtime
    {
        titleLbl.setText(stat.getName());
        //textLbl.setText(stat.getStatistic());
    }

    public void updateValues() {

    }

    //@FXML
    //private Label statLbl1;
    //private StatAvailableProperties statistic = new StatAvailableProperties();



/*
    private void showData(){
        statLbl1.setText(controller.getID());
        statLbl1.setText("Average number of reviews");

    }
*/
}
