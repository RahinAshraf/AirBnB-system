package project_files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AccountPanelController implements Initializable {

    //private ArrayList<AirbnbListing> filteredListings; = new ArrayList<>();
    //private Listings listings;
    private Account currentUser;
    private ObservableList<String> userInformationList = FXCollections.observableArrayList();
    private ObservableList<Reservation> upcomingTrips = FXCollections.observableArrayList();

    DatabaseConnection connection = new DatabaseConnection();
    Connection connectDB = connection.getConnection();

    @FXML
    ListView informationList;

    @FXML
    TableView tripsTable;

    public void initializeAccount(Account currentUser) {
        //this.listings = listings;
        //filteredListings = listings.getFilteredListings();

        this.currentUser = currentUser;
        loadUserInformation();
        loadData();
        informationList.setItems(userInformationList);
        tripsTable.setItems(upcomingTrips);


        TableColumn bookingIDCol = new TableColumn<>("Reservation");
        bookingIDCol.setMinWidth(120);
        bookingIDCol.setMaxWidth(150);
        //boroughPriceCol.setCellFactory(TextFieldTableCell.forTableColumn());
        PropertyValueFactory bookingTemp = new PropertyValueFactory<AirbnbListing, Integer> (("reservationID"));
        bookingIDCol.setCellValueFactory(bookingTemp);

        tripsTable.getColumns().addAll(bookingIDCol);


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

        // Load the content of the arraylist inside the upcomingtrips table
    }

    private String hashPW(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        messageDigest.reset();
        messageDigest.update(password.getBytes(StandardCharsets.UTF_8));

        return String.format("%0128x", new BigInteger(1, messageDigest.digest()));
    }
}
