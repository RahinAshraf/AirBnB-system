package project_files;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    String URL = "jdbc:mysql://35.189.117.22:3306/booking_system";
    String USER_NAME = "root";
    String PASSWORD = "root";
    Connection conn;

    public Connection getConnection() {

        try {
            conn = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            Statement myStm = conn.createStatement();
            System.out.println("Successful connection");
            //String sql = "select * from account";
            //ResultSet rs = myStm.executeQuery();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Database connection error!");
        }
        return conn;
    }

}
