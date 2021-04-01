package project_files;

import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;

/**
 * Class AccommodationTypeStat is the statistic of how many properties in the set of data are of the type "entire home and apartments"
 */
public class StatBookingsScatterChart extends Statistic {


    final NumberAxis xAxis = new NumberAxis("Latitude", 0, 10, 1);
    final NumberAxis yAxis = new NumberAxis("Longitude", 0, 10, 1); // FIND OUT NUMBERS
    ScatterChart<Number, Number> bookingsChart = new ScatterChart<>(xAxis, yAxis);

    /**
     * Create an object for the statistic counting the number of entire home and apartments listed.
     */
    public StatBookingsScatterChart(ArrayList<AirbnbListing> listings)
    {

        name = "Locations of bookings";
        updateStatistic(listings);
    }


    /**
     * Update the statistic.
     * @param listings A list of boroughListings the statistic should be calculated for.
     * @return
     */
    protected Node updateStatistic(ArrayList<AirbnbListing> listings)
    {
        bookingsChart.getData().removeAll(); // Clear the chart
        XYChart.Series locationsPrivateRooms = new XYChart.Series();
        locationsPrivateRooms.setName("Private Room");
        XYChart.Series locationsEntireHouse = new XYChart.Series();
        locationsEntireHouse.setName("Entire house/ apartment");

        // Two queries: booking of private room and entire house / apartment. Maybe only query for changes, faster.
        //get array and then .longitude to series.getData.add(new XYChart.Data(lat, long));

        bookingsChart.getData().addAll(locationsPrivateRooms, locationsEntireHouse); // Add the new data to the graph
        return bookingsChart;
    }
}
