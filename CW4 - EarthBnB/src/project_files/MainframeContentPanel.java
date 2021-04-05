package project_files;

import javafx.scene.Parent;

/**
 *
 * @author Valentin Magis, Barnabas Szalai
 * @version 1.0
 * @since 2021-03-11
 */
public abstract class MainframeContentPanel {

    /**
     * Pass on the loaded listings and the current user to this contentPanel.
     * Every contentPanel needs either both or one of them to run correctly.
     * @param listings The listings.
     * @param currentAccount The currently logged in user.
     */
    public abstract void initializeData(Listings listings, Account currentAccount);

    /**
     * Call this to update the contents of the current panel. Used when the filters applied on the listings are changed.
     */
    public abstract void updatePanel();

    protected MainFrameController mainFrameController; // Used to communicate back to the main frame.
    protected Parent panelRoot; // The root of this panel. Put into the center of the mainframe to display this panel.
    protected Listings listings;
    protected Account currentUser;
    protected String name; // The name of the panel.

    /**
     * Receive the root of the panel.
     * @return The root of the panel.
     */
    public Parent getPanelRoot(){
        return panelRoot;
    }

    /**
     * @param controller The main window controller. Used to communicate back to the mainframe.
     * @param currentUser The user currently logged in.
     * @param panelRoot The root of a panel. This is the top node of the view which can be received to be displayed in the mainframe.
     * @param listings The listings.
     */
    public void initialize(MainFrameController controller, Account currentUser, Parent panelRoot, Listings listings) {
        this.mainFrameController = controller;
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

}
