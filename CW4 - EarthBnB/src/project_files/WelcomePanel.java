package project_files;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.time.LocalDate;


public class WelcomePanel extends Panel {

    @FXML
    Label welcomeLabel;
    @FXML
    Label enterLabel;
    @FXML
    DatePicker checkIn;
    @FXML
    DatePicker checkOut;


    /**
     * Checks whether the check-in date is not after the check out date.
     */
    @FXML
    private void checkDate(ActionEvent event){
        if (checkIn.getValue().isAfter(checkOut.getValue())){
            checkOutDateAlert();
            checkOut.getValue().isEqual(checkIn.getValue());
        }

        if (checkIn.getValue().isBefore(LocalDate.now())){
            checkInDateAlert();
        }
    }

    private void checkOutDateAlert(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Error while choosing date.");
        alert.setContentText("Make sure that you are entering the check out date after the check in date!");
        alert.showAndWait();
    }

    private void checkInDateAlert(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText("Error while choosing date.");
        alert.setContentText("You can't check in before today!");
        alert.showAndWait();
    }
}
