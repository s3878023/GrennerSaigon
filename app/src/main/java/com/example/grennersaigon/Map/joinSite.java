package com.example.grennersaigon.Map;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.example.grennersaigon.Model.PinModel;
import com.example.grennersaigon.R;
import com.example.grennersaigon.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

public class joinSite extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.grennersaigon.databinding.ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fetchPinsFromFirebase();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMarkerClickListener(marker -> {
            String pinName = marker.getTitle();
            String documentId = marker.getSnippet();
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(joinSite.this, PinDetailsActivity.class);
                intent.putExtra("pinName", pinName);
                intent.putExtra("documentId", documentId);
                startActivity(intent);
            }, 2000);

            return false;
        });

    }
    public Bitmap resizeBitmap(String drawableName, int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(drawableName, "drawable", getPackageName()));
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }
    private void fetchPinsFromFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference pinsCollection = db.collection("pins");

        pinsCollection.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(joinSite.this, "Success Fetch", Toast.LENGTH_SHORT).show();
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

                                // Check if the current user is a member of the site
                                boolean isMember = pin.getSiteMembers() != null && pin.getSiteMembers().contains(getCurrentUserId());

                                // Set marker color based on membership
                                if (isMember) {
                                    mMap.addMarker(new MarkerOptions().position(pinLatLng).title(pin.getSiteName())
                                            .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("joined",150,150))).snippet(document.getId()));
                                } else if (pin.getSiteOwner() != null && pin.getSiteOwner().equals(getCurrentUserId())) {
                                    mMap.addMarker(new MarkerOptions().position(pinLatLng).title(pin.getSiteName())
                                            .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("mypin",150,150))).snippet(document.getId()));
                                } else {
                                    mMap.addMarker(new MarkerOptions().position(pinLatLng).title(pin.getSiteName())
                                            .icon(BitmapDescriptorFactory.fromBitmap(resizeBitmap("notjoin",150,150))).snippet(document.getId()));
                                }

                                boundsBuilder.include(pinLatLng);
                            }
                        }
                    }
                    LatLngBounds bounds = boundsBuilder.build();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
                }
            } else {
                Toast.makeText(joinSite.this, "Cannot Fetch", Toast.LENGTH_SHORT).show();
                Log.w("MapsActivity", "Error getting documents.", task.getException());
            }
        });
    }
    private String getCurrentUserId() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        return user != null ? user.getUid() : null;
    }
}
