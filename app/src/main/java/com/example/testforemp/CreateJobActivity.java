package com.example.testforemp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testforemp.Models.Job;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreateJobActivity extends AppCompatActivity {

    private EditText titleEditText, descriptionEditText, locationEditText, dateEditText, companyEditText, typeEditText, domainEditText;
    private Button createJobButton;
    private FirebaseFirestore db;
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_job);

        // Initialisation des EditText
        titleEditText = findViewById(R.id.jobTitleEditText);
        descriptionEditText = findViewById(R.id.jobDescriptionEditText);
        locationEditText = findViewById(R.id.jobLocationEditText);
        dateEditText = findViewById(R.id.jobDateEditText);
        companyEditText = findViewById(R.id.jobCompanyEditText);
        typeEditText = findViewById(R.id.jobTypeEditText);
        domainEditText = findViewById(R.id.jobDomainEditText); // Initialisation du champ domaine

        createJobButton = findViewById(R.id.saveJobButton);

        // Initialisation de Firestore
        db = FirebaseFirestore.getInstance();

        // Logique pour afficher le DatePickerDialog
        dateEditText.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            selectedYear = calendar.get(Calendar.YEAR);
            selectedMonth = calendar.get(Calendar.MONTH);
            selectedDay = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(CreateJobActivity.this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        selectedYear = year;
                        selectedMonth = monthOfYear;
                        selectedDay = dayOfMonth;
                        showTimePickerDialog(); // Afficher TimePicker après sélection de la date
                    }, selectedYear, selectedMonth, selectedDay);
            datePickerDialog.show();
        });

        // Logique pour la création d'une offre d'emploi
        createJobButton.setOnClickListener(v -> {
            // Récupérer les valeurs des champs de texte
            String title = titleEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            String location = locationEditText.getText().toString();
            String date = dateEditText.getText().toString();
            String company = companyEditText.getText().toString();
            String type = typeEditText.getText().toString();
            String domain = domainEditText.getText().toString(); // Récupération du domaine

            // Vérifier que tous les champs sont remplis
            if (title.isEmpty() || description.isEmpty() || location.isEmpty() || date.isEmpty() || company.isEmpty() || type.isEmpty() || domain.isEmpty()) {
                Toast.makeText(CreateJobActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            // Créer un objet Job avec les valeurs récupérées
            Job job = new Job(title, description, location, company, date, type, domain);

            // Ajouter l'offre dans Firestore
            db.collection("jobs")
                    .add(job)
                    .addOnSuccessListener(documentReference -> {
                        String jobId = documentReference.getId();
                        job.setId(jobId);
                        documentReference.update("id", jobId);
                        Toast.makeText(CreateJobActivity.this, "Offre d'emploi ajoutée avec succès. ID: " + jobId, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CreateJobActivity.this, RecruiterDashboardActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(CreateJobActivity.this, "Erreur lors de l'ajout de l'offre", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        selectedHour = calendar.get(Calendar.HOUR_OF_DAY);
        selectedMinute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(CreateJobActivity.this,
                (view, hourOfDay, minute) -> {
                    selectedHour = hourOfDay;
                    selectedMinute = minute;
                    updateDateTimeField();
                }, selectedHour, selectedMinute, true);
        timePickerDialog.show();
    }

    private void updateDateTimeField() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String formattedDate = sdf.format(calendar.getTime());
        dateEditText.setText(formattedDate);
    }
}
