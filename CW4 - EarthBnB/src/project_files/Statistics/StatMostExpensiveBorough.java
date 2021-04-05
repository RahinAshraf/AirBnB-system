package project_files.Statistics;

import project_files.AirbnbListing;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Class MostExpensiveBoroughStat is the statistic finding the most expensive borough in the given set of data.
 * The price for a property is the price per night * the minimum number of nights.
 * @author Valentin Magis
 * @version 1.0
 * @since 2021-03-11
 */
public class StatMostExpensiveBorough extends StatisticAsText {

    /**
     * Create a statistic object for the most expensive borough.
     * The number calculated is updated when the filters are changed by the user.
     */
    public StatMostExpensiveBorough(ArrayList<AirbnbListing> listings)
    {
        name = "Most Expensive Borough";
        updateStatistic(listings);
    }

    /**
     * Update the statistic. If multiple boroughs happen to have the same value, the one found first is considered to be the more expensive.
     * @param listings A list of boroughListings the statistic should be calculated for.
     * @return
     */
    protected void updateStatistic(ArrayList<AirbnbListing> listings)
    {
        if (!listings.isEmpty()) {
            String mostExpensiveBorough = listings.stream()
                    .collect(Collectors.groupingBy(AirbnbListing::getNeighbourhood, Collectors.averagingDouble(AirbnbListing::getAveragePrice))) // Create a map mapping each borough to its average price
                    .entrySet().stream().max((borough1, borough2) -> borough1.getValue() >= borough2.getValue() ? 1 : -1).get().getKey(); // Get the name (the key) of that map with the largest average price (value)
            statLabel.setText(mostExpensiveBorough);
        }
        else
            statLabel.setText("No properties fit Your search.");
    }
}
