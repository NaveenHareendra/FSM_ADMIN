package com.example.agrisite;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DivisionRecyclerViewAdapter extends RecyclerView.Adapter<DivisionRecyclerViewAdapter.MyViewHolder> {

    private final List<FOName> items; // Items Array List
    private final Context context; // Context

    // Constructor
    public DivisionRecyclerViewAdapter(List<FOName> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public DivisionRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_division_recycler_view_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DivisionRecyclerViewAdapter.MyViewHolder holder, int position) {
        FOName foName = items.get(position);
        FOName uid = items.get(position);

        holder.TextViewFOName.setText(foName.getFull_name_of_user());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // MyViewHolder Class to hold View Reference for every item in the RecyclerView
    class MyViewHolder extends RecyclerView.ViewHolder {

        // Declaring TextViews
        private final TextView TextViewFOName;
        private final ImageView ImageViewShowPerformance, ImageViewTrackLocation, ImageViewDownloadReports;
        private final ConstraintLayout ShowAnalyticsCardView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            // Getting TextViews from division_recycler_view_adapter_layout XML file
            TextViewFOName = itemView.findViewById(R.id.TextViewFOName);

            ShowAnalyticsCardView = itemView.findViewById(R.id.ShowAnalyticsCardView);
            ImageViewShowPerformance = itemView.findViewById(R.id.ImageViewPerformance);
            ImageViewDownloadReports = itemView.findViewById(R.id.ImageViewDownloadReport);
            ImageViewTrackLocation = itemView.findViewById(R.id.ImageViewTrackLocation);

            //Opens the Each FO Performance Activity
            ImageViewShowPerformance.setOnClickListener(view -> {
                FOName foName = items.get(getAdapterPosition());
                FOName uid = items.get(getAdapterPosition());

                Intent i = new Intent(itemView.getContext(), EachFOPerformance.class);

                i.putExtra("FIELD_OFFICER_NAME", foName.getFull_name_of_user());
                i.putExtra("FIELD_OFFICER_ID", foName.getUid());

                itemView.getContext().startActivity(i);

                // Print to verify values
                Log.d("THE FO NAME CAME to DRVA", "Full Name: " + foName.getFull_name_of_user());
                Log.d("THE FO ID CAME DRVA", "FO ID: " + uid.getUid());
            });

            //Opens the Download Reports Activity
            ImageViewDownloadReports.setOnClickListener(view -> {
                FOName foName = items.get(getAdapterPosition());
                FOName uid = items.get(getAdapterPosition());

                Intent j = new Intent(itemView.getContext(), DownlaodReports.class);

                j.putExtra("FIELD_OFFICER_NAME", foName.getFull_name_of_user());
                j.putExtra("FIELD_OFFICER_ID", foName.getUid());

                itemView.getContext().startActivity(j);

                // Print to verify values
                Log.d("THE FO NAME CAME to DRVA", "Full Name: " + foName.getFull_name_of_user());
                Log.d("THE FO ID CAME DRVA", "FO ID: " + uid.getUid());

            });

            ImageViewTrackLocation.setOnClickListener(view -> {
                FOName foName = items.get(getAdapterPosition());
                FOName uid = items.get(getAdapterPosition());

                Intent j = new Intent(itemView.getContext(), FieldOfficerLocation.class);

                j.putExtra("FIELD_OFFICER_NAME", foName.getFull_name_of_user());
                j.putExtra("FIELD_OFFICER_ID", foName.getUid());

                itemView.getContext().startActivity(j);

                // Print to verify values
                Log.d("THE FO NAME CAME to DRVA", "Full Name: " + foName.getFull_name_of_user());
                Log.d("THE FO ID CAME DRVA", "FO ID: " + uid.getUid());

            });
        }
    }
}
