package com.example.agrisite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import io.github.muddz.styleabletoast.StyleableToast;

public class MyProfileActivity extends AppCompatActivity {

    String fullName, VSDomainFromDB, userIDFromDB;
    TextView TextViewAdminFullName, TextViewAdminProvince ,TextViewAdminDivision, TextViewAdminVSDomain;

    Button BtnLogout;

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        //TextView initialization
        TextViewAdminFullName = findViewById(R.id.TextViewAdminFullName);
        TextViewAdminProvince = findViewById(R.id.TextViewAdminProvince);
        TextViewAdminDivision = findViewById(R.id.TextViewAdminDivision);
        TextViewAdminVSDomain = findViewById(R.id.TextViewAdminVSDomain);

        //Button Initialization

        BtnLogout = findViewById(R.id.BtnLogout);

        BtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sign out the user
                FirebaseAuth.getInstance().signOut();

                // Redirect to the login activity
                StyleableToast.makeText(MyProfileActivity.this, "Logout Successfully!", Toast.LENGTH_SHORT, R.style.SuccessToast).show();
                startActivity(new Intent(MyProfileActivity.this, WelcomePage.class));

                finish(); // Close the current activity
            }
        });

        // Retrieve userIDFromDB from intent
        Intent intent = getIntent();
        fullName = intent.getStringExtra("full_name_of_user");

        // Initialize the Firebase Database reference
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        // Retrieve data from Firebase based on userIDFromDB
        usersRef.orderByChild("full_name_of_user").equalTo(fullName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        if (userSnapshot.hasChild("selectedProvince") && userSnapshot.hasChild("selectedVSDomain") && userSnapshot.hasChild("selectedDivision")) {
                            String province_of_admin = userSnapshot.child("selectedProvince").getValue(String.class);
                            String division_of_admin = userSnapshot.child("selectedDivision").getValue(String.class);
                            String VSdomain_of_admin = userSnapshot.child("selectedVSDomain").getValue(String.class);

                            // Set the retrieved data to the respective TextViews
                            TextViewAdminProvince.setText(province_of_admin);
                            TextViewAdminDivision.setText(division_of_admin);
                            TextViewAdminVSDomain.setText(VSdomain_of_admin);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled if needed
            }
        });

        showUserData();  // Show user data from intent
    }


    public void showUserData() {
        Intent intent = getIntent();
        fullName = intent.getStringExtra("full_name_of_user");
        VSDomainFromDB = intent.getStringExtra("selectedVSDomain");
        userIDFromDB = intent.getStringExtra("userID");

        TextViewAdminFullName.setText(fullName);

    }
}