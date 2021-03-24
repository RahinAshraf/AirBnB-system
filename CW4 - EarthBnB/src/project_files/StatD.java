package project_files;

import java.util.ArrayList;

/**
 * Class AccommodationTypeStat is the statistic of how many properties in the set of data are of the type "entire home and apartments"
 */
public class StatD extends Statistic {

    /**
     * Create an object for the statistic counting the number of entire home and apartments listed.
     */
    public StatD(ArrayList<AirbnbListing> listings)
    {
        name = "D";
        updateStatistic(listings);
    }

    /**
     * Update the statistic.
     * @param listings A list of boroughListings the statistic should be calculated for.
     * @return
     */
    protected String updateStatistic(ArrayList<AirbnbListing> listings)
    {
        return "4"; //used to get it to run - need changing

    }
}
