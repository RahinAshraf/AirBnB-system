package project_files.Statistics;

import project_files.AirbnbListing;

import java.util.ArrayList;

/**
 * Class AvailablePropertiesStat is the statistic for the number of available properties in the given set of data.
 * @author Valentin Magis
 * @version 1.0
 * @since 2021-03-11
 */
public class StatAvailableProperties extends StatisticAsText {

    /**
     * Create a statistic object for the number of available properties.
     * @param listings The listings to calculate the statistic for.
     */
    public StatAvailableProperties(ArrayList<AirbnbListing> listings)
    {
        name = "Number of available properties";
        updateStatistic(listings);
    }

    /**
     * Update the statistic. Properties are only available if their availability is > 0
     * @param listings The listings the statistic should be calculated for.
     */
    protected void updateStatistic(ArrayList<AirbnbListing> listings)
    {
        long available = listings.stream().filter(property -> property.getAvailability365() > 0).count();
        statLabel.setText("" + available);
    }
}
