package project_files;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Test Class for the borough property controller.
 *
 * @author Vandad Vafai Tabrizi
 */
public class BoroughPropertiesControllerTest {

    private BoroughPropertiesController boroughController = new BoroughPropertiesController();

    private ObservableList<AirbnbListing> listings;
    private ObservableList<AirbnbListing> filteredList;

    /**
     *
     */
    @Test
    public void initializeListing() {
    }

    /**
     *
     */
    @Test
    public void filterBoroughs() {
    }

    /**
     *
     */
    @Test
    public void dropDownClicked() {
    }

    /**
     *
     */
    @Test
    public void updateSort() {
    }

    /**
     *
     */
    @Test
    public void backNavigation() {
    }

    /**
     *
     */
    @Test
    public void changeFilter() {
    }

    /**
     *
     */
    @Test
    public void filterAmenity() throws MalformedURLException {

        URL urlPic = new URL("https://a0.muscache.com/pictures/miso/Hosting-13913/original/7e27055e-5eed-48ac-8c75-355336f1eaea.jpeg");
        URL urlHost = new URL("https://a0.muscache.com/im/users/54730/profile_pic/1327774386/original.jpg?aki_policy=profile_small");
        URL urlPic2 = new URL("https://a0.muscache.com/im/users/54730/profile_pic/1327774386/original.jpg?aki_policy=profile_x_medium");
        ArrayList<String> amenities = new ArrayList<>();
        amenities.add("Toys");
        amenities.add("Books");
        amenities.add("Washer");
        amenities.add("Balcony");

        AirbnbListing data = new AirbnbListing("13913","Holiday London DB Room Let-on going" ,"Finsbury Park is a friendly " +
                "melting pot community composed of Turkish, French, Spanish, Middle Eastern, Irish and English families. " +
                "<br />We have a wonderful variety of international restaurants directly under us on Stroud Green Road. " +
                "And there are many shops and large Tescos supermarket right next door. <br /><br />But you can also venture " +
                "up to Crouch End and along Greens Lanes where there will endless choice of Turkish and Middle Eastern cuisines.s"
                , urlPic, "54730","Alina", "within a few hours" , false, urlHost
                , urlPic2,3, "Islington",51.56802,-0.11121,"Private room",2
                ,"1 shared bath", 1,
                amenities, 40,1,29,365,21,50,
                5,5,5,0.16);

        URL urlPic3 = new URL("https://a0.muscache.com/pictures/miso/Hosting-13913/original/7e27055e-5eed-48ac-8c75-355336f1eaea.jpeg");
        URL urlHost1 = new URL("https://a0.muscache.com/im/users/54730/profile_pic/1327774386/original.jpg?aki_policy=profile_small");
        URL urlPic4 = new URL("https://a0.muscache.com/im/users/54730/profile_pic/1327774386/original.jpg?aki_policy=profile_x_medium");
        ArrayList<String> amenities1 = new ArrayList<>();
        amenities1.add("Wifi");
        amenities1.add("Pool");
        amenities1.add("Toys");
        amenities1.add("Books");
        amenities1.add("Washer");

        AirbnbListing data1 = new AirbnbListing("13913","Holiday London DB Room Let-on going" ,"Finsbury Park is a friendly " +
                "melting pot community composed of Turkish, French, Spanish, Middle Eastern, Irish and English families. " +
                "<br />We have a wonderful variety of international restaurants directly under us on Stroud Green Road. " +
                "And there are many shops and large Tescos supermarket right next door. <br /><br />But you can also venture " +
                "up to Crouch End and along Greens Lanes where there will endless choice of Turkish and Middle Eastern cuisines.s"
                , urlPic3, "54730","Alina", "within a few hours" , true, urlHost1
                , urlPic4,3, "Islington",51.56802,-0.11121,"Private room",2
                ,"1 shared bath", 1,
                amenities1, 40,1,29,365,21,50,
                5,5,5,0.16);

        listings = FXCollections.observableArrayList();
        listings.add(data);
        listings.add(data1);

        filteredList = FXCollections.observableArrayList();
        filteredList.add(data1);

        Assert.assertEquals(filteredList, boroughController.filterAmenity(listings, "Wifi"));

        URL urlPic5 = new URL("https://a0.muscache.com/pictures/miso/Hosting-13913/original/7e27055e-5eed-48ac-8c75-355336f1eaea.jpeg");
        URL urlHost2 = new URL("https://a0.muscache.com/im/users/54730/profile_pic/1327774386/original.jpg?aki_policy=profile_small");
        URL urlPic6 = new URL("https://a0.muscache.com/im/users/54730/profile_pic/1327774386/original.jpg?aki_policy=profile_x_medium");
        ArrayList<String> amenities2 = new ArrayList<>();
        amenities.add("Toys");
        amenities.add("Books");
        amenities.add("Washer");
        amenities.add("Balcony");

        AirbnbListing data3 = new AirbnbListing("13913","Holiday London DB Room Let-on going" ,"Finsbury Park is a friendly " +
                "melting pot community composed of Turkish, French, Spanish, Middle Eastern, Irish and English families. " +
                "<br />We have a wonderful variety of international restaurants directly under us on Stroud Green Road. " +
                "And there are many shops and large Tescos supermarket right next door. <br /><br />But you can also venture " +
                "up to Crouch End and along Greens Lanes where there will endless choice of Turkish and Middle Eastern cuisines.s"
                , urlPic, "54730","Alina", "within a few hours" , false, urlHost
                , urlPic2,3, "Islington",51.56802,-0.11121,"Private room",2
                ,"1 shared bath", 1,
                amenities, 40,1,29,365,21,50,
                5,5,5,0.16);

        URL urlPic7 = new URL("https://a0.muscache.com/pictures/miso/Hosting-13913/original/7e27055e-5eed-48ac-8c75-355336f1eaea.jpeg");
        URL urlHost4 = new URL("https://a0.muscache.com/im/users/54730/profile_pic/1327774386/original.jpg?aki_policy=profile_small");
        URL urlPic8 = new URL("https://a0.muscache.com/im/users/54730/profile_pic/1327774386/original.jpg?aki_policy=profile_x_medium");
        ArrayList<String> amenities4 = new ArrayList<>();
        amenities1.add("Wifi");
        amenities1.add("Pool");
        amenities1.add("Toys");
        amenities1.add("Books");
        amenities1.add("Washer");

        AirbnbListing data4 = new AirbnbListing("13913","Holiday London DB Room Let-on going" ,"Finsbury Park is a friendly " +
                "melting pot community composed of Turkish, French, Spanish, Middle Eastern, Irish and English families. " +
                "<br />We have a wonderful variety of international restaurants directly under us on Stroud Green Road. " +
                "And there are many shops and large Tescos supermarket right next door. <br /><br />But you can also venture " +
                "up to Crouch End and along Greens Lanes where there will endless choice of Turkish and Middle Eastern cuisines.s"
                , urlPic3, "54730","Alina", "within a few hours" , true, urlHost1
                , urlPic4,3, "Islington",51.56802,-0.11121,"Private room",2
                ,"1 shared bath", 1,
                amenities1, 40,1,29,365,21,50,
                5,5,5,0.16);

        listings = FXCollections.observableArrayList();
        listings.add(data4);
        listings.add(data3);

        filteredList = FXCollections.observableArrayList();
        filteredList.add(data4);
        Assert.assertEquals(filteredList, boroughController.filterAmenity(listings, "Pool"));
    }

