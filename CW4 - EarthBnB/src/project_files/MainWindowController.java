package project_files;



import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;




public class MainWindowController extends Application {

    private ArrayList<AirbnbListing> listings = new ArrayList<>();

    Account currentUser;
    private boolean accountOpen;

    //The panels and their roots
    //private Panel statisticsPanel;
    //private Node statsPanelRoot;
    //private Panel welcomePanel;
    //private Node welcomePanelRoot;

    //private ArrayList<Node> panelRoots;

    @FXML
    Button nextPaneBtn;
    @FXML
    BorderPane contentPane;
    @FXML
    BorderPane bottomPane;
    @FXML
    Button accountButton;

    private int currentPage = 0;



    @Override
    public void start(Stage primaryStage) throws Exception {
        //load("listings.csv");
        Parent root = FXMLLoader.load(getClass().getResource("MainFrameView.fxml"));
        //contentPane.setCenter(FXMLLoader.load(getClass().getResource("welcomePanelView.fxml")));
        primaryStage.setTitle("EarthBnB");
        primaryStage.setScene(new Scene(root, 600, 500));
        primaryStage.setResizable(false);
        primaryStage.show();
        currentUser = null;
        accountOpen = false;
    }

    public void load(String filename){
        AirbnbDataLoader loader = new AirbnbDataLoader();
        listings = loader.load(filename);
    }

    public void navigateToAccount(ActionEvent e) throws IOException {
        if(currentUser != null) {
            Parent nextPanel;
            if(accountOpen == false) {

                FXMLLoader accountLoader = new FXMLLoader(getClass().getResource("accountView.fxml"));
                nextPanel = accountLoader.load();
                AccountPanelController accountPanelController = accountLoader.getController();
                accountPanelController.initializeAccount(listings, currentUser);
                //bookingController.initializeMap(listings);
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



    @FXML
    private void setNextPane(ActionEvent e) throws IOException
    {
        AirbnbDataLoader loader = new AirbnbDataLoader();
        Parent nextPanel;
        switch (currentPage)
        {
            case 0: nextPanel = FXMLLoader.load(getClass().getResource("welcomePanelView.fxml"));
                    currentPage++;
                    break;
            case 2: FXMLLoader statsLoader = new FXMLLoader(getClass().getResource("statisticsView.fxml"));
                    nextPanel = statsLoader.load();
                    StatisticsPanelController statisticsPanel = statsLoader.getController();
                    listings = loader.load("listings.csv");
                    statisticsPanel.initializeStats(listings);
                    currentPage++;
                    break;
            case 1: FXMLLoader mapLoader = new FXMLLoader(getClass().getResource("mapView.fxml"));
                    nextPanel = mapLoader.load();
                    MapController mapController = mapLoader.getController();
                    listings = loader.load("listings.csv");
                    mapController.initializeMap(listings);
                    currentPage++;
                    break;
            case 4: FXMLLoader bookingLoader = new FXMLLoader(getClass().getResource("bookingView.fxml"));
                    nextPanel = bookingLoader.load();
                    BookingController bookingController = bookingLoader.getController();
                    bookingController.initializeBooking(listings);
                    //bookingController.initializeMap(listings);
                    currentPage = 0;
                    break;
            default: nextPanel = FXMLLoader.load(getClass().getResource("welcomePanelView.fxml"));
                    break;
        }
        contentPane.setCenter(nextPanel);
    }

    @FXML
    private void setPrevPane(ActionEvent e)
    {
        //setPanel(panel);
    }

    public void setCurrentUser(Account user) {
        currentUser = user;
        accountButton.setText(user.getUsername());
    }

    /**
     * Set a panel to be shown
     * @param panel The panel
     */
    private void setPanel(Panel panel)
    {

    }

    public void updatePanel(Integer pageNumber) throws IOException {
        Parent nextPanel;
        currentPage = pageNumber;
        switch (currentPage)
        {
            case 0: nextPanel = FXMLLoader.load(getClass().getResource("welcomePanelView.fxml"));
                currentPage++;
                break;
            case 2: FXMLLoader statsLoader = new FXMLLoader(getClass().getResource("statisticsView.fxml"));
                nextPanel = statsLoader.load();
                StatisticsPanelController statisticsPanel = statsLoader.getController();
                AirbnbDataLoader loader = new AirbnbDataLoader();
                listings = loader.load("listings.csv");
                statisticsPanel.initializeStats(listings);
                currentPage++;
                break;
            case 1: FXMLLoader mapLoader = new FXMLLoader(getClass().getResource("mapView.fxml"));
                nextPanel = mapLoader.load();
                MapController mapController = mapLoader.getController();
                mapController.initializeMap(listings);
                currentPage = 4;
                break;
            case 4: FXMLLoader bookingLoader = new FXMLLoader(getClass().getResource("bookingView.fxml"));
                nextPanel = bookingLoader.load();
                BookingController bookingController = bookingLoader.getController();
                bookingController.initializeBooking(listings);
                //bookingController.initializeMap(listings);
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
