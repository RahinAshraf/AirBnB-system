package project_files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BookingController extends MainframeContentPanel implements Initializable {


    ArrayList<AirbnbListing> listings;
    Account currentUser;
    TableColumn propertyNameCol;
    private ObservableList<AirbnbListing> data = FXCollections.observableArrayList();

    @FXML
    TableView favoritesTable;

    @FXML
    Label totalLabel, calculationLabel;

    @FXML
    DatePicker checkInDate, checkOutDate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentUser = null;
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
        System.out.println(listings.size());
        for(int i = 0; i<listings.size(); i++) {
            if(currentUser.getSavedProperties().contains(listings.get(i))) {
                data.add(listings.get(i));
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
        if (e.getClickCount() == 2)
        {
            Object chosenObject = favoritesTable.getSelectionModel().getSelectedItem();
            if (chosenObject.getClass() == AirbnbListing.class) { // Safety check for cast
                AirbnbListing chosenProperty = (AirbnbListing) chosenObject;
                loadFromFavouritesTable(chosenProperty);
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
    public void initializeList(ArrayList<AirbnbListing> listings, Account currentUser) {
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
}