    /**
     *
     */
    @Test
    public void filterSuperHost() throws MalformedURLException {

        URL urlPic = new URL("https://a0.muscache.com/pictures/miso/Hosting-13913/original/7e27055e-5eed-48ac-8c75-355336f1eaea.jpeg");
        URL urlHost = new URL("https://a0.muscache.com/im/users/54730/profile_pic/1327774386/original.jpg?aki_policy=profile_small");
        URL urlPic2 = new URL("https://a0.muscache.com/im/users/54730/profile_pic/1327774386/original.jpg?aki_policy=profile_x_medium");
        ArrayList<String> amenities = new ArrayList<String>(Collections.singleton("Oven, Shampoo, Refrigerator, " +
                "Hangers, Long term stays allowed, Hot water, Kitchen, Children\\u2019s dinnerware," +
                " Extra pillows and blankets,Stove, Room-darkening shades, Ethernet connection, Outlet covers, Toys, Washer,Patio or balcony, Essentials, Hair dryer" +
                ",Paid parking off premises, Smoke alarm, Lock on bedroom door, Carbon monoxide alarm, Heating, Dishes and silverware, TV, Dryer, " +
                "Babysitter recommendations, Iron, Bathtub, Crib,Cooking basics, Children\\u2019s books and toys, Free street parking, Dedicated workspac" +
                ", Bed linens, Luggage dropoff allowed, Coffee maker,Cable TV,Fire extinguisher, Host greets you, Pack \\u2019n Play/travel crib"));

       AirbnbListing data = new AirbnbListing("13913","Holiday London DB Room Let-on going" ,"Finsbury Park is a friendly " +
               "melting pot community composed of Turkish, French, Spanish, Middle Eastern, Irish and English families. " +
               "<br />We have a wonderful variety of international restaurants directly under us on Stroud Green Road. " +
               "And there are many shops and large Tescos supermarket right next door. <br /><br />But you can also venture " +
               "up to Crouch End and along Greens Lanes where there will endless choice of Turkish and Middle Eastern cuisines.s"
               , urlPic, "54730","Alina", "within a few hours" , false, urlHost
               , urlPic2,3, "Islington",51.56802,-0.11121,"Private room",2
               ,"1 shared bath", 1,
               amenities, 40,1,29,365,21,50,
               5,5,5,0.16);

        URL urlPic3 = new URL("https://a0.muscache.com/pictures/miso/Hosting-13913/original/7e27055e-5eed-48ac-8c75-355336f1eaea.jpeg");
        URL urlHost1 = new URL("https://a0.muscache.com/im/users/54730/profile_pic/1327774386/original.jpg?aki_policy=profile_small");
        URL urlPic4 = new URL("https://a0.muscache.com/im/users/54730/profile_pic/1327774386/original.jpg?aki_policy=profile_x_medium");
        ArrayList<String> amenities1 = new ArrayList<String>(Collections.singleton("Oven, Shampoo, Refrigerator, " +
                "Hangers, Long term stays allowed, Hot water, Kitchen, Children\\u2019s dinnerware," +
                " Extra pillows and blankets,Stove, Room-darkening shades, Ethernet connection, Outlet covers, Wifi, Washer,Patio or balcony, Essentials, Hair dryer" +
                ",Paid parking off premises, Smoke alarm, Lock on bedroom door, Carbon monoxide alarm, Heating, Dishes and silverware, TV, Dryer, " +
                "Babysitter recommendations, Iron, Bathtub, Crib,Cooking basics, Children\\u2019s books and toys, Free street parking, Dedicated workspac" +
                ", Bed linens, Luggage dropoff allowed, Coffee maker,Cable TV,Fire extinguisher, Host greets you, Pack \\u2019n Play/travel crib"));

        AirbnbListing data1 = new AirbnbListing("13913","Holiday London DB Room Let-on going" ,"Finsbury Park is a friendly " +
                "melting pot community composed of Turkish, French, Spanish, Middle Eastern, Irish and English families. " +
                "<br />We have a wonderful variety of international restaurants directly under us on Stroud Green Road. " +
                "And there are many shops and large Tescos supermarket right next door. <br /><br />But you can also venture " +
                "up to Crouch End and along Greens Lanes where there will endless choice of Turkish and Middle Eastern cuisines.s"
                , urlPic3, "54730","Alina", "within a few hours" , true, urlHost1
                , urlPic4,3, "Islington",51.56802,-0.11121,"Private room",2
                ,"1 shared bath", 1,
                amenities1, 40,1,29,365,21,50,
                5,5,5,0.16);

        listings = FXCollections.observableArrayList();
        listings.add(data);
        listings.add(data1);

        filteredList = FXCollections.observableArrayList();
        filteredList.add(data1);

        Assert.assertEquals(filteredList, boroughController.filterSuperHost(listings));
    }

