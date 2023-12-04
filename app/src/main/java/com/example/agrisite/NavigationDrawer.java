package com.example.agrisite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
                    Log.i("MENU_DRAWER_TAG", "Task item is clicked");
                    Intent i = new Intent(NavigationDrawer.this, RegisterFO.class);
                    startActivity(i);
                    drawerLayout.closeDrawer(GravityCompat.START);

                } else if (itemId == R.id.nav_taskboard) {
                    Log.i("MENU_DRAWER_TAG", "Reports item is clicked");

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
                    i.putExtra("selectedVSDomain", VSDomainFromDB);
                    i.putExtra("selectedDivision", DivisionFromDB);
                    i.putExtra("userID", userIDFromDB);

                    Toast.makeText(NavigationDrawer.this, "Selected Division : " +DivisionFromDB, Toast.LENGTH_SHORT).show();

                    startActivity(i);

                    drawerLayout.closeDrawer(GravityCompat.START);

                } else if (itemId == R.id.nav_analytics) {
                    /*Log.i("MENU_DRAWER_TAG", "FO Analytics item is clicked");

                    Intent i = new Intent(NavigationDrawer.this, xyz.class);

                    i.putExtra("full_name_of_user", fullnameFromDB);
                    i.putExtra("selectedVSDomain", VSDomainFromDB);
                    i.putExtra("userID", userIDFromDB);

                    startActivity(i);*/

                } else if (itemId == R.id.nav_reports) {
                Log.i("MENU_DRAWER_TAG", "Download Reports item is clicked");

                Intent i = new Intent(NavigationDrawer.this, DownlaodReports.class);

                i.putExtra("full_name_of_user", fullnameFromDB);
                i.putExtra("selectedVSDomain", VSDomainFromDB);
                i.putExtra("selectedDivision", DivisionFromDB);
                i.putExtra("userID", userIDFromDB);

                startActivity(i);

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
        Query querySameDomainTask = tasksRef.orderByChild("vsdomainFromDB").equalTo(VSDomainFromDB);

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




   /* private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void askPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with creating PDF
                createPDF();
            } else {
                Toast.makeText(this, "Permission denied. Cannot create PDF.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void createPDF() {
        // Create a new PdfDocument
        PdfDocument document = new PdfDocument();

        // Create a page info with the desired dimensions
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1080, 1920, 1).create();

        // Start a page
        PdfDocument.Page page = document.startPage(pageInfo);

        // Get the Canvas from the page
        android.graphics.Canvas canvas = page.getCanvas();

        // Create a Paint object for styling
        Paint paint = new Paint();
        paint.setColor(android.graphics.Color.RED);
        paint.setTextSize(42);

        // Specify the text and position to draw
        String text = "Hello, World";
        float x = 500;
        float y = 900;

        // Draw the text on the Canvas
        canvas.drawText(text, x, y, paint);

        // Finish the page
        document.finishPage(page);

        // Create a file in the Downloads directory
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileName = "example.pdf";
        File file = new File(downloadsDir, fileName);

        try {
            // Write the PDF content to the file
            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);

            // Close the document
            document.close();
            fos.close();
            Toast.makeText(this, "PDF created successfully!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("CreatePDF", "Error creating PDF", e);
        }
    }*/
}


