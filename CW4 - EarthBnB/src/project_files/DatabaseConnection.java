package project_files;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    String URL = "jdbc:mysql://SG-Barnabas-4055-mysql-master.servers.mongodirector.com:3306/cw4";
    String USER_NAME = "sgroot";
    String PASSWORD = "EV.DNyM5kI2ff7OB";
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
