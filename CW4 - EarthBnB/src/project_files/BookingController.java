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
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BookingController extends MainframeContentPanel implements Initializable {


    TableColumn propertyNameCol;
    TableColumn propertyBoroughCol;
    private ObservableList<AirbnbListing> data = FXCollections.observableArrayList();
    List<LocalDate> reservedDates = new ArrayList<>();

    AirbnbListing selectedListing;

    @FXML
    TableView favoritesTable;

    @FXML
    Label totalLabel, calculationLabel;

    @FXML
    DatePicker checkInDate, checkOutDate;

    private boolean usingDatabase;

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

    private String getCalculationString(int price, long daysOfStay) {
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

    ArrayList<Reservation> reservations;
    public void updateCalendar() {
        System.out.println("updatecalendar called");
        if(!usingDatabase) {
            reservations = mainFrameController.getOfflineReservations();
        } else {
            reservations = new ArrayList<>();
            String getReservations = "SELECT * FROM booking";

            try {
                DatabaseConnection connection = new DatabaseConnection();
                Connection connectDB = connection.getConnection();
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
    }


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

                    if (selectedListing != null) {
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
                    }
                if(usingDatabase) {
                    try {
                        DatabaseConnection connection = new DatabaseConnection();
                        Connection connectDB = connection.getConnection();
                        System.out.println("violation: " + violation);
                        Statement statement = connectDB.createStatement();
                        if (violation == false) {
                            System.out.println("executed");
                            statement.executeUpdate(createBooking);
                            System.out.println("removed");
                            //data.remove(favoritesTable.getSelectionModel().getSelectedItem());
                            favoritesTable.getSelectionModel().clearSelection();
                        }
                    } catch (Exception e) {

                    }
                } else {
                    if(violation == false) {
                        ArrayList<Reservation> reservations = mainFrameController.getOfflineReservations();
                        Reservation reservation = new Reservation(reservations.size() + 1, checkInDate.getValue(), checkOutDate.getValue(), currentUser.getAccountID(), usersData.getNumberOfPeople(),
                                selectedListing.getPrice() * daysBetween(checkInDate.getValue(), checkOutDate.getValue()), selectedListing.getId());
                        reservations.add(reservation);
                        currentUser.addOfflineReservation(reservation);
                        System.out.println("Created offline reservation");
                        favoritesTable.getSelectionModel().clearSelection();
                        selectedListing = null;
                    }
                }
            } else {
                // Remove property from saved list
            }
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
