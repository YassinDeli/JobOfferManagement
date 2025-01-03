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
import com.example.testforemp.R;
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

        // Configuration de la Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Direction vers l'activité de création de candidat
        Button btnCreateCandidate = findViewById(R.id.createCandidateButton);
        btnCreateCandidate.setOnClickListener(v -> {
            Intent intent = new Intent(CandidateDashboardActivity.this, CreateCandidateActivity.class);
            startActivity(intent);
        });

        // Direction vers l'activité de mise à jour de candidat
        // Récupérer ici l'identityCardNumber (qui est l'ID du candidat)
        String identityCardNumber = "example_id_123";  // Remplacer par l'ID réel du candidat

        // Passer l'ID (identityCardNumber) à l'activité de mise à jour
        Intent intent = new Intent(CandidateDashboardActivity.this, UpdateCandidateActivity.class);
        intent.putExtra("candidateId", identityCardNumber);
        startActivity(intent);


        // Action sur le menu hamburger
        menuBurger.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        // Gestion des éléments du menu latéral
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_logout) {
                logout(); // Appeler la méthode de déconnexion
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void logout() {
        // Logique de déconnexion
        Toast.makeText(this, "Déconnexion réussie", Toast.LENGTH_SHORT).show();

        // Rediriger vers l'écran de connexion
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
