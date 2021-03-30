package project_files;


import javafx.scene.control.Label;

import java.util.ArrayList;

public class StatsPanelElement {
    private ArrayList<Statistic> statisticsList;
    private Statistic currentStatistic;
    private Listings listings;

    private Label statNameLbl, statLbl;


    public StatsPanelElement(ArrayList<Statistic> freeStatisticsList, Statistic currentStatistic, Label statNameLbl, Label statLbl, Listings listings) {
        statisticsList = new ArrayList<>();
        this.statisticsList.addAll(freeStatisticsList);
        this.statisticsList.add(0, currentStatistic);
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
        return statisticsList.indexOf(currentStatistic);
    }

    public void updateList(Statistic addStat, Statistic removeStat)
    {
        statisticsList.add(addStat);
        statisticsList.remove(removeStat);
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
            nextStat = statisticsList.get(i - 1);
        else // Reached end and get first.
            nextStat = statisticsList.get(statisticsList.size() - 1);

        currentStatistic = nextStat;
        displayStatistic();
        return nextStat;
    }

    public Statistic getNextStat()
    {
        Statistic nextStat;
        int i = getIndexOfCurrent();

        if (i < statisticsList.size()-1) // next element wont be out of bounds
            nextStat = statisticsList.get(i+1);
        else // Reached end and get first.
            nextStat = statisticsList.get(0);

        currentStatistic = nextStat;
        displayStatistic();
        return nextStat;
    }
}
