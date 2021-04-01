package project_files;

import javafx.scene.Node;
import javafx.scene.chart.ScatterChart;

import java.util.ArrayList;

/**
 * Class AccommodationTypeStat is the statistic of how many properties in the set of data are of the type "entire home and apartments"
 */
public class StatBookingsScatterChart extends Statistic {

    ScatterChart bookingsChart;

    /**
     * Create an object for the statistic counting the number of entire home and apartments listed.
     */
    public StatBookingsScatterChart(ArrayList<AirbnbListing> listings)
    {
        name = "Luxurious Offer!";
        updateStatistic(listings);
    }


    /**
     * Update the statistic.
     * @param listings A list of boroughListings the statistic should be calculated for.
     * @return
     */
    protected Node updateStatistic(ArrayList<AirbnbListing> listings)
    {
        return bookingsChart;
    }
}
