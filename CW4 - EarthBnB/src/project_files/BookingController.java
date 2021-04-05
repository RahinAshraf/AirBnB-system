package project_files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


/**
 * This class provides a controller for the bookingView. It provides the user with the possibility to select a saved
 * property from a TableView and reserve it if it has not been booked by someone else at that time period.
 * The user can also edit the settings for the chosen dates.
 *
 * @author  Valentin Magis, Rahin Ashraf, Vandad Vafai Tabrizi, Barnabas Szalai
 * @version 1.0
 * @since   2021-03-11
 */
public class BookingController extends MainframeContentPanel implements Initializable {


    TableColumn propertyNameCol;
    TableColumn propertyBoroughCol;
    private ObservableList<AirbnbListing> data = FXCollections.observableArrayList();
    List<LocalDate> reservedDates = new ArrayList<>();

    AirbnbListing selectedListing;
    ArrayList<Reservation> reservations;

    @FXML
    TableView favoritesTable;

    @FXML
    Label totalLabel, calculationLabel;

    @FXML
    DatePicker checkInDate, checkOutDate;

    private boolean usingDatabase;


    /**
     * This constructor method is used to initialize the booking window, and some of its instance variables.
     */
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
     * @param listing   the listing that has been selected from the TableView
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

    private String getCalculationString(int price, long daysOfStay) {
        return "" + price + "€ x " + daysOfStay + " days";
    }


