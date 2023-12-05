package com.example.agrisite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class ReportAdapter {
    private final Context context;
    private final int totalTasks;
    private final int completedTasks;
    private final float completionRatio;
    private final float incompletionRatio;
    private final String officerName;
    private final String fromDate;
    private final String toDate;

    public ReportAdapter(Context context, int totalTasks, int completedTasks, float completionRatio, float incompletionRatio, String officerName, String fromDate, String toDate) {
        this.context = context;
        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
        this.completionRatio = completionRatio;
        this.incompletionRatio = incompletionRatio;
        this.officerName = officerName;
        this.fromDate = fromDate;
        this.toDate = toDate;

    }

    public View getView() {
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.report_item, null);

        // Set text for field officer name
        TextView officerNameTextView = view.findViewById(R.id.ReportFoName);
        officerNameTextView.setText(officerName);

        // Set text for from date
        TextView fromDateTextView = view.findViewById(R.id.ReportFrom);
        fromDateTextView.setText(fromDate);

        // Set text for to date
        TextView toDateTextView = view.findViewById(R.id.ReportTo);
        toDateTextView.setText(toDate);

        // Set text for total tasks
        TextView totalTasksTextView = view.findViewById(R.id.total_tasks);
        totalTasksTextView.setText(String.valueOf(totalTasks));

        // Set text for completed tasks
        TextView completedTasksTextView = view.findViewById(R.id.completed_tasks);
        completedTasksTextView.setText(String.valueOf(completedTasks));

        // Set text for completion ratio
        TextView completionRatioTextView = view.findViewById(R.id.completion_ratio);
        completionRatioTextView.setText((completionRatio*100) + "%");

        // Set text for incompleted ratio
        TextView incompletionRatioTextView = view.findViewById(R.id.incompletion_ratio);
        incompletionRatioTextView.setText((incompletionRatio*100) + "%");

        return view;
    }
}
