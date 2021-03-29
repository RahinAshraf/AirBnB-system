package project_files;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.commons.lang3.math.NumberUtils;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * The WelcomePanelClass -
 */

public class WelcomePanel extends MainframeContentPanel {


    @FXML
    Label welcomeLabel;
    @FXML
    Label enterLabel;
    @FXML
    DatePicker checkIn;
    @FXML
    DatePicker checkOut;
    @FXML
    TextField numberOfPeople;
    @FXML
    Button submitButton;

    public WelcomePanel() {
        currentUser = null;
        name = "Welcome";
    }



    @Override
    public void initializeList(ArrayList<AirbnbListing> listings, Account currentUser) {
    }

    /**
     * An alert which occurs when the check-out date is not valid.
     */
    private void checkOutDateAlert(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Error while choosing date.");
        alert.setContentText("Make sure that you are entering the check out date after the check in date!");
        alert.showAndWait();
    }

    /**
     * An alert which occurs when the check-in date is not valid.
     */
    private void pastDateAlert(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Error while choosing date.");
        alert.setContentText("You can't book before today!");
        alert.showAndWait();
    }

    /**
     * An alert which occurs when the check-in date is not valid.
     */
    private void submitAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Successful!");
        alert.setHeaderText("Details saved successfully!");
        alert.setContentText("Your booking details have been saved.");
        alert.showAndWait();
    }

    private void setNumberOfPeopleAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Invalid number of people entered");
        alert.setContentText("You have to book for at least one person.");
        alert.showAndWait();
    }

    private void missingInformationAlert()
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("");
        alert.setHeaderText("Missing Information");
        alert.setContentText("Please enter all information before searching..");
        alert.showAndWait();
    }

    /**
     * Saves the data and moves it on to the booking panel.
     * @param event
     */
    @FXML
    private void setSubmitButton(ActionEvent event){
        if (checkEntries()) {
            int people = Integer.parseInt(numberOfPeople.getText());
            currentUser.setBookingData(checkIn.getValue(), checkOut.getValue(), people, currentUser.getAccountID());
            mainWindowController.setFrameSwitchingButtonsActive(true);
            submitAlert();
        }
    }

    /**
     * Safety check before booking. Dates, if entered, have to be valid already because of the live checking.
     * If not entered, an alert is displayed.
     * @return If all fields have been entered and if the number of guests is valid.
     */
    private boolean checkEntries()
    {
        if (checkIn.getValue() != null && checkOut.getValue() != null && numberOfPeople.getText() != null) {
            if (NumberUtils.isParsable(numberOfPeople.getText()) && Integer.parseInt(numberOfPeople.getText()) > 0)
                return true;
            else {
                setNumberOfPeopleAlert();
                return false;
            }
        }
        else {
            missingInformationAlert();
            return false;
        }
    }

    /**
     * Live check if the entered dates are valid.
     * @param e
     */
    @FXML
    private void setDate(ActionEvent e)
    {
        if (e.getSource().getClass() == DatePicker.class)
        {
            DatePicker datePicker = (DatePicker) e.getSource();

            if (datePicker.getId().equalsIgnoreCase("checkIn")) { // The chosen checkIn date is in the past
                if (checkIn.getValue() != null && checkIn.getValue().isBefore(LocalDate.now())) {
                    checkIn.setValue(null);
                    pastDateAlert();
                }
            }
            else { // CheckOut was selected

                if (checkOut.getValue() != null && checkOut.getValue().isBefore(LocalDate.now())) // The chosen checkout date is in the past
                {
                    checkOut.setValue(null);
                    pastDateAlert();
                }
                if (checkIn.getValue() != null && checkOut.getValue() != null && (!checkIn.getValue().isBefore(checkOut.getValue()))) { // There is a checkIn Date entered and the chosen checkOut date is before that.
                    checkOut.setValue(null);
                    checkOutDateAlert();
                }
            }
        }
    }
}
