package com.example.testforemp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

public class CandidateDashboardActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView menuBurger;
    private RecyclerView jobListRecyclerView;
    private Spinner typeFilterSpinner, domainFilterSpinner;
    private CandidateJobOfferAdapter adapter;
    private List<Job> jobList = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_dashboard);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        menuBurger = findViewById(R.id.menuBurger);
        jobListRecyclerView = findViewById(R.id.jobListRecyclerView);
        typeFilterSpinner = findViewById(R.id.typeFilter);
        domainFilterSpinner = findViewById(R.id.domainFilter);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = FirebaseFirestore.getInstance();

        jobListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CandidateJobOfferAdapter(this, jobList);
        jobListRecyclerView.setAdapter(adapter);

        loadJobOffers(null, null);

        menuBurger.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_logout) {
                logout();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        setupFilters();

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

    private void setupFilters() {
        typeFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedType = position == 0 ? null : parent.getItemAtPosition(position).toString();
                String selectedDomain = domainFilterSpinner.getSelectedItem().toString();
                loadJobOffers(selectedType, selectedDomain);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                loadJobOffers(null, null);
            }
        });

        domainFilterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedDomain = position == 0 ? null : parent.getItemAtPosition(position).toString();
                String selectedType = typeFilterSpinner.getSelectedItem().toString();
                loadJobOffers(selectedType, selectedDomain);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                loadJobOffers(null, null);
            }
        });
    }

    private void loadJobOffers(String typeFilter, String domainFilter) {
        Query query = db.collection("jobs");

        if (typeFilter != null && !typeFilter.isEmpty()) {
            query = query.whereEqualTo("type", typeFilter);
        }

        if (domainFilter != null && !domainFilter.isEmpty()) {
            query = query.whereEqualTo("domain", domainFilter);
        }

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                jobList = task.getResult().toObjects(Job.class);
                adapter.updateJobList(jobList);
            } else {
                Toast.makeText(CandidateDashboardActivity.this, "Erreur lors du chargement des offres.", Toast.LENGTH_SHORT).show();
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
