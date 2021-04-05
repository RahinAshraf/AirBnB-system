package project_files.Statistics;


import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import project_files.Listings;

import java.util.ArrayList;

public class StatsPanelElement {
    private ArrayList<Statistic> statisticsQueue;
    private Statistic currentStatistic;
    private Listings listings;

    private Label statNameLbl;
    private BorderPane contentPane;


    public StatsPanelElement(BorderPane borderPane, ArrayList<Statistic> freeStatisticsList, Statistic currentStatistic, Label statNameLbl, Listings listings) {
        contentPane = borderPane;
        this.statisticsQueue = new ArrayList<>();
        this.statisticsQueue.addAll(freeStatisticsList);
        this.statisticsQueue.add(0, currentStatistic);
        this.currentStatistic = currentStatistic;
        this.statNameLbl = statNameLbl;
        this.listings = listings;
        displayStatistic();
    }


    public Statistic getCurrentStatistic()
    {
        return currentStatistic;
    }

    public int getIndexOfCurrent()
    {
        return statisticsQueue.indexOf(currentStatistic);
    }

    public void updateList(Statistic addStat, Statistic removeStat)
    {
        statisticsQueue.add(addStat);
        statisticsQueue.remove(removeStat);
    }

    private void displayStatistic()
    {
        statNameLbl.setText(currentStatistic.getName());
        contentPane.setCenter(currentStatistic.getStatistic());
    }

    public void updatePanelElement()
    {
        currentStatistic.updateStatistic(listings.getFilteredListings());
        displayStatistic();
    }

    // Get rid of code duplication in getNext and getPrev?

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
