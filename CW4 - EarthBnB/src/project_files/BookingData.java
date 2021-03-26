package project_files;

import java.util.Date;

/**
 * The BookingData Class stores the data received from the welcome panel.
 *
 * @author Vandad Vafai Tabrizi
 */

public class BookingData {

    private Date checkIn;
    private Date checkOut;
    private int numberOfPeople;
    private int bookerID;

    public BookingData(Date checkIn, Date checkOut, int numberOfPeople, int bookerID){
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.numberOfPeople = numberOfPeople;
        this.bookerID = bookerID;
    }


}
