package project_files;


import javafx.scene.control.Label;

import java.util.ArrayList;

public class StatsPanelElement {
    private ArrayList<Statistic> statisticsQueue;
    private Statistic currentStatistic;
    private Listings listings;

    private Label statNameLbl, statLbl;


    public StatsPanelElement(ArrayList<Statistic> freeStatisticsList, Statistic currentStatistic, Label statNameLbl, Label statLbl, Listings listings) {
        this.statisticsQueue = new ArrayList<>();
        this.statisticsQueue.addAll(freeStatisticsList);
        this.statisticsQueue.add(0, currentStatistic);
        this.currentStatistic = currentStatistic;
        this.statNameLbl = statNameLbl;
        this.statLbl = statLbl;
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
        System.out.println(this.toString() + " added: " + addStat.getName() + " removed " + removeStat.getName());
    }

    public void displayStatistic()
    {
        statNameLbl.setText(currentStatistic.getName());
        statLbl.setText(currentStatistic.getStatistic(listings.getFilteredListings()));
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
