package com.hackathonstuttgart.smartfoodapp.smartfoodapp;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackathonstuttgart.smartfoodapp.smartfoodapp.data.Item;
import com.hackathonstuttgart.smartfoodapp.smartfoodapp.helper.ItemHelper;

import java.util.Date;

public class ItemDetailActivity extends AppCompatActivity {

    private Item item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        item = SmartFood.getInstance().clickedItem;

        TextView titleTextView = (TextView) findViewById(R.id.item_detail_header);
        TextView addedTextView = (TextView) findViewById(R.id.item_detail_added);
        TextView expirationTextView = (TextView) findViewById(R.id.item_detail_expiration);
        ImageView imageDetailView = (ImageView) findViewById(R.id.item_detail_image);

        String label = item.getLabel();

        int imgRes = ItemHelper.getInstance().getImageId(label);
        imageDetailView.setImageResource(imgRes);

        titleTextView.setText(item.getDisplayName());
        addedTextView.setText(item.getDateString());
        long expirationDays = item.getExpirationDays();

        String expireString;
        if(expirationDays == Integer.MAX_VALUE)
            expireString = "never";
        else if (expirationDays == Integer.MIN_VALUE)
            expireString = "unknown";
        else if (expirationDays == 0)
            expireString = "today";
        else if (expirationDays == 1)
            expireString = "tomorrow";
        else if (expirationDays == -1)
            expireString = "yesterday";
        else if (expirationDays < 0)
            expireString = (-expirationDays) + " days ago";
        else
            expireString = "in " + expirationDays + " days";
        expirationTextView.setText(expireString);
    }
    @Override
    protected void onStart() {
        super.onStart();
    }
}