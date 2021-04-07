package project_files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.apache.commons.lang3.math.NumberUtils;
import org.controlsfx.control.CheckComboBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Class MainFrameController - This is the main frame in which the other content panels are being displayed.
 * It provides functionalities such as choosing a price range, logging in, selecting filters and opening an panel displaying information about the users account.
 * If different panels have to communicate with each other, this is done through this class because it holds references to the contentpanels and all other windows hold
 * a reference to this frame.
 *
 * @author  Valentin Magis, Rahin Ashraf, Vandad Vafai Tabrizi, Barnabas Szalai
 * @version 1.0
 * @since   2021-03-11
 */
public class MainFrameController implements Initializable {

    private Account currentUser; // null if not logged in.
    private boolean accountOpen; // If the account window has been opened

    // Booleans used to activate different buttons at the beginning of the user interaction.
    private boolean firstRequestSubmitted = false;
    private boolean initialPriceRangeSelected = false;

    private static boolean usingDatabase; // whether the user chose to run the program with our without the online database.

    private int currentPage = 0; // The current page being displayed.

    private MainframeContentPanel[] contentPanels; // An array holding all panels to be displayd inside the mainframe.

    private Listings listings;   // Filters the list according to the input the user gives.

    @FXML
    private Button nextPaneBtn, prevPaneBtn;
    @FXML
    private BorderPane contentPane;

    @FXML
    private Button accountButton;

    @FXML
    private ChoiceBox minPriceChoiceBox, maxPriceChoiceBox;

    @FXML
    private CheckComboBox filtersComboBox;

    @FXML
    private Label nameOfCurrent;


    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        currentUser = null; // set to null if the user is not logged in
        accountOpen = false;
        loadListings("airbnb-listings.csv");
        if (!usingDatabase)
            new OfflineData(listings); // Loads the offline bookingdata (is static)

        try {
            createPanels();
        } catch (IOException e) {
            e.printStackTrace();
        }

        contentPane.setCenter(contentPanels[0].getPanelRoot());
        setLoggedIn(false); //Disables the submit-button in the welcome panel.
        setFrameSwitchingButtonsActive(false); // Disables the buttons for switching frames (if a price range has already been selected)

