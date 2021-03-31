package project_files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.CheckBox;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

public class Listings {

    private ArrayList<AirbnbListing> originalListings;
    private ArrayList<AirbnbListing> listingsFilteredByBookingData = new ArrayList<>();
    private ArrayList<AirbnbListing> listingsFilteredByPrice = new ArrayList<>();
    private ArrayList<AirbnbListing> listingsFilteredBySelectedBoroughs = new ArrayList<>();
    private ArrayList<AirbnbListing> listingsFilteredByCheckboxes = new ArrayList<>();
    private ArrayList<AirbnbListing> filteredListings = new ArrayList<>();

    // The filters that can be applied.
    private BookingData bookingData;
    private int[] priceRange = new int[2];
    private ArrayList<String> selectedBoroughs = new ArrayList<>();
    private HashSet<CheckBox> activeFilters = new HashSet<>();


    public Listings(ArrayList<AirbnbListing> originalListings)
    {
        this.originalListings = originalListings;
        listingsFilteredByBookingData.addAll(originalListings);
        listingsFilteredByPrice.addAll(originalListings);
        listingsFilteredBySelectedBoroughs.addAll(originalListings);
        listingsFilteredByCheckboxes.addAll(originalListings);
        filteredListings.addAll(originalListings);
        priceRange[0] = 0;
        priceRange[1] = Integer.MAX_VALUE;
    }


    public ArrayList<AirbnbListing> getFilteredListings()
    {
        return filteredListings;
    }

    public ObservableList<AirbnbListing> getObservableFilteredListings() { return FXCollections.observableArrayList(filteredListings); }


    //
    public void changeBookingData(BookingData bookingData)
    {
        this.bookingData = bookingData;
        filterBookingData();
    }

    /**
     * Filter for entered booking data.
     * Checked values are: Minimum and maximum nights, number of guests, price range, availability according to the database.
     * @return
     */
    private void filterBookingData()
    {
        //listingsFilteredByBookingData.clear();
        listingsFilteredByBookingData = originalListings.stream()
                .filter(l -> l.getMinimumNights() <= bookingData.getDaysOfStay() && l.getMaximumNights() >= bookingData.getDaysOfStay())
                .filter(l -> l.getMaxGuests() >= bookingData.getNumberOfPeople())
                .filter(l -> l.getPrice() >= priceRange[0] && l.getPrice() <= priceRange[1])
                .collect(Collectors.toCollection(ArrayList::new));

        //filterDates(bookingData.getCheckIn(), bookingData.getCheckOut()); // Checked through the database. Filter from and store in listingsFilteredByBookingData
        filterPriceRange();
    }

    public void changePriceRange(int minPrice, int maxPrice) throws InvalidParameterException
    {
        if (minPrice < 0 || maxPrice < 0)
            throw new InvalidParameterException("Prices cant be negative");
        priceRange[0] = minPrice;
        priceRange[1] = maxPrice;
        filterPriceRange();
    }

    /**
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
        filterForCheckBoxes();
    }

    // Noeed to compare against ids because new objects are created every time when boroughpropertiescontroller is opened.
    public void changeActiveFilters(CheckBox checkBox)
    {
        ArrayList<String> checkBoxIds = new ArrayList<>();
        for (CheckBox c : activeFilters)
            checkBoxIds.add(c.getId());

        if (checkBoxIds.contains(checkBox.getId()))
            activeFilters.remove(checkBox);
        else
            activeFilters.add(checkBox);

        //if (!activeFilters.add(checkBox))
        //    activeFilters.remove(checkBox);
        System.out.print("Active filters ");
        for (CheckBox c : activeFilters)
        System.out.print(" " + c.getId());
        filterForCheckBoxes();
    }

    private void filterForCheckBoxes() {

            listingsFilteredByCheckboxes.clear();
            listingsFilteredByCheckboxes.addAll(listingsFilteredBySelectedBoroughs);
            if (!activeFilters.isEmpty()) {
                for (CheckBox box : activeFilters) {
                    //System.out.println("Filtering for checkbox: " + filter.getId());
                    switch (box.getId()) {
                        case "wifiBox":
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
                }
            }
            filteredListings.clear();
            filteredListings.addAll(listingsFilteredByCheckboxes);

    }




    private void filterDates(LocalDate checkIn, LocalDate checkOut) {
        // @Barni
    }


    public HashSet<CheckBox> getActiveFilters()
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
