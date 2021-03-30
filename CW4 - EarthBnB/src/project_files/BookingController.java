package project_files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class BookingController extends MainframeContentPanel implements Initializable {


    //ArrayList<AirbnbListing> listings;
    TableColumn propertyNameCol;
    private ObservableList<AirbnbListing> data = FXCollections.observableArrayList();

    DatabaseConnection connection = new DatabaseConnection();
    Connection connectDB = connection.getConnection();

    AirbnbListing selectedListing;

    @FXML
    TableView favoritesTable;

    @FXML
    Label totalLabel, calculationLabel;

    @FXML
    DatePicker checkInDate, checkOutDate;

    boolean usingDatabase;

    public BookingController()
    {
        name = "Bookings";
        currentUser = null;
        usingDatabase = false;
    }

    public void setUsingDatabase(boolean usingDatabase) {
        this.usingDatabase = usingDatabase;
    }




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //currentUser = null;
        selectedListing = null;

    }

    /**
     * Initialize the booking panel with a given property
     * @param listing
     */
    public void initializeWithProperty(AirbnbListing listing)
    {
        BookingData bookingData = currentUser.getBookingData();
        checkInDate.setValue(bookingData.getCheckIn());
        checkOutDate.setValue(bookingData.getCheckOut());
        calculationLabel.setText(getCalculationString(listing.getPrice(), bookingData.getDaysOfStay()));
        totalLabel.setText("Total: " + (listing.getPrice() * bookingData.getDaysOfStay()) + "€");
    }

    private String getCalculationString(int price, int daysOfStay) {
        return "" + price + "€ x " + daysOfStay + " days";
    }


    public void loadSavedProperties() {
        data.clear();
        System.out.println(listings.getFilteredListings().size());
        for(int i = 0; i<listings.getFilteredListings().size(); i++) {
            if(currentUser.getSavedProperties().contains(listings.getFilteredListings().get(i))) {
                data.add(listings.getFilteredListings().get(i));
                System.out.println("added");
            }
        }



    }

    /**
     * Initiated when a row in the tableview has been clicked. Loads the selected property into the booking panel.
     * @throws IOException
     */
    @FXML
    public void rowClicked(MouseEvent e) throws IOException {
        if(!(favoritesTable.getSelectionModel().getSelectedItem() == null)) {
            Object chosenObject = favoritesTable.getSelectionModel().getSelectedItem();
            if (chosenObject.getClass() == AirbnbListing.class) { // Safety check for cast
                AirbnbListing chosenProperty = (AirbnbListing) chosenObject;
                selectedListing = chosenProperty;
                totalLabel.setText("Total: " + (selectedListing.getPrice() * currentUser.getBookingData().getDaysOfStay()) + "€");
                calculationLabel.setText(getCalculationString(selectedListing.getPrice(), currentUser.getBookingData().getDaysOfStay()));
                if (e.getClickCount() == 2)
                {
                    loadFromFavouritesTable(chosenProperty);
                }
            }



            List<LocalDate> reservedDates = new ArrayList<>();
            ArrayList<Reservation> reservations;

            if(!usingDatabase) {
                reservations = mainWindowController.getOfflineReservations();
            } else {
                reservations = new ArrayList<>();
                String getReservations = "SELECT * FROM booking";

                try {
                    Statement statement = connectDB.createStatement();
                    ResultSet queryResult = statement.executeQuery(getReservations);
                    while (queryResult.next()) {
                        LocalDate arrivalDate = new java.sql.Date(queryResult.getDate(2).getTime()).toLocalDate();
                        LocalDate departureDate = new java.sql.Date(queryResult.getDate(3).getTime()).toLocalDate();
                        Reservation reservation = new Reservation(
                                queryResult.getInt(1),
                                arrivalDate,
                                departureDate,
                                queryResult.getInt(4),
                                queryResult.getInt(5),
                                queryResult.getInt(6),
                                queryResult.getString(7)
                        );
                        reservations.add(reservation);
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                    exception.getCause();

                }
            }

            System.out.println("total reservations: " + reservations.size());
            for(int i = 0; i < reservations.size(); i++) {
                Reservation reservation = reservations.get(i);
                System.out.println(reservation.getArrival());
                if(reservation.getListingID().equals(selectedListing.getId())) {
                    System.out.println("there is resevation for this property");
                    LocalDate date = reservation.getArrival();
                    while(date.isBefore(reservation.getDeparture())) {
                        reservedDates.add(date);
                        System.out.println("added" + date);
                        date = date.plusDays(1);
                    }
                }
            }

            Callback<DatePicker, DateCell> reservedDayCellFactory = new Callback<>() {
                public DateCell call(final DatePicker datePicker) {
                    return new DateCell() {
                        @Override
                        public void updateItem(LocalDate item, boolean empty) {
                            // Must call super
                            super.updateItem(item, empty);

                            //Show reserved dates in red
                            if (!empty && item != null) {
                                if (reservedDates.contains(item)) {
                                    this.setStyle("-fx-background-color: #F96E3A");
                                }
                            }
                        }
                    };
                }
            };
            checkInDate.setDayCellFactory(reservedDayCellFactory);
        }


    }


    public void reserveProperty() {
        if(usingDatabase) {
            System.out.println(currentUser.getAccountID());
            BookingData usersData = currentUser.getBookingData();
            String createBooking = "INSERT INTO booking VALUES (NULL, '" + usersData.getCheckIn() + "', '" + usersData.getCheckOut() + "', '" + currentUser.getAccountID() + "', '" +
                    usersData.getNumberOfPeople() + "', '" + selectedListing.getPrice() * usersData.getDaysOfStay() + "', '" + selectedListing.getId() + "')";
            //String checkSignup = "SELECT * FROM account WHERE username = '"+ nameField.getText() + "'";

            try {
                Statement statement = connectDB.createStatement();
                if (selectedListing != null) {
                    statement.executeUpdate(createBooking);
                }
            } catch (Exception e) {

            }
        } else {
            BookingData usersData = currentUser.getBookingData();
            ArrayList<Reservation> reservations = mainWindowController.getOfflineReservations();
            Reservation reservation = new Reservation(reservations.size()+1, usersData.getCheckIn(), usersData.getCheckOut(), currentUser.getAccountID(), usersData.getNumberOfPeople(),
                    selectedListing.getPrice() * usersData.getDaysOfStay(), selectedListing.getId());
            reservations.add(reservation);
        }





    }

    private void loadFromFavouritesTable(AirbnbListing chosenProperty) {
        if (canBeBooked(chosenProperty))
            initializeWithProperty(chosenProperty);
        else
            incompatibleBooking();
    }

    private boolean canBeBooked(AirbnbListing property) {
        BookingData bookingData = currentUser.getBookingData();
        return property.getMinimumNights() <= bookingData.getDaysOfStay()
                && property.getMaximumNights() >= bookingData.getDaysOfStay()
                && property.getMaxGuests() >= bookingData.getNumberOfPeople();
                // && database.isFree();
    }


    @Override
    public void initializeList(Listings listings, Account currentUser) {
        System.out.println("called");
        this.currentUser = currentUser;
        this.listings = listings;
        loadSavedProperties();

        propertyNameCol = new TableColumn("Name");
        propertyNameCol.setMinWidth(300);
        propertyNameCol.setMaxWidth(300);
        propertyNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        propertyNameCol.setCellValueFactory(new PropertyValueFactory<AirbnbListing, String>("name"));

        TableColumn propertyBoroughCol = new TableColumn("Borough");
        propertyBoroughCol.setMinWidth(300);
        propertyBoroughCol.setMaxWidth(300);
        propertyBoroughCol.setCellFactory(TextFieldTableCell.forTableColumn());
        propertyBoroughCol.setCellValueFactory(new PropertyValueFactory<AirbnbListing, String>("neighbourhood"));

        favoritesTable.getColumns().addAll(propertyNameCol, propertyBoroughCol);
        favoritesTable.setItems(data);
    }


    /**
     * An alert which occurs when the check-in date is not valid.
     */
    private void incompatibleBooking(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText("Incompatible Property");
        alert.setContentText("The selected property does not match your search request.");
        alert.showAndWait();
    }

    @Override
    public void updatePanel() {
        // invoked when price range is changed
    }
}
