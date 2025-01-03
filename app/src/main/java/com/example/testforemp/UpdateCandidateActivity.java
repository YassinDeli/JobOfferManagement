package com.example.testforemp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testforemp.Models.Cand;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UpdateCandidateActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etEmail, etPhoneNumber, etCity, etExperience, etSkills, etIdentityCardNumber, etBirthDate;
    private Button btnUpdateCandidate;
    private FirebaseFirestore db;
    private String candidateId;  // L'ID du candidat à mettre à jour

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_candidate);

        // Initialisation des éléments
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etCity = findViewById(R.id.etCity);
        etExperience = findViewById(R.id.etExperience);
        etSkills = findViewById(R.id.etSkills);
        etIdentityCardNumber = findViewById(R.id.etIdentityCardNumber);
        etBirthDate = findViewById(R.id.etBirthDate);  // Initialisation de la date de naissance
        btnUpdateCandidate = findViewById(R.id.btnUpdateCandidate);

        db = FirebaseFirestore.getInstance();

        // Récupérer l'ID du candidat à mettre à jour depuis l'intent
        candidateId = getIntent().getStringExtra("candidateId");

        // Charger les données du candidat existant
        loadCandidateData();

        btnUpdateCandidate.setOnClickListener(view -> updateCandidate());
    }

    private void loadCandidateData() {
        db.collection("candidates")
                .document(candidateId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Cand candidate = documentSnapshot.toObject(Cand.class);
                        etFirstName.setText(candidate.getFirstName());
                        etLastName.setText(candidate.getLastName());
                        etEmail.setText(candidate.getEmail());
                        etPhoneNumber.setText(candidate.getPhoneNumber());
                        etCity.setText(candidate.getCity());
                        etExperience.setText(candidate.getExperience());
                        etSkills.setText(String.join(", ", candidate.getSkills()));
                        etIdentityCardNumber.setText(candidate.getIdentityCardNumber());
                        etBirthDate.setText(candidate.getBirthDate());  // Affichage de la date de naissance
                    }
                });
    }

    private void updateCandidate() {
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String experience = etExperience.getText().toString().trim();
        String skillsString = etSkills.getText().toString().trim();  // Chaîne de compétences
        String identityCardNumber = etIdentityCardNumber.getText().toString().trim();
        String birthDate = etBirthDate.getText().toString().trim();  // Date de naissance

        // Convertir la chaîne skills en une liste
        List<String> skills = new ArrayList<>();
        if (!skillsString.isEmpty()) {
            skills = Arrays.asList(skillsString.split(",\\s*"));  // Séparer par des virgules et des espaces
        }

        // Mettre à jour l'objet Cand avec les nouvelles données
        Cand updatedCandidate = new Cand(candidateId, firstName, lastName, email, phoneNumber, birthDate, city, experience, skills, identityCardNumber);

        // Mettre à jour les données dans Firestore
        db.collection("candidates")
                .document(candidateId)
                .set(updatedCandidate)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(UpdateCandidateActivity.this, "Candidat mis à jour avec succès", Toast.LENGTH_SHORT).show();
                    finish();  // Retour à la page précédente
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(UpdateCandidateActivity.this, "Échec de la mise à jour", Toast.LENGTH_SHORT).show();
                });
    }

}
