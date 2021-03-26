package project_files;

// Disable search before login
// Disable switching before search
// FIlter for properties in map controller

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.commons.lang3.math.NumberUtils;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ResourceBundle;


public class MainWindowController extends Application implements Initializable {

    private ArrayList<AirbnbListing> filteredListings;
    private ArrayList<AirbnbListing> originalListings;

    private MapController mapController = new MapController();
    private WelcomePanel welcomePanel = new WelcomePanel();

    private LinkedList<String> names = new LinkedList<>();

    private Account currentUser; // null if not logged in.
    private boolean accountOpen; // If the account window has been opened
    //private boolean buttonsActive = false; // Buttons to switch panels are disabled by default


    // Stores names of all views that should be displayed in the main frame. Displayed in the order added.
    private static final String[] panelViews = new String[] {"welcomePanelView.fxml", "mapView.fxml", "statisticsView.fxml", "bookingView.fxml"};
    private int currentPage = 0;

    @FXML
    Button nextPaneBtn, prevPaneBtn;
    @FXML
    BorderPane contentPane;
    @FXML
    BorderPane bottomPane;
    @FXML
    Button accountButton;

    @FXML
    ChoiceBox minPriceChoiceBox, maxPriceChoiceBox;

    @FXML
    Label nameOfCurrent;

