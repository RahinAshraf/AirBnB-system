package project_files;

import java.util.Date;

public class BookingData {

    private Date checkIn;
    private Date checkOut;
    private int numberOfPeople;

    public BookingData(Date checkIn, Date checkOut, int numberOfPeople){
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.numberOfPeople = numberOfPeople;
    }


}
