package project_files;

import java.util.ArrayList;

/**
 * Class AvailablePropertiesStat is the statistic for the number of available properties in the given set of data.
 */
public class StatAvailableProperties extends Statistic {

    /**
     * Create a statistic object for the number of available properties. Starts with a default database.
     */
    public StatAvailableProperties(ArrayList<AirbnbListing> listings)
    {
        name = "Number of available properties";
        updateStatistic(listings);
    }

    /**
     * Update the statistic. Properties are only available if their availability is > 0
     * @param listings A list of listings the statistic should be calculated for.
     */
    public void updateStatistic(ArrayList<AirbnbListing> listings)
    {
        long available = listings.stream().filter(property -> property.getAvailability365() > 0).count();
        statistic = "" + available;
    }
}
