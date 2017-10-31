package com.hackathonstuttgart.smartfoodapp.smartfoodapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hackathonstuttgart.smartfoodapp.smartfoodapp.data.Item;
import com.hackathonstuttgart.smartfoodapp.smartfoodapp.helper.ItemHelper;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_CANCEL = "CANCELLED";
    private static final String TAG_CHANGE_DATA = "CHANGED";
    private static final String TAG_LOGIN_FAIL = "LOGIN FAILED";
    private static final String TAG_LOGIN_SUCCESS = "LOGGED IN";

    private static final String LABEL_APPLE = "apple";
    private static final String LABEL_BANANA = "banana";
    private static final String LABEL_ORANGE = "orange";
    private static final String LABEL_MILK = "milk";
    private static final String LABEL_COKE = "cola";


    private FirebaseAuth auth;
    private final ItemHelper itemHelper = ItemHelper.getInstance();
    private GridView itemGrid;

    public MainActivity()
    {
        itemHelper.setDefaultImage(R.drawable.milk);
        itemHelper.putMapping(LABEL_APPLE, LABEL_APPLE, R.drawable.apple);
        itemHelper.putMapping(LABEL_ORANGE, LABEL_ORANGE, R.drawable.orange);
        itemHelper.putMapping(LABEL_BANANA, LABEL_BANANA, R.drawable.banana);
        itemHelper.putMapping(LABEL_MILK, LABEL_MILK, R.drawable.milk);

        itemHelper.setExpiration(LABEL_MILK, 14);
        itemHelper.setExpiration(LABEL_APPLE, 9);
        itemHelper.setExpiration(LABEL_ORANGE, 7);
        itemHelper.setExpiration(LABEL_BANANA, 5);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        itemGrid = (GridView) findViewById(R.id.itemGrid);
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            loginFirebase();
            setupFirebaseDatabase();
        }
        catch (Exception ex)
        {
            Toast.makeText(MainActivity.this, "An error occurred while retrieving the data from the database: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        loadTestItems();
    }

    public void loadTestItems()
    {
        Date now = new Date();

        LinkedList<Item> items = new LinkedList<Item>();
        items.add(new Item("apple", "apple", now, ItemHelper.getExpirationDate(now, 2)));
        items.add(new Item("apple", "apple", now, ItemHelper.getExpirationDate(now, -1)));
        items.add(new Item("banana", "banana", now, ItemHelper.getExpirationDate(now, 0)));
        items.add(new Item("orange", "orange", now, ItemHelper.getExpirationDate(now, 1)));
        items.add(new Item("orange", "orange", now, ItemHelper.getExpirationDate(now, -2)));
        updateView(items);
    }

    public void findRecipes(View view) {

    }

    private void updateView(List<Item> itemList)
    {
        itemGrid.setAdapter(new ImageAdapter(this, itemList));
    }

    private void loginFirebase()
    {
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        auth.signInWithEmailAndPassword("tom.dockle@hackathon-stuttgart.de", "tomdockle")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG_LOGIN_SUCCESS, "signInWithEmail:success");
                            FirebaseUser user = auth.getCurrentUser();
                            //Todo: do something with the user
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG_LOGIN_FAIL, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void showItemDetailActivity(Item item)
    {
        SmartFood.getInstance().clickedItem = item;
        Intent intent = new Intent(this, ItemDetailActivity.class);
        startActivity(intent);
    }

    private void setupFirebaseDatabase() throws FileNotFoundException {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        // Create a storage reference from our app
        DatabaseReference dbRef = db.getReference();
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                LinkedList<Item> itemList = new LinkedList<>();
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for(DataSnapshot child : dataSnapshot.child("inventory").child("items").getChildren())
                {
                    String dateString = child.child("date_added").getValue(Long.class).toString();
                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddkkmmss");
                    Date date = new Date();
                    try {
                        date = format.parse(dateString);
                    } catch (ParseException e) {

                    }
                    String itemLabel = child.getKey();
                    String name = itemHelper.getName(itemLabel);
                    int expirationDays = itemHelper.getExpiration(itemLabel);

                    Date expirationDate = ItemHelper.getExpirationDate(date, expirationDays);

                    Item item = new Item(itemLabel, name, date, expirationDate);
                    itemList.add(item);
                }
                updateView(itemList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG_CANCEL, "Failed to receive values from database.", error.toException());
            }
        });
        //DatabaseReference items = dbRef.child("items").child("produce");
    }

    public class ImageAdapter extends BaseAdapter {
        private final List<Item> itemList;
        private final Context mContext;

        public ImageAdapter(Context context, List<Item> items) {
            itemList = items;
            mContext = context;
        }

        public int getCount() {
            return itemList.size();
        }

        public Object getItem(int position) {
            return itemList.get(position);
        }

        public long getItemId(int position) {
            return 0;
        }

//        // create a new ImageView for each item referenced by the Adapter
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ImageView imageView;
//            if (convertView == null) {
//                // if it's not recycled, initialize some attributes
//                imageView = new ImageView(mContext);
//                imageView.setLayoutParams(new GridView.LayoutParams(200, 200));
//                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                imageView.setPadding(8, 8, 8, 8);
//                imageView.setBackgroundColor(0xFFFFFF);
//            } else {
//                imageView = (ImageView) convertView;
//            }
//            Item item = (Item) getItem(position);
//            imageView.setImageResource(itemHelper.getImageId(item.getLabel()));
//            return imageView;
//        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Item item = (Item) getItem(position);

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.griditem, null);
                TextView titleTextView = (TextView) convertView.findViewById(R.id.titleTextView);
                ImageView imageView = (ImageView)convertView.findViewById(R.id.imageView);
                titleTextView.setText(item.getDisplayName());
                imageView.setImageResource(itemHelper.getImageId(item.getLabel()));
                convertView.setOnClickListener(new ItemDetailClickListener(item));
            }
            return convertView;
        }

        private class ItemDetailClickListener implements View.OnClickListener
        {
            private final Item item;

            ItemDetailClickListener(Item item)
            {
                this.item = item;
            }

            @Override
            public void onClick(View view) {
                showItemDetailActivity(item);
            }
        }
    }
}
