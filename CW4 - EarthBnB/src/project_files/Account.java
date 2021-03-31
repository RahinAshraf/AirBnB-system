package project_files;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

public class Account {

    private int accountID;
    private String username;
    private String password;
    private String emailAddress;
    private HashSet<AirbnbListing> favouriteProperties;
    private ArrayList<Integer> reservedTrips;
    private BookingData bookingData;

    Account(int accountID, String username, String password, String emailAddress) {
        this.accountID = accountID;
        this.username = username;
        this.password = password;
        this.emailAddress = emailAddress;
        favouriteProperties = new HashSet<>();
        reservedTrips = new ArrayList<>();
        bookingData = new BookingData();
    }


    public int getAccountID() {
        return accountID;
    }

    public void addReservedTrip(Integer tripID) {
        reservedTrips.add(tripID);
    }

    public void addFavouriteProperty(AirbnbListing property) {
        favouriteProperties.add(property);
    }

    public boolean removeFavouriteProperty(AirbnbListing property) {
        if(favouriteProperties.contains(property)) {
            favouriteProperties.remove(property);
            return true;
        }
        return false;
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

    public void setBookingData(BookingData bookingData) {
        this.bookingData = bookingData;
    }

    public void setBookingData(LocalDate checkIn, LocalDate checkOut, int people, int accountID) {
        bookingData.setCheckIn(checkIn);
        bookingData.setCheckOut(checkOut);
        bookingData.setNumberOfPeople(people);
        bookingData.setBookerID(accountID);
    }
}
