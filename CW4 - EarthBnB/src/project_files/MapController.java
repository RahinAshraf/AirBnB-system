package project_files;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.util.*;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.HashMap;
import java.util.stream.Collectors;

public class MapController implements Initializable {

    private Button selectedBorough;
    private Double filterValue;
    private Map<String, Long> propertyCount = new HashMap<String, Long>();
    private String boroughs[] = new String[32];

    @FXML
    Slider filterSlider;

    @FXML
    AnchorPane mapView;

    ArrayList<AirbnbListing> listings;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void selectBorough(javafx.event.ActionEvent actionEvent) {
        selectNewBorough((Button)actionEvent.getSource());
    }

    public void initializeMap(ArrayList<AirbnbListing> listings)
    {
        this.listings = listings;
        updateBoroughs();
        System.out.println(propertyCount.get("Westminster"));
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

        if (selectedBorough != null) {
            selectedBorough.setStyle("-fx-background-color: #FFFFFF");
        }

        button.setStyle("-fx-background-color: #50B4D4");

        try {
            FXMLLoader boroughLoader = new FXMLLoader(getClass().getResource("boroughPropertiesView.fxml"));
            Parent root = boroughLoader.load();
            Stage newStage = new Stage();
            newStage.setScene(new Scene(root, 600, 500));
            newStage.setResizable(false);
            newStage.show();
            BoroughPropertiesController boroughController = boroughLoader.getController();
            boroughController.initializeMap(listings);
            mapView.getScene().getWindow().hide();
        } catch(Exception e) {
            e.printStackTrace();
        }

        selectedBorough = button;
        String borough = button.getId();
        System.out.println(borough);
    }

    public void updateFilter(MouseEvent mouseEvent) {
        filterValue = filterSlider.getValue();
    }

    private void updateBoroughs() // !! Change to only save the first word of the borough
    {
        resizePropertyNeighbourhood();
        propertyCount = listings.stream()
                .collect(Collectors.groupingBy(AirbnbListing::getNeighbourhood, Collectors.counting()));
    }


    public void resizePropertyNeighbourhood() {
        for(int i = 0; i<listings.size(); i++) {
           listings.get(i).chopNeighbourhoodName();
        }
    }
}
