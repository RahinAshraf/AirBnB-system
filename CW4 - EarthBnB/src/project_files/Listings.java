package project_files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.security.InvalidParameterException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
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
 * @author Valentin Magis, Vandad Vafai Tabrizi, Barnabas Szalai
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
     * @param originalListings The original listings loaded from the csv file.
     */
    public Listings(ArrayList<AirbnbListing> originalListings)
    {
        // Initializing stages of filtering making sure the user can start with any filter without problems.
        this.originalListings = originalListings;
        originalListings.sort(AirbnbListing::compareTo);
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
     * Get the original listings loaded at the beginning.
     * @return The original listings.
     */
    public ArrayList<AirbnbListing> getOriginalListings()
    {
        return originalListings;
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
     * Checked values are: Minimum and maximum nights, number of guests, availability according to the database.
     * @return The list filtered by the booking data and all following filters.
     */
    private void filterBookingData() throws SQLException {
        listingsFilteredByBookingData = originalListings.stream()
                .filter(l -> l.getMinimumNights() <= bookingData.getDaysOfStay() && l.getMaximumNights() >= bookingData.getDaysOfStay())
                .filter(l -> l.getMaxGuests() >= bookingData.getNumberOfPeople())
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<String> unavailableReservationIDs;

        if (MainFrameController.isUsingDatabase())
            unavailableReservationIDs = filterDBDates(bookingData.getCheckIn(), bookingData.getCheckOut());
        else
            unavailableReservationIDs = filterOfflineDates(bookingData.getCheckIn(), bookingData.getCheckOut());

        // Remove the found reservations from the listings. The user wont be able to book them.
        for (String id : unavailableReservationIDs)
        {
            listingsFilteredByBookingData.remove(iterativeSearch(listingsFilteredByBookingData, id));
        }
        filterPriceRange();
    }

    /**
     * Filter for the properties which have not been booked by other users in the specified timeframe.
     * Uses the database or offline generated data.
     * @param checkIn The checkin date (inclusive)
     * @param checkOut The checkout date (inclusive)
     */
    private ArrayList<String> filterOfflineDates(LocalDate checkIn, LocalDate checkOut) {
        ArrayList<String> unavailableReservationIDs;
        ArrayList<Reservation> reservations = OfflineData.getReservations();

        unavailableReservationIDs = reservations.stream()
                .filter(r -> r.getArrival().isAfter(checkIn.minusDays(1))
                        && r.getArrival().isBefore(checkOut)
                        || r.getDeparture().isAfter(checkIn)
                        && r.getDeparture().isBefore(checkOut.plusDays(1)))
                .map(Reservation::getListingID)
                .collect(Collectors.toCollection(ArrayList::new));
        return unavailableReservationIDs;
    }

    /**
     * Filter for the properties which have not been booked by other users in the specified timeframe.
     * Uses the database or online database.
     * @param checkIn The checkin date (inclusive)
     * @param checkOut The checkout date (inclusive)
     */
    private ArrayList<String> filterDBDates(LocalDate checkIn, LocalDate checkOut) throws SQLException {
        ArrayList<String> unavailableReservationIDs = new ArrayList<>();
        DatabaseConnection connection = new DatabaseConnection();
        Connection connectDB = connection.getConnection();
        Statement statement = connectDB.createStatement();

        // Returns all of the booking IDs that are in between the checkIn and checkOut dates
        String filteredReservations = "SELECT listingID FROM booking WHERE (Arrival BETWEEN '" + checkIn + "'- INTERVAL 1 DAY AND '" + checkOut + "' ) OR (DEPARTURE BETWEEN '"
                + checkIn + "' AND '" + checkOut + "' + INTERVAL 1 DAY ) GROUP BY listingID";

        ResultSet queryResult = statement.executeQuery(filteredReservations);
        while (queryResult.next()) {
            unavailableReservationIDs.add(queryResult.getString(1));
        }
        return unavailableReservationIDs;
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
        filterBoroughs();
    }


    /**
     * Change the boroughs filtered for. The filter is only activated when a borough is selected and the user searches for the properties.
     * Other panels in the mainframecontroller should not be affected by this filter.
     * @param selectedBoroughs The boroughs to be filtered for.
     */
    public void changeSelectedBoroughs(ArrayList<String> selectedBoroughs)
    {
        this.selectedBoroughs = selectedBoroughs;
        filterBoroughs();
    }

    /**
     * Filters the list for the searched boroughs.
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
     * A method which helps us for the test class.
     * @return the size of the listingsFilteredBySelectedBoroughs array list
     */
    public int getlistingsFilteredByBoroughSize(){
        return listingsFilteredBySelectedBoroughs.size();
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
                    if (filter.name().equals(FilterNames.WIFI_FILTER.name()))
                        listingsFilteredByCheckboxes = filterAmenity(listingsFilteredByCheckboxes, "Wifi");
                    else if (filter.name().equals(FilterNames.POOL_FILTER.name()))
                        listingsFilteredByCheckboxes = filterAmenity(listingsFilteredByCheckboxes, "Pool");
                    else if (filter.name().equals(FilterNames.ROOM_FILTER.name()))
                        listingsFilteredByCheckboxes = filterPrivateRoom(listingsFilteredByCheckboxes);
                    else if (filter.name().equals(FilterNames.SUPER_FILTER.name()))
                        listingsFilteredByCheckboxes = filterSuperHost(listingsFilteredByCheckboxes);
                }
            }
            filteredListings.clear();
            filteredListings.addAll(listingsFilteredByCheckboxes);
    }

    /**
     * Get the checkbox filters that have been activated.
     * @return The activated filters.
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
    public ArrayList<AirbnbListing> filterAmenity(ArrayList<AirbnbListing> list, String filterString)
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
    public ArrayList<AirbnbListing> filterSuperHost(ArrayList<AirbnbListing> list){
        return list.stream()
                .filter(airbnbListing -> airbnbListing.isHostSuperhost())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * A method which filters houses that are private.
     * @param list The list to be filtered.
     * @return A new list only containing private rooms.
     */
    public ArrayList<AirbnbListing> filterPrivateRoom(ArrayList<AirbnbListing> list){
        return list.stream()
                .filter(airbnbListing -> airbnbListing.getRoomType().equals("Private room"))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * Binary Search with ID in an arrayList. Utilizing the fact that the loaded csv file is ordered by ids.
     * @param arrayToSearch The array to be searched for the element.
     * @param element The element to be searched for in the array.
     * @return The Airbnblisting if found, otherwise null.
     */
    public static AirbnbListing iterativeSearch(ArrayList<AirbnbListing> arrayToSearch, String element) {
        int lowIndex = 0;
        int highIndex = arrayToSearch.size()-1;

        // Holds the position in array for given element
        // Initial negative integer set to be returned if no match was found on array
        //int elementPos = -1;

        // If lowIndex less than highIndex, there's still elements in the array
        while (lowIndex <= highIndex) {
            int midIndex = (lowIndex + highIndex) / 2;
            int midID = Integer.parseInt(arrayToSearch.get(midIndex).getId());
            int elementInteger = Integer.parseInt(element);
            if (element.equals(arrayToSearch.get(midIndex).getId())) {
                return arrayToSearch.get(midIndex);
            } else if (elementInteger < midID) {
                highIndex = midIndex-1;
            } else if (elementInteger > midID) {
                lowIndex = midIndex+1;
            }
        }
        return null;
    }

    /**
     * A method which helps us for the test class.
     * @return the list filtered by the filterBookingData
     */
    public ArrayList<AirbnbListing> getListingsFilteredByBookingData(){
        return listingsFilteredByBookingData;
    }

    /**
     * A method which helps us for the test class.
     * @return the list filtered by the filterPriceRange
     */
    public ArrayList<AirbnbListing> getListingsFilteredByPrice(){
        return listingsFilteredByPrice;
    }

    /**
     * A method which helps us for the test class.
     * @return the list filtered by the filterSelectedBoroughs
     */
    public ArrayList<AirbnbListing> getListingsFilteredBySelectedBoroughs(){
        return listingsFilteredBySelectedBoroughs;
    }

    /**
     * Helper method for the Unit Test.
     * @return  The size of the active filter hash set.
     */
    public int getActiveFilterSize(){
        return activeFilters.size();
    }
}
