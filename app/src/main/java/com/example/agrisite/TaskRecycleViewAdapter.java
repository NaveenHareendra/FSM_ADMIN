package com.example.agrisite;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class TaskRecycleViewAdapter extends RecyclerView.Adapter<TaskRecycleViewAdapter.MyViewHolder> {

    private final List<TaskItems> items; // Items ArrayList
    private final Context context; // Context
    private final DatabaseReference databaseReference; // Firebase Database reference

    public TaskRecycleViewAdapter(List<TaskItems> items, Context context) {
        this.items = items;
        this.context = context;

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Tasks");
    }

    @NonNull
    @Override
    public TaskRecycleViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_task_recycle_view_adapter, null));
    }

    @Override
    public void onBindViewHolder(@NonNull TaskRecycleViewAdapter.MyViewHolder holder, int position) {
        TaskItems tasks = items.get(position);

        holder.title.setText(tasks.getTitle());
        holder.FullName.setText(tasks.getFullName());
        holder.description.setText(tasks.getDescription());
        holder.startdate.setText(tasks.getStartdate());
        holder.enddate.setText(tasks.getEnddate());
        //holder.taskstatus.setText(tasks.getTaskstatus());

        holder.btnAccept.setOnClickListener(view -> {

            // Update the task status to "IN PROGRESS" in the TextView
            //holder.taskstatus.setText("Accepted");

            // Update the task status to "IN PROGRESS" in the Firebase Database
            updateTaskStatus(tasks.getKey(), "Accepted");


        });

        holder.btnReject.setOnClickListener(view -> {

            showRejectAlertDialog(holder, tasks.getKey());

        });
    }

    private void showRejectAlertDialog(TaskRecycleViewAdapter.MyViewHolder holder, String taskKey) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setIcon(R.drawable.baseline_info_24);
        builder.setTitle("Are you sure you want to reject?");
        builder.setMessage("Rejected tasks can't undo.");

        // Add the buttons
        builder.setPositiveButton("Yes", (dialog, which) -> {
            // Update the task status to "REJECTED" in the Firebase Database
            //holder.taskstatus.setText("Rejected");

            updateTaskStatus(taskKey, "Rejected");
        });

        builder.setNegativeButton("No", (dialog, which) -> {
            // User canceled the rejection, do nothing
            Toast.makeText(holder.title.getContext(), "Cancelled", Toast.LENGTH_LONG).show();
        });

        // Create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView title, FullName,description, startdate, enddate /*, taskstatus*/;
        Button btnAccept, btnReject;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.Title_of_Task);
            FullName = itemView.findViewById(R.id.TaskOwner);
            description = itemView.findViewById(R.id.Description_of_Task);
            startdate = itemView.findViewById(R.id.Start_of_Task);
            enddate = itemView.findViewById(R.id.End_of_Task);

            //taskstatus = itemView.findViewById(R.id.Task_Status);

            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }

    private void updateTaskStatus(String key, String newStatus) {
        // Update the task status in the Firebase Database
        databaseReference.child(String.valueOf(key)).child("taskStatus").setValue(newStatus);
    }
}
