package project_files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.stream.Collectors;

/**
 * Class Listings - Performs and stores every manipulation of the dataset.
 *
 * A filter-chain ensures a reduction of the amount of performed filter-calculations.
 * The filters usually changed the least are first in the chain to not be filtered for again when filters at the end of the chain are changed.
 * The order of the filter is:
 * Booking data --> Price Range --> Boroughs --> Checkbox filters (Wifi,...)
 *
 * The filters are invoked by the previous filters or by a public changeXY method.
 *
 * @version 01-04-2021
 * @author Valentin Magis
 */

public class Listings {

    // The different stages the listing can take. Stored so not every filter needs to be applied newly in case something is changed.
    private static ArrayList<AirbnbListing> originalListings;
    private static ArrayList<AirbnbListing> listingsFilteredByBookingData = new ArrayList<>();
    private static ArrayList<AirbnbListing> listingsFilteredByPrice = new ArrayList<>();
    private static ArrayList<AirbnbListing> listingsFilteredBySelectedBoroughs = new ArrayList<>();
    private static ArrayList<AirbnbListing> listingsFilteredByCheckboxes = new ArrayList<>();
    private static ArrayList<AirbnbListing> filteredListings = new ArrayList<>();

    // The filters that can be applied.
    private static BookingData bookingData; // Checkin, checkout, number of people
    private static int[] priceRange = new int[2]; // Min, max
    private static ArrayList<String> selectedBoroughs = new ArrayList<>(); // The boroughs the user has searched for. Does not affect contents of main window.
    private static HashSet<FilterNames> activeFilters = new HashSet<>(); // Wifi, Pool, Superhost, Private Room


    /**
     * Create a new Listings object. Initializes the different lists so the user can add any filter at the beginning.
     * @param originalListings
     */
    public Listings(ArrayList<AirbnbListing> originalListings)
    {
        // Initializing stages of filtering making sure the user can start with any filter without problems.
        this.originalListings = originalListings;
        listingsFilteredByBookingData.addAll(originalListings);
        listingsFilteredByPrice.addAll(originalListings);
        listingsFilteredBySelectedBoroughs.addAll(originalListings);
        listingsFilteredByCheckboxes.addAll(originalListings);
        filteredListings.addAll(originalListings);
        priceRange[0] = 0;
        priceRange[1] = Integer.MAX_VALUE;
    }


    /**
     * Get current result of all filter operations.
     * @return The result as an ArrayList
     */
    public ArrayList<AirbnbListing> getFilteredListings()
    {
        return filteredListings;
    }

    /**
     * Returns an observable list which is used for displaying content in e.g. a tableview.
     * @return The result as an ObservableList
     */
    public ObservableList<AirbnbListing> getObservableFilteredListings() { return FXCollections.observableArrayList(filteredListings); }


    /**
     * Change the booking data filter for it.
     * This is the top filter of the filter-chain because it is expected to be changed the least.
     * @param bookingData The booking data the user has entered.
     */
    public void changeBookingData(BookingData bookingData) throws SQLException {
        this.bookingData = bookingData;
        filterBookingData();
    }

    /**
     * Filter for entered booking data. Uses to original listings to avoid loss of data.
     * Checked values are: Minimum and maximum nights, number of guests, price range, availability according to the database.
     * @return
     */
    private void filterBookingData() throws SQLException {
        listingsFilteredByBookingData = originalListings.stream()
                .filter(l -> l.getMinimumNights() <= bookingData.getDaysOfStay() && l.getMaximumNights() >= bookingData.getDaysOfStay())
                .filter(l -> l.getMaxGuests() >= bookingData.getNumberOfPeople())
                .filter(l -> l.getPrice() >= priceRange[0] && l.getPrice() <= priceRange[1])
                .collect(Collectors.toCollection(ArrayList::new));

        filterDates(bookingData.getCheckIn(), bookingData.getCheckOut()); // Checked through the database. Filter from and store in listingsFilteredByBookingData
        filterPriceRange();
    }

