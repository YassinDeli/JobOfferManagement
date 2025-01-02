package com.example.testforemp;

import static com.example.testforemp.R.id.signupButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testforemp.Utils.DataBase;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton, signupButton;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialisation des vues
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);

        // Action pour le bouton de connexion
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Validation des champs
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Tous les champs doivent être remplis.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validation de l'email
                if (!isValidEmail(email)) {
                    Toast.makeText(LoginActivity.this, "E-mail invalide.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Vérification de l'utilisateur dans Firestore
                DataBase database = new DataBase();
                database.checkUser(email, password, new DataBase.LoginCallback() {
                    @Override
                    public void onLoginSuccess(boolean isRecruiter) {
                        Toast.makeText(LoginActivity.this, "Connexion réussie.", Toast.LENGTH_SHORT).show();
                        Log.d("LoginActivity", "Redirection réussie : " + (isRecruiter ? "Recruteur" : "Candidat"));
                        // Rediriger vers l'écran principal en fonction du type d'utilisateur
                        Intent intent;
                        if (isRecruiter) {
                            intent = new Intent(LoginActivity.this, RecruiterDashboardActivity.class); // Par exemple, écran recruteur
                        } else {
                            intent = new Intent(LoginActivity.this, CandidateDashboardActivity.class); // Par exemple, écran candidat
                        }
                        startActivity(intent);
                        finish();  // Empêche de revenir à la page de login
                    }

                    @Override
                    public void onLoginFailure(String message) {
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Action pour le bouton de création de compte
        signupButton.setOnClickListener(view -> {
            // Rediriger vers l'écran d'inscription
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }

    // Méthode pour valider l'email
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        return email.matches(emailPattern);
    }
}
