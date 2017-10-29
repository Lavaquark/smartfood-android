package com.hackathonstuttgart.smartfoodapp.smartfoodapp.data;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Created by Robin on 29.10.2017.
 */

public class Item {
    private final String label;
    private final String displayName;
    private final Date dateAdded;
    private final Date expirationDate;

    public Item(String label, String displayName, Date dateAdded, Date expirationDate)
    {
        this.label = label;
        this.displayName = displayName;
        this.dateAdded = dateAdded;
        this.expirationDate = expirationDate;
    }

    public String getLabel()
    {
        return label;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public String getDateString()
    {
        SimpleDateFormat formatter = new SimpleDateFormat("YYYY/MM/dd");
        return formatter.format(dateAdded);
    }

    public long getExpirationDays()
    {
        return (expirationDate.getTime() - dateAdded.getTime()) / (1000 * 3600 * 24);
    }

    @Override
    public String toString() {
        return displayName;
    }
}
