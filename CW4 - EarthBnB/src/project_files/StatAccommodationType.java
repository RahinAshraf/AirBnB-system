package project_files;

import java.util.ArrayList;

/**
 * Class AccommodationTypeStat is the statistic of how many properties in the set of data are of the type "entire home and apartments"
 */
public class StatAccommodationType extends Statistic {

    /**
     * Create an object for the statistic counting the number of entire home and apartments listed.
     */
    public StatAccommodationType(ArrayList<AirbnbListing> listings)
    {
        name = "Number of entire home and apartments";
        updateStatistic(listings);
    }

    /**
     * Update the statistic.
     * @param listings A list of listings the statistic should be calculated for.
     * @return
     */
    protected String updateStatistic(ArrayList<AirbnbListing> listings)
    {
        long numHouses = listings.stream().filter(listing -> listing.getRoom_type().equals("Entire home/apt")).count();
        return "" + numHouses;
    }
}
