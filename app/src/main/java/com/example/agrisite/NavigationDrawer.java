package com.example.agrisite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

public class NavigationDrawer extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    DatabaseReference obj;
    TextView AdminNameText, TextViewAllTasksCount, TextViewOpenedTasksCount,TextViewCompletedTasksCount, TextViewInprogressTasksCount, TextViewOnHoldTasksCount, TextViewRejectedTasksCount, TextViewAcceptedTasksCount, TextViewIncompletedTasksCount;
    String fullnameFromDB, VSDomainFromDB, userIDFromDB, DivisionFromDB;

    int totalcount = 0, openedCount = 0, inProgressCount = 0, onHoldCount = 0, completedCount = 0, incompletedCount = 0, rejectedcount = 0, acceptedCount = 0;
    BottomAppBar bottomAppBar;
    private PieChart chart;
    //private static final int REQUEST_CODE = 1232;

    ImageView iconGenerateReports;

    /*SharedPreferences sharedPreferences;

    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_NAME = "name";
    private static final String KEY_PASSWORD = "password";*/

    //Adds the toggle functionality to the header section of the navigation drawer.
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);

        obj = FirebaseDatabase.getInstance().getReference().child("Users");

        View regFoIcon = findViewById(R.id.bookmark);

        regFoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NavigationDrawer.this, RegisterFO.class);
                startActivity(intent);
            }
        });

        FloatingActionButton divisionPreviewbtn = findViewById(R.id.floatBtn);

        divisionPreviewbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NavigationDrawer.this, DvisionPreview.class);
                intent.putExtra("full_name_of_user", fullnameFromDB);
                intent.putExtra("selectedDivision", DivisionFromDB);
                intent.putExtra("selectedVSDomain", VSDomainFromDB);
                intent.putExtra("userID", userIDFromDB);
                startActivity(intent);
            }
        });

        View taskPreviewIcon = findViewById(R.id.notifications);

        taskPreviewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NavigationDrawer.this, TaskPreview.class);
                intent.putExtra("full_name_of_user", fullnameFromDB);
                intent.putExtra("selectedDivision", DivisionFromDB);
                intent.putExtra("selectedVSDomain", VSDomainFromDB);
                intent.putExtra("userID", userIDFromDB);

                startActivity(intent);
            }
        });

        View myProfileAct = findViewById(R.id.profile);

        myProfileAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NavigationDrawer.this, MyProfileActivity.class);
                intent.putExtra("full_name_of_user", fullnameFromDB);
                intent.putExtra("selectedDivision", DivisionFromDB);
                intent.putExtra("selectedVSDomain", VSDomainFromDB);
                intent.putExtra("userID", userIDFromDB);
                startActivity(intent);
            }
        });
        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navigationView);
        // bottomAppBar = findViewById(R.id.bottomAppBar);

        AdminNameText = findViewById(R.id.AdminNameText);

        TextViewAllTasksCount = findViewById(R.id.TextViewAllTasksCount);
        TextViewOpenedTasksCount = findViewById(R.id.TextViewOpenedTasksCount);
        TextViewIncompletedTasksCount = findViewById(R.id.TextViewInCompletedTasksCount);
        TextViewCompletedTasksCount = findViewById(R.id.TextViewCompletedTasksCount);
        TextViewInprogressTasksCount = findViewById(R.id.TextViewInprogressTasksCount);
        TextViewOnHoldTasksCount = findViewById(R.id.TextViewOnHoldTasksCount);
        TextViewAcceptedTasksCount = findViewById(R.id.TextViewAcceptTasksCount);
        TextViewRejectedTasksCount = findViewById(R.id.TextViewRejectTasksCount);


        chart = findViewById(R.id.pie_chart);

        //sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        showUserData();

        //When open activity first check shared preferences data available or not

        /*String user_name = sharedPreferences.getString(KEY_NAME, null);
        String user_password = sharedPreferences.getString(KEY_PASSWORD, null);

        if(user_name!=null || user_password!=null){
            //Set the data on text views

            AdminNameText.setText(user_name);
        }*/

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.menu_open, R.string.menu_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    Log.i("MENU_DRAWER_TAG", "Home item is clicked");
                    drawerLayout.closeDrawer(GravityCompat.START);

                } else if (itemId == R.id.nav_addfo) {
                    Log.i("MENU_DRAWER_TAG", "Register Field Officer item is clicked");
                    Intent i = new Intent(NavigationDrawer.this, RegisterFO.class);
                    startActivity(i);
                    drawerLayout.closeDrawer(GravityCompat.START);

                } else if (itemId == R.id.nav_taskboard) {
                    Log.i("MENU_DRAWER_TAG", "Task Preview item is clicked");

                    Intent i = new Intent(NavigationDrawer.this, TaskPreview.class);

                    i.putExtra("full_name_of_user", fullnameFromDB);
                    i.putExtra("selectedDivision", DivisionFromDB);
                    i.putExtra("selectedVSDomain", VSDomainFromDB);
                    i.putExtra("userID", userIDFromDB);

                    startActivity(i);

                    drawerLayout.closeDrawer(GravityCompat.START);

                } else if (itemId == R.id.nav_FOList) {
                    Log.i("MENU_DRAWER_TAG", "My Division item is clicked");

                    Intent i = new Intent(NavigationDrawer.this, DvisionPreview.class);

                    i.putExtra("full_name_of_user", fullnameFromDB);
                    i.putExtra("selectedDivision", DivisionFromDB);
                    i.putExtra("selectedVSDomain", VSDomainFromDB);
                    i.putExtra("userID", userIDFromDB);

                    Toast.makeText(NavigationDrawer.this, "Selected Division : " +DivisionFromDB, Toast.LENGTH_SHORT).show();

                    startActivity(i);

                    drawerLayout.closeDrawer(GravityCompat.START);

                } else if(itemId == R.id.nav_profile){
                    Log.i("MENU_DRAWER_TAG", "Profile item is clicked");

                    Intent i = new Intent(NavigationDrawer.this,MyProfileActivity.class);

                    i.putExtra("full_name_of_user", fullnameFromDB);
                    i.putExtra("selectedDivision", DivisionFromDB);
                    i.putExtra("selectedVSDomain", VSDomainFromDB);
                    i.putExtra("userID", userIDFromDB);

                    startActivity(i);
                }
                return true;
            }
        });

        // Get the task count of each division
        DatabaseReference tasksRef = FirebaseDatabase.getInstance().getReference("Tasks");
        Query querySameDomainTask = tasksRef.orderByChild("divisionFromDB").equalTo(DivisionFromDB);

        querySameDomainTask.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Iterate Through Tasks and Update Counters

                for (DataSnapshot taskSnapshot : snapshot.getChildren()) {
                    if (taskSnapshot.child("taskStatus").exists()) {
                        String taskStatus = taskSnapshot.child("taskStatus").getValue(String.class);
                        if (taskStatus != null) {
                            switch (taskStatus) {
                                case "Opened":
                                    openedCount++;
                                    break;
                                case "Resolved":
                                    completedCount++;
                                    break;
                                case "In Progress":
                                    inProgressCount++;
                                    break;
                                case "Accepted":
                                    acceptedCount++;
                                    break;
                                case "On Hold":
                                    onHoldCount++;
                                    break;
                                case "Rejected":
                                    rejectedcount++;
                                    break;
                                case "Unresolved":
                                    incompletedCount++;
                                    break;
                            }
                            totalcount = openedCount+rejectedcount+acceptedCount+inProgressCount+onHoldCount+completedCount+incompletedCount;
                        }
                    }
                }

                // Step 4: Display the Counts
                TextViewAllTasksCount.setText(String.valueOf(totalcount));
                TextViewCompletedTasksCount.setText(String.valueOf(completedCount));
                TextViewInprogressTasksCount.setText(String.valueOf(inProgressCount));
                TextViewOnHoldTasksCount.setText(String.valueOf(onHoldCount));
                TextViewRejectedTasksCount.setText(String.valueOf(rejectedcount));
                TextViewIncompletedTasksCount.setText(String.valueOf(incompletedCount));
                TextViewAcceptedTasksCount.setText(String.valueOf(acceptedCount));
                TextViewOpenedTasksCount.setText(String.valueOf(openedCount));

                addToPieChart();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }

    public void showUserData() {
        Intent intent = getIntent();

        fullnameFromDB = intent.getStringExtra("full_name_of_user");
        VSDomainFromDB = intent.getStringExtra("selectedVSDomain");
        DivisionFromDB = intent.getStringExtra("selectedDivision");
        userIDFromDB = intent.getStringExtra("userID");

        AdminNameText.setText(fullnameFromDB);

        // Print to verify values
        Log.d("NavigationDrawer", "Full Name: " + fullnameFromDB);
        Log.d("NavigationDrawer", "selectedDivision: " + DivisionFromDB);
        Log.d("NavigationDrawer", "selectedVSDomain: " + VSDomainFromDB);
        Log.d("NavigationDrawer", "userID: " + userIDFromDB);
    }

    private void addToPieChart() {

        chart.addPieSlice(new PieModel("Opened Count", openedCount, Color.parseColor("#DFFF00")));
        chart.addPieSlice(new PieModel("Completed Count", completedCount, Color.parseColor("#9DC44D")));
        chart.addPieSlice(new PieModel("In Progress Count ", inProgressCount, Color.parseColor("#FFBF00")));
        chart.addPieSlice(new PieModel("On Hold Count", onHoldCount, Color.parseColor("#B5AC95")));

        chart.addPieSlice(new PieModel("Accepted Count", acceptedCount, Color.parseColor("#FF7F50")));
        chart.addPieSlice(new PieModel("Rejected Count", rejectedcount, Color.parseColor("#DE3163")));
        chart.addPieSlice(new PieModel("Incomplete Count ", incompletedCount, Color.parseColor("#40E0D0")));

        chart.startAnimation();
    }
}
