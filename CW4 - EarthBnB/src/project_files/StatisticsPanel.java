package project_files;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;

public class StatisticsPanel extends Panel {
    Statistic accommodationType;
    Statistic availableProperties;
    Statistic averageNumReviews;
    Statistic mostExpensiveBorough;
    ArrayList<AirbnbListing> listings;

    LinkedList<Statistic> panel1, panel2, panel3, panel4;


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


    public StatisticsPanel() {

    }

    public void initializeStats(ArrayList<AirbnbListing> listings)
    {
        accommodationType = new StatAccommodationType(listings);
        availableProperties = new StatAvailableProperties(listings);
        averageNumReviews = new StatAverageNumReviews(listings);
        mostExpensiveBorough = new StatMostExpensiveBorough(listings);
        this.listings = listings;
    }


    @FXML
    private void buttonClicked(ActionEvent event) {
        Node selectedPane = (Node) event.getSource();
        switch(selectedPane.getId())
        {
            case "nextBtn1": displaySinglePanel(statNameLbl1, statLbl1, getNext(statLbl1));
                break;
            case "prevBtn1": displaySinglePanel(statNameLbl1, statLbl1, accommodationType);
                break;
            case "nextBtn2": displaySinglePanel(statNameLbl2, statLbl2, accommodationType);
                break;
            case "prevBtn2": displaySinglePanel(statNameLbl2, statLbl2, availableProperties);
                break;
            case "nextBtn3": displaySinglePanel(statNameLbl3, statLbl3, accommodationType);
                break;
            case "prevBtn3": displaySinglePanel(statNameLbl3, statLbl3, accommodationType);
                break;
            case "nextBtn4": displaySinglePanel(statNameLbl4, statLbl4, accommodationType);
                break;
            case "prevBtn4": displaySinglePanel(statNameLbl4, statLbl4, accommodationType);
                break;
        }
    }

    private Statistic getNext(Label labelNumber)
    {
        return accommodationType; //Just some bs to make it run
    }

    private void displaySinglePanel(Label titleLbl, Label textLbl, Statistic stat) // Get correct stat at runtime
    {
        titleLbl.setText(stat.getName());
        textLbl.setText(stat.getStatistic(listings));
    }
}
