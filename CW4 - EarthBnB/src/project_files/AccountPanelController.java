package project_files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;

import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AccountPanelController implements Initializable {

    private ArrayList<AirbnbListing> listings = new ArrayList<>();
    private Account currentUser;
    private ObservableList<String> data = FXCollections.observableArrayList();

    TableColumn infoCol;

    @FXML
    ListView informationList;

    public void initializeAccount(ArrayList<AirbnbListing> listings, Account currentUser) {
        this.listings = listings;
        this.currentUser = currentUser;
        loadData();
        informationList.setItems(data);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }
    public void loadData() {
        data.add("Account ID: " + (currentUser.getAccountID()));
        data.add("Username: " + currentUser.getUsername());
        data.add("Password: " + currentUser.getPassword());
        data.add("Email Address: " + currentUser.getEmailAddress());
    }
}
