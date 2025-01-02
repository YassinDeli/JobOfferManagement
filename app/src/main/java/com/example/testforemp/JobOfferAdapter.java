package com.example.testforemp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testforemp.Models.Job;

import java.util.ArrayList;
import java.util.List;

 class JobOfferAdapter extends RecyclerView.Adapter<JobOfferAdapter.JobOfferViewHolder> {

    private Context context;
    private List<Job> jobList;

    public JobOfferAdapter(Context context, List<Job> jobList) {
        this.context = context;
        this.jobList = jobList != null ? jobList : new ArrayList<>(); // Initialisation de jobList si null
    }

    @Override
    public JobOfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_job_offer, parent, false);
        return new JobOfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(JobOfferViewHolder holder, int position) {
        if (position >= 0 && position < jobList.size()) {
            Job job = jobList.get(position);
            holder.titleTextView.setText(job.getTitle());
            holder.companyTextView.setText(job.getCompany());
            holder.locationTextView.setText(job.getLocation());
            holder.dateTextView.setText(job.getDate());
            holder.descriptionTextView.setText(job.getDescription());

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, JobDetailsActivity.class);
                intent.putExtra("jobTitle", job.getTitle());
                intent.putExtra("jobCompany", job.getCompany());
                intent.putExtra("jobLocation", job.getLocation());
                intent.putExtra("jobDatePosted", job.getDate());
                intent.putExtra("jobDescription", job.getDescription());
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return jobList != null ? jobList.size() : 0;  // Gestion du cas où jobList est null
    }

    public static class JobOfferViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView, companyTextView, locationTextView, dateTextView, descriptionTextView;

        public JobOfferViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.jobTitle);
            companyTextView = itemView.findViewById(R.id.jobCompany);
            locationTextView = itemView.findViewById(R.id.jobLocation);
            dateTextView = itemView.findViewById(R.id.jobDate);
            descriptionTextView = itemView.findViewById(R.id.jobDescription);
        }
    }

     public void updateJobList(List<Job> jobList) {
         this.jobList.clear();
         this.jobList.addAll(jobList);
         notifyDataSetChanged();
     }
}