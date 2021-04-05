package project_files;

import javafx.collections.FXCollections;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

/**
 * A test class for the class Listings.
 * This class tests all the methods called in the Listings class.
 *
 * @author Vandad Vafai Tabrizi
 */
public class ListingsTest {
    // An instance variable of the class:
    // An Array List for the parameter of the constructor of class Listings.
    private ArrayList<AirbnbListing> listingArrayList = new ArrayList<>();
    // An object type of class Listings
    private Listings listings = new Listings(listingArrayList, false);

    // An object type of class BookingData.
    private BookingData bookingData;
    // The Array lists which the are for different filters:
    // The first and complete list of data.
    private ArrayList<AirbnbListing> originalListing;
    // The list of check box filters
    private ArrayList<AirbnbListing> checkBoxFilters;
    // The list of the filtered data.
    private ArrayList<AirbnbListing> filteredList;
    // The list for the booking data that is set.
    private ArrayList<AirbnbListing> bookingDataList;
    // The list for the price ranges which the user has chose.
    private ArrayList<AirbnbListing> priceRangeList;
    // The set of check box filters.
    private HashSet<FilterNames> activeFilters;

    private URL urlPic = new URL("https://a0.muscache.com/pictures/miso/Hosting-13913/original/7e27055e-5eed-48ac-8c75-355336f1eaea.jpeg");
    private URL urlHost = new URL("https://a0.muscache.com/im/users/54730/profile_pic/1327774386/original.jpg?aki_policy=profile_small");
    private URL urlPic1 = new URL("https://a0.muscache.com/im/users/54730/profile_pic/1327774386/original.jpg?aki_policy=profile_x_medium");
    private ArrayList<String> amenities = new ArrayList<>();


    private AirbnbListing data = new AirbnbListing("13913", "Holiday London DB Room Let-on going", "Finsbury Park is a friendly " +
            "melting pot community composed of Turkish, French, Spanish, Middle Eastern, Irish and English families. " +
            "<br />We have a wonderful variety of international restaurants directly under us on Stroud Green Road. " +
            "And there are many shops and large Tescos supermarket right next door. <br /><br />But you can also venture " +
            "up to Crouch End and along Greens Lanes where there will endless choice of Turkish and Middle Eastern cuisines.s"
            , urlPic, "54730", "Alina", "within a few hours", false, urlHost
            , urlPic1, 3, "Islington", 51.56802, -0.11121, "room", 1
            , "1 shared bath", 1,
            amenities, 500, 1, 2, 365, 21, 50,
            5, 5, 5, 0.16);

    private URL urlPic3 = new URL("https://a0.muscache.com/pictures/miso/Hosting-13913/original/7e27055e-5eed-48ac-8c75-355336f1eaea.jpeg");
    private URL urlHost1 = new URL("https://a0.muscache.com/im/users/54730/profile_pic/1327774386/original.jpg?aki_policy=profile_small");
    private URL urlPic4 = new URL("https://a0.muscache.com/im/users/54730/profile_pic/1327774386/original.jpg?aki_policy=profile_x_medium");
    private ArrayList<String> amenities1 = new ArrayList<>();

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

        originalListing =  new ArrayList<>();
        originalListing.add(data);
        originalListing.add(data1);


        LocalDate checkIn = LocalDate.of(2021, 04, 12);
        LocalDate checkOut = LocalDate.of(2021, 04, 29);
        bookingData = new BookingData(checkIn, checkOut, 3, 123);
    }

    @Test
    public void getFilteredListings() {
        Assert.assertEquals(filteredList, listings.getFilteredListings());
    }

    @Test
    public void getObservableFilteredListings() {
        Assert.assertEquals(FXCollections.observableArrayList(filteredList), listings.getObservableFilteredListings());
    }

    @Test
    public void getActiveFilters() {
        Assert.assertEquals(activeFilters, listings.getActiveFilters());
    }

    @Before
    public void init(){
        // Adding the data to the check box list.
        checkBoxFilters = new ArrayList<>();
        checkBoxFilters.add(data);
        checkBoxFilters.add(data1);
        // Adding the data to the booking data list.
        bookingDataList = new ArrayList<>();
        bookingDataList.add(data);
        bookingDataList.add(data1);
        // Adding the data to the price range list.
        priceRangeList = new ArrayList<>();
        priceRangeList.add(data);
        priceRangeList.add(data1);

        activeFilters = new HashSet<>();

        filteredList = new ArrayList<>();
    }

    @Test
    public void filterBookingData() throws SQLException {
       // listings.changeBookingData(bookingData);
       // listings.filterBookingData();
       // Assert.assertEquals(1, bookingDataList.size());
    }

    @Test
    public void filterPriceRange(){
        // listings.changePriceRange(1, 50);
        // Assert.assertEquals(1, filteredList.size());
    }

    @Test
    public void setActiveFilters() {
    }

    @Test
    public void toggleActiveFilter() {
        listings.toggleActiveFilter(FilterNames.SUPER_FILTER);
        listings.toggleActiveFilter(FilterNames.POOL_FILTER);
        Assert.assertEquals(1, activeFilters.size());
    }

    @Test
    public void filterBoroughs(){
    }

    @Test
    public void filterDates(){
    }

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