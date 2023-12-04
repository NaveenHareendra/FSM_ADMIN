package com.example.agrisite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.CalendarView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class AdminAnalytics extends AppCompatActivity {
    String userIDFromDB, UserName, VSDomaiFromDB, FIELD_OFFICER_NAME, FIELD_OFFICER_ID;
    int openedCount = 0, inProgressCount = 0, onHoldCount = 0, completedCount = 0;
    private TextView yearView;
    TreeMap<Date, Integer> sortedAccepted;
    TreeMap<Date, Integer> sortedOnProgress;
    TreeMap<Date, Integer> sortedOnHold ;
    TreeMap<Date, Integer> sortedUnresolved;
    TreeMap<Date, Integer> sortedOpen;
    TreeMap<Date, Integer> sortedComplete;
    TreeMap<Date, Integer> sortedRejected;
    private CalendarView calendar;
    private int year = 2023;
    int year_cal = 2023;
    int month_cal = 9;
    Calendar currYCalendar = Calendar.getInstance();
    float currentyear = currYCalendar.get(Calendar.YEAR);
    LineChart mChart;
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
        setContentView(R.layout.activity_admin_analytics);

        showUserData();
        //Set the Name of the Field Officer
        ((TextView) findViewById(R.id.admin_analytics_type)).setText(FIELD_OFFICER_NAME + " - Analytics");
        // Assuming you have a Firebase reference
        DatabaseReference tasksRef = FirebaseDatabase.getInstance().getReference("Tasks");
        Query query = tasksRef.orderByChild("fullName").equalTo(FIELD_OFFICER_NAME);
        createChart(query);
        /*The calender implementation*/
        // By ID we can use each component
        // which id is assign in xml file
        // use findViewById() to get the
        // CalendarView and TextView
        calendar = (CalendarView)
                findViewById(R.id.calendar);

        // Add Listener in calendar
        calendar
                .setOnDateChangeListener(
                        new CalendarView
                                .OnDateChangeListener() {
                            @Override

                            // In this Listener have one method
                            // and in this method we will
                            // get the value of DAYS, MONTH, YEARS
                            public void onSelectedDayChange(
                                    @NonNull CalendarView view,
                                    int year,
                                    int month,
                                    int dayOfMonth)
                            {

                                // Store the value of date with
                                // format in String type Variable
                                // Add 1 in month because month
                                // index is start with 0
                                String Date
                                        = dayOfMonth + "/"
                                        + (month + 1) + "/" + year;

                                // set this date in TextView for Display

                                mChart.clear();
                                Date date;
                                try {
                                    date=new SimpleDateFormat("dd/MM/yyyy").parse(Date);
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                                draw_graphBasedDate(date);
                            }

                        });
    }

    private void createChart(Query qry){
        DatabaseReference tasksRef = FirebaseDatabase.getInstance().getReference("Tasks");
        Query query = qry;
        HashMap<Date, Integer> completed = new HashMap<Date, Integer>();
        HashMap<Date, Integer> opened = new HashMap<Date, Integer>();
        HashMap<Date, Integer> onProgress = new HashMap<Date, Integer>();
        HashMap<Date, Integer> accepted = new HashMap<Date, Integer>();
        HashMap<Date, Integer> onHold = new HashMap<Date, Integer>();
        HashMap<Date, Integer> unresolved = new HashMap<Date, Integer>();
        HashMap<Date, Integer> rejected = new HashMap<Date, Integer>();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //Iterate Through Tasks and Update Counters
                for (DataSnapshot taskSnapshot : snapshot.getChildren()) {
                    if (taskSnapshot.child("taskStatus").exists()) {
                        try{
                            String taskStatus = taskSnapshot.child("taskStatus").getValue(String.class);
                            String endDateStr = taskSnapshot.child("enddate").getValue(String.class);
                            Date taskEndDate = new SimpleDateFormat("MMM dd yyy")
                                    .parse(endDateStr);
//                            Date taskEndDate = new SimpleDateFormat("DD/MM/YYYY")
//                                    .parse(taskSnapshot.child("enddate").getValue(String.class));
//                            if(taskEndDate.compareTo(new Date())==-1){
                            int openedCount=0;
                            int completedCount=0;
                            int onProgressCount=0;
                            int acceptedCount=0;
                            int onHoldCount=0;
                            int unResolvedCount=0;
                            int rejectedCount = 0;
                            //taskStartDate is still the bigger
                            if (taskStatus != null) {
                                switch (taskStatus) {
                                    case "Opened":
                                        if(opened.get(taskEndDate)!=null){
                                            openedCount=opened.get(taskEndDate);
                                            openedCount++;
                                            opened.put(taskEndDate, openedCount);
                                        }else{
                                            openedCount++;
                                            opened.put(taskEndDate, openedCount);
                                        }
                                        break;
                                    case "Resolved":
                                        if(completed.get(taskEndDate)!=null){
                                            completedCount=completed.get(taskEndDate);
                                            completedCount++;
                                            completed.put(taskEndDate, completedCount);
                                        }else{
                                            completedCount++;
                                            completed.put(taskEndDate, completedCount);
                                        }
                                        break;
                                    case "In Progress":
                                        if(onProgress.get(taskEndDate)!=null){
                                            onProgressCount=onProgress.get(taskEndDate);
                                            onProgressCount++;
                                            onProgress.put(taskEndDate, onProgressCount);
                                        }else{
                                            onProgressCount++;
                                            onProgress.put(taskEndDate, onProgressCount);
                                        }
                                        break;
                                    case "Accepted":
                                        if(accepted.get(taskEndDate)!=null){
                                            acceptedCount=accepted.get(taskEndDate);
                                            acceptedCount++;
                                            accepted.put(taskEndDate, acceptedCount);
                                        }else{
                                            acceptedCount++;
                                            accepted.put(taskEndDate, acceptedCount);
                                        }
                                        break;
                                    case "On Hold":
                                        if(onHold.get(taskEndDate)!=null){
                                            onHoldCount=onHold.get(taskEndDate);
                                            onHoldCount++;
                                            onHold.put(taskEndDate, onHoldCount);
                                        }else{
                                            onHoldCount++;
                                            onHold.put(taskEndDate, onHoldCount);
                                        }
                                        break;
                                    case "Unresolved":
                                        if(unresolved.get(taskEndDate)!=null){
                                            unResolvedCount=unresolved.get(taskEndDate);
                                            unResolvedCount++;
                                            unresolved.put(taskEndDate, unResolvedCount);
                                        }else{
                                            unResolvedCount++;
                                            unresolved.put(taskEndDate, unResolvedCount);
                                        }
                                        break;
                                    case "Rejected":
                                        if(rejected.get(taskEndDate)!=null){
                                            rejectedCount=rejected.get(taskEndDate);
                                            rejectedCount++;
                                            rejected.put(taskEndDate, rejectedCount);
                                        }else{
                                            rejectedCount++;
                                            rejected.put(taskEndDate, rejectedCount);
                                        }
                                        break;

                                }
                            }
                        }catch(Exception e){
                            Log.e("Exception Occured:", e.toString());
                        }

                    }
                }
                draw_graph(opened, completed, onProgress, accepted, onHold, unresolved,rejected, currentyear);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database error
            }
        });
    }
        public void showUserData() {
        Intent intent = getIntent();
        UserName = intent.getStringExtra("full_name_of_user");
        userIDFromDB = intent.getStringExtra("userID");
        VSDomaiFromDB = intent.getStringExtra("selectedVSDomain");
        FIELD_OFFICER_NAME = intent.getStringExtra("FIELD_OFFICER_NAME");
        FIELD_OFFICER_ID = intent.getStringExtra("FIELD_OFFICER_ID");

        Log.d("THE FO NAME CAME to the ANALYTICS", "Full Name: " + FIELD_OFFICER_NAME);
        Log.d("THE FO ID CAME to the ANALYTICS", "FO ID: " + FIELD_OFFICER_ID);
    }

    /*
    * Below algorithm sorts a map based on the dates
    * returns a single array for all the dates, sorted
     */
    private TreeMap<Date, Integer> getHmapSortedByDate(HashMap<Date, Integer> unsorted){
        TreeMap<Date, Integer> sorted = new TreeMap<Date, Integer>(unsorted);
        sorted.putAll(unsorted);
        return sorted;
    }

    /*
    * Below algorithm sorts and makes the graph points
     */
    private ArrayList<Entry> createGraphEntries(TreeMap<Date, Integer> sortedComplete, float year ){

        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<Float> formattedMonthArr = new ArrayList<Float>();
        entries.add(new Entry(0, 0));
        formattedMonthArr.add(0f);
        for (Map.Entry<Date, Integer> entry : sortedComplete.entrySet()) {
            SimpleDateFormat dateYearFormatter = new SimpleDateFormat("YYYY");
            float currYear=Integer.parseInt(dateYearFormatter.format(entry.getKey()));
            if(year == currYear){
                SimpleDateFormat dateNoFormatter = new SimpleDateFormat("dd");
                float currDateFormatter=Integer.parseInt(dateNoFormatter.format(entry.getKey()));
                float datePercentage = currDateFormatter/30;
//                Log.d("currDateFormatter", String.valueOf(currDateFormatter));
//                Log.d("datePercentage", String.valueOf(datePercentage));
                SimpleDateFormat dateFormatter = new SimpleDateFormat("MM");
                float currMonthFormatted=Integer.parseInt(dateFormatter.format(entry.getKey()));
                float previousMonth = formattedMonthArr.get(formattedMonthArr.size()-1);

                float monthDifference = currMonthFormatted - previousMonth;
                --monthDifference;

                previousMonth++;
                while(monthDifference>=previousMonth){
                    entries.add(new Entry(previousMonth,0));
                    previousMonth++;
                }
                float datePointX = currMonthFormatted+datePercentage;
//                Log.d("datePointX", String.valueOf(datePointX));
                entries.add(new Entry(datePointX, entry.getValue()));
                formattedMonthArr.add(currMonthFormatted);
            }
        }
        return entries;
    }
    private void draw_graph(HashMap<Date, Integer> opened, HashMap<Date, Integer> completed
            , HashMap<Date, Integer> onProgress, HashMap<Date, Integer> accepted
            , HashMap<Date, Integer> onHold, HashMap<Date, Integer> unresolved, HashMap<Date, Integer> rejected,
                            float year){

        mChart = findViewById(R.id.chart1);

        ArrayList<String> xAxisValues = new ArrayList<String>();

        final String[] months = new String[] { "", "Jan", "Feb", "Mar","Apr", "May", "Jun","Jul", "Aug", "Sep"
                ,"Oct", "Nov", "Dec" };
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return months[(int) value];
            }
        };

        /*Opened task entries*/
        ArrayList<Entry> entriesOpened = new ArrayList<>();
        sortedOpen = getHmapSortedByDate(opened);
        entriesOpened = createGraphEntries(sortedOpen, year);

        /*Completed datasets drawing line */
        ArrayList<Entry> entriesCompleted = new ArrayList<>();
         sortedComplete = getHmapSortedByDate(completed);
        entriesCompleted=createGraphEntries(sortedComplete, year);

        ArrayList<Entry> entriesOnProgress = new ArrayList<>();
        sortedOnProgress = getHmapSortedByDate(onProgress);
        entriesOnProgress=createGraphEntries(sortedOnProgress, year);

        /*Accepted datasets drawing line */
        ArrayList<Entry> entriesAccepted = new ArrayList<>();
        sortedAccepted = getHmapSortedByDate(accepted);
        entriesAccepted=createGraphEntries(sortedAccepted, year);


        /*On Hold datasets drawing line */
        ArrayList<Entry> entriesOnHold = new ArrayList<>();
        sortedOnHold = getHmapSortedByDate(onHold);
        entriesOnHold=createGraphEntries(sortedOnHold, year);

        /*Incompleted datasets drawing line */
        ArrayList<Entry> entriesUnresolved = new ArrayList<>();
        sortedUnresolved = getHmapSortedByDate(unresolved);
        entriesUnresolved=createGraphEntries(sortedUnresolved, year);

        /*rejected datasets drawing line */
        ArrayList<Entry> entriesRejected = new ArrayList<>();
        sortedRejected = getHmapSortedByDate(rejected);
        entriesRejected=createGraphEntries(sortedRejected, year);

        if((entriesRejected.size()==0)&&(entriesAccepted.size()==0)&&(entriesOnHold.size()==0)
                &&(entriesCompleted.size()==0)&&(entriesOnProgress.size()==0)&&(entriesUnresolved.size()==0)
                &&(entriesOnProgress.size()==0)){
            mChart.setNoDataText("No Data Available");
            Paint p = mChart.getPaint(Chart.PAINT_INFO);
            p.setColor(Color.RED);
        }else{
            XAxis xAxis = mChart.getXAxis();
//        xAxis.setLabelCount(13,true);
            xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
            xAxis.setValueFormatter(formatter);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            ArrayList<LineDataSet> lines = new ArrayList<LineDataSet>();
            LineDataSet set1 = new LineDataSet(entriesOpened, "Opened");
            set1.setDrawFilled(true);
            set1.setFillColor(Color.WHITE);
            set1.setColor(Color.RED);
            set1.setCircleColor(Color.RED);
            lines.add(set1);

            /*Completed datasets drawing line */
            LineDataSet setCompleted = new LineDataSet(entriesCompleted, "Completed");
            setCompleted.setDrawFilled(true);
            setCompleted.setFillColor(Color.WHITE);
            setCompleted.setColor(Color.BLUE);
            setCompleted.setCircleColor(Color.BLUE);
            lines.add(setCompleted);

            /*On progress datasets drawing line */
            LineDataSet setOnProgress = new LineDataSet(entriesOnProgress, "In Progress");
            setOnProgress.setDrawFilled(true);
            setOnProgress.setFillColor(Color.WHITE);
            setOnProgress.setColor(Color.GREEN);
            setOnProgress.setCircleColor(Color.GREEN);
            lines.add(setOnProgress);

            /*Accepted datasets drawing line */
            LineDataSet setAccepted= new LineDataSet(entriesAccepted, "Accepted");
            setAccepted.setDrawFilled(true);
            setAccepted.setFillColor(Color.WHITE);
            setAccepted.setColor(Color.YELLOW);
            setAccepted.setCircleColor(Color.YELLOW);
            lines.add(setAccepted);

            /*On Hold datasets drawing line */
            LineDataSet setOnHold = new LineDataSet(entriesOnHold, "On Hold");
            setOnHold.setDrawFilled(true);
            setOnHold.setFillColor(Color.WHITE);
            setOnHold.setColor(Color.BLACK);
            setOnHold.setCircleColor(Color.BLACK);
            lines.add(setOnHold);

            /*Incompleted datasets drawing line */
            LineDataSet setUnresolved = new LineDataSet(entriesUnresolved, "Incompleted");
            setUnresolved.setDrawFilled(true);
            setUnresolved.setFillColor(Color.WHITE);
            setUnresolved.setColor(Color.MAGENTA);
            setUnresolved.setCircleColor(Color.MAGENTA);
            lines.add(setUnresolved);

            /*Rejected datasets drawing line */
            LineDataSet setRejected = new LineDataSet(entriesRejected, "Rejected");
            setRejected.setDrawFilled(true);
            setRejected.setFillColor(Color.WHITE);
            setRejected.setColor(Color.DKGRAY);
            setRejected.setCircleColor(Color.DKGRAY);
            lines.add(setRejected);

            Legend legend = mChart.getLegend();
//        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
//        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
            legend.setWordWrapEnabled(true);
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            dataSets.add(setCompleted);
            dataSets.add(setOnProgress);
            dataSets.add(setAccepted);
            dataSets.add(setOnHold);
            dataSets.add(setUnresolved);
            dataSets.add(setRejected);

            mChart.invalidate();
            mChart.setData(new LineData(dataSets));
        }

        mChart.getDescription().setText("");
        mChart.getDescription().setTextColor(Color.RED);
        mChart.animateY(1400, Easing.EaseInOutBounce);
    }

    /*
     * Below algorithm sorts and makes the graph points
     * Based on Dates
     */
    private ArrayList<Entry> createGraphEntriesBDate(Date date,TreeMap<Date, Integer> sortedComplete ){

        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<Float> formattedMonthArr = new ArrayList<Float>();
//        Log.d("Came to here somehow", String.valueOf(sortedComplete.get(date)));
        if (sortedComplete.get(date)!=null) {
            float dateValue = sortedComplete.get(date);
            entries.add(new Entry(0, 0));
            formattedMonthArr.add(0f);
            SimpleDateFormat dateNoFormatter = new SimpleDateFormat("dd");
            float currDateFormatter=Integer.parseInt(dateNoFormatter.format(date));
            float datePercentage = currDateFormatter/30;
//                Log.d("currDateFormatter", String.valueOf(currDateFormatter));
//                Log.d("datePercentage", String.valueOf(datePercentage));
            SimpleDateFormat dateFormatter = new SimpleDateFormat("MM");
            float currMonthFormatted=Integer.parseInt(dateFormatter.format(date));
            float previousMonth = formattedMonthArr.get(formattedMonthArr.size()-1);

            float monthDifference = currMonthFormatted - previousMonth;
            --monthDifference;

            previousMonth++;
            while(monthDifference>=previousMonth){
                entries.add(new Entry(previousMonth,0));
                previousMonth++;
            }
            float datePointX = currMonthFormatted+datePercentage;
//                Log.d("datePointX", String.valueOf(datePointX));
            entries.add(new Entry(datePointX, dateValue));
            formattedMonthArr.add(currMonthFormatted);
        }

        return entries;
    }

    private void draw_graphBasedDate(Date date){

        mChart = findViewById(R.id.chart1);

        ArrayList<String> xAxisValues = new ArrayList<String>();

        final String[] months = new String[] { "", "Jan", "Feb", "Mar","Apr", "May", "Jun","Jul", "Aug", "Sep"
                ,"Oct", "Nov", "Dec" };
        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return months[(int) value];
            }
        };
        SimpleDateFormat dateYearFormatter = new SimpleDateFormat("YYYY");
        int currYear=Integer.parseInt(dateYearFormatter.format(date));
