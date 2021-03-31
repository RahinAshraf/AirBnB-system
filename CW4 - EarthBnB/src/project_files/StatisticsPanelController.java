package project_files;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

import java.util.ArrayList;


public class StatisticsPanelController extends MainframeContentPanel {
    private Statistic accommodationType, availableProperties, averageNumReviews, mostExpensiveBorough,
            closestListingToAttraction, statB, statC, statD;

    private StatsPanelElement panel1, panel2, panel3, panel4;

    private ArrayList<StatsPanelElement> allStatPanels;

    @FXML
    private Label statLbl1, statLbl2, statLbl3, statLbl4;

    @FXML
    private Label statNameLbl1, statNameLbl2, statNameLbl3, statNameLbl4;

    public StatisticsPanelController() {
        currentUser = null;
        name = "Statistics";
    }

    @Override
    public void initializeList(Listings listings, Account currentUser)
    {
        this.listings = listings;
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
        panel1 = new StatsPanelElement(statisticsInQueue, accommodationType, statNameLbl1, statLbl1, listings);
        panel2 = new StatsPanelElement(statisticsInQueue, availableProperties, statNameLbl2, statLbl2, listings);
        panel3 = new StatsPanelElement(statisticsInQueue, averageNumReviews, statNameLbl3, statLbl3, listings);
        panel4 = new StatsPanelElement(statisticsInQueue, mostExpensiveBorough, statNameLbl4, statLbl4, listings);

        allStatPanels = new ArrayList<>();
        allStatPanels.add(panel1);
        allStatPanels.add(panel2);
        allStatPanels.add(panel3);
        allStatPanels.add(panel4);
    }

    /**
     * Depending on which button has been clicked, the chosen panel shows the ´previous or the next statistic.
     * @param event
     */
    @FXML
    private void buttonClicked(ActionEvent event) {
        Node selectedPane = (Node) event.getSource();

        switch (selectedPane.getId())
        {
            case "nextBtn1": nextStat(panel1);
                break;
            case "prevBtn1": prevStat(panel1);
                break;
            case "nextBtn2": prevStat(panel2);
                break;
            case "prevBtn2": nextStat(panel2);
                break;
            case "nextBtn3": prevStat(panel3);
                break;
            case "prevBtn3": nextStat(panel3);
                break;
            case "nextBtn4": prevStat(panel4);
                break;
            case "prevBtn4": nextStat(panel4);
                break;
        }
    }

    /**
     * Invokes that the given panel loads the next statistic and accordingly updates the lists of available statistics of the other panels.
     * @param panel
     */
    private void nextStat(StatsPanelElement panel)
    {
        updateOtherPanels(panel, panel.getNextStat()); // Getting the next stat automatically displays the new stat
    }


    /**
     * Invokes that the given panel loads the previous statistic and accordingly updates the lists of available statistics of the other panels.
     * @param panel
     */
    private void prevStat(StatsPanelElement panel){
        updateOtherPanels(panel, panel.getPrevStat());
    }

    /**
     * Remove the nextstat from the other lists (will now be shown here and therefore should not be shown in other)
     * Add the currentstat to the other lists (not displayed anymore, can be shown by others now)
     * @param currentPanel
     * @param nextStat
     */
    private void updateOtherPanels(StatsPanelElement currentPanel, Statistic nextStat) {
        for (int k = 0; k < allStatPanels.size(); k++) {
            if (allStatPanels.get(k) != currentPanel)
                allStatPanels.get(k).updateList(currentPanel.getCurrentStatistic(), nextStat); // The previously current stat of the panel is now free. Add it to the list of other panels. The nextStat is now taken, remove it from the list of the other panels.
        }
    }

    /**
     * Updates the statistic displayed in each panel.
     */
    @Override
    public void updatePanel() {
        for (StatsPanelElement panel : allStatPanels) {
            panel.displayStatistic();
        }
    }
}

