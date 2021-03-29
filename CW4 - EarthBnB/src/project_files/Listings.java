package project_files;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Listings {


    private ArrayList<AirbnbListing> originalListings;
    private ArrayList<AirbnbListing> listingsFilteredByBookingData;
    private ArrayList<AirbnbListing> filteredListings = new ArrayList<>();

    public Listings(ArrayList<AirbnbListing> originalListings)
    {
        this.originalListings = originalListings;
        filteredListings.addAll(originalListings);
    }

    public ArrayList<AirbnbListing> getFilteredListings()
    {
        return filteredListings;
    }

    /**
     * Filter for entered booking data.
     * Checked values are: Minimum and maximum nights, number of guests, price range, availability according to the database.
     * @param bookingData
     * @return
     */
    public ArrayList<AirbnbListing> filterBookingData(BookingData bookingData)
    {
        listingsFilteredByBookingData = originalListings.stream()
                .filter(l -> l.getMinimumNights() <= bookingData.getDaysOfStay() && l.getMaximumNights() >= bookingData.getDaysOfStay())
                .filter(l -> l.getMaxGuests() >= bookingData.getNumberOfPeople())
                .filter(l -> l.getPrice() >= bookingData.getMinPrice() && l.getPrice() <= bookingData.getMaxPrice())
                .collect(Collectors.toCollection(ArrayList::new));

        //filterDates(bookingData.getCheckIn(), bookingData.getCheckOut()); // Checked through the database. Filter from and store in listingsFilteredByBookingData
        filteredListings.clear();
        filteredListings.addAll(listingsFilteredByBookingData);
        return filteredListings;
    }


    private void filterDates(LocalDate checkIn, LocalDate checkOut) {
        // @Barni
    }

    /**
     * Separate method so the user can change price range at any time without having filter for booking data again.
     * @param minPrice
     * @param maxPrice
     */
    public void filterPriceRange(int minPrice, int maxPrice)
    {
        filteredListings = listingsFilteredByBookingData.stream()
                .filter(l -> l.getPrice() >= minPrice && l.getPrice() <= maxPrice)
                .collect(Collectors.toCollection(ArrayList::new));
    }


}
