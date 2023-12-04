package com.example.agrisite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FieldOfficerLocation extends AppCompatActivity implements OnMapReadyCallback {
    String foName, uid;
    private DatabaseReference locationReference;
    GoogleMap map;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_officer_location);

        // Initialize Firebase database reference for location data
        locationReference = FirebaseDatabase.getInstance().getReference("FieldOfficer_Fused_Location_Details");

        showUserData();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);

        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        retrieveLocationData(uid);
        //fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        map.getUiSettings().setMyLocationButtonEnabled(true);
        //6.8532492,79.908804

        /*LatLng latLng = new LatLng(6.8532492,79.908804); // for navinna

        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.zoomTo(12f));

        map.addMarker(new MarkerOptions().position(latLng).title("I am Here"));*/
    }

    private void retrieveLocationData(String uid) {
        // Listen for changes in the location data for the specific user
        locationReference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    LocationData locationData = dataSnapshot.getValue(LocationData.class);

                    if (locationData != null) {
                        double latitude = locationData.getLatitude();
                        double longitude = locationData.getLongitude();

                        // Use the latitude and longitude to display the field officer's location on the map
                        LatLng fieldOfficerLocation = new LatLng(latitude, longitude);
                        map.clear();  // Clear any existing markers
                        map.addMarker(new MarkerOptions().position(fieldOfficerLocation).title(foName +" is here"));
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(fieldOfficerLocation, 12f));
                    }
                } else {
                    Log.d("LocationData", "Location data for user not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("LocationData", "Failed to retrieve location data: " + databaseError.getMessage());
            }
        });
    }


    //Google Map Marker Icon
/*    public BitmapDescriptor setIcon(Activity context, int drawableID){
        Drawable drawable = ActivityCompat.getDrawable(context, drawableID);
        drawable.setBounds(0,0,drawable.getIntrinsicHeight(), drawable.getIntrinsicWidth());

        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicHeight(), drawable.getIntrinsicWidth(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);

    }*/

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