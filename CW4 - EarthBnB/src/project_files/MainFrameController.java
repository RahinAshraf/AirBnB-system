package project_files;

import javafx.application.Application;
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
import org.apache.commons.lang3.math.NumberUtils;
import org.controlsfx.control.CheckComboBox;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class MainFrameController extends Application implements Initializable {

    private Account currentUser; // null if not logged in.
    private boolean accountOpen; // If the account window has been opened
    private boolean firstRequestSubmitted;

    private ArrayList<Account> offlineAccounts;
    private ArrayList<Reservation> offlineReservations;

    private boolean usingDatabase;

    private int currentPage = 0;

    private MainframeContentPanel[] contentPanels;

    // Filters the list according to the search and price range the user has entered (maybe also the checkboxes?)
    private Listings listings;

    @FXML
    Button nextPaneBtn, prevPaneBtn;
    @FXML
    BorderPane contentPane;

    @FXML
    Button accountButton;

    @FXML
    ChoiceBox minPriceChoiceBox, maxPriceChoiceBox;

    @FXML
    CheckComboBox filtersComboBox;

    @FXML
    Label nameOfCurrent;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("connectionSelectorView.fxml"));
        primaryStage.setTitle("EarthBnB");
        primaryStage.setScene(new Scene(root, 600, 300));
        primaryStage.setResizable(true);
        primaryStage.show();

    }


    @Override
    public void initialize(URL url, ResourceBundle bundle)
    {
        firstRequestSubmitted = false;
        currentUser = null; // set to null if the user is not logged in
        accountOpen = false;
        loadListings("airbnb-listings.csv");
        //teagenerateBookings();
        //generateUsers();
        try {
            createPanels();
        } catch (IOException e) {
            e.printStackTrace();
        }
        contentPane.setCenter(contentPanels[0].getPanelRoot());
        setLoggedIn(false); //Disables the submit-button in the welcome panel.
        setFrameSwitchingButtonsActive(false); // Disables the buttons for switching frames. They stay

        // Fill the dropdown with selectable price ranges
        initializePriceRangeDropDown();
        initializeFiltersComboBox();
        usingDatabase = false;
        offlineAccounts = new ArrayList<>();
        offlineReservations = new ArrayList<>();
    }




    private void initializeFiltersComboBox()
    {
        ObservableList<FilterNames> filterNamesObservableList = FXCollections.observableArrayList(FilterNames.WIFI_FILTER, FilterNames.SUPER_FILTER, FilterNames.ROOM_FILTER, FilterNames.POOL_FILTER);
        filtersComboBox.getItems().addAll(filterNamesObservableList);
        filtersComboBox.addEventHandler(ComboBox.ON_HIDDEN, event -> activatedFilters(event));
    }

    private void activatedFilters(Event event)
    {
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

    public void setChoiceComboBoxFilters()
    {
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
       String[] panelViewsStrings = new String[] {"welcomePanelView.fxml", "mapView.fxml", "Statistics/statisticsView.fxml", "bookingView.fxml"};
       contentPanels = new MainframeContentPanel[panelViewsStrings.length];
       for (int i = 0; i < panelViewsStrings.length; i++)
       {
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


    public void loadListings(String filename){
        AirbnbDataLoader loader = new AirbnbDataLoader();
        listings = new Listings(loader.load(filename));
    }

    public void addOfflineAccount(Account account) {
        offlineAccounts.add(account);
    }
    public void addOfflineReservation(Reservation reservation) {
        offlineReservations.add(reservation);
    }
    public ArrayList<Account> getOfflineAccounts() {
        return offlineAccounts;
    }
    public ArrayList<Reservation> getOfflineReservations() {
        return offlineReservations;
    }


    @FXML
    public void navigateToAccount(ActionEvent e) throws IOException {
        if(currentUser != null) {
            Parent nextPanel;
            if(!accountOpen) {
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
            accountAlert();
        }
    }

    /**
     * An alert which tells you to create/login into an account
     */
    private void accountAlert(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Account not found!");
        alert.setHeaderText("");
        alert.setContentText("You have to log in before you can go to your dashboard!");

        alert.showAndWait();
    }

    /**
     * Switched the panel when next or previous have been clicked.
     * @param e
     * @throws IOException When loading the new panel was unsuccessful.
     */
    @FXML
    private void switchPanel(ActionEvent e) {
        if (e.getSource().getClass() == Button.class)
        {
            Button btn = (Button) e.getSource();
            String direction = btn.getId();

            MainframeContentPanel controller = getNextView(direction);
            controller.initializeList(listings, currentUser);
            contentPane.setCenter(controller.getPanelRoot());
            nameOfCurrent.setText(controller.getName());
        }
    }

    /**
     * Get the next panel to be shown in the center of the mainframe.
     * @param direction If the "next" or the "previous" button has been clicked.
     * @return Name of the next fxml file.
     */
    private MainframeContentPanel getNextView(String direction)
    {
        // Loop forwards
        if (direction.equalsIgnoreCase("nextPaneBtn")) {
            System.out.println("clicked next");
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
     * @param listing
     * @throws IOException
     */
    public void loadBookingPanel(AirbnbListing listing) throws IOException {
        MainframeContentPanel controller = contentPanels[3];
        if (controller.getClass() == BookingController.class) {
            controller.initializeList(listings, currentUser);
            ((BookingController) controller).initializeWithProperty(listing);
            ((BookingController) controller).setUsingDatabase(usingDatabase);
            contentPane.setCenter(controller.getPanelRoot());
            currentPage = 3;
        }
    }

    public void loginNavigationClicked() throws IOException {
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("loginPanel.fxml"));
        Parent root = loginLoader.load();
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root, 600, 500));
        newStage.setResizable(false);
        newStage.show();
        LoginPanelController loginPanelController = loginLoader.getController();
        loginPanelController.createUser(currentUser);
        loginPanelController.setMainWindowController(this);
    }

    public boolean isUsingDatabase() {
        return usingDatabase;
    }

    /**
     * Activates the buttons for switching the frame to be active.
     * @param areEnabled
     */
    public void setFrameSwitchingButtonsActive(boolean areEnabled) {
        prevPaneBtn.setDisable(!areEnabled);
        nextPaneBtn.setDisable(!areEnabled);
    }


    // Code for setting the price range.

    /**
     * Set a price range. Checks for validity.
     * @param e
     */
    @FXML
    public void applyPriceRange(ActionEvent e)
    {
        Integer minPrice = convertChoiceBoxToInteger(minPriceChoiceBox);
        Integer maxPrice = convertChoiceBoxToInteger(maxPriceChoiceBox);

        // DO SAFETY CHECK BEFORE CAST

        // Checking validity
        if (((ChoiceBox) e.getSource()).getId().equals("minPriceChoiceBox")) {
            if (maxPrice != null) {
                if (minPrice >= maxPrice) {
                    minPriceChoiceBox.getSelectionModel().clearSelection();
                    minPrice = null;
                    priceRangeAlert();
                }
            }
        }
        else if (((ChoiceBox) e.getSource()).getId().equals("maxPriceChoiceBox")){
            if (minPrice != null) {
                if (maxPrice <= minPrice) {
                    maxPriceChoiceBox.getSelectionModel().clearSelection();
                    maxPrice = null;
                    priceRangeAlert();
                }
            }
        }
        applyPriceRange(minPrice, maxPrice);
    }

    /**
     * Converts the selected item of a choicebox into an integer if possible.
     * @param box Contents of this box will be converted.
     * @return The integer. If the conversion was not possible, return null.
     */
    private Integer convertChoiceBoxToInteger(ChoiceBox box)
    {
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
     * @param minPrice
     * @param maxPrice
     */
    private void applyPriceRange(Integer minPrice, Integer maxPrice) {
        if (minPrice != null && maxPrice != null && currentUser != null)
        {
            listings.changePriceRange(minPrice, maxPrice);
            updateCurrentPanel();
        }
    }

    public void updateCurrentPanel()
    {
        contentPanels[currentPage].updatePanel();
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

    /**
     * Disable the selection of the price range and the submitButton in the welcomePanel if the user is not logged in.
     * The user can only use the program if logged in.
     * @param isLoggedIn
     */
    public void setLoggedIn(boolean isLoggedIn)
    {
        //minPriceChoiceBox.setDisable(!isLoggedIn);
        //maxPriceChoiceBox.setDisable(!isLoggedIn);

        if (contentPanels[0].getClass() == WelcomePanel.class) {
            WelcomePanel welcomePanel = (WelcomePanel) contentPanels[0];
            welcomePanel.submitButton.setDisable(!isLoggedIn);
        }
    }

    public Listings getListings()
    {
        return listings;
    }

    /**
     * If a request for checkin, checkout and number of guests has been performed already.
     * Has an effect on how buttons for switching panels behave.
     * @param submitted
     */
    public void setFirstRequestSubmitted(boolean submitted)
    {
        setFrameSwitchingButtonsActive(true);
        firstRequestSubmitted = submitted;
    }

    /*

    // Code for populating database
    private void generateUsers() {
        ArrayList<String> names = loadNames();
        try {
            DatabaseConnection connection = new DatabaseConnection();
            Connection connectDB = connection.getConnection();
            Statement statement = connectDB.createStatement();

            for (int i=1; i < 300; i++)
            {
                String insert1 = "INSERT INTO account VALUES (NULL," + "'" + names.get(i) + "' , '" + givenUsingJava8_whenGeneratingRandomAlphanumericString_thenCorrect() + "', '" +
                        names.get(i).replaceAll("\\s", "" + "") + "@gmail.com', '" + generateRandomDate() + "')" + ";";
                statement.executeUpdate(insert1);
            }
        } catch (Exception e) {
            System.out.println("failed");
            e.printStackTrace();
        }
    }
    //https://www.baeldung.com/java-random-string
    private String givenUsingJava8_whenGeneratingRandomAlphanumericString_thenCorrect() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 6;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private ArrayList<String> loadNames() {
        ArrayList<String> names = new ArrayList<>();
        try{
            URL url = getClass().getResource("names.csv");
            CSVReader reader = new CSVReader(new FileReader(new File(url.toURI()).getAbsolutePath()));
            String [] line;
            reader.readNext();
            while ((line = reader.readNext()) != null) {
                String name = line[0];
                names.add(name);
            }
        } catch(IOException | URISyntaxException e){
            System.out.println("Failure! Something went wrong");
            e.printStackTrace();
        }
        return names;
    }

    // https://stackoverflow.com/questions/34051291/generate-a-random-localdate-with-java-time
    public Date generateRandomDate() {
        long minDay = LocalDate.of(2018, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(2021, 4, 30).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
        return java.sql.Date.valueOf(randomDate);
    }





    private void generateBookings() {
        ArrayList<AirbnbListing> listing = listings.getFilteredListings();

        ArrayList<String> users = getUsers();

        try {
            DatabaseConnection connection = new DatabaseConnection();
            Connection connectDB = connection.getConnection();
            Statement statement = connectDB.createStatement();

            Random rand = new Random();
            for (int i=0; i < listing.size(); i+=70)
            {
                AirbnbListing l = listing.get(i);

                createRandomBooking(l, users.get(rand.nextInt(users.size()-1)));

                String insert = "INSERT INTO booking VALUES (NULL, '" + usersData.getCheckIn() + "', '" + usersData.getCheckOut() + "', '" + users.get(rand.nextInt(users.size()-1)) + "', '" +
                        l.getMaxGuests() + "', '" + l.getPrice() *  + "', '" + l.getId() + "')" + ";";
                statement.executeUpdate(insert);
            }
        } catch (Exception e) {
        }
    }

    private void createRandomBooking(AirbnbListing l, String s) {

    }

    private ArrayList<String> getUsers() {
        ArrayList<String> userIds = new ArrayList<>();
        try {
            DatabaseConnection connection = new DatabaseConnection();
            Connection connectDB = connection.getConnection();
            Statement statement = connectDB.createStatement();

            String checkSignup = "SELECT accountID FROM account";

            ResultSet queryResult = statement.executeQuery(checkSignup);
            while (queryResult.next()) {
                userIds.add(queryResult.getString(1)); // Unsafe operation?
            }
        } catch (Exception e) {
        }
        return userIds;
    }

     */


}


