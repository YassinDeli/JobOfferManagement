package com.example.testforemp.Models;

import android.app.Application;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.FirebaseApp;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialisation de Firebase
        FirebaseApp.initializeApp(this);

        // Initialisation de Firestore (la persistance hors ligne est activée par défaut)
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Initialisation de Firebase Realtime Database (avec persistance activée par défaut)
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);

        // Initialisation de l'authentification Firebase
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // Vérification de l'utilisateur actuel
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            // L'utilisateur est connecté, vous pouvez faire quelque chose ici
        } else {
            // Aucune connexion, l'utilisateur peut être redirigé vers l'écran de connexion
        }
    }
}
