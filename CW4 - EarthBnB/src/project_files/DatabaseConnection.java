package project_files;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


/**
 * This class provides a model that connects to the database. This class can be instantiated every time a
 * database query has to be executed.
 *
 * @author  Valentin Magis, Rahin Ashraf, Vandad Vafai Tabrizi, Barnabas Szalai
 * @version 1.0
 * @since   2021-03-11
 */
public class DatabaseConnection {

    String URL = "jdbc:mysql://35.189.117.22:3306/booking_system"; // Google Cloud's MySQL Server
    String USER_NAME = "root";
    String PASSWORD = "root";
    Connection conn;


    /**
     * This method calls the DriverManager class which manages the jdbc drivers to establish
     * a connection with the database using the URL, username and the password. It uses SQLException in case the
     * database cannot be accessed.
     * @return  Connection  returns the Connection object if the connection was successful
     */
    public Connection getConnection() {

        try {
            conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            System.out.println("Successful connection");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Database connection error!");
        }
        return conn;
    }

}
