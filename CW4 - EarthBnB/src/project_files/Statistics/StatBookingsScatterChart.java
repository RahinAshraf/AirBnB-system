package project_files.Statistics;

import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
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
 * Class AccommodationTypeStat is the statistic of how many properties in the set of data are of the type "entire home and apartments"
 */
public class StatBookingsScatterChart extends Statistic {


    final NumberAxis xAxis = new NumberAxis(51.3, 51.68, 0.01); // The southern and northern boundaries of london
    final NumberAxis yAxis = new NumberAxis(-0.515, 0.29, 0.01); // The western and eastern boundaries of london
    ScatterChart<Number, Number> bookingsChart = new ScatterChart<>(xAxis, yAxis);
    XYChart.Series locationsPrivateRooms = new XYChart.Series();
    XYChart.Series locationsEntireHouse = new XYChart.Series();
    /**
     * Create an object for the statistic counting the number of entire home and apartments listed.
     */
    public StatBookingsScatterChart(ArrayList<AirbnbListing> listings)
    {
        name = "Locations of bookings";
        statistic = bookingsChart;
        bookingsChart.setVerticalZeroLineVisible(false);
        bookingsChart.setVerticalGridLinesVisible(false);
        bookingsChart.setHorizontalGridLinesVisible(false);
        bookingsChart.setHorizontalZeroLineVisible(false);
        bookingsChart.setAnimated(false);

        locationsPrivateRooms.setName("Private Room");
        locationsEntireHouse.setName("Entire house/ apartment");

        xAxis.setTickLabelsVisible(false);
        yAxis.setTickLabelsVisible(false);

        xAxis.setTickMarkVisible(false);
        yAxis.setTickMarkVisible(false);

        xAxis.setMinorTickVisible(false);
        yAxis.setMinorTickVisible(false);

        updateStatistic(listings);
    }


    /**
     * Update the statistic.
     * @param listings A list of boroughListings the statistic should be calculated for.
     * @return
     */
    protected void updateStatistic(ArrayList<AirbnbListing> listings)
    {
        locationsPrivateRooms.getData().clear();
        locationsEntireHouse.getData().clear();

        HashSet<String> bookedPropertyIds = getBookedPropertiesFromDB();
        long time = System.currentTimeMillis();
        for (String id : bookedPropertyIds)
        {
            for (int i = 0; i < listings.size(); i++)
            {
                AirbnbListing l = listings.get(i);
                if (id.equals(l.getId()))
                {
                    if (listings.get(i).getRoomType().equals("Private room"))
                        locationsPrivateRooms.getData().add(new XYChart.Data(l.getLatitude(), l.getLongitude()));
                    else
                        locationsEntireHouse.getData().add(new XYChart.Data(l.getLatitude(), l.getLongitude()));
                }
            }
        }
        bookingsChart.getData().setAll(locationsPrivateRooms, locationsEntireHouse); // Add the new data to the graph
        System.out.println(System.currentTimeMillis() - time);
    }



    private HashSet<String> getBookedPropertiesFromDB() {
        HashSet<String> bookedPropertyIds = new HashSet<>();
        try {
            DatabaseConnection connection = new DatabaseConnection();
            Connection connectDB = connection.getConnection();
            Statement statement = connectDB.createStatement();

            LocalDate currentDate = LocalDate.now();

            String checkSignup = "SELECT listingID FROM booking WHERE listingID < '" + currentDate + "'";

            ResultSet queryResult = statement.executeQuery(checkSignup);
            while (queryResult.next()) {
                bookedPropertyIds.add(queryResult.getString(1)); // Unsafe operation?
            }
        } catch (Exception e) {
        }
        System.out.println(bookedPropertyIds.size());
        return bookedPropertyIds;
    }
}
