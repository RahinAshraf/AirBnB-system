package project_files;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class StatisticsPanel extends Panel {


    @FXML
    private void leftButton(ActionEvent event) {
        System.out.println("You have clicked the left button");
    }

    @FXML
    public void rightButton(ActionEvent event) {
        System.out.println("You have clicked the right button");
    }
}
