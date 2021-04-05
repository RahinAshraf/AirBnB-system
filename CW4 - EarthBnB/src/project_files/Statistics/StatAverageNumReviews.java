package project_files.Statistics;

import project_files.AirbnbListing;

import java.util.ArrayList;

/**
 * Class AverageNumReviewsStat represents the statistic calculating the average number of reviews each property has in the given set of data.
 * @author Valentin Magis
 * @version 1.0
 * @since 2021-03-11
 */
public class StatAverageNumReviews extends StatisticAsText {

    /**
     * Create an object for the statistic of the average number of reviews per property.
     * @param listings The listings to calculate the statistic for.
     */
    public StatAverageNumReviews(ArrayList<AirbnbListing> listings)
    {
        name = "Average number of reviews per property";
        updateStatistic(listings);
    }


    /**
     * Update the statistic.
     * @param listings A list of airbnb properties the statistic should be calculated for.
     */
    protected void updateStatistic(ArrayList<AirbnbListing> listings)
    {
        long average = Math.round(listings.stream().mapToInt(listing -> listing.getNumberOfReviews()).average().orElse(Double.NEGATIVE_INFINITY)); // Calculate the rounded average. If the given list is empty, the result is negative infinity
        statLabel.setText("" + average);
    }
}
