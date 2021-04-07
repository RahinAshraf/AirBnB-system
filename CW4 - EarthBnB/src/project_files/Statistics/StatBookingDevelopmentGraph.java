package project_files.Statistics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import project_files.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class StatBookingDevelopmentGraph - Creates a Linegraph of the all monthly bookings of all users,
 * either from the online database or from the offline generated dummy data.
 *
 * Differentiates between bookings of listings where the host is a superhost and where he is not.
 * @author Valentin Magis
 * @version 1.0
 * @since 2021-03-11
 */
public class StatBookingDevelopmentGraph extends StatisticAsText {

    final private CategoryAxis xAxis = new CategoryAxis(); // Months
    final private NumberAxis yAxis = new NumberAxis(); // Number of bookings
    final private LineChart<String, Number> bookingsGraph = new LineChart<>(xAxis, yAxis);
    final private XYChart.Series monthlySuperhostBookings = new XYChart.Series();
    final private XYChart.Series monthlyNonSuperhostBookings = new XYChart.Series();
    /**
     * Create an object for the statistic and create the first graph.
     * @param listings The listings to create the graph for.
     */
    public StatBookingDevelopmentGraph(ArrayList<AirbnbListing> listings)
    {
        name = "Development of Bookings";
        statistic = bookingsGraph;
        bookingsGraph.setAnimated(false); // JavaFX live-animation is buggy.
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
        // empty bookings
        monthlySuperhostBookings.getData().clear();
        monthlyNonSuperhostBookings.getData().clear();

        HashSet<IdDatePair> allBookings = getArrivalDates();
        ArrayList<IdDatePair> superhostPairs = new ArrayList<>();
        ArrayList<IdDatePair> notSuperhostPairs = new ArrayList<>();
        listings.sort(AirbnbListing::compareTo); // Make sure listings are in order for the following binary search.
        // Sort the pairs to superhost and not-superhost
        for (IdDatePair pair : allBookings) {
            AirbnbListing listing = Listings.binarySearch(listings, pair.getId());
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

        // Populate the series utilizing the sorting mechanism of the tree map
        for (Map.Entry<YearMonth, Long> entry : superhostCounter.entrySet()) {
            monthlySuperhostBookings.getData().add(new XYChart.Data(entry.getKey().toString(), entry.getValue()));
        }

        for (Map.Entry<YearMonth, Long> entry : notSuperhostCounter.entrySet()) {
            monthlyNonSuperhostBookings.getData().add(new XYChart.Data(entry.getKey().toString(), entry.getValue()));
        }

        // Add both series
        bookingsGraph.getData().setAll(monthlySuperhostBookings, monthlyNonSuperhostBookings);

        setCategoriesManually(superhostCounter, notSuperhostCounter);
    }

    /**
     * Implemented because the automatic ordering of the LineChart is buggy.
     * Solution suggested by Galya, https://stackoverflow.com/questions/19264919/javafx-linechart-areachart-wrong-sorted-values-on-x-axis-categoryaxis
     * @param superhostCounter The counter of the superhost.
     * @param notSuperhostCounter The counter of the nonSuperhosts
     */
    private void setCategoriesManually(TreeMap<YearMonth, Long> superhostCounter, TreeMap<YearMonth, Long> notSuperhostCounter) {
        superhostCounter.putAll(notSuperhostCounter);
        CategoryAxis xAxis = (CategoryAxis) bookingsGraph.getXAxis();
        ObservableList<String> months = superhostCounter.keySet().stream().map(s -> s.toString()).collect(Collectors.toCollection(FXCollections::observableArrayList));
        Collections.sort(months);
        xAxis.setAutoRanging(true);
        xAxis.setCategories(months);
    }

    /**
     * Return the arrival dates and listingsId of all Bookings, either from offline or from the database.
     * @return A set of IDDatePair specifying all bookings that have been made in the system.
     */
    private HashSet<IdDatePair> getArrivalDates() {
        HashSet<IdDatePair> allBookings = new HashSet<>();
        if (!MainFrameController.isUsingDatabase()) {
            for (Reservation r : OfflineData.getReservations()) {
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
     * Nested class IdDatePair - Specifies the datatype needed tp store a list of ids connected to the month their booking was made.
     */
    private class IdDatePair {
        private String id;
        private YearMonth yearMonth;

        /**
         * Create a pair of an ID and a Date.
         * @param id The id of the listing in the database
         * @param yearMonth The year and month this listing was booked in.
         */
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