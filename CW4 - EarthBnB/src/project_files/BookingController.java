package project_files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BookingController extends MainframeContentPanel implements Initializable {

    ArrayList<String> testFavorit; // Delete later!!

    ArrayList<AirbnbListing> listings;
    Account currentUser;
    TableColumn propertyIDCol;
    private ObservableList<AirbnbListing> data = FXCollections.observableArrayList();

    @FXML
    TableView favoritTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        testFavorit = new ArrayList<>();
        testFavorit.add("13913");
        testFavorit.add("17506");
        currentUser = null;
    }


    public void loadData(ArrayList<String> favoritBoroughs) {
        System.out.println(favoritBoroughs.size());
        System.out.println(listings.size());
        for(int i = 0; i<listings.size(); i++) {
            if(favoritBoroughs.contains(listings.get(i).getId())) {
                data.add(listings.get(i));
                System.out.println("added");
            }

        }
    }

    @Override
    public void initializeList(ArrayList<AirbnbListing> listings, Account currentUser) {
        System.out.println("called");
        this.currentUser = currentUser;
        this.listings = listings;
        loadData(testFavorit);
        propertyIDCol = new TableColumn("Property ID");
        propertyIDCol.setMinWidth(100);
        propertyIDCol.setMaxWidth(120);
        propertyIDCol.setCellFactory(TextFieldTableCell.forTableColumn());
        propertyIDCol.setCellValueFactory(new PropertyValueFactory<AirbnbListing, String>("id"));

        favoritTable.getColumns().addAll(propertyIDCol);
        favoritTable.setItems(data);
    }

}
