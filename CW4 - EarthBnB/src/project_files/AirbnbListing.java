package project_files;

import javafx.fxml.FXML;

import java.net.URL;
import java.util.ArrayList;

/**
 * Represents one listing of a property for rental on Airbnb.
 * This is essentially one row in the data table. Each column
 * has a corresponding field.
 */ 

public class AirbnbListing {

    /**
     * The id and name of the individual property
     */
    private String id;
    private String name;

    /**
     * Overview of neighbourhood
     */
    private String neighbourhoodOverview;

    /**
     * Url of picture showing the property
     */
    private URL pictureUrl;

    /**
     * The id and name of the host for this listing.
     * Each listing has only one host, but one host may
     * list many properties.
     */
    private String hostID;
    private String hostName;

    /**
     * The time a host usually takes to respond. (Descriptions like "within a few hours")
     * Can be N/A
     */
    private String hostResponseTime;

    /**
     * Whether the host has reached the status of superhost.
     */
    private boolean hostIsSuperhost;

    private URL hostThumbnail;
    private URL hostPicture;


    /**
     * The total number of listings the host holds across AirBnB
     */
    private int hostListingsCount;

    /**
     * The grouped location to where the listed property is situated.
     * For this data set, it is a london borough.
     */
    private String neighbourhood;

    /**
     * The location on a map where the property is situated.
     */
    private double latitude;
    private double longitude;

    /**
     * The type of property, either "Private room" or "Entire Home/apt".
     */
    private String roomType;

    /**
     * The maximum number of guests this property can accommodate.
     */
    private int maxGuests;

    /**
     * A Description of the available bathrooms.
     * Strings have some differences in spelling and can be null.
     */
    private String bathroomsText;

    /**
     * The amount of available beds.
     */
    private int bedrooms;

    /**
     * A list of the amenities the host provides
     */
    private ArrayList<String> amenities;

    /**
     * The price per night's stay
     */
    private int price;

    /**
     * The minimum and maximum number of nights the listed property must and can be booked for.
     */
    private int minimumNights;
    private int maximumNights;

    /**
     * The total number of days in the year that the property is available for
     */
    private int availability365;

    /**
     * Stats related to the rating of a property
     */
    private int numberOfReviews;
    private int reviewScoresRating;
    private int reviewScoresCleanliness;
    private int reviewScoresCommunication;
    private int reviewScoresLocation;
    private double reviewsPerMonth;


    public AirbnbListing(String id, String name, String neighbourhoodOverview, URL pictureUrl, String hostID,
                         String hostName, String hostResponseTime, boolean hostIsSuperhost, URL hostThumbnail, URL hostPicture,
                         int hostListingsCount, String neighbourhood, double latitude, double longitude, String roomType,
                         int maxGuests, String bathroomsText, int bedrooms, ArrayList<String> amenities, int price,
                         int minimumNights, int maximumNights, int availability365, int numberOfReviews,
                         int reviewScoresRating, int reviewScoresCleanliness, int reviewScoresCommunication,
                         int reviewScoresLocation, double reviewsPerMonth) {
        this.id = id;
        this.name = name;
        this.neighbourhoodOverview = neighbourhoodOverview;
        this.pictureUrl = pictureUrl;
        this.hostID = hostID;
        this.hostName = hostName;
        this.hostResponseTime = hostResponseTime;
        this.hostIsSuperhost = hostIsSuperhost;
        this.hostThumbnail = hostThumbnail;
        this.hostPicture = hostPicture;
        this.hostListingsCount = hostListingsCount;
        this.neighbourhood = neighbourhood;
        this.latitude = latitude;
        this.longitude = longitude;
        this.roomType = roomType;
        this.maxGuests = maxGuests;
        this.bathroomsText = bathroomsText;
        this.bedrooms = bedrooms;
        this.amenities = amenities;
        this.price = price;
        this.minimumNights = minimumNights;
        this.maximumNights = maximumNights;
        this.availability365 = availability365;
        this.numberOfReviews = numberOfReviews;
        this.reviewScoresRating = reviewScoresRating;
        this.reviewScoresCleanliness = reviewScoresCleanliness;
        this.reviewScoresCommunication = reviewScoresCommunication;
        this.reviewScoresLocation = reviewScoresLocation;
        this.reviewsPerMonth = reviewsPerMonth;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNeighbourhoodOverview() {
        return neighbourhoodOverview;
    }

    public URL getPictureUrl() {
        return pictureUrl;
    }

    public String getHostID() {
        return hostID;
    }

    public String getHostName() {
        return hostName;
    }

    public String getHostResponseTime() {
        return hostResponseTime;
    }

    public boolean isHostIsSuperhost() {
        return hostIsSuperhost;
    }

    public URL getHostThumbnail() {
        return hostThumbnail;
    }

    public URL getHostPicture() {
        return hostPicture;
    }

    public int getHostListingsCount() {
        return hostListingsCount;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void chopNeighbourhoodName() {
        String tempString[] = neighbourhood.split(" ", 2);
        neighbourhood = tempString[0];
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public String getBathroomsText() {
        return bathroomsText;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public ArrayList<String> getAmenities() {
        return amenities;
    }

    public int getPrice() {
        return price;
    }

    public int getMinimumNights() {
        return minimumNights;
    }

    public int getMaximumNights() {
        return maximumNights;
    }

    public int getAvailability365() {
        return availability365;
    }

    public int getNumberOfReviews() {
        return numberOfReviews;
    }

    public int getReviewScoresRating() {
        return reviewScoresRating;
    }

    public int getReviewScoresCleanliness() {
        return reviewScoresCleanliness;
    }

    public int getReviewScoresCommunication() {
        return reviewScoresCommunication;
    }

    public int getReviewScoresLocation() {
        return reviewScoresLocation;
    }

    public double getReviewsPerMonth() {
        return reviewsPerMonth;
    }

    /**
     * The average price of a listing is calculated by the price per night * the minimum nights
     * @return The average price
     */
    public int getAveragePrice() { return price * minimumNights; }

    /*
    @Override
    public String toString() {
        return "AirbnbListing{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", host_id='" + host_id + '\'' +
                ", host_name='" + host_name + '\'' +
                ", neighbourhood='" + neighbourhood + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", room_type='" + room_type + '\'' +
                ", price=" + price +
                ", minimumNights=" + minimumNights +
                ", numberOfReviews=" + numberOfReviews +
                ", lastReview='" + lastReview + '\'' +
                ", reviewsPerMonth=" + reviewsPerMonth +
                ", calculatedHostListingsCount=" + calculatedHostListingsCount +
                ", availability365=" + availability365 +
                '}';
    }

     */
}


