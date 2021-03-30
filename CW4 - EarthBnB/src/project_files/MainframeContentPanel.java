package project_files;

import javafx.scene.Parent;

public abstract class MainframeContentPanel {

    public abstract void initializeList(Listings listings, Account currentAccount);
    public abstract void updatePanel();

    protected MainWindowController mainWindowController;
    protected Parent panelRoot;
    protected Listings listings;
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
    public void initialize(MainWindowController controller, Account currentUser, Parent panelRoot, Listings listings) {
        this.mainWindowController = controller;
        this.currentUser = currentUser;
        this.panelRoot = panelRoot;
        this.listings = listings;
    }

    public String getName()
    {
        return name;
    }

    public void setCurrentUser(Account currentUser)
    {
        this.currentUser = currentUser;
    }

    public void updateListings(Listings listings) {
        this.listings = listings;
    }
}
