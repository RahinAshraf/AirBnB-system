package project_files;

import java.util.ArrayList;

/**
 * Class MostExpensiveBoroughStat is the statistic finding the most expensive borough in the given set of data.
 * The price for a property is the price per night * the minimum number of nights.
 */
public class StatMostExpensiveBorough extends Statistic {

    /**
     * Create a statistic object for the most expensive borough.
     * The number calculated is updated when the location is changed by the user.
     */
    public StatMostExpensiveBorough(ArrayList<AirbnbListing> listings)
    {
        name = "Most Expensive Borough";
        updateStatistic(listings);
    }

    /**
     * Update the statistic.
     * @param listings A list of listings the statistic should be calculated for.
     */
    public void updateStatistic(ArrayList<AirbnbListing> listings)
    {

    }
}