    /**
     * This method is used to iterate through all of the saved properties of the user, and check whether they are
     * valid regarding the current filtered settings. If they are, they are added to the data, which is then displayed
     * in the TableView.
     */
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
     * This method is used to update the calendar by highlighting the days which the property is already reserved in
     * red. If the program is using the database, the reservations are loaded from the database. Otherwise, the
     * reservations are loaded from the user.
     */
    public void updateCalendar() {
        System.out.println("updatecalendar called");
        if(!usingDatabase) {
            reservations = OfflineData.getReservations();
        } else {
            reservations = new ArrayList<>();
            String getReservations = "SELECT * FROM booking";

            try {
                DatabaseConnection connection = new DatabaseConnection();
                Connection connectDB = connection.getConnection();
                Statement statement = connectDB.createStatement();
                ResultSet queryResult = statement.executeQuery(getReservations);
                while (queryResult.next()) {
                    reservations.add(AccountPanelController.createReservation(queryResult));
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                exception.getCause();

            }
        }

        updateDate();

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
        checkOutDate.setDayCellFactory(reservedDayCellFactory);
    }


    /**
     * This method is called when the calendar is updated. It iterates through the previous reservations made by
     * everybody searching for the selected property of the user, and adds the dates reserved for that property in the
     * reservedDates.
     */
    public void updateDate() {
        System.out.println("total reservations: " + reservations.size());
        reservedDates.clear();
        for(int i = 0; i < reservations.size(); i++) {
            Reservation reservation = reservations.get(i);
            System.out.println(reservation.getArrival());
            if(reservation.getListingID().equals(selectedListing.getId())) {
                System.out.println("there is reservation for this property");
                LocalDate date = reservation.getArrival();
                while(date.isBefore(reservation.getDeparture().plusDays(1))) {
                    reservedDates.add(date);
                    System.out.println("added" + date);
                    date = date.plusDays(1);
                }
            }
        }
    }

    /**
     * Initiated when a row in the tableview has been clicked. Loads the selected property into the booking panel.
     * @throws IOException
     */
    @FXML
    public void rowClicked(MouseEvent e) throws IOException {
        if (!(favoritesTable.getSelectionModel().getSelectedItem() == null)) {
            Object chosenObject = favoritesTable.getSelectionModel().getSelectedItem();
            if (chosenObject.getClass() == AirbnbListing.class) { // Safety check for cast
                AirbnbListing chosenProperty = (AirbnbListing) chosenObject;
                selectedListing = chosenProperty;
                totalLabel.setText("Total: " + (selectedListing.getPrice() * currentUser.getBookingData().getDaysOfStay()) + "€");
                calculationLabel.setText(getCalculationString(selectedListing.getPrice(), currentUser.getBookingData().getDaysOfStay()));
                loadFromFavouritesTable(chosenProperty);
            }
            updateCalendar();
        } else {
            System.out.println("selected item is null");
        }
    }

    private long daysBetween(LocalDate date1, LocalDate date2) {
        return ChronoUnit.DAYS.between(date1, date2);
    }



    /**
     * This method is called when the user changes the dates of the calendar on the booking page. The dates are
     * validated and the price and calculations are updated based on the newly selected dates.
     */
    @FXML
    private void userCalendarChange(ActionEvent e) {

        if(selectedListing != null) {
            //AirbnbListing listing = (AirbnbListing) favoritesTable.getSelectionModel().getSelectedItem();
            long daysBetween = ChronoUnit.DAYS.between(checkInDate.getValue(), checkOutDate.getValue());

            if (daysBetween <= 0) {
                checkOutDate.setValue(checkInDate.getValue().plusDays(1));

                calculationLabel.setText(getCalculationString(selectedListing.getPrice(), 1));
                totalLabel.setText("Total: " + (selectedListing.getPrice() * 1) + "€");
            } else {
                LocalDate localDate = LocalDate.now();
                if ((daysBetween(checkInDate.getValue(), localDate) > 0) || (daysBetween(checkOutDate.getValue(), localDate) > 0)) {
                    checkInDate.setValue(localDate);
                    checkOutDate.setValue(localDate.plusDays(1));

                    calculationLabel.setText(getCalculationString(selectedListing.getPrice(), 1));
                    totalLabel.setText("Total: " + (selectedListing.getPrice() * 1) + "€");
                } else {
                    calculationLabel.setText(getCalculationString(selectedListing.getPrice(), daysBetween));
                    totalLabel.setText("Total: " + (selectedListing.getPrice() * daysBetween) + "€");
                }
            }
        } else {
            if(e.getTarget() == checkInDate) {
                checkInDate.setValue(checkOutDate.getValue());
            } else {
                checkOutDate.setValue(checkInDate.getValue());
            }
            calculationLabel.setText("Unknown");
            totalLabel.setText("Total: unknown");
        }
    }


    /**
     This method is called when the user clicks on reserve button. It validates the reservation by checking if the
     selected dates in the calendar are not reserved yet for the selected property. If there are no violations in
     the validation, a new reservation is instantiated. The new reservation is saved either in the database, if it is
     turned on, or in the user's Account object.
     */
    public void reserveProperty() {
        if(favoritesTable.getSelectionModel().getSelectedItem() != null && favoritesTable.getSelectionModel().getSelectedItem().getClass() == AirbnbListing.class) {
            selectedListing = (AirbnbListing) favoritesTable.getSelectionModel().getSelectedItem();
            LocalDate currentDate = LocalDate.now();
            if (selectedListing != null) {
                System.out.println(currentUser.getAccountID());
                BookingData usersData = currentUser.getBookingData();
                String createBooking = "INSERT INTO booking VALUES (NULL, '" + checkInDate.getValue() + "', '" + checkOutDate.getValue() + "', '" + currentUser.getAccountID() + "', '" +
                        usersData.getNumberOfPeople() + "', '" + selectedListing.getPrice() * daysBetween(checkInDate.getValue(), checkOutDate.getValue()) + "', '" + selectedListing.getId() + "', '" + currentDate + "')";
                //String checkSignup = "SELECT * FROM account WHERE username = '"+ nameField.getText() + "'";

                    boolean violation = false;
                    int index = 0;

                    while (index < reservedDates.size() && !violation) {
                        System.out.println("# of reserved dates: " + reservedDates.size());
                        if (checkInDate.getValue().isBefore(reservedDates.get(index)) && checkOutDate.getValue().isAfter(reservedDates.get(index))) {
                            System.out.println("Some days are reserved in between your selection");
                            violation = true;
                        } else if (checkInDate.getValue().equals(reservedDates.get(index)) || checkOutDate.getValue().equals(reservedDates.get(index))) {
                            System.out.println("Some days are reserved at your selections");
                            violation = true;
                        }
                        index++;
                    }
                if(usingDatabase) {
                    try {
                        DatabaseConnection connection = new DatabaseConnection();
                        Connection connectDB = connection.getConnection();
                        System.out.println("violation: " + violation);
                        Statement statement = connectDB.createStatement();
                        if (!violation) {
                            System.out.println("executed");
                            statement.executeUpdate(createBooking);
                            System.out.println("removed");
                            //data.remove(favoritesTable.getSelectionModel().getSelectedItem());
                            favoritesTable.getSelectionModel().clearSelection();
                        }
                    } catch (Exception e) {

                    }
                } else {
                    if(!violation) {
                        ArrayList<Reservation> reservations = OfflineData.getReservations();
                        Reservation reservation = new Reservation(reservations.size() + 1, checkInDate.getValue(), checkOutDate.getValue(), currentUser.getAccountID(), usersData.getNumberOfPeople(),
                                selectedListing.getPrice() * daysBetween(checkInDate.getValue(), checkOutDate.getValue()), selectedListing.getId());
                        reservations.add(reservation);
                        currentUser.addOfflineReservation(reservation);
                        OfflineData.addReservation(reservation);
                        System.out.println("Created offline reservation");
                        favoritesTable.getSelectionModel().clearSelection();
                        selectedListing = null;
                    }
                }
            } else {

            }
        }


    }

    /**
     * This method is used to validate the reservation to the current settings when it is selected from the TableView.
     * If it is not compatible with the current settings, an error message is issued.
     * @param chosenProperty    the property that is selected from the TableView
     */
    private void loadFromFavouritesTable(AirbnbListing chosenProperty) {
        if (canBeBooked(chosenProperty))
            initializeWithProperty(chosenProperty);
        else
            incompatibleBooking();
    }

    /**
     * This method checks whether a particular property matches the current settings of the user.
     * @param property  the property that is selected from the TableView
     */
    private boolean canBeBooked(AirbnbListing property) {
        BookingData bookingData = currentUser.getBookingData();
        return property.getMinimumNights() <= bookingData.getDaysOfStay()
                && property.getMaximumNights() >= bookingData.getDaysOfStay()
                && property.getMaxGuests() >= bookingData.getNumberOfPeople();
                // && database.isFree();
    }


    /**
     * This method is used initialize the TableView when the window is created. It loads the table with a list of
     * peroperties that have been saved by the current user. It creates columns for the table to display the name and
     * the neighbourhood of the property, so that it can be identified.
     * @param listings  the listings with the current filters
     * @param currentUser   the current logged in user
     */
    @Override
    public void initializeData(Listings listings, Account currentUser) {
        System.out.println("called");
        this.currentUser = currentUser;
        this.listings = listings;
        loadSavedProperties();

        propertyNameCol = new TableColumn("Name");
        propertyNameCol.setMinWidth(300);
        propertyNameCol.setMaxWidth(300);
        propertyNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        propertyNameCol.setCellValueFactory(new PropertyValueFactory<AirbnbListing, String>("name"));

        propertyBoroughCol = new TableColumn("Borough");
        propertyBoroughCol.setMinWidth(300);
        propertyBoroughCol.setMaxWidth(300);
        propertyBoroughCol.setCellFactory(TextFieldTableCell.forTableColumn());
        propertyBoroughCol.setCellValueFactory(new PropertyValueFactory<AirbnbListing, String>("neighbourhood"));

        favoritesTable.getColumns().clear();
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
