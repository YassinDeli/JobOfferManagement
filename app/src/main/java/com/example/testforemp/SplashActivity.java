package com.example.testforemp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);  // Charge l'écran de splash avec le logo

        // Utilisation de Handler pour attendre 3 secondes avant de passer à l'écran de connexion
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Lancer l'activité de login après 3 secondes
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Empêche le retour à l'écran de splash
            }
        }, 1000); // 3000 millisecondes = 3 secondes
    }
}