    public void setName(String name){
        nameOfCurrent.setText(name);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("MainFrameView.fxml"));
        primaryStage.setTitle("EarthBnB");
        primaryStage.setScene(new Scene(root, 600, 500));
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle bundle)
    {
        currentUser = null;
        accountOpen = false;
        try {
            load("listings.csv");
            //contentPane.setCenter(FXMLLoader.load(getClass().getResource("welcomePanelView.fxml")));

            FXMLLoader welcomePanelLoader = new FXMLLoader(getClass().getResource("welcomePanelView.fxml"));
            contentPane.setCenter(welcomePanelLoader.load());
            WelcomePanel welcomePanel = welcomePanelLoader.getController();
            welcomePanel.setMainWindowController(this);
            setButtonsActive(false);

        } catch (IOException e) {
            System.out.println("Error while starting program. Please restart.");
        }

        // Fill the dropdown with selectable price ranges
        minPriceChoiceBox.setItems(FXCollections.observableArrayList("0", "25", "50", "100", "200", "500"));
        maxPriceChoiceBox.setItems(FXCollections.observableArrayList("25", "50", "100", "200", "500", "3000"));
    }

    public void load(String filename){
        AirbnbDataLoader loader = new AirbnbDataLoader();
        originalListings = new ArrayList<>();
        originalListings = loader.load(filename);

        filteredListings = new ArrayList<>();
        filteredListings.addAll(originalListings);
    }

    @FXML
    public void navigateToAccount(ActionEvent e) throws IOException {
        if(currentUser != null) {
            Parent nextPanel;
            if(accountOpen == false) {

                FXMLLoader accountLoader = new FXMLLoader(getClass().getResource("accountView.fxml"));
                nextPanel = accountLoader.load();
                AccountPanelController accountPanelController = accountLoader.getController();
                accountPanelController.initializeAccount(filteredListings, currentUser);
                accountOpen = true;
                accountButton.setText("Exit");
                nextPaneBtn.setDisable(true);
            } else {
                nextPanel = FXMLLoader.load(getClass().getResource("welcomePanelView.fxml"));
                accountOpen = false;
                accountButton.setText(currentUser.getUsername());
                nextPaneBtn.setDisable(false);
            }
            contentPane.setCenter(nextPanel);
        } else {
            System.out.println("You have to log in before you can go to your dashboard!");
        }
    }



    /**
     * Switched the panel when next or previous have been clicked.
     * @param e
     * @throws IOException When loading the new panel was unsuccessful.
     */
    @FXML
    private void switchPanel(ActionEvent e) throws IOException {
        String direction;
        if (e.getSource().getClass() == Button.class)
        {
            Button btn = (Button) e.getSource();
            direction = btn.getId();

            contentPane.setCenter(getNewPanel(getNextViewName(direction))); //Get new panel by getting name of panel to be loaded.

            if(contentPane.getCenter().toString().equals("AnchorPane[id=mapView]")){
                setName("Map");
            }
            else if(contentPane.getCenter().toString().equals("VBox[id=statBox]")){
                setName("Statistics");
            }
            else if(contentPane.getCenter().toString().equals("AnchorPane[id=bookingView]")){ //booking

                setName("Bookings");
            }
            else{
                setName("Welcome");
            }
        }
    }

    /**
     * Returns an initialized Panel ready to be displayed.
     * @param viewName The name of the panel to be displayed.
     * @return The root of the panel to be displayed.
     * @throws IOException When loading the fxml was unsuccessful
     */
    private Parent getNewPanel(String viewName) throws IOException {
        FXMLLoader panelLoader = new FXMLLoader(getClass().getResource(viewName));
        Parent nextPanel = panelLoader.load();
        MainframeContentPanel controller = panelLoader.getController();
        controller.initializeList(filteredListings, currentUser);

        if (controller.getClass() == WelcomePanel.class) {
            WelcomePanel welcomePanel = (WelcomePanel) controller;
            welcomePanel.setMainWindowController(this);
        }

        return nextPanel;
    }

    /**
     * Get the name of the next fxml file to be loaded into the center of the mainframe.
     * @param direction If the "next" or the "previous" button has been clicked.
     * @return Name of the next fxml file.
     */
    private String getNextViewName(String direction)
    {
        // Loop forwards
        if (direction.equalsIgnoreCase("nextPaneBtn")) {
            if (currentPage < panelViews.length - 1)
                currentPage++;
            else
                currentPage = 0;
        }
        // Loop backwards
        else {
            if (currentPage > 0)
                currentPage--;
            else
                currentPage = panelViews.length - 1;
        }
        return panelViews[currentPage];
    }


    public void setCurrentUser(Account user) {
        currentUser = user;
        accountButton.setText(user.getUsername());
    }


    // For the "back button" it would be better to just load the last currentPage
    public void updatePanel(Integer pageNumber) throws IOException {
        Parent nextPanel;
        currentPage = pageNumber;
        switch (currentPage)

        {
            case 0: nextPanel = FXMLLoader.load(getClass().getResource("welcomePanelView.fxml"));
                currentPage++;
                break;

            case 1: FXMLLoader mapLoader = new FXMLLoader(getClass().getResource("mapView.fxml"));
                nextPanel = mapLoader.load();
                MapController mapController = mapLoader.getController();
                mapController.initializeList(filteredListings, currentUser);
                currentPage = 4;
                break;

            case 2: FXMLLoader statsLoader = new FXMLLoader(getClass().getResource("statisticsView.fxml"));
                nextPanel = statsLoader.load();
                StatisticsPanelController statisticsPanel = statsLoader.getController();
                AirbnbDataLoader loader = new AirbnbDataLoader();
                filteredListings = loader.load("boroughListings.csv");
                statisticsPanel.initializeList(filteredListings, currentUser);
                currentPage++;
                break;

            case 3: FXMLLoader bookingLoader = new FXMLLoader(getClass().getResource("bookingView.fxml"));
                nextPanel = bookingLoader.load();
                BookingController bookingController = bookingLoader.getController();
                bookingController.initializeList(filteredListings, currentUser);
                currentPage = 0;
                break;

            default: nextPanel = FXMLLoader.load(getClass().getResource("welcomePanelView.fxml"));
                break;
        }
        contentPane.setCenter(nextPanel);
    }

    public void initializeListings(ArrayList<AirbnbListing> listings, Account currentUser) {

        this.filteredListings = listings;
        this.currentUser = currentUser;
    }

    public void loginNavigationClicked() throws IOException {
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("loginPanel.fxml"));
        Parent root = loginLoader.load();
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root, 600, 500));
        newStage.setResizable(false);
        newStage.show();
        LoginPanelController loginPanelController = loginLoader.getController();
        loginPanelController.initializeListings(filteredListings);
        loginPanelController.createUser(currentUser);
        contentPane.getScene().getWindow().hide();
    }

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Switch if the buttons to change the panel are active.
     */
    /*
    public void switchButtonsActive() {
        buttonsActive = !buttonsActive;
        prevPaneBtn.setDisable(buttonsActive);
        nextPaneBtn.setDisable(buttonsActive);
    }

     */

    public void setButtonsActive(boolean areEnabled) {
        prevPaneBtn.setDisable(!areEnabled);
        nextPaneBtn.setDisable(!areEnabled);
    }


    // Code for setting the price range.

    /**
     * Set a price range. Checks for validity.
     * @param e
     */
    @FXML
    public void setPriceRange(ActionEvent e)
    {
        Integer minPrice = convertChoiceBoxToInteger(minPriceChoiceBox);
        Integer maxPrice = convertChoiceBoxToInteger(maxPriceChoiceBox);

        // Checking validity
        if (((ChoiceBox) e.getSource()).getId().equals("minPriceChoiceBox")) {
            if (maxPrice != null) {
                if (minPrice >= maxPrice) {
                    minPriceChoiceBox.getSelectionModel().clearSelection();
                    priceRangeAlert();
                }
            }
        }
        else if (((ChoiceBox) e.getSource()).getId().equals("maxPriceChoiceBox")){
            if (minPrice != null) {
                if (maxPrice <= minPrice) {
                    maxPriceChoiceBox.getSelectionModel().clearSelection();
                    priceRangeAlert();
                }
            }
        }

        setPriceRange(minPrice, maxPrice);
    }

    /**
     * Converts the selected item of a choicebox into an integer if possible.
     * @param box Contents of this box will be converted.
     * @return The integer. If the conversion was not possible, return null.
     */
    private Integer convertChoiceBoxToInteger(ChoiceBox box)
    {
        Object selection = box.getSelectionModel().getSelectedItem();
        String selectionString = "";
        if (selection != null)
            selectionString = selection.toString();

        Integer selectionInteger = null;

        if (NumberUtils.isParsable(selectionString))
            selectionInteger = Integer.parseInt(selectionString);

        return selectionInteger;
    }

    /**
     * Set the current chosen price range of objects to be shown.
     * @param minPrice
     * @param maxPrice
     */
    private void setPriceRange(Integer minPrice, Integer maxPrice) {
        if (minPrice != null && maxPrice != null)
        {
            //if (currentUser != null)
            currentUser.setPriceRange(minPrice, maxPrice);
        }
    }


    /**
     * An alert which occurs when the check-in date is not valid.
     */
    private void priceRangeAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Error while choosing price range.");
        alert.setContentText("Maximum price range must be more than minimum");
        alert.showAndWait();
    }

}


