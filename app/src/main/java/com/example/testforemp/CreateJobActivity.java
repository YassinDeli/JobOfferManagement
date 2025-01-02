package com.example.testforemp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.testforemp.Models.Job;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreateJobActivity extends AppCompatActivity {

    private EditText titleEditText, descriptionEditText, locationEditText, dateEditText, companyEditText;
    private Button createJobButton;
    private FirebaseFirestore db;
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_job);

        // Initialisation des EditText avec les bons IDs
        titleEditText = findViewById(R.id.jobTitleEditText);
        descriptionEditText = findViewById(R.id.jobDescriptionEditText);
        locationEditText = findViewById(R.id.jobLocationEditText);
        dateEditText = findViewById(R.id.jobDateEditText);
        companyEditText = findViewById(R.id.jobCompanyEditText);

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
                        showTimePickerDialog(); // Show time picker once the date is selected
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

            // Vérification des champs (ajoute une validation si nécessaire)
            if (title.isEmpty() || description.isEmpty() || location.isEmpty() || date.isEmpty() || company.isEmpty()) {
                Toast.makeText(CreateJobActivity.this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            // Créer un objet Job avec les valeurs récupérées
            Job job = new Job(title, company, location, description, date);  // Crée un Job avec toutes les informations

            // Ajouter l'offre d'emploi dans la base de données Firestore
            db.collection("jobs")
                    .add(job)  // Ajoute le job dans la collection "jobs"
                    .addOnSuccessListener(documentReference -> {
                        // Afficher un message de succès
                        Toast.makeText(CreateJobActivity.this, "Offre d'emploi ajoutée avec succès", Toast.LENGTH_SHORT).show();
                        // Rediriger vers le dashboard recruteur pour rafraîchir la liste des emplois
                        Intent intent = new Intent(CreateJobActivity.this, RecruiterDashboardActivity.class);
                        startActivity(intent);
                        finish();  // Ferme l'activité actuelle
                    })
                    .addOnFailureListener(e -> {
                        // Afficher un message d'erreur
                        Toast.makeText(CreateJobActivity.this, "Erreur lors de l'ajout de l'offre d'emploi", Toast.LENGTH_SHORT).show();
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
        // Formater la date et l'heure dans un format spécifique (ex: 01/01/2024 14:30)
        Calendar calendar = Calendar.getInstance();
        calendar.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String formattedDate = sdf.format(calendar.getTime());
        dateEditText.setText(formattedDate);
    }
}
