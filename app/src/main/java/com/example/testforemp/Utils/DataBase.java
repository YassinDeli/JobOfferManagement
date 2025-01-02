package com.example.testforemp.Utils;

import android.util.Log;
import android.widget.Toast;

import com.example.testforemp.Models.Job;
import com.example.testforemp.Models.User;
import com.example.testforemp.RecruiterDashboardActivity;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class DataBase {

    private FirebaseFirestore db;

    public DataBase() {
        db = FirebaseFirestore.getInstance();
    }

    // Méthode pour ajouter un utilisateur avec rôle (isRecruiter)
    public void addUser(User user) {
        // Vérification et définition de la valeur par défaut pour 'isRecruiter'
        if (user.getIsRecruiter() == null) {
            user.setIsRecruiter(false); // Valeur par défaut : non recruteur
        }

        // Ajout de l'utilisateur à la collection Firestore
        db.collection("users")
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    Log.d("Firebase", "Utilisateur ajouté avec l'ID : " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
                });
    }

    // Vérification des utilisateurs pour la connexion
    public void checkUser(String email, String password, LoginCallback callback) {
        db.collection("users")
                .whereEqualTo("email", email)
                .whereEqualTo("password", password)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);

                        // Gestion du champ isRecruiter avec une valeur par défaut
                        Boolean isRecruiterObj = document.getBoolean("isRecruiter");
                        if (isRecruiterObj != null) {
                            boolean isRecruiter = isRecruiterObj;
                            Log.d("Firebase", "Utilisateur trouvé : " + document.getData());
                            callback.onLoginSuccess(isRecruiter); // Redirection selon le rôle de l'utilisateur
                        } else {
                            // Définir une valeur par défaut pour isRecruiter
                            boolean defaultIsRecruiter = false; // Par exemple, false pour les candidats
                            Log.d("Firebase", "Champ 'isRecruiter' manquant ou null. Utilisation de la valeur par défaut : " + defaultIsRecruiter);
                            callback.onLoginSuccess(defaultIsRecruiter);
                        }
                    } else {
                        Log.d("Firebase", "Aucun utilisateur trouvé pour cet email/mot de passe.");
                        callback.onLoginFailure("Email ou mot de passe incorrect.");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Erreur Firestore : " + e.getMessage());
                    callback.onLoginFailure("Erreur : " + e.getMessage());
                });
    }

    // Méthode pour récupérer toutes les offres d'emploi depuis Firestore
    public void getAllJobs(JobListCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("jobs")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        List<Job> jobList = new ArrayList<>();
                        for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                            Job job = document.toObject(Job.class);
                            if (job != null) {
                                jobList.add(job);
                            }
                        }
                        Log.d("FirebaseSuccess", "Offres récupérées : " + jobList.size());
                        callback.onJobsRetrieved(jobList);
                    } else {
                        Log.d("FirebaseSuccess", "Aucune offre d'emploi trouvée.");
                        callback.onJobsRetrieved(new ArrayList<>());
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("FirebaseError", "Erreur lors de la récupération des offres", e);

                    callback.onJobsRetrieved(new ArrayList<>());
                });
    }

    // Interface pour récupérer les offres d'emploi de manière asynchrone
    public interface JobListCallback {
        void onJobsRetrieved(List<Job> jobList);

        void onFailure(Exception e);
    }

    public interface LoginCallback {
        void onLoginSuccess(boolean isRecruiter);
        void onLoginFailure(String message);
    }
}
