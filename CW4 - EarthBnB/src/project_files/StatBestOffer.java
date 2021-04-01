package project_files;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Class AccommodationTypeStat is the statistic of how many properties in the set of data are of the type "entire home and apartments"
 */
public class StatBestOffer extends Statistic {

    /**
     * Create an object for the statistic counting the number of entire home and apartments listed.
     */
    public StatBestOffer(ArrayList<AirbnbListing> listings)
    {
        name = "Best Offer!";
        updateStatistic(listings);
    }

    /**
     * Update the statistic.
     * @param listings A list of boroughListings the statistic should be calculated for.
     * @return
     */
    protected String updateStatistic(ArrayList<AirbnbListing> listings)
    {
        return "2"; // Calculate building with lowest price but highest rating in the chosen borough.

        // listings.stream()
        //         .collect(Collectors.groupingBy(AirbnbListing::getNeighbourhood, Collectors.averagingDouble(AirbnbListing::getAveragePrice)))
        //         .
    }
}
