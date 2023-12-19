package com.example.grennersaigon.Map;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.grennersaigon.R;

public class PinDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_details); // Set your layout XML here

        // Retrieve data from the intent
        String pinName = getIntent().getStringExtra("pinName");
        String documentId = getIntent().getStringExtra("documentId");

        // Use the data as needed, for example, set them in TextViews
        TextView pinNameTextView = findViewById(R.id.pinNameTextView); // Replace with your TextView ID
        pinNameTextView.setText(pinName);

        TextView documentIdTextView = findViewById(R.id.documentIdTextView); // Replace with your TextView ID
        documentIdTextView.setText(documentId);
    }
}
