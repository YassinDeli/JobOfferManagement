package com.example.testforemp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testforemp.Models.Job;

import java.util.List;

public class CandidateJobOfferAdapter extends RecyclerView.Adapter<CandidateJobOfferAdapter.JobViewHolder> {

    private List<Job> jobList;
    private final Context context;

    public CandidateJobOfferAdapter(Context context, List<Job> jobList) {
        this.context = context;
        this.jobList = jobList;
    }

    @Override
    public JobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_candidate_job_offer, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(JobViewHolder holder, int position) {
        Job job = jobList.get(position);

        holder.jobTitle.setText(job.getTitle());
        holder.jobCompany.setText(job.getCompany());
        holder.jobLocation.setText(job.getLocation());
        holder.jobDate.setText(job.getDate());
        holder.jobDescription.setText(job.getDescription());
        holder.jobDomain.setText(job.getDomain());
        holder.jobType.setText(job.getType());

        holder.applyJobButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, ApplyJobActivity.class);
            intent.putExtra("jobId", job.getId()); // Assurez-vous que Job a un champ id
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public void updateJobList(List<Job> newJobList) {
        if (newJobList != null) {
            jobList.clear();
            jobList.addAll(newJobList);
            notifyDataSetChanged();
        }
    }

    public static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle, jobCompany, jobLocation, jobDate, jobDescription, jobDomain, jobType;
        Button applyJobButton;

        public JobViewHolder(View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.jobTitle);
            jobCompany = itemView.findViewById(R.id.jobCompany);
            jobLocation = itemView.findViewById(R.id.jobLocation);
            jobDate = itemView.findViewById(R.id.jobDate);
            jobDescription = itemView.findViewById(R.id.jobDescription);
            jobDomain = itemView.findViewById(R.id.jobDomain);
            jobType = itemView.findViewById(R.id.jobType);
            applyJobButton = itemView.findViewById(R.id.applyJobButton);
        }
    }
}
