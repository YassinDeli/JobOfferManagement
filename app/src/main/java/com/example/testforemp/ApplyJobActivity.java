package com.example.testforemp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testforemp.Models.Postuler;
import com.google.firebase.firestore.FirebaseFirestore;
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

        jobId = getIntent().getStringExtra("jobId");

        btnUploadFile.setOnClickListener(v -> openFileChooser());

        btnSubmit.setOnClickListener(v -> {
            String name = etName.getText().toString();
            String surname = etSurname.getText().toString();
            String phone = etPhone.getText().toString();
            String description = etDescription.getText().toString();

            if (fileUri != null) {
                uploadFile(fileUri, name, surname, phone, description);
            } else {
                submitInformation(name, surname, phone, description, null);
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
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

    private void uploadFile(Uri fileUri, String name, String surname, String phone, String description) {
        StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(fileUri));
        fileReference.putFile(fileUri)
                .addOnSuccessListener(taskSnapshot -> {
                    fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String fileUrl = uri.toString();
                        submitInformation(name, surname, phone, description, fileUrl);
                    });
                })
                .addOnFailureListener(e -> {
                    Log.e("FirebaseError", "Erreur lors du téléchargement du fichier", e);
                    Toast.makeText(ApplyJobActivity.this, "Échec du téléchargement du fichier.", Toast.LENGTH_SHORT).show();
                });
    }

    private void submitInformation(String name, String surname, String phone, String description, String fileUrl) {
        Postuler postuler = new Postuler(name, surname, phone, description, fileUrl, jobId);

        db.collection("postuler")
                .add(postuler)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(ApplyJobActivity.this, "Informations soumises avec succès!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e("FirebaseError", "Erreur lors de l'ajout des informations à Firestore", e);
                    Toast.makeText(ApplyJobActivity.this, "Échec de la soumission des informations.", Toast.LENGTH_SHORT).show();
                });
    }

    private String getFileExtension(Uri uri) {
        String filePath = uri.getPath();
        return filePath != null ? filePath.substring(filePath.lastIndexOf(".") + 1) : null;
    }
}
