package project_files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.Observable;
import java.util.ResourceBundle;

public class BoroughPropertiesController implements Initializable {

    @FXML
    Button backNavigationbackNavigation;

    @FXML
    TableView propertiesTable;

    private final ObservableList<AirbnbListing> data = FXCollections.observableArrayList();

    ArrayList<AirbnbListing> listings;

    public void initializeListing(ArrayList<AirbnbListing> listings, ArrayList<String> selectedBoroughs)
    {
        this.listings = listings;

        loadData(selectedBoroughs);

        TableColumn boroughHostCol = new TableColumn("Host Name");
        boroughHostCol.setMinWidth(100);
        boroughHostCol.setMaxWidth(120);
        boroughHostCol.setCellFactory(TextFieldTableCell.forTableColumn());
        boroughHostCol.setCellValueFactory(new PropertyValueFactory<AirbnbListing, String>("hostName"));

        TableColumn boroughCol = new TableColumn("Borough");
        boroughCol.setMinWidth(100);
        boroughCol.setMaxWidth(120);
        boroughCol.setCellFactory(TextFieldTableCell.forTableColumn());
        boroughCol.setCellValueFactory(new PropertyValueFactory<AirbnbListing, String>("neighbourhood"));

        TableColumn<AirbnbListing, Integer> boroughPriceCol = new TableColumn<>("Price");
        boroughPriceCol.setMinWidth(100);
        boroughPriceCol.setMaxWidth(120);
        //boroughPriceCol.setCellFactory(TextFieldTableCell.forTableColumn());
        PropertyValueFactory temp = new PropertyValueFactory<AirbnbListing, Integer> (("price"));
        boroughPriceCol.setCellValueFactory(temp);

        TableColumn<AirbnbListing, Integer> reviewsCountCol = new TableColumn<>("# of Reviews");
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


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
