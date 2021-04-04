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
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AccountPanelController implements Initializable {

    //private ArrayList<AirbnbListing> filteredListings; = new ArrayList<>();
    //private Listings listings;
    private Account currentUser;
    private ObservableList<String> userInformationList = FXCollections.observableArrayList();
    private ObservableList<Reservation> upcomingTrips = FXCollections.observableArrayList();
    Listings listings;

    MainFrameController mainFrameController;

    Object chosenObject;
    Reservation chosenProperty;

    @FXML
    ListView informationList;

    @FXML
    MenuItem checkBookingItem;

    @FXML
    TableView tripsTable;

    @FXML
    ContextMenu checkBookingCMenu;

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
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }




    public void loadUserInformation() {
        userInformationList.add("Account ID: " + (currentUser.getAccountID()));
        userInformationList.add("Username: " + currentUser.getUsername());
        userInformationList.add("Email Address: " + currentUser.getEmailAddress());
    }


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
                    System.out.println("loaded data to account panel");
                    upcomingTrips.add(reservation);
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

    @FXML
    private void rowClicked(MouseEvent e) throws IOException {
        if (e.isSecondaryButtonDown()) {

//            Object chosenObject = propertiesTable.getSelectionModel().getSelectedItem();
//            if (chosenObject.getClass() == AirbnbListing.class) { // Safety check for cast
//                AirbnbListing chosenProperty = (AirbnbListing) chosenObject;
//                openPropertyDisplayView(chosenProperty);
//            }

            checkBookingCMenu.show(tripsTable, e.getScreenX(), e.getScreenY());

        }


    }

    @FXML
    private void checkBooking(ActionEvent e) throws IOException {
            //if (chosenObject.getClass() == Reservation.class) { // Safety check for cast
            chosenObject = tripsTable.getSelectionModel().getSelectedItem();
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

    public void checkProperty() throws IOException {
        FXMLLoader displayerLoader = new FXMLLoader(getClass().getResource("PropertyDisplayerView.fxml"));
        Parent root = displayerLoader.load();
        Stage newStage = new Stage();
        newStage.setTitle("Property");
        newStage.setScene(new Scene(root, 950, 650));

        PropertyDisplayerController propertyDisplayer = displayerLoader.getController();
        propertyDisplayer.loadData(findListingByID(), currentUser); // Load the data into the window.
        propertyDisplayer.setMainWindowController(mainFrameController);
        //propertyDisplayer.setBoroughPropertiesController(this);
        newStage.show();
    }

    private AirbnbListing findListingByID() {
        System.out.println("chosenpropertyID: " + chosenProperty.getListingID());
        ArrayList<AirbnbListing> originalListings = listings.getOriginalListings();


//        for(int i=0; i<originalListings.size(); i++) {
//            if(originalListings.get(i).getId().equals(chosenProperty.getListingID())) {
//                System.out.println("Listing found");
//                return originalListings.get(i);
//            }
//        }
        return Listings.iterativeSearch(originalListings, chosenProperty.getListingID());

        //Also has to return null
    }



    private String hashPW(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        messageDigest.reset();
        messageDigest.update(password.getBytes(StandardCharsets.UTF_8));

        return String.format("%0128x", new BigInteger(1, messageDigest.digest()));
    }
}
