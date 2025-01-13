package com.example.testforemp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testforemp.Models.Postuler;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CandidateAdapter extends RecyclerView.Adapter<CandidateAdapter.CandidateViewHolder> {

    private List<Postuler> candidateList;
    private final Context context;
    private FirebaseFirestore db;

    public CandidateAdapter(Context context, List<Postuler> candidateList) {
        this.context = context;
        this.candidateList = candidateList;
        this.db = FirebaseFirestore.getInstance();
    }

    @Override
    public CandidateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_candidate, parent, false);
        return new CandidateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CandidateViewHolder holder, int position) {
        Postuler candidate = candidateList.get(position);
        holder.candidateName.setText(candidate.getName() + " " + candidate.getSurname());
        holder.candidateEmail.setText(candidate.getPhone());
        holder.candidatePhone.setText(candidate.getDescription());

        // Mettre à jour l'icône de statut
        if ("accepted".equals(candidate.getStatus())) {
            holder.statusIcon.setImageResource(R.drawable.accept); // Remplacez par l'icône d'acceptation appropriée
        } else if ("refused".equals(candidate.getStatus())) {
            holder.statusIcon.setImageResource(R.drawable.refuse); // Remplacez par l'icône de refus appropriée
        } else {
            holder.statusIcon.setImageResource(R.drawable.att); // Remplacez par l'icône de statut en attente appropriée
        }

        holder.acceptButton.setOnClickListener(v -> acceptCandidate(candidate));
        holder.refuseButton.setOnClickListener(v -> refuseCandidate(candidate));
    }

    @Override
    public int getItemCount() {
        return candidateList.size();
    }

    public void updateCandidateList(List<Postuler> newCandidateList) {
        if (newCandidateList != null) {
            candidateList.clear();
            candidateList.addAll(newCandidateList);
            notifyDataSetChanged();
        }
    }

    private void acceptCandidate(Postuler candidate) {
        candidate.setStatus("accepted");
        db.collection("postuler").document(candidate.getId())
                .update("status", "accepted")
                .addOnSuccessListener(aVoid -> {
                    Log.d("CandidateAdapter", "Candidature acceptée avec succès");
                    Toast.makeText(context, "Candidature acceptée", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("CandidateAdapter", "Erreur lors de l'acceptation de la candidature", e);
                    Toast.makeText(context, "Erreur lors de l'acceptation de la candidature", Toast.LENGTH_SHORT).show();
                });
    }

    private void refuseCandidate(Postuler candidate) {
        candidate.setStatus("refused");
        db.collection("postuler").document(candidate.getId())
                .update("status", "refused")
                .addOnSuccessListener(aVoid -> {
                    Log.d("CandidateAdapter", "Candidature refusée avec succès");
                    Toast.makeText(context, "Candidature refusée", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("CandidateAdapter", "Erreur lors du refus de la candidature", e);
                    Toast.makeText(context, "Erreur lors du refus de la candidature", Toast.LENGTH_SHORT).show();
                });
    }

    public static class CandidateViewHolder extends RecyclerView.ViewHolder {
        TextView candidateName, candidateEmail, candidatePhone;
        Button acceptButton, refuseButton;
        ImageView statusIcon;

        public CandidateViewHolder(View itemView) {
            super(itemView);
            candidateName = itemView.findViewById(R.id.candidateName);
            candidateEmail = itemView.findViewById(R.id.candidateEmail);
            candidatePhone = itemView.findViewById(R.id.candidatePhone);
            acceptButton = itemView.findViewById(R.id.acceptButton);
            refuseButton = itemView.findViewById(R.id.refuseButton);
            statusIcon = itemView.findViewById(R.id.statusIcon);
        }
    }
}
