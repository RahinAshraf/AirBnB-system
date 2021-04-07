package project_files;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * This is the controller class for the connectionSelectorView. It is the first window that opens up when the program
 * is launched. The user can select whether to use the database connected to the program or not. This selection is
 * later stored as a boolean value.
 *
 * @author  Valentin Magis, Rahin Ashraf, Vandad Vafai Tabrizi, Barnabas Szalai
 * @version 1.0
 * @since   2021-03-11
 */
public class ConnectionSelector extends Application {

    @FXML
    private Button dbSelectButton, offlineSelectButton;

    /**
     * Start the program with displaying the connectionSelector.
     * The user here is able to choose whether to use the program with or without the online database.
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("connectionSelectorView.fxml"));
        stage.setTitle("EarthBnB");
        stage.setScene(new Scene(root, 600, 300));
        stage.setResizable(true);
        stage.show();
    }


    /**
     * This method is called when the user selects one of the two options. It closes the current window and
     * instantiates and shows a new stage with the MainFrameView.
     */
    @FXML
    private void selectDBConnection(ActionEvent e) throws IOException {
        boolean usingDatabase = false;
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("MainFrameView.fxml"));
        Parent root = mainLoader.load();
        Stage newStage = new Stage();
        newStage.setTitle("EarthBnB");
        newStage.setScene(new Scene(root, 700, 550));
        newStage.setResizable(true);
        newStage.show();
        if(((Button)e.getSource()).getId().equals("dbSelectButton")) {
            usingDatabase = true;
        }
        MainFrameController mainFrameController = mainLoader.getController();
        mainFrameController.setUsingDatabase(usingDatabase);
        dbSelectButton.getScene().getWindow().hide();
    }

    /**
     * Launch the program.
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }


    // Code we used for populating the database with random accounts and bookings.
        /*

    // Code for populating database
    private void generateUsers() {
        ArrayList<String> names = loadNames();
        try {
            DatabaseConnection connection = new DatabaseConnection();
            Connection connectDB = connection.getConnection();
            Statement statement = connectDB.createStatement();

            for (int i=1; i < 300; i++)
            {
                String insert1 = "INSERT INTO account VALUES (NULL," + "'" + names.get(i) + "' , '" + givenUsingJava8_whenGeneratingRandomAlphanumericString_thenCorrect() + "', '" +
                        names.get(i).replaceAll("\\s", "" + "") + "@gmail.com', '" + generateRandomDate() + "')" + ";";
                statement.executeUpdate(insert1);
            }
        } catch (Exception e) {
            System.out.println("failed");
            e.printStackTrace();
        }
    }
    //https://www.baeldung.com/java-random-string
    private String givenUsingJava8_whenGeneratingRandomAlphanumericString_thenCorrect() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 6;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }

    private ArrayList<String> loadNames() {
        ArrayList<String> names = new ArrayList<>();
        try{
            URL url = getClass().getResource("names.csv"); // Has been removed since unnecessary. To use, add list with 1000 names.
            CSVReader reader = new CSVReader(new FileReader(new File(url.toURI()).getAbsolutePath()));
            String [] line;
            reader.readNext();
            while ((line = reader.readNext()) != null) {
                String name = line[0];
                names.add(name);
            }
        } catch(IOException | URISyntaxException e){
            System.out.println("Failure! Something went wrong");
            e.printStackTrace();
        }
        return names;
    }

    // https://stackoverflow.com/questions/34051291/generate-a-random-localdate-with-java-time
    public Date generateRandomDate() {
        long minDay = LocalDate.of(2018, 1, 1).toEpochDay();
        long maxDay = LocalDate.of(2021, 4, 30).toEpochDay();
        long randomDay = ThreadLocalRandom.current().nextLong(minDay, maxDay);
        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
        return java.sql.Date.valueOf(randomDate);
    }



*/

    /*
    private void generateBookings() {
        ArrayList<AirbnbListing> listing = listings.getFilteredListings();
        ArrayList<String> users = getUsers();

        try {
            DatabaseConnection connection = new DatabaseConnection();
            Connection connectDB = connection.getConnection();
            Statement statement = connectDB.createStatement();
            LocalDate currentDate = LocalDate.now().plusDays(365);
            Random rand = new Random();
            for (int i = 0; i < 1000; i++)
            {
                AirbnbListing l = listing.get(i);
                int daysOfStay = rand.nextInt(l.getMaximumNights() % 30 + 1);


                String insert = "INSERT INTO booking VALUES (NULL, '" + currentDate.minusDays(i+daysOfStay) + "', '" + currentDate.minusDays(i) + "', '" + users.get(rand.nextInt(users.size()-1)) + "', '" +
                        l.getMaxGuests() + "', '" + l.getPrice() * daysOfStay + "', '" + l.getId() + "','" +  currentDate.minusDays(i+ + daysOfStay + rand.nextInt(45)) + "');";
                System.out.println(insert);
                statement.executeUpdate(insert);
            }
        } catch (Exception e) {
        }
    }



    private ArrayList<String> getUsers() {
        ArrayList<String> userIds = new ArrayList<>();
        try {
            DatabaseConnection connection = new DatabaseConnection();
            Connection connectDB = connection.getConnection();
            Statement statement = connectDB.createStatement();

            String checkSignup = "SELECT accountID FROM account";

            ResultSet queryResult = statement.executeQuery(checkSignup);
            while (queryResult.next()) {
                userIds.add(queryResult.getString(1)); // Unsafe operation?
            }
        } catch (Exception e) {
        }
        return userIds;
    }

     */
}
