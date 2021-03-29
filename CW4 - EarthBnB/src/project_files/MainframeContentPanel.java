package project_files;

import javafx.scene.Parent;

import java.util.ArrayList;

public abstract class MainframeContentPanel {

    public abstract void initializeList(ArrayList<AirbnbListing> listings, Account currentAccount);


    protected MainWindowController mainWindowController;
    protected Parent panelRoot;
    protected ArrayList<AirbnbListing> listings;
    protected Account currentUser;
    protected String name;

    /**
     * Receive the root of the panel.
     * @return
     */
    public Parent getPanelRoot(){
        return panelRoot;
    }

    /**
     * @param controller The main window controller. Used to communicate back to the mainframe.
     * @param currentUser
     * @param panelRoot The root of a panel. This is the top node of the view which can be received to be displayed in the mainframe.
     * @param listings
     */
    public void initialize(MainWindowController controller, Account currentUser, Parent panelRoot, ArrayList<AirbnbListing> listings) {
        this.mainWindowController = controller;
        this.currentUser = currentUser;
        this.panelRoot = panelRoot;
        this. listings = listings;
    }

    public String getName()
    {
        return name;
    }

    public void setCurrentUser(Account currentUser)
    {
        this.currentUser = currentUser;
    }

}
