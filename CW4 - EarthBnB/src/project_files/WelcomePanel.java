package project_files;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.apache.commons.lang3.math.NumberUtils;

import java.sql.SQLException;
import java.time.LocalDate;
/**
 * The WelcomePanelClass - This contentPanel is first shown in the MainFrame. It welcomes the user.
 * The user has to enter a search request (and select a price range in the MainFrame) to be able to switch to other panels.
 * For the search, the user can select arrival and departure date and number of guests.
 *
 * @author Vandad Vafai Tabrizi, Rahin Ashraf, Valentin Magis
 * @version 1.0
 * @since 2021-03-11
 */

public class WelcomePanel extends MainframeContentPanel {

    @FXML
    Label welcomeLabel, enterLabel, minPriceRangeLabel, maxPriceRangeLabel;
    @FXML
    DatePicker checkIn, checkOut;
    @FXML
    TextField numberOfPeople;
    @FXML
    Button submitButton;

    /**
     * Create the welcome panel internals.
     */
    public WelcomePanel() {
        currentUser = null;
        name = "Welcome";
    }


    /**
     * Implements the abstract method in MainframeContentPanel.
     * Gives this panel a reference to the listings and the current user to get/manipulate data.
     * @param listings The listings.
     * @param currentUser The currently logged in user.
     */
    @Override
    public void initializeData(Listings listings, Account currentUser) {
    }

    /**
     * Update the current panel. The welcome panel does not display anything related to the user or the the statistics
     * and therefore no action is performed here.
     */
    @Override
    public void updatePanel() {};


    /**
     * Saves the data and moves it on to the booking panel.
     * @param e
     */
    @FXML
    private void setSubmitButton(ActionEvent e) throws SQLException {
        if (checkEntries()) {
            int people = Integer.parseInt(numberOfPeople.getText());
            currentUser.setBookingData(checkIn.getValue(), checkOut.getValue(), people, currentUser.getAccountID());
            mainFrameController.getListings().changeBookingData(currentUser.getBookingData());
            mainFrameController.setFirstRequestSubmitted(true);
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
        if (e.getSource().getClass() == DatePicker.class) {
            DatePicker datePicker = (DatePicker) e.getSource();

            if (datePicker.getId().equalsIgnoreCase("checkIn")) { // The chosen checkIn date is in the past
                if (checkIn.getValue() != null && checkIn.getValue().isBefore(LocalDate.now())) {
                    checkIn.setValue(null);
                    pastDateAlert();
                }
            }
            else { // CheckOut was selected

                if (checkOut.getValue() != null && checkOut.getValue().isBefore(LocalDate.now())) { // The chosen checkout date is in the past
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

    /**
     * Set labels displaying the price range
     * @param min The String of the minimum price range.
     * @param max The String of the maximum price range.
     */
    public void setPriceRangeLabels(Integer min, Integer max)
    {
        if (min != null)
            minPriceRangeLabel.setText(min.toString());
        else
            minPriceRangeLabel.setText("-");
        if (max != null)
            maxPriceRangeLabel.setText(max.toString());
        else
            maxPriceRangeLabel.setText("-");
    }



    // Alerts for wrong user interaction

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
    /**
     * An alert which occurs when an invalid string has been entered for the number of people
     */
    private void setNumberOfPeopleAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Invalid number of people entered");
        alert.setContentText("You have to book for at least one person.");
        alert.showAndWait();
    }

    /**
     * An alert which occurs when information is missing.
     */
    private void missingInformationAlert()
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("");
        alert.setHeaderText("Missing Information");
        alert.setContentText("Please enter all information before searching..");
        alert.showAndWait();
    }
}
