package project_files;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

import java.util.*;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.HashMap;
import java.util.stream.Collectors;

public class MapController extends MainframeContentPanel implements Initializable {

    //private Button selectedBorough;
    private Double filterValue;
    private Map<String, Long> propertyCount = new HashMap<String, Long>();
    private String boroughs[] = new String[32];


    private ArrayList<Button> selectedBoroughs;

    @FXML
    Slider filterSlider;

    @FXML
    AnchorPane mapView;

    public MapController()
    {
        name = "Map";
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        selectedBoroughs = new ArrayList<>();
        currentUser = null;
    }

    public void selectBorough(javafx.event.ActionEvent actionEvent) {
        selectNewBorough((Button)actionEvent.getSource());
    }

    public void searchProperties(javafx.event.ActionEvent actionEvent) {
        for(int i = 0; i<selectedBoroughs.size(); i++) {
            System.out.println(selectedBoroughs.get(i));
        }

        try {
            FXMLLoader boroughLoader = new FXMLLoader(getClass().getResource("boroughPropertiesView.fxml"));
            Parent root = boroughLoader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root, 800, 500));
            newStage.setResizable(true);
            newStage.show();
            String titleString = "";
            for(int i = 0; i<selectedBoroughs.size(); i++) {
                if(i == 0) {
                    titleString = titleString + "" + selectedBoroughs.get(i).getId();
                } else {
                    titleString = titleString + ", " + selectedBoroughs.get(i).getId();
                }
            }
            newStage.setTitle(titleString);
            BoroughPropertiesController boroughController = boroughLoader.getController();
            ArrayList<String> tempArray = new ArrayList<>();
            for(int i = 0; i<selectedBoroughs.size(); i++) {
                tempArray.add(selectedBoroughs.get(i).getId());
            }
            //boroughController.initializeListing(listings, tempArray, currentUser);
            boroughController.setMainWindowController(mainFrameController); // Passing on the mainWindowController Object
            mapView.getScene().getWindow().hide();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initializeList(Listings listings, Account currentUser)
    {
        this.listings = listings;
        this.currentUser = currentUser;
        updatePanel();
    }

    @Override
    public void updatePanel() {
        updateBoroughs();
        for(int i=0; i<33; i++) {
            String buttonID = mapView.getChildren().get(i).getId();
            int boroughPropertyCount;
            try {
                boroughPropertyCount = propertyCount.get(buttonID).intValue();
            } catch(Exception e) {
                boroughPropertyCount = 0;
            }
            try {
                if (boroughPropertyCount >= 5000) {
                    mapView.getChildren().get(i).setStyle("-fx-background-color: #FF1515");
                    System.out.println("Index: " + i + ", " + mapView.getChildren().get(i).getId() + ": " + boroughPropertyCount);
                } else if (boroughPropertyCount >= 300 && boroughPropertyCount < 5000) {
                    mapView.getChildren().get(i).setStyle("-fx-background-color: #FFB06F");
                    System.out.println("Index: " + i + ", " + mapView.getChildren().get(i).getId() + ": " + boroughPropertyCount);
                } else if (boroughPropertyCount > 0 && boroughPropertyCount < 300) {
                    mapView.getChildren().get(i).setStyle("-fx-background-color: #F2E02C");
                    System.out.println("Index: " + i + ", " + mapView.getChildren().get(i).getId() + ": " + boroughPropertyCount);
                } else {
                    mapView.getChildren().get(i).setStyle("-fx-background-color: #CCCCCC");
                    System.out.println("Index: " + i + ", " + mapView.getChildren().get(i).getId() + ": " + boroughPropertyCount);
                }
            } catch (Exception e) {
                System.out.println("error: " + i);
            }
        }
    }

    public void selectNewBorough(Button button) {

        if (selectedBoroughs.contains(button)) {
            selectedBoroughs.remove(button);
            button.setStyle("-fx-background-color: #FFFFFF"); // Change back to old color instead of white
        } else {
            selectedBoroughs.add(button);
            button.setStyle("-fx-background-color: #50B4D4");
        }
        String borough = button.getId();
        System.out.println(borough);
    }

    public void updateFilter(MouseEvent mouseEvent) {
        filterValue = filterSlider.getValue();
    }

    private void updateBoroughs() // !! Change to only save the first word of the borough
    {
        resizePropertyNeighbourhood();
        propertyCount = listings.getFilteredListings().stream()
                .collect(Collectors.groupingBy(AirbnbListing::getNeighbourhood, Collectors.counting()));
    }


    public void resizePropertyNeighbourhood() {
        for(int i = 0; i<listings.getFilteredListings().size(); i++) {
           listings.getFilteredListings().get(i).chopNeighbourhoodName();
        }
    }

}
