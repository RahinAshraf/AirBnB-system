package project_files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class BoroughPropertiesController implements Initializable {

    //
    TableColumn boroughHostCol;
    //
    TableColumn<AirbnbListing, Integer> boroughPriceCol;
    //
    TableColumn<AirbnbListing, Integer> reviewsCountCol;

    //
    private boolean isDropClicked;

    //
    @FXML
    Button backNavigation;
    //
    @FXML
    TableView propertiesTable;
    //
    @FXML
    Button sortReviews;
    //
    @FXML
    Button sortPrice;
    //
    @FXML
    Button sortHost;

    @FXML
    CheckBox wifiBox;
    @FXML
    CheckBox poolBox;
    @FXML
    CheckBox superBox;
    @FXML
    CheckBox roomBox;


    // The list of data which is shown.
    private final ObservableList<AirbnbListing> data = FXCollections.observableArrayList();
    // The new data which will be set if any of the filters are chosen.
    private ObservableList<AirbnbListing> displayData = FXCollections.observableArrayList();
    // The list of the properties.
    ArrayList<AirbnbListing> listings;

    // An Array List that stores check boxes that hold filters.
    private ArrayList<CheckBox> filters = new ArrayList<>();

    /**
     *
     */
    public void initializeListing(ArrayList<AirbnbListing> listings, ArrayList<String> selectedBoroughs)
    {
        this.listings = listings;
        // Loads the data.
        loadData(selectedBoroughs);
        // Adding all the check boxes.
        filters.add(wifiBox);
        filters.add(superBox);
        filters.add(roomBox);
        filters.add(poolBox);

        // Creates a table column which contains the hosts' names.
        boroughHostCol = new TableColumn("Host Name");
        boroughHostCol.setMinWidth(100);
        boroughHostCol.setMaxWidth(120);
        boroughHostCol.setCellFactory(TextFieldTableCell.forTableColumn());
        boroughHostCol.setCellValueFactory(new PropertyValueFactory<AirbnbListing, String>("hostName"));

        // Creates a table column which contains the borough's names.
        TableColumn boroughCol = new TableColumn("Borough");
        boroughCol.setMinWidth(100);
        boroughCol.setMaxWidth(120);
        boroughCol.setCellFactory(TextFieldTableCell.forTableColumn());
        boroughCol.setCellValueFactory(new PropertyValueFactory<AirbnbListing, String>("neighbourhood"));

        // Creates a table column which contains the price of each property.
        boroughPriceCol = new TableColumn<>("Price");
        boroughPriceCol.setMinWidth(100);
        boroughPriceCol.setMaxWidth(120);
        //boroughPriceCol.setCellFactory(TextFieldTableCell.forTableColumn());
        PropertyValueFactory temp = new PropertyValueFactory<AirbnbListing, Integer> (("price"));
        boroughPriceCol.setCellValueFactory(temp);

        // Creates a table column which contains the reviews of each property.
        reviewsCountCol = new TableColumn<>("# of Reviews");
        reviewsCountCol.setMinWidth(100);
        reviewsCountCol.setMaxWidth(120);
        //boroughPriceCol.setCellFactory(TextFieldTableCell.forTableColumn());
        PropertyValueFactory reviewsTemp = new PropertyValueFactory<AirbnbListing, Integer> (("numberOfReviews"));
        reviewsCountCol.setCellValueFactory(reviewsTemp);

        // Creates a table column which contains the minimum nights which you can stay
        // at a property.
        TableColumn<AirbnbListing, Integer> minimumNightsCol = new TableColumn<>("Minimum Nights");
        minimumNightsCol.setMinWidth(100);
        minimumNightsCol.setMaxWidth(120);
        //boroughPriceCol.setCellFactory(TextFieldTableCell.forTableColumn());
        PropertyValueFactory nightsTemp = new PropertyValueFactory<AirbnbListing, Integer> (("minimumNights"));
        minimumNightsCol.setCellValueFactory(nightsTemp);

        // Sets all of the data into the table.
        propertiesTable.getColumns().addAll(boroughHostCol, boroughPriceCol, boroughCol, reviewsCountCol, minimumNightsCol);
        propertiesTable.setItems(data);

    }

    /**
     * Loads the data from the csv file into the table.
     */
    public void loadData(ArrayList<String> selectedBoroughs) {
        for(int i = 0; i<listings.size(); i++) {
            if(selectedBoroughs.contains(listings.get(i).getNeighbourhood())) {
                data.add(listings.get(i));
            }
        }
    }

    public void dropDownClicked(javafx.event.ActionEvent actionEvent) {
        if(isDropClicked) {
            sortReviews.setVisible(false);
            sortPrice.setVisible(false);
            sortHost.setVisible(false);
            isDropClicked = false;
        } else {
            sortReviews.setVisible(true);
            sortPrice.setVisible(true);
            sortHost.setVisible(true);
            isDropClicked = true;
        }
    }

    /**
     * Sorts properties by number of reviews, price or host name.
     */
    public void updateSort(javafx.event.ActionEvent actionEvent) {
        if(((Button) actionEvent.getSource()).getId().equals("sortReviews")) {
            propertiesTable.getSortOrder().add(reviewsCountCol);
        }
     else if (((Button) actionEvent.getSource()).getId().equals("sortPrice")){
            propertiesTable.getSortOrder().add(boroughPriceCol);
        }
        // else if (((Button) actionEvent.getSource()).getId().equals("sortHost")){
           // propertiesTable.getSortOrder().add(boroughHostCol);
        // }
    }




    /**
     *
     */
    public void backNavigation() {
        try {
            FXMLLoader boroughLoader = new FXMLLoader(getClass().getResource("MainFrameView.fxml"));
            Parent root = boroughLoader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root, 600, 500));
            newStage.setResizable(false);
            newStage.show();
            MainWindowController mainWindowController = boroughLoader.getController();
            mainWindowController.initializeListings(listings);
            mainWindowController.updatePanel(3);
            propertiesTable.getScene().getWindow().hide();
        } catch (Exception e) {

        }
    }

    /**
     * A method which filters houses that have wi-fi as amenities.
     */
    @FXML
    public void filterWifi(ActionEvent event){
        if (wifiBox.isSelected()){
            displayData = data.stream() //displayData
                    .filter(airbnbListing -> airbnbListing.getAmenities().contains("Wifi"))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            propertiesTable.setItems(displayData);
        }
        else {
            propertiesTable.setItems(data);
        }
    }


    /**
     * A method which filters houses that are hosted by a super host.
     */
    @FXML
    public void filterSuperHost(ActionEvent event){
        if (superBox.isSelected()){
            displayData = data.stream()
                    .filter(airbnbListing -> airbnbListing.isHostSuperhost())
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            propertiesTable.setItems(displayData);
        }
        else {
            propertiesTable.setItems(data);
        }
    }

    /**
     * A method which filters houses that are private.
     */
    @FXML
    public void filterPrivateRoom(ActionEvent event){
        if (roomBox.isSelected()){
            displayData = data.stream()
                    .filter(airbnbListing -> airbnbListing.getRoomType().equals("Private room"))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            propertiesTable.setItems(displayData);
        }
        else {
            propertiesTable.setItems(data);
        }
    }

    /**
     * A method which filters houses that have pool as amenities.
     */
    @FXML
    public void filterPool(ActionEvent event){
        if (poolBox.isSelected()){
            displayData = data.stream()
                    .filter(airbnbListing -> airbnbListing.getAmenities().contains("Pool"))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
            propertiesTable.setItems(displayData);
        }
            else{
            propertiesTable.setItems(data);
        }
    }

    /**
     *
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sortReviews.setVisible(false);
        sortPrice.setVisible(false);
        sortHost.setVisible(false);
        isDropClicked = false;
    }

    @FXML
    public void rowClicked(MouseEvent e) throws IOException {
        if (e.getClickCount() == 2)
        {
            Object chosenObject = propertiesTable.getSelectionModel().getSelectedItem();
            if (chosenObject.getClass() == AirbnbListing.class) { // Safety check for cast
                AirbnbListing chosenProperty = (AirbnbListing) chosenObject;
                openPropertyDisplayView(chosenProperty);
            }
        }
    }

    // Change this
    private void openPropertyDisplayView(AirbnbListing property) throws IOException {
        //Parent root
        FXMLLoader displayerLoader = new FXMLLoader(getClass().getResource("PropertyDisplayerView.fxml"));
        Parent root = displayerLoader.load();
        Stage newStage = new Stage();
        newStage.setTitle("Property");
        newStage.setScene(new Scene(root, 600, 500));

        PropertyDisplayerController propertyDisplayer = displayerLoader.getController();
        propertyDisplayer.loadData(property);
        newStage.show();
    }
}
