package com.example.testforemp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.testforemp.Models.Job;
import com.google.firebase.firestore.FirebaseFirestore;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UpdateJobActivity extends AppCompatActivity {

    private EditText titleEditText, descriptionEditText, locationEditText, dateEditText, companyEditText;
    private Button updateJobButton;
    private FirebaseFirestore db;
    private String jobId;

    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_job);

        // Initialize fields
        titleEditText = findViewById(R.id.jobTitleEditText);
        descriptionEditText = findViewById(R.id.jobDescriptionEditText);
        locationEditText = findViewById(R.id.jobLocationEditText);
        dateEditText = findViewById(R.id.jobDateEditText);
        companyEditText = findViewById(R.id.jobCompanyEditText);
        updateJobButton = findViewById(R.id.updateJobButton);

        db = FirebaseFirestore.getInstance();

        // Get job details passed from the previous activity
        jobId = getIntent().getStringExtra("jobId");
        titleEditText.setText(getIntent().getStringExtra("title"));
        descriptionEditText.setText(getIntent().getStringExtra("description"));
        locationEditText.setText(getIntent().getStringExtra("location"));
        dateEditText.setText(getIntent().getStringExtra("date"));
        companyEditText.setText(getIntent().getStringExtra("company"));

        // Handle date and time picker
        dateEditText.setOnClickListener(v -> showDatePicker());

        // Update job logic
        updateJobButton.setOnClickListener(v -> {
            String title = titleEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            String location = locationEditText.getText().toString();
            String date = dateEditText.getText().toString();
            String company = companyEditText.getText().toString();

            if (title.isEmpty() || description.isEmpty() || location.isEmpty() || date.isEmpty() || company.isEmpty()) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            Job updatedJob = new Job(title, company, location, description, date);
            db.collection("jobs").document(jobId)
                    .set(updatedJob)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Offre mise à jour avec succès", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Erreur lors de la mise à jour", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            selectedYear = year;
            selectedMonth = month;
            selectedDay = dayOfMonth;
            showTimePicker();
        }, selectedYear, selectedMonth, selectedDay);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        selectedHour = calendar.get(Calendar.HOUR_OF_DAY);
        selectedMinute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minute) -> {
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
        dateEditText.setText(sdf.format(calendar.getTime()));
    }
}
