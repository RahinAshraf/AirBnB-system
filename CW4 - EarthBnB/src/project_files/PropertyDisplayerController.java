package project_files;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Class PropertyDisplayerController - The controller for displaying the information about a single property.
 * It loads images, displays different texts, shows ratings and has google maps embedded for showing the location of
 * the property.
 * The mechanisms for opening the map are inspired by http://java-buddy.blogspot.com/2012/03/embed-google-maps-in-javafx-webview.html.
 * @author Valentin Magis
 * @version 1.0
 */
public class PropertyDisplayerController implements Initializable {

    private AirbnbListing displayedListing;
    private Account currentUser;
    private MainFrameController mainFrameController;
    private BoroughPropertiesController boroughPropertiesController;

    @FXML
    private ImageView propertyImg, hostLargeImg;
    @FXML
    private Label bathroomsLbl, hostIsSuperHostLbl, hostNameLbl, boroughLbl, neighbourHoodDescriptionLbl, bedsLbl, propertyTypeLbl, propertyNameLbl;
    @FXML
    private Button saveButton, bookButton;
    @FXML
    private Label pricePerNight;
    @FXML
    private Label maxNumberOfPeople;
    @FXML
    private TextArea amenitiesText; // Expand thing, only show a couple
    @FXML
    private ProgressBar cleanlinessBar, communicationBar, locationBar, totalBar;
    @FXML
    private WebView mapWebView; // View for displaying webEngine
    private WebEngine webEngine; // Non-visible, used for loading content.


    /**
     * Initialize the PropertyDisplayerController. Used to create the objects necessary for displaying the map.
     * @param url Not used.
     * @param rb Not used.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        final  URL urlGoogleMaps = getClass().getResource("googleMaps.html"); // create a url for the html file
        webEngine = mapWebView.getEngine();
        mapWebView.setVisible(false);
        mapWebView.setContextMenuEnabled(false);
        webEngine.load(urlGoogleMaps.toExternalForm()); // load google maps file into the webengine (nothing shown yet)
        currentUser = null;
    }


    /**
     * Load the specific data of the property into the panel.
     * @param listing The property that should be displayed.
     */
    public void loadData(AirbnbListing listing, Account currentUser)
    {
        displayedListing = listing;
        this.currentUser = currentUser;
        if (currentUser.getSavedProperties().contains(displayedListing))
            setSaved(true);
        // Displaying basic information at top
        propertyImg.setImage(new Image(String.valueOf(listing.getPictureUrl())));
        propertyNameLbl.setText(listing.getName());
        propertyTypeLbl.setText(listing.getRoomType());
        bedsLbl.setText(listing.getBedrooms() + " Bedroom/s");
        bathroomsLbl.setText(listing.getBathroomsText());

        // Display the amenities
        String amenitiesString = "";
        for (String a : listing.getAmenities())
        {
            amenitiesString += "\r \n " + a;
        }
        amenitiesText.setText(amenitiesString);

        // Display information about the host
        hostLargeImg.setImage(new Image (String.valueOf(listing.getHostPicture())));
        hostNameLbl.setText(listing.getHostName());
        if (listing.isHostSuperhost()) {
            hostIsSuperHostLbl.setText("Is a Superhost");
        }

        //Set progress bars for reviews
        cleanlinessBar.setProgress(listing.getReviewScoresCleanliness() / 10.0);
        communicationBar.setProgress(listing.getReviewScoresCommunication() / 10.0);
        locationBar.setProgress(listing.getReviewScoresLocation() / 10.0);
        totalBar.setProgress(listing.getReviewScoresRating() / 100.0);

        // Display information about the neighbourhood. Map is initialized in initialize()
        boroughLbl.setText("Borough: " + listing.getNeighbourhood());
        neighbourHoodDescriptionLbl.setText(listing.getNeighbourhoodOverview());

        pricePerNight.setText("Price per minimum night: " + listing.getAveragePrice());

        maxNumberOfPeople.setText("Maximum number of guests: " + listing.getMaxGuests());
    }

    /**
     * Show the map and load the location of the current listing.
     * @param e
     */
    @FXML
    private void loadMap(ActionEvent e)
    {
        double latitude = displayedListing.getLatitude();
        double longitude = displayedListing.getLongitude();

        // Executes the function goToLocation in the HTML file. Moves the displayed map to the given location and puts a marker there.
        webEngine.executeScript("" +
                "window.lat = " + latitude + ";" +
                "window.lon = " + longitude + ";" +
                "document.goToLocation(window.lat, window.lon);"
        );
        mapWebView.setVisible(true);
    }

    /**
     * The user has clicked "book". Close this window and load the booking screen to show the selected listing.
     * Communication is done through the mainFrameController as a central connection point of other panels.
     */
    @FXML
    private void goToBookingScreen()
    {
        if (boroughPropertiesController != null) {
            currentUser.addFavouriteProperty(displayedListing); // Using a hashset, enforcing unique values.
            try {
                mainFrameController.loadBookingPanel(displayedListing); // Load the bookingPanel into the mainframe
                mainFrameController.getListings().changeSelectedBoroughs(new ArrayList<>()); // Reset the active filter
                // Pass on this listing to bookingPanel somehow

                Stage thisStage = (Stage) saveButton.getScene().getWindow(); // Get the stage of this window from a random control
                thisStage.close(); // Close the stage.

                Stage boroughsStage = (Stage) boroughPropertiesController.getWindow();
                boroughsStage.close();

                Stage mainWindowStage = (Stage) mainFrameController.getWindow().getScene().getWindow();
                mainWindowStage.show();

            } catch (IOException e) { e.printStackTrace(); }
        }
    }

    /**
     * Hide the buttons save and book. Used when the user enters the booking via account --> upcoming trips --> right click check booking --> check property.
     */
    public void hideNavigationButtons() {
        saveButton.setVisible(false);
        bookButton.setVisible(false);
    }

    /**
     * Set or remove a saved property. Saved properties are discarded at the end of each session.
     * The user can use it to select various properties that they can later choose in the booking panel to reserve.
     */
    @FXML
    public void saveProperty() {
        if(currentUser.removeFavouriteProperty(displayedListing)) {
           setSaved(false);
        } else {
            currentUser.addFavouriteProperty(displayedListing);
            setSaved(true);
        }
    }

    /**
     * Changes the style of the save button according to if the property has been saved or not.
     * @param saved True if the listing has been saved.
     */
    private void setSaved(boolean saved)
    {
        if (!saved) {
            saveButton.setStyle("-fx-background-color: grey");
            saveButton.setText("save");
        }
        else {
            saveButton.setStyle("-fx-background-color: #F75737");
            saveButton.setText("unsave");
        }
    }

    /**
     * Set the object of the MainWindowController. Used to communicate back in case the user presses "book".
     * @param mainFrameController The object of the mainFrameController this PropertyDisplayerController has been opened from.
     */
    public void setMainWindowController(MainFrameController mainFrameController)
    {
        this.mainFrameController = mainFrameController;
    }

    /**
     * Set the boroughPropertiesController. Used to close the window of the boroughPropertiesController when the user clicks "book"
     * @param controller The object of the BoroughPropertiesController.
     */
    public void setBoroughPropertiesController(BoroughPropertiesController controller)
    {
        this.boroughPropertiesController = controller;
    }


}