    /**
     * Change the price range being applied.
     * @param minPrice The minimum price the user wants to find properties for.
     * @param maxPrice The maximum price the user wants to find properties for.
     * @throws InvalidParameterException If a given price is below 0 or the min is not smaller than the max.
     */
    public void changePriceRange(int minPrice, int maxPrice) throws InvalidParameterException
    {
        if (minPrice < 0 || maxPrice < 0 || !(minPrice < maxPrice))
            throw new InvalidParameterException("Prices cant be negative");
        priceRange[0] = minPrice;
        priceRange[1] = maxPrice;
        filterPriceRange();
    }

    /**
     * Second filter in the filter-chain
     * Separate method so the user can change price range at any time without having filter for booking data again.
     */
    private void filterPriceRange()
    {
        listingsFilteredByPrice = listingsFilteredByBookingData.stream()
                .filter(l -> l.getPrice() >= priceRange[0] && l.getPrice() <= priceRange[1])
                .collect(Collectors.toCollection(ArrayList::new));

        System.out.println("Wrong counted price " + listingsFilteredByPrice.stream()
                .filter(l -> l.getPrice() < priceRange[0] && l.getPrice() > priceRange[1])
                .count());

        filterBoroughs();
    }

    /**
     * Change the boroughs filtered for.
     * @param selectedBoroughs
     */
    public void changeSelectedBoroughs(ArrayList<String> selectedBoroughs)
    {
        this.selectedBoroughs = selectedBoroughs;
        filterBoroughs();
    }

    /**
     * Filters the list for the searched boroughs.
     * Just a temporary filter and not part of the further filtering process.
     */
    private void filterBoroughs() {
        if (!selectedBoroughs.isEmpty()) {
            listingsFilteredBySelectedBoroughs = listingsFilteredByPrice.stream()
                    .filter(l -> selectedBoroughs.contains(l.getNeighbourhood()))
                    .collect(Collectors.toCollection(ArrayList::new));
        }
        else {
            listingsFilteredBySelectedBoroughs.clear();
            listingsFilteredBySelectedBoroughs.addAll(listingsFilteredByPrice);
        }
        filterForActiveFilters();
    }

    /**
     * Set the active filters with a list.
     * @param checkedList The active filters.
     */
    public void setActiveFilters(ArrayList<FilterNames> checkedList)
    {
        activeFilters.clear();
        activeFilters.addAll(checkedList);
        filterForActiveFilters();
    }

    /**
     * Add/Remove a filter depending if it is in the list of active filters or not.
     * @param filter The filter to be added/removed
     */
    public void toggleActiveFilter(FilterNames filter)
    {
        if (activeFilters.contains(filter))
            activeFilters.remove(filter);
        else
            activeFilters.add(filter);
        filterForActiveFilters();
    }

    /**
     * Filter for the filters that have been specified by the user. (Wifi, Pool, Private room, Superhost)
     */
    private void filterForActiveFilters() {

            listingsFilteredByCheckboxes.clear();
            listingsFilteredByCheckboxes.addAll(listingsFilteredBySelectedBoroughs);
            if (!activeFilters.isEmpty()) {
                for (FilterNames filter : activeFilters) {
                    //System.out.println("Filtering for checkbox: " + filter.getId());

                    if (filter.name().equals(FilterNames.WIFI_FILTER.name()))
                        listingsFilteredByCheckboxes = filterAmenity(listingsFilteredByCheckboxes, "Wifi");
                    else if (filter.name().equals(FilterNames.POOL_FILTER.name()))
                        listingsFilteredByCheckboxes = filterAmenity(listingsFilteredByCheckboxes, "Pool");
                    else if (filter.name().equals(FilterNames.ROOM_FILTER.name()))
                        listingsFilteredByCheckboxes = filterPrivateRoom(listingsFilteredByCheckboxes);
                    else if (filter.name().equals(FilterNames.SUPER_FILTER.name()))
                        listingsFilteredByCheckboxes = filterSuperHost(listingsFilteredByCheckboxes);
                    /*
                    switch (box.getId()) {
                        case FilterNames.POOL_FILTER.toString():
                            listingsFilteredByCheckboxes = filterAmenity(listingsFilteredByCheckboxes, "Wifi");
                            break;
                        case "poolBox":
                            listingsFilteredByCheckboxes = filterAmenity(listingsFilteredByCheckboxes, "Pool");
                            break;
                        case "roomBox":
                            listingsFilteredByCheckboxes = filterPrivateRoom(listingsFilteredByCheckboxes);
                            break;
                        case "superBox":
                            listingsFilteredByCheckboxes = filterSuperHost(listingsFilteredByCheckboxes);
                            break;
                    }

                     */
                }
            }
            filteredListings.clear();
            filteredListings.addAll(listingsFilteredByCheckboxes);

    }

