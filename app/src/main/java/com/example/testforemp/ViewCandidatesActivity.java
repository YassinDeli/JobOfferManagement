package com.example.testforemp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testforemp.Models.Postuler;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ViewCandidatesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CandidateAdapter candidateAdapter;
    private FirebaseFirestore db;
    private String jobId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_candidates);

        recyclerView = findViewById(R.id.candidateListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();

        jobId = getIntent().getStringExtra("jobId");

        candidateAdapter = new CandidateAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(candidateAdapter);

        loadCandidates();
    }

    private void loadCandidates() {
        db.collection("postuler")
                .whereEqualTo("jobId", jobId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<Postuler> candidates = task.getResult().toObjects(Postuler.class);
                        candidateAdapter.updateCandidateList(candidates);
                    } else {
                        Toast.makeText(ViewCandidatesActivity.this, "Erreur lors du chargement des candidatures.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
