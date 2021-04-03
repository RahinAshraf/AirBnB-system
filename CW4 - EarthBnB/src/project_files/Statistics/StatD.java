package project_files.Statistics;

import project_files.AirbnbListing;

import java.util.ArrayList;

/**
 * Class AccommodationTypeStat is the statistic of how many properties in the set of data are of the type "entire home and apartments"
 */
public class StatD extends StatisticAsText {

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
    protected void updateStatistic(ArrayList<AirbnbListing> listings)
    {
        statLabel.setText("4");
    }
}
