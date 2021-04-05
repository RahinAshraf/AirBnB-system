package project_files;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

/**
 * Class OfflineData - Mimics the database instead of the online-version in case the user chooses to run the program without a database connection.
 * This was added to ensure marking without any database related problems.
 * @author Valentin Magis
 * @version 1.0
 * @since 2021-03-11
 */
public class OfflineData {

    private static ArrayList<Reservation> reservations = new ArrayList<>(); // The reservations that were made by the user during the session.
    private static ArrayList<Account> offlineAccounts = new ArrayList<>(); // The offline accounts that have been created since the start of the program.

    /**
     * Generate offline bookings to "populate the database"
     */
    public OfflineData(Listings listings)
    {
        generateBookings(listings);
    }

    private void generateBookings(Listings listings) {
        LocalDate currentDate = LocalDate.now().plusDays(365);
        Random rand = new Random();
        for (int i = 0; i < 2500; i++)
        {
            AirbnbListing l = listings.getOriginalListings().get(i);
            int reservationId = i+1;
            int daysOfStay = rand.nextInt((l.getMaximumNights() % 30) + 5);
            LocalDate departure = currentDate.minusDays(i);
            LocalDate arrival = departure.minusDays(i+daysOfStay);

            reservations.add(new Reservation(reservationId, arrival, departure, 0, l.getMaxGuests(), l.getPrice() * daysOfStay, l.getId()));
        }
    }

    /**
     * Return all reservations that have been made in the offline session.
     * @return The reservations.
     */
    public static ArrayList<Reservation> getReservations() {
        return reservations;
    }

    /**
     * Get all accounts that have been created in the offline session.
     * @return The accounts.
     */
    public static ArrayList<Account> getAccounts()
    {
        return offlineAccounts;
    }

    /**
     * Add a reservation to the list of reservations.
     * @param reservation The reservation to be added.
     */
    public static void addReservation(Reservation reservation)
    {
        reservations.add(reservation);
    }
}
