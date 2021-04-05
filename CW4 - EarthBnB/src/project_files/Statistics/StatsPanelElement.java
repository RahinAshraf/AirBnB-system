package project_files.Statistics;


import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import project_files.Listings;

import java.util.ArrayList;

/**
 * Class StatsPanelElement - One element in the StatsPanelController. Each element has an individual queue to ensure going back and forth shows the same statistics.
 *
 * @author Valentin Magis
 * @version 1.0
 * @since 2021-03-11
 */
public class StatsPanelElement {
    private ArrayList<Statistic> statisticsQueue;
    private Statistic currentStatistic;
    private Listings listings;

    private Label statNameLbl; // The label to display the title of a statistic in.
    private BorderPane contentPane; // The pane to display the result of the statistic.

    /**
     * Create a new StatsPanelElement
     * @param borderPane The borderpane to display its content in.
     * @param freeStatisticsList The list of statistics not currently being displayed which therefore could be shown by this panel later on.
     * @param currentStatistic The statistic to be displayed at the beginning.
     * @param statNameLbl The Label to display the title of a statistic in
     * @param listings The Listings-object. The Panel gets the current ArrayList<AirbnbListings> from there.
     */
    public StatsPanelElement(BorderPane borderPane, ArrayList<Statistic> freeStatisticsList, Statistic currentStatistic, Label statNameLbl, Listings listings) {
        contentPane = borderPane;
        this.statisticsQueue = new ArrayList<>();
        this.statisticsQueue.addAll(freeStatisticsList);
        this.statisticsQueue.add(0, currentStatistic); // Add the currentStatistic to the queue of this panel.
        this.currentStatistic = currentStatistic;
        this.statNameLbl = statNameLbl;
        this.listings = listings;
        displayStatistic();
    }

    /**
     * Get the statistic which is currently being displayed.
     * @return The current statistic.
     */
    public Statistic getCurrentStatistic()
    {
        return currentStatistic;
    }

    /**
     * Get the index of the current statistic  in the statisticsQueue.
     * @return The index of the current statistic in the statisticsQueue
     */
    public int getIndexOfCurrent()
    {
        return statisticsQueue.indexOf(currentStatistic);
    }

    /**
     * Update the statistics list. If another panel is changed, the stat it changed to is removed from this panels queue
     * and the stat that now is available from the other panel is added to the list of this panel to potentially be shown.
     * @param addStat The stat to be added.
     * @param removeStat The stat to be removed.
     */
    public void updateList(Statistic addStat, Statistic removeStat)
    {
        statisticsQueue.add(addStat);
        statisticsQueue.remove(removeStat);
    }

    /**
     * Display the statistic.
     * Sets the title and the content.
     */
    private void displayStatistic()
    {
        statNameLbl.setText(currentStatistic.getName());
        contentPane.setCenter(currentStatistic.getStatistic());
    }

    /**
     * Reload the currently shown statistic. Always invoke when a filter has been changed.
     */
    public void updatePanelElement()
    {
        currentStatistic.updateStatistic(listings.getFilteredListings());
        displayStatistic();
    }

    /**
     * Load the stat which is in the queue before the current stat.
     * @return The new statistic being displayed.
     */
    public Statistic getPrevStat()
    {
        Statistic nextStat;
        int i = getIndexOfCurrent();

        if (i > 0) // next element wont be out of bounds
            nextStat = statisticsQueue.get(i - 1);
        else // Reached end and get first.
            nextStat = statisticsQueue.get(statisticsQueue.size() - 1);

        currentStatistic = nextStat;
        displayStatistic();
        return nextStat;
    }

    /**
     * Load the stat which in the queue after the current stat.
     * @return The next statistic being displayed.
     */
    public Statistic getNextStat()
    {
        Statistic nextStat;
        int i = getIndexOfCurrent();

        if (i < statisticsQueue.size()-1) // next element wont be out of bounds
            nextStat = statisticsQueue.get(i+1);
        else // Reached end and get first.
            nextStat = statisticsQueue.get(0);
        currentStatistic = nextStat;
        displayStatistic();
        return nextStat;
    }
}
