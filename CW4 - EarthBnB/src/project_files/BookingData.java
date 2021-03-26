package project_files;

import java.time.LocalDate;
import java.util.Date;

public class BookingData {

    private LocalDate checkIn;
    private LocalDate checkOut;
    private int numberOfPeople;
    private int bookerID;

    public BookingData(LocalDate checkIn, LocalDate checkOut, int numberOfPeople, int bookerID){
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.numberOfPeople = numberOfPeople;
        this.bookerID = bookerID;
    }
}
