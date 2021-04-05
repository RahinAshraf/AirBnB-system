package project_files.Statistics;

import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import project_files.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 *
 */
public class StatBookingDevelopmentGraph extends StatisticAsText {

    final private CategoryAxis xAxis = new CategoryAxis();
    final private NumberAxis yAxis = new NumberAxis();
    final private LineChart<String, Number> bookingsGraph = new LineChart<>(xAxis, yAxis);
    final private XYChart.Series monthlySuperhostBookings = new XYChart.Series();
    final private XYChart.Series monthlyNonSuperhostBookings = new XYChart.Series();
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
        monthlySuperhostBookings.setName("Bookings for Superhosts");
        monthlyNonSuperhostBookings.setName("Bookings for non-Superhosts");
        updateStatistic(listings);
    }

    /**
     * Update the statistic.
     * @param listings A list of boroughListings the statistic should be calculated for.
     * @return
     */
    protected void updateStatistic(ArrayList<AirbnbListing> listings)
    {
        monthlySuperhostBookings.getData().clear(); // empty bookings
        monthlyNonSuperhostBookings.getData().clear();

        HashSet<IdDatePair> allBookings = getArrivalDates();
        ArrayList<IdDatePair> superhostPairs = new ArrayList<>();
        ArrayList<IdDatePair> notSuperhostPairs = new ArrayList<>();

        // Sort the pairs to above and below median
        for (IdDatePair pair : allBookings) {
            AirbnbListing listing = Listings.iterativeSearch(listings, pair.getId());
            if (listing != null) {
                if (listing.isHostSuperhost()) { // Check whether the booking was contained in the currently displayed list and then where it belongs
                    superhostPairs.add(pair);
                } else {
                    notSuperhostPairs.add(pair);
                }
            }
        }

        // Count the occurrences and put them in a treemap sorted by the year and month.
        TreeMap<YearMonth, Long> superhostCounter = superhostPairs.stream()
                .collect(Collectors.groupingBy(IdDatePair::getYearMonth, TreeMap::new, Collectors.counting()));

        TreeMap<YearMonth, Long> notSuperhostCounter = notSuperhostPairs.stream()
                .collect(Collectors.groupingBy(IdDatePair::getYearMonth, TreeMap::new, Collectors.counting()));

        // Populate the series
        for (Map.Entry<YearMonth, Long> entry : superhostCounter.entrySet()) {
            monthlySuperhostBookings.getData().add(new XYChart.Data(entry.getKey().toString(), entry.getValue()));
        }

        for (Map.Entry<YearMonth, Long> entry : notSuperhostCounter.entrySet()) {
            monthlyNonSuperhostBookings.getData().add(new XYChart.Data(entry.getKey().toString(), entry.getValue()));
        }

        // Add the series and display it
        bookingsGraph.getData().setAll(monthlySuperhostBookings, monthlyNonSuperhostBookings);
    }

    private HashSet<IdDatePair> getArrivalDates() {
        HashSet<IdDatePair> allBookings = new HashSet<>();

        if (!MainFrameController.isUsingDatabase()) {
            for (Reservation r : OfflineData.getDummyReservations())
            {
                allBookings.add(new IdDatePair(r.getListingID(), YearMonth.from(r.getArrival())));
            }
        }

        else {
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