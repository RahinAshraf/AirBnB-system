package project_files;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.util.Date;


public class WelcomePanel extends Panel {


    @FXML
    Label welcomeLabel;
    @FXML
    Label enterLabel;
    @FXML
    DatePicker checkIn;
    @FXML
    DatePicker checkOut;
    @FXML
    int numberOfPeople;
    @FXML
    Button submitButton;

    /**
     * Checks whether the dates are entered correctly.
     */
    private Boolean checkDate(){
        if (checkIn.getValue().isAfter(checkOut.getValue())){
            checkOutDateAlert();
            checkOut.getValue().isEqual(checkIn.getValue());
            return false;
        }

        else if (checkIn.getValue().isBefore(LocalDate.now())){
            checkInDateAlert();
            return false;
        }
        else {
            return true;
        }
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
    private void checkInDateAlert(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Error while choosing date.");
        alert.setContentText("You can't check in before today!");
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
     * Saves the data and moves it on to the booking panel.
     * @param event
     */
    @FXML
    private void setSubmitButton(ActionEvent event){
        try {
            if (checkDate()) {
                BookingData bookingData = new BookingData((Date) checkIn.getUserData() ,(Date) checkOut.getUserData(), numberOfPeople);
                submitAlert();
            }
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
}
