package project_files;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * A test class for the class Listings.
 * This class tests all the methods called in the Listings class.
 *
 * @author Vandad Vafai Tabrizi
 */
public class ListingsTest {

    // An object type of class BookingData.
    private BookingData bookingData;
    // The Array lists which the are for different filters:
    // The first and complete list of data.
    private ArrayList<AirbnbListing> originalListing = new ArrayList<>();
    // The list of check box filters
    private ArrayList<AirbnbListing> checkBoxFilters;
    // An Hash set for our active check box filters.
    private HashSet<FilterNames> checkedFilters = new HashSet<>();
    // The list of the filtered data.
    private ArrayList<AirbnbListing> filteredList;
    // The boroughs selected by the user.
    private ArrayList<String> selectedBoroughs = new ArrayList<>();
    // An object type of class Listings
    private Listings listings;
    //
    private LocalDate checkIn;
    //
    private LocalDate checkOut;

    private URL urlPic = new URL("https://a0.muscache.com/pictures/miso/Hosting-13913/original/7e27055e-5eed-48ac-8c75-355336f1eaea.jpeg");
    private URL urlHost = new URL("https://a0.muscache.com/im/users/54730/profile_pic/1327774386/original.jpg?aki_policy=profile_small");
    private URL urlPic1 = new URL("https://a0.muscache.com/im/users/54730/profile_pic/1327774386/original.jpg?aki_policy=profile_x_medium");
    private ArrayList<String> amenities = new ArrayList<>();

    // The first object type of AirbnbListing.
    private AirbnbListing data = new AirbnbListing("13914", "Holiday London DB Room Let-on going", "Finsbury Park is a friendly " +
            "melting pot community composed of Turkish, French, Spanish, Middle Eastern, Irish and English families. " +
            "<br />We have a wonderful variety of international restaurants directly under us on Stroud Green Road. " +
            "And there are many shops and large Tescos supermarket right next door. <br /><br />But you can also venture " +
            "up to Crouch End and along Greens Lanes where there will endless choice of Turkish and Middle Eastern cuisines.s"
            , urlPic, "54730", "Alina", "within a few hours", false, urlHost
            , urlPic1, 3, "Kensington", 51.56802, -0.11121, "room", 1
            , "1 shared bath", 1,
            amenities, 500, 1, 2, 365, 21, 50,
            5, 5, 5, 0.16);

    private URL urlPic3 = new URL("https://a0.muscache.com/pictures/miso/Hosting-13913/original/7e27055e-5eed-48ac-8c75-355336f1eaea.jpeg");
    private URL urlHost1 = new URL("https://a0.muscache.com/im/users/54730/profile_pic/1327774386/original.jpg?aki_policy=profile_small");
    private URL urlPic4 = new URL("https://a0.muscache.com/im/users/54730/profile_pic/1327774386/original.jpg?aki_policy=profile_x_medium");
    private ArrayList<String> amenities1 = new ArrayList<>();

    // The second object type of AirbnbListing.
    private AirbnbListing data1 = new AirbnbListing("13913", "Holiday London DB Room Let-on going", "Finsbury Park is a friendly " +
            "melting pot community composed of Turkish, French, Spanish, Middle Eastern, Irish and English families. " +
            "<br />We have a wonderful variety of international restaurants directly under us on Stroud Green Road. " +
            "And there are many shops and large Tescos supermarket right next door. <br /><br />But you can also venture " +
            "up to Crouch End and along Greens Lanes where there will endless choice of Turkish and Middle Eastern cuisines.s"
            , urlPic3, "54730", "Alina", "within a few hours", true, urlHost1
            , urlPic4, 3, "Islington", 51.56802, -0.11121, "Private room", 3
            , "1 shared bath", 1,
            amenities1, 40, 1, 29, 365, 21, 50,
            5, 5, 5, 0.16);

    /**
     * The constructor of the test class ListingsTest.
     * @throws MalformedURLException
     */
    public ListingsTest() throws MalformedURLException {
        amenities.add("Toys");
        amenities.add("Books");
        amenities.add("Washer");
        amenities.add("Balcony");

        amenities1.add("Wifi");
        amenities1.add("Pool");
        amenities1.add("Toys");
        amenities1.add("Books");
        amenities1.add("Washer");

        originalListing.add(data);
        originalListing.add(data1);
        listings = new Listings(originalListing);

        checkIn = LocalDate.of(2021, 05, 5);
        checkOut = LocalDate.of(2021, 05, 18);
        bookingData = new BookingData(checkIn, checkOut, 2, 123);
    }

