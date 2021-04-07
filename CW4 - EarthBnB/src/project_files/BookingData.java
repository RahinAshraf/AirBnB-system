package project_files;

import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * The BookingData Class stores the data received from the welcome panel.
 *
 * @author
 */

public class BookingData {

    private LocalDate checkIn;
    private LocalDate checkOut;
    private int numberOfPeople;
    private int bookerID;

    /**
     * A constructor for the class BookingData.
     * @param checkIn the date the user checks in
     * @param checkOut the date the user checks out
     * @param numberOfPeople the number of guests the user enters
     * @param bookerID the user's ID
     */
    public BookingData(LocalDate checkIn, LocalDate checkOut, int numberOfPeople, int bookerID) throws InvalidParameterException {
        if (numberOfPeople < 0)
            throw new InvalidParameterException("Number of people cant be negative");
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.numberOfPeople = numberOfPeople;
        this.bookerID = bookerID;
    }

    public BookingData() {}

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

    /**
     * Sets the number of guests the user has entered.
     *
     * @param numberOfPeople  the number of guests the user enters
     * @throws InvalidParameterException  if the number of guests are negative numbers
     */
    public void setNumberOfPeople(int numberOfPeople) throws InvalidParameterException {
        if (numberOfPeople < 0)
            throw new InvalidParameterException("Number of people cant be negative");
        this.numberOfPeople = numberOfPeople;
    }

    public void setBookerID(int bookerID) {
        this.bookerID = bookerID;
    }

    public int getDaysOfStay()
    {
        return (int) ChronoUnit.DAYS.between(checkIn, checkOut);
    }
}
