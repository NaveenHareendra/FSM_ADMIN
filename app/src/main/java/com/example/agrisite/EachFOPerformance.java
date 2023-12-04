package com.example.agrisite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

public class EachFOPerformance extends AppCompatActivity {
    private PieChart chart;
    String FIELD_OFFICER_NAME, FIELD_OFFICER_ID;
    int totalcount = 0, openedCount = 0, inProgressCount = 0, onHoldCount = 0, completedCount = 0, incompletedCount = 0, rejectedcount = 0, acceptedCount = 0;

    Button BtnShowAnalytics;
    public static class CustomView extends CalendarView {

        public CustomView(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {

            Paint paint = new Paint();
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(2);

            canvas.drawLine(0, 0, 100, 100, paint);
            super.onDraw(canvas);
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_each_foperformance);

        showUserData();

        chart = findViewById(R.id.pie_chart);
        BtnShowAnalytics =findViewById(R.id.BtnShowAnalytics);

        //Checking that the userID (userIDFromDB) coming from the DB equals to the FIELD_OFFICER_ID coming from the intent
        DatabaseReference tasksRef = FirebaseDatabase.getInstance().getReference("Tasks");
        Query query = tasksRef.orderByChild("userIDFromDB").equalTo(FIELD_OFFICER_ID);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
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

                addToPieChart();
            }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle database error
                }
            });

        BtnShowAnalytics.setOnClickListener(v -> {
            Intent i = new Intent(EachFOPerformance.this, AdminAnalytics.class);
            i.putExtra("FIELD_OFFICER_NAME",  FIELD_OFFICER_NAME);
            startActivity(i);

        });
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
    //Get the FIELD_OFFICER_NAME through the intent coming from DivisionRecyclerViewAdapter
    public void showUserData() {
        Intent intent = getIntent();
        FIELD_OFFICER_NAME = intent.getStringExtra("FIELD_OFFICER_NAME");
        FIELD_OFFICER_ID = intent.getStringExtra("FIELD_OFFICER_ID");

        // Print to verify values
        Log.d("THE FO NAME CAME TO EACHFOPERFORMANCE", "Full Name: " + FIELD_OFFICER_NAME);
        Log.d("THE FO ID CAME TO EACHFOPERFORMANCE", "FO ID: " + FIELD_OFFICER_ID);
    }
}