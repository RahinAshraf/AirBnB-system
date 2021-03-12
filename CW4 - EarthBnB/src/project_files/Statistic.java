package project_files;

import java.util.ArrayList;

/**
 * Class Statistic - Represents a type of statistic.
 * The specific implementations of the statistics are provided in the inheriting subclasses.
 */
public abstract class Statistic {
    protected String statistic; // The result of the calculation of the statistic. A String so adding multiple numbers or text is possible.
    protected String name; // The name of the statistic. Displayed as the label of each statistic.

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
    public String getStatistic()
    {
        return statistic;
    }

    /**
     * Update the current statistic.
     * @param listings
     */
    public abstract void updateStatistic(ArrayList<AirbnbListing> listings);

}
