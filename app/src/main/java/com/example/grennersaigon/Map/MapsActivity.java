//package com.example.grennersaigon;
//
//import androidx.fragment.app.FragmentActivity;
//import android.os.Bundle;
//import android.util.Log;
//
//import com.example.grennersaigon.Model.PinModel;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.example.grennersaigon.databinding.ActivityMapsBinding;
//import com.google.firebase.firestore.CollectionReference;
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QuerySnapshot;
//
//public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
//
//    private GoogleMap mMap;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        com.example.grennersaigon.databinding.ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//
//        // Fetch pins from Firebase and add markers
//        fetchPinsFromFirebase();
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//
//        // Set default location (for example, center on (0, 0))
//        LatLng defaultLocation = new LatLng(0, 0);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation));
//    }
//
//    private void fetchPinsFromFirebase() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        CollectionReference pinsCollection = db.collection("pins");
//
//        pinsCollection.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                QuerySnapshot querySnapshot = task.getResult();
//                if (querySnapshot != null) {
//                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
//                        // Convert the document data to the PinModel
//                        PinModel pin = document.toObject(PinModel.class);
//
//                        // Add a marker for each pin on the map
//                        if (pin != null) {
//                            LatLng pinLatLng = new LatLng(pin.getLatitude(), pin.getLongitude());
//                            mMap.addMarker(new MarkerOptions().position(pinLatLng).title("Pin Marker"));
//                        }
//                    }
//                }
//            } else {
//                Log.w("MapsActivity", "Error getting documents.", task.getException());
//            }
//        });
//    }
//}
package com.example.grennersaigon.Map;

import androidx.fragment.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.grennersaigon.Model.PinModel;
import com.example.grennersaigon.R;
import com.example.grennersaigon.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.example.grennersaigon.databinding.ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fetchPinsFromFirebase();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        
    }

    private void fetchPinsFromFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference pinsCollection = db.collection("pins");

        pinsCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(MapsActivity.this, "Success Fetch", Toast.LENGTH_SHORT).show();
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null) {
                    LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        PinModel pin = document.toObject(PinModel.class);

                        if (pin != null) {
                            GeoPoint geoPoint = document.getGeoPoint("position");
                            if (geoPoint != null) {
                                double lat = geoPoint.getLatitude();
                                double lng = geoPoint.getLongitude();
                                LatLng pinLatLng = new LatLng(lat, lng);
                                mMap.addMarker(new MarkerOptions().position(pinLatLng).title("Test Marker"));
                                boundsBuilder.include(pinLatLng);
                            }
                        }
                    }

                    // Move the camera to include all pins
                    LatLngBounds bounds = boundsBuilder.build();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
                }
            } else {
                Toast.makeText(MapsActivity.this, "Cannot Fetch", Toast.LENGTH_SHORT).show();
                Log.w("MapsActivity", "Error getting documents.", task.getException());
            }
        });
    }
}
