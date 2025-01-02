package com.example.testforemp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class JobDetailsActivity extends AppCompatActivity {

    private TextView titleTextView, companyTextView, locationTextView, dateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        titleTextView = findViewById(R.id.jobTitleDetail);
        companyTextView = findViewById(R.id.jobCompanyDetail);
        locationTextView = findViewById(R.id.jobLocationDetail);
        dateTextView = findViewById(R.id.jobDateDetail);

        Intent intent = getIntent();
        String title = intent.getStringExtra("jobTitle");
        String company = intent.getStringExtra("jobCompany");
        String location = intent.getStringExtra("jobLocation");
        String datePosted = intent.getStringExtra("jobDatePosted");

        titleTextView.setText(title);
        companyTextView.setText(company);
        locationTextView.setText(location);
        dateTextView.setText(datePosted);
    }
}
