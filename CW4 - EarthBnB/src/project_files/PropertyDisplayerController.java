package project_files;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Long.MAX_VALUE;


public class PropertyDisplayerController implements Initializable {

    private AirbnbListing displayedListing;

    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        final  URL urlGoogleMaps = getClass().getResource("googleMaps.html");
        webEngine = mapWebView.getEngine();
        mapWebView.setContextMenuEnabled(false);
        webEngine.load(urlGoogleMaps.toExternalForm());
    }

    public void loadData(AirbnbListing listing)
    {
        displayedListing = listing;
        propertyImg.setImage(new Image(String.valueOf(listing.getPictureUrl())));
        propertyNameLbl.setText(listing.getName());
        propertyTypeLbl.setText(listing.getRoomType());
        bedsLbl.setText(String.valueOf(listing.getBedrooms()));
        bathroomsLbl.setText(listing.getBathroomsText());
        hostThumbnailImg.setImage(new Image(String.valueOf(listing.getHostThumbnail())));

        hostLargeImg.setImage(new Image (String.valueOf(listing.getHostPicture())));

        String amenitiesString = "";
        for (String a : listing.getAmenities())
        {
            amenitiesString += "\r \n " + a;
        }
        amenitiesText.setText(amenitiesString);

        hostNameLbl.setText(listing.getHostName());
        if (listing.isHostSuperhost()) {
            hostIsSuperhostLbl.setText("Is a Superhost");
        }

        //Set progress bars for reviews
        cleanlinessBar.setProgress(listing.getReviewScoresCleanliness() / 10.0);
        communicationBar.setProgress(listing.getReviewScoresCommunication() / 10.0);
        locationBar.setProgress(listing.getReviewScoresLocation() / 10.0);
        totalBar.setProgress(listing.getReviewScoresRating() / 100.0);

        boroughLbl.setText("Borough: " + listing.getNeighbourhood());
        neighbourHoodDescriptionLbl.setText(listing.getNeighbourhoodOverview());
    }

    @FXML
    private void loadMap(ActionEvent e)
    {
        mapWebView.setVisible(true);
        mapWebView.setMaxHeight(MAX_VALUE);
        mapWebView.setMaxWidth(MAX_VALUE);
        double latitude = displayedListing.getLatitude();
        double longitude = displayedListing.getLongitude();

        webEngine.executeScript("" +
                "window.lat = " + latitude + ";" +
                "window.lon = " + longitude + ";" +
                "document.goToLocation(window.lat, window.lon);"
        );
    }


    @FXML
    ImageView propertyImg, hostThumbnailImg, hostLargeImg;

    @FXML
    Label propertyNameLbl, propertyTypeLbl, bedsLbl, bathroomsLbl, hostIsSuperhostLbl, hostNameLbl, boroughLbl, neighbourHoodDescriptionLbl;

    @FXML
    TextArea amenitiesText; // Expand thing, only show a couple

    @FXML
    ProgressBar cleanlinessBar, communicationBar, locationBar, totalBar;

    @FXML
    WebView mapWebView;
    private WebEngine webEngine;
}
