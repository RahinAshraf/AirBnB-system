package project_files;

import javafx.util.Pair;

import java.util.*;

/**
 * Class StatClosestListingToAttraction retrieves the closest apartments to given points. Creates a printable list.
 */
public class StatClosestListingToAttraction extends Statistic {

    HashMap<String, double[]> locationsList = new HashMap<>();
    /**
     * Create an object for the statistic counting the number of entire home and apartments listed.
     */
    public StatClosestListingToAttraction(ArrayList<AirbnbListing> listings)
    {
        name = "Closest to following Attractions:";

        locationsList.put("Julian Markham", new double[] {51.49220743976158, -0.09855447430590904});
        locationsList.put("Angel Lane", new double[] {51.543337686258475, 0.00015711332398934524});
        locationsList.put("Janet's Bar", new double[] {51.49370290200051, -0.17536185674457905});
        locationsList.put("Octoberfest Pub", new double[] {51.475224901745115, -0.20636514421978416});
        updateStatistic(listings);
    }

    /**
     * Update the statistic.
     * @param listings A list of boroughListings the statistic should be calculated for.
     * @return
     */
    protected String updateStatistic(ArrayList<AirbnbListing> listings)
    {
        String result = "";
        for (Map.Entry<String, double[]> location : locationsList.entrySet())
        {
            Pair<AirbnbListing, Integer> currentResult = getClosestTo(listings, location.getValue()[0], location.getValue()[1]); //Pass in the boroughListings and the two location values stored in the array
            result += location.getKey() + ": " + "\n" + currentResult.getKey().getName() + " " + currentResult.getValue() + "m\n"; // Get the name of the original location, the nearest apartment and the distance in meters
        }
        return result;
    }

    /**
     * Get the listing closest to the given location.
     * @param listings The list of boroughListings to perform the comparison on.
     * @param latOrigin The latitude of the origin (e.g. the attraction)
     * @param lonOrigin The longitude of the origin
     * @return A pair of the closest listing and the distance between the two boroughListings.
     */
    private Pair<AirbnbListing, Integer> getClosestTo(ArrayList<AirbnbListing> listings, double latOrigin, double lonOrigin)
    {
        AirbnbListing currentMin = listings.get(0);
        double currentMinDistance = distance(latOrigin, lonOrigin, currentMin.getLatitude(), currentMin.getLongitude());
        for (AirbnbListing l : listings)
        {
            double newDistance = distance(latOrigin, lonOrigin, l.getLatitude(), l.getLongitude());
            if (newDistance < currentMinDistance)
            {
                currentMin = l;
                currentMinDistance = newDistance;
            }
        }
        return new Pair<>(currentMin, (int) currentMinDistance);
    }


    /**
     * Calculate the distance between two latitude/longitude points
     * Method edited from https://stackoverflow.com/questions/389211/geospatial-coordinates-and-distance-in-kilometers
     * @param lat1 The latitude of the first point
     * @param lon1 The longitude of the first point
     * @param lat2 The latitude of the second point
     * @param lon2 The longitude of the second point
     * @return The distance between the two points.
     */
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.852 * 1000; // * 60 (convert degree to nautical miles) * 1.852 (convert to nm km) * 1000 (convert km to meters)
        return (dist);
    }


    /**
     * Convert degree to radians
     * @param deg
     * @return
     */
    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    /**
     * Convert radians to degree
     * @param rad
     * @return
     */
    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}