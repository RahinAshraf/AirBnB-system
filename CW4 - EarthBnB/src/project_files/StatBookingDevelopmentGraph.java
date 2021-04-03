package project_files;

import javafx.collections.FXCollections;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;

/**
 *
 */
public class StatBookingDevelopmentGraph extends StatisticAsText {

    final private NumberAxis xAxis = new NumberAxis();
    final private NumberAxis yAxis = new NumberAxis();
    final private LineChart<Number, Number> bookingsGraph = new LineChart<>(xAxis, yAxis);
    final private XYChart.Series dailyBookings = new XYChart.Series();
    /**
     * Create an object for the statistic counting the number of entire home and apartments listed.
     */
    public StatBookingDevelopmentGraph(ArrayList<AirbnbListing> listings)
    {
        name = "Development of Bookings";
        statistic = bookingsGraph;
        xAxis.setLabel("Time");
        yAxis.setLabel("Bookings");
        dailyBookings.setName("Daily Bookings");
        updateStatistic(listings);
    }

    /**
     * Update the statistic.
     * @param listings A list of boroughListings the statistic should be calculated for.
     * @return
     */
    protected void updateStatistic(ArrayList<AirbnbListing> listings)
    {
        bookingsGraph.getData().clear();
        dailyBookings.setData(FXCollections.observableArrayList()); // empty bookings
        // get all bookings, group by timestamp.getDay, count and
        //dailyBookings.getData().add(new XYChart.Data("Week", count));
        bookingsGraph.getData().addAll(dailyBookings);
    }
}
