package project_files;

public enum FilterNames {
    POOL_FILTER("Pool"), ROOM_FILTER("Room"), SUPER_FILTER("Superhost"), WIFI_FILTER("Wi-fi");

    private String displayName;

    FilterNames(String displayName)
    {
        this.displayName = displayName;
    }

    /**
     * Use to get the display value.
     * @return
     */
    @Override
    public String toString() {
        return displayName;
    }

    public static FilterNames getValue(String value)
    {
        for (FilterNames filter : FilterNames.values())
        {
            if (filter.toString().equals(value))
                return filter;
        }
        return null;
    }

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