    /**
     *
     */
    @Test
    public void filterPrivateRoom() throws MalformedURLException {
        URL urlPic = new URL("https://a0.muscache.com/pictures/miso/Hosting-13913/original/7e27055e-5eed-48ac-8c75-355336f1eaea.jpeg");
        URL urlHost = new URL("https://a0.muscache.com/im/users/54730/profile_pic/1327774386/original.jpg?aki_policy=profile_small");
        URL urlPic2 = new URL("https://a0.muscache.com/im/users/54730/profile_pic/1327774386/original.jpg?aki_policy=profile_x_medium");
        ArrayList<String> amenities = new ArrayList<String>(Collections.singleton("Oven, Shampoo, Refrigerator, " +
                "Hangers, Long term stays allowed, Hot water, Kitchen, Children\\u2019s dinnerware," +
                " Extra pillows and blankets,Stove, Room-darkening shades, Ethernet connection, Outlet covers, Wifi, Washer,Patio or balcony, Essentials, Hair dryer" +
                ",Paid parking off premises, Smoke alarm, Lock on bedroom door, Carbon monoxide alarm, Heating, Dishes and silverware, TV, Dryer, " +
                "Babysitter recommendations, Iron, Bathtub, Crib,Cooking basics, Children\\u2019s books and toys, Free street parking, Dedicated workspac" +
                ", Bed linens, Luggage dropoff allowed, Coffee maker,Cable TV,Fire extinguisher, Host greets you, Pack \\u2019n Play/travel crib"));

        AirbnbListing data = new AirbnbListing("13913","Holiday London DB Room Let-on going" ,"Finsbury Park is a friendly " +
                "melting pot community composed of Turkish, French, Spanish, Middle Eastern, Irish and English families. " +
                "<br />We have a wonderful variety of international restaurants directly under us on Stroud Green Road. " +
                "And there are many shops and large Tescos supermarket right next door. <br /><br />But you can also venture " +
                "up to Crouch End and along Greens Lanes where there will endless choice of Turkish and Middle Eastern cuisines.s"
                , urlPic, "54730","Alina", "within a few hours" , false, urlHost
                , urlPic2,3, "Islington",51.56802,-0.11121,"Public room",2
                ,"1 shared bath", 1,
                amenities, 40,1,29,365,21,50,
                5,5,5,0.16);


        URL urlPic3 = new URL("https://a0.muscache.com/pictures/miso/Hosting-13913/original/7e27055e-5eed-48ac-8c75-355336f1eaea.jpeg");
        URL urlHost1 = new URL("https://a0.muscache.com/im/users/54730/profile_pic/1327774386/original.jpg?aki_policy=profile_small");
        URL urlPic4 = new URL("https://a0.muscache.com/im/users/54730/profile_pic/1327774386/original.jpg?aki_policy=profile_x_medium");
        ArrayList<String> amenities1 = new ArrayList<String>(Collections.singleton("Oven, Shampoo, Refrigerator, " +
                "Hangers, Long term stays allowed, Hot water, Kitchen, Children\\u2019s dinnerware," +
                " Extra pillows and blankets,Stove, Room-darkening shades, Ethernet connection, Outlet covers, Wifi, Washer,Patio or balcony, Essentials, Hair dryer" +
                ",Paid parking off premises, Smoke alarm, Lock on bedroom door, Carbon monoxide alarm, Heating, Dishes and silverware, TV, Dryer, " +
                "Babysitter recommendations, Iron, Bathtub, Crib,Cooking basics, Children\\u2019s books and toys, Free street parking, Dedicated workspac" +
                ", Bed linens, Luggage dropoff allowed, Coffee maker,Cable TV,Fire extinguisher, Host greets you, Pack \\u2019n Play/travel crib"));

        AirbnbListing data1 = new AirbnbListing("13913","Holiday London DB Room Let-on going" ,"Finsbury Park is a friendly " +
                "melting pot community composed of Turkish, French, Spanish, Middle Eastern, Irish and English families. " +
                "<br />We have a wonderful variety of international restaurants directly under us on Stroud Green Road. " +
                "And there are many shops and large Tescos supermarket right next door. <br /><br />But you can also venture " +
                "up to Crouch End and along Greens Lanes where there will endless choice of Turkish and Middle Eastern cuisines.s"
                , urlPic3, "54730","Alina", "within a few hours" , true, urlHost1
                , urlPic4,3, "Islington",51.56802,-0.11121,"Private room",2
                ,"1 shared bath", 1,
                amenities1, 40,1,29,365,21,50,
                5,5,5,0.16);

        listings = FXCollections.observableArrayList();
        listings.add(data);
        listings.add(data1);

        filteredList = FXCollections.observableArrayList();
        filteredList.add(data1);

        Assert.assertEquals(filteredList, boroughController.filterPrivateRoom(listings));
    }

    /**
     *
     */
    @Test
    public void initialize() {
    }

    /**
     *
     */
    @Test
    public void rowClicked() {
    }

    /**
     *
     */
    @Test
    public void setMainWindowController() {
    }
}