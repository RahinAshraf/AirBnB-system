package project_files;

import java.util.ArrayList;

public class Account {

    private int accountID;
    private String username;
    private String password;
    private String emailAddress;
    private ArrayList<AirbnbListing> favouriteProperties;
    private ArrayList<Integer> reservedTrips;

    Account(int accountID, String username, String password, String emailAddress) {
        this.accountID = accountID;
        this.username = username;
        this.password = password;
        this.emailAddress = emailAddress;
        favouriteProperties = new ArrayList<>();
        reservedTrips = new ArrayList<>();
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
}
