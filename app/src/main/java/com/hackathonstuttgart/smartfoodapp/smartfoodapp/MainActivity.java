package com.hackathonstuttgart.smartfoodapp.smartfoodapp;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    private String TAG_CANCEL = "CANCELLED";
    private String TAG_CHANGE_DATA = "CHANGED";
    private String TAG_LOGIN_FAIL = "LOGIN FAILED";
    private String TAG_LOGIN_SUCCESS = "LOGGED IN";

    private FirebaseAuth auth;
    private GridLayout itemGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        itemGrid = (GridLayout) findViewById(R.id.itemGrid);
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

        }
    }

    public void findRecipes(View view) {

    }

    private void updateView()
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
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for(DataSnapshot child : dataSnapshot.child("items").getChildren())
                {
                    String itemName = child.getKey();

                    Log.d(TAG_CHANGE_DATA, "Item name is: " + itemName);
                }
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
