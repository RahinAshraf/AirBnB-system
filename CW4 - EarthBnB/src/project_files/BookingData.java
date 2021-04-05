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
     *
     */
    public BookingData(LocalDate checkIn, LocalDate checkOut, int numberOfPeople, int bookerID) throws InvalidParameterException {
        if (numberOfPeople < 0)
            throw new InvalidParameterException("Number of people cant be negative");
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.numberOfPeople = numberOfPeople;
        this.bookerID = bookerID;
    }

    /**
     *
     */
    public BookingData() {}

    /**
     *
     * @return
     */
    public LocalDate getCheckIn() {
        return checkIn;
    }

    /**
     *
     * @param checkIn
     */
    public void setCheckIn(LocalDate checkIn) {
        this.checkIn = checkIn;
    }

    /**
     *
     * @return
     */
    public LocalDate getCheckOut() {
        return checkOut;
    }

    /**
     *
     * @param checkOut
     */
    public void setCheckOut(LocalDate checkOut) {
        this.checkOut = checkOut;
    }

    /**
     *
     * @return
     */
    public int getNumberOfPeople() {
        return numberOfPeople;
    }

    /**
     *
     * @param numberOfPeople
     * @throws InvalidParameterException
     */
    public void setNumberOfPeople(int numberOfPeople) throws InvalidParameterException {
        if (numberOfPeople < 0)
            throw new InvalidParameterException("Number of people cant be negative");
        this.numberOfPeople = numberOfPeople;
    }

    /**
     *
     * @return
     */
    public int getBookerID() {
        return bookerID;
    }

    /**
     *
     * @param bookerID
     */
    public void setBookerID(int bookerID) {
        this.bookerID = bookerID;
    }

    /**
     *
     * @return
     */
    public int getDaysOfStay()
    {
        return (int) ChronoUnit.DAYS.between(checkIn, checkOut);
    }
}
