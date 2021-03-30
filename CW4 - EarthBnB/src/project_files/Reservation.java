package project_files;

import java.time.LocalDate;

public class Reservation {

    private int reservationID;
    private LocalDate arrival;
    private LocalDate departure;
    private int bookerID;
    private int numberOfGuests;
    private double price;
    private String listingID;

    Reservation(int reservationID, LocalDate arrival, LocalDate departure, int bookerID,
                int numberOfGuests, double price, String listingID) {
        this.reservationID = reservationID;
        this.arrival = arrival;
        this.departure = departure;
        this.bookerID = bookerID;
        this.numberOfGuests = numberOfGuests;
        this.price = price;
        this.listingID = listingID;
    }

    public int getReservationID() {
        return reservationID;
    }

    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }

    public LocalDate getArrival() {
        return arrival;
    }

    public void setArrival(LocalDate arrival) {
        this.arrival = arrival;
    }

    public LocalDate getDeparture() {
        return departure;
    }

    public void setDeparture(LocalDate departure) {
        this.departure = departure;
    }

    public int getBookerAccount() {
        return bookerID;
    }

    public void setBookerAccount(int bookerID) {
        this.bookerID = bookerID;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getListingID() {
        return listingID;
    }

    public void setListingID(String listingID) {
        this.listingID = listingID;
    }
}
