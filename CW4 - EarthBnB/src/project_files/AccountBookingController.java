package project_files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AccountBookingController implements Initializable {

    @FXML
    ListView fieldList;

    AccountPanelController accountPanelController;

    private ObservableList<String> reservationInfoList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fieldList.setItems(reservationInfoList);
    }


    public void LoadData(Reservation reservation, AccountPanelController accountPanelController) {

        this.accountPanelController = accountPanelController;

        reservationInfoList.add("ID: " + reservation.getReservationID());
        reservationInfoList.add("Check In: " + reservation.getArrival());
        reservationInfoList.add("Check Out: " + reservation.getDeparture());
        reservationInfoList.add("Total Cost: " + reservation.getPrice());
        reservationInfoList.add("# of Guests: " + reservation.getNumberOfGuests());
        reservationInfoList.add("ListingID: " + reservation.getListingID());

    }

    @FXML
    private void checkPropertyDetails() throws IOException {
        accountPanelController.checkProperty();
    }


}
