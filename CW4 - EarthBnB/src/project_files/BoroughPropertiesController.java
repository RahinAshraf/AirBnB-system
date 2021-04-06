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
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * This class displays a list of properties that are available in the selected boroughs. It also allows the user to
 * filter the tableview for price, hostname and # of reviews. It also allows the user to inspect a particular
 * property from the list.
 *
 * @author  Valentin Magis, Rahin Ashraf, Vandad Vafai Tabrizi, Barnabas Szalai
 * @version 1.0
 * @since   2021-03-11
 */
public class BoroughPropertiesController implements Initializable {

    private TableColumn boroughHostCol;
    private TableColumn<AirbnbListing, Integer> boroughPriceCol;
    private TableColumn<AirbnbListing, Integer> reviewsCountCol;
    private MainFrameController mainFrameController;
    private boolean isDropClicked;
    @FXML
    private Button backNavigation;
    @FXML
    private ComboBox comboBox;
    @FXML
    private TableView propertiesTable;
    @FXML
    private CheckBox wifiBox, poolBox, superBox, roomBox;

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
        isDropClicked = false;
        setFilterCheckBoxIds();
        buildTable();

        comboBox.getItems().addAll(
                "# of Reviews",
            "Price",
            "Host Name"
        );

    }

    /**
     * This method updates the ids and text of the filter checkboxes based on a static method in the FilterNames class.
     */
    private void setFilterCheckBoxIds() {
        wifiBox.setId(FilterNames.WIFI_FILTER.name());
        poolBox.setId(FilterNames.POOL_FILTER.name());
        superBox.setId(FilterNames.SUPER_FILTER.name());
        roomBox.setId(FilterNames.ROOM_FILTER.name());

        wifiBox.setText(FilterNames.WIFI_FILTER.toString());
        poolBox.setText(FilterNames.POOL_FILTER.toString());
        superBox.setText(FilterNames.SUPER_FILTER.toString());
        roomBox.setText(FilterNames.ROOM_FILTER.toString());
    }

    /**
     * This method creates the columns of the TableView and loads the rows, displaying the properties.
     */
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
        for (FilterNames filter : listings.getActiveFilters())
        {
            if (filter.name().equals(FilterNames.POOL_FILTER.name()))
                poolBox.setSelected(true);
            else if (filter.name().equals(FilterNames.WIFI_FILTER.name()))
                wifiBox.setSelected(true);
            else if (filter.name().equals(FilterNames.SUPER_FILTER.name()))
                superBox.setSelected(true);
            else if (filter.name().equals(FilterNames.ROOM_FILTER.name()))
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
     * This method is used to pass on default fields which the object can use later. It also initializes some of the
     * class's instance variables.
     * @param   listings    the filtered listings that are displayed after filtering for the borough
     * @param   selectedBoroughs    the list of boroughs that the user selected from the map
     * @param currentUser   the current logged in user
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

    /**
     * Sorts properties by number of reviews, price or host name.
     */
    public void updateSort(javafx.event.ActionEvent actionEvent) {
        propertiesTable.getSortOrder().clear();
        if((comboBox.getValue().equals("# of Reviews"))) {
            propertiesTable.getSortOrder().add(reviewsCountCol);
            propertiesTable.sort();
        }
        else if ((comboBox.getValue().equals("Price"))){
            propertiesTable.getSortOrder().add(boroughPriceCol);
            propertiesTable.sort();
        }
        else if ((comboBox.getValue().equals("Host Name"))){
            propertiesTable.getSortOrder().add(boroughHostCol);
            propertiesTable.sort();
        }
    }

    /**
     * This method is called when the back button is pressed. It closes the current window and opens a new stage
     * showing the map
     */
    public void backNavigation() {
        try {
            listings.changeSelectedBoroughs(new ArrayList<>()); // Reset the selected boroughs
            Stage stage = (Stage) mainFrameController.getWindow();
            stage.show();
            mainFrameController.updateCurrentPanel(); // make sure any modification made to filters are immediately loaded
            Stage thisStage = (Stage) propertiesTable.getScene().getWindow();
            thisStage.close();
        } catch (Exception e) {
            e.printStackTrace();
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
        if (e.getSource().getClass() == CheckBox.class) {
            checkBox = (CheckBox) e.getSource();
            if (FilterNames.getFilter(checkBox.getId()) != null)
            listings.toggleActiveFilter(FilterNames.getFilter(checkBox.getId())); // get the filternames object from the previously stored names. (risky stuff, bad code?)
            System.out.println("Toggled filter: ");
            for (FilterNames f : listings.getActiveFilters())
            {
                System.out.println(f.name() + " ");
            }
        }
        displayList();
        mainFrameController.setChoiceComboBoxFilters(); // Update the filter selection in the main frame. Would be enough to check when "back is clicked" but safer this way.
    }


    /**
     * This method loads the filtered listings into the tableview.
     */
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
        newStage.setScene(new Scene(root, 950, 650));

        PropertyDisplayerController propertyDisplayer = displayerLoader.getController();
        propertyDisplayer.loadData(property, currentUser); // Load the data into the window.
        propertyDisplayer.setMainWindowController(mainFrameController);
        propertyDisplayer.setBoroughPropertiesController(this);
        newStage.show();
    }

    /**
     * This method initiates the mainFrameController. It is called from the MapController.
     */
    public void setMainWindowController(MainFrameController mainFrameController) {
        this.mainFrameController = mainFrameController;
    }

    public Window getWindow()
    {
        return propertiesTable.getScene().getWindow();
    }
}
