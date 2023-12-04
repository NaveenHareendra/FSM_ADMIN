package com.example.agrisite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DvisionPreview extends AppCompatActivity {
    TextView AdminNameText,AdminDivisionText;
    String DivisionFromDB, userIDFromDB, VSDomainFromDB, fullnameFromDB;
    private final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private List<FOName> FOItemList = new ArrayList<>();
    private DivisionRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dvision_preview);

        AdminNameText = findViewById(R.id.AdminNameText);
        AdminDivisionText = findViewById(R.id.AdminDivisionText);

        final RecyclerView divisionRecyclerView = findViewById(R.id.AGSCDomain_preview_recycle_view);

        divisionRecyclerView.setHasFixedSize(true);
        divisionRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        showUserData();

        // Initialize the adapter with an empty list
        adapter = new DivisionRecyclerViewAdapter(FOItemList,this);
        divisionRecyclerView.setAdapter(adapter);

        // Update the adapter when data changes
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FOItemList.clear();

                // Getting all children users from "Users" whose role is field officer & division is similar to admin's division
                for (DataSnapshot userSnapshot : snapshot.child("Users").getChildren()) {

                    if (userSnapshot.hasChild("full_name_of_user") && userSnapshot.hasChild("selectedDivision") && userSnapshot.hasChild("selectedRole")) {

                        String fullName = userSnapshot.child("full_name_of_user").getValue(String.class);
                        String selecteddivision = userSnapshot.child("selectedDivision").getValue(String.class);
                        String Role = userSnapshot.child("selectedRole").getValue(String.class);

                        if (selecteddivision != null && selecteddivision.equals(DivisionFromDB) && (Objects.equals(Role, "Field Officer"))){
                            String uid = userSnapshot.getKey();
                            FOName foName = new FOName(fullName,uid);
                            FOItemList.add(foName);
                        }
                    }
                }

                // Notify the adapter about the data change
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle errors here
            }
        });

    }

    public void showUserData() {
        Intent intent = getIntent();

        fullnameFromDB = intent.getStringExtra("full_name_of_user");
        VSDomainFromDB = intent.getStringExtra("selectedVSDomain");
        DivisionFromDB = intent.getStringExtra("selectedDivision");
        userIDFromDB = intent.getStringExtra("userID");

        Log.d("DivisionPreview", "Full Name: " + fullnameFromDB);
        Log.d("DivisionPreview", "selectedDivision: " + DivisionFromDB);
        Log.d("DivisionPreview", "selectedVSDomain: " + VSDomainFromDB);
        Log.d("DivisionPreview", "userID: " + userIDFromDB);

        AdminNameText.setText(fullnameFromDB);
        AdminDivisionText.setText(DivisionFromDB);
    }
}