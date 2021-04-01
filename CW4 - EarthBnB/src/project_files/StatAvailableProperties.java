package project_files;

import javafx.scene.Node;

import java.util.ArrayList;

/**
 * Class AvailablePropertiesStat is the statistic for the number of available properties in the given set of data.
 */
public class StatAvailableProperties extends StatisticAsText {

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
     * @param listings A list of boroughListings the statistic should be calculated for.
     * @return
     */
    protected Node updateStatistic(ArrayList<AirbnbListing> listings)
    {
        long available = listings.stream().filter(property -> property.getAvailability365() > 0).count();
        statLabel.setText("" + available);
        return statLabel;
    }
}
