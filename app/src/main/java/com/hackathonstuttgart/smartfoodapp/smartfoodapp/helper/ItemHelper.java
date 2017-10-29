package com.hackathonstuttgart.smartfoodapp.smartfoodapp.helper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Robin on 29.10.2017.
 */

public class ItemHelper {
    private final Map<String,String> labelToNameMap = new HashMap<>();

    public void putMapping(String label, String name)
    {
        labelToNameMap.put(label, name);
    }

    public String getMapping(String label)
    {
        if(labelToNameMap.containsKey(label))
            return labelToNameMap.get(label);
        return label;
    }
}
