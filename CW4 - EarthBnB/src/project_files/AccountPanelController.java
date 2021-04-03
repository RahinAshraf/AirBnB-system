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
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AccountPanelController implements Initializable {

    //private ArrayList<AirbnbListing> filteredListings; = new ArrayList<>();
    //private Listings listings;
    private Account currentUser;
    private ObservableList<String> userInformationList = FXCollections.observableArrayList();


    @FXML
    ListView informationList;

    public void initializeAccount(Account currentUser) {
        //this.listings = listings;
        //filteredListings = listings.getFilteredListings();
        this.currentUser = currentUser;
        loadUserInformation();
        informationList.setItems(userInformationList);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }
    public void loadUserInformation() {

        userInformationList.add("Account ID: " + (currentUser.getAccountID()));
        userInformationList.add("Username: " + currentUser.getUsername());
        userInformationList.add("Email Address: " + currentUser.getEmailAddress());
    }

    private String hashPW(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        messageDigest.reset();
        messageDigest.update(password.getBytes(StandardCharsets.UTF_8));

        return String.format("%0128x", new BigInteger(1, messageDigest.digest()));
    }
}
