package com.example.testforemp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testforemp.Models.Job;
import com.example.testforemp.Utils.DataBase;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class RecruiterDashboardActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private JobOfferAdapter jobListAdapter;
    private Button createJobButton;
    private DataBase databaseHelper;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruiter_dashboard);

        // Initialisation du RecyclerView et de l'adaptateur
        recyclerView = findViewById(R.id.jobListRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialisation de l'objet DataBase
        databaseHelper = new DataBase();

        // Initialisation de Firebase
        db = FirebaseFirestore.getInstance();

        // Initialisation de l'adaptateur avec une liste vide au début
        jobListAdapter = new JobOfferAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(jobListAdapter);

        // Récupérer les offres d'emploi depuis Firebase
        databaseHelper.getAllJobs(new DataBase.JobListCallback() {
            @Override
            public void onJobsRetrieved(List<Job> jobList) {
                Log.d("Firebase", "Nombre d'offres récupérées : " + jobList.size());
                if (jobList == null || jobList.isEmpty()) {
                    Toast.makeText(RecruiterDashboardActivity.this, "Aucune offre d'emploi disponible.", Toast.LENGTH_SHORT).show();
                } else {
                    jobListAdapter.updateJobList(jobList);
                    Toast.makeText(RecruiterDashboardActivity.this, "Offres d'emploi récupérées.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Exception e) {
                // En cas d'erreur
                Log.e("Firebase", "Erreur lors de la récupération des offres : " + e.getMessage());
                Toast.makeText(RecruiterDashboardActivity.this, "Erreur lors de la récupération des offres.", Toast.LENGTH_SHORT).show();
            }
        });

        // Ajouter un listener en temps réel pour les mises à jour dans Firestore
        db.collection("jobs").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable com.google.firebase.firestore.FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("RecruiterDashboard", "Listen failed.", error);
                    return;
                }
                List<Job> jobList = value.toObjects(Job.class);
                jobListAdapter.updateJobList(jobList); // Met à jour la liste avec les nouvelles données
            }
        });

        // Initialisation de la Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Affichage du bouton hamburger
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.menuhumb);
            Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 48, 48, false);
            getSupportActionBar().setHomeAsUpIndicator(new BitmapDrawable(getResources(), scaledBitmap));
        }

        // Configuration du tiroir de navigation (Drawer)
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);

        // Listener pour la sélection des éléments dans le menu de navigation
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            if (id == R.id.nav_profile) {
                startActivity(new Intent(RecruiterDashboardActivity.this, ProfileSettingsActivity.class));
            } else if (id == R.id.nav_logout) {
                startActivity(new Intent(RecruiterDashboardActivity.this, LoginActivity.class));
                finish();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // Bouton pour créer une nouvelle offre d'emploi
        createJobButton = findViewById(R.id.createJobButton);
        createJobButton.setOnClickListener(v -> {
            Intent intent = new Intent(RecruiterDashboardActivity.this, CreateJobActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
