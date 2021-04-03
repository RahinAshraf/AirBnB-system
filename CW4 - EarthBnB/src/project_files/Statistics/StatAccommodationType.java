package project_files.Statistics;

import project_files.AirbnbListing;

import java.util.ArrayList;

/**
 * Class AccommodationTypeStat is the statistic of how many properties in the set of data are of the type "entire home and apartments"
 */
public class StatAccommodationType extends StatisticAsText {


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
     * @param listings A list of boroughListings the statistic should be calculated for.
     * @return
     */
    protected void updateStatistic(ArrayList<AirbnbListing> listings)
    {
        long numHouses = listings.stream().filter(listing -> listing.getRoomType().equals("Entire home/apt")).count();
        statLabel.setText("" + numHouses);
    }
}
