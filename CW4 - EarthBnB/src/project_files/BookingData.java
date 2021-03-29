package project_files;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * The BookingData Class stores the data received from the welcome panel.
 *
 * @author
 */

public class BookingData {

    public LocalDate getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDate getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(int numberOfPeople) throws InvalidParameterException {
        if (numberOfPeople < 0)
            throw new InvalidParameterException("Number of people cant be negative");
        this.numberOfPeople = numberOfPeople;
    }

    public int getBookerID() {
        return bookerID;
    }

    public void setBookerID(int bookerID) {
        this.bookerID = bookerID;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }


    private LocalDate checkIn;
    private LocalDate checkOut;
    private int numberOfPeople;
    private int bookerID;
    private int minPrice = 0;
    private int maxPrice = Integer.MAX_VALUE;

    public BookingData(LocalDate checkIn, LocalDate checkOut, int numberOfPeople, int bookerID) throws InvalidParameterException {
        if (numberOfPeople < 0)
            throw new InvalidParameterException("Number of people cant be negative");
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.numberOfPeople = numberOfPeople;
        this.bookerID = bookerID;
    }

    public BookingData()
    {
    }

    public void setPriceRange(int minPrice, int maxPrice) throws InvalidParameterException
    {
        if (minPrice < 0 || maxPrice < 0)
            throw new InvalidParameterException("Prices cant be negative");
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public int getDaysOfStay()
    {
        return (int) ChronoUnit.DAYS.between(checkIn, checkOut);
    }
}
