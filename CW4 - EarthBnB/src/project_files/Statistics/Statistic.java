package project_files.Statistics;

import javafx.scene.Node;
import project_files.AirbnbListing;

import java.util.ArrayList;

/**
 * Class Statistic - Represents a type of statistic.
 * The specific implementations of the statistics are provided in the inheriting subclasses.
 * @author Valentin Magis
 * @version 1.0
 * @since 2021-03-11
 */
public abstract class Statistic {
    protected String name; // The name of the statistic. Displayed as the label of each statistic.
    protected Node statistic;

    /**
     * Get the name of the statistic. Displayed in the label of a statistics panel.
     * @return The name of the statistic.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Get the result of the statistics calculation.
     * @return
     */
    public Node getStatistic()
    {
        return statistic;
    }


    /**
     * Update the current statistic.
     * @param listings
     * @return
     */
    protected abstract void updateStatistic(ArrayList<AirbnbListing> listings);
}
