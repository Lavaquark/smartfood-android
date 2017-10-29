package com.hackathonstuttgart.smartfoodapp.smartfoodapp;

import android.app.Application;

import com.hackathonstuttgart.smartfoodapp.smartfoodapp.data.Item;
import com.hackathonstuttgart.smartfoodapp.smartfoodapp.helper.ItemHelper;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Robin on 29.10.2017.
 */

public class SmartFood {
    private static SmartFood instance = null;
    public Item clickedItem;

    public static SmartFood getInstance()
    {
        if(instance == null)
            instance = new SmartFood();
        return instance;
    }

    public static List<Item> getTestData()
    {
        Date now = new Date();

        LinkedList<Item> testItems = new LinkedList<>();

        Item item1 = new Item("apple", "apple", now, now);
        Item item2 = new Item("orange", "orange", now, now);
        Item item3 = new Item("milk", "milk", now, now);
        Item item4 = new Item("banana", "banana", now, now);

        testItems.add(item1);
        testItems.add(item2);
        testItems.add(item3);
        testItems.add(item4);

        return testItems;
    }
}
