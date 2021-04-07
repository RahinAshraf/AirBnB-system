package project_files;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;



/**
 * The MapController class displays all of the boroughs in London. Each of the boroughs can be selected by the user.
 * Multiple boroughs can also be selected, and boroughs can be deselected.
 *
 * @author  Valentin Magis, Rahin Ashraf, Vandad Vafai Tabrizi, Barnabas Szalai
 * @version 1.0
 * @since   2021-03-11
 */
public class MapController extends MainframeContentPanel implements Initializable {

    private Double filterValue;
    private Map<String, Long> propertyCount = new HashMap<String, Long>();
    private String boroughs[] = new String[32];


    private ArrayList<Button> selectedBoroughs;

    @FXML
    Slider filterSlider;

    @FXML
    AnchorPane mapView;

    public MapController()
    {
        name = "Map";
    }


    /**
     * This method initializes some of the controller's method's fields.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedBoroughs = new ArrayList<>();
        currentUser = null;
    }

    /**
     * This method is called when the user clicks on a borough. It converts the clicked item's source to a button,
     * and uses it as parameter for the selectNewBorough method call.
     */
    @FXML
    public void selectBorough(javafx.event.ActionEvent actionEvent) {
        selectNewBorough((Button)actionEvent.getSource());
    }

    /**
     * Instantiates a new boroughPropertiesView and sends passes a list of borough IDs that are selected by the user.
     * It is called when the user clicks on the search properties button.
     */
    @FXML
    public void searchProperties(javafx.event.ActionEvent actionEvent) {
        try {
            FXMLLoader boroughLoader = new FXMLLoader(getClass().getResource("boroughPropertiesView.fxml"));
            Parent root = boroughLoader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root, 800, 500));
            newStage.setResizable(true);
            newStage.show();
            String titleString = "";
            for(int i = 0; i<selectedBoroughs.size(); i++) {
                if(i == 0) {
                    titleString = titleString + "" + selectedBoroughs.get(i).getId();
                } else {
                    titleString = titleString + ", " + selectedBoroughs.get(i).getId();
                }
            }
            newStage.setTitle(titleString);
            BoroughPropertiesController boroughController = boroughLoader.getController();
            ArrayList<String> selectedBoroughsStrings = new ArrayList<>();
            for(int i = 0; i<selectedBoroughs.size(); i++) {
                selectedBoroughsStrings.add(selectedBoroughs.get(i).getId());
            }
            boroughController.initializeListing(listings, selectedBoroughsStrings, currentUser);
            boroughController.setMainWindowController(mainFrameController); // Passing on the mainWindowController Object
            mapView.getScene().getWindow().hide();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Initializes some of the fields of the controller. Calls the updatePanel method.
     * @param listings  the filtered listings that is used to set the colors of the boroughs
     * @param currentUser   the current logged in user
     */
    @Override
    public void initializeData(Listings listings, Account currentUser)
    {
        this.listings = listings;
        this.currentUser = currentUser;
        updatePanel();
    }

    /**
     * Instantiates a new boroughPropertiesView and sends passes a list of borough IDs that are selected by the user.
     * It is called when the user clicks on the search properties button.
     */
    @Override
    public void updatePanel() {
        updateBoroughs();
        for(int i=0; i<33; i++) {
            Button button = (Button) mapView.getChildren().get(i);
            if(selectedBoroughs.contains(button)) {
                button.setStyle("-fx-background-color: #50B4D4");
            } else {
                setBoroughDefaultColor(button);
            }
        }
    }

    /**
     * Sets the colors of the boroughs based on the filtered listings that is passed to the object.
     * @param button    the button whose colors is to be set.
     */
    public void setBoroughDefaultColor(Button button) {
        int boroughPropertyCount;
        try {
            boroughPropertyCount = propertyCount.get(button.getId()).intValue();
        } catch(Exception e) {
            boroughPropertyCount = 0;
        }
        try {
            if (boroughPropertyCount >= 5000) {
             button.setStyle("-fx-background-color: #FF1515");
            } else if (boroughPropertyCount >= 300 && boroughPropertyCount < 5000) {
                button.setStyle("-fx-background-color: #FFB06F");
            } else if (boroughPropertyCount > 0 && boroughPropertyCount < 300) {
                button.setStyle("-fx-background-color: #F2E02C");
            } else {
                button.setStyle("-fx-background-color: #CCCCCC");
            }
        } catch (Exception e) {
            System.out.println("error");
        }
    }

    /**
     * This method is called when the user clicks on a borough. It changes the color of the borough button to blue
     * if it is selected. If it is deselected, the color is changed back to the original.
     * @param button    the button that is clicked
     */
    public void selectNewBorough(Button button) {

        if (selectedBoroughs.contains(button)) {
            selectedBoroughs.remove(button);
            setBoroughDefaultColor(button);
        } else {
            selectedBoroughs.add(button);
            button.setStyle("-fx-background-color: #50B4D4");
        }
    }

    /**
     * Stores the number of properties for each of the boroughs. Also calls a method to shorten the borough names.
     */
    private void updateBoroughs()
    {
        resizePropertyNeighbourhood();
        propertyCount = listings.getFilteredListings().stream()
                .collect(Collectors.groupingBy(AirbnbListing::getNeighbourhood, Collectors.counting()));
    }


    /**
     * Goes through the filtered listings and only keeps the first word of the boroughs.
     * The boroughs are compared to the button IDs later; thus, they have to match.
     */
    public void resizePropertyNeighbourhood() {
        for(int i = 0; i<listings.getFilteredListings().size(); i++) {
           listings.getFilteredListings().get(i).chopNeighbourhoodName();
        }
    }

}
