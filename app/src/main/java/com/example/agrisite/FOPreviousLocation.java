package com.example.agrisite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FOPreviousLocation extends AppCompatActivity implements OnMapReadyCallback {

    String foName, uid;
    private DatabaseReference previousLocationReference;
    GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foprevious_location);

        // Initialize Firebase database reference for previous location data
        previousLocationReference = FirebaseDatabase.getInstance().getReference("FieldOfficer_Previous_Location_Details");

        showUserData();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView2);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        retrievePreviousLocationData(uid);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(true);
    }

    private void retrievePreviousLocationData(String uid) {
        // Listen for changes in the previous location data for the specific user
        previousLocationReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<LocationData> locationHistory = new ArrayList<>();

                    for (DataSnapshot locationSnapshot : dataSnapshot.getChildren()) {
                        LocationData locationData = locationSnapshot.getValue(LocationData.class);
                        if (locationData != null) {
                            locationHistory.add(locationData);
                        }
                    }

                    displayLocationHistory(locationHistory);
                } else {
                    Log.d("LocationData", "Previous location data for user not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("LocationData", "Failed to retrieve previous location data: " + databaseError.getMessage());
            }
        });
    }

    private void displayLocationHistory(List<LocationData> locationHistory) {
        map.clear();  // Clear any existing markers

        for (LocationData locationData : locationHistory) {
            double latitude = locationData.getLatitude();
            double longitude = locationData.getLongitude();

            LatLng fieldOfficerLocation = new LatLng(latitude, longitude);
            map.addMarker(new MarkerOptions().position(fieldOfficerLocation).title(foName + " visited here"));
        }

        if (!locationHistory.isEmpty()) {
            // Move the camera to the last location in the history
            LocationData lastLocation = locationHistory.get(locationHistory.size() - 1);
            LatLng lastLatLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLng, 12f));
        }
    }

    public void showUserData() {
        Intent intent = getIntent();

        // Retrieve the values coming from DivisionRecyclerViewAdapter
        foName = intent.getStringExtra("FIELD_OFFICER_NAME");
        uid = intent.getStringExtra("FIELD_OFFICER_ID");

        // Now you have the foName and uid, you can use them as needed
        Log.d("DownloadReports", "FIELD_OFFICER_NAME: " + foName);
        Log.d("DownloadReports", "FIELD_OFFICER_ID: " + uid);
    }
}
