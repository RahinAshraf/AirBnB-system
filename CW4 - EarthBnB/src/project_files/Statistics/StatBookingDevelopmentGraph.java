package project_files.Statistics;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import project_files.AirbnbListing;
import project_files.DatabaseConnection;
import project_files.Listings;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

/**
 *
 */
public class StatBookingDevelopmentGraph extends StatisticAsText {

    final private CategoryAxis xAxis = new CategoryAxis();
    final private NumberAxis yAxis = new NumberAxis();
    final private LineChart<String, Number> bookingsGraph = new LineChart<>(xAxis, yAxis);
    final private XYChart.Series weeklyBookingsBelowReviewMedian = new XYChart.Series();
    final private XYChart.Series weeklyBookingsAboveReviewMedian = new XYChart.Series();
    /**
     * Create an object for the statistic counting the number of entire home and apartments listed.
     */
    public StatBookingDevelopmentGraph(ArrayList<AirbnbListing> listings)
    {
        name = "Development of Bookings";
        statistic = bookingsGraph;
        bookingsGraph.setAnimated(false); // Animation is buggy.
        xAxis.setLabel("Time");
        yAxis.setLabel("Bookings");
        weeklyBookingsBelowReviewMedian.setName("Bookings Below Review Median");
        weeklyBookingsAboveReviewMedian.setName("Bookings Above Review Median");
        updateStatistic(listings);
    }

    /**
     * Update the statistic.
     * @param listings A list of boroughListings the statistic should be calculated for.
     * @return
     */
    protected void updateStatistic(ArrayList<AirbnbListing> listings)
    {
        weeklyBookingsBelowReviewMedian.getData().clear(); // empty bookings
        weeklyBookingsAboveReviewMedian.getData().clear();

        HashSet<IdDatePair> allBookings = getArrivalDateFromDB();
        ArrayList<IdDatePair> belowMedianPairs = new ArrayList<>();
        ArrayList<IdDatePair> aboveMedianPairs = new ArrayList<>();

        double avgPriceMedian = calculateMedian(listings);

        // Sort the pairs to above and below median
        for (IdDatePair pair : allBookings) {
            AirbnbListing listing = Listings.iterativeSearch(listings, pair.getId());
            if (listing != null && listing.getAveragePrice() < avgPriceMedian) // Check whether the booking was contained in the currently displayed list and then where it belongs
               belowMedianPairs.add(pair);
            else
               aboveMedianPairs.add(pair);
        }

        // Count the occurrences
        Map<YearMonth, Long> belowCounter = belowMedianPairs.stream()
                .collect(Collectors.groupingBy(IdDatePair::getYearMonth, Collectors.counting()));

        Map<YearMonth, Long> aboveCounter = aboveMedianPairs.stream()
                .collect(Collectors.groupingBy(IdDatePair::getYearMonth, Collectors.counting()));

        // Populate the series
        for (YearMonth ym : belowCounter.keySet()) {
            weeklyBookingsBelowReviewMedian.getData().add(new XYChart.Data(ym.toString(), belowCounter.get(ym)));
        }

        for (YearMonth ym : aboveCounter.keySet()) {
            weeklyBookingsAboveReviewMedian.getData().add(new XYChart.Data(ym.toString(), aboveCounter.get(ym)));
        }


        // Add the series and display it
        bookingsGraph.getData().setAll(weeklyBookingsBelowReviewMedian, weeklyBookingsAboveReviewMedian);
    }

    /**
     * Calculate the median of the average price of the given listings.
     * Reference: https://stackoverflow.com/questions/43667989/finding-the-median-value-from-a-list-of-objects-using-java-8
     * @param listings
     * @return
     */
    private double calculateMedian(ArrayList<AirbnbListing> listings) {
        DoubleStream sortedPrices = listings.stream().mapToDouble(AirbnbListing::getAveragePrice).sorted();
        double median = listings.size() % 2 == 0 ?
                sortedPrices.skip(listings.size()/2-1).limit(2).average().getAsDouble():
                sortedPrices.skip(listings.size()/2).findFirst().getAsDouble();
        return median;
    }

    private HashSet<IdDatePair> getArrivalDateFromDB() {

        HashSet<IdDatePair> allBookings = new HashSet<>();

        try {
            DatabaseConnection connection = new DatabaseConnection();
            Connection connectDB = connection.getConnection();
            Statement statement = connectDB.createStatement();

            String bookingsQuery = "SELECT listingID, Arrival FROM booking";

            ResultSet queryResult = statement.executeQuery(bookingsQuery);
            while (queryResult.next()) {
                allBookings.add(new IdDatePair(queryResult.getString(1), YearMonth.from(queryResult.getDate(2).toLocalDate())));
            }
        } catch (Exception e) {
        }
        return allBookings;
    }



    /**
     * Maybe implement Comparable and use treeSet?
     */
    private class IdDatePair {
        private String id;
        private YearMonth yearMonth;

        public IdDatePair(String id, YearMonth yearMonth)
        {
            this.id = id;
            this.yearMonth = yearMonth;
        }

        public String getId() {
            return id;
        }

        public YearMonth getYearMonth() {
            return yearMonth;
        }
    }
}

// Statistic: Development of amount of bookings per week. Two lines: one above and one below median of price
// group by week

/*
        for (IdDatePair pair : allBookings)
        {
            AirbnbListing listing = Listings.iterativeSearch(listings, pair.getId());
            if (listing.getAveragePrice() < avgPriceMedian) {
                if (yearMonthCounterBelow.get(pair.getYearMonth()) == null) { // Returns null if key doesn't exist yet
                    yearMonthCounterBelow.put(pair.getYearMonth(), new MutableInt(0));
                }
                else {
                    yearMonthCounterBelow.get(pair.getYearMonth()).increment();
                }
            }
            else {
                if (yearMonthCounterAbove.get(pair.getYearMonth()) == null) { // Returns null if key doesn't exist yet
                    yearMonthCounterAbove.put(pair.getYearMonth(), new MutableInt(0));
                }
                else {
                    yearMonthCounterAbove.get(pair.getYearMonth()).increment();
                }
            }
        }

         */