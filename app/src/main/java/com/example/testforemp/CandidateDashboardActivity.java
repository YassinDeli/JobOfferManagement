package com.example.testforemp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testforemp.Models.Job;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class CandidateDashboardActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView menuBurger;
    private RecyclerView jobListRecyclerView;
    private JobOfferAdapter adapter;
    private List<Job> jobList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_dashboard);

        // Initialisation des vues
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        menuBurger = findViewById(R.id.menuBurger);
        jobListRecyclerView = findViewById(R.id.jobListRecyclerView);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Initialisation de Firebase
        db = FirebaseFirestore.getInstance();

        // Configuration du RecyclerView
        jobListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new JobOfferAdapter(this, jobList);
        jobListRecyclerView.setAdapter(adapter);

        // Chargement des offres d'emploi
        loadJobOffers();

        // Gestion du menu hamburger
        menuBurger.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_logout) {
                logout();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // Boutons pour gérer les candidats
        Button btnCreateCandidate = findViewById(R.id.createCandidateButton);
        btnCreateCandidate.setOnClickListener(v -> {
            Intent intent = new Intent(CandidateDashboardActivity.this, CreateCandidateActivity.class);
            startActivity(intent);
        });

        Button btnUpdateCandidate = findViewById(R.id.editCandidateButton);
        btnUpdateCandidate.setOnClickListener(v -> {
            String candidateId = "example_id_123"; // Remplacer par un ID réel
            Intent intent = new Intent(CandidateDashboardActivity.this, UpdateCandidateActivity.class);
            intent.putExtra("candidateId", candidateId);
            startActivity(intent);
        });
    }

    private void loadJobOffers() {
        db.collection("jobs").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable com.google.firebase.firestore.FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(CandidateDashboardActivity.this, "Erreur lors du chargement des offres.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (value != null) {
                    List<Job> jobList = value.toObjects(Job.class);
                    adapter.updateJobList(jobList); // Met à jour la liste avec les nouvelles données
                }
            }
        });
    }

    private void logout() {
        Toast.makeText(this, "Déconnexion réussie", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(CandidateDashboardActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