//        Log.d("Came to opened tasks creating entries", "opened");
        /*Opened task entries*/
        ArrayList<Entry> entriesOpened = new ArrayList<>();
        entriesOpened = createGraphEntriesBDate(date, sortedOpen);
//        Log.d("Came to completed tasks creating entries", "completed");
        /*Completed datasets drawing line */
        ArrayList<Entry> entriesCompleted = new ArrayList<>();
        entriesCompleted=createGraphEntriesBDate(date, sortedComplete);

        ArrayList<Entry> entriesOnProgress = new ArrayList<>();
        entriesOnProgress=createGraphEntriesBDate(date, sortedOnProgress);

        /*Accepted datasets drawing line */
        ArrayList<Entry> entriesAccepted = new ArrayList<>();
        entriesAccepted=createGraphEntriesBDate(date, sortedAccepted);


        /*On Hold datasets drawing line */
        ArrayList<Entry> entriesOnHold = new ArrayList<>();
        entriesOnHold=createGraphEntriesBDate(date, sortedOnHold);

        /*Incompleted datasets drawing line */
        ArrayList<Entry> entriesUnresolved = new ArrayList<>();
        entriesUnresolved=createGraphEntriesBDate(date, sortedUnresolved);

        /*rejected datasets drawing line */
        ArrayList<Entry> entriesRejected = new ArrayList<>();
        entriesRejected=createGraphEntriesBDate(date, sortedRejected);

        if((entriesRejected.size()==0)&&(entriesAccepted.size()==0)&&(entriesOnHold.size()==0)
                &&(entriesCompleted.size()==0)&&(entriesOnProgress.size()==0)&&(entriesUnresolved.size()==0)
                &&(entriesOnProgress.size()==0)){
            mChart.setNoDataText("No Data Available");
            Paint p = mChart.getPaint(Chart.PAINT_INFO);
            p.setColor(Color.RED);
        }else{
            XAxis xAxis = mChart.getXAxis();
//        xAxis.setLabelCount(13,true);
            xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
            xAxis.setValueFormatter(formatter);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

            ArrayList<LineDataSet> lines = new ArrayList<LineDataSet>();
            LineDataSet set1 = new LineDataSet(entriesOpened, "Opened");
            set1.setDrawFilled(true);
            set1.setFillColor(Color.WHITE);
            set1.setColor(Color.RED);
            set1.setCircleColor(Color.RED);
            lines.add(set1);

            /*Completed datasets drawing line */
            LineDataSet setCompleted = new LineDataSet(entriesCompleted, "Completed");
            setCompleted.setDrawFilled(true);
            setCompleted.setFillColor(Color.WHITE);
            setCompleted.setColor(Color.BLUE);
            setCompleted.setCircleColor(Color.BLUE);
            lines.add(setCompleted);

            /*On progress datasets drawing line */
            LineDataSet setOnProgress = new LineDataSet(entriesOnProgress, "In Progress");
            setOnProgress.setDrawFilled(true);
            setOnProgress.setFillColor(Color.WHITE);
            setOnProgress.setColor(Color.GREEN);
            setOnProgress.setCircleColor(Color.GREEN);
            lines.add(setOnProgress);

            /*Accepted datasets drawing line */
            LineDataSet setAccepted= new LineDataSet(entriesAccepted, "Accepted");
            setAccepted.setDrawFilled(true);
            setAccepted.setFillColor(Color.WHITE);
            setAccepted.setColor(Color.YELLOW);
            setAccepted.setCircleColor(Color.YELLOW);
            lines.add(setAccepted);

            /*On Hold datasets drawing line */
            LineDataSet setOnHold = new LineDataSet(entriesOnHold, "On Hold");
            setOnHold.setDrawFilled(true);
            setOnHold.setFillColor(Color.WHITE);
            setOnHold.setColor(Color.BLACK);
            setOnHold.setCircleColor(Color.BLACK);
            lines.add(setOnHold);

            /*Incompleted datasets drawing line */
            LineDataSet setUnresolved = new LineDataSet(entriesUnresolved, "Incompleted Tasks");
            setUnresolved.setDrawFilled(true);
            setUnresolved.setFillColor(Color.WHITE);
            setUnresolved.setColor(Color.MAGENTA);
            setUnresolved.setCircleColor(Color.MAGENTA);
            lines.add(setUnresolved);

            /*Rejected datasets drawing line */
            LineDataSet setRejected = new LineDataSet(entriesRejected, "Rejected");
            setRejected.setDrawFilled(true);
            setRejected.setFillColor(Color.WHITE);
            setRejected.setColor(Color.DKGRAY);
            setRejected.setCircleColor(Color.DKGRAY);
            lines.add(setRejected);

            Legend legend = mChart.getLegend();
//        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
//        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
            legend.setWordWrapEnabled(true);
            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            dataSets.add(setCompleted);
            dataSets.add(setOnProgress);
            dataSets.add(setAccepted);
            dataSets.add(setOnHold);
            dataSets.add(setUnresolved);
            dataSets.add(setRejected);

            mChart.invalidate();
            mChart.setData(new LineData(dataSets));
        }

        mChart.getDescription().setText("");
        mChart.getDescription().setTextColor(Color.RED);
        mChart.animateY(1400, Easing.EaseInOutBounce);
    }

    private void processTaskData(DataSnapshot dataSnapshot) {

        for (DataSnapshot taskSnapshot : dataSnapshot.getChildren()) {
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
                        case "On Hold":
                            onHoldCount++;
                            break;
                    }
                }
            }
        }
    }













