package com.example.testforemp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testforemp.Models.Cand;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateCandidateActivity extends AppCompatActivity {

    private EditText etFirstName, etLastName, etEmail, etPhoneNumber, etBirthDate, etCity, etExperience, etSkills, etIdentityCardNumber;
    private Button btnSubmitCandidate;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_candidate);

        // Initialisation des vues
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etBirthDate = findViewById(R.id.etBirthDate);
        etCity = findViewById(R.id.etCity);
        etExperience = findViewById(R.id.etExperience);
        etSkills = findViewById(R.id.etSkills);
        etIdentityCardNumber = findViewById(R.id.etIdentityCardNumber);
        btnSubmitCandidate = findViewById(R.id.btnSubmitCandidate);

        // Initialisation de Firestore
        db = FirebaseFirestore.getInstance();

        // Gestion du clic sur le bouton
        btnSubmitCandidate.setOnClickListener(v -> submitCandidateForm());

        // Ouvrir un calendrier lors du clic sur le champ de date
        etBirthDate.setOnClickListener(v -> openDatePicker());
    }

    private void openDatePicker() {
        // Utilisation du DatePickerDialog pour sélectionner une date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            // Format de la date : JJ/MM/AAAA
            String selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
            etBirthDate.setText(selectedDate);
        }, year, month, day);

        datePickerDialog.show();
    }

    private void submitCandidateForm() {
        // Récupération des informations saisies
        String firstName = etFirstName.getText().toString().trim();
        String lastName = etLastName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phoneNumber = etPhoneNumber.getText().toString().trim();
        String birthDate = etBirthDate.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String experience = etExperience.getText().toString().trim();
        String skillsInput = etSkills.getText().toString().trim();
        String identityCardNumber = etIdentityCardNumber.getText().toString().trim();

        // Validation des champs
        if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(birthDate) || TextUtils.isEmpty(city) ||
                TextUtils.isEmpty(experience) || TextUtils.isEmpty(skillsInput) || TextUtils.isEmpty(identityCardNumber)) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        // Transformation des compétences en liste
        List<String> skillsList = Arrays.asList(skillsInput.split(","));

        // Création de l'objet candidat avec le numéro de carte d'identité
        Cand candidate = new Cand(null, firstName, lastName, email, phoneNumber, birthDate, city, experience, skillsList, identityCardNumber);

        // Enregistrement du candidat dans Firestore
        db.collection("candidature")
                .add(toMap(candidate))  // Conversion de l'objet candidat en Map
                .addOnSuccessListener(documentReference -> {
                    // Récupération de l'ID généré par Firestore
                    String id = documentReference.getId();
                    // Mise à jour de l'ID dans l'objet candidat
                    candidate.setId(id);
                    Log.d("Firestore", "Candidat ajouté avec ID : " + id);
                    Toast.makeText(CreateCandidateActivity.this, "Candidat ajouté avec succès", Toast.LENGTH_SHORT).show();
                    finish(); // Fermer l'activité après l'ajout
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Erreur lors de l'ajout du candidat : " + e.getMessage());
                    Toast.makeText(CreateCandidateActivity.this, "Erreur lors de l'ajout", Toast.LENGTH_SHORT).show();
                });
    }

    // Méthode pour convertir l'objet Cand en Map afin d'enregistrer dans Firestore
    private Map<String, Object> toMap(Cand candidate) {
        Map<String, Object> candidateMap = new HashMap<>();
        candidateMap.put("firstName", candidate.getFirstName());
        candidateMap.put("lastName", candidate.getLastName());
        candidateMap.put("email", candidate.getEmail());
        candidateMap.put("phoneNumber", candidate.getPhoneNumber());
        candidateMap.put("birthDate", candidate.getBirthDate());
        candidateMap.put("city", candidate.getCity());
        candidateMap.put("experience", candidate.getExperience());
        candidateMap.put("skills", candidate.getSkills());
        candidateMap.put("identityCardNumber", candidate.getIdentityCardNumber()); // Ajouter le numéro de carte d'identité
        return candidateMap;
    }
}
