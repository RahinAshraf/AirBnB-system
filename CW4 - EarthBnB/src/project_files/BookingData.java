package project_files;

import java.time.LocalDate;
import java.util.Date;

/**
 * The BookingData Class stores the data received from the welcome panel.
 *
 * @author Vandad Vafai Tabrizi
 */

public class BookingData {

    private LocalDate checkIn;
    private LocalDate checkOut;
    private int numberOfPeople;
    private int bookerID;
    private int minPrice;
    private int maxPrice;

    public BookingData(LocalDate checkIn, LocalDate checkOut, int numberOfPeople, int bookerID){
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.numberOfPeople = numberOfPeople;
        this.bookerID = bookerID;
    }

    public void setPriceRange(int minPrice, int maxPrice)
    {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }
}
