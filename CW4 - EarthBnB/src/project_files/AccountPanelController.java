package project_files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * This class provides the controller for the accountView. It is used to display information about the current user,
 * and show the upcoming trips that are reserved by the user.
 *
 * @author  Valentin Magis, Rahin Ashraf, Vandad Vafai Tabrizi, Barnabas Szalai
 * @version 1.0
 * @since   2021-03-11
 */
public class AccountPanelController implements Initializable {

    //private ArrayList<AirbnbListing> filteredListings; = new ArrayList<>();
    //private Listings listings;
    private Account currentUser;
    private ObservableList<String> userInformationList = FXCollections.observableArrayList();
    private ObservableList<Reservation> upcomingTrips = FXCollections.observableArrayList();
    private Listings listings;

    private MainFrameController mainFrameController;

    private Object chosenObject;
    private Reservation chosenProperty;

    @FXML
    private ListView informationList;

    @FXML
    private MenuItem checkBookingItem;

    @FXML
    private TableView tripsTable;

    @FXML
    private ContextMenu checkBookingCMenu;


    /**
     * This method is used to initialize the fields that store information about the user. It also creates the columns
     * for the tableview that displays the upcoming reservations and adds the personal details to the list.
     * @param currentUser the Account of the current user
     * @param mainFrameController the instance of the MainFrameController that instantiated the accountView
     * @param listings  the Listings object containing all of the properties
     */
    public void initializeAccount(Account currentUser, MainFrameController mainFrameController, Listings listings) {
        //this.listings = listings;
        //filteredListings = listings.getFilteredListings();

        this.currentUser = currentUser;
        this.mainFrameController = mainFrameController;
        this.listings = listings;
        loadUserInformation();
        loadData();
        informationList.setItems(userInformationList);
        tripsTable.setItems(upcomingTrips);

        TableColumn bookingIDCol = new TableColumn<>("ID");
        bookingIDCol.setMinWidth(40);
        bookingIDCol.setMaxWidth(50);
        //boroughPriceCol.setCellFactory(TextFieldTableCell.forTableColumn());
        PropertyValueFactory bookingTemp = new PropertyValueFactory<AirbnbListing, Integer> (("reservationID"));
        bookingIDCol.setCellValueFactory(bookingTemp);

        TableColumn checkInCol = new TableColumn<>("Check In");
        checkInCol.setMinWidth(60);
        checkInCol.setMaxWidth(80);
        //boroughPriceCol.setCellFactory(TextFieldTableCell.forTableColumn());
        PropertyValueFactory checkInTemp = new PropertyValueFactory<AirbnbListing, Integer> (("arrival"));
        checkInCol.setCellValueFactory(checkInTemp);

        TableColumn checkOutCol = new TableColumn<>("Check Out");
        checkOutCol.setMinWidth(60);
        checkOutCol.setMaxWidth(80);
        //boroughPriceCol.setCellFactory(TextFieldTableCell.forTableColumn());
        PropertyValueFactory checkOutTemp = new PropertyValueFactory<AirbnbListing, Integer> (("departure"));
        checkOutCol.setCellValueFactory(checkOutTemp);

        tripsTable.getColumns().addAll(bookingIDCol, checkInCol, checkOutCol);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {}


    /**
     * A simple method to store basic personal information about the current user into an observable list that can be
     * displayed by a listview.
     */
    public void loadUserInformation() {
        userInformationList.add("Account ID: " + (currentUser.getAccountID()));
        userInformationList.add("Username: " + currentUser.getUsername());
        userInformationList.add("Email Address: " + currentUser.getEmailAddress());
    }


    /**
     * This method is used to load all of the reservations that have been made by the current user. If the application
     * is using the database, these reservations are requested from the database. Otherwise, they are loaded from an
     * instance variable of the user Account.
     */
    public void loadData() {
        // Generate an arraylist of all of the bookings of the currentUser
        if(mainFrameController.isUsingDatabase()) {
            DatabaseConnection connection = new DatabaseConnection();
            Connection connectDB = connection.getConnection();

            //Check if using database
            LocalDate currentDate = LocalDate.now();
            try {
                String getPropertiesBooked = "SELECT * FROM booking WHERE bookerID = '" + currentUser.getAccountID() + "' AND DEPARTURE > '" + currentDate + "'";
                Statement statement = connectDB.createStatement();
                ResultSet queryResult = statement.executeQuery(getPropertiesBooked);
                while (queryResult.next()) {
                    System.out.println("loaded data to account panel");
                    upcomingTrips.add(createReservation(queryResult));
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                exception.getCause();

            }

        } else {
            ArrayList<Reservation> offlineReservations = currentUser.getOfflineReservations();
            for(int i = 0; i < offlineReservations.size(); i++) {
                upcomingTrips.add(offlineReservations.get(i));
            }
        }

    }


    /**
     * This method is used to create a Reservation object from a booking query result.
     * @param   queryResult the queryResult from a successful database query
     * @return  Reservation the Reservation object that is created
     */
     static Reservation createReservation(ResultSet queryResult) throws SQLException {
        LocalDate arrivalDate = new Date(queryResult.getDate(2).getTime()).toLocalDate();
        LocalDate departureDate = new Date(queryResult.getDate(3).getTime()).toLocalDate();
        Reservation reservation = new Reservation(
                queryResult.getInt(1),
                arrivalDate,
                departureDate,
                queryResult.getInt(4),
                queryResult.getInt(5),
                queryResult.getInt(6),
                queryResult.getString(7)
        );
        return reservation;
    }


    /**
     * This method is called when the user right clicks on a row when selecting an upcoming reservation. When this
     * occurs, a contextmenu appears near the user's mouse location displaying some menuitems.
     */
    @FXML
    private void rowClicked(MouseEvent e) throws IOException {
        if (e.isSecondaryButtonDown()) {
            checkBookingCMenu.show(tripsTable, e.getScreenX(), e.getScreenY());
        }
    }

    /**
     * This method is called when the user clicks on the menuItem inside the contextmenu upon right clicking on an
     * upcoming reservation. It instantiates a new fxml view and displays it in a new window. It also passes on some
     * information about the selected property to the controller of the instantiated view.
     */
    @FXML
    private void checkBooking(ActionEvent e) throws IOException {
            //if (chosenObject.getClass() == Reservation.class) { // Safety check for cast
            chosenObject = tripsTable.getSelectionModel().getSelectedItem();
        if (chosenObject != null) {
            chosenProperty = (Reservation) chosenObject;

            FXMLLoader displayerLoader = new FXMLLoader(getClass().getResource("accountBookingView.fxml"));
            Parent root = displayerLoader.load();
            Stage newStage = new Stage();
            newStage.setTitle("Reservation");
            newStage.setScene(new Scene(root, 400, 300));
            AccountBookingController accountBookingController = displayerLoader.getController();
            accountBookingController.LoadData(chosenProperty, this);
            newStage.show();

            System.out.println("MenuItem clicked");
            //}
        }

    }

    /**
     * This method is called when the user clicks on the button to show information about the property in the
     * accountBookingView. It instantiates a new fxml view of type PropertyDisplayerView, which displays information
     * about the property. It also passes on some data to the controller of the newly created view, and disables the
     * saving and booking options from the new window.
     */
    public void checkProperty() throws IOException {
        FXMLLoader displayerLoader = new FXMLLoader(getClass().getResource("PropertyDisplayerView.fxml"));
        Parent root = displayerLoader.load();
        Stage newStage = new Stage();
        newStage.setTitle("Property");
        newStage.setScene(new Scene(root, 950, 650));

        PropertyDisplayerController propertyDisplayer = displayerLoader.getController();
        propertyDisplayer.loadData(findListingByID(), currentUser); // Load the data into the window.
        propertyDisplayer.setMainWindowController(mainFrameController);
        propertyDisplayer.hideNavigationButtons();
        //propertyDisplayer.setBoroughPropertiesController(this);
        newStage.show();
    }

    /**
     * This method searches for a property in the listings using the property ID of the selected reservation
     * in the tableview. It calls another method that executes a binary search.
     * @return  AirbnbListing   the AirbnbListing that the ID corresponds to. Returns null if not found.
     */
    private AirbnbListing findListingByID() {
        System.out.println("chosenpropertyID: " + chosenProperty.getListingID());
        ArrayList<AirbnbListing> originalListings = listings.getOriginalListings();
        originalListings.sort(AirbnbListing::compareTo); // make sure its in the right order
        return Listings.iterativeSearch(originalListings, chosenProperty.getListingID());
    }
}
