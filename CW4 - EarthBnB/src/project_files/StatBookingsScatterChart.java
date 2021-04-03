package project_files;

import javafx.collections.FXCollections;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

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

        locationsPrivateRooms.setName("Private Room");
        locationsEntireHouse.setName("Entire house/ apartment");
        /*
        try {
            Image image = new Image(new FileInputStream("C:\\Users\\gunny1\\OneDrive\\KCL\\Courses\\Y1T2\\PPA\\CW4\\CW4 - EarthBnB\\src\\project_files\\londonMap.png"));
            bookingsChart.setBackground(new Background(new BackgroundImage(image, null, null, null, null)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } */
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
        bookingsChart.getData().clear(); // Clear the chart
        locationsPrivateRooms.setData(FXCollections.observableArrayList());
        locationsEntireHouse.setData(FXCollections.observableArrayList()); // clear
        /*
        XYChart.Series locationsPrivateRooms = new XYChart.Series();
        locationsPrivateRooms.setName("Private Room");
        XYChart.Series locationsEntireHouse = new XYChart.Series();
        locationsEntireHouse.setName("Entire house/ apartment");

         */
        for (int i = 0; i < listings.size() * 0.1 ; i++) // Should be .size and run in thread
        {
            AirbnbListing l = listings.get(i);
            if (l.getRoomType().equals("Private room"))
            {
                locationsPrivateRooms.getData().add(new XYChart.Data(l.getLatitude(), l.getLongitude()));
            }else
                locationsEntireHouse.getData().add(new XYChart.Data(l.getLatitude(), l.getLongitude()));
        }

        //Query request


        try {
            DatabaseConnection connection = new DatabaseConnection();
            Connection connectDB = connection.getConnection();
            Statement statement = connectDB.createStatement();

            LocalDate currentDate = LocalDate.now();

            String checkSignup = "SELECT listingID FROM booking WHERE listingID < '" + currentDate + "'";

            ResultSet queryResult = statement.executeQuery(checkSignup);
            while (queryResult.next()) {
                // do something with the ID (queryResult is an ID integer)
            }
        } catch (Exception e) {

        }

        // Two queries: booking of private room and entire house / apartment. Maybe only query for changes, faster.
        //get array and then .longitude to series.getData.add(new XYChart.Data(lat, long));

        bookingsChart.getData().addAll(locationsPrivateRooms, locationsEntireHouse); // Add the new data to the graph
    }

}
