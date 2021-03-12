package project_files;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class will not be part of the final project, just created it to test the statistics
 */
public class StatisticsTester {

    public static void main(String[] args)
    {
        AirbnbDataLoader loader = new AirbnbDataLoader();
        ArrayList<AirbnbListing> testListing = loader.load();

        Statistic avgReview = new StatAverageNumReviews(testListing);
        Statistic properties = new StatAvailableProperties(testListing);
        Statistic accType = new StatAccommodationType(testListing);
        Statistic borough = new StatMostExpensiveBorough(testListing);

        List<Statistic> statistics = Arrays.asList(avgReview, properties, accType, borough);

        for (Statistic s : statistics)
        {
            System.out.println(s.getName() + ": " + s.getStatistic());
        }
    }
}
