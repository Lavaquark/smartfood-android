package com.hackathonstuttgart.smartfoodapp.smartfoodapp.data;

/**
 * Created by Robin on 29.10.2017.
 */

public class Item {
    private final String label;
    private final String displayName;

    public Item(String label, String displayName)
    {
        this.label = label;
        this.displayName = displayName;
    }

    public String getLabel()
    {
        return label;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
