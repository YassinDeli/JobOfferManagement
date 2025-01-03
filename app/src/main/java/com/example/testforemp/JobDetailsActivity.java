package com.example.testforemp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class JobDetailsActivity extends AppCompatActivity {

    private TextView titleTextView, companyTextView, locationTextView, dateTextView, domainTextView, typeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        // Initialisation des TextViews
        titleTextView = findViewById(R.id.jobTitleDetail);
        companyTextView = findViewById(R.id.jobCompanyDetail);
        locationTextView = findViewById(R.id.jobLocationDetail);
        dateTextView = findViewById(R.id.jobDateDetail);  // Correction du nom pour la date
        domainTextView = findViewById(R.id.jobDomainDetail);  // Correction du nom pour le domaine
        typeTextView = findViewById(R.id.jobTypeDetail);  // Correction du nom pour le type

        // Récupérer les données passées depuis l'intention
        Intent intent = getIntent();
        String title = intent.getStringExtra("jobTitle");
        String company = intent.getStringExtra("jobCompany");
        String location = intent.getStringExtra("jobLocation");
        String datePosted = intent.getStringExtra("jobDatePosted");
        String domain = intent.getStringExtra("jobDomain");  // Correction du nom de la clé
        String type = intent.getStringExtra("jobType");

        // Afficher les données dans les TextViews
        titleTextView.setText(title);
        companyTextView.setText(company);
        locationTextView.setText(location);
        dateTextView.setText(datePosted);
        domainTextView.setText(domain);  // Affichage du domaine
        typeTextView.setText(type);      // Affichage du type
    }

    // Méthode pour récupérer les résultats d'UpdateJobActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // Récupérer les données mises à jour
            String updatedTitle = data.getStringExtra("updatedTitle");
            String updatedDescription = data.getStringExtra("updatedDescription");
            String updatedLocation = data.getStringExtra("updatedLocation");
            String updatedCompany = data.getStringExtra("updatedCompany");
            String updatedDate = data.getStringExtra("updatedDate");
            String updatedType = data.getStringExtra("updatedType");
            String updatedDomain = data.getStringExtra("updatedDomain");

            // Mettre à jour les TextViews avec les nouvelles données
            titleTextView.setText(updatedTitle);
            companyTextView.setText(updatedCompany);
            locationTextView.setText(updatedLocation);
            dateTextView.setText(updatedDate);
            domainTextView.setText(updatedDomain);  // Affichage du domaine
            typeTextView.setText(updatedType);      // Affichage du type
        }
    }
}
