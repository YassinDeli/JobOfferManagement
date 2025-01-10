package com.example.testforemp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class ApplyJobActivity extends AppCompatActivity {

    private static final int PICK_FILE = 2;
    private EditText etName, etSurname, etPhone, etDescription;
    private Button btnSubmit, btnUploadFile;
    private TextView tvFilePath;
    private Uri fileUri;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseFirestore db;
    private String jobId;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_job);

        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        etPhone = findViewById(R.id.etPhone);
        etDescription = findViewById(R.id.etDescription);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnUploadFile = findViewById(R.id.btnUploadFile);
        tvFilePath = findViewById(R.id.tvFilePath);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        db = FirebaseFirestore.getInstance();

        // Récupérer l'ID de l'offre d'emploi et l'ID de l'utilisateur depuis l'intent
        jobId = getIntent().getStringExtra("jobId");
        userId = getIntent().getStringExtra("userId");

        btnUploadFile.setOnClickListener(v -> openFileChooser());

        btnSubmit.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String surname = etSurname.getText().toString();
            String phone = etPhone.getText().toString();
            String description = etDescription.getText().toString();

            checkIfAlreadyApplied(name, surname, phone, description);
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Permet de sélectionner tous types de fichiers
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), PICK_FILE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            fileUri = data.getData();
            tvFilePath.setText(fileUri.toString());
        }
    }

    private void checkIfAlreadyApplied(String name, String surname, String phone, String description) {
        db.collection("postuler")
                .whereEqualTo("jobId", jobId)
                .whereEqualTo("userId", userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            Toast.makeText(ApplyJobActivity.this, "Vous avez déjà postulé pour cette offre.", Toast.LENGTH_SHORT).show();
                        } else {
                            if (fileUri != null) {
                                uploadFile(fileUri, name, surname, phone, description);
                            } else {
                                saveApplicationData(name, surname, phone, description, null);
                            }
                        }
                    } else {
                        Log.e("FirebaseError", "Erreur lors de la vérification de la candidature", task.getException());
                        Toast.makeText(ApplyJobActivity.this, "Erreur lors de la vérification de la candidature.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void uploadFile(Uri fileUri, String name, String surname, String phone, String description) {
        StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(fileUri));
        fileReference.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> {
                    fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String fileUrl = uri.toString();
                        saveApplicationData(name, surname, phone, description, fileUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    Log.e("FirebaseError", "Erreur lors du téléchargement du fichier", e);
                    Toast.makeText(ApplyJobActivity.this, "Échec du téléchargement du fichier.", Toast.LENGTH_SHORT).show();
                });
    }

    private void saveApplicationData(String name, String surname, String phone, String description, String fileUrl) {
        Map<String, Object> applicationData = new HashMap<>();
        applicationData.put("name", name);
        applicationData.put("surname", surname);
        applicationData.put("phone", phone);
        applicationData.put("description", description);
        applicationData.put("jobId", jobId);
        applicationData.put("userId", userId);
        if (fileUrl != null) {
            applicationData.put("fileUrl", fileUrl);
        }

        db.collection("postuler")
                .add(applicationData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(ApplyJobActivity.this, "Candidature soumise avec succès!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e("FirebaseError", "Erreur lors de l'ajout de la candidature", e);
                    Toast.makeText(ApplyJobActivity.this, "Échec de la soumission de la candidature.", Toast.LENGTH_SHORT).show();
                });
    }

    private String getFileExtension(Uri uri) {
        String filePath = uri.getPath();
        return filePath != null ? filePath.substring(filePath.lastIndexOf(".") + 1) : null;
    }
}
