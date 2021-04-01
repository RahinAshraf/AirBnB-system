package project_files;

import javafx.scene.Node;

import java.util.ArrayList;

/**
 * Class AverageNumReviewsStat represents the statistic calculating the average number of reviews each property has in the given set of data.
 */
public class StatAverageNumReviews extends StatisticAsText {

    /**
     * Create an object for the statistic of the average number of reviews per property.
     */
    public StatAverageNumReviews(ArrayList<AirbnbListing> listings)
    {
        name = "Average number of reviews per property";
        updateStatistic(listings);
    }


    /**
     * Update the statistic.
     * @param listings A list of airbnb properties the statistic should be calculated for.
     * @return
     */
    protected Node updateStatistic(ArrayList<AirbnbListing> listings)
    {
        long average = Math.round(listings.stream().mapToInt(listing -> listing.getNumberOfReviews()).average().orElse(Double.NEGATIVE_INFINITY)); // Calculate the rounded average. If the given list is empty, the result is negative infinity
        statLabel.setText("" + average);
        return statLabel;
    }
}
