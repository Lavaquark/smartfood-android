package com.hackathonstuttgart.smartfoodapp.smartfoodapp;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackathonstuttgart.smartfoodapp.smartfoodapp.data.Item;
import com.hackathonstuttgart.smartfoodapp.smartfoodapp.helper.ItemHelper;

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

        titleTextView.setText(item.getDisplayName());
        addedTextView.setText(item.getDateString());
        expirationTextView.setText(item.getExpirationDays() + " days");
        imageDetailView.setImageResource(imgRes);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
