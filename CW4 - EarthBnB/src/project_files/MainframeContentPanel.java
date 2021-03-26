package project_files;

import java.util.ArrayList;

public abstract class MainframeContentPanel {

    public abstract void initializeList(ArrayList<AirbnbListing> listings, Account currentAccount);


    protected MainWindowController mainWindowController;

    protected void setMainWindowController(MainWindowController controller)
    {
        this.mainWindowController = controller;
    }
}
