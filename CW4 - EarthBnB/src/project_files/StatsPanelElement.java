package project_files;

import java.util.ArrayList;

public class StatsPanelElement {
    private ArrayList<Statistic> statisticsList;
    private Statistic currentStatistic;

    public StatsPanelElement(ArrayList<Statistic> freeStatisticsList, Statistic currentStatistic) {
        statisticsList = new ArrayList<>();
        this.statisticsList.addAll(freeStatisticsList);
        this.statisticsList.add(0, currentStatistic);
        this.currentStatistic = currentStatistic;
    }

    public ArrayList<Statistic> getStatisticsList()
    {
        return statisticsList;
    }

    public Statistic getCurrentStatistic()
    {
        return currentStatistic;
    }

    public void setCurrentStatistic(Statistic currentStatistic) {
        this.currentStatistic = currentStatistic;
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
}
