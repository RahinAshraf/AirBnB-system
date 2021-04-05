package project_files;

import java.time.LocalDate;
import java.util.ArrayList;

public class OfflineData {

    private static ArrayList<Reservation> dummyReservations = new ArrayList<>();

    public OfflineData(Listings listings)
    {
        generateBookings(listings);
    }

    private void generateBookings(Listings listings) {
        LocalDate currentDate = LocalDate.now();

        for (int i = 0; i < 1000; i++)
        {
            AirbnbListing listing = listings.getOriginalListings().get(i);
            int reservationId = i+1;
            LocalDate departure = currentDate.minusDays(i);
            LocalDate arrival = departure.minusDays(listing.getMaximumNights());
            dummyReservations.add(new Reservation(reservationId, arrival, departure, 0, listing.getMaxGuests(), listing.getPrice() * listing.getMaximumNights(), listing.getId()));
        }
    }

    public static ArrayList<Reservation> getDummyReservations() {
        return dummyReservations;
    }
}
