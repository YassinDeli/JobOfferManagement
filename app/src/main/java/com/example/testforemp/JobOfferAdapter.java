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

public class JobOfferAdapter extends RecyclerView.Adapter<JobOfferAdapter.JobViewHolder> {

    private List<Job> jobList;
    private final Context context;

    public JobOfferAdapter(Context context, List<Job> jobList) {
        this.context = context;
        this.jobList = jobList;
    }

    @Override
    public JobViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_job_offer, parent, false);
        return new JobViewHolder(view);
    }

    @Override
    public void onBindViewHolder(JobViewHolder holder, int position) {
        // Récupérer l'offre d'emploi à la position donnée
        Job job = jobList.get(position);

        // Afficher les informations dans les TextViews
        holder.jobTitle.setText(job.getTitle());
        holder.jobCompany.setText(job.getCompany());
        holder.jobLocation.setText(job.getLocation());
        holder.jobDate.setText(job.getDate());
        holder.jobDescription.setText(job.getDescription());
        holder.jobDomain.setText(job.getDomain());  // Affichage du domaine
        holder.jobType.setText(job.getType());      // Affichage du type

        // Action du bouton Modifier
        holder.editJobButton.setOnClickListener(v -> {
            // Passer les informations de l'offre à UpdateJobActivity
            Intent intent = new Intent(context, UpdateJobActivity.class);
            intent.putExtra("jobTitle", job.getTitle());
            intent.putExtra("jobCompany", job.getCompany());
            intent.putExtra("jobLocation", job.getLocation());
            intent.putExtra("jobDate", job.getDate());
            intent.putExtra("jobDescription", job.getDescription());
            intent.putExtra("jobDomain", job.getDomain());
            intent.putExtra("jobType", job.getType());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    // Méthode pour mettre à jour la liste des offres d'emploi
    public void updateJobList(List<Job> newJobList) {
        if (newJobList != null) {
            jobList.clear();
            jobList.addAll(newJobList);
            notifyDataSetChanged();  // Rafraîchir la vue
        }
    }

    // ViewHolder pour chaque item de la liste
    public static class JobViewHolder extends RecyclerView.ViewHolder {
        TextView jobTitle, jobCompany, jobLocation, jobDate, jobDescription, jobDomain, jobType;
        Button editJobButton;

        public JobViewHolder(View itemView) {
            super(itemView);
            jobTitle = itemView.findViewById(R.id.jobTitle);
            jobCompany = itemView.findViewById(R.id.jobCompany);
            jobLocation = itemView.findViewById(R.id.jobLocation);
            jobDate = itemView.findViewById(R.id.jobDate);
            jobDescription = itemView.findViewById(R.id.jobDescription);
            jobDomain = itemView.findViewById(R.id.jobDomain);
            jobType = itemView.findViewById(R.id.jobType);
            editJobButton = itemView.findViewById(R.id.editJobButton);
        }
    }
}
