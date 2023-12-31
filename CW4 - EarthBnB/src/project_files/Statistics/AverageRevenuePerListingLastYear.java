package project_files.Statistics;

import project_files.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Class AverageDailyRevenuePerListing - Calculate the average revenue the selected listings generated in the last year.
 * @author Valentin Magis
 * @version 1.0
 * @since 2021-03-11
 */
public class AverageRevenuePerListingLastYear extends StatisticAsText {

    /**
     * Create an object for the statistic counting the number of entire home and apartments listed.
     */
    public AverageRevenuePerListingLastYear(ArrayList<AirbnbListing> listings)
    {
        name = "The average revenue generated by each listing last year";
        updateStatistic(listings);
    }

    /**
     * Update the statistic.
     * @param listings A list of boroughListings the statistic should be calculated for.
     */
    protected void updateStatistic(ArrayList<AirbnbListing> listings)
    {
        String result = "No properties fit Your search.";
        if (!listings.isEmpty()) {
            listings.sort(AirbnbListing::compareTo); // Make sure the listings are sorted for the iterative search.
            HashSet<BookingIDDuration> lastYearBookings = getBookedProperties();
            HashSet<String> bookedListings = new HashSet<>(); // contains all listings that a booking was made for. Used as a count.
            int revenueSum = 0; // The accumulated revenue generated by the bookings
            for (BookingIDDuration booking : lastYearBookings) {
                AirbnbListing listing = Listings.binarySearch(listings, booking.getId());
                if (listing != null) {
                    bookedListings.add(listing.getId());
                    revenueSum += listing.getPrice() * booking.getDuration();
                }
            }
            if (bookedListings.size() > 0) {// Safety check so there is no division by 0
                result = "" + revenueSum / bookedListings.size() + "$"; // Calculate the average revenue
            }
        }
        statLabel.setText(result);
    }

    /**
     * Get the properties that were booked last year. Either returns the bookings from the online database or the generated offline bookings.
     * @return The bookings from last year.
     */
    private HashSet<BookingIDDuration> getBookedProperties() {
        HashSet<BookingIDDuration> lastYearBookings = new HashSet<>();
        LocalDate currentDate = LocalDate.now();

        if (!MainFrameController.isUsingDatabase()) {
            for (Reservation r : OfflineData.getReservations())
            {
                if (r.getArrival().isBefore(currentDate) && r.getArrival().isAfter(currentDate.minusDays(365)) // Filter for stays within the past year
                        && r.getDeparture().isBefore(currentDate) && r.getDeparture().isAfter(currentDate.minusDays(365)))
                lastYearBookings.add(new BookingIDDuration(r.getListingID(), getDuration(r.getArrival(), r.getDeparture())));
            }
        }

        else {
            try {
                DatabaseConnection connection = new DatabaseConnection();
                Connection connectDB = connection.getConnection();
                Statement statement = connectDB.createStatement();
                String checkSignup = "SELECT listingID, Arrival, Departure FROM booking " +
                        "WHERE Arrival <'" + currentDate + "' AND Arrival > '" + currentDate.minusDays(365) + "' AND Departure < '" + currentDate + "' AND Departure > '" + currentDate.minusDays(365) + "'";

                ResultSet queryResult = statement.executeQuery(checkSignup);
                while (queryResult.next()) {
                    long duration = getDuration(queryResult.getDate(2).toLocalDate(), queryResult.getDate(3).toLocalDate());
                    lastYearBookings.add(new BookingIDDuration(queryResult.getString(1), duration));
                }
            } catch (Exception e) { e.printStackTrace();}
        }
        return lastYearBookings;
    }

    /**
     * Calculate the duration between two days.
     * @param arrival The arrival date.
     * @param departure The departure date.
     * @return The number of days between arrival and departure.
     */
    private long getDuration(LocalDate arrival, LocalDate departure) {
        return ChronoUnit.DAYS.between(arrival, departure);
    }

    /**
     * Nested class BoookindIDDuration - Stores the information of the duration of the bookings in combination with their id.
     * Used as datatype for then calculating the statistic.
     */
    private class BookingIDDuration
    {
        private String id;
        private long duration;

        /**
         * Create a bookingIDDUration
         * @param id The id of the listing
         * @param duration The duration of the associated stay.
         */
        public BookingIDDuration(String id, long duration)
        {
            this.duration = duration;
            this.id = id;
        }

        public String getId() {
            return id;
        }

        public long getDuration() {
            return duration;
        }
    }
}