    /**
     * Initializes the elements of each of the lists before the testing occurs.
     */
    @Before
    public void init(){
        // Adding the data to the check box list.
        checkBoxFilters = new ArrayList<>();
        checkBoxFilters.add(data);
        checkBoxFilters.add(data1);
        // Adding the active filters to the hash set.
        checkedFilters.add(FilterNames.WIFI_FILTER);
        checkedFilters.add(FilterNames.SUPER_FILTER);
        checkedFilters.add(FilterNames.POOL_FILTER);
        // listings.setActiveFilters(checkedFilters);
        // Adding the pseudo selected boroughs;
        selectedBoroughs.add("Islington");

        filteredList = new ArrayList<>();
    }

    /**
     * Checks if the method filters the Airbnb Listing data by the
     * check-in date, check-out date and number of guests written by
     * the user and applies it to the list.
     */
    @Test
    public void filterBookingData() throws SQLException {
       listings.changeBookingData(bookingData);
       Assert.assertEquals(data1, listings.getListingsFilteredByBookingData().get(0));
    }

    /**
     * Checks if the method filters the Airbnb Listing data
     * by the price range selected by the user and applies it to the list.
     */
    @Test
    public void filterPriceRange(){
        listings.changePriceRange(1,50);
        for (AirbnbListing boroughs: listings.getListingsFilteredByPrice()){
            System.out.println(boroughs.getId() + " " + boroughs.getNeighbourhood() );
        }
        Assert.assertEquals(data1, listings.getListingsFilteredByPrice().get(0));
    }

    /**
     * Checks if the method filters the Airbnb Listing data by the
     * boroughs selected by the user and applies to the list.
     */
    @Test
    public void filterBoroughs(){
        listings.changeSelectedBoroughs(selectedBoroughs);
        for (AirbnbListing boroughs: listings.getListingsFilteredByPrice()){
            System.out.println(boroughs.getId() + " " + boroughs.getNeighbourhood() );
        }
        // for (AirbnbListing boroughs: listings.getListingsFilteredBySelectedBoroughs()){
          //  System.out.println(boroughs.getId() + " " + boroughs.getNeighbourhood() );
        // }
        Assert.assertEquals(data1.getId(), listings.getListingsFilteredBySelectedBoroughs().get(0).getId());
    }

    /**
     * Checks if the method filters the Airbnb Listing data by the
     * dates selected by the user and applies to the list.
     */
    @Test
    public void filterDates() throws SQLException {
        //OfflineData.addReservation(new Reservation(0, new LocalDate(2021, 05, 01), new));
        //.filterDates(checkIn, checkOut);
    }

    /**
     * Checks if the method returns the correct amount of filters which
     * are active.
     */
    @Test
    public void setActiveFilters() {
        ArrayList<FilterNames> activatedFilters = new ArrayList<>();
        activatedFilters.add(FilterNames.SUPER_FILTER);
        activatedFilters.add(FilterNames.WIFI_FILTER);
        listings.setActiveFilters(activatedFilters);

        Assert.assertEquals(true, listings.getActiveFilters().contains(FilterNames.WIFI_FILTER));
        Assert.assertEquals(true, listings.getActiveFilters().contains(FilterNames.SUPER_FILTER));
        Assert.assertEquals(2, activatedFilters.size());
    }

    /**
     * Checks if it deletes or adds the check box filters into the array list
     * to be applied to the filtered list.
     */
    @Test
    public void toggleActiveFilter() {
        listings.toggleActiveFilter(FilterNames.POOL_FILTER);
        listings.toggleActiveFilter(FilterNames.WIFI_FILTER);
        listings.toggleActiveFilter(FilterNames.SUPER_FILTER);
        Assert.assertEquals(true, listings.getActiveFilters().contains(FilterNames.WIFI_FILTER));
        Assert.assertEquals(true, listings.getActiveFilters().contains(FilterNames.SUPER_FILTER));
        Assert.assertEquals(true, listings.getActiveFilters().contains(FilterNames.POOL_FILTER));
        Assert.assertEquals(3, listings.getActiveFilterSize());

        listings.toggleActiveFilter(FilterNames.WIFI_FILTER);
        checkedFilters.remove(FilterNames.WIFI_FILTER);
        Assert.assertEquals(true, listings.getActiveFilters().contains(FilterNames.SUPER_FILTER));
        Assert.assertEquals(true, listings.getActiveFilters().contains(FilterNames.POOL_FILTER));
        Assert.assertEquals(2, listings.getActiveFilterSize());
    }

