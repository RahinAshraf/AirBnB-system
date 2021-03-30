package project_files;

import java.time.LocalDate;

public class Reservation {

    private int reservationID;
    private LocalDate arrival;
    private LocalDate departure;
    private Account bookerAccount;
    private int numberOfGuests;
    private double price;

    Reservation(int reservationID, LocalDate arrival, LocalDate departure, Account bookerAccount,
                int numberOfGuests, double price) {
        this.reservationID = reservationID;
        this.arrival = arrival;
        this.departure = departure;
        this.bookerAccount = bookerAccount;
        this.numberOfGuests = numberOfGuests;
        this.price = price;
    }
}
