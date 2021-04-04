package project_files.Statistics;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import project_files.AirbnbListing;
import project_files.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

/**
 *
 */
public class StatBookingDevelopmentGraph extends StatisticAsText {

    final private NumberAxis xAxis = new NumberAxis();
    final private NumberAxis yAxis = new NumberAxis();
    final private LineChart<Number, Number> bookingsGraph = new LineChart<>(xAxis, yAxis);
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
        //Thread thread = new Thread(this);
        //thread.start();
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

        HashSet allBookings = getArrivalDateFromDB();
        // get the airbnblisting
        // calculate the price median
        // separate the listings accordingly
        // group by week & then count each week
        // add the weeks with week nr and amount to series

        //dailyBookings.getData().add(new XYChart.Data("Week", count));
        bookingsGraph.getData().setAll(weeklyBookingsBelowReviewMedian, weeklyBookingsAboveReviewMedian);
    }

    private HashSet<idDatePair> getArrivalDateFromDB() {

        HashSet<idDatePair> allBookings = new HashSet<>();

        try {
            DatabaseConnection connection = new DatabaseConnection();
            Connection connectDB = connection.getConnection();
            Statement statement = connectDB.createStatement();

            String bookingsQuery = "SELECT listingID, createdDate FROM booking";

            ResultSet queryResult = statement.executeQuery(bookingsQuery);
            while (queryResult.next()) {
                allBookings.add(new idDatePair(queryResult.getString(1), queryResult.getDate(2).toLocalDate()));
            }
        } catch (Exception e) {
        }
        //System.out.println(bookedPropertyIds.size());
        return allBookings;
    }

    /**
     * Maybe implement Comparable and use treeSet?
     */
    private class idDatePair {
        String id;
        LocalDate date;

        public idDatePair(String id, LocalDate date)
        {
            this.id = id;
            this.date = date;
        }

        public String getId() {
            return id;
        }
    }
}

// Statistic: Development of amount of bookings per week. Two lines: one above and one below median of price
// group by week
