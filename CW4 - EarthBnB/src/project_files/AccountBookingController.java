package project_files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;




/**
 * This class provides the controller for the accountBookingView. It displays information about an upcoming reservation
 * that the user has made. The user can also view details about the particular property using a button on the view that
 * is handled by this controller.
 *
 * @author  Valentin Magis, Rahin Ashraf, Vandad Vafai Tabrizi, Barnabas Szalai
 * @version 1.0
 * @since   2021-03-11
 */
public class AccountBookingController implements Initializable {

    @FXML
    private ListView fieldList;

    private AccountPanelController accountPanelController;

    private ObservableList<String> reservationInfoList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fieldList.setItems(reservationInfoList);
    }



    /**
     * This method is called by the class that instantiated the accountBookingView. It is used to load the ListView
     * with data about the reservation that is perceived.
     * @param reservation the reservation that the user is inspecting
     * @param accountPanelController the instance of the accountPanelController that instantiated the accountBookinView
     */
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
