package project_files;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

import com.opencsv.CSVReader;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;


public class AirbnbDataLoader {

    /** 
     * Return an ArrayList containing the rows in the AirBnB London data set csv file.
     */
    public ArrayList<AirbnbListing> load(String filename) {
        System.out.print("Begin loading Airbnb london dataset...");
        ArrayList<AirbnbListing> listings = new ArrayList<>();
        try{
            URL url = getClass().getResource(filename);
            CSVReader reader = new CSVReader(new FileReader(new File(url.toURI()).getAbsolutePath()));
            String [] line;
            //skip the first row (column headers)
            reader.readNext();
            while ((line = reader.readNext()) != null) {
                String id = line[0];
                String name = line[1];
                String neighbourhoodOverview = convertHTMLSymbols(line[2]);
                URL pictureURL = convertURL(line[3]);
                String hostID = line[4];
                String hostName = line[5];
                String hostResponseTime = line[6];
                boolean hostIsSuperhost = convertBoolean(line[7]);
                URL hostThumbnail = convertURL(line[8]);
                URL hostPicture = convertURL(line[9]);
                int hostListingsCount = convertInt(line[10]);
                String neighbourhood = line[11];
                double latitude = convertDouble(line[12]);
                double longitude = convertDouble(line[13]);
                String roomType = line[14];
                int maxGuests = convertInt(line[15]);
                String bathroomsText = line[16];
                int bedrooms = convertInt(line[17]);
                ArrayList<String> amenities = convertStringArrayList(line[18]);
                int price = convertInt(line[19]);
                int minimumNights = convertInt(line[20]);
                int maximumNights = convertInt(line[21]);
                int availability365 = convertInt(line[22]);
                int numberOfReviews = convertInt(line[23]);
                int reviewScoresRating = convertInt(line[24]);
                int reviewScoresCleanliness = convertInt(line[25]);
                int reviewScoresCommunication = convertInt(line[26]);
                int reviewScoresLocation = convertInt(line[27]);
                double reviewsPerMonth = convertDouble(line[28]);

                AirbnbListing listing = new AirbnbListing(id, name, neighbourhoodOverview, pictureURL, hostID, hostName,
                        hostResponseTime, hostIsSuperhost, hostThumbnail, hostPicture, hostListingsCount, neighbourhood,
                        latitude, longitude, roomType, maxGuests, bathroomsText, bedrooms, amenities, price, minimumNights,
                        maximumNights, availability365, numberOfReviews, reviewScoresRating, reviewScoresCleanliness,
                        reviewScoresCommunication, reviewScoresLocation, reviewsPerMonth
                    );
                listings.add(listing);
            }
        } catch(IOException | URISyntaxException e){
            System.out.println("Failure! Something went wrong");
            e.printStackTrace();
        }
        System.out.println("Success! Number of loaded records: " + listings.size());
        return listings;
    }

    /**
     * Remove html symbols contained in the string. A break is converted to a new line.
     * Italic, bold and underlining are removed.
     * @param s The string to be converted.
     * @return The converted string.
     */
    private String convertHTMLSymbols(String s)
    {
        s = s.replace("<br />", "\n");
        s = s.replace("<u>", "");
        s = s.replace("</u>", "");
        s = s.replace("<i>", "");
        s = s.replace("</i>", "");
        s = s.replace("<em>", "");
        s = s.replace("</em>", "");
        s = s.replace("<b>", "");
        s = s.replace("</b>", "");
        return s;
    }

    /**
     *
     * @param doubleString the string to be converted to Double type
     * @return the Double value of the string, or -1.0 if the string is 
     * either empty or just whitespace
     */
    private Double convertDouble(String doubleString){
        if(doubleString != null && !doubleString.trim().equals("")){
            return Double.parseDouble(doubleString);
        }
        return 0.0;
    }

    /**
     * Converts a string separated by commas into an ArrayList. Existing double quotes and whitespaces
     * in between the strings are deleted before the process.
     * @param arrayListString The string to be converted
     * @return The ArrayList of Strings.
     */
    private ArrayList<String> convertStringArrayList(String arrayListString)
    {
        arrayListString = arrayListString.replace("\"", ""); // Remove the double quotes
        String[] elements = arrayListString.split("\\s*" + "," + "\\s*"); // Remove spaces and split into Array
        List<String> listFixedLength = Arrays.asList(elements); // Convert into list
        return new ArrayList<String>(listFixedLength); // Convert into ArrayList and return
    }

    /**
     *
     * @param intString the string to be converted to Integer type
     * @return the Integer value of the string, or -1 if the string is 
     * either empty or just whitespace
     */
    private Integer convertInt(String intString){
        if(intString != null && !intString.trim().equals("")){
            return Integer.parseInt(intString);
        }
        return 0;
    }

    /**
     * Convert string to a boolean. The string value for true in the set of data being used is "t"
     * @param bool A string. Returns true if the string is "t"
     * @return The truth value of the field
     */
    private boolean convertBoolean(String bool)
    {
        if (bool != null && !bool.trim().equals(""))
            return bool.equals("t");
        else
            return false;
    }

    /**
     * @param urlString The string to be converted into an url
     * @return The url or null if the link is invalid.
     * @throws MalformedURLException
     */
    private URL convertURL(String urlString) throws MalformedURLException {
        if (urlString != null && !urlString.trim().equals("")) {
            try {
                URI uri = new URI(urlString);
                URL url = uri.toURL();
                return url;
            } catch (Exception e) {
                System.out.println("URL could not be loaded");
                e.printStackTrace();
            }
        }
        return null;
    }

}
