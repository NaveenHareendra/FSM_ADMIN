package com.example.agrisite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class TaskPreview extends AppCompatActivity {

    /*This activity is responsible for fetching tasks data from the Firebase Realtime Database and displaying it in a RecyclerView.*/

    //Getting firebase database reference to communicate with firebase database
    String fullName, VSDomainFromDB, userIDFromDB,DivisionFromDB;

    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private final List<TaskItems> tasksItemList = new ArrayList<>();
    private TaskRecycleViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_preview);

        //Getting Recyclerview from the xml file
        final RecyclerView taskRecyclerView = findViewById(R.id.task_preview_recycle_view);

        //Setting Recyclerview size fixed for every item in the recycler view
        taskRecyclerView.setHasFixedSize(true);

        //Set Layout Manger for the Recyclerview. Ex: LinearLayoutManager (Vertical) Mode
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(TaskPreview.this));

        showUserData();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                //Clear Old Task Details from the list to add new Task Details
                tasksItemList.clear();

                //Getting all children users from
                for (DataSnapshot Tasks : snapshot.child("Tasks").getChildren()) {

                    //To prevent crashes, check if tasks has all the details in the DB

                    if (Tasks.hasChild("title") && Tasks.hasChild("description") && Tasks.hasChild("startdate") && Tasks.hasChild("enddate") && Tasks.hasChild("taskStatus") && Tasks.hasChild("fullName") && Tasks.hasChild("vsdomainFromDB") && Tasks.hasChild("userIDFromDB") && Tasks.hasChild("divisionFromDB")) {

                        //String FOUserID = Tasks.child("userIDFromDB").getValue(String.class);

                        String FODivision = Tasks.child("divisionFromDB").getValue(String.class);
                        String FODomain = Tasks.child("vsdomainFromDB").getValue(String.class);
                        String Status_of_task = Tasks.child("taskStatus").getValue(String.class);

                        if (FODomain != null && FODivision.equals(DivisionFromDB) && Status_of_task.equals("Opened")) {

                            //In, here give column IDs as per the way mentioned in the firebase DB
                            final String getKey = Tasks.child("key").getValue(String.class);
                            final String getTitle = Tasks.child("title").getValue(String.class);
                            final String getDescription = Tasks.child("description").getValue(String.class);
                            final String getStart = Tasks.child("startdate").getValue(String.class);
                            final String getEnd = Tasks.child("enddate").getValue(String.class);
                            final String getTaskStatus = Tasks.child("taskStatus").getValue(String.class);
                            final String getFullName = Tasks.child("fullName").getValue(String.class);
                            final String getDivisionFromDB = Tasks.child("divisionFromDB").getValue(String.class);
                            final String getVSDomainFromDB = Tasks.child("vsdomainFromDB").getValue(String.class);
                            final String getUserIDFromDB = Tasks.child("userIDFromDB").getValue(String.class);

                            //Creating task items with task details
                            TaskItems tasks = new TaskItems(getKey, getTitle, getDescription, getStart, getEnd, getTaskStatus, getFullName,getDivisionFromDB, getVSDomainFromDB, getUserIDFromDB);

                            //Adding task items with task details
                            tasksItemList.add(tasks);
                        }
                    }
                }
                //After adding all the task related data to the list, set the adapter
                taskRecyclerView.setAdapter(new TaskRecycleViewAdapter(tasksItemList, TaskPreview.this));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void showUserData() {
        Intent intent = getIntent();
        fullName = intent.getStringExtra("full_name_of_user");
        DivisionFromDB = intent.getStringExtra("selectedDivision");
        VSDomainFromDB = intent.getStringExtra("selectedVSDomain");
        userIDFromDB = intent.getStringExtra("userID");

        Log.d("DivisionPreview", "selectedDivision: " + DivisionFromDB);

    }
}