    /**
     * Filter for the properties which have not been booked by other users.
     * @param checkIn The checkin date (inclusive)
     * @param checkOut The checkout date (inclusive)
     */
    private void filterDates(LocalDate checkIn, LocalDate checkOut) throws SQLException {


        ArrayList<String> unavailableReservationIDs = new ArrayList<>();
        ZoneId defaultZoneId = ZoneId.systemDefault();

        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();
        Statement statement = connectDB.createStatement();


        Date checkInDate = Date.from(checkIn.atStartOfDay(defaultZoneId).toInstant());
        Date checkOutDate = Date.from(checkOut.atStartOfDay(defaultZoneId).toInstant());

        // Returns all of the booking IDs that are in between the checkIn and checkOut dates
        String filteredReservations = "SELECT listingID FROM booking WHERE (Arrival BETWEEN '" + checkIn + "'- INTERVAL 1 DAY AND '" + checkOut+ "' ) OR (DEPARTURE BETWEEN '"
                + checkIn + "' AND '" + checkOut + "' + INTERVAL 1 DAY ) GROUP BY listingID";

        ResultSet queryResult = statement.executeQuery(filteredReservations);
        while(queryResult.next()) {
            unavailableReservationIDs.add(queryResult.getString(1));
        }
        //System.out.println("checin: " + checkIn + ", checkindate: " + checkInDate);
        for(int i=0; i<listingsFilteredByBookingData.size(); i++) {
            for(int j=0; j<unavailableReservationIDs.size(); j++) {
                if(listingsFilteredByBookingData.get(i).getId().equals(unavailableReservationIDs.get(j))) {
                    System.out.print("removed: " + listingsFilteredByBookingData.get(i).getId() + ", ");
                    listingsFilteredByBookingData.remove(i);
                }
            }
        }
    }

    /**
     * Get the checkbox filters that have been activated.
     * @return
     */
    public HashSet<FilterNames> getActiveFilters()
    {
        return activeFilters;
    }


    /**
     * Filter the given list by an amenity.
     * @param list The list to be filtered.
     * @param filterString The amenity to be filtered by.
     * @return A new list only containing the properties which supply the specified amenity.
     */
    private ArrayList<AirbnbListing> filterAmenity(ArrayList<AirbnbListing> list, String filterString)
    {
        return list.stream()
                .filter(airbnbListing -> airbnbListing.getAmenities().contains(filterString))
                .collect(Collectors.toCollection(ArrayList::new));
    }


    /**
     * A method which filters houses that are hosted by a super host.
     * @param list The list to be filtered.
     * @return A new list only containing superhosts.
     */
    private ArrayList<AirbnbListing> filterSuperHost(ArrayList<AirbnbListing> list){
        return list.stream()
                .filter(airbnbListing -> airbnbListing.isHostSuperhost())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * A method which filters houses that are private.
     * @param list The list to be filtered.
     * @return A new list only containing private rooms.
     */
    private ArrayList<AirbnbListing> filterPrivateRoom(ArrayList<AirbnbListing> list){
        return list.stream()
                .filter(airbnbListing -> airbnbListing.getRoomType().equals("Private room"))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
