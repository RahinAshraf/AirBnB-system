package project_files;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class StatisticsPanelController extends MainframeContentPanel implements Initializable {
    Statistic accommodationType;
    Statistic availableProperties;
    Statistic averageNumReviews;
    Statistic mostExpensiveBorough;
    Statistic closestListingToAttraction;
    //dummy stats - used for now to be able to implement structure
    Statistic statB;
    Statistic statC;
    Statistic statD;

    StatsPanelElement panel1;
    StatsPanelElement panel2;
    StatsPanelElement panel3;
    StatsPanelElement panel4;

    ArrayList<StatsPanelElement> allStatPanels;

    @FXML
    Label statLbl1, statLbl2, statLbl3, statLbl4;

    @FXML
    Label statNameLbl1, statNameLbl2, statNameLbl3, statNameLbl4;

    public StatisticsPanelController() {
        currentUser = null;
        name = "Statistics";
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /*
        statLbl1 = new Label();
        statLbl2 = new Label();
        statLbl3 = new Label();
        statLbl4 = new Label();

        statNameLbl1 = new Label();
        statNameLbl2 = new Label();
        statNameLbl3 = new Label();
        statNameLbl4 = new Label();

         */
    }

    @Override
    public void initializeList(Listings listings, Account currentUser)
    {
        ArrayList<AirbnbListing> filteredListings = listings.getFilteredListings();
        // Create and load the statistics
        this.currentUser = currentUser;
        accommodationType = new StatAccommodationType(filteredListings);
        availableProperties = new StatAvailableProperties(filteredListings);
        averageNumReviews = new StatAverageNumReviews(filteredListings);
        mostExpensiveBorough = new StatMostExpensiveBorough(filteredListings);
        closestListingToAttraction = new StatClosestListingToAttraction(filteredListings);
        statB = new StatB(filteredListings);
        statC = new StatC(filteredListings);
        statD = new StatD(filteredListings);

        // Just a temporary list, specifies all elements that are not being shown.
        // Would be better to derive them from the ones being displayed, making sure no bs can happen.
        ArrayList<Statistic> statisticsInQueue = new ArrayList<>();
        statisticsInQueue.add(closestListingToAttraction);
        statisticsInQueue.add(statB);
        statisticsInQueue.add(statC);
        statisticsInQueue.add(statD);

        // Each panel stores the elements that it could possibly show later.
        panel1 = new StatsPanelElement(statisticsInQueue, accommodationType, statNameLbl1, statLbl1);
        panel2 = new StatsPanelElement(statisticsInQueue, availableProperties, statNameLbl2, statLbl2);
        panel3 = new StatsPanelElement(statisticsInQueue, averageNumReviews, statNameLbl3, statLbl3);
        panel4 = new StatsPanelElement(statisticsInQueue, mostExpensiveBorough, statNameLbl4, statLbl4);

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
        textLbl.setText(stat.getStatistic(listings.getFilteredListings()));
    }

    /**
     * Updates the statistic displayed in each panel. BS CODE THIS HAS TO BE CHANGED!
     */
    @Override
    public void updatePanel() {
        /*
        for (StatsPanelElement panel : allStatPanels)
        {
            //String newStat = statsPanelElement.getCurrentStatistic().updateStatistic(listings.getFilteredListings());
            displaySinglePanel(panel.getStatNameLbl(), panel.getStatLbl(), panel.getCurrentStatistic());
        }

         */

        displaySinglePanel(statNameLbl1, statLbl1, panel1.getCurrentStatistic());
        displaySinglePanel(statNameLbl2, statLbl2, panel2.getCurrentStatistic());
        displaySinglePanel(statNameLbl3, statLbl3, panel3.getCurrentStatistic());
        displaySinglePanel(statNameLbl4, statLbl4, panel4.getCurrentStatistic());
    }
}
