package project_files;



import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class MainWindowController extends Application implements Initializable {

    private ArrayList<AirbnbListing> listings;

    private Account currentUser;
    private boolean accountOpen;

    // Stores names of all views that should be displayed in the main frame. Displayed in the order added.
    private static final String[] panelViews = new String[] {"welcomePanelView.fxml", "mapView.fxml", "statisticsView.fxml", "bookingView.fxml"};
    private int currentPage = 0;

    @FXML
    Button nextPaneBtn;
    @FXML
    BorderPane contentPane;
    @FXML
    BorderPane bottomPane;
    @FXML
    Button accountButton;


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("MainFrameView.fxml"));
        primaryStage.setTitle("EarthBnB");
        primaryStage.setScene(new Scene(root, 600, 500));
        primaryStage.setResizable(true);
        primaryStage.show();
        currentUser = null;
        accountOpen = false;
    }

    @Override
    public void initialize(URL url, ResourceBundle bundle)
    {
        try {
            load("listings.csv");
            contentPane.setCenter(FXMLLoader.load(getClass().getResource("welcomePanelView.fxml")));
        } catch (IOException e) {
            System.out.println("Error while starting program. Please restart.");
        }
    }

    public void load(String filename){
        AirbnbDataLoader loader = new AirbnbDataLoader();
        listings = new ArrayList<>();
        listings = loader.load(filename);
    }

    @FXML
    public void navigateToAccount(ActionEvent e) throws IOException {
        if(currentUser != null) {
            Parent nextPanel;
            if(accountOpen == false) {

                FXMLLoader accountLoader = new FXMLLoader(getClass().getResource("accountView.fxml"));
                nextPanel = accountLoader.load();
                AccountPanelController accountPanelController = accountLoader.getController();
                accountPanelController.initializeAccount(listings, currentUser);
                currentPage = 0;
                accountOpen = true;
                accountButton.setText("Exit");
                nextPaneBtn.setDisable(true);
            } else {
                nextPanel = FXMLLoader.load(getClass().getResource("welcomePanelView.fxml"));
                //updatePanel(3);
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
        controller.initializeList(listings);
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
                mapController.initializeList(listings);
                currentPage = 4;
                break;

            case 2: FXMLLoader statsLoader = new FXMLLoader(getClass().getResource("statisticsView.fxml"));
                nextPanel = statsLoader.load();
                StatisticsPanelController statisticsPanel = statsLoader.getController();
                AirbnbDataLoader loader = new AirbnbDataLoader();
                listings = loader.load("boroughListings.csv");
                statisticsPanel.initializeList(listings);
                currentPage++;
                break;

            case 3: FXMLLoader bookingLoader = new FXMLLoader(getClass().getResource("bookingView.fxml"));
                nextPanel = bookingLoader.load();
                BookingController bookingController = bookingLoader.getController();
                bookingController.initializeList(listings);
                currentPage = 0;
                break;

            default: nextPanel = FXMLLoader.load(getClass().getResource("welcomePanelView.fxml"));
                break;
        }
        contentPane.setCenter(nextPanel);
    }

    public void initializeListings(ArrayList<AirbnbListing> listings) {
        this.listings = listings;
    }

    public void loginNavigationClicked() throws IOException {
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("loginPanel.fxml"));
        Parent root = loginLoader.load();
        Stage newStage = new Stage();
        newStage.setScene(new Scene(root, 600, 500));
        newStage.setResizable(false);
        newStage.show();
        LoginPanelController loginPanelController = loginLoader.getController();
        loginPanelController.initializeListings(listings);
        contentPane.getScene().getWindow().hide();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
