package project_files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class BoroughPropertiesController implements Initializable {

    //
    TableColumn boroughHostCol;
    //
    TableColumn<AirbnbListing, Integer> boroughPriceCol;
    //
    TableColumn<AirbnbListing, Integer> reviewsCountCol;


    private MainFrameController mainFrameController;

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
    private ObservableList<AirbnbListing> boroughListings;
    private Listings listings;

    /**
     *
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sortReviews.setVisible(false);
        sortPrice.setVisible(false);
        sortHost.setVisible(false);
        isDropClicked = false;
        setFilterCheckBoxIds();
        buildTable();
    }

    private void setFilterCheckBoxIds() {
        wifiBox.setId(FilterNames.WIFI_FILTER.toString());
        poolBox.setId(FilterNames.POOL_FILTER.toString());
        superBox.setId(FilterNames.SUPER_FILTER.toString());
        roomBox.setId(FilterNames.ROOM_FILTER.toString());
    }

    private void buildTable() {
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

    }

    /**
     * Activates the stored checkbox filters.
     * Problem: this class is newly loaded every time and therefore the objects of checkbox are different ones --> cant compare checkboxes instead of strings rn.
     * Solution: Only create this controller once and later just show it. (Barni?)
     */
    private void setActivatedCheckboxFilters() {
        for (String filter : listings.getActiveFilters())
        {
            if (filter.equals(FilterNames.POOL_FILTER.toString()))
                poolBox.setSelected(true);
            else if (filter.equals(FilterNames.WIFI_FILTER.toString()))
                wifiBox.setSelected(true);
            else if (filter.equals(FilterNames.SUPER_FILTER.toString()))
                superBox.setSelected(true);
            else if (filter.equals(FilterNames.ROOM_FILTER.toString()))
                roomBox.setSelected(true);
            /*
            switch (box.getId())
            {
                case FilterNames.WIFI_FILTER.toString(): wifiBox.setSelected(true); break;
                case "poolBox": poolBox.setSelected(true); break;
                case "superBox": superBox.setSelected(true); break;
                case "roomBox": roomBox.setSelected(true); break;
            }
             */
        }
    }

    /**
     *
     */
    public void initializeListing(Listings listings, ArrayList<String> selectedBoroughs, Account currentUser)
    {
        // Loads the data.
        this.listings = listings;
        listings.changeSelectedBoroughs(selectedBoroughs); // Filter for the selected boroughs
        this.boroughListings = listings.getObservableFilteredListings();
        this.currentUser = currentUser;
        displayData = FXCollections.observableArrayList(boroughListings);
        setActivatedCheckboxFilters(); // Load the active user preferences
        propertiesTable.setItems(displayData);
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
            listings.changeSelectedBoroughs(new ArrayList<>()); // Reset the selected boroughs
            Stage stage = (Stage) mainFrameController.contentPane.getScene().getWindow();
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
            listings.changeActiveFilters(checkBox.getId());
            System.out.println(listings.getFilteredListings().size());
        }
        displayList();
    }


    private void displayList()
    {
        propertiesTable.setItems(listings.getObservableFilteredListings());
        System.out.println(listings.getObservableFilteredListings().size());
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
        propertyDisplayer.setMainWindowController(mainFrameController);
        propertyDisplayer.setBoroughPropertiesController(this);
        newStage.show();
    }

    public void setMainWindowController(MainFrameController mainFrameController) {
        this.mainFrameController = mainFrameController;
    }
}
