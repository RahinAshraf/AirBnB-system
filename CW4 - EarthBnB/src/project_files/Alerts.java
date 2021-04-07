package project_files;

import javafx.scene.control.Alert;

/**
 * Alerts class - Instead of writing the codes for the alerts in each class, this class
 * is designed to help us use more cohesive and short code.
 *
 * @author Valentin Magis, Rahin Ashraf, Vandad Vafai Tabrizi, Barnabas Szalai
 * @since  2021-04-04
 */
public class Alerts {

    /**
     * A method which creates a warning alert.
     *
     * @param titleText The text which is set for the title
     * @param headerText The text which is set for the header
     * @param contentText The text which is set for the content
     */
    public static void warningAlert(String titleText, String headerText, String contentText){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titleText);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        alert.showAndWait();
    }

    /**
     * A method which creates an information alert.
     *
     * @param titleText The text which is set for the title
     * @param headerText The text which is set for the header
     * @param contentText The text which is set for the content
     */
    public static void informationAlert(String titleText, String headerText, String contentText){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titleText);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        alert.showAndWait();
    }

    /**
     * A method which creates an error alert.
     *
     * @param titleText The text which is set for the title
     * @param headerText The text which is set for the header
     * @param contentText The text which is set for the content
     */
    public static void errorAlerts(String titleText, String headerText, String contentText){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titleText);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);

        alert.showAndWait();
    }

}
