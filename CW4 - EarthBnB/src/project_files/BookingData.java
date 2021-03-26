package project_files;

import java.util.Date;

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
