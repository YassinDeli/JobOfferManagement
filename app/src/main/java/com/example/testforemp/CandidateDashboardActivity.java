package com.example.testforemp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class CandidateDashboardActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView menuBurger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_dashboard);

        // Initialisation des vues
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationView);
        menuBurger = findViewById(R.id.menuBurger);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Bouton pour créer un candidat
        Button btnCreateCandidate = findViewById(R.id.createCandidateButton);
        btnCreateCandidate.setOnClickListener(v -> {
            Intent intent = new Intent(CandidateDashboardActivity.this, CreateCandidateActivity.class);
            startActivity(intent);
        });

        // Bouton pour mettre à jour un candidat
        Button btnUpdateCandidate = findViewById(R.id.editCandidateButton); // Assurez-vous que ce bouton existe
        btnUpdateCandidate.setOnClickListener(v -> {
            String candidateId = "example_id_123"; // Remplacer par un ID réel ou dynamique
            Intent intent = new Intent(CandidateDashboardActivity.this, UpdateCandidateActivity.class);
            intent.putExtra("candidateId", candidateId);
            startActivity(intent);
        });

        menuBurger.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_logout) {
                logout();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
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
