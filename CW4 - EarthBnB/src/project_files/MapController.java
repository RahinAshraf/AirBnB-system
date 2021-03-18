package project_files;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.util.*;

import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

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
        for(int i=0; i<32; i++) {
            String buttonID = mapView.getChildren().get(i).getId();
            Long boroughPropertyCount = propertyCount.get(buttonID);
            try {
                if (boroughPropertyCount > 5000) {
                    mapView.getChildren().get(i).setStyle("-fx-background-color: #FF1515");
                } else {
                    mapView.getChildren().get(i).setStyle("-fx-background-color: #FFB06F");
                }
            } catch (Exception e) {
                System.out.println("error");
            }
        }

    }

    public void selectNewBorough(Button button) {
        if (selectedBorough != null) {
            selectedBorough.setStyle("-fx-background-color: #FFFFFF");
        }

        button.setStyle("-fx-background-color: #50B4D4");

        selectedBorough = button;
        String borough = button.getId();
        System.out.println(borough);
    }

    public void updateFilter(MouseEvent mouseEvent) {
        filterValue = filterSlider.getValue();
    }

    private void updateBoroughs() // !! Change to only save the first word of the borough
    {
        propertyCount = listings.stream()
                .collect(Collectors.groupingBy(AirbnbListing::getNeighbourhood, Collectors.counting()));
    }
}
