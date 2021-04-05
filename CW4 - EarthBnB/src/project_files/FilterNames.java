package project_files;

/**
 * Enum FilterNames - Makes the connection of the filters "pool", "private room", "superhost" and "Wifi" typo-safe.
 * Used in the MainFrameController and the BoroughPropertiesViewer to specify the active filters filtered for in the Listings.
 */
public enum FilterNames {
    POOL_FILTER("Pool"), ROOM_FILTER("Private Room"), SUPER_FILTER("Superhost"), WIFI_FILTER("Wi-fi");

    private String displayName; // The name to be displayed to the user.

    FilterNames(String displayName)
    {
        this.displayName = displayName;
    }

    /**
     * Use to get the display value.
     * @return The display value of a constant.
     */
    @Override
    public String toString() {
        return displayName;
    }

    /**
     * Get a constant by its name.
     * @param name The name of the requested constant.
     * @return The constant.
     */
    public static FilterNames getFilter(String name)
    {
        for (FilterNames filter : FilterNames.values())
        {
            if (filter.name().equals(name))
                return filter;
        }
        return null;
    }
}
