package project_files;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;


/**
 * The Account class creates a blueprint for the account of the user. When the user logs in,
 * an account is instantiate using either the values from the database or it is assigned an account from
 * the offline registered accounts.
 *
 * @author  Valentin Magis, Rahin Ashraf, Vandad Vafai Tabrizi, Barnabas Szalai
 * @version 1.0
 * @since   2021-03-11
 */
public class Account {

    private int accountID;
    private String username;
    private String password;
    private String emailAddress;
    private HashSet<AirbnbListing> favouriteProperties;
    private ArrayList<Integer> reservedTrips;
    private BookingData bookingData;

    private ArrayList<Reservation> offlineReservations;


    /**
     * This is the constructor method for the Account class. It is used to initialize its fields. It also initializes
     * arraylists that store booking activities of the user.
     * @param accountID the account ID of the user. It is auto incremented for every new user
     * @param username  the username of the user that is used to log in
     * @param password  the password of the user that is used to log in
     * @param emailAddress  the email address of the user. It is validated before the account can be created.
     */
    Account(int accountID, String username, String password, String emailAddress) {
        this.accountID = accountID;
        this.username = username;
        this.password = password;
        this.emailAddress = emailAddress;
        favouriteProperties = new HashSet<>();
        reservedTrips = new ArrayList<>();
        bookingData = new BookingData();
        offlineReservations = new ArrayList<>();
    }


    public void addReservedTrip(Integer tripID) {
        reservedTrips.add(tripID);
    }
    public void addOfflineReservation(Reservation reservation) {
        offlineReservations.add(reservation);
    }
    public void addFavouriteProperty(AirbnbListing property) {
        favouriteProperties.add(property);
    }

    public void setBookingData(LocalDate checkIn, LocalDate checkOut, int people, int accountID) {
        bookingData.setCheckIn(checkIn);
        bookingData.setCheckOut(checkOut);
        bookingData.setNumberOfPeople(people);
        bookingData.setBookerID(accountID);
    }

    /**
     * This method removes a particular property from the user's saved properties if the property has been
     * actually saved by the user.
     * @param property the property that is to be removed
     * @return boolean  whether the property was saved already by the user and could be removed
     */
    public boolean removeFavouriteProperty(AirbnbListing property) {
        if(favouriteProperties.contains(property)) {
            favouriteProperties.remove(property);
            return true;
        }
        return false;
    }

    public ArrayList<Reservation> getOfflineReservations() {
        return offlineReservations;
    }
    public int getAccountID() {
        return accountID;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getEmailAddress() {
        return emailAddress;
    }
    public HashSet<AirbnbListing> getSavedProperties() {
        return favouriteProperties;
    }
    public BookingData getBookingData() {
        return bookingData;
    }
}
