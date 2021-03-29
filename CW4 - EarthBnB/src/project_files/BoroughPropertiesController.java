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
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


public class BoroughPropertiesController implements Initializable {

    //
    TableColumn boroughHostCol;
    //
    TableColumn<AirbnbListing, Integer> boroughPriceCol;
    //
    TableColumn<AirbnbListing, Integer> reviewsCountCol;



    private MainWindowController mainWindowController;

    //
    private boolean isDropClicked;

    //
    @FXML
    Button backNavigation, sortReviews, sortPrice, sortHost;
    //
    @FXML
    TableView propertiesTable;

    @FXML
    CheckBox wifiBox, poolBox, superBox, roomBox;


    // The new data which will be set if any of the filters are chosen.
    private ObservableList<AirbnbListing> displayData = FXCollections.observableArrayList();
    private Account currentUser;

    // The list of the properties in the selected boroughs
    ArrayList<AirbnbListing> boroughListings;
    ArrayList<AirbnbListing> listings;

    // An Array List that stores check boxes that hold filters.
    private ArrayList<CheckBox> activeFilters = new ArrayList<>();

    /**
     *
     */
    public void initializeListing(ArrayList<AirbnbListing> listings, ArrayList<String> selectedBoroughs, Account currentUser)
    {
        // Loads the data.
        this.boroughListings = filterBoroughs(listings, selectedBoroughs);
        this.listings = listings;
        this.currentUser = currentUser;
        displayData.addAll(boroughListings);

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
        propertiesTable.setItems(displayData);

    }

    /**
     * Loads the data from the csv file into the table.
     */
    public ArrayList<AirbnbListing> filterBoroughs(ArrayList<AirbnbListing> listings, ArrayList<String> selectedBoroughs) {

        return listings.stream().filter(listing -> selectedBoroughs.contains(listing.getNeighbourhood()))
                .collect(Collectors.toCollection(ArrayList::new));
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
        propertiesTable.getSortOrder().clear();

        if(((Button) actionEvent.getSource()).getId().equals("sortReviews")) {
            propertiesTable.getSortOrder().add(reviewsCountCol);
            propertiesTable.sort();
        }
     else if (((Button) actionEvent.getSource()).getId().equals("sortPrice")){
            propertiesTable.getSortOrder().add(boroughPriceCol);
            propertiesTable.sort();
        }
         else if (((Button) actionEvent.getSource()).getId().equals("sortHost")){
            propertiesTable.getSortOrder().add(boroughHostCol);
            propertiesTable.sort();
         }
    }

    // FUCKED
    /**
     *
     */
    public void backNavigation() {
        try {
            Stage stage = (Stage) mainWindowController.contentPane.getScene().getWindow();
            stage.show();
            Stage thisStage = (Stage) propertiesTable.getScene().getWindow();
            thisStage.close();
        } catch (Exception e) {

        }
    }

    /**
     * Invoked every time a filter is checked or unchecked.
     * Accordingly adds or removes the filter from the list of active filters which the list then is filtered by and
     * then initiates the list to be filtered with the new composition of activated filters.
     * @param e
     */
    @FXML
    public void changeFilter(ActionEvent e)
    {
        CheckBox checkBox;
        System.out.println(e.getSource());
        if (e.getSource().getClass() == CheckBox.class) {
            checkBox = (CheckBox) e.getSource();
            if (activeFilters.contains(checkBox))
                activeFilters.remove(checkBox);
            else
                activeFilters.add(checkBox);
        }
        filter(activeFilters);
    }


    /**
     * Filters the list by the filters passed in.
     * Depends on the id of the checkboxes.
     * @param activeFilters The filters to be applied.
     */
    private void filter(ArrayList<CheckBox> activeFilters){
        //Reset the display data
        displayData.clear();
        displayData.addAll(boroughListings);

        for (CheckBox filter : activeFilters)
        {
            switch(filter.getId())
            {
                case "wifiBox": displayData = filterAmenity(displayData, "Wifi"); break;
                case "poolBox": displayData = filterAmenity(displayData, "Pool"); break;
                case "roomBox": displayData = filterPrivateRoom(displayData); break;
                case "superBox": displayData = filterSuperHost(displayData); break;
            }
        }
        propertiesTable.setItems(displayData);
    }


    /**
     * Filter the given list by an amenity.
     * @param list The list to be filtered.
     * @param filterString The amenity to be filtered by.
     * @return A new list only containing the properties which supply the specified amenity.
     */
    private ObservableList<AirbnbListing> filterAmenity(ObservableList<AirbnbListing> list, String filterString)
    {
        return list.stream()
            .filter(airbnbListing -> airbnbListing.getAmenities().contains(filterString))
            .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }


    /**
     * A method which filters houses that are hosted by a super host.
     * @param list The list to be filtered.
     * @return A new list only containing superhosts.
     */
    public ObservableList<AirbnbListing> filterSuperHost(ObservableList<AirbnbListing> list){
            return list.stream()
                    .filter(airbnbListing -> airbnbListing.isHostSuperhost())
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
    }

    /**
     * A method which filters houses that are private.
     * @param list The list to be filtered.
     * @return A new list only containing private rooms.
     */
    public ObservableList<AirbnbListing> filterPrivateRoom(ObservableList<AirbnbListing> list){
           return list.stream()
                    .filter(airbnbListing -> airbnbListing.getRoomType().equals("Private room"))
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));
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

    /**
     * Initiated when a row in the tableview has been clicked. Initiates opening up a new window displaying further information
     * about the property.
     * @param e
     * @throws IOException
     */
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


    /**
     * Opens up a new window displaying information about the property.
     * @param property
     * @throws IOException
     */
    private void openPropertyDisplayView(AirbnbListing property) throws IOException {
        //Parent root
        FXMLLoader displayerLoader = new FXMLLoader(getClass().getResource("PropertyDisplayerView.fxml"));
        Parent root = displayerLoader.load();
        Stage newStage = new Stage();
        newStage.setTitle("Property");
        newStage.setScene(new Scene(root, 890, 560));

        PropertyDisplayerController propertyDisplayer = displayerLoader.getController();
        propertyDisplayer.loadData(property, currentUser); // Load the data into the window.
        propertyDisplayer.setMainWindowController(mainWindowController);
        propertyDisplayer.setBoroughPropertiesController(this);
        newStage.show();
    }

    public void setMainWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }
}
