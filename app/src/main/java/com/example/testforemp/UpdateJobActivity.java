package com.example.testforemp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testforemp.Models.Job;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UpdateJobActivity extends AppCompatActivity {

    private EditText titleEditText, descriptionEditText, locationEditText, companyEditText, dateEditText, editTextType, editTextDomain;
    private Button updateButton;
    private String jobTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_job);

        // Récupérer le titre de l'offre passé dans l'intent
        jobTitle = getIntent().getStringExtra("jobTitle");

        // Initialisation des EditText
        titleEditText = findViewById(R.id.editTextTitle);
        descriptionEditText = findViewById(R.id.editTextDescription);
        locationEditText = findViewById(R.id.editTextLocation);
        companyEditText = findViewById(R.id.editTextCompany);
        dateEditText = findViewById(R.id.editTextDate);
        editTextType = findViewById(R.id.editTextType);
        editTextDomain = findViewById(R.id.editTextDomain);

        updateButton = findViewById(R.id.buttonUpdate);

        // Charger les informations existantes de l'offre
        loadJobDetails(jobTitle);

        updateButton.setOnClickListener(v -> {
            // Mettre à jour l'offre d'emploi dans Firebase
            updateJobDetails(jobTitle, titleEditText.getText().toString(), descriptionEditText.getText().toString(),
                    locationEditText.getText().toString(), companyEditText.getText().toString(),
                    dateEditText.getText().toString(), editTextType.getText().toString(), editTextDomain.getText().toString());
        });
    }

    private void loadJobDetails(String jobTitle) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("jobs")
                .whereEqualTo("title", jobTitle)  // Rechercher l'offre par son titre
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                        Job job = document.toObject(Job.class);

                        // Remplir les champs avec les données récupérées
                        if (job != null) {
                            titleEditText.setText(job.getTitle());
                            descriptionEditText.setText(job.getDescription());
                            locationEditText.setText(job.getLocation());
                            companyEditText.setText(job.getCompany());
                            dateEditText.setText(job.getDate());
                            // Remplir les nouveaux champs
                            editTextType.setText(job.getType());  // Remplir avec le type
                            editTextDomain.setText(job.getDomain());  // Remplir avec le domaine
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(UpdateJobActivity.this, "Error loading job details", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateJobDetails(String jobTitle, String newTitle, String newDescription, String newLocation, String newCompany, String newDate, String newType, String newDomain) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("jobs")
                .whereEqualTo("title", jobTitle)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                        String jobId = document.getId();

                        // Mettre à jour l'offre dans Firebase
                        db.collection("jobs")
                                .document(jobId)
                                .update(
                                        "title", newTitle,
                                        "description", newDescription,
                                        "location", newLocation,
                                        "company", newCompany,
                                        "date", newDate,
                                        "type", newType,  // Ajouter le champ type
                                        "domain", newDomain  // Ajouter le champ domain
                                )
                                .addOnSuccessListener(aVoid -> {
                                    // Toast pour confirmation
                                    Toast.makeText(UpdateJobActivity.this, "Job updated successfully", Toast.LENGTH_SHORT).show();

                                    // Créer un Intent pour renvoyer les nouvelles données vers JobDetailsActivity
                                    Intent resultIntent = new Intent();
                                    resultIntent.putExtra("updatedTitle", newTitle);
                                    resultIntent.putExtra("updatedDescription", newDescription);
                                    resultIntent.putExtra("updatedLocation", newLocation);
                                    resultIntent.putExtra("updatedCompany", newCompany);
                                    resultIntent.putExtra("updatedDate", newDate);
                                    resultIntent.putExtra("updatedType", newType);  // Passer le type
                                    resultIntent.putExtra("updatedDomain", newDomain);  // Passer le domaine

                                    // Renvoyer les résultats à l'activité précédente
                                    setResult(RESULT_OK, resultIntent);
                                    finish();  // Fermer l'activité actuelle
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(UpdateJobActivity.this, "Error updating job", Toast.LENGTH_SHORT).show();
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(UpdateJobActivity.this, "Error finding job to update", Toast.LENGTH_SHORT).show();
                });
    }
}
