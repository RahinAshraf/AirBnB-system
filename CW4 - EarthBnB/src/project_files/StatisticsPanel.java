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
    //all the different statistics
    Statistic accommodationType;
    Statistic availableProperties;
    Statistic averageNumReviews;
    Statistic mostExpensiveBorough;
    //dummy stats - used for now to be able to implement structure
    Statistic statA;
    Statistic statB;
    Statistic statC;
    Statistic statD;

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
        statA = new StatA(listings);
        statB = new StatB(listings);
        statC = new StatC(listings);
        statD = new StatD(listings);


        this.listings = listings;
        //create list objects for each panel
        panel1 = new LinkedList<Statistic>();
        panel2 = new LinkedList<>();
        panel3 = new LinkedList<>();
        panel4 = new LinkedList<>();

        //store the stats in the lists for each panel. Hard coded for now, try find better way later if possible
        panel1.add(accommodationType);
        panel1.add(availableProperties);
        panel1.add(averageNumReviews);
        panel1.add(mostExpensiveBorough);
        panel1.add(statA);
        panel1.add(statB);
        panel1.add(statC);
        panel1.add(statD);

        panel2.add(accommodationType);
        panel2.add(availableProperties);
        panel2.add(averageNumReviews);
        panel2.add(mostExpensiveBorough);
        panel2.add(statA);
        panel2.add(statB);
        panel2.add(statC);
        panel2.add(statD);

        panel3.add(accommodationType);
        panel3.add(availableProperties);
        panel3.add(averageNumReviews);
        panel3.add(mostExpensiveBorough);
        panel3.add(statA);
        panel3.add(statB);
        panel3.add(statC);
        panel3.add(statD);

        panel4.add(accommodationType);
        panel4.add(availableProperties);
        panel4.add(averageNumReviews);
        panel4.add(mostExpensiveBorough);
        panel4.add(statA);
        panel4.add(statB);
        panel4.add(statC);
        panel4.add(statD);


    }


    @FXML
    private void buttonClicked(ActionEvent event) {
        Node selectedPane = (Node) event.getSource();
        switch(selectedPane.getId())
        {
            case "nextBtn1": displaySinglePanel(statNameLbl1, statLbl1, getNext(statLbl1));
                break;
            case "prevBtn1": displaySinglePanel(statNameLbl1, statLbl1, getPrev(statLbl1));
                break;
            case "nextBtn2": displaySinglePanel(statNameLbl2, statLbl2, getNext(statLbl2));
                break;
            case "prevBtn2": displaySinglePanel(statNameLbl2, statLbl2, getPrev(statLbl2));
                break;
            case "nextBtn3": displaySinglePanel(statNameLbl3, statLbl3, getNext(statLbl3));
                break;
            case "prevBtn3": displaySinglePanel(statNameLbl3, statLbl3, getPrev(statLbl3));
                break;
            case "nextBtn4": displaySinglePanel(statNameLbl4, statLbl4, getNext(statLbl4));
                break;
            case "prevBtn4": displaySinglePanel(statNameLbl4, statLbl4, getPrev(statLbl4));
                break;
        }
    }



    private Statistic getNext(Label labelNumber)
    {

        return accommodationType; //Just some bs to make it run
    }

    private Statistic getPrev(Label labelNumber){

        return accommodationType; //Just some bs to make it run

    }
    private void displaySinglePanel(Label titleLbl, Label textLbl, Statistic stat) // Get correct stat at runtime
    {
        titleLbl.setText(stat.getName());
        textLbl.setText(stat.getStatistic(listings));
    }
}
