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


    ArrayList<AirbnbListing> listings;
    Account currentUser;
    TableColumn propertyNameCol;
    private ObservableList<AirbnbListing> data = FXCollections.observableArrayList();

    @FXML
    TableView favoritTable;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentUser = null;
    }


    public void loadData() {
        System.out.println(listings.size());
        for(int i = 0; i<listings.size(); i++) {
            if(currentUser.getSavedProperties().contains(listings.get(i))) {
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
        loadData();

        propertyNameCol = new TableColumn("Name");
        propertyNameCol.setMinWidth(300);
        propertyNameCol.setMaxWidth(300);
        propertyNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        propertyNameCol.setCellValueFactory(new PropertyValueFactory<AirbnbListing, String>("name"));

        TableColumn propertyBoroughCol = new TableColumn("Borough");
        propertyBoroughCol.setMinWidth(300
        );
        propertyBoroughCol.setMaxWidth(300);
        propertyBoroughCol.setCellFactory(TextFieldTableCell.forTableColumn());
        propertyBoroughCol.setCellValueFactory(new PropertyValueFactory<AirbnbListing, String>("neighbourhood"));

        favoritTable.getColumns().addAll(propertyNameCol, propertyBoroughCol);
        favoritTable.setItems(data);
    }

}
