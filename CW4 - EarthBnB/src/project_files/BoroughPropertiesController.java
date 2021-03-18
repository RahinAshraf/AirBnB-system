package project_files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

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

    public void initializeMap(ArrayList<AirbnbListing> listings)
    {
        this.listings = listings;

        loadData();

        TableColumn boroughHostCol = new TableColumn("Host Name");
        boroughHostCol.setMinWidth(100);
        boroughHostCol.setMaxWidth(120);
        boroughHostCol.setCellFactory(TextFieldTableCell.forTableColumn());
        boroughHostCol.setCellValueFactory(new PropertyValueFactory<AirbnbListing, String>("hostName"));

        TableColumn<AirbnbListing, Integer> boroughPriceCol = new TableColumn<>("Price");
        boroughPriceCol.setMinWidth(100);
        boroughPriceCol.setMaxWidth(120);
        //boroughPriceCol.setCellFactory(TextFieldTableCell.forTableColumn());
        PropertyValueFactory temp = new PropertyValueFactory<AirbnbListing, Integer> (("price"));
        boroughPriceCol.setCellValueFactory(temp);

        propertiesTable.getColumns().addAll(boroughHostCol, boroughPriceCol);
        propertiesTable.setItems(data);

    }

    public void loadData() {
        for(int i = 0; i<50; i++) {
            data.add(listings.get(i));
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