        // Fill the dropdown with selectable price ranges
        initializePriceRangeDropDown();
        initializeFiltersComboBox();
    }


    private void initializeFiltersComboBox() {
        // Create a list of Strings of the names of the filters to be displayed to the user using the enums.
        ObservableList<FilterNames> filterNamesObservableList = FXCollections.observableArrayList(FilterNames.WIFI_FILTER, FilterNames.SUPER_FILTER, FilterNames.ROOM_FILTER, FilterNames.POOL_FILTER);
        filtersComboBox.getItems().addAll(filterNamesObservableList);
        filtersComboBox.addEventHandler(ComboBox.ON_HIDDEN, event -> activatedFilters(event)); // Initiate updating the filters when to box is closed
    }

    private void activatedFilters(Event event) {
        ArrayList<FilterNames> checkedList = new ArrayList<>();
        for (Object s : filtersComboBox.getCheckModel().getCheckedItems()) {
            if (s.getClass() == FilterNames.class) {
                FilterNames filter = (FilterNames) s;
                checkedList.add(filter);
            }
        }
        listings.setActiveFilters(checkedList);
        updateCurrentPanel();
    }

    /**
     * Set the selection of the filterComboBox.
     * Used to mirror the changes to filters in the boroughpropertiescontroller in the main frame.
     */
    public void setChoiceComboBoxFilters() {
        filtersComboBox.getCheckModel().clearChecks();
        for (FilterNames filter : listings.getActiveFilters()) {
            filtersComboBox.getCheckModel().check(filter);
        }
        updateCurrentPanel();
    }


    public void setUsingDatabase(boolean usingDatabase) {
        this.usingDatabase = usingDatabase;
    }

    private void createPanels() throws IOException {
        // Names of all views that should be displayed in the main frame. Displayed in the order added.
        String[] panelViewsStrings = new String[]{"welcomePanelView.fxml", "mapView.fxml", "Statistics/statisticsView.fxml", "bookingView.fxml"};
        contentPanels = new MainframeContentPanel[panelViewsStrings.length];
        for (int i = 0; i < panelViewsStrings.length; i++) {
            FXMLLoader panelLoader = new FXMLLoader(getClass().getResource(panelViewsStrings[i]));
            Parent loadedPanel = panelLoader.load();
            MainframeContentPanel controller = panelLoader.getController();
            controller.initialize(this, currentUser, loadedPanel, listings);
            contentPanels[i] = controller;
        }
    }

    /**
     * Initialize the drop downs with selectable prices.
     */
    private void initializePriceRangeDropDown() {
        minPriceChoiceBox.setItems(FXCollections.observableArrayList("0", "25", "50", "100", "200", "500"));
        maxPriceChoiceBox.setItems(FXCollections.observableArrayList("25", "50", "100", "200", "500", "3000"));
    }


    /**
     * Load the listings from the csv file.
     *
     * @param filename
     */
    public void loadListings(String filename) {
        AirbnbDataLoader loader = new AirbnbDataLoader();
        listings = new Listings(loader.load(filename));
    }

    @FXML
    public void navigateToAccount(ActionEvent e) throws IOException {
        if (currentUser != null) {
            Parent nextPanel;
            if (!accountOpen) {
                FXMLLoader accountLoader = new FXMLLoader(getClass().getResource("accountView.fxml"));
                nextPanel = accountLoader.load();
                AccountPanelController accountPanelController = accountLoader.getController();
                accountPanelController.initializeAccount(currentUser, this, listings);
                accountOpen = true;
                accountButton.setText("Exit");
                setFrameSwitchingButtonsActive(false);
                contentPane.setCenter(nextPanel);
            } else {
                contentPane.setCenter(contentPanels[currentPage].getPanelRoot());
                accountOpen = false;
                accountButton.setText(currentUser.getUsername());
                if (firstRequestSubmitted)
                    setFrameSwitchingButtonsActive(true);
            }

        } else {
            Alerts.warningAlert("Account not found!", "", "You have to log in before you can go to your dashboard!");
        }
    }

    /**
     * Switched the panel when next or previous have been clicked.
     *
     * @param e
     * @throws IOException When loading the new panel was unsuccessful.
     */
    @FXML
    private void switchPanel(ActionEvent e) {
        if (e.getSource().getClass() == Button.class) {
            Button btn = (Button) e.getSource();
            String direction = btn.getId();

            MainframeContentPanel controller = getNextView(direction);
            controller.initializeData(listings, currentUser);
            contentPane.setCenter(controller.getPanelRoot());
            nameOfCurrent.setText(controller.getName());
        }
    }

    /**
     * Get the next panel to be shown in the center of the mainframe.
     *
     * @param direction If the "next" or the "previous" button has been clicked.
     * @return Name of the next fxml file.
     */
    private MainframeContentPanel getNextView(String direction) {
        // Loop forwards
        if (direction.equalsIgnoreCase("nextPaneBtn")) {
            if (currentPage < contentPanels.length - 1)
                currentPage++;
            else
                currentPage = 0;
        }
        // Loop backwards
        else {
            if (currentPage > 0)
                currentPage--;
            else
                currentPage = contentPanels.length - 1;
        }
        return contentPanels[currentPage];
    }

    /**
     * Sets the current user in the mainframe and initializes to the currently displayed content panel.
     * Added to all other panels automatically when switching.
     *
     * @param user The user who has just logged in.
     */
    public void setCurrentUser(Account user) {
        currentUser = user;
        accountButton.setText(user.getUsername());
        contentPanels[currentPage].setCurrentUser(currentUser);
    }

    /**
     * Maybe redo.
     * Loads the booking panel and passes in a listing to be displayed in combination with the search the user applied.
     * Used for communication between the propertyDisplayer and the booking panel.
     *
     * @param listing
     * @throws IOException
     */
    public void loadBookingPanel(AirbnbListing listing) throws IOException {
        MainframeContentPanel controller = contentPanels[3];
        if (controller.getClass() == BookingController.class) {
            controller.initializeData(listings, currentUser);
            ((BookingController) controller).initializeWithProperty(listing);
            ((BookingController) controller).setUsingDatabase(usingDatabase);
            contentPane.setCenter(controller.getPanelRoot());
            currentPage = 3;
        }
    }

    public void loginNavigationClicked() throws IOException {
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("loginPanelView.fxml"));
        Parent root = loginLoader.load();
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root, 600, 500));
        newStage.setResizable(false);
        newStage.show();
        LoginPanelController loginPanelController = loginLoader.getController();
        loginPanelController.createUser(currentUser);
        loginPanelController.setMainWindowController(this);
    }

    public static boolean isUsingDatabase() {
        return usingDatabase;
    }

    /**
     * Activates the buttons for switching the frame to be active.
     *
     * @param areEnabled
     */
    public void setFrameSwitchingButtonsActive(boolean areEnabled) {
        if (areEnabled && initialPriceRangeSelected && firstRequestSubmitted) {
            prevPaneBtn.setDisable(false);
            nextPaneBtn.setDisable(false);
        } else {
            prevPaneBtn.setDisable(true);
            nextPaneBtn.setDisable(true);
        }
    }

    /**
     * Get a random node. Can be used to get the
     *
     * @return
     */
    public Window getWindow() {
        return contentPane.getScene().getWindow();
    }


    // Code for setting the price range.

    /**
     * Set a price range. Checks for validity.
     *
     * @param e
     */
    @FXML
    public void applyPriceRange(ActionEvent e) {
        Integer minPrice = convertChoiceBoxToInteger(minPriceChoiceBox);
        Integer maxPrice = convertChoiceBoxToInteger(maxPriceChoiceBox);

        if (e.getSource().getClass() == ChoiceBox.class) {
            if (((ChoiceBox) e.getSource()).getId().equals("minPriceChoiceBox")) {
                if (maxPrice != null) {
                    if (minPrice >= maxPrice) {
                        minPriceChoiceBox.getSelectionModel().clearSelection();
                        minPrice = null;
                        Alerts.warningAlert("Warning", "Error while choosing price range.", "Maximum price range must be more than minimum");
                    }
                }
            } else if (((ChoiceBox) e.getSource()).getId().equals("maxPriceChoiceBox")) {
                if (minPrice != null) {
                    if (maxPrice <= minPrice) {
                        maxPriceChoiceBox.getSelectionModel().clearSelection();
                        maxPrice = null;
                        Alerts.warningAlert("Warning", "Error while choosing price range.", "Maximum price range must be more than minimum");
                    }
                }
            }
        }
        if (contentPanels[0].getClass() == WelcomePanel.class)
            ((WelcomePanel) contentPanels[0]).setPriceRangeLabels(minPrice, maxPrice);
        applyPriceRange(minPrice, maxPrice);
    }

    /**
     * Converts the selected item of a choicebox into an integer if possible.
     *
     * @param box Contents of this box will be converted.
     * @return The integer. If the conversion was not possible, return null.
     */
    private Integer convertChoiceBoxToInteger(ChoiceBox box) {
        Object selection = box.getSelectionModel().getSelectedItem();
        String selectionString = null;
        if (selection != null)
            selectionString = selection.toString();

        Integer selectionInteger = null;

        if (NumberUtils.isParsable(selectionString))
            selectionInteger = Integer.parseInt(selectionString);

        return selectionInteger;
    }

    /**
     * Set the current chosen price range of objects to be shown.
     *
     * @param minPrice The minimum price range to be filtered for.
     * @param maxPrice The maximum price range to be filtered for.
     */
    private void applyPriceRange(Integer minPrice, Integer maxPrice) {
        if (minPrice != null && maxPrice != null && currentUser != null) {
            listings.changePriceRange(minPrice, maxPrice);
            updateCurrentPanel();
            if (!initialPriceRangeSelected) {
                initialPriceRangeSelected = true;
                setFrameSwitchingButtonsActive(true);
            }
        }
    }

    /**
     * Update the contents of the panel currently being displayed.
     */
    public void updateCurrentPanel() {
        contentPanels[currentPage].updatePanel();
    }

    /**
     * Disable the selection of the price range and the submitButton in the welcomePanel if the user is not logged in.
     * The user can only use the program if logged in.
     *
     * @param isLoggedIn Whether the user is logged in.
     */
    public void setLoggedIn(boolean isLoggedIn) {
        minPriceChoiceBox.setDisable(!isLoggedIn);
        maxPriceChoiceBox.setDisable(!isLoggedIn);
        if (contentPanels[0].getClass() == WelcomePanel.class) {
            WelcomePanel welcomePanel = (WelcomePanel) contentPanels[0];
            welcomePanel.submitButton.setDisable(!isLoggedIn);
        }
    }

    /**
     * Get the listings.
     *
     * @return The listings.
     */
    public Listings getListings() {
        return listings;
    }

    /**
     * If a request for checkin, checkout and number of guests has been performed already.
     * Has an effect on how buttons for switching panels behave.
     *
     * @param submitted Whether the first request has been submitted.
     */
    public void setFirstRequestSubmitted(boolean submitted) {
        firstRequestSubmitted = submitted;
        setFrameSwitchingButtonsActive(firstRequestSubmitted);
    }
}