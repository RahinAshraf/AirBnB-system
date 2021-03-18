package project_files;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

import java.util.ArrayList;

public class StatisticsPanelController extends Panel {
    //The four default statistics
    Statistic accommodationType;
    Statistic availableProperties;
    Statistic averageNumReviews;
    Statistic mostExpensiveBorough;
    // Further not immediately
    //dummy stats - used for now to be able to implement structure
    Statistic statA;
    Statistic statB;
    Statistic statC;
    Statistic statD;

    StatsPanelElement panel1;
    StatsPanelElement panel2;
    StatsPanelElement panel3;
    StatsPanelElement panel4;

    ArrayList<AirbnbListing> listings;

    ArrayList<StatsPanelElement> allStatPanels;



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


    public StatisticsPanelController() {
    }

    public void initializeStats(ArrayList<AirbnbListing> listings)
    {
        // Create and load the statistics
        accommodationType = new StatAccommodationType(listings);
        availableProperties = new StatAvailableProperties(listings);
        averageNumReviews = new StatAverageNumReviews(listings);
        mostExpensiveBorough = new StatMostExpensiveBorough(listings);
        statA = new StatA(listings);
        statB = new StatB(listings);
        statC = new StatC(listings);
        statD = new StatD(listings);

        // Just a temporary list, specifies all elements that are not being shown.
        // Would be better to derive them from the ones being displayed, making sure no bs can happen.
        ArrayList<Statistic> statisticsInQueue = new ArrayList<>();
        statisticsInQueue.add(statA);
        statisticsInQueue.add(statB);
        statisticsInQueue.add(statC);
        statisticsInQueue.add(statD);

        // Each panel stores the elements that it could possibly show later.
        panel1 = new StatsPanelElement(statisticsInQueue, accommodationType);
        panel2 = new StatsPanelElement(statisticsInQueue, availableProperties);
        panel3 = new StatsPanelElement(statisticsInQueue, averageNumReviews);
        panel4 = new StatsPanelElement(statisticsInQueue, mostExpensiveBorough);

        allStatPanels = new ArrayList<>();
        allStatPanels.add(panel1);
        allStatPanels.add(panel2);
        allStatPanels.add(panel3);
        allStatPanels.add(panel4);

        this.listings = listings;
        //create list objects for each panel

        //Display the first statistics
        displaySinglePanel(statNameLbl1, statLbl1, accommodationType);

        displaySinglePanel(statNameLbl2, statLbl2, availableProperties);

        displaySinglePanel(statNameLbl3, statLbl3, averageNumReviews);

        displaySinglePanel(statNameLbl4, statLbl4, mostExpensiveBorough);
    }


    @FXML
    private void buttonClicked(ActionEvent event) {
        Node selectedPane = (Node) event.getSource();
        switch(selectedPane.getId())
        {
            case "nextBtn1": displaySinglePanel(statNameLbl1, statLbl1, getNext(panel1));
                break;
            case "prevBtn1": displaySinglePanel(statNameLbl1, statLbl1, getPrev(panel1));
                break;
            case "nextBtn2": displaySinglePanel(statNameLbl2, statLbl2, getNext(panel2));
                break;
            case "prevBtn2": displaySinglePanel(statNameLbl2, statLbl2, getPrev(panel2));
                break;
            case "nextBtn3": displaySinglePanel(statNameLbl3, statLbl3, getNext(panel3));
                break;
            case "prevBtn3": displaySinglePanel(statNameLbl3, statLbl3, getPrev(panel3));
                break;
            case "nextBtn4": displaySinglePanel(statNameLbl4, statLbl4, getNext(panel4));
                break;
            case "prevBtn4": displaySinglePanel(statNameLbl4, statLbl4, getPrev(panel4));
                break;
        }
    }

    // Get rid of code duplication in getNext and getPrev

    private Statistic getNext(StatsPanelElement panel)
    {
        Statistic nextStat;
        int i = panel.getIndexOfCurrent();

        if (i < panel.getStatisticsList().size()-1) // next element wont be out of bounds
            nextStat = panel.getStatisticsList().get(i+1);
        else // Reached end and get first.
            nextStat = panel.getStatisticsList().get(0);

        updateOtherPanels(panel, nextStat);
        panel.setCurrentStatistic(nextStat);
        return nextStat;
    }

    private Statistic getPrev(StatsPanelElement panel){

        Statistic nextStat;
        int i = panel.getIndexOfCurrent();

        if (i > 0) // next element wont be out of bounds
            nextStat = panel.getStatisticsList().get(i-1);
        else // Reached end and get first.
            nextStat = panel.getStatisticsList().get(panel.getStatisticsList().size()-1);

        updateOtherPanels(panel, nextStat);
        panel.setCurrentStatistic(nextStat);
        return nextStat;
    }

    /**
     * Remove the nextstat from the other lists (will now be shown here and therefore should not be shown in other)
     * Add the currentstat to the other lists (not displayed anymore, can be shown by others now)
     * @param currentPanel
     * @param nextStat
     */
    private void updateOtherPanels(StatsPanelElement currentPanel, Statistic nextStat) {
        for (int k = 0; k < allStatPanels.size(); k++)
        {
            if (allStatPanels.get(k) != currentPanel)
            {
                allStatPanels.get(k).updateList(currentPanel.getCurrentStatistic(), nextStat); // The previously current stat of the panel is now free. Add it to the list of other panels. The nextStat is now taken, remove it from the list of the other panels.
            }
        }
    }

    private void displaySinglePanel(Label titleLbl, Label textLbl, Statistic stat) // Get correct stat at runtime
    {
        titleLbl.setText(stat.getName());
        textLbl.setText(stat.getStatistic(listings));
    }
}