    /**
     * Checks if the method adds and apply the filter to the filtered list.
     */
    @Test
    public void filterForActiveFilters() {
        checkBoxFilters = listings.filterAmenity(checkBoxFilters, "Wifi");
        filteredList.addAll(checkBoxFilters);
        Assert.assertEquals(1, filteredList.size());
    }

    /**
     * Checks if the method returns a data form with a private room.
     */
    @Test
    public void filterPrivateRoom(){
        filteredList =  new ArrayList<>();
        filteredList.add(data1);

        Assert.assertEquals(filteredList, listings.filterPrivateRoom(originalListing));

    }

    /**
     * Checks if the method returns a data form with Wifi or a pool.
     */
    @Test
    public void filterAmenities() throws MalformedURLException {
        filteredList =  new ArrayList<>();
        filteredList.add(data1);

        Assert.assertEquals(filteredList, listings.filterAmenity(originalListing, "Wifi"));

        // Filter for pool with new data.

        URL urlPic5 = new URL("https://a0.muscache.com/pictures/miso/Hosting-13913/original/7e27055e-5eed-48ac-8c75-355336f1eaea.jpeg");
        URL urlHost2 = new URL("https://a0.muscache.com/im/users/54730/profile_pic/1327774386/original.jpg?aki_policy=profile_small");
        URL urlPic6 = new URL("https://a0.muscache.com/im/users/54730/profile_pic/1327774386/original.jpg?aki_policy=profile_x_medium");
        ArrayList<String> amenities2 = new ArrayList<>();
        amenities.add("Toys");
        amenities.add("Books");
        amenities.add("Washer");
        amenities.add("Balcony");

        AirbnbListing data3 = new AirbnbListing("13913", "Holiday London DB Room Let-on going", "Finsbury Park is a friendly " +
                "melting pot community composed of Turkish, French, Spanish, Middle Eastern, Irish and English families. " +
                "<br />We have a wonderful variety of international restaurants directly under us on Stroud Green Road. " +
                "And there are many shops and large Tescos supermarket right next door. <br /><br />But you can also venture " +
                "up to Crouch End and along Greens Lanes where there will endless choice of Turkish and Middle Eastern cuisines.s"
                , urlPic5, "54730", "Alina", "within a few hours", false, urlHost2
                , urlPic6, 3, "Islington", 51.56802, -0.11121, "Private room", 2
                , "1 shared bath", 1,
                amenities, 40, 1, 29, 365, 21, 50,
                5, 5, 5, 0.16);

        URL urlPic7 = new URL("https://a0.muscache.com/pictures/miso/Hosting-13913/original/7e27055e-5eed-48ac-8c75-355336f1eaea.jpeg");
        URL urlHost4 = new URL("https://a0.muscache.com/im/users/54730/profile_pic/1327774386/original.jpg?aki_policy=profile_small");
        URL urlPic8 = new URL("https://a0.muscache.com/im/users/54730/profile_pic/1327774386/original.jpg?aki_policy=profile_x_medium");
        ArrayList<String> amenities4 = new ArrayList<>();
        amenities1.add("Wifi");
        amenities1.add("Pool");
        amenities1.add("Toys");
        amenities1.add("Books");
        amenities1.add("Washer");

        AirbnbListing data4 = new AirbnbListing("13913", "Holiday London DB Room Let-on going", "Finsbury Park is a friendly " +
                "melting pot community composed of Turkish, French, Spanish, Middle Eastern, Irish and English families. " +
                "<br />We have a wonderful variety of international restaurants directly under us on Stroud Green Road. " +
                "And there are many shops and large Tescos supermarket right next door. <br /><br />But you can also venture " +
                "up to Crouch End and along Greens Lanes where there will endless choice of Turkish and Middle Eastern cuisines.s"
                , urlPic3, "54730", "Alina", "within a few hours", true, urlHost1
                , urlPic4, 3, "Islington", 51.56802, -0.11121, "Private room", 2
                , "1 shared bath", 1,
                amenities1, 40, 1, 29, 365, 21, 50,
                5, 5, 5, 0.16);

        originalListing =  new ArrayList<>();
        originalListing.add(data4);
        originalListing.add(data3);

        filteredList = new ArrayList<>();
        filteredList.add(data4);
        Assert.assertEquals(filteredList, listings.filterAmenity(originalListing, "Pool"));

    }

    /**
     * Checks if the method returns a data form with a super host.
     */
    @Test
    public void filterSuperHost(){
        filteredList = new ArrayList<>();
        filteredList.add(data1);

        Assert.assertEquals(filteredList, listings.filterSuperHost(originalListing));
    }
}