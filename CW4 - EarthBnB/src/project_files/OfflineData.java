package project_files;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

public class OfflineData {

    private static ArrayList<Reservation> dummyReservations = new ArrayList<>();

    /**
     *
     */
    public OfflineData(Listings listings)
    {
        generateBookings(listings);
    }

    /**
     *
     */
    private void generateBookings(Listings listings) {
        LocalDate currentDate = LocalDate.now().plusDays(365);
        Random rand = new Random();
        for (int i = 0; i < 1000; i++)
        {
            AirbnbListing l = listings.getOriginalListings().get(i);
            int reservationId = i+1;
            int daysOfStay = rand.nextInt((l.getMaximumNights() % 30) + 5);
            LocalDate departure = currentDate.minusDays(i);
            LocalDate arrival = departure.minusDays(i+daysOfStay);

            dummyReservations.add(new Reservation(reservationId, arrival, departure, 0, l.getMaxGuests(), l.getPrice() * daysOfStay, l.getId()));
        }
    }

    /**
     *
     * @return
     */
    public static ArrayList<Reservation> getDummyReservations() {
        return dummyReservations;
    }
}
