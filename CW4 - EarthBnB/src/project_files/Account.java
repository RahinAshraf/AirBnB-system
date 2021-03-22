package project_files;

import java.util.ArrayList;

public class Account {

    private int accountID;
    private String username;
    private String password;
    private String emailAddress;
    private ArrayList<Integer> savedProperties;

    Account(int accountID, String username, String password, String emailAddress) {
        this.accountID = accountID;
        this.username = username;
        this.emailAddress = emailAddress;
    }


    public int getAccountID() {
        return accountID;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getEmailAddress() {
        return emailAddress;
    }
}
