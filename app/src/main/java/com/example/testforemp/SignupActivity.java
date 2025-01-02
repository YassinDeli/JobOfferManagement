package com.example.testforemp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;
import java.util.Locale;
import com.example.testforemp.Models.User;
import com.example.testforemp.Utils.DataBase;

public class SignupActivity extends AppCompatActivity {

    private EditText firstNameEditText, lastNameEditText, emailEditText, phoneNumberEditText,
            birthDateEditText, passwordEditText;
    private Spinner nationalitySpinner, citySpinner;
    private Button signupButton, loginButton;
    private CheckBox recruiterCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialiser les vues
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        birthDateEditText = findViewById(R.id.birthDateEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signupButton = findViewById(R.id.signupButton);
        loginButton = findViewById(R.id.loginButton);
        citySpinner = findViewById(R.id.citySpinner);
        recruiterCheckBox = findViewById(R.id.recruiterCheckbox);

        // Lier le Spinner des villes
        ArrayAdapter<CharSequence> cityAdapter = ArrayAdapter.createFromResource(this,
                R.array.cities, android.R.layout.simple_spinner_item);
        cityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(cityAdapter);

        // Configurer le DatePicker pour la date de naissance
        birthDateEditText.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(SignupActivity.this, (view, selectedYear, selectedMonth, selectedDay) -> {
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(selectedYear, selectedMonth, selectedDay);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                birthDateEditText.setText(dateFormat.format(selectedDate.getTime()));
            }, year, month, day);

            datePickerDialog.show();
        });

        signupButton.setOnClickListener(view -> {
            String firstName = firstNameEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String phoneNumber = phoneNumberEditText.getText().toString();
            String birthDate = birthDateEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String selectedCity = citySpinner.getSelectedItem().toString();
            boolean isRecruiter = recruiterCheckBox.isChecked();

            // Validation des champs
            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || birthDate.isEmpty() || password.isEmpty()) {
                Toast.makeText(SignupActivity.this, "Tous les champs doivent être remplis.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidEmail(email)) {
                Toast.makeText(SignupActivity.this, "Email invalide.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidPhoneNumber(phoneNumber)) {
                Toast.makeText(SignupActivity.this, "Numéro de téléphone invalide.", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 8) {
                Toast.makeText(SignupActivity.this, "Le mot de passe doit contenir au moins 8 caractères.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Créer l'utilisateur
            User newUser = new User(firstName, lastName, email, phoneNumber, birthDate, password, selectedCity, isRecruiter);
            DataBase databaseHelper = new DataBase();
            databaseHelper.addUser(newUser);

            // Message de succès
            Toast.makeText(SignupActivity.this, "Compte créé avec succès.", Toast.LENGTH_SHORT).show();

            // Redirection vers la page de connexion
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    // Méthode pour valider l'email
    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Méthode pour valider le numéro de téléphone
    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.length() >= 10;
    }
}
