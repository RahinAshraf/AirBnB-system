package project_files;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    String URL = "jdbc:mysql://sql4.freemysqlhosting.net/sql4400152";
    String USER_NAME = "sql4400152";
    String PASSWORD = "iCm5GV3bnv";
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
