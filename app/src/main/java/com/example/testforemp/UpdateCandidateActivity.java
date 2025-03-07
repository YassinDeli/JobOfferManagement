package com.example.testforemp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testforemp.Models.Cand;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class UpdateCandidateActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etEmail, etPhoneNumber, etCity, etExperience, etSkills, etIdentityCardNumber, etBirthDate;
    private Button btnUpdateCandidate;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_candidate);

        // Initialisation des vues
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etCity = findViewById(R.id.etCity);
        etExperience = findViewById(R.id.etExperience);
        etSkills = findViewById(R.id.etSkills);
        etIdentityCardNumber = findViewById(R.id.etIdentityCardNumber);
        etBirthDate = findViewById(R.id.etBirthDate);
        btnUpdateCandidate = findViewById(R.id.btnUpdateCandidate);

        db = FirebaseFirestore.getInstance();

        // Récupérer les données passées via l'Intent
        loadCandidateData();

        // Ajouter un DatePicker pour le champ "Date de naissance"
        etBirthDate.setOnClickListener(v -> showDatePicker());

        btnUpdateCandidate.setOnClickListener(view -> updateCandidate());
    }

    private void loadCandidateData() {
        String identityCardNumber = getIntent().getStringExtra("identityCardNumber");
        if (identityCardNumber != null) {
            db.collection("candidature")
                    .whereEqualTo("identityCardNumber", identityCardNumber)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            Cand candidate = queryDocumentSnapshots.getDocuments().get(0).toObject(Cand.class);
                            if (candidate != null) {
                                etFirstName.setText(candidate.getFirstName());
                                etLastName.setText(candidate.getLastName());
                                etEmail.setText(candidate.getEmail());
                                etPhoneNumber.setText(candidate.getPhoneNumber());
                                etCity.setText(candidate.getCity());
                                etExperience.setText(candidate.getExperience());
                                etSkills.setText(String.join(", ", candidate.getSkills()));
                                etIdentityCardNumber.setText(candidate.getIdentityCardNumber());
                                etBirthDate.setText(candidate.getBirthDate());
                            }
                        }
                    })
                    .addOnFailureListener(e -> Toast.makeText(UpdateCandidateActivity.this, "Erreur lors de la récupération des données du candidat", Toast.LENGTH_SHORT).show());
        }
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
            etBirthDate.setText(selectedDate);
        }, year, month, day);

        datePickerDialog.show();
    }

    private void updateCandidate() {
        // Récupérer les nouvelles données saisies
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String experience = etExperience.getText().toString().trim();
        String skillsString = etSkills.getText().toString().trim();
        String identityCardNumber = etIdentityCardNumber.getText().toString().trim();
        String birthDate = etBirthDate.getText().toString().trim();

        List<String> skills = new ArrayList<>();
        if (!skillsString.isEmpty()) {
            skills = Arrays.asList(skillsString.split(",\\s*"));
        }

        // Créer un objet candidat mis à jour
        Cand updatedCandidate = new Cand(identityCardNumber, firstName, lastName, email, phoneNumber, birthDate, city, experience, skills, identityCardNumber);

        db.collection("candidature")
                .whereEqualTo("identityCardNumber", identityCardNumber)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        String documentId = queryDocumentSnapshots.getDocuments().get(0).getId();

                        // Mettre à jour le document dans Firestore
                        db.collection("candidature")
                                .document(documentId)
                                .set(updatedCandidate)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(this, "Candidat mis à jour avec succès", Toast.LENGTH_SHORT).show();
                                    finish();
                                })
                                .addOnFailureListener(e -> Toast.makeText(this, "Erreur lors de la mise à jour", Toast.LENGTH_SHORT).show());
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Erreur lors de la récupération du document", Toast.LENGTH_SHORT).show());
    }
}