//    private void draw_graph(int year) {
//        ((TextView) findViewById(R.id.aaa_year)).setText(String.format(Locale.US, "%04d", year));
//
//        String data = "";
//        BufferedReader reader = null;
//        try {
//            reader = new BufferedReader(
//                    new InputStreamReader(getAssets().open("index.html")));
//
//            // do reading, usually loop until end of file reading
//            String mLine;
//            while ((mLine = reader.readLine()) != null) {
//                data += mLine + "\n";
//            }
//        } catch (IOException e) {
//            //log the exception
//        } finally {
//            if (reader != null) {
//                try {
//                    reader.close();
//                } catch (IOException e) {
//                    //log the exception
//                }
//            }
//        }
//    }



    //    private void draw_calendar(int year, int month) {
//        ((TextView) findViewById(R.id.aaa_year_month)).setText(String.format(Locale.US, "%04d-%02d", year, month));
//
//        TableLayout calendar = findViewById(R.id.aaa_calendar);
//        calendar.removeAllViews();
//
//        {
//            String[] days = {"M", "T", "W", "T", "F", "S", "S"};
//            TableRow row = new TableRow(this);
//
//            for (int day = 0; day < days.length; day++) {
//                TextView view = new TextView(this);
//                view.setText(days[day]);
//                int padding = 8;
//                view.setPadding(0, padding, 0, padding * 2);
//                view.setTextSize(18);
//                view.setTextColor(Color.BLACK);
//
//                view.setGravity(Gravity.CENTER_HORIZONTAL);
//                view.setGravity(View.TEXT_ALIGNMENT_CENTER);
//                view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                row.addView(view);
//            }
//            calendar.addView(row);
//        }
//
//        String day = String.format(Locale.US, "%04d-%02d-%02d", year, month, 1);
//        Date date = null;
//
//
//        try {
//            date = new SimpleDateFormat("yyyy-MM-dd", Locale.US).parse(day);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        if (date == null) return;
//
//        List<Task> tasks = new ArrayList<>();
//        HashMap<Integer, Task> map = new HashMap<>();
//
//        try {
//            TaskList taskList = officer.tasks.get(String.format(Locale.US, "%04d-%02d", year, month));
//            if (taskList != null) {
//                List<Task> t = taskList.tasks;
//                if (t != null) {
//                    tasks = t;
//                }
//            }
//
//
//
//            for (Task task : tasks) {
//                Integer d = Integer.parseInt(task.enddate.trim().split("-")[2]);
//                map.put(d, task);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        int start_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
//        if (start_index == 0) start_index = 7;
//
//        int current = 1;
//
//        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
//        int end = cal.get(Calendar.DAY_OF_MONTH);
//
//        while (current <= end) {
//            TableRow row = new TableRow(this);
//            for (int j = 0; j < 7; j++) {
//                TextView view = new TextView(this);
//
//                int padding = 12;
//                view.setPadding(0, padding, 0, padding);
//                view.setTextSize(18);
//
//                view.setGravity(Gravity.CENTER_HORIZONTAL);
//                view.setGravity(View.TEXT_ALIGNMENT_CENTER);
//                view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//                row.addView(view);
//
//                if ((start_index == (j + 1) || current > 1) && current <= end) {
//                    view.setText(String.valueOf(current));
//
//                    Task task = map.get(current);
//                    if (task != null) {
//                        view.setBackgroundColor(task.completed ? Color.GREEN : Color.YELLOW);
//                        int today = current;
//                        view.setOnClickListener(v -> {
//                            new AlertDialog.Builder(this)
//                                    .setTitle(String.format(Locale.US, "%04d-%02d-%02d", year, month, today))
//                                    .setMessage(String.format(Locale.US, "Task: %s\nDeadline: %s\nStatus: %s", task.name, task.deadline, task.completed ? "Completed" : "Pending")).show();
//                        });
//                    }
//
//                    current++;
//                }
//            }
//            calendar.addView(row);
//        }
//    }
}