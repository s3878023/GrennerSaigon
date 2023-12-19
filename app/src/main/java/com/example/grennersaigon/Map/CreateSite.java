//
//package com.example.grennersaigon.Map;
//
//import android.content.Context;
//import android.location.Address;
//import android.location.Geocoder;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.grennersaigon.Model.PinModel;
//import com.example.grennersaigon.R;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.GeoPoint;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Locale;
//
//public class CreateSite extends AppCompatActivity implements OnMapReadyCallback {
//
//    private EditText editTextSiteName, editTextSiteDescription, editTextSiteAddress;
//    private Button buttonPinLocation, buttonCheckAddress;
//    private GoogleMap googleMap;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_site);
//
//        editTextSiteName = findViewById(R.id.editTextSiteName);
//        editTextSiteDescription = findViewById(R.id.editTextSiteDescription);
//        editTextSiteAddress = findViewById(R.id.editTextSiteAddress);
//        buttonPinLocation = findViewById(R.id.buttonPinLocation);
//        buttonCheckAddress = findViewById(R.id.buttonCheckAddress);
//
//        // Initialize the map
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
//        mapFragment.getMapAsync(this);
//
//        buttonPinLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Handle pinning location on the map and pushing information to Firebase
//                pinLocationAndPushToFirebase();
//            }
//        });
//
//        buttonCheckAddress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Handle checking the input address and showing it on the map
//                checkAddressAndShowOnMap();
//            }
//        });
//    }
//
//    private void pinLocationAndPushToFirebase() {
//        String address = editTextSiteAddress.getText().toString().trim();
//
//        if (!address.isEmpty()) {
//            // Geocode the address to get coordinates
//            LatLng location = geocodeAddress(address);
//
//            if (location != null) {
//                // Clear existing markers
//                googleMap.clear();
//
//                // Add a marker on the map
//                googleMap.addMarker(new MarkerOptions().position(location).title("Pinned Location"));
//
//                // Move the camera to the pinned location
//                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15.0f));
//
//                // Save the site information to Firestore
//                saveSiteInformation(
//                        editTextSiteName.getText().toString(),
//                        editTextSiteDescription.getText().toString(),
//                        address,
//                        new GeoPoint(location.latitude, location.longitude)
//                );
//            } else {
//                showToast("Could not find location for the provided address.");
//            }
//        } else {
//            showToast("Please enter an address.");
//        }
//    }
//
//    private void checkAddressAndShowOnMap() {
//        String address = editTextSiteAddress.getText().toString().trim();
//
//        if (!address.isEmpty()) {
//            // Geocode the address to get coordinates
//            LatLng location = geocodeAddress(address);
//
//            if (location != null) {
//                // Clear existing markers
//                googleMap.clear();
//
//                // Add a marker on the map
//                googleMap.addMarker(new MarkerOptions().position(location).title("Checked Location"));
//
//                // Move the camera to the checked location
//                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15.0f));
//            } else {
//                showToast("Could not find location for the provided address.");
//            }
//        } else {
//            showToast("Please enter an address.");
//        }
//    }
//
//    private LatLng geocodeAddress(String address) {
//        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//        try {
//            List<Address> addresses = geocoder.getFromLocationName(address, 1);
//            if (!addresses.isEmpty()) {
//                double latitude = addresses.get(0).getLatitude();
//                double longitude = addresses.get(0).getLongitude();
//                return new LatLng(latitude, longitude);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    private void saveSiteInformation(String siteName, String siteDescription, String siteAddress, GeoPoint position) {
//        // Save the site information to Firestore
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        // Create a PinModel object with the site information
//        // (replace "pins" with your desired Firestore collection name)
//        // Note: Make sure to add proper error handling in production code
//        db.collection("pins").document()
//                .set(new PinModel(siteName, siteDescription, siteAddress, position))
//                .addOnSuccessListener(documentReference -> showToast("Site information saved successfully"))
//                .addOnFailureListener(e -> showToast("Failed to save site information: " + e.getMessage()));
//    }
//
//    private void showToast(String message) {
//        Context context = getApplicationContext();
//        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onMapReady(GoogleMap map) {
//        googleMap = map;
//        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        googleMap.getUiSettings().setZoomControlsEnabled(true);
//        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
//    }
//}
package com.example.grennersaigon.Map;

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
import java.util.List;
import java.util.Locale;

public class CreateSite extends AppCompatActivity implements OnMapReadyCallback {

    private EditText editTextSiteName, editTextSiteDescription, editTextSiteAddress;
    private Button buttonPinLocation, buttonCheckAddress;
    private GoogleMap googleMap;
    private FirebaseFirestore db;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_site);

        editTextSiteName = findViewById(R.id.editTextSiteName);
        editTextSiteDescription = findViewById(R.id.editTextSiteDescription);
        editTextSiteAddress = findViewById(R.id.editTextSiteAddress);
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
    }

    private void pinLocationAndPushToFirebase() {
        String address = editTextSiteAddress.getText().toString().trim();

        if (!address.isEmpty()) {
            LatLng location = geocodeAddress(address);

            if (location != null) {
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions().position(location).title("Pinned Location"));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15.0f));
                saveSiteInformation(
                        editTextSiteName.getText().toString(),
                        editTextSiteDescription.getText().toString(),
                        address,
                        currentUser.getUid(), // User ID
                        new GeoPoint(location.latitude, location.longitude)
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
            // Geocode the address to get coordinates
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

    private void saveSiteInformation(String siteName, String siteDescription, String siteAddress, String siteOwner, GeoPoint position) {
        // Save the site information to Firestore
        db.collection("pins").document()
                .set(new PinModel(siteName, siteDescription, siteAddress, siteOwner, position))
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
