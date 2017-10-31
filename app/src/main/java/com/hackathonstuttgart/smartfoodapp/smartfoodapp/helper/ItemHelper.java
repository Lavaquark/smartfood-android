package com.hackathonstuttgart.smartfoodapp.smartfoodapp.helper;

import com.hackathonstuttgart.smartfoodapp.smartfoodapp.data.Item;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Robin on 29.10.2017.
 */

public class ItemHelper {
    private final Map<String,String> labelToNameMap = new HashMap<>();
    private final Map<String,Integer> labelToImageMap = new HashMap<>();
    private final Map<String,Integer> labelToExpirationMap = new HashMap<>();
    private int defaultImage = -1;

    private static ItemHelper instance = null;

    private ItemHelper()
    {

    }

    public static ItemHelper getInstance()
    {
        if(instance == null)
            instance = new ItemHelper();
        return instance;
    }

    public void putMapping(String label, String name, int imageId)
    {
        labelToNameMap.put(label, name);
        labelToImageMap.put(label, imageId);
    }

    public void setExpiration(String label, int days)
    {
        labelToExpirationMap.put(label, days);
    }

    public int getExpiration(String label)
    {
        if(labelToExpirationMap.containsKey(label))
            return labelToExpirationMap.get(label);
        return -1;
    }

    public static Date getExpirationDate(Date date, int expirationDays)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, expirationDays);
        return cal.getTime();
    }

    public String getName(String label)
    {
        if(labelToNameMap.containsKey(label))
            return labelToNameMap.get(label);
        return label;
    }

    public int getImageId(String label)
    {
        if(labelToImageMap.containsKey(label))
            return labelToImageMap.get(label);
        return defaultImage;
    }

    public void setDefaultImage(int id)
    {
        defaultImage = id;
    }
}
