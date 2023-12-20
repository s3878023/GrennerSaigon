package com.example.grennersaigon.Map;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.grennersaigon.Model.PinModel;
import com.example.grennersaigon.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CreateSite extends AppCompatActivity implements OnMapReadyCallback {

    private EditText editTextSiteName, editTextSiteDescription, editTextSiteAddress, editTextSiteReport;
    private Button buttonPinLocation, buttonCheckAddress;
    private GoogleMap googleMap;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private Calendar selectedDateTime = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_site);

        editTextSiteName = findViewById(R.id.editTextSiteName);
        editTextSiteDescription = findViewById(R.id.editTextSiteDescription);
        editTextSiteAddress = findViewById(R.id.editTextSiteAddress);
        editTextSiteReport = findViewById(R.id.editTextSiteReport);
        buttonPinLocation = findViewById(R.id.buttonPinLocation);
        buttonCheckAddress = findViewById(R.id.buttonCheckAddress);

        db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        // Initialize the map
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);

        buttonPinLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle pinning location on the map and pushing information to Firebase
                pinLocationAndPushToFirebase();
            }
        });

        buttonCheckAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkAddressAndShowOnMap();
            }
        });
        Button buttonSelectDateTime = findViewById(R.id.buttonSelectDateTime);
        buttonSelectDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimePicker();
            }
        });
    }
    private void showDateTimePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    // Set selected date
                    selectedDateTime.set(Calendar.YEAR, year);
                    selectedDateTime.set(Calendar.MONTH, monthOfYear);
                    selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(
                            this,
                            (view1, hourOfDay, minute) -> {
                                selectedDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                selectedDateTime.set(Calendar.MINUTE, minute);
                                handleSelectedDateTime();
                            },
                            selectedDateTime.get(Calendar.HOUR_OF_DAY),
                            selectedDateTime.get(Calendar.MINUTE),
                            true
                    );

                    timePickerDialog.show();
                },
                selectedDateTime.get(Calendar.YEAR),
                selectedDateTime.get(Calendar.MONTH),
                selectedDateTime.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
    private void handleSelectedDateTime() {
        Date selectedDate = selectedDateTime.getTime();
        showToast("Selected Date and Time: " + selectedDate.toString());
    }

    private void pinLocationAndPushToFirebase() {
        String address = editTextSiteAddress.getText().toString().trim();
        ArrayList<String> siteMembers = new ArrayList<>();

        if (!address.isEmpty()) {
            LatLng location = geocodeAddress(address);

            if (location != null) {
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions().position(location).title("Pinned Location"));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15.0f));

                Date selectedDate = selectedDateTime.getTime();

                saveSiteInformation(
                        editTextSiteName.getText().toString(),
                        editTextSiteDescription.getText().toString(),
                        address,
                        currentUser.getUid(),
                        new GeoPoint(location.latitude, location.longitude),
                        selectedDate,
                        siteMembers,
                        editTextSiteReport.getText().toString()
                );
            } else {
                showToast("Could not find location for the provided address.");
            }
        } else {
            showToast("Please enter an address.");
        }
    }




    private void checkAddressAndShowOnMap() {
        String address = editTextSiteAddress.getText().toString().trim();

        if (!address.isEmpty()) {
            LatLng location = geocodeAddress(address);

            if (location != null) {
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions().position(location).title("Checked Location"));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15.0f));
            } else {
                showToast("Could not find location for the provided address.");
            }
        } else {
            showToast("Please enter an address.");
        }
    }

    private LatLng geocodeAddress(String address) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if (!addresses.isEmpty()) {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();
                return new LatLng(latitude, longitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void saveSiteInformation(String siteName, String siteDescription, String siteAddress, String siteOwner, GeoPoint position, Date dateTime, ArrayList<String> siteMembers, String siteReport) {
        db.collection("pins").document()
                .set(new PinModel(siteName, siteDescription, siteAddress, siteOwner, position, dateTime, siteMembers, siteReport))
                .addOnSuccessListener(documentReference -> showToast("Site information saved successfully"))
                .addOnFailureListener(e -> showToast("Failed to save site information: " + e.getMessage()));
    }
    private void showToast(String message) {
        Context context = getApplicationContext();
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
    }
}
