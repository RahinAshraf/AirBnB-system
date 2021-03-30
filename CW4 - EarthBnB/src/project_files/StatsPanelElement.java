package project_files;


import javafx.scene.control.Label;

import java.util.ArrayList;

public class StatsPanelElement {
    private ArrayList<Statistic> statisticsList;
    private Statistic currentStatistic;


    private Label statNameLbl, statLbl;

    public StatsPanelElement(ArrayList<Statistic> freeStatisticsList, Statistic currentStatistic, Label statNameLbl, Label statLbl) {
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

    public Label getStatNameLbl() { return statNameLbl; }

    public Label getStatLbl() { return statLbl; }

    public void setStatLbl(String text) {
        statLbl.setText(text);
    }

    public void setStatNameLbl(String text) {
        statNameLbl.setText(text);
    }
}
