//
//package com.example.agrisite;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.os.Bundle;
//import android.view.Gravity;
//import android.view.View;
//import android.webkit.WebView;
//import android.widget.CalendarView;
//import android.widget.TableLayout;
//import android.widget.TableRow;
//import android.widget.TextView;
//import com.google.android.gms.tasks.Task;
//import androidx.appcompat.app.AlertDialog;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//import java.text.ParseException;
//
//public class xyz extends AppCompatActivity {
//
//    String AdminDivision, userIDFromDB, UserName,AdminNameText;
//
//    public static Officer officer;
//    public static boolean all_division;
//
//    private int year = 2023;
//
//    int year_cal = 2023;
//    int month_cal = 9;
//
//    public static class CustomView extends CalendarView {
//
//        public CustomView(@NonNull Context context) {
//            super(context);
//        }
//
//        @Override
//        protected void onDraw(Canvas canvas) {
//
//            Paint paint = new Paint();
//            paint.setColor(Color.BLACK);
//            paint.setStrokeWidth(2);
//
//            canvas.drawLine(0, 0, 100, 100, paint);
//            super.onDraw(canvas);
//        }
//
//
//    }
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_admin_analytics);
//
//        showUserData();
//
//        //Set the Name of the Field Officer
//        ((TextView) findViewById(R.id.admin_analytics_type)).setText(AdminNameText + " - Analytics");
//
//        draw_graph(year);
//
//        //When clicks on right arrow move forward with year calendar
//        findViewById(R.id.aaa_next).setOnClickListener(v -> {
//            year++;
//            draw_graph(year);
//        });
//
//        //When clicks on left arrow move backward with year calendar
//        findViewById(R.id.aaa_prev).setOnClickListener(v -> {
//            year--;
//            draw_graph(year);
//        });
//
//        draw_calendar(year_cal, month_cal);
//
//        //When clicks on left arrow move foraward with year month calendar
//        findViewById(R.id.aaa_next2).setOnClickListener(v -> {
//            month_cal++;
//            if (month_cal > 12) {
//                month_cal = 1;
//                year_cal++;
//            }
//            draw_calendar(year_cal, month_cal);
//        });
//
//        //When clicks on left arrow move backward with year month calendar
//        findViewById(R.id.aaa_prev2).setOnClickListener(v -> {
//            month_cal--;
//            if (month_cal < 1) {
//                month_cal = 12;
//                year_cal--;
//            }
//
//            draw_calendar(year_cal, month_cal);
//        });
//
//        findViewById(R.id.aaa_report).setOnClickListener(v -> {
//
//            //Need to star Admin ReportActivity
//            ReportGenerate.officer = AdminNameText;
//            ReportGenerate.all_division = AdminDivision;
//            startActivity(new Intent(this, ReportGenerate.class));
//        });
//    }
//
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
//
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
//
///*const yValues = [];
//    const y2Values = [${completed}];
//    const y3Values = [${leftover}];*/
//
//
//        String all_ = "";
//        String complete = "";
//        String leftover = "";
//
//        for (int i = 1; i <= 12; i++) {
//            String key = String.format(Locale.US, "%04d-%02d", year, i);
//            TaskList tasks;
//            try {
//                tasks = officer.tasks.get(key);
//            } catch (Exception e) {
//                e.printStackTrace();
//                continue;
//            }
//
//
//            int all = 0, completed = 0, left = 0;
//
//            if (tasks != null) {
//                for (Task task : tasks.tasks) {
//                    all++;
//                    if (task.completed) completed++;
//                    else left++;
//
//                }
//            }
//            all_ += all + ", ";
//            complete += completed + ", ";
//            leftover += left + ", ";
//        }
//
//        data = data.replace("${all}", all_);
//        data = data.replace("${completed}", complete);
//        data = data.replace("${leftover}", leftover);
//
//
//        WebView web = findViewById(R.id.aaa_graph);
//        web.getSettings().setJavaScriptEnabled(true);
//
//        web.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        web.loadData(data, "text/html; charset=utf-8", "UTF-8");
//    }
//
//    public void showUserData() {
//        Intent intent = getIntent();
//        UserName = intent.getStringExtra("full_name_of_user");
//        userIDFromDB = intent.getStringExtra("userID");
//
//    }
//
//}