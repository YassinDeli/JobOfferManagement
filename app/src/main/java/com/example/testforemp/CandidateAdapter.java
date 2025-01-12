package com.example.testforemp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testforemp.Models.Postuler;

import java.util.List;

public class CandidateAdapter extends RecyclerView.Adapter<CandidateAdapter.CandidateViewHolder> {

    private List<Postuler> candidateList;
    private final Context context;

    public CandidateAdapter(Context context, List<Postuler> candidateList) {
        this.context = context;
        this.candidateList = candidateList;
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

    public static class CandidateViewHolder extends RecyclerView.ViewHolder {
        TextView candidateName, candidateEmail, candidatePhone;

        public CandidateViewHolder(View itemView) {
            super(itemView);
            candidateName = itemView.findViewById(R.id.candidateName);
            candidateEmail = itemView.findViewById(R.id.candidateEmail);
            candidatePhone = itemView.findViewById(R.id.candidatePhone);
        }
    }
}
