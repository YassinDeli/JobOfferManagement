package com.example.testforemp;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public class ViewResultActivity extends AppCompatActivity {

    private TextView resultTextView;
    private FirebaseFirestore db;
    private String jobId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_result);

        resultTextView = findViewById(R.id.resultTextView);
        db = FirebaseFirestore.getInstance();

        jobId = getIntent().getStringExtra("jobId");

        loadResult();
    }

    private void loadResult() {
        db.collection("postuler")
                .whereEqualTo("jobId", jobId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        if (!task.getResult().isEmpty()) {
                            String status = task.getResult().getDocuments().get(0).getString("status");
                            if ("accepted".equals(status)) {
                                resultTextView.setText("Votre candidature a été acceptée.");
                            } else if ("refused".equals(status)) {
                                resultTextView.setText("Votre candidature a été refusée.");
                            } else {
                                resultTextView.setText("Votre candidature est en attente.");
                            }
                        } else {
                            resultTextView.setText("Aucune candidature trouvée pour cette offre.");
                        }
                    } else {
                        Toast.makeText(ViewResultActivity.this, "Erreur lors du chargement du résultat.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
