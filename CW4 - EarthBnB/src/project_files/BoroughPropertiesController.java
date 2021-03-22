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
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BoroughPropertiesController implements Initializable {

    TableColumn boroughHostCol;
    TableColumn<AirbnbListing, Integer> boroughPriceCol;
    TableColumn<AirbnbListing, Integer> reviewsCountCol;

    private boolean isDropClicked;

    @FXML
    Button backNavigationbackNavigation;

    @FXML
    TableView propertiesTable;

    @FXML
    Button sortReviews;
    @FXML
    Button sortPrice;
    @FXML
    Button sortHost;

    private final ObservableList<AirbnbListing> data = FXCollections.observableArrayList();

    ArrayList<AirbnbListing> listings;

    public void initializeListing(ArrayList<AirbnbListing> listings, ArrayList<String> selectedBoroughs)
    {
        this.listings = listings;

        loadData(selectedBoroughs);

        boroughHostCol = new TableColumn("Host Name");
        boroughHostCol.setMinWidth(100);
        boroughHostCol.setMaxWidth(120);
        boroughHostCol.setCellFactory(TextFieldTableCell.forTableColumn());
        boroughHostCol.setCellValueFactory(new PropertyValueFactory<AirbnbListing, String>("hostName"));

        TableColumn boroughCol = new TableColumn("Borough");
        boroughCol.setMinWidth(100);
        boroughCol.setMaxWidth(120);
        boroughCol.setCellFactory(TextFieldTableCell.forTableColumn());
        boroughCol.setCellValueFactory(new PropertyValueFactory<AirbnbListing, String>("neighbourhood"));

        boroughPriceCol = new TableColumn<>("Price");
        boroughPriceCol.setMinWidth(100);
        boroughPriceCol.setMaxWidth(120);
        //boroughPriceCol.setCellFactory(TextFieldTableCell.forTableColumn());
        PropertyValueFactory temp = new PropertyValueFactory<AirbnbListing, Integer> (("price"));
        boroughPriceCol.setCellValueFactory(temp);

        reviewsCountCol = new TableColumn<>("# of Reviews");
        reviewsCountCol.setMinWidth(100);
        reviewsCountCol.setMaxWidth(120);
        //boroughPriceCol.setCellFactory(TextFieldTableCell.forTableColumn());
        PropertyValueFactory reviewsTemp = new PropertyValueFactory<AirbnbListing, Integer> (("numberOfReviews"));
        reviewsCountCol.setCellValueFactory(reviewsTemp);

        TableColumn<AirbnbListing, Integer> minimumNightsCol = new TableColumn<>("Minimum Nights");
        minimumNightsCol.setMinWidth(100);
        minimumNightsCol.setMaxWidth(120);
        //boroughPriceCol.setCellFactory(TextFieldTableCell.forTableColumn());
        PropertyValueFactory nightsTemp = new PropertyValueFactory<AirbnbListing, Integer> (("minimumNights"));
        minimumNightsCol.setCellValueFactory(nightsTemp);

        propertiesTable.getColumns().addAll(boroughHostCol, boroughPriceCol, boroughCol, reviewsCountCol, minimumNightsCol);
        propertiesTable.setItems(data);

    }

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

    public void updateSort(javafx.event.ActionEvent actionEvent) {
        if(((Button) actionEvent.getSource()).getId().equals("sortReviews")) {
            propertiesTable.getSortOrder().add(reviewsCountCol);
        }
    }

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
            mainWindowController.updatePanel(1);
            propertiesTable.getScene().getWindow().hide();
        } catch (Exception e) {

        }
    }


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
