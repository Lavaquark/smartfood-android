package com.hackathonstuttgart.smartfoodapp.smartfoodapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
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
    private final ItemHelper itemHelper = new ItemHelper();
    private GridView itemGrid;

    public MainActivity()
    {
        itemHelper.putMapping(LABEL_APPLE, LABEL_APPLE, R.drawable.apple);
        itemHelper.putMapping(LABEL_ORANGE, LABEL_ORANGE, R.drawable.orange);
        itemHelper.putMapping(LABEL_BANANA, LABEL_BANANA, R.drawable.banana);
        itemHelper.putMapping(LABEL_MILK, LABEL_MILK, R.drawable.milk);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    }

    public void findRecipes(View view) {

    }

    private void updateView(List<Item> itemList)
    {
        //itemGrid.getchildre
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
                for(DataSnapshot child : dataSnapshot.child("items").getChildren())
                {
                    String itemLabel = child.getKey();
                    String name = itemHelper.getMapping(itemLabel);
                    Item item = new Item(itemLabel, name);
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
}
