package project_files.Statistics;

import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import project_files.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Class StatsBookingsScatterChart - Creates a graph showing the locations of listings booked on a scatterchart.
 * Differentiates between entire homes and private rooms. Uses latitude and longitude to display the exact location.
 * @author Valentin Magis
 * @version 1.0
 * @since 2021-03-11
 */
public class StatBookingsScatterChart extends Statistic {

    final NumberAxis yAxis = new NumberAxis(51.3, 51.68, 0.01); // The southern and northern boundaries of london
    final NumberAxis xAxis = new NumberAxis(-0.515, 0.29, 0.01); // The western and eastern boundaries of london
    ScatterChart<Number, Number> bookingsChart = new ScatterChart<>(xAxis, yAxis);
    XYChart.Series locationsPrivateRooms = new XYChart.Series();
    XYChart.Series locationsEntireHouse = new XYChart.Series();

    /**
     * Create an object for the statistic counting the number of entire home and apartments listed, initialize the looks.
     * @param listings The listings the statistic should initially be calculated for.
     */
    public StatBookingsScatterChart(ArrayList<AirbnbListing> listings)
    {
        name = "Locations of all bookings";
        statistic = bookingsChart;
        bookingsChart.setVerticalZeroLineVisible(false);
        bookingsChart.setVerticalGridLinesVisible(false);
        bookingsChart.setHorizontalGridLinesVisible(false);
        bookingsChart.setHorizontalZeroLineVisible(false);
        bookingsChart.setAnimated(false);

        locationsPrivateRooms.setName("Private Room");
        locationsEntireHouse.setName("Entire house/ apartment");

        xAxis.setLabel("Longitude");
        yAxis.setLabel("Latitude");

        xAxis.setTickLabelsVisible(true);
        yAxis.setTickLabelsVisible(true);

        xAxis.setTickMarkVisible(true);
        yAxis.setTickMarkVisible(true);

        xAxis.setMinorTickVisible(false);
        yAxis.setMinorTickVisible(false);

        updateStatistic(listings);
    }


    /**
     * Update the statistic for a new list of AirbnbListings
     * @param listings The listings the statistic should be calculated for.
     */
    protected void updateStatistic(ArrayList<AirbnbListing> listings)
    {
        // Clear the data from the last update
        locationsPrivateRooms.getData().clear();
        locationsEntireHouse.getData().clear();
        if (!listings.isEmpty()) {
            HashSet<String> bookedPropertyIds = getBookedProperties();
            for (String id : bookedPropertyIds) {
                AirbnbListing listing = Listings.iterativeSearch(listings, id);
                if (listing != null) {
                    if (listing.getRoomType().equals("Private room"))
                        locationsPrivateRooms.getData().add(new XYChart.Data(listing.getLongitude(),listing.getLatitude()));
                    else
                        locationsEntireHouse.getData().add(new XYChart.Data(listing.getLongitude(), listing.getLatitude()));
                }
            }
        }
        bookingsChart.getData().setAll(locationsPrivateRooms, locationsEntireHouse); // Add the new data to the graph
    }


    /**
     * Get the ids of all properties which have been booked.
     * @return A set of all booked properties. (Using a set to avoid duplicates since every booked listing is only displayed once regardless of how many times it was booked)
     */
    private HashSet<String> getBookedProperties() {
        HashSet<String> bookedPropertyIds = new HashSet<>();

        if (!MainFrameController.isUsingDatabase()) {
            for (Reservation r : OfflineData.getReservations())
            {
                bookedPropertyIds.add(r.getListingID());
            }
        }

        else {
            try {
                DatabaseConnection connection = new DatabaseConnection();
                Connection connectDB = connection.getConnection();
                Statement statement = connectDB.createStatement();

                String checkSignup = "SELECT listingID FROM booking";

                ResultSet queryResult = statement.executeQuery(checkSignup);
                while (queryResult.next()) {
                    bookedPropertyIds.add(queryResult.getString(1)); // Unsafe operation?
                }
            } catch (Exception e) {
            }
        }
        return bookedPropertyIds;
    }
}