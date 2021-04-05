package project_files.Statistics;

import project_files.AirbnbListing;

import java.util.ArrayList;

/**
 * Class AccommodationTypeStat is the statistic of how many properties in the set of data are of the type "entire home and apartments"
 * @author  Valentin Magis
 * @version 1.0
 * @since   2021-03-11
 */


public class StatAccommodationType extends StatisticAsText {


    /**
     * Create an object for the statistic counting the number of entire home and apartments listed.
     * @param listings The listings to calculate the statistic for.
     */
    public StatAccommodationType(ArrayList<AirbnbListing> listings)
    {
        name = "Number of entire home and apartments";
        updateStatistic(listings);
    }

    /**
     * Update the statistic and the label displaying it.
     * @param listings The listings the statistic should be calculated for.
     */
    protected void updateStatistic(ArrayList<AirbnbListing> listings)
    {
        long numHouses = listings.stream().filter(listing -> listing.getRoomType().equals("Entire home/apt")).count();
        statLabel.setText("" + numHouses);
    }
}
