package com.hackathonstuttgart.smartfoodapp.smartfoodapp.helper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Robin on 29.10.2017.
 */

public class ItemHelper {
    private final Map<String,String> labelToNameMap = new HashMap<>();
    private final Map<String,Integer> labelToImageMap = new HashMap<>();
    private int defaultImage = -1;

    public void putMapping(String label, String name, int imageId)
    {
        labelToNameMap.put(label, name);
        labelToImageMap.put(label, imageId);
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